package com.feiyou.headstyle.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feiyou.headstyle.App;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.ui.adapter.TestInfoAdapter;
import com.feiyou.headstyle.ui.base.BaseFragmentActivity;
import com.feiyou.headstyle.ui.custom.LoginDialog;
import com.feiyou.headstyle.ui.custom.NormalDecoration;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;

import butterknife.BindView;

/**
 * Created by myflying on 2018/11/23.
 */
public class MoreTestActivity extends BaseFragmentActivity {

    @BindView(R.id.topbar)
    QMUITopBar mTopBar;

    @BindView(R.id.more_list)
    RecyclerView mMoreListView;

    ImageView mBackImageView;

    TestInfoAdapter testInfoAdapter;

    LoginDialog loginDialog;

    @Override
    protected int getContextViewId() {
        return R.layout.activity_more_test;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTopBar();
        initData();
    }

    private void initTopBar() {
        QMUIStatusBarHelper.setStatusBarLightMode(this);
        View topSearchView = getLayoutInflater().inflate(R.layout.common_top_back, null);
        topSearchView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(48)));
        TextView titleTv = topSearchView.findViewById(R.id.tv_title);
        titleTv.setText("更多测试");

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
        loginDialog = new LoginDialog(this, R.style.login_dialog);

        testInfoAdapter = new TestInfoAdapter(this, App.testInfoList);
        mMoreListView.setLayoutManager(new LinearLayoutManager(this));
        mMoreListView.setAdapter(testInfoAdapter);
        mMoreListView.addItemDecoration(new NormalDecoration(ContextCompat.getColor(this, R.color.line_color), 1));

        testInfoAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                if (!App.getApp().isLogin) {
                    if (loginDialog != null && !loginDialog.isShowing()) {
                        loginDialog.show();
                    }
                    return;
                }

                if (testInfoAdapter.getData().get(position).getTestType() == 1) {
                    Intent intent = new Intent(MoreTestActivity.this, TestDetailActivity.class);
                    intent.putExtra("tid", testInfoAdapter.getData().get(position).getId());
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(MoreTestActivity.this, TestImageDetailActivity.class);
                    intent.putExtra("tid", testInfoAdapter.getData().get(position).getId());
                    startActivity(intent);
                }

            }
        });
    }

    @Override
    public void onBackPressed() {
        popBackStack();
    }
}
