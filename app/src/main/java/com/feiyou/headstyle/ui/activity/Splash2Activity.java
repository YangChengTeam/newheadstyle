package com.feiyou.headstyle.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.MainThread;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.RomUtils;
import com.blankj.utilcode.util.StringUtils;
import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTSplashAd;
import com.feiyou.headstyle.App;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.ResultInfo;
import com.feiyou.headstyle.bean.VersionInfoRet;
import com.feiyou.headstyle.common.Constants;
import com.feiyou.headstyle.presenter.VersionPresenterImp;
import com.feiyou.headstyle.utils.TTAdManagerHolder;
import com.feiyou.headstyle.utils.WeakHandler;
import com.feiyou.headstyle.view.VersionView;
import com.orhanobut.logger.Logger;
import com.qq.e.ads.splash.SplashAD;
import com.qq.e.ads.splash.SplashADListener;
import com.qq.e.comm.util.AdError;

public class Splash2Activity extends Activity implements SplashADListener, VersionView, WeakHandler.IHandler {

    private ViewGroup container;

    private TextView skipView;

    private ImageView splashHolder;

    private SplashAD splashAD;

    private static final String SKIP_TEXT = "点击跳过 %d";

    /**
     * 为防止无广告时造成视觉上类似于"闪退"的情况，设定无广告时页面跳转根据需要延迟一定时间，demo
     * 给出的延时逻辑是从拉取广告开始算开屏最少持续多久，仅供参考，开发者可自定义延时逻辑，如果开发者采用demo
     * 中给出的延时逻辑，也建议开发者考虑自定义minSplashTimeWhenNoAD的值（单位ms）
     **/
    private int minSplashTimeWhenNoAD = 2000;
    /**
     * 记录拉取广告的时间
     */
    private long fetchSplashADTime = 0;

    private Handler handler = new Handler(Looper.getMainLooper());

    private boolean isHuaWeiClose;

    private TTAdNative mTTAdNative;

    private static final int AD_TIME_OUT = 3000;

    //开屏广告是否已经加载
    private boolean mHasLoaded;

    //开屏广告加载发生超时但是SDK没有及时回调结果的时候，做的一层保护。
    private final WeakHandler mHandler = new WeakHandler(this);

    private static final int MSG_GO_MAIN = 1;

    //是否强制跳转到主页面
    private boolean mForceGoMain;

    private int startType = 1;

