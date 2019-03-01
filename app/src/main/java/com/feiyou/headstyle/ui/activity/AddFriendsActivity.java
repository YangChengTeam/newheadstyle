package com.feiyou.headstyle.ui.activity;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feiyou.headstyle.App;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.FollowInfoRet;
import com.feiyou.headstyle.bean.HeadInfoRet;
import com.feiyou.headstyle.bean.ResultInfo;
import com.feiyou.headstyle.bean.UserInfoListRet;
import com.feiyou.headstyle.common.Constants;
import com.feiyou.headstyle.presenter.FollowInfoPresenterImp;
import com.feiyou.headstyle.presenter.UserInfoListPresenterImp;
import com.feiyou.headstyle.ui.adapter.AddFriendsListAdapter;
import com.feiyou.headstyle.ui.adapter.BlackListAdapter;
import com.feiyou.headstyle.ui.base.BaseFragmentActivity;
import com.feiyou.headstyle.ui.custom.NormalDecoration;
import com.feiyou.headstyle.view.UserInfoListView;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by myflying on 2018/11/23.
 */
public class AddFriendsActivity extends BaseFragmentActivity implements UserInfoListView {

    @BindView(R.id.topbar)
    QMUITopBar mTopBar;

    @BindView(R.id.et_key_word)
    EditText mHotWordEditText;

    ImageView mBackImageView;

    @BindView(R.id.friends_list)
    RecyclerView mFriendsListView;

    AddFriendsListAdapter addFriendsListAdapter;

    UserInfoListPresenterImp userInfoListPresenterImp;

    FollowInfoPresenterImp followInfoPresenterImp;

    private int currentPage = 1;

    private int pageSize = 30;

    private int searchPage = 1;

    String keyWord;

    private int currentPosition;

    @Override
    protected int getContextViewId() {
        return R.layout.activity_add_friends;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTopBar();
        initData();
    }

    private void initTopBar() {
        QMUIStatusBarHelper.setStatusBarLightMode(this);
        View topSearchView = getLayoutInflater().inflate(R.layout.common_top_back, null);
        topSearchView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(48)));
        TextView titleTv = topSearchView.findViewById(R.id.tv_title);
        titleTv.setText("添加好友");

        mTopBar.setCenterView(topSearchView);
        mBackImageView = topSearchView.findViewById(R.id.iv_back);
        mBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });
    }

    public void initData() {
        initProgress("搜索中");
        mHotWordEditText.setOnEditorActionListener(new EditorActionListener());

        addFriendsListAdapter = new AddFriendsListAdapter(this, null);
        mFriendsListView.setLayoutManager(new LinearLayoutManager(this));
        mFriendsListView.setAdapter(addFriendsListAdapter);
        mFriendsListView.addItemDecoration(new NormalDecoration(ContextCompat.getColor(this, R.color.line_color), 1));
        addFriendsListAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (StringUtils.isEmpty(keyWord)) {
                    currentPage++;
                    userInfoListPresenterImp.addFriendsList(currentPage);
                } else {
                    searchPage++;
                    userInfoListPresenterImp.searchFriendsList(searchPage, keyWord);
                }
            }
        }, mFriendsListView);

        addFriendsListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                currentPosition = position;
                followInfoPresenterImp.addFollow(App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "", addFriendsListAdapter.getData().get(position).getId());
            }
        });

        userInfoListPresenterImp = new UserInfoListPresenterImp(this, this);
        followInfoPresenterImp = new FollowInfoPresenterImp(this, this);
        userInfoListPresenterImp.addFriendsList(currentPage);
    }

    private class EditorActionListener implements TextView.OnEditorActionListener {
        @Override
        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            switch (textView.getId()) {
                case R.id.et_key_word:
                    if (i == EditorInfo.IME_ACTION_SEARCH) {
                        if (StringUtils.isEmpty(textView.getText())) {
                            ToastUtils.showLong("请输入关键词后搜索");
                            break;
                        }
                        showDialog();
                        keyWord = textView.getText().toString();
                        userInfoListPresenterImp.searchFriendsList(searchPage, keyWord);

                        KeyboardUtils.hideSoftInput(AddFriendsActivity.this);
                    }
                    break;
                default:
                    break;
            }
            return false;
        }
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
        dismissDialog();
    }

    @Override
    public void loadDataSuccess(ResultInfo tData) {
        dismissDialog();
        if (tData != null && tData.getCode() == Constants.SUCCESS) {
            if (tData instanceof UserInfoListRet) {
                if (StringUtils.isEmpty(keyWord)) {
                    if (currentPage == 1) {
                        addFriendsListAdapter.setNewData(((UserInfoListRet) tData).getData());
                    } else {
                        addFriendsListAdapter.addData(((UserInfoListRet) tData).getData());
                    }

                    if (((UserInfoListRet) tData).getData().size() == pageSize) {
                        addFriendsListAdapter.loadMoreComplete();
                    } else {
                        addFriendsListAdapter.loadMoreEnd();
                    }
                } else {
                    if (searchPage == 1) {
                        addFriendsListAdapter.setNewData(((UserInfoListRet) tData).getData());
                    } else {
                        addFriendsListAdapter.addData(((UserInfoListRet) tData).getData());
                    }

                    if (((UserInfoListRet) tData).getData().size() == pageSize) {
                        addFriendsListAdapter.loadMoreComplete();
                    } else {
                        addFriendsListAdapter.loadMoreEnd();
                    }
                }
            }
            if (tData instanceof FollowInfoRet) {
                int tempResult = ((FollowInfoRet) tData).getData().getIsGuan();
                ToastUtils.showLong(tempResult == 0 ? "已取消" : "已关注");
                addFriendsListAdapter.getData().get(currentPosition).setFollow(tempResult == 0 ? false : true);
                addFriendsListAdapter.notifyDataSetChanged();
            }
        } else {
            ToastUtils.showLong(StringUtils.isEmpty(tData.getMsg()) ? "操作失败" : tData.getMsg());
        }
    }

    @Override
    public void loadDataError(Throwable throwable) {
        dismissDialog();
    }
}