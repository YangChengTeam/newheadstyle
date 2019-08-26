package com.feiyou.headstyle.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feiyou.headstyle.App;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.GoodInfoRet;
import com.feiyou.headstyle.bean.WelfareInfo;
import com.feiyou.headstyle.common.Constants;
import com.feiyou.headstyle.presenter.GoodInfoPresenterImp;
import com.feiyou.headstyle.ui.adapter.GoodsListAdapter;
import com.feiyou.headstyle.ui.adapter.TaskListAdapter;
import com.feiyou.headstyle.ui.base.BaseFragmentActivity;
import com.feiyou.headstyle.ui.custom.NormalDecoration;
import com.feiyou.headstyle.view.GoodInfoView;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class GoldMailActivity extends BaseFragmentActivity implements GoodInfoView, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mRefreshLayout;

    @BindView(R.id.topbar)
    QMUITopBar mTopBar;

    ImageView mBackImageView;

    @BindView(R.id.good_list_view)
    RecyclerView mGoodListView;

    @BindView(R.id.layout_no_data)
    LinearLayout mNoDataLayout;

    @BindView(R.id.avi)
    AVLoadingIndicatorView avi;

    GoodsListAdapter goodsListAdapter;

    GoodInfoPresenterImp goodInfoPresenterImp;

    private int currentPage = 1;

    private int pageSize = 20;

    List<WelfareInfo.SignSetInfo> signList;

    private int signDays;//连续签到的天数

    private double randomHongbao;

    private int isSignToday;

    @Override
    protected int getContextViewId() {
        return R.layout.activity_gold_mail;
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
        titleTv.setText("金币商城");

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

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            randomHongbao = bundle.getDouble("random_money");
            signDays = bundle.getInt("sign_days");
            isSignToday = bundle.getInt("is_sign_today");
        }

        signList = (List<WelfareInfo.SignSetInfo>) getIntent().getSerializableExtra("sign_list");

        mRefreshLayout.setOnRefreshListener(this);
        //设置进度View样式的大小，只有两个值DEFAULT和LARGE
        //设置进度View下拉的起始点和结束点，scale 是指设置是否需要放大或者缩小动画
        mRefreshLayout.setProgressViewOffset(true, -0, 200);
        //设置进度View下拉的结束点，scale 是指设置是否需要放大或者缩小动画
        mRefreshLayout.setProgressViewEndTarget(true, 180);
        //设置进度View的组合颜色，在手指上下滑时使用第一个颜色，在刷新中，会一个个颜色进行切换
        mRefreshLayout.setColorSchemeColors(ContextCompat.getColor(this, R.color.colorPrimary), Color.RED, Color.YELLOW, Color.BLUE);

        //设置触发刷新的距离
        mRefreshLayout.setDistanceToTriggerSync(200);
        //如果child是自己自定义的view，可以通过这个回调，告诉mSwipeRefreshLayoutchild是否可以滑动
        mRefreshLayout.setOnChildScrollUpCallback(null);

        List<String> goodList = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            goodList.add(i + "");
        }

        goodsListAdapter = new GoodsListAdapter(this, null);
        mGoodListView.setLayoutManager(new GridLayoutManager(this, 2));
        mGoodListView.setAdapter(goodsListAdapter);

        View topView = new View(this);
        topView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(12)));
        goodsListAdapter.addHeaderView(topView);

        goodsListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(GoldMailActivity.this, GoodDetailActivity.class);
                intent.putExtra("gid", goodsListAdapter.getData().get(position).getId() + "");
                intent.putExtra("sign_list", (Serializable) signList);
                intent.putExtra("sign_days", signDays);
                intent.putExtra("random_money", randomHongbao);
                intent.putExtra("is_sign_today", isSignToday);
                startActivity(intent);
            }
        });

        goodsListAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                currentPage++;
                goodInfoPresenterImp.getGoodListData(currentPage, pageSize);
            }
        }, mGoodListView);

        goodInfoPresenterImp = new GoodInfoPresenterImp(this, this);
        goodInfoPresenterImp.getGoodListData(currentPage, pageSize);
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
        avi.hide();
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void loadDataSuccess(GoodInfoRet tData) {
        avi.hide();
        mRefreshLayout.setRefreshing(false);
        if (tData != null && tData.getCode() == Constants.SUCCESS) {
            if (tData.getData() != null && tData.getData().size() > 0) {
                mNoDataLayout.setVisibility(View.GONE);
                mGoodListView.setVisibility(View.VISIBLE);
                if (currentPage == 1) {
                    goodsListAdapter.setNewData(tData.getData());
                } else {
                    goodsListAdapter.addData(tData.getData());
                }

                if (tData.getData().size() == pageSize) {
                    goodsListAdapter.loadMoreComplete();
                } else {
                    goodsListAdapter.loadMoreEnd();
                }
            } else {
                if (currentPage == 1) {
                    mNoDataLayout.setVisibility(View.VISIBLE);
                    mGoodListView.setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    public void loadDataError(Throwable throwable) {
        avi.hide();
        mRefreshLayout.setRefreshing(false);
        if (currentPage == 1) {
            mNoDataLayout.setVisibility(View.VISIBLE);
            mGoodListView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRefresh() {
        mRefreshLayout.setRefreshing(true);
        currentPage = 1;
        goodInfoPresenterImp.getGoodListData(currentPage, pageSize);
    }
}