    VersionPresenterImp versionPresenterImp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash2);

        container = findViewById(R.id.splash_container);
        skipView = findViewById(R.id.skip_view);
        splashHolder = findViewById(R.id.splash_holder);

        versionPresenterImp = new VersionPresenterImp(this, this);
        //请求接口，判断开屏广告使用的类型
        versionPresenterImp.getVersionInfo(App.appChannel);
    }

    public void loadAdInfo() {
        if (startType == 1) {
            startTencentAd();
            skipView.setVisibility(View.VISIBLE);
        } else if (startType == 2) {
            //mHandler.sendEmptyMessageDelayed(MSG_GO_MAIN, AD_TIME_OUT);
            startTouTiaoAd();
            skipView.setVisibility(View.GONE);
        } else {
            startTencentAd();
            skipView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 加载腾讯广告
     */
    public void startTencentAd() {
        //此处不判断权限，直接调用
        fetchSplashAD(this, container, skipView, Constants.APP_ID, Constants.OPEN_ID, this, 0);
    }

    private void fetchSplashAD(Activity activity, ViewGroup adContainer, View skipContainer,
                               String appId, String posId, SplashADListener adListener, int fetchDelay) {
        fetchSplashADTime = System.currentTimeMillis();
        splashAD = new SplashAD(activity, skipContainer, appId, posId, adListener, fetchDelay);
        splashAD.fetchAndShowIn(adContainer);
    }

    public void startTouTiaoAd() {
        loadTouTiaoAd();
    }

    /**
     * 加载头条的开屏广告
     */
    public void loadTouTiaoAd() {
        mTTAdNative = TTAdManagerHolder.get().createAdNative(this);
        //step3:创建开屏广告请求参数AdSlot,具体参数含义参考文档
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId("820819798")
                .setSupportDeepLink(true)
                .setImageAcceptedSize(1080, 1920)
                .build();
        //step4:请求广告，调用开屏广告异步请求接口，对请求回调的广告作渲染处理
        mTTAdNative.loadSplashAd(adSlot, new TTAdNative.SplashAdListener() {
            @Override
            @MainThread
            public void onError(int code, String message) {
                mHasLoaded = true;
                //showToast(message);
                Logger.i("load error --->" + message);
                goToMainActivity();
            }

            @Override
            @MainThread
            public void onTimeout() {
                mHasLoaded = true;
                //showToast("开屏广告加载超时");
                Logger.i("开屏广告加载超时");
                goToMainActivity();
            }

            @Override
            @MainThread
            public void onSplashAdLoad(TTSplashAd ad) {
                Logger.i("开屏广告加载完成" + ad.hashCode());
                mHasLoaded = true;
                mHandler.removeCallbacksAndMessages(null);
                if (ad == null) {
                    return;
                }
                //获取SplashView
                View view = ad.getSplashView();
                Logger.i("get splash view --->" + view.hashCode());
                if (view != null) {
                    container.removeAllViews();
                    //把SplashView 添加到ViewGroup中,注意开屏广告view：width >=70%屏幕宽；height >=50%屏幕高
                    container.addView(view);
                    //设置不开启开屏广告倒计时功能以及不显示跳过按钮,如果这么设置，您需要自定义倒计时逻辑
                    //ad.setNotAllowSdkCountdown();
                } else {
                    goToMainActivity();
                }

                //设置SplashView的交互监听器
                ad.setSplashInteractionListener(new TTSplashAd.AdInteractionListener() {
                    @Override
                    public void onAdClicked(View view, int type) {
                        //showToast("开屏广告点击");
                    }

                    @Override
                    public void onAdShow(View view, int type) {
                        //showToast("开屏广告展示");
                        //skipView.setText(String.format(SKIP_TEXT, 5));
                    }

                    @Override
                    public void onAdSkip() {
                        //showToast("开屏广告跳过");
                        goToMainActivity();
                    }

                    @Override
                    public void onAdTimeOver() {
                        //showToast("开屏广告倒计时结束");
                        goToMainActivity();
                    }
                });
                if (ad.getInteractionType() == TTAdConstant.INTERACTION_TYPE_DOWNLOAD) {
                    ad.setDownloadListener(new TTAppDownloadListener() {
                        boolean hasShow = false;

                        @Override
                        public void onIdle() {

                        }

                        @Override
                        public void onDownloadActive(long totalBytes, long currBytes, String fileName, String appName) {
                            if (!hasShow) {
                                //showToast("下载中...");
                                hasShow = true;
                            }
                        }

                        @Override
                        public void onDownloadPaused(long totalBytes, long currBytes, String fileName, String appName) {
                            //showToast("下载暂停...");

                        }

                        @Override
                        public void onDownloadFailed(long totalBytes, long currBytes, String fileName, String appName) {
                            //showToast("下载失败...");

                        }

                        @Override
                        public void onDownloadFinished(long totalBytes, String fileName, String appName) {

                        }

                        @Override
                        public void onInstalled(String fileName, String appName) {

                        }
                    });
                }
            }
        }, AD_TIME_OUT);
    }

    @Override
    public void onADPresent() {
        Logger.i("SplashADPresent");
        splashHolder.setVisibility(View.INVISIBLE); // 广告展示后一定要把预设的开屏图片隐藏起来
    }

    @Override
    public void onADClicked() {
        Logger.i("SplashADClicked");
    }

    /**
     * 倒计时回调，返回广告还将被展示的剩余时间。
     * 通过这个接口，开发者可以自行决定是否显示倒计时提示，或者还剩几秒的时候显示倒计时
     *
     * @param millisUntilFinished 剩余毫秒数
     */
    @Override
    public void onADTick(long millisUntilFinished) {
        Logger.i("SplashADTick " + millisUntilFinished + "ms");
        if (skipView != null) {
            skipView.setText(String.format(SKIP_TEXT, Math.round(millisUntilFinished / 1000f)));
        }
    }

    @Override
    public void onADLoaded(long expireTimestamp) {
        Logger.i("SplashADFetch expireTimestamp:" + expireTimestamp);
    }

    @Override
    public void onADExposure() {
        Logger.i("SplashADExposure");
    }

    @Override
    public void onADDismissed() {
        Logger.i("SplashADDismissed");
        goToMainActivity();
    }

    @Override
    public void onNoAD(AdError error) {
        Logger.i(String.format("LoadSplashADFail, eCode=%d, errorMsg=%s", error.getErrorCode(), error.getErrorMsg()));
        /**
         * 为防止无广告时造成视觉上类似于"闪退"的情况，设定无广告时页面跳转根据需要延迟一定时间，demo
         * 给出的延时逻辑是从拉取广告开始算开屏最少持续多久，仅供参考，开发者可自定义延时逻辑，如果开发者采用demo
         * 中给出的延时逻辑，也建议开发者考虑自定义minSplashTimeWhenNoAD的值
         **/
        long alreadyDelayMills = System.currentTimeMillis() - fetchSplashADTime;//从拉广告开始到onNoAD已经消耗了多少时间
        long shouldDelayMills = alreadyDelayMills > minSplashTimeWhenNoAD ? 0 : minSplashTimeWhenNoAD
                - alreadyDelayMills;//为防止加载广告失败后立刻跳离开屏可能造成的视觉上类似于"闪退"的情况，根据设置的minSplashTimeWhenNoAD
        // 计算出还需要延时多久
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Splash2Activity.this.startActivity(new Intent(Splash2Activity.this, MainActivity.class));
                Splash2Activity.this.finish();
            }
        }, shouldDelayMills);
    }

    /**
     * 设置一个变量来控制当前开屏页面是否可以跳转，当开屏广告为普链类广告时，点击会打开一个广告落地页，此时开发者还不能打开自己的App主页。当从广告落地页返回以后，
     * 才可以跳转到开发者自己的App主页；当开屏广告是App类广告时只会下载App。
     */
    private void goToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        container.removeAllViews();
        this.finish();
    }

    @Override
    protected void onResume() {
        //判断是否该跳转到主页面
        if (mForceGoMain) {
            goToMainActivity();
        }
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mForceGoMain = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

    /**
     * 开屏页一定要禁止用户对返回按钮的控制，否则将可能导致用户手动退出了App而广告无法正常曝光和计费
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void dismissProgress() {

    }

    @Override
    public void loadDataSuccess(ResultInfo tData) {
        Logger.i("spa load data--->" + JSON.toJSONString(tData));

        if (tData != null && tData.getCode() == Constants.SUCCESS) {
            if (tData instanceof VersionInfoRet) {
                startType = ((VersionInfoRet) tData).getData().getStartType();
                //华为机型
                if (RomUtils.isHuawei()) {
                    if (((VersionInfoRet) tData).getData().getAppOpenad() == 1) {
                        loadAdInfo();
                    } else {
                        goToMainActivity();
                    }
                } else {
                    loadAdInfo();
                }
            }
        }
    }

    @Override
    public void loadDataError(Throwable throwable) {
        goToMainActivity();
    }

    @Override
    public void handleMsg(Message msg) {
        if (msg.what == MSG_GO_MAIN) {
            if (!mHasLoaded) {
                //showToast("广告已超时，跳到主页面");
                goToMainActivity();
            }
        }
    }
}
