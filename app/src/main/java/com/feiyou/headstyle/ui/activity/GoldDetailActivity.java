package com.feiyou.headstyle.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feiyou.headstyle.App;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.GoldDetailRet;
import com.feiyou.headstyle.common.Constants;
import com.feiyou.headstyle.presenter.GoldDetailPresenterImp;
import com.feiyou.headstyle.ui.adapter.GoldDetailAdapter;
import com.feiyou.headstyle.ui.base.BaseFragmentActivity;
import com.feiyou.headstyle.ui.custom.NormalDecoration;
import com.feiyou.headstyle.view.GoldDetailView;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;

import butterknife.BindView;

public class GoldDetailActivity extends BaseFragmentActivity implements GoldDetailView {

    @BindView(R.id.topbar)
    QMUITopBar mTopBar;

    ImageView mBackImageView;

    @BindView(R.id.gold_detail_list_view)
    RecyclerView mGoldDetailListView;

    GoldDetailAdapter goldDetailAdapter;

    View topView;

    TextView mGoldNumTv;

    TextView mGoldTodayTv;

    TextView mGoldTotalEarnTv;

    TextView mGetGoldTv;

    GoldDetailPresenterImp goldDetailPresenterImp;

    private int currentPage = 1;

    private int pageSize = 20;

    @Override
    protected int getContextViewId() {
        return R.layout.fragment_gold_detail;
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
        titleTv.setText("金币明细");

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
        goldDetailAdapter = new GoldDetailAdapter(this, null, 1);
        mGoldDetailListView.setLayoutManager(new LinearLayoutManager(this));
        mGoldDetailListView.addItemDecoration(new NormalDecoration(ContextCompat.getColor(this, R.color.line_color), 1));
        mGoldDetailListView.setAdapter(goldDetailAdapter);

        topView = LayoutInflater.from(this).inflate(R.layout.gold_detail_top_view, null);
        mGoldNumTv = topView.findViewById(R.id.tv_gold_num);
        mGoldTodayTv = topView.findViewById(R.id.tv_gold_today);
        mGoldTotalEarnTv = topView.findViewById(R.id.tv_gold_total_earn);
        mGetGoldTv = topView.findViewById(R.id.tv_get_gold);
        goldDetailAdapter.addHeaderView(topView);
        mGetGoldTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GoldDetailActivity.this, GoldTaskActivity.class);
                startActivity(intent);
            }
        });

        goldDetailPresenterImp = new GoldDetailPresenterImp(this, this);

        String openid = App.getApp().mUserInfo != null ? App.getApp().mUserInfo.getOpenid() : "";
        goldDetailPresenterImp.goldDetailList(App.getApp().mUserInfo != null ? App.getApp().mUserInfo.getId() : "", openid, currentPage, pageSize);

        goldDetailAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                currentPage++;
                goldDetailPresenterImp.goldDetailList(App.getApp().mUserInfo != null ? App.getApp().mUserInfo.getId() : "", openid, currentPage, pageSize);
            }
        }, mGoldDetailListView);
    }

    @Override
    public void onBackPressed() {
        popBackStack();
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void dismissProgress() {

    }

    @Override
    public void loadDataSuccess(GoldDetailRet tData) {
        if (tData != null && tData.getCode() == Constants.SUCCESS) {
            if (tData.getData() != null) {

                mGoldNumTv.setText(tData.getData().getGoldNum() + "");
                mGoldTodayTv.setText(tData.getData().getGoldToday() + "");
                mGoldTotalEarnTv.setText(tData.getData().getGoldTotal() + "");

                if (tData.getData().getGoldDetailList() != null && tData.getData().getGoldDetailList().size() > 0) {
                    if (currentPage == 1) {
                        goldDetailAdapter.setNewData(tData.getData().getGoldDetailList());
                    } else {
                        goldDetailAdapter.addData(tData.getData().getGoldDetailList());
                    }

                    if (tData.getData().getGoldDetailList().size() == pageSize) {
                        goldDetailAdapter.loadMoreComplete();
                    } else {
                        goldDetailAdapter.loadMoreEnd();
                    }
                }


            }
        }
    }

    @Override
    public void loadDataError(Throwable throwable) {

    }
}
