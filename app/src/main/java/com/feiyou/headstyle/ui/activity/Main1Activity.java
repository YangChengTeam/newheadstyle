package com.feiyou.headstyle.ui.activity;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.blankj.utilcode.constant.TimeConstants;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.PathUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdManager;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTRewardVideoAd;
import com.feiyou.headstyle.App;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.MessageEvent;
import com.feiyou.headstyle.bean.UserInfo;
import com.feiyou.headstyle.bean.VersionInfo;
import com.feiyou.headstyle.bean.VersionInfoRet;
import com.feiyou.headstyle.common.Constants;
import com.feiyou.headstyle.presenter.VersionPresenterImp;
import com.feiyou.headstyle.ui.adapter.MyFragmentAdapter;
import com.feiyou.headstyle.ui.base.BaseFragmentActivity;
import com.feiyou.headstyle.ui.custom.EveryDayHongBaoDialog;
import com.feiyou.headstyle.ui.custom.PraiseDialog;
import com.feiyou.headstyle.ui.custom.VersionUpdateDialog;
import com.feiyou.headstyle.utils.GoToScoreUtils;
import com.feiyou.headstyle.utils.NotificationUtils;
import com.feiyou.headstyle.utils.RandomUtils;
import com.feiyou.headstyle.utils.TTAdManagerHolder;
import com.feiyou.headstyle.view.VersionView;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.orhanobut.logger.Logger;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.umeng.socialize.UMShareAPI;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;


@RuntimePermissions
public class Main1Activity extends BaseFragmentActivity implements VersionView, ViewPager.OnPageChangeListener, RadioGroup.OnCheckedChangeListener, PraiseDialog.PraiseListener, VersionUpdateDialog.UpdateListener, EveryDayHongBaoDialog.EveryDayHongBaoListener {

    @BindView(R.id.viewpager)
    ViewPager viewPager;

    @BindView(R.id.layout_bottom_nav)
    RadioGroup mTabRadioGroup;

    @BindView(R.id.iv_home_message_remind)
    ImageView mTotalCountIv;

    @BindView(R.id.iv_create)
    ImageView mCreateIv;

    private MyFragmentAdapter adapter;

    private UserInfo userInfo;

    private long clickTime = 0;

    PraiseDialog praiseDialog;

    VersionPresenterImp versionPresenterImp;

    private ProgressDialog progressDialog = null;

    private VersionInfo versionInfo;

    BaseDownloadTask task;

    VersionUpdateDialog updateDialog;

    private int currentIndex;

    EveryDayHongBaoDialog everyDayHongBaoDialog;

    private TTAdNative mTTAdNative;

    private TTRewardVideoAd mttRewardVideoAd;

    @Override
    protected int getContextViewId() {
        return R.layout.activity_main1;
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    int progress = (Integer) msg.obj;
                    updateDialog.setProgress(progress);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Main1ActivityPermissionsDispatcher.showRecordWithCheck(this);
        initTopBar();
        initData();
    }

