package com.feiyou.headstyle.ui.custom;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTImage;
import com.bytedance.sdk.openadsdk.TTNativeAd;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.utils.TTAdManagerHolder;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

public class NewSignSuccessDialog extends Dialog implements View.OnClickListener {

    private Context mContext;

    private FrameLayout mBannerContainer;

    private TextView mTitleTv;

    private TextView mCloseTv;

    private TextView mProfitTv;

    private TTAdNative mTTAdNative;

    private Button mPlayGameBtn;

    private Button mSeeVideoBtn;

    public NewSignListener newSignListener;

    public void setNewSignListener(NewSignListener newSignListener) {
        this.newSignListener = newSignListener;
    }

    public interface NewSignListener {
        void newSignSeeVideo();

        void newSignPlayGame();
    }

    public NewSignSuccessDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public NewSignSuccessDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_sign_success_view);
        setCanceledOnTouchOutside(false);
        initView();
    }

    public void setSignInfo(int addGold) {
        mProfitTv.setText("+" + addGold);
    }

    private void initView() {
        //step2:创建TTAdNative对象，createAdNative(Context context) banner广告context需要传入Activity对象
        mTTAdNative = TTAdManagerHolder.get().createAdNative(mContext);
        mBannerContainer = findViewById(R.id.layout_ad);
        mPlayGameBtn = findViewById(R.id.btn_play_game);
        mSeeVideoBtn = findViewById(R.id.btn_see_video);

        mCloseTv = findViewById(R.id.tv_close);
        mCloseTv.setOnClickListener(this);
        mTitleTv = findViewById(R.id.tv_title);
        mProfitTv = findViewById(R.id.tv_add_gold_num);
        mPlayGameBtn.setOnClickListener(this);
        mSeeVideoBtn.setOnClickListener(this);

        setCanceledOnTouchOutside(true);
        loadBannerAd("920819188");
    }

    private void loadBannerAd(String codeId) {
        //step4:创建广告请求参数AdSlot,具体参数含义参考文档
        final AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(codeId)
                .setSupportDeepLink(true)
                .setImageAcceptedSize(600, 300)
                .setNativeAdType(AdSlot.TYPE_BANNER) //请求原生广告时候，请务必调用该方法，设置参数为TYPE_BANNER或TYPE_INTERACTION_AD
                .setAdCount(1)
                .build();
        //step5:请求广告，对请求回调的广告作渲染处理
        mTTAdNative.loadNativeAd(adSlot, new TTAdNative.NativeAdListener() {
            @Override
            public void onError(int code, String message) {
                //TToast.show(NativeBannerActivity.this, "load error : " + code + ", " + message);
                Logger.i("load error : " + code + ", " + message);
            }

            @Override
            public void onNativeAdLoad(List<TTNativeAd> ads) {
                if (ads.get(0) == null) {
                    return;
                }
                View bannerView = LayoutInflater.from(mContext).inflate(R.layout.native_ad, mBannerContainer, false);
                if (bannerView == null) {
                    return;
                }

                mBannerContainer.removeAllViews();
                mBannerContainer.addView(bannerView);

                //绑定原生广告的数据
                setAdData(bannerView, ads.get(0));
            }
        });
    }

    @SuppressWarnings("RedundantCast")
    private void setAdData(View nativeView, TTNativeAd nativeAd) {

        if (nativeAd.getImageList() != null && !nativeAd.getImageList().isEmpty()) {
            TTImage image = nativeAd.getImageList().get(0);
            if (image != null && image.isValid()) {
                ImageView im = nativeView.findViewById(R.id.iv_native_image);
                Glide.with(mContext).load(image.getImageUrl()).into(im);
            }
        }

        //可以被点击的view, 也可以把nativeView放进来意味整个广告区域可被点击
        List<View> clickViewList = new ArrayList<>();
        clickViewList.add(nativeView);

        //触发创意广告的view（点击下载或拨打电话）
        List<View> creativeViewList = new ArrayList<>();
        //如果需要点击图文区域也能进行下载或者拨打电话动作，请将图文区域的view传入
        //creativeViewList.add(nativeView);
        //creativeViewList.add(mCreativeButton);

        //重要! 这个涉及到广告计费，必须正确调用。convertView必须使用ViewGroup。
        nativeAd.registerViewForInteraction((ViewGroup) nativeView, clickViewList, creativeViewList, null, new TTNativeAd.AdInteractionListener() {
            @Override
            public void onAdClicked(View view, TTNativeAd ad) {
                if (ad != null) {
                    //TToast.show(mContext, "广告" + ad.getTitle() + "被点击");
                }
            }

            @Override
            public void onAdCreativeClick(View view, TTNativeAd ad) {
                if (ad != null) {
                    //TToast.show(mContext, "广告" + ad.getTitle() + "被创意按钮被点击");
                }
            }

            @Override
            public void onAdShow(TTNativeAd ad) {
                if (ad != null) {
                    //TToast.show(mContext, "广告" + ad.getTitle() + "展示");
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_play_game:
                this.newSignListener.newSignPlayGame();
                dismiss();
                break;
            case R.id.btn_see_video:
                this.newSignListener.newSignSeeVideo();
                dismiss();
                break;
            case R.id.tv_close:
                this.dismiss();
                break;
            default:
                break;
        }
    }
}