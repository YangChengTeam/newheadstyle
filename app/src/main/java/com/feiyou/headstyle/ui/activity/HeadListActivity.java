package com.feiyou.headstyle.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.HeadInfoRet;
import com.feiyou.headstyle.common.Constants;
import com.feiyou.headstyle.presenter.HeadListDataPresenterImp;
import com.feiyou.headstyle.ui.adapter.HeadInfoAdapter;
import com.feiyou.headstyle.ui.base.BaseFragmentActivity;
import com.feiyou.headstyle.utils.StatusBarUtil;
import com.feiyou.headstyle.view.HeadListDataView;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.wang.avi.AVLoadingIndicatorView;

import butterknife.BindView;

/**
 * Created by myflying on 2018/11/23.
 */
public class HeadListActivity extends BaseFragmentActivity implements HeadListDataView {

    @BindView(R.id.topbar)
    QMUITopBar mTopBar;

    @BindView(R.id.avi)
    AVLoadingIndicatorView avi;

    @BindView(R.id.head_info_list)
    RecyclerView mHeadInfoListView;

    @BindView(R.id.layout_no_data)
    LinearLayout mNoDataLayout;

    ImageView mBackImageView;

    HeadInfoAdapter headInfoAdapter;

    private HeadListDataPresenterImp headListDataPresenterImp;

    private int currentPage = 1;

    private int pageSize = 30;

    private String tagId;

    @Override
    protected int getContextViewId() {
        return R.layout.activity_head_list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTopBar();
        initData();
    }

    private void initTopBar() {
        mTopBar.setTitle(getResources().getString(R.string.app_name));
        View topSearchView = getLayoutInflater().inflate(R.layout.common_top_back, null);
        topSearchView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(48)));

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(48));
        params.setMargins(0, StatusBarUtil.getStatusBarHeight(this), 0, 0);
        mTopBar.setCenterView(topSearchView);
        //mTopBar.setLayoutParams(params);

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
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && !StringUtils.isEmpty(bundle.getString("tag_id"))) {
            tagId = bundle.getString("tag_id");
        }

        headListDataPresenterImp = new HeadListDataPresenterImp(this, this);

        headInfoAdapter = new HeadInfoAdapter(this, null);
        mHeadInfoListView.setLayoutManager(new GridLayoutManager(this, 3));
        mHeadInfoListView.setAdapter(headInfoAdapter);

        headInfoAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                currentPage++;
                headListDataPresenterImp.getDataByTagId(tagId, currentPage, pageSize);
            }
        }, mHeadInfoListView);

        avi.show();
        headListDataPresenterImp.getDataByTagId(tagId, currentPage, pageSize);
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
    public void loadDataSuccess(HeadInfoRet tData) {
        avi.hide();
        if (tData != null) {
            if (tData.getCode() == Constants.SUCCESS) {
                mNoDataLayout.setVisibility(View.GONE);

                if (currentPage == 1) {
                    headInfoAdapter.setNewData(tData.getData());
                } else {
                    headInfoAdapter.addData(tData.getData());
                }

                if (tData.getData().size() == pageSize) {
                    headInfoAdapter.loadMoreComplete();
                } else {
                    headInfoAdapter.loadMoreEnd();
                }
            } else {
                headInfoAdapter.loadMoreEnd();
                mNoDataLayout.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void loadDataError(Throwable throwable) {

    }
}
