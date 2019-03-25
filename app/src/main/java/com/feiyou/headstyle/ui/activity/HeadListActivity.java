package com.feiyou.headstyle.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feiyou.headstyle.App;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.HeadInfoRet;
import com.feiyou.headstyle.bean.ResultInfo;
import com.feiyou.headstyle.common.Constants;
import com.feiyou.headstyle.presenter.HeadListDataPresenterImp;
import com.feiyou.headstyle.ui.adapter.HeadInfoAdapter;
import com.feiyou.headstyle.ui.base.BaseFragmentActivity;
import com.feiyou.headstyle.utils.StatusBarUtil;
import com.feiyou.headstyle.view.HeadListDataView;
import com.orhanobut.logger.Logger;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.wang.avi.AVLoadingIndicatorView;

import butterknife.BindView;

/**
 * Created by myflying on 2018/11/23.
 */
public class HeadListActivity extends BaseFragmentActivity implements HeadListDataView, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.topbar)
    QMUITopBar mTopBar;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mRefreshLayout;

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

    private String tagName;

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
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && !StringUtils.isEmpty(bundle.getString("tag_id"))) {
            tagId = bundle.getString("tag_id");
        }

        if (bundle != null && !StringUtils.isEmpty(bundle.getString("tag_name"))) {
            tagName = bundle.getString("tag_name");
        }

        View topView = getLayoutInflater().inflate(R.layout.common_top_back, null);
        topView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(48)));

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(48));
        params.setMargins(0, StatusBarUtil.getStatusBarHeight(this), 0, 0);
        mTopBar.setCenterView(topView);
        mBackImageView = topView.findViewById(R.id.iv_back);

        TextView mTitleTv = topView.findViewById(R.id.tv_title);
        mTitleTv.setText(StringUtils.isEmpty(tagName) ? "个性头像" : tagName);

        mBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });

        QMUIStatusBarHelper.setStatusBarLightMode(this);
    }

    public void initData() {
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

        headListDataPresenterImp = new HeadListDataPresenterImp(this, this);

        headInfoAdapter = new HeadInfoAdapter(this, null);
        mHeadInfoListView.setLayoutManager(new GridLayoutManager(this, 3));
        mHeadInfoListView.setAdapter(headInfoAdapter);

        View topEmptyView = new View(this);
        topEmptyView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(12)));
        headInfoAdapter.setHeaderView(topEmptyView);

        headInfoAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                int jumpPage = position / pageSize;
                int jumpPosition = position % pageSize;

                Logger.i("jumpPage page--->" + jumpPage + "---jumpPosition--->" + jumpPosition);

                Intent intent = new Intent(HeadListActivity.this, HeadShowActivity.class);
                intent.putExtra("from_type", 1);
                intent.putExtra("tag_id", tagId);
                intent.putExtra("jump_page", jumpPage + 1);
                intent.putExtra("jump_position", jumpPosition);
                startActivity(intent);
            }
        });

        headInfoAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                currentPage++;
                headListDataPresenterImp.getDataByTagId(App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "", tagId, currentPage, pageSize);
            }
        }, mHeadInfoListView);

        avi.show();
        headListDataPresenterImp.getDataByTagId(App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "", tagId, currentPage, pageSize);
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
    public void loadDataSuccess(ResultInfo tData) {
        avi.hide();
        mRefreshLayout.setRefreshing(false);
        if (tData != null && tData.getCode() == Constants.SUCCESS) {
            if (tData instanceof HeadInfoRet) {

                if (((HeadInfoRet) tData).getData() != null && ((HeadInfoRet) tData).getData().size() > 0) {
                    mHeadInfoListView.setVisibility(View.VISIBLE);
                    mNoDataLayout.setVisibility(View.GONE);
                    if (currentPage == 1) {
                        headInfoAdapter.setNewData(((HeadInfoRet) tData).getData());
                    } else {
                        headInfoAdapter.addData(((HeadInfoRet) tData).getData());
                    }

                    if (((HeadInfoRet) tData).getData().size() == pageSize) {
                        headInfoAdapter.loadMoreComplete();
                    } else {
                        headInfoAdapter.loadMoreEnd();
                    }
                } else {
                    mHeadInfoListView.setVisibility(View.GONE);
                    mNoDataLayout.setVisibility(View.VISIBLE);
                }
            }
        } else {
            mHeadInfoListView.setVisibility(View.GONE);
            mNoDataLayout.setVisibility(View.VISIBLE);
            Logger.i(StringUtils.isEmpty(tData.getMsg()) ? "加载失败" : tData.getMsg());
        }
    }

    @Override
    public void loadDataError(Throwable throwable) {
        avi.hide();
        mRefreshLayout.setRefreshing(false);
        mHeadInfoListView.setVisibility(View.GONE);
        mNoDataLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onRefresh() {
        mRefreshLayout.setRefreshing(true);
        currentPage = 1;
        headListDataPresenterImp.getDataByTagId(App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "", tagId, currentPage, pageSize);
    }
}
