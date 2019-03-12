package com.feiyou.headstyle.ui.fragment.sub;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feiyou.headstyle.App;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.FollowInfoRet;
import com.feiyou.headstyle.bean.ResultInfo;
import com.feiyou.headstyle.bean.UserInfoListRet;
import com.feiyou.headstyle.common.Constants;
import com.feiyou.headstyle.presenter.FollowInfoPresenterImp;
import com.feiyou.headstyle.presenter.UserInfoListPresenterImp;
import com.feiyou.headstyle.ui.adapter.AddFriendsListAdapter;
import com.feiyou.headstyle.ui.adapter.MyFriendsListAdapter;
import com.feiyou.headstyle.ui.base.BaseFragment;
import com.feiyou.headstyle.ui.custom.NormalDecoration;
import com.feiyou.headstyle.utils.MyToastUtils;
import com.feiyou.headstyle.view.UserInfoListView;
import com.orhanobut.logger.Logger;
import com.wang.avi.AVLoadingIndicatorView;

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

    private int currentPosition;

    private int type;

    public static MyFriendsFragment newInstance(int type) {
        MyFriendsFragment fragment = new MyFriendsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("friend_type", type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected View onCreateView() {
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_my_friends, null);
        ButterKnife.bind(this, root);
        initData();
        return root;
    }

    public void initData() {

        Bundle bundle = getArguments();
        if (bundle != null && bundle.getInt("friend_type") > 0) {
            type = bundle.getInt("friend_type");
        }

        Logger.i("friend_type--->" + type);

        myFriendsListAdapter = new MyFriendsListAdapter(getActivity(), null);
        mFriendsListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mFriendsListView.setAdapter(myFriendsListAdapter);
        mFriendsListView.addItemDecoration(new NormalDecoration(ContextCompat.getColor(getActivity(), R.color.line_color), 1));
        myFriendsListAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                currentPage++;
                userInfoListPresenterImp.getMyGuanFenList(currentPage, App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "", type);
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

        followInfoPresenterImp = new FollowInfoPresenterImp(this, getActivity());
        userInfoListPresenterImp = new UserInfoListPresenterImp(this, getActivity());
        userInfoListPresenterImp.getMyGuanFenList(currentPage, App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "", type);
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
                    myFriendsListAdapter.loadMoreEnd();
                }
            }
            if (tData instanceof FollowInfoRet) {
                int tempResult = ((FollowInfoRet) tData).getData().getIsGuan();
                //ToastUtils.showLong(tempResult == 0 ? "已取消" : "已关注");

                if (tempResult == 0) {
                    MyToastUtils.showToast(getActivity(), 1, "已取消");
                } else {
                    MyToastUtils.showToast(getActivity(), 0, "关注成功");
                }

                if (myFriendsListAdapter.getData().size() > 0) {
                    myFriendsListAdapter.getData().remove(0);
                }
                myFriendsListAdapter.notifyDataSetChanged();
            }
        } else {
            if (tData instanceof UserInfoListRet) {
                mFriendsListView.setVisibility(View.GONE);
                mNoDataLayout.setVisibility(View.VISIBLE);
                //mNoDataTitleTv.setText(type == 1 ? "还没有关注的好友？不如去多认识几个朋友！" : "还没收到过关注？不如发个帖求波关注！");
                //mNoDataToTv.setText(type == 1 ? "去关注" : "去发帖");
            }
            ToastUtils.showLong(StringUtils.isEmpty(tData.getMsg()) ? "操作失败" : tData.getMsg());
        }
    }

    @Override
    public void loadDataError(Throwable throwable) {
        avi.hide();
    }
}
