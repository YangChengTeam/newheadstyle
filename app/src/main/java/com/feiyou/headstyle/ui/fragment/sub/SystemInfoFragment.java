package com.feiyou.headstyle.ui.fragment.sub;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feiyou.headstyle.App;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.SystemInfoRet;
import com.feiyou.headstyle.common.Constants;
import com.feiyou.headstyle.presenter.MyCommentPresenterImp;
import com.feiyou.headstyle.presenter.SystemInfoPresenterImp;
import com.feiyou.headstyle.ui.adapter.MyNoticeAdapter;
import com.feiyou.headstyle.ui.base.BaseFragment;
import com.feiyou.headstyle.view.SystemInfoView;
import com.orhanobut.logger.Logger;
import com.wang.avi.AVLoadingIndicatorView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by myflying on 2018/11/26.
 */
public class SystemInfoFragment extends BaseFragment implements SystemInfoView, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mRefreshLayout;

    @BindView(R.id.avi)
    AVLoadingIndicatorView avi;

    @BindView(R.id.my_notice_list)
    RecyclerView mNoticeListView;

    @BindView(R.id.layout_no_data)
    LinearLayout noDataLayout;

    MyNoticeAdapter noticeAdapter;

    private SystemInfoPresenterImp systemInfoPresenterImp;

    private int currentPage = 1;

    private int pageSize = 30;

    public static SystemInfoFragment getInstance() {
        return new SystemInfoFragment();
    }

    @Override
    protected View onCreateView() {
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_my_notice, null);
        ButterKnife.bind(this, root);
        initViews();
        return root;
    }

    public void initViews() {
        mRefreshLayout.setOnRefreshListener(this);
        //设置进度View样式的大小，只有两个值DEFAULT和LARGE
        //设置进度View下拉的起始点和结束点，scale 是指设置是否需要放大或者缩小动画
        mRefreshLayout.setProgressViewOffset(true, -0, 200);
        //设置进度View下拉的结束点，scale 是指设置是否需要放大或者缩小动画
        mRefreshLayout.setProgressViewEndTarget(true, 180);
        //设置进度View的组合颜色，在手指上下滑时使用第一个颜色，在刷新中，会一个个颜色进行切换
        mRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getActivity(), R.color.colorPrimary), Color.RED, Color.YELLOW, Color.BLUE);

        //设置触发刷新的距离
        mRefreshLayout.setDistanceToTriggerSync(200);
        //如果child是自己自定义的view，可以通过这个回调，告诉mSwipeRefreshLayoutchild是否可以滑动
        mRefreshLayout.setOnChildScrollUpCallback(null);


        systemInfoPresenterImp = new SystemInfoPresenterImp(this, getActivity());

        noticeAdapter = new MyNoticeAdapter(getActivity(), null);
        mNoticeListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mNoticeListView.setAdapter(noticeAdapter);

        noticeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Logger.i("my comment pos--->" + position);
            }
        });

        systemInfoPresenterImp.getSystemInfoList(App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "", 3, currentPage);
        noticeAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                currentPage++;
                systemInfoPresenterImp.getSystemInfoList(App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "", 3, currentPage);
            }
        }, mNoticeListView);
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
    public void loadDataSuccess(SystemInfoRet tData) {
        Logger.i(JSONObject.toJSONString(tData));
        if (tData != null && tData.getCode() == Constants.SUCCESS) {
            noDataLayout.setVisibility(View.GONE);
            mNoticeListView.setVisibility(View.VISIBLE);
            if (currentPage == 1) {
                noticeAdapter.setNewData(tData.getData());
            } else {
                noticeAdapter.addData(tData.getData());
            }

            if (tData.getData().size() == pageSize) {
                noticeAdapter.loadMoreComplete();
            } else {
                noticeAdapter.loadMoreEnd(true);
            }
        } else {
            noDataLayout.setVisibility(View.VISIBLE);
            mNoticeListView.setVisibility(View.GONE);
        }
    }

    @Override
    public void loadDataError(Throwable throwable) {
        avi.hide();
        mRefreshLayout.setRefreshing(false);
        noDataLayout.setVisibility(View.VISIBLE);
        mNoticeListView.setVisibility(View.GONE);
    }

    @Override
    public void onRefresh() {
        mRefreshLayout.setRefreshing(true);
        currentPage = 1;
        systemInfoPresenterImp.getSystemInfoList(App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "", 3, currentPage);
    }
}
