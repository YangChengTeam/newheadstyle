package com.feiyou.headstyle.ui.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bytedance.sdk.openadsdk.FilterWord;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdDislike;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.HeadInfo;
import com.feiyou.headstyle.ui.custom.DislikeDialog;
import com.feiyou.headstyle.ui.custom.RoundedCornersTransformation;
import com.orhanobut.logger.Logger;

import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class HeadMultipleAdapter extends BaseMultiItemQuickAdapter<HeadInfo, BaseViewHolder> {

    private Map<BaseViewHolder, TTAppDownloadListener> mTTAppDownloadListenerMap = new WeakHashMap<>();

    private List<HeadInfo> headData = null;

    private int tempWidth;

    private int showType;

    public HeadMultipleAdapter(List<HeadInfo> data, int type) {
        super(data);
        this.headData = data;
        addItemType(HeadInfo.HEAD_IMG, R.layout.head_info_item);
        addItemType(HeadInfo.HEAD_AD, R.layout.head_info_ad_item);
        tempWidth = (ScreenUtils.getScreenWidth() - SizeUtils.dp2px(24)) / 3;
        this.showType = type;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, HeadInfo item) {

        if (helper.getItemViewType() == HeadInfo.HEAD_AD) {

            RelativeLayout adItemLayout = helper.getView(R.id.layout_ad_item);
            RelativeLayout.LayoutParams itemParams = new RelativeLayout.LayoutParams(ScreenUtils.getScreenWidth() - SizeUtils.dp2px(showType == 1 ? 24 : 12), (ScreenUtils.getScreenWidth() - SizeUtils.dp2px(showType == 1 ? 24 : 12)) / 3 + SizeUtils.dp2px(12));
            adItemLayout.setLayoutParams(itemParams);

            FrameLayout tempView = helper.getView(R.id.iv_listitem_express);
            if (item.getTtNativeExpressAd() != null) {
                bindData(tempView, helper, item.getTtNativeExpressAd());
                //tempView.removeAllViews();
                View adView = item.getTtNativeExpressAd().getExpressAdView();
                if (adView.getParent() != null) {
                    ((ViewGroup) adView.getParent()).removeView(adView);
                }

                tempView.removeAllViews();
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.addRule(RelativeLayout.CENTER_IN_PARENT);
                adView.setLayoutParams(params);
                tempView.addView(adView);
            }

        } else {
            LinearLayout itemLayout = helper.itemView.findViewById(R.id.head_item_layout);
            LinearLayout.LayoutParams itemParams = new LinearLayout.LayoutParams(tempWidth, tempWidth);
            itemLayout.setLayoutParams(itemParams);

            RequestOptions options = new RequestOptions().skipMemoryCache(true);
            options.placeholder(R.mipmap.image_def).error(R.mipmap.image_def);
            options.override(tempWidth - 8, tempWidth - 8);
            //options.transform(new GlideRoundTransform(mContext, 5));
            options.transform(new RoundedCornersTransformation(SizeUtils.dp2px(5), 0));
            Glide.with(mContext).load(item.getImgurl()).apply(options).into((ImageView) helper.getView(R.id.iv_head_info));
        }
    }

    /**
     * 设置广告的不喜欢，注意：强烈建议设置该逻辑，如果不设置dislike处理逻辑，则模板广告中的 dislike区域不响应dislike事件。
     *
     * @param ad
     * @param customStyle 是否自定义样式，true:样式自定义
     */
    private void bindDislike(final TTNativeExpressAd ad, boolean customStyle) {
        if (customStyle) {
            //使用自定义样式
            List<FilterWord> words = ad.getFilterWords();
            if (words == null || words.isEmpty()) {
                return;
            }

            final DislikeDialog dislikeDialog = new DislikeDialog(mContext, words);
            dislikeDialog.setOnDislikeItemClick(new DislikeDialog.OnDislikeItemClick() {
                @Override
                public void onItemClick(FilterWord filterWord) {
                    //屏蔽广告
                    //TToast.show(mContext, "点击 " + filterWord.getName());
                    //用户选择不喜欢原因后，移除广告展示
                    headData.remove(ad);
                    notifyDataSetChanged();
                }
            });
            ad.setDislikeDialog(dislikeDialog);
            return;
        }
        //使用默认模板中默认dislike弹出样式
        ad.setDislikeCallback((Activity) mContext, new TTAdDislike.DislikeInteractionCallback() {
            @Override
            public void onSelected(int position, String value) {
                //TToast.show(mContext, "点击 " + value);
                //用户选择不喜欢原因后，移除广告展示
                headData.remove(ad);
                notifyDataSetChanged();
            }

            @Override
            public void onCancel() {
                //TToast.show(mContext, "点击取消 ");
            }
        });
    }

    private void bindData(View convertView, final BaseViewHolder adViewHolder, TTNativeExpressAd ad) {
        //设置dislike弹窗，这里展示自定义的dialog
        bindDislike(ad, true);
        switch (ad.getInteractionType()) {
            case TTAdConstant.INTERACTION_TYPE_DOWNLOAD:
                bindDownloadListener(adViewHolder, ad);
                break;
        }
    }


    private void bindDownloadListener(final BaseViewHolder adViewHolder, TTNativeExpressAd ad) {
        TTAppDownloadListener downloadListener = new TTAppDownloadListener() {
            private boolean mHasShowDownloadActive = false;

            @Override
            public void onIdle() {
                if (!isValid()) {
                    return;
                }
                //TToast.show(mContext, "点击广告开始下载");
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onDownloadActive(long totalBytes, long currBytes, String fileName, String appName) {
                if (!isValid()) {
                    return;
                }
                if (!mHasShowDownloadActive) {
                    mHasShowDownloadActive = true;
                    //TToast.show(mContext, appName + " 下载中，点击暂停", Toast.LENGTH_LONG);
                }
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onDownloadPaused(long totalBytes, long currBytes, String fileName, String appName) {
                if (!isValid()) {
                    return;
                }
                //TToast.show(mContext, appName + " 下载暂停", Toast.LENGTH_LONG);

            }

            @Override
            public void onDownloadFailed(long totalBytes, long currBytes, String fileName, String appName) {
                if (!isValid()) {
                    return;
                }
                //TToast.show(mContext, appName + " 下载失败，重新下载", Toast.LENGTH_LONG);
            }

            @Override
            public void onInstalled(String fileName, String appName) {
                if (!isValid()) {
                    return;
                }
                //TToast.show(mContext, appName + " 安装完成，点击打开", Toast.LENGTH_LONG);
            }

            @Override
            public void onDownloadFinished(long totalBytes, String fileName, String appName) {
                if (!isValid()) {
                    return;
                }
                //TToast.show(mContext, appName + " 下载成功，点击安装", Toast.LENGTH_LONG);

            }

            @SuppressWarnings("BooleanMethodIsAlwaysInverted")
            private boolean isValid() {
                return mTTAppDownloadListenerMap.get(adViewHolder) == this;
            }
        };
        //一个ViewHolder对应一个downloadListener, isValid判断当前ViewHolder绑定的listener是不是自己
        ad.setDownloadListener(downloadListener); // 注册下载监听器
        mTTAppDownloadListenerMap.put(adViewHolder, downloadListener);
    }

}