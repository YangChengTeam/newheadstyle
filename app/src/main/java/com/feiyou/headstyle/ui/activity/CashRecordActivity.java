package com.feiyou.headstyle.ui.activity;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feiyou.headstyle.App;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.CashRecordRet;
import com.feiyou.headstyle.common.Constants;
import com.feiyou.headstyle.presenter.CashRecordPresenterImp;
import com.feiyou.headstyle.ui.adapter.CashRecordAdapter;
import com.feiyou.headstyle.ui.base.BaseFragmentActivity;
import com.feiyou.headstyle.ui.custom.ConfigDialog;
import com.feiyou.headstyle.ui.custom.NormalDecoration;
import com.feiyou.headstyle.view.CashRecordView;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class CashRecordActivity extends BaseFragmentActivity implements CashRecordView {

    @BindView(R.id.topbar)
    QMUITopBar mTopBar;

    ImageView mBackImageView;

    @BindView(R.id.cash_record_list_view)
    RecyclerView mCashRecordListView;

    @BindView(R.id.layout_no_data)
    LinearLayout mNoDataLayout;

    CashRecordAdapter cashRecordAdapter;

    CashRecordPresenterImp cashRecordPresenterImp;

    private int currentPage = 1;

    private int pageSize = 20;

    @Override
    protected int getContextViewId() {
        return R.layout.activity_cash_record;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTopBar();
        initData();
    }

    private void initTopBar() {
        QMUIStatusBarHelper.setStatusBarLightMode(this);
        View aboutView = getLayoutInflater().inflate(R.layout.common_top_back, null);
        aboutView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(48)));
        TextView titleTv = aboutView.findViewById(R.id.tv_title);
        titleTv.setText("提现记录");

        mTopBar.setCenterView(aboutView);
        mBackImageView = aboutView.findViewById(R.id.iv_back);
        mBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });
    }

    public void initData() {
        cashRecordAdapter = new CashRecordAdapter(this, null);
        mCashRecordListView.setLayoutManager(new LinearLayoutManager(this));
        mCashRecordListView.addItemDecoration(new NormalDecoration(ContextCompat.getColor(this, R.color.line_color), 1));
        mCashRecordListView.setAdapter(cashRecordAdapter);

        cashRecordPresenterImp = new CashRecordPresenterImp(this, this);
        cashRecordPresenterImp.cashList(App.getApp().mUserInfo != null ? App.getApp().mUserInfo.getId() : "", currentPage, pageSize);

        cashRecordAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                currentPage++;
                cashRecordPresenterImp.cashList(App.getApp().mUserInfo != null ? App.getApp().mUserInfo.getId() : "", currentPage, pageSize);
            }
        }, mCashRecordListView);
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void dismissProgress() {

    }

    @Override
    public void loadDataSuccess(CashRecordRet tData) {
        if (tData != null && tData.getCode() == Constants.SUCCESS) {
            if (tData.getData() != null && tData.getData().size() > 0) {
                mNoDataLayout.setVisibility(View.GONE);
                mCashRecordListView.setVisibility(View.VISIBLE);
                if (currentPage == 1) {
                    cashRecordAdapter.setNewData(tData.getData());
                } else {
                    cashRecordAdapter.addData(tData.getData());
                }

                if (tData.getData().size() == pageSize) {
                    cashRecordAdapter.loadMoreComplete();
                } else {
                    cashRecordAdapter.loadMoreEnd();
                }
            } else {
                if (currentPage == 1) {
                    mNoDataLayout.setVisibility(View.VISIBLE);
                    mCashRecordListView.setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    public void loadDataError(Throwable throwable) {
        if (currentPage == 1) {
            mNoDataLayout.setVisibility(View.VISIBLE);
            mCashRecordListView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        popBackStack();
    }
}
