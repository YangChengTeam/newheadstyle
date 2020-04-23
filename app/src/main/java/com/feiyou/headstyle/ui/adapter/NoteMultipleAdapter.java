package com.feiyou.headstyle.ui.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bytedance.sdk.openadsdk.FilterWord;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdDislike;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feiyou.headstyle.App;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.HeadInfo;
import com.feiyou.headstyle.bean.NoteInfo;
import com.feiyou.headstyle.ui.activity.ShowImageListActivity;
import com.feiyou.headstyle.ui.custom.DislikeDialog;
import com.feiyou.headstyle.ui.custom.RoundedCornersTransformation;
import com.feiyou.headstyle.utils.MyTimeUtil;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class NoteMultipleAdapter extends BaseMultiItemQuickAdapter<NoteInfo, BaseViewHolder> {

    private Map<BaseViewHolder, TTAppDownloadListener> mTTAppDownloadListenerMap = new WeakHashMap<>();

    private List<NoteInfo> headData = null;

    private int tempWidth;

    private int showType = 1; //1.普通类别,2.个人操作列表

    public NoteMultipleAdapter(List<NoteInfo> data, int type) {
        super(data);
        this.headData = data;
        addItemType(NoteInfo.NOTE_NORMAL, R.layout.note_item);
        addItemType(NoteInfo.NOTE_AD, R.layout.note_info_ad_item);
        tempWidth = (ScreenUtils.getScreenWidth() - SizeUtils.dp2px(12)) / 3;
        this.showType = type;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, NoteInfo item) {

        if (helper.getItemViewType() == NoteInfo.NOTE_AD) {

            LinearLayout adItemLayout = helper.getView(R.id.layout_ad_item);
            LinearLayout.LayoutParams itemParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            adItemLayout.setLayoutParams(itemParams);

            FrameLayout tempView = null;

            if (helper.getView(R.id.iv_listitem_express).getTag(R.id.iv_listitem_express) != null) {
                Logger.i("note ad item 111---->" + helper.getAdapterPosition());
                tempView = (FrameLayout) helper.getView(R.id.iv_listitem_express).getTag(R.id.iv_listitem_express);
            } else {
                Logger.i("note ad item 222---->" + helper.getAdapterPosition());
                tempView = helper.getView(R.id.iv_listitem_express);
                helper.getView(R.id.iv_listitem_express).setTag(R.id.iv_listitem_express, tempView);

                //TODO
                if (item.getTtNativeExpressAd() != null) {
                    bindData(tempView, helper, item.getTtNativeExpressAd());
                    //tempView.removeAllViews();
                    View adView = item.getTtNativeExpressAd().getExpressAdView();
                    if (adView.getParent() != null) {
                        ((ViewGroup) adView.getParent()).removeView(adView);
                    }
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.addRule(RelativeLayout.CENTER_IN_PARENT);
                    adView.setLayoutParams(params);
                    tempView.addView(adView);
                }
            }
        } else {
            Date currentDate = TimeUtils.millis2Date(item.getCommentTime() != null ? item.getCommentTime() * 1000 : 0);
            String tempDateStr = MyTimeUtil.isOutMouth(currentDate) ? TimeUtils.millis2String(item.getCommentTime() != null ? item.getCommentTime() * 1000 : 0) : MyTimeUtil.getTimeFormatText(currentDate);

            String nickName = "火星用户";
            if (!StringUtils.isEmpty(item.getNickname())) {
                nickName = item.getNickname().replace("\r", "").replace("\n", "");
            }

            helper.setText(R.id.tv_nick_name, nickName)
                    .setText(R.id.tv_topic_name, item.getName())
                    .setText(R.id.tv_note_date, tempDateStr)
                    .setText(R.id.tv_message_count, item.getCommentNum() < 0 ? "0" : item.getCommentNum() + "")
                    .setText(R.id.tv_zan_count, item.getZanNum() < 0 ? "0" : item.getZanNum() + "");


            TextView contentTv = helper.getView(R.id.tv_note_content);
            contentTv.setText(Html.fromHtml(StringUtils.isEmpty(item.getContent()) ? "" : item.getContent().replace("\n", "<br>")));

            TextView isZanTv = helper.itemView.findViewById(R.id.tv_zan_count);
            Drawable isZan = ContextCompat.getDrawable(mContext, R.mipmap.is_zan);
            Drawable notZan = ContextCompat.getDrawable(mContext, R.mipmap.note_zan);
            if (item.getIsZan() == 0) {
                isZanTv.setCompoundDrawablesWithIntrinsicBounds(notZan, null, null, null);
            } else {
                isZanTv.setCompoundDrawablesWithIntrinsicBounds(isZan, null, null, null);
            }

            if (showType == 1) {
                helper.setVisible(R.id.layout_follow, true);
                helper.setVisible(R.id.layout_operation, false);
                helper.addOnClickListener(R.id.layout_follow);

                //自己对自己发的贴，隐藏相关操作按钮
                if (App.getApp().getmUserInfo() != null && App.getApp().getmUserInfo().getId().equals(item.getUserId())) {
                    helper.setVisible(R.id.layout_follow, false).setVisible(R.id.layout_operation, false);
                } else {
                    helper.setVisible(R.id.layout_follow, true).setVisible(R.id.layout_operation, false);
                }
                helper.addOnClickListener(R.id.layout_operation);
            } else {
                //自己对自己发的贴，隐藏相关操作按钮
                if (App.getApp().getmUserInfo() != null && App.getApp().getmUserInfo().getId().equals(item.getUserId())) {
                    helper.setVisible(R.id.layout_follow, false).setVisible(R.id.layout_operation, true);
                } else {
                    helper.setVisible(R.id.layout_follow, false).setVisible(R.id.layout_operation, false);
                }
                helper.addOnClickListener(R.id.layout_operation);
            }

            helper.setBackgroundRes(R.id.layout_follow, item.getIsGuan() == 0 ? R.drawable.into_bg : R.drawable.is_follow_bg);
            helper.setTextColor(R.id.tv_follow_txt, ContextCompat.getColor(mContext, item.getIsGuan() == 0 ? R.color.tab_select_color : R.color.is_follow_color));
            helper.setText(R.id.tv_follow_txt, item.getIsGuan() == 0 ? "+关注" : "已关注");

            helper.addOnClickListener(R.id.layout_item_zan).addOnClickListener(R.id.iv_user_head).addOnClickListener(R.id.layout_note_share);

            RequestOptions options = new RequestOptions();
            options.override(SizeUtils.dp2px(36), SizeUtils.dp2px(36));
            options.placeholder(R.mipmap.head_def);
            options.transform(new RoundedCornersTransformation(SizeUtils.dp2px(18), 0));
            Glide.with(mContext).load(item.getUserimg()).apply(options).into((ImageView) helper.itemView.findViewById(R.id.iv_user_head));

            List<HeadInfo> headInfos = new ArrayList<>();
            String[] tempImg = item.getImageArr();
            final ArrayList<String> imageUrls = new ArrayList<>();
            for (int i = 0; i < tempImg.length; i++) {
                HeadInfo headInfo = new HeadInfo();
                headInfo.setImgurl(tempImg[i]);
                headInfos.add(headInfo);
                imageUrls.add(tempImg[i]);
            }

            CommunityItemAdapter communityItemAdapter = new CommunityItemAdapter(mContext, headInfos, showType);
            RecyclerView noteImageListView = helper.itemView.findViewById(R.id.note_img_list);
            noteImageListView.setLayoutManager(new GridLayoutManager(mContext, 3));
            noteImageListView.setAdapter(communityItemAdapter);

            communityItemAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    Intent intent = new Intent(mContext, ShowImageListActivity.class);
                    intent.putExtra("image_index", position);
                    intent.putStringArrayListExtra("image_list", imageUrls);
                    mContext.startActivity(intent);
                }
            });

            if (item.getUserId().equals("1")) {
                helper.setVisible(R.id.iv_system_user, true);
            } else {
                helper.setVisible(R.id.iv_system_user, false);
            }
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