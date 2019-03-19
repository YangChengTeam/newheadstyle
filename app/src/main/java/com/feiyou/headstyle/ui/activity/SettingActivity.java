package com.feiyou.headstyle.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.feiyou.headstyle.App;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.UserInfo;
import com.feiyou.headstyle.common.Constants;
import com.feiyou.headstyle.ui.base.BaseFragmentActivity;
import com.feiyou.headstyle.ui.custom.ConfigDialog;
import com.feiyou.headstyle.ui.custom.VersionUpdateDialog;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by myflying on 2018/11/23.
 */
public class SettingActivity extends BaseFragmentActivity implements ConfigDialog.ConfigListener {

    @BindView(R.id.topbar)
    QMUITopBar mTopBar;

    ImageView mBackImageView;

    @BindView(R.id.layout_my_info)
    RelativeLayout mMyInfoLayout;

    @BindView(R.id.layout_login_out)
    LinearLayout mLoginOutLayout;

    @BindView(R.id.tv_user_id)
    TextView mUserIdTv;

    @BindView(R.id.tv_user_phone)
    TextView mUserPhoneTv;

    VersionUpdateDialog updateDialog;

    private VersionUpdateDialog.UpdateListener listener;

    UserInfo userInfo;

    ConfigDialog configDialog;

    @Override
    protected int getContextViewId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTopBar();
        initData();
        initDialog();
    }

    private void initTopBar() {
        QMUIStatusBarHelper.setStatusBarLightMode(this);
        View topSearchView = getLayoutInflater().inflate(R.layout.common_top_back, null);
        topSearchView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(48)));
        TextView titleTv = topSearchView.findViewById(R.id.tv_title);
        titleTv.setText("设置");

        mTopBar.setCenterView(topSearchView);
        mBackImageView = topSearchView.findViewById(R.id.iv_back);
        mBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });
    }

    public void initData() {
        configDialog = new ConfigDialog(this, R.style.login_dialog, 1, "确认退出吗?", "请你确认是否退出当前账号，退出后无法获取更多消息哦!");
        configDialog.setConfigListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        userInfo = App.getApp().getmUserInfo();
        if (userInfo != null) {
            mUserIdTv.setText(userInfo.getId() + "");
            //mUserAddressTv.setText(userInfo.getAddr());
            if (!StringUtils.isEmpty(userInfo.getPhone())) {
                mUserPhoneTv.setText(userInfo.getPhone() + "");
            }
        }
    }

    public void initDialog() {
        listener = new VersionUpdateDialog.UpdateListener() {
            @Override
            public void update(Dialog dialog) {

            }
        };
        updateDialog = new VersionUpdateDialog(this, R.style.login_dialog, listener);
    }

    @OnClick(R.id.layout_bind_phone)
    void bindPhone() {
        Intent intent = new Intent(this, BindPhoneActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.layout_my_info)
    public void myInfo() {
        Intent intent = new Intent(this, EditUserInfoActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.layout_update_version)
    void updateVersion() {
        if (updateDialog != null && !updateDialog.isShowing()) {
            updateDialog.show();
        }
    }

    @OnClick(R.id.layout_login_out)
    void loginOut() {
        if (configDialog != null && !configDialog.isShowing()) {
            configDialog.show();
        }
    }

    @Override
    public void onBackPressed() {
        popBackStack();
    }

    @Override
    public void config() {
        App.getApp().setmUserInfo(null);
        App.getApp().setLogin(false);
        //移除存储的对象
        SPUtils.getInstance().remove(Constants.USER_INFO);
        finish();
    }

    @Override
    public void cancel() {
        if (configDialog != null && configDialog.isShowing()) {
            configDialog.dismiss();
        }
    }
}
