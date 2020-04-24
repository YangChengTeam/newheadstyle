package com.feiyou.headstyle.ui.custom;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdDislike;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTBannerAd;
import com.bytedance.sdk.openadsdk.TTImage;
import com.bytedance.sdk.openadsdk.TTNativeAd;
import com.bytedance.sdk.openadsdk.TTRewardVideoAd;
import com.feiyou.headstyle.App;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.utils.RandomUtils;
import com.feiyou.headstyle.utils.TTAdManagerHolder;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class GameProfitDialog extends Dialog implements View.OnClickListener {

    private Context mContext;

    private FrameLayout mBannerContainer;

    private ImageView moreGoldGif;

    private TextView mTitleTv;

    private TextView mCloseTv;

    private TextView mProfitTv;

    private TTAdNative mTTAdNative;

    private TTRewardVideoAd mttRewardVideoAd;

    private boolean mHasShowDownloadActive = false;

    public GameSeeVideoListener gameSeeVideoListener;

    public void setGameSeeVideoListener(GameSeeVideoListener gameSeeVideoListener) {
        this.gameSeeVideoListener = gameSeeVideoListener;
    }

    public interface GameSeeVideoListener {
        void startVideo();

        void endVideo();

        void closeProfit();
    }

    public GameProfitDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public GameProfitDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_profit_view);
        setCanceledOnTouchOutside(false);
        initView();
    }


    public void setTitleValue(String title, int num) {
        mTitleTv.setText(title);
        mProfitTv.setText("+" + num);
    }

    private void initView() {
        //step2:创建TTAdNative对象，createAdNative(Context context) banner广告context需要传入Activity对象
        mTTAdNative = TTAdManagerHolder.get().createAdNative(mContext);
        mBannerContainer = findViewById(R.id.layout_ad);
        moreGoldGif = findViewById(R.id.more_gold_gif);
        mCloseTv = findViewById(R.id.tv_close);
        mCloseTv.setOnClickListener(this);
        mTitleTv = findViewById(R.id.tv_title);
        mProfitTv = findViewById(R.id.tv_add_gold_num);

        Glide.with(mContext).load(R.drawable.shouyi_btn).into(moreGoldGif);
        moreGoldGif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mttRewardVideoAd != null) {
                    //step6:在获取到广告后展示
                    mttRewardVideoAd.showRewardVideoAd((Activity) mContext);
                    //mttRewardVideoAd = null;
                    gameSeeVideoListener.startVideo();
                }
                dismiss();
            }
        });

        setCanceledOnTouchOutside(true);
        loadAd("920819888", TTAdConstant.VERTICAL, 1);
    }

    public void updateSignAdView(View view) {
        if (view != null) {
            if (view.getParent() != null) {
                ((ViewGroup) view).removeView(view);
            }
            mBannerContainer.removeAllViews();

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            view.setLayoutParams(params);
            params.gravity = Gravity.CENTER;
            mBannerContainer.addView(view);
        }
    }

    private void loadAd(String codeId, int orientation, int goldNum) {
        //step4:创建广告请求参数AdSlot,具体参数含义参考文档
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(codeId)
                .setSupportDeepLink(true)
                .setImageAcceptedSize(1080, 1920)
                .setRewardName("金币") //奖励的名称
                .setRewardAmount(goldNum)  //奖励的数量
                .setUserID("10000" + RandomUtils.nextInt())//用户id,必传参数
                .setMediaExtra("media_extra") //附加参数，可选
                .setOrientation(orientation) //必填参数，期望视频的播放方向：TTAdConstant.HORIZONTAL 或 TTAdConstant.VERTICAL
                .build();

        //step5:请求广告
        mTTAdNative.loadRewardVideoAd(adSlot, new TTAdNative.RewardVideoAdListener() {
            @Override
            public void onError(int code, String message) {
                Logger.i("code--->" + code + "---" + message);
            }

            //视频广告加载后，视频资源缓存到本地的回调，在此回调后，播放本地视频，流畅不阻塞。
            @Override
            public void onRewardVideoCached() {
                Logger.i("rewardVideoAd video cached");
            }

            //视频广告的素材加载完毕，比如视频url等，在此回调后，可以播放在线视频，网络不好可能出现加载缓冲，影响体验。
            @Override
            public void onRewardVideoAdLoad(TTRewardVideoAd ad) {
                Logger.i("rewardVideoAd loaded");

                mttRewardVideoAd = ad;
//                mttRewardVideoAd.setShowDownLoadBar(false);
                mttRewardVideoAd.setRewardAdInteractionListener(new TTRewardVideoAd.RewardAdInteractionListener() {

                    @Override
                    public void onAdShow() {
                        Logger.i("rewardVideoAd show");
                    }

                    @Override
                    public void onAdVideoBarClick() {
                        Logger.i("rewardVideoAd bar click");
                    }

                    @Override
                    public void onAdClose() {
                        Logger.i("rewardVideoAd close");
                    }

                    //视频播放完成回调
                    @Override
                    public void onVideoComplete() {
                        Logger.i("rewardVideoAd complete");
                    }

                    @Override
                    public void onVideoError() {
                        Logger.i("rewardVideoAd error");
                    }

                    //视频播放完成后，奖励验证回调，rewardVerify：是否有效，rewardAmount：奖励梳理，rewardName：奖励名称
                    @Override
                    public void onRewardVerify(boolean rewardVerify, int rewardAmount, String rewardName) {
//                        ToastUtils.showLong("verify:" + rewardVerify + " amount:" + rewardAmount +
//                                " name:" + rewardName);
                        if (rewardVerify) {
                            //ToastUtils.showLong("正常播放完成，奖励有效");
                            Logger.i("game see video finish --->");
                            gameSeeVideoListener.endVideo();
                        }
                    }

                    @Override
                    public void onSkippedVideo() {
                        Logger.i("rewardVideoAd has onSkippedVideo");
                    }
                });

                mttRewardVideoAd.setDownloadListener(new TTAppDownloadListener() {
                    @Override
                    public void onIdle() {
                        mHasShowDownloadActive = false;
                    }

                    @Override
                    public void onDownloadActive(long totalBytes, long currBytes, String fileName, String appName) {
                        if (!mHasShowDownloadActive) {
                            mHasShowDownloadActive = true;
                            ToastUtils.showLong("下载中，点击下载区域暂停", Toast.LENGTH_LONG);
                        }
                    }

                    @Override
                    public void onDownloadPaused(long totalBytes, long currBytes, String fileName, String appName) {
                        ToastUtils.showLong("下载暂停，点击下载区域继续", Toast.LENGTH_LONG);
                    }

                    @Override
                    public void onDownloadFailed(long totalBytes, long currBytes, String fileName, String appName) {
                        ToastUtils.showLong("下载失败，点击下载区域重新下载", Toast.LENGTH_LONG);
                    }

                    @Override
                    public void onDownloadFinished(long totalBytes, String fileName, String appName) {
                        ToastUtils.showLong("下载完成，点击下载区域重新下载", Toast.LENGTH_LONG);
                    }

                    @Override
                    public void onInstalled(String fileName, String appName) {
                        ToastUtils.showLong("安装完成，点击下载区域打开", Toast.LENGTH_LONG);
                    }
                });
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_close:
                this.gameSeeVideoListener.closeProfit();
                break;
            default:
                break;
        }
    }
}