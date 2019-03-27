package com.feiyou.headstyle.ui.fragment.sub;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feiyou.headstyle.App;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.FollowInfoRet;
import com.feiyou.headstyle.bean.MessageEvent;
import com.feiyou.headstyle.bean.ResultInfo;
import com.feiyou.headstyle.bean.UserInfo;
import com.feiyou.headstyle.bean.UserInfoListRet;
import com.feiyou.headstyle.common.Constants;
import com.feiyou.headstyle.presenter.FollowInfoPresenterImp;
import com.feiyou.headstyle.presenter.UserInfoListPresenterImp;
import com.feiyou.headstyle.ui.activity.MyFollowActivity;
import com.feiyou.headstyle.ui.adapter.MyFriendsListAdapter;
import com.feiyou.headstyle.ui.base.BaseFragment;
import com.feiyou.headstyle.ui.custom.NormalDecoration;
import com.feiyou.headstyle.utils.MyToastUtils;
import com.feiyou.headstyle.view.UserInfoListView;
import com.orhanobut.logger.Logger;
import com.wang.avi.AVLoadingIndicatorView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by myflying on 2018/11/26.
 */
public class MyFriendsFragment extends BaseFragment implements UserInfoListView {

    @BindView(R.id.avi)
    AVLoadingIndicatorView avi;

    @BindView(R.id.my_friends_list)
    RecyclerView mFriendsListView;

    @BindView(R.id.layout_no_data)
    LinearLayout mNoDataLayout;

    @BindView(R.id.tv_no_data_title)
    TextView mNoDataTitleTv;

    @BindView(R.id.tv_no_data)
    TextView mNoDataToTv;

    MyFriendsListAdapter myFriendsListAdapter;

    UserInfoListPresenterImp userInfoListPresenterImp;

    FollowInfoPresenterImp followInfoPresenterImp;

    private int currentPage = 1;

    private int pageSize = 30;

    private boolean isMyInfo;

    private UserInfo userInfo;

    private String intoUserId;

    MyFollowActivity myFollowActivity;

    private int currentPosition;

    private Handler handler = new Handler();

    private View rootView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected View onCreateView() {
        if (rootView == null) {
            rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_my_friends, null);
            ButterKnife.bind(this, rootView);
            initData();
        }
        return rootView;
    }

    public void initData() {
        myFollowActivity = (MyFollowActivity) getActivity();
        userInfo = App.getApp().getmUserInfo();

        isMyInfo = myFollowActivity.isMyInfo();
        intoUserId = StringUtils.isEmpty(myFollowActivity.getIntoUserId()) ? userInfo.getId() : myFollowActivity.getIntoUserId();
        Logger.i("friend_type--->" + isMyInfo + "--->" + intoUserId);

        followInfoPresenterImp = new FollowInfoPresenterImp(this, getActivity());
        userInfoListPresenterImp = new UserInfoListPresenterImp(this, getActivity());

        myFriendsListAdapter = new MyFriendsListAdapter(getActivity(), null);
        mFriendsListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mFriendsListView.setAdapter(myFriendsListAdapter);
        mFriendsListView.addItemDecoration(new NormalDecoration(ContextCompat.getColor(getActivity(), R.color.line_color), 1));
        myFriendsListAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                currentPage++;
                userInfoListPresenterImp.getMyGuanFenList(currentPage, userInfo != null ? userInfo.getId() : "", isMyInfo ? userInfo.getId() : intoUserId, 1);
            }
        }, mFriendsListView);
        myFriendsListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.layout_follow) {
                    currentPosition = position;
                    followInfoPresenterImp.addFollow(App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "", myFriendsListAdapter.getData().get(position).getId());
                }
            }
        });

        userInfoListPresenterImp.getMyGuanFenList(currentPage, userInfo != null ? userInfo.getId() : "", isMyInfo ? userInfo.getId() : intoUserId, 1);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(MessageEvent messageEvent) {
        if (messageEvent.getMessage().equals("load_friend_list")) {
            userInfoListPresenterImp.getMyGuanFenList(currentPage, userInfo != null ? userInfo.getId() : "", isMyInfo ? userInfo.getId() : intoUserId, 1);
        }
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void dismissProgress() {
        avi.hide();
    }

    @Override
    public void loadDataSuccess(ResultInfo tData) {
        avi.hide();
        Logger.i("result data --->" + JSON.toJSONString(tData));

        if (tData != null && tData.getCode() == Constants.SUCCESS) {
            mFriendsListView.setVisibility(View.VISIBLE);
            mNoDataLayout.setVisibility(View.GONE);

            if (tData instanceof UserInfoListRet) {
                if (currentPage == 1) {
                    myFriendsListAdapter.setNewData(((UserInfoListRet) tData).getData());
                } else {
                    myFriendsListAdapter.addData(((UserInfoListRet) tData).getData());
                }

                if (((UserInfoListRet) tData).getData().size() == pageSize) {
                    myFriendsListAdapter.loadMoreComplete();
                } else {
                    myFriendsListAdapter.loadMoreEnd(true);
                }
            }
            if (tData instanceof FollowInfoRet) {
                int tempResult = ((FollowInfoRet) tData).getData().getIsGuan();

//                if (tempResult == 0) {
//                    MyToastUtils.showToast(getActivity(), 1, "已取消");
//                } else {
//                    MyToastUtils.showToast(getActivity(), 0, "关注成功");
//                }

                userInfoListPresenterImp.getMyGuanFenList(currentPage, userInfo != null ? userInfo.getId() : "", isMyInfo ? userInfo.getId() : intoUserId, 1);
            }
        } else {
            if (tData instanceof UserInfoListRet) {
                mFriendsListView.setVisibility(View.GONE);
                mNoDataLayout.setVisibility(View.VISIBLE);
                //mNoDataTitleTv.setText(type == 1 ? "还没有关注的好友？不如去多认识几个朋友！" : "还没收到过关注？不如发个帖求波关注！");
                //mNoDataToTv.setText(type == 1 ? "去关注" : "去发帖");
            }
            Logger.i(StringUtils.isEmpty(tData.getMsg()) ? "操作失败" : tData.getMsg());
        }
    }

    @Override
    public void loadDataError(Throwable throwable) {
        avi.hide();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
