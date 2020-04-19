package com.feiyou.headstyle.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feiyou.headstyle.App;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.TestInfoRet;
import com.feiyou.headstyle.common.Constants;
import com.feiyou.headstyle.presenter.TestInfoPresenterImp;
import com.feiyou.headstyle.ui.adapter.TestInfoAdapter;
import com.feiyou.headstyle.ui.base.BaseFragmentActivity;
import com.feiyou.headstyle.ui.custom.LoginDialog;
import com.feiyou.headstyle.ui.custom.NormalDecoration;
import com.feiyou.headstyle.view.TestInfoView;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.umeng.analytics.MobclickAgent;
import com.wang.avi.AVLoadingIndicatorView;

import butterknife.BindView;

/**
 * Created by myflying on 2019/1/11.
 */
public class TestCategoryActivity extends BaseFragmentActivity implements TestInfoView {

    @BindView(R.id.topbar)
    QMUITopBar mTopBar;

    @BindView(R.id.avi)
    AVLoadingIndicatorView avi;

    @BindView(R.id.test_list)
    RecyclerView mTypeListView;

    @BindView(R.id.layout_no_data)
    LinearLayout mNoDataLayout;

    ImageView mBackImageView;

    private int currentPage = 1;

    private int pageSize = 30;

    private TestInfoPresenterImp testInfoPresenterImp;

    private TestInfoAdapter testInfoAdapter;

    private String cid;

    private String title;

    LoginDialog loginDialog;

    @Override
    protected int getContextViewId() {
        return R.layout.activity_test_category;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTopBar();
        initData();
    }

    private void initTopBar() {
        MobclickAgent.onEvent(this, "test_category_into", AppUtils.getAppVersionName());

        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.getString("cid") != null) {
            cid = bundle.getString("cid");
        }

        if (bundle != null && bundle.getString("title") != null) {
            title = bundle.getString("title");
        }

        View topSearchView = getLayoutInflater().inflate(R.layout.common_top_back, null);
        topSearchView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(48)));
        TextView titleTv = topSearchView.findViewById(R.id.tv_title);
        titleTv.setText(StringUtils.isEmpty(title) ? "精选测试" : title);
        mTopBar.setCenterView(topSearchView);

        mBackImageView = topSearchView.findViewById(R.id.iv_back);
        mBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });

        QMUIStatusBarHelper.setStatusBarLightMode(this);
    }

    public void initData() {

        loginDialog = new LoginDialog(this, R.style.login_dialog);
        testInfoPresenterImp = new TestInfoPresenterImp(this, this);
        testInfoAdapter = new TestInfoAdapter(this, null);
        mTypeListView.setLayoutManager(new LinearLayoutManager(this));
        mTypeListView.addItemDecoration(new NormalDecoration(ContextCompat.getColor(this, R.color.line_color), 1));
        mTypeListView.setAdapter(testInfoAdapter);
        avi.show();
        testInfoPresenterImp.getDataListByCid(cid);

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
                    Intent intent = new Intent(TestCategoryActivity.this, TestDetailActivity.class);
                    intent.putExtra("tid", testInfoAdapter.getData().get(position).getId());
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(TestCategoryActivity.this, TestImageDetailActivity.class);
                    intent.putExtra("tid", testInfoAdapter.getData().get(position).getId());
                    startActivity(intent);
                }

            }
        });
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void dismissProgress() {

    }

    @Override
    public void loadDataSuccess(TestInfoRet tData) {
        avi.hide();
        if (tData != null) {
            if (tData.getCode() == Constants.SUCCESS) {
                mNoDataLayout.setVisibility(View.GONE);
                testInfoAdapter.setNewData(tData.getData());
            } else {
                mNoDataLayout.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void loadDataError(Throwable throwable) {

    }

    @Override
    public void onBackPressed() {
        popBackStack();
    }
}
