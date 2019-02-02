package com.feiyou.headstyle.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.HeadType;
import com.feiyou.headstyle.ui.adapter.MoreHeadTypeAdapter;
import com.feiyou.headstyle.ui.base.BaseFragmentActivity;
import com.feiyou.headstyle.ui.custom.LoginDialog;
import com.feiyou.headstyle.ui.custom.VersionUpdateDialog;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by myflying on 2018/11/23.
 */
public class SettingActivity extends BaseFragmentActivity {

    @BindView(R.id.topbar)
    QMUITopBar mTopBar;

    ImageView mBackImageView;

    @BindView(R.id.layout_my_info)
    RelativeLayout mMyInfoLayout;

    VersionUpdateDialog updateDialog;

    private VersionUpdateDialog.UpdateListener listener;

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
        mTopBar.setTitle(getResources().getString(R.string.app_name));
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

    @OnClick(R.id.layout_black)
    void blackList() {
        Intent intent = new Intent(this, BlackListActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        popBackStack();
    }
}
