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
import com.feiyou.headstyle.bean.ResultInfo;
import com.feiyou.headstyle.bean.UserInfo;
import com.feiyou.headstyle.bean.VersionInfo;
import com.feiyou.headstyle.bean.VersionInfoRet;
import com.feiyou.headstyle.common.Constants;
import com.feiyou.headstyle.presenter.TaskRecordInfoPresenterImp;
import com.feiyou.headstyle.presenter.VersionPresenterImp;
import com.feiyou.headstyle.ui.adapter.MyFragmentAdapter;
import com.feiyou.headstyle.ui.base.BaseFragmentActivity;
import com.feiyou.headstyle.ui.custom.EveryDayHongBaoDialog;
import com.feiyou.headstyle.ui.custom.PraiseDialog;
import com.feiyou.headstyle.ui.custom.PrivacyDialog;
import com.feiyou.headstyle.ui.custom.VersionUpdateDialog;
import com.feiyou.headstyle.ui.custom.WarmDialog;
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
public class Main1Activity extends BaseFragmentActivity implements VersionView, ViewPager.OnPageChangeListener, RadioGroup.OnCheckedChangeListener, PraiseDialog.PraiseListener {

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

    private int currentIndex;

    @Override
    protected int getContextViewId() {
        return R.layout.activity_main1;
    }

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
        //ToastUtils.showLong("允许使用权限");
        EventBus.getDefault().post(new MessageEvent("permission_use"));
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
        //TTAdManagerHolder.get().requestPermissionIfNecessary(this);

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

        if (currentIndex > 0) {
            viewPager.setCurrentItem(currentIndex);
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

    }

    @Override
    public void loadDataSuccess(ResultInfo tData) {
        Logger.i("version info ---> " + JSON.toJSONString(tData));

        if (tData != null) {

        }
    }

    @Override
    public void loadDataError(Throwable throwable) {

    }

    public int getCurrentTabIndex() {
        return viewPager.getCurrentItem();
    }

}
