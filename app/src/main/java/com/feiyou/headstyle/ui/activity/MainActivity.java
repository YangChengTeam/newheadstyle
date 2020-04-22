package com.feiyou.headstyle.ui.activity;


import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.viewpager.widget.ViewPager;

import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.blankj.utilcode.constant.TimeConstants;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.PhoneUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.bumptech.glide.Glide;
import com.feiyou.headstyle.App;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.MessageEvent;
import com.feiyou.headstyle.bean.ResultInfo;
import com.feiyou.headstyle.bean.UserInfo;
import com.feiyou.headstyle.common.Constants;
import com.feiyou.headstyle.ui.adapter.MyFragmentAdapter;
import com.feiyou.headstyle.ui.base.BaseFragmentActivity;
import com.feiyou.headstyle.ui.custom.PraiseDialog;
import com.feiyou.headstyle.ui.custom.PrivacyDialog;
import com.feiyou.headstyle.ui.custom.WarmDialog;
import com.feiyou.headstyle.utils.GoToScoreUtils;
import com.feiyou.headstyle.view.VersionView;
import com.orhanobut.logger.Logger;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMShareAPI;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;


public class MainActivity extends BaseFragmentActivity implements VersionView, ViewPager.OnPageChangeListener, RadioGroup.OnCheckedChangeListener, PraiseDialog.PraiseListener {

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

    private String[] YM_TITLES = {"home_tab_click", "click_shequ", "click_fuli", "click_ceshi", "click_my_info"};

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected int getContextViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
        initData();
    }

    private void initViews() {
        QMUIStatusBarHelper.setStatusBarLightMode(this);
    }

    public void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            currentIndex = bundle.getInt("home_index", 0);
        }

        MobclickAgent.onEvent(this, "home_into", AppUtils.getAppVersionName());
        Glide.with(this).load(R.drawable.welfare_gif).into(mCreateIv);

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

    public void setViewPagerItem(int pos){
        viewPager.setCurrentItem(pos);
        currentIndex = pos;
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
                Logger.i("select current index --->" + i);
                MobclickAgent.onEvent(this, YM_TITLES[i], AppUtils.getAppVersionName());
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
