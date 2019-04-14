package com.feiyou.headstyle.ui.fragment.sub;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.dingmouren.layoutmanagergroup.viewpager.OnViewPagerListener;
import com.dingmouren.layoutmanagergroup.viewpager.ViewPagerLayoutManager;
import com.feiyou.headstyle.App;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.MessageEvent;
import com.feiyou.headstyle.bean.ResultInfo;
import com.feiyou.headstyle.bean.UserInfo;
import com.feiyou.headstyle.bean.VideoInfo;
import com.feiyou.headstyle.bean.VideoInfoRet;
import com.feiyou.headstyle.common.Constants;
import com.feiyou.headstyle.presenter.VideoInfoPresenterImp;
import com.feiyou.headstyle.ui.activity.VideoItemShowActivity;
import com.feiyou.headstyle.ui.activity.VideoShowActivity;
import com.feiyou.headstyle.ui.adapter.VideoListAdapter;
import com.feiyou.headstyle.ui.base.BaseFragment;
import com.feiyou.headstyle.view.VideoInfoView;
import com.orhanobut.logger.Logger;
import com.wang.avi.AVLoadingIndicatorView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by myflying on 2018/11/26.
 */
public class VideoFragment extends BaseFragment implements VideoInfoView, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mRefreshLayout;

    @BindView(R.id.video_list)
    RecyclerView mVideoListView;

    @BindView(R.id.layout_no_data)
    LinearLayout noDataLayout;

    @BindView(R.id.tv_reload)
    TextView mTvReload;

    @BindView(R.id.avi)
    AVLoadingIndicatorView avi;

    VideoListAdapter videoListAdapter;

    private VideoInfoPresenterImp videoInfoPresenterImp;

    private int randomPage;

    private int pageSize = 30;

    private UserInfo userInfo;

    private View rootView;

    private int currentPage = 1;

    public static VideoFragment getInstance() {
        return new VideoFragment();
    }

    @Override
    protected View onCreateView() {
        if (rootView == null) {
            rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_tab_video, null);
            ButterKnife.bind(this, rootView);
            initViews();
        }
        return rootView;
    }

    public void initViews() {
        Logger.i("video info init view--->");
        userInfo = App.getApp().getmUserInfo();

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

        videoInfoPresenterImp = new VideoInfoPresenterImp(this, getActivity());

        videoListAdapter = new VideoListAdapter(getActivity(), null);
        mVideoListView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mVideoListView.setAdapter(videoListAdapter);

        mVideoListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int topRowVerticalPosition = (recyclerView == null || recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();
                mRefreshLayout.setEnabled(topRowVerticalPosition >= 0);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        videoListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                Logger.i("video_id--->" + videoListAdapter.getData().get(position).getId());

                int jumpPage = randomPage + position / pageSize;

                int jumpPosition = position % pageSize;

                Intent intent = new Intent(getActivity(), VideoItemShowActivity.class);
                intent.putExtra("jump_page", jumpPage);
                intent.putExtra("jump_position", jumpPosition);
                startActivity(intent);
            }
        });

        videoListAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                currentPage++;
                videoInfoPresenterImp.getDataList(currentPage, userInfo != null ? userInfo.getId() : "");
            }
        }, mVideoListView);

        videoInfoPresenterImp.getDataList(currentPage, userInfo != null ? userInfo.getId() : "");
    }

    @OnClick(R.id.tv_reload)
    void reload() {
        onRefresh();
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
            mVideoListView.setVisibility(View.VISIBLE);
            noDataLayout.setVisibility(View.GONE);
            if (tData instanceof VideoInfoRet) {
                if (currentPage == 0) {
                    randomPage = ((VideoInfoRet) tData).getData().getPage();
                    currentPage = randomPage;

                    if (((VideoInfoRet) tData).getData().getList() != null) {
                        videoListAdapter.setNewData(((VideoInfoRet) tData).getData().getList());
                    }
                } else {
                    if (((VideoInfoRet) tData).getData().getList() != null) {
                        videoListAdapter.addData(((VideoInfoRet) tData).getData().getList());
                    }
                }

                if (((VideoInfoRet) tData).getData().getList().size() == pageSize) {
                    videoListAdapter.loadMoreComplete();
                } else {
                    videoListAdapter.loadMoreEnd(true);
                }
            }
        } else {
            if (currentPage == 1) {
                mVideoListView.setVisibility(View.GONE);
                noDataLayout.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void loadDataError(Throwable throwable) {
        avi.hide();
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        mRefreshLayout.setRefreshing(true);
        currentPage = 0;
        videoInfoPresenterImp.getDataList(currentPage, userInfo != null ? userInfo.getId() : "");
    }

}