    private void initTopBar() {
        QMUIStatusBarHelper.setStatusBarLightMode(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Main1ActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @NeedsPermission({Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE})
    void showRecord() {
        //ToastUtils.showLong("允许使用存储权限");
    }

    @OnPermissionDenied({Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE})
    void onRecordDenied() {
        Toast.makeText(this, R.string.permission_storage_denied, Toast.LENGTH_SHORT).show();
    }

    @OnShowRationale({Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE})
    void showRationaleForRecord(PermissionRequest request) {
        showRationaleDialog(R.string.permission_storage_rationale, request);
    }

    @OnNeverAskAgain({Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE})
    void onCameraNeverAskAgain() {
        Toast.makeText(this, R.string.permission_storage_never_ask_again, Toast.LENGTH_SHORT).show();
    }

    public void initData() {
        //step2:(可选，强烈建议在合适的时机调用):申请部分权限，如read_phone_state,防止获取不了imei时候，下载类广告没有填充的问题。
        TTAdManagerHolder.get().requestPermissionIfNecessary(this);

        TTAdManager ttAdManager = TTAdManagerHolder.get();

        //step3:创建TTAdNative对象,用于调用广告请求接口
        mTTAdNative = ttAdManager.createAdNative(this);

        loadAd("920819619", TTAdConstant.VERTICAL, 100);

        Glide.with(this).load(R.drawable.welfare_gif).into(mCreateIv);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            currentIndex = bundle.getInt("home_index", 0);
        }

        //默认设置选中首页
        RadioButton radioButton = (RadioButton) mTabRadioGroup.getChildAt(currentIndex);
        radioButton.setChecked(true);

        adapter = new MyFragmentAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(this);
        viewPager.setOffscreenPageLimit(1);
        mTabRadioGroup.setOnCheckedChangeListener(this);

        everyDayHongBaoDialog = new EveryDayHongBaoDialog(this, R.style.login_dialog);
        everyDayHongBaoDialog.setEveryDayHongBaoListener(this);

        praiseDialog = new PraiseDialog(this, R.style.login_dialog);
        praiseDialog.setPraiseListener(this);
        praiseDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                Logger.i("praiseDialog setOnDismissListener--->");
                //关闭后存储状态,时间
                SPUtils.getInstance().put(Constants.OPEN_STATE, false);
                SPUtils.getInstance().put(Constants.SCORE_CLOSE_TIME, TimeUtils.getNowMills());
            }
        });

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在检测新版本");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (versionInfo != null && versionInfo.getVersionIsChange() == 1) {
                        return true;//不执行父类点击事件
                    }
                    return false;
                }
                return false;
            }
        });

        updateDialog = new VersionUpdateDialog(this, R.style.login_dialog);
        updateDialog.setUpdateListener(this);
        updateDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (versionInfo != null && versionInfo.getVersionIsChange() == 1) {
                        return true;//不执行父类点击事件
                    }
                    return false;
                }
                return false;
            }
        });

        if (currentIndex > 0) {
            viewPager.setCurrentItem(currentIndex);
        }

        versionPresenterImp = new VersionPresenterImp(this, this);
        //请求版本更新
        versionPresenterImp.getVersionInfo(com.feiyou.headstyle.utils.AppUtils.getMetaDataValue(this, "UMENG_CHANNEL"));

        if (everyDayHongBaoDialog != null && !everyDayHongBaoDialog.isShowing()) {
            everyDayHongBaoDialog.show();
        }
    }

    public void showScore() {

        long lastTime = SPUtils.getInstance().getLong(Constants.SCORE_CLOSE_TIME, 0);
        if (lastTime > 0) {
            //时间间隔超过7天后可以再次弹出
            if (TimeUtils.getTimeSpanByNow(lastTime, TimeConstants.DAY) > 7) {
                SPUtils.getInstance().put(Constants.OPEN_STATE, true);
            }
        }

        //是否能打开
        if (SPUtils.getInstance().getBoolean(Constants.OPEN_STATE, true)) {
            //是否满足条件
            if (SPUtils.getInstance().getBoolean(Constants.IS_OPEN_SCORE, false)) {
                if (praiseDialog != null && !praiseDialog.isShowing()) {
                    praiseDialog.show();
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        Logger.i("main onresume--->");

        if (!StringUtils.isEmpty(SPUtils.getInstance().getString(Constants.USER_INFO))) {
            Logger.i(SPUtils.getInstance().getString(Constants.USER_INFO));
            userInfo = JSON.parseObject(SPUtils.getInstance().getString(Constants.USER_INFO), new TypeReference<UserInfo>() {
            });
            App.getApp().setmUserInfo(userInfo);
            App.getApp().setLogin(true);
        }

        showScore();
    }

    private void showRationaleDialog(@StringRes int messageResId, final PermissionRequest request) {
        new AlertDialog.Builder(this)
                .setPositiveButton(R.string.button_allow, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        request.proceed();
                    }
                })
                .setNegativeButton(R.string.button_deny, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        request.cancel();
                    }
                })
                .setCancelable(false)
                .setMessage(messageResId)
                .show();
    }

    @OnClick(R.id.layout_create)
    public void createImage() {
        viewPager.setCurrentItem(2);
    }

    @OnClick(R.id.rb_home)
    public void homeClick() {
        Logger.i("view pager home --->");
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {
        Logger.i("view pager onPageScrolled--->" + i);
    }

    @Override
    public void onPageSelected(int i) {
        Logger.i("view pager onPageSelected--->" + i);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        Logger.i("view pager onPageScrollStateChanged--->state--->" + state + viewPager.getCurrentItem());
        if (state == 2) {
            Logger.i("view pager onPageScrollStateChanged--->state--->" + state + viewPager.getCurrentItem());
            RadioButton radioButton = (RadioButton) mTabRadioGroup.getChildAt(viewPager.getCurrentItem());
            radioButton.setChecked(true);
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        Logger.i("check id--->" + checkedId);
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            if (radioGroup.getChildAt(i).getId() == checkedId) {
                viewPager.setCurrentItem(i);
                if (i == 4) {
                    mTotalCountIv.setVisibility(View.GONE);
                }
                return;
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(MessageEvent messageEvent) {
        if (messageEvent.getMessage().equals("home_message_remind")) {
            mTotalCountIv.setVisibility(View.VISIBLE);
            App.isShowTotalCount = true;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        exit();
    }

    private void exit() {
        if ((System.currentTimeMillis() - clickTime) > 2000) {
            clickTime = System.currentTimeMillis();
            Toasty.normal(getApplicationContext(), "再按一次退出").show();
        } else {
            System.exit(0);
        }
    }

    @Override
    public void config() {
        if (praiseDialog != null && praiseDialog.isShowing()) {
            praiseDialog.dismiss();
        }
        GoToScoreUtils.goToMarket(this, Constants.APP_PACKAGE_NAME);
    }

    @Override
    public void cancel() {
        if (praiseDialog != null && praiseDialog.isShowing()) {
            praiseDialog.dismiss();
        }

        //关闭后存储状态,时间
        SPUtils.getInstance().put(Constants.OPEN_STATE, false);
        SPUtils.getInstance().put(Constants.SCORE_CLOSE_TIME, TimeUtils.getNowMills());
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void dismissProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void loadDataSuccess(VersionInfoRet tData) {
        Logger.i("version info ---> " + JSON.toJSONString(tData));

        if (tData != null && tData.getCode() == Constants.SUCCESS) {
            versionInfo = tData.getData();
            if (versionInfo.getVersionCode() > AppUtils.getAppVersionCode()) {
                if (updateDialog != null && !updateDialog.isShowing()) {
                    updateDialog.setVersionCode(tData.getData().getVersionName());
                    updateDialog.setVersionContent(tData.getData().getVersionDesc());
                    updateDialog.setIsForceUpdate(tData.getData().getVersionIsChange());
                    updateDialog.show();
                }
            }
        }
    }

    @Override
    public void loadDataError(Throwable throwable) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void update() {
        if (versionInfo != null && !StringUtils.isEmpty(versionInfo.getVersionUrl())) {
            downAppFile(versionInfo.getVersionUrl());
        }
    }

    @Override
    public void updateCancel() {

    }


    public void downAppFile(String downUrl) {
        final String filePath = PathUtils.getExternalAppFilesPath() + "/new_app.apk";
        Logger.i("down app path --->" + filePath);

        task = FileDownloader.getImpl().create(downUrl)
                .setPath(filePath)
                .setListener(new FileDownloadListener() {
                    @Override
                    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        //Toasty.normal(SettingActivity.this, "正在更新版本后...").show();
                    }

                    @Override
                    protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
                    }

                    @Override
                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        int progress = (int) ((soFarBytes * 1.0 / totalBytes) * 100);
                        Logger.i("progress--->" + soFarBytes + "---" + totalBytes + "---" + progress);

                        Message message = new Message();
                        message.what = 0;
                        message.obj = progress;
                        mHandler.sendMessage(message);
                    }

                    @Override
                    protected void blockComplete(BaseDownloadTask task) {
                    }

                    @Override
                    protected void retry(final BaseDownloadTask task, final Throwable ex, final int retryingTimes, final int soFarBytes) {
                    }

                    @Override
                    protected void completed(BaseDownloadTask task) {
                        ToastUtils.showLong("下载完成");
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }

                        AppUtils.installApp(filePath);
                    }

                    @Override
                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                    }

                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {
                    }

                    @Override
                    protected void warn(BaseDownloadTask task) {
                    }
                });

        task.start();
    }

    private boolean mHasShowDownloadActive = false;

    private void loadAd(String codeId, int orientation, int goldNum) {
        //step4:创建广告请求参数AdSlot,具体参数含义参考文档
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(codeId)
                .setSupportDeepLink(true)
                .setImageAcceptedSize(1080, 1920)
                .setRewardName("金币") //奖励的名称
                .setRewardAmount(goldNum)  //奖励的数量
                .setUserID(userInfo != null ? userInfo.getId() : "10000" + RandomUtils.nextInt())//用户id,必传参数
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
                        if (rewardVerify) {
                            ToastUtils.showLong("正常播放完成，奖励有效");
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
    public void openEveryDayHongBao() {
        //step6:在获取到广告后展示
        mttRewardVideoAd.showRewardVideoAd(this);
        mttRewardVideoAd = null;
    }

    @Override
    public void closeEveryDayHongBao() {
        ToastUtils.showLong("关闭领红包");
    }
}
