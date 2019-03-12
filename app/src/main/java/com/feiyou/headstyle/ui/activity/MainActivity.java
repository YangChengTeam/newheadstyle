package com.feiyou.headstyle.ui.activity;


import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.feiyou.headstyle.App;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.UserInfo;
import com.feiyou.headstyle.bean.UserInfoRet;
import com.feiyou.headstyle.common.Constants;
import com.feiyou.headstyle.presenter.UserInfoPresenterImp;
import com.feiyou.headstyle.ui.adapter.MyFragmentAdapter;
import com.feiyou.headstyle.ui.base.BaseFragmentActivity;
import com.feiyou.headstyle.ui.custom.CustomViewPager;
import com.feiyou.headstyle.view.UserInfoView;
import com.orhanobut.logger.Logger;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.umeng.socialize.UMShareAPI;

import butterknife.BindView;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;


@RuntimePermissions
public class MainActivity extends BaseFragmentActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    @BindView(R.id.viewpager)
    ViewPager viewPager;

    @BindView(R.id.layout_home)
    LinearLayout mHomeLayout;

    @BindView(R.id.layout_community)
    LinearLayout mCommunityLayout;

    @BindView(R.id.layout_test)
    LinearLayout mTestLayout;

    @BindView(R.id.layout_my)
    LinearLayout mMyLayout;

    @BindView(R.id.iv_home)
    Button mHomeImageView;

    @BindView(R.id.iv_community)
    Button mCommunityImageView;

    @BindView(R.id.iv_test)
    Button mTestImageView;

    @BindView(R.id.iv_my)
    Button mMyImageView;

    @BindView(R.id.tv_home)
    TextView mHomeTextView;

    @BindView(R.id.tv_community)
    TextView mCommunityTextView;

    @BindView(R.id.tv_test)
    TextView mTestTextView;

    @BindView(R.id.tv_my)
    TextView mMyTextView;

    private MyFragmentAdapter adapter;

    private UserInfo userInfo;

    private int lastIndex = -1;

    public interface IOnFocusListener {
        void onWindowFocusChanged(boolean hasFocus);
    }

    @Override
    protected int getContextViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTopBar();
        initData();
    }

    private void initTopBar() {
        QMUIStatusBarHelper.setStatusBarLightMode(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void showRecord() {
        //ToastUtils.showLong("允许使用录音权限");
    }

    @OnPermissionDenied(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void onRecordDenied() {
        Toast.makeText(this, R.string.permission_storage_denied, Toast.LENGTH_SHORT).show();
    }

    @OnShowRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void showRationaleForRecord(PermissionRequest request) {
        showRationaleDialog(R.string.permission_storage_rationale, request);
    }

    @OnNeverAskAgain(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void onCameraNeverAskAgain() {
        Toast.makeText(this, R.string.permission_storage_never_ask_again, Toast.LENGTH_SHORT).show();
    }

    public void initData() {
        MainActivityPermissionsDispatcher.showRecordWithCheck(this);

        adapter = new MyFragmentAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        mHomeLayout.setOnClickListener(this);
        mCommunityLayout.setOnClickListener(this);
        mTestLayout.setOnClickListener(this);
        mMyLayout.setOnClickListener(this);
        viewPager.addOnPageChangeListener(this);
        setCheckedItem(0);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!StringUtils.isEmpty(SPUtils.getInstance().getString(Constants.USER_INFO))) {
            Logger.i(SPUtils.getInstance().getString(Constants.USER_INFO));
            userInfo = JSON.parseObject(SPUtils.getInstance().getString(Constants.USER_INFO), new TypeReference<UserInfo>() {
            });
            App.getApp().setmUserInfo(userInfo);
            App.getApp().setLogin(true);
        }
    }

    public void wordType() {
        Intent intent = new Intent(this, Collection1Activity.class);
        startActivity(intent);
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

    @Override
    public void onClick(View v) {
        Logger.i("view pager click --->" + v.getId());
        switch (v.getId()) {
            case R.id.layout_home:
                viewPager.setCurrentItem(0);
                setCheckedItem(0);
                break;
            case R.id.layout_community:
                viewPager.setCurrentItem(1);
                setCheckedItem(1);
                break;
            case R.id.layout_test:
                viewPager.setCurrentItem(2);
                setCheckedItem(2);
                break;
            case R.id.layout_my:
                viewPager.setCurrentItem(3);
                setCheckedItem(3);
                break;
            case R.id.layout_create:
                ToastUtils.showLong("制作");
                break;
            default:
                break;
        }
    }

    public void setCheckedItem(int current) {
        Button[] tabImages = {mHomeImageView, mCommunityImageView, mTestImageView, mMyImageView};

        TextView[] tabTexts = {mHomeTextView, mCommunityTextView, mTestTextView, mMyTextView};

        if (lastIndex == current) {
            return;
        }

        tabImages[current].setSelected(true);
        tabTexts[current].setTextColor(ContextCompat.getColor(this, R.color.tab_select_color));

        if (lastIndex > -1) {
            tabImages[lastIndex].setSelected(false);
            tabTexts[lastIndex].setTextColor(ContextCompat.getColor(this, R.color.tab_normal_color));
        }

        lastIndex = current;

//        for (int i = 0; i < 4; i++) {
//            if (current == i) {
//                tabImages[i].setSelected(true);
//                tabTexts[i].setTextColor(ContextCompat.getColor(this, R.color.tab_select_color));
//            } else {
//                tabImages[i].setSelected(false);
//                tabTexts[i].setTextColor(ContextCompat.getColor(this, R.color.tab_normal_color));
//            }
//        }

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
        if (state == 2) {
            Logger.i("view pager onPageScrollStateChanged--->" + viewPager.getCurrentItem());
            switch (viewPager.getCurrentItem()) {
                case 0:
                    setCheckedItem(0);
                    break;
                case 1:
                    setCheckedItem(1);
                    break;
                case 2:
                    setCheckedItem(2);
                    break;
                case 3:
                    setCheckedItem(3);
                    break;
                default:
                    setCheckedItem(0);
                    break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}
