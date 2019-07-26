package com.feiyou.headstyle.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.blankj.utilcode.util.ToastUtils;
import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTDrawFeedAd;
import com.bytedance.sdk.openadsdk.TTNativeAd;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.utils.TTAdManagerHolder;
import com.orhanobut.logger.Logger;


import java.util.ArrayList;
import java.util.List;

/**
 * 开屏广告Activity示例
 */
public class NativeVerticalVideoActivity extends Activity {
    private static final String TAG = "VerticalVideo";
    private TTAdNative mTTAdNative;
    private FrameLayout mAdContainer;
    private Context mContext;

    @SuppressWarnings("RedundantCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_ad);
        mAdContainer = (FrameLayout) findViewById(R.id.splash_container);
        //step2:创建TTAdNative对象
        mTTAdNative = TTAdManagerHolder.get().createAdNative(this);
        //在合适的时机申请权限，如read_phone_state,防止获取不了imei时候，下载类广告没有填充的问题
        TTAdManagerHolder.get().requestPermissionIfNecessary(this);
        //加载开屏广告
        mContext = this;
        loadAd();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Logger.i("test onresume--->");
    }

    /**
     * 加载广告
     */
    private void loadAd() {
        //step3:创建广告请求参数AdSlot,具体参数含义参考文档
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId("920819306")//开发者申请的广告位
                .setSupportDeepLink(true)
                .setImageAcceptedSize(1080, 1920)//符合广告场景的广告尺寸
                .setAdCount(1) //请求广告数量为1到3条
                .build();
        //step4:请求广告,对请求回调的广告作渲染处理
        mTTAdNative.loadDrawFeedAd(adSlot, new TTAdNative.DrawFeedAdListener() {
            @Override
            public void onError(int code, String message) {
                Log.d(TAG, message);
                showToast(message);
            }

            @Override
            public void onDrawFeedAdLoad(List<TTDrawFeedAd> ads) {
                if (ads == null || ads.isEmpty()) {
                    ToastUtils.showLong(" ad is null!");
                    return;
                }
                //为广告设置activity对象，下载相关行为需要该context对象
                for (TTDrawFeedAd ad : ads) {
                    ad.setActivityForDownloadApp(NativeVerticalVideoActivity.this);
                }
                //设置广告视频区域是否响应点击行为，控制视频暂停、继续播放，默认不响应；
                ads.get(0).setCanInterruptVideoPlay(true);
                //设置视频暂停的Icon和大小
                ads.get(0).setPauseIcon(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.app_share), 60);
                //获取广告视频播放的view并放入广告容器中
                mAdContainer.addView(ads.get(0).getAdView());
                //初始化并绑定广告行为
                initAdViewAndAction(ads.get(0));
            }
        });
    }

    private void initAdViewAndAction(TTDrawFeedAd ad) {
        Button action = new Button(this);
        action.setText(ad.getButtonText());
        Button btTitle = new Button(this);
        btTitle.setText(ad.getTitle());

        int height = (int) dip2Px(this, 50);
        int margin = (int) dip2Px(this, 10);
        //noinspection SuspiciousNameCombination
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(height * 3, height);
        lp.gravity = Gravity.END | Gravity.BOTTOM;
        lp.rightMargin = margin;
        lp.bottomMargin = margin;
        mAdContainer.addView(action, lp);

        FrameLayout.LayoutParams lp1 = new FrameLayout.LayoutParams(height * 3, height);
        lp1.gravity = Gravity.START | Gravity.BOTTOM;
        lp1.rightMargin = margin;
        lp1.bottomMargin = margin;
        mAdContainer.addView(btTitle, lp1);

        //响应点击区域的设置，分为普通的区域clickViews和创意区域creativeViews
        //clickViews中的view被点击会尝试打开广告落地页；creativeViews中的view被点击会根据广告类型
        //响应对应行为，如下载类广告直接下载，打开落地页类广告直接打开落地页。
        //注意：ad.getAdView()获取的view请勿放入这两个区域中。
        List<View> clickViews = new ArrayList<>();
        clickViews.add(btTitle);
        List<View> creativeViews = new ArrayList<>();
        creativeViews.add(action);
        ad.registerViewForInteraction(mAdContainer, clickViews, creativeViews, new TTNativeAd.AdInteractionListener() {
            @Override
            public void onAdClicked(View view, TTNativeAd ad) {
                showToast("onAdClicked");
            }

            @Override
            public void onAdCreativeClick(View view, TTNativeAd ad) {
                showToast("onAdCreativeClick");
            }

            @Override
            public void onAdShow(TTNativeAd ad) {
                showToast("onAdShow");
            }
        });


    }

    private float dip2Px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return dipValue * scale + 0.5f;
    }

    private void showToast(String msg) {
        ToastUtils.showLong(msg);
    }

}
