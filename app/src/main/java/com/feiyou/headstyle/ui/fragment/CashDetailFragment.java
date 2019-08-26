package com.feiyou.headstyle.ui.fragment;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feiyou.headstyle.App;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.GoldDetailRet;
import com.feiyou.headstyle.common.Constants;
import com.feiyou.headstyle.presenter.GoldDetailPresenterImp;
import com.feiyou.headstyle.ui.adapter.GoldDetailAdapter;
import com.feiyou.headstyle.ui.base.BaseFragment;
import com.feiyou.headstyle.ui.custom.NormalDecoration;
import com.feiyou.headstyle.view.GoldDetailView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CashDetailFragment extends BaseFragment implements GoldDetailView {

    @BindView(R.id.cash_detail_list_view)
    RecyclerView mGoldDetailListView;

    @BindView(R.id.layout_no_data)
    LinearLayout mNoDataLayout;

    GoldDetailAdapter goldDetailAdapter;

    GoldDetailPresenterImp goldDetailPresenterImp;

    private int currentPage = 1;

    private int pageSize = 20;

    public static CashDetailFragment getInstance() {
        return new CashDetailFragment();
    }

    @Override
    protected View onCreateView() {
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_cash_detail, null);
        ButterKnife.bind(this, root);
        initData();
        return root;
    }

    public void initData() {
        goldDetailAdapter = new GoldDetailAdapter(getActivity(), null, 2);
        mGoldDetailListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mGoldDetailListView.addItemDecoration(new NormalDecoration(ContextCompat.getColor(getActivity(), R.color.line_color), 1));
        mGoldDetailListView.setAdapter(goldDetailAdapter);

        goldDetailPresenterImp = new GoldDetailPresenterImp(this, getActivity());

        String openid = App.getApp().mUserInfo != null ? App.getApp().mUserInfo.getOpenid() : "";
        goldDetailPresenterImp.goldCashDetailList(App.getApp().mUserInfo != null ? App.getApp().mUserInfo.getId() : "", openid, currentPage, pageSize, 1);

        goldDetailAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                currentPage++;
                goldDetailPresenterImp.goldCashDetailList(App.getApp().mUserInfo != null ? App.getApp().mUserInfo.getId() : "", openid, currentPage, pageSize, 1);
            }
        }, mGoldDetailListView);
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

                if (tData.getData().getCashList() != null && tData.getData().getCashList().size() > 0) {
                    if (currentPage == 1) {
                        goldDetailAdapter.setNewData(tData.getData().getCashList());
                    } else {
                        goldDetailAdapter.addData(tData.getData().getCashList());
                    }

                    if (tData.getData().getCashList().size() == pageSize) {
                        goldDetailAdapter.loadMoreComplete();
                    } else {
                        goldDetailAdapter.loadMoreEnd();
                    }
                }else {
                    if (currentPage == 1) {
                        mNoDataLayout.setVisibility(View.VISIBLE);
                        mGoldDetailListView.setVisibility(View.GONE);
                    }
                }

            } else {
                if (currentPage == 1) {
                    mNoDataLayout.setVisibility(View.VISIBLE);
                    mGoldDetailListView.setVisibility(View.GONE);
                }
            }
        } else {
            if (currentPage == 1) {
                mNoDataLayout.setVisibility(View.VISIBLE);
                mGoldDetailListView.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void loadDataError(Throwable throwable) {
        if (currentPage == 1) {
            mNoDataLayout.setVisibility(View.VISIBLE);
            mGoldDetailListView.setVisibility(View.GONE);
        }
    }
}
