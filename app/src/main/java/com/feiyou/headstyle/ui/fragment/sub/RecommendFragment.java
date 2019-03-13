package com.feiyou.headstyle.ui.fragment.sub;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feiyou.headstyle.App;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.FollowInfoRet;
import com.feiyou.headstyle.bean.HeadInfo;
import com.feiyou.headstyle.bean.HeadType;
import com.feiyou.headstyle.bean.MessageEvent;
import com.feiyou.headstyle.bean.NoteInfo;
import com.feiyou.headstyle.bean.NoteInfoRet;
import com.feiyou.headstyle.bean.ResultInfo;
import com.feiyou.headstyle.bean.UserInfo;
import com.feiyou.headstyle.bean.ZanResultRet;
import com.feiyou.headstyle.common.Constants;
import com.feiyou.headstyle.presenter.AddZanPresenterImp;
import com.feiyou.headstyle.presenter.FollowInfoPresenterImp;
import com.feiyou.headstyle.presenter.NoteDataPresenterImp;
import com.feiyou.headstyle.ui.activity.CommunityArticleActivity;
import com.feiyou.headstyle.ui.activity.CommunityTypeActivity;
import com.feiyou.headstyle.ui.activity.PushNoteActivity;
import com.feiyou.headstyle.ui.activity.UserInfoActivity;
import com.feiyou.headstyle.ui.adapter.NoteInfoAdapter;
import com.feiyou.headstyle.ui.adapter.TopicAdapter;
import com.feiyou.headstyle.ui.base.BaseFragment;
import com.feiyou.headstyle.ui.custom.GlideRoundTransform;
import com.feiyou.headstyle.ui.custom.LoginDialog;
import com.feiyou.headstyle.view.NoteDataView;
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
public class RecommendFragment extends BaseFragment implements NoteDataView, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mRefreshLayout;

    @BindView(R.id.recommend_list)
    RecyclerView mRecommendListView;

    @BindView(R.id.layout_no_data)
    LinearLayout noDataLayout;

    @BindView(R.id.tv_no_data)
    TextView mNoDataTiltTv;

    @BindView(R.id.tv_follow_user)
    TextView mFollowUserTv;

    @BindView(R.id.avi)
    AVLoadingIndicatorView avi;

    @BindView(R.id.fab)
    FloatingActionButton mFabButton;

    View topView;

    RecyclerView mTopicListView;

    TopicAdapter topicAdapter;

    NoteInfoAdapter noteInfoAdapter;

    private NoteDataPresenterImp noteDataPresenterImp;

    private FollowInfoPresenterImp followInfoPresenterImp;

    private AddZanPresenterImp addZanPresenterImp;

    private int currentPage = 1;

    private int pageSize = 10;

    private int currentClickIndex;

    public static RecommendFragment getInstance() {
        return new RecommendFragment();
    }

    private UserInfo userInfo;

    LoginDialog loginDialog;

    private int communityType = 1;

    public static RecommendFragment newInstance(int type) {
        RecommendFragment fragment = new RecommendFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("community_type", type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected View onCreateView() {
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_tab_recommend, null);
        ButterKnife.bind(this, root);
        initData();
        return root;
    }

    public void initData() {
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

        Bundle bundle = getArguments();

        if (bundle != null && bundle.getInt("community_type") > 0) {
            communityType = bundle.getInt("community_type");
        }

        Logger.i("communityType" + communityType);

        noteInfoAdapter = new NoteInfoAdapter(getActivity(), null, 1);
        mRecommendListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecommendListView.setAdapter(noteInfoAdapter);

        if (communityType == 2) {
            //顶部view
            topView = LayoutInflater.from(getActivity()).inflate(R.layout.recommend_top, null);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(80));
            topView.setLayoutParams(params);

            mTopicListView = topView.findViewById(R.id.topic_list_view);
            topicAdapter = new TopicAdapter(getActivity(), null);
            mTopicListView.setLayoutManager(new GridLayoutManager(getActivity(), 5));
            mTopicListView.setAdapter(topicAdapter);
            topicAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    Intent intent = new Intent(getActivity(), CommunityTypeActivity.class);
                    intent.putExtra("topic_id", topicAdapter.getData().get(position).getId());
                    startActivity(intent);
                }
            });
            noteInfoAdapter.setHeaderView(topView);
        }

        CoordinatorLayout.LayoutParams listParams = new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.MATCH_PARENT);
        listParams.setMargins(0, 0, 0, SizeUtils.dp2px(48));
        mRefreshLayout.setLayoutParams(listParams);

        noteInfoAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getActivity(), CommunityArticleActivity.class);
                intent.putExtra("msg_id", noteInfoAdapter.getData().get(position).getId());
                startActivity(intent);
            }
        });

        noteInfoAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (!App.getApp().isLogin) {
                    if (loginDialog != null && !loginDialog.isShowing()) {
                        loginDialog.show();
                    }
                    return;
                }

                currentClickIndex = position;
                if (view.getId() == R.id.layout_follow) {
                    followInfoPresenterImp.addFollow(App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "", noteInfoAdapter.getData().get(position).getUserId());
                }
                if (view.getId() == R.id.layout_item_zan) {
                    String messageId = noteInfoAdapter.getData().get(position).getId();
                    addZanPresenterImp.addZan(1, App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "", noteInfoAdapter.getData().get(position).getUserId(), messageId, "", "", 1);
                }
                if (view.getId() == R.id.iv_user_head) {
                    if (view.getId() == R.id.iv_user_head) {
                        Intent intent = new Intent(getActivity(), UserInfoActivity.class);
                        intent.putExtra("user_id", noteInfoAdapter.getData().get(position).getUserId());
                        startActivity(intent);
                    }
                }
            }
        });

        noteInfoAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener()

        {
            @Override
            public void onLoadMoreRequested() {
                currentPage++;
                noteDataPresenterImp.getNoteData(currentPage, communityType, userInfo != null ? userInfo.getId() : "");
            }
        }, mRecommendListView);

        mRecommendListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

        loginDialog = new LoginDialog(getActivity(), R.style.login_dialog);
        noteDataPresenterImp = new NoteDataPresenterImp(this, getActivity());
        followInfoPresenterImp = new FollowInfoPresenterImp(this, getActivity());
        addZanPresenterImp = new AddZanPresenterImp(this, getActivity());

        noteDataPresenterImp.getNoteData(currentPage, communityType, userInfo != null ? userInfo.getId() : "");
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!StringUtils.isEmpty(SPUtils.getInstance().getString(Constants.USER_INFO))) {
            Logger.i(SPUtils.getInstance().getString(Constants.USER_INFO));
            userInfo = JSON.parseObject(SPUtils.getInstance().getString(Constants.USER_INFO), new TypeReference<UserInfo>() {
            });
        }

        if (loginDialog != null && loginDialog.isShowing()) {
            loginDialog.dismiss();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(MessageEvent messageEvent) {
        if (messageEvent.getMessage().equals("login_success")) {
            onResume();
        } else {
            if (communityType == 2) {
                topicAdapter.setNewData(App.topicInfoList);
            }
        }
    }

    @OnClick(R.id.fab)
    void fabButton() {
        if (!App.getApp().isLogin) {
            if (loginDialog != null && !loginDialog.isShowing()) {
                loginDialog.show();
            }
            return;
        }

        Intent intent = new Intent(getActivity(), PushNoteActivity.class);
        startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void dismissProgress() {
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void loadDataSuccess(ResultInfo tData) {
        avi.hide();
        mRefreshLayout.setRefreshing(false);
        if (tData != null && tData.getCode() == Constants.SUCCESS) {
            if (tData instanceof NoteInfoRet) {
                if (((NoteInfoRet) tData).getData() != null && ((NoteInfoRet) tData).getData().size() > 0) {
                    mRecommendListView.setVisibility(View.VISIBLE);
                    noDataLayout.setVisibility(View.GONE);
                    if (currentPage == 1) {
                        noteInfoAdapter.setNewData(((NoteInfoRet) tData).getData());
                    } else {
                        noteInfoAdapter.addData(((NoteInfoRet) tData).getData());
                    }

                    if (((NoteInfoRet) tData).getData().size() == pageSize) {
                        noteInfoAdapter.loadMoreComplete();
                    } else {
                        noteInfoAdapter.loadMoreEnd();
                    }
                } else {
                    mRecommendListView.setVisibility(View.GONE);
                    noDataLayout.setVisibility(View.VISIBLE);
                    mNoDataTiltTv.setText(communityType == 1 ? "暂无数据" : "还没有关注任何人");
                }

            }

            if (tData instanceof FollowInfoRet) {
                if (((FollowInfoRet) tData).getData() != null) {
                    ToastUtils.showLong(((FollowInfoRet) tData).getData().getIsGuan() == 0 ? "已取消" : "已关注");
                    noteInfoAdapter.getData().get(currentClickIndex).setIsGuan(((FollowInfoRet) tData).getData().getIsGuan());
                    noteInfoAdapter.notifyDataSetChanged();
                } else {
                    ToastUtils.showLong(StringUtils.isEmpty(tData.getMsg()) ? "操作错误" : tData.getMsg());
                }
            }

            if (tData instanceof ZanResultRet) {
                int tempNum = noteInfoAdapter.getData().get(currentClickIndex).getZanNum();
                if (((ZanResultRet) tData).getData().getIsZan() == 0) {
                    tempNum = tempNum - 1;
                } else {
                    tempNum = tempNum + 1;
                }

                noteInfoAdapter.getData().get(currentClickIndex).setZanNum(tempNum);
                noteInfoAdapter.getData().get(currentClickIndex).setIsZan(((ZanResultRet) tData).getData().getIsZan());
                noteInfoAdapter.notifyDataSetChanged();
            }

        } else {
            if (tData instanceof NoteInfoRet) {
                mRecommendListView.setVisibility(View.GONE);
                noDataLayout.setVisibility(View.VISIBLE);
                mNoDataTiltTv.setText(communityType == 1 ? "暂无数据" : "还没有关注任何人");
            }

            if (tData instanceof FollowInfoRet) {
                ToastUtils.showLong(StringUtils.isEmpty(tData.getMsg()) ? "操作错误" : tData.getMsg());
            } else {
                Logger.i("error--->" + tData.getMsg());
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
        currentPage = 1;
        noteDataPresenterImp.getNoteData(currentPage, communityType, userInfo != null ? userInfo.getId() : "");
    }
}
