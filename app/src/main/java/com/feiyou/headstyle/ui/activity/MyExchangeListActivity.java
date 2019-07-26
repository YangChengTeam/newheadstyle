package com.feiyou.headstyle.ui.activity;

import android.content.Intent;
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
import com.feiyou.headstyle.bean.ExchangeInfoRet;
import com.feiyou.headstyle.common.Constants;
import com.feiyou.headstyle.presenter.ExchangeInfoPresenterImp;
import com.feiyou.headstyle.ui.adapter.MyExchangeListAdapter;
import com.feiyou.headstyle.ui.base.BaseFragmentActivity;
import com.feiyou.headstyle.ui.custom.NormalDecoration;
import com.feiyou.headstyle.view.ExchangeInfoView;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MyExchangeListActivity extends BaseFragmentActivity implements ExchangeInfoView {

    @BindView(R.id.topbar)
    QMUITopBar mTopBar;

    ImageView mBackImageView;

    @BindView(R.id.exchange_list_view)
    RecyclerView mExchangeListView;

    @BindView(R.id.layout_no_data)
    LinearLayout mNoDataLayout;

    MyExchangeListAdapter myExchangeListAdapter;

    ExchangeInfoPresenterImp exchangeInfoPresenterImp;

    private int currentPage = 1;

    private int pageSize = 20;

    private String eid = "";

    @Override
    protected int getContextViewId() {
        return R.layout.activity_my_exchange_list;
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
        titleTv.setText("兑换记录");

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
            eid = bundle.getString("eid", "");
        }
        String openid = App.getApp().mUserInfo != null ? App.getApp().mUserInfo.getOpenid() : "";
        exchangeInfoPresenterImp = new ExchangeInfoPresenterImp(this, this);
        exchangeInfoPresenterImp.exchangeList(App.getApp().mUserInfo != null ? App.getApp().mUserInfo.getId() : "",openid, currentPage, pageSize, eid);

        myExchangeListAdapter = new MyExchangeListAdapter(this, null);
        mExchangeListView.setLayoutManager(new LinearLayoutManager(this));
        mExchangeListView.addItemDecoration(new NormalDecoration(ContextCompat.getColor(this, R.color.line_color), 1));
        mExchangeListView.setAdapter(myExchangeListAdapter);

        myExchangeListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(MyExchangeListActivity.this, ExchangeDetailActivity.class);
                intent.putExtra("order_item", myExchangeListAdapter.getData().get(position));
                startActivity(intent);
            }
        });
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
    public void loadDataSuccess(ExchangeInfoRet tData) {
        if (tData != null && tData.getCode() == Constants.SUCCESS) {
            if (tData.getData() != null && tData.getData().size() > 0) {
                mNoDataLayout.setVisibility(View.GONE);
                mExchangeListView.setVisibility(View.VISIBLE);
                if (currentPage == 1) {
                    myExchangeListAdapter.setNewData(tData.getData());
                } else {
                    myExchangeListAdapter.addData(tData.getData());
                }

                if (tData.getData().size() == pageSize) {
                    myExchangeListAdapter.loadMoreComplete();
                } else {
                    myExchangeListAdapter.loadMoreEnd();
                }
            } else {
                if (currentPage == 1) {
                    mNoDataLayout.setVisibility(View.VISIBLE);
                    mExchangeListView.setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    public void loadDataError(Throwable throwable) {
        if (currentPage == 1) {
            mNoDataLayout.setVisibility(View.VISIBLE);
            mExchangeListView.setVisibility(View.GONE);
        }
    }
}
