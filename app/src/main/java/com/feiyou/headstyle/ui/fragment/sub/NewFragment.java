package com.feiyou.headstyle.ui.fragment.sub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feiyou.headstyle.App;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.FollowInfoRet;
import com.feiyou.headstyle.bean.NoteTypeRet;
import com.feiyou.headstyle.bean.ResultInfo;
import com.feiyou.headstyle.bean.ZanResultRet;
import com.feiyou.headstyle.common.Constants;
import com.feiyou.headstyle.presenter.AddZanPresenterImp;
import com.feiyou.headstyle.presenter.FollowInfoPresenterImp;
import com.feiyou.headstyle.presenter.NoteTypePresenterImp;
import com.feiyou.headstyle.ui.activity.CommunityArticleActivity;
import com.feiyou.headstyle.ui.adapter.NoteInfoAdapter;
import com.feiyou.headstyle.ui.base.BaseFragment;
import com.feiyou.headstyle.ui.custom.LoginDialog;
import com.feiyou.headstyle.view.NoteTypeView;
import com.wang.avi.AVLoadingIndicatorView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by myflying on 2018/12/24.
 */
public class NewFragment extends BaseFragment implements NoteTypeView {

    @BindView(R.id.news_list)
    RecyclerView mNewsListView;

    @BindView(R.id.avi)
    AVLoadingIndicatorView avi;

    NoteInfoAdapter noteInfoAdapter;

    private NoteTypePresenterImp noteTypePresenterImp;

    private String topicId;

    private int currentPage = 1;

    private int pageSize = 30;

    private int queryType = 1;

    LoginDialog loginDialog;

    private int currentClickIndex;

    private FollowInfoPresenterImp followInfoPresenterImp;

    private AddZanPresenterImp addZanPresenterImp;

    public static NewFragment newInstance(String topId, int type) {
        NewFragment fragment = new NewFragment();
        Bundle bundle = new Bundle();
        bundle.putString("topic_id", topId);
        bundle.putInt("query_type", type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected View onCreateView() {
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_new, null);
        ButterKnife.bind(this, root);
        initData();
        return root;
    }

    public void initData() {
        Bundle bundle = getActivity().getIntent().getExtras();
        if (bundle != null && !StringUtils.isEmpty(bundle.getString("topic_id"))) {
            topicId = bundle.getString("topic_id");
        }

        if (bundle != null && bundle.getInt("query_type") > 0) {
            queryType = bundle.getInt("query_type");
        }

        noteInfoAdapter = new NoteInfoAdapter(getActivity(), null, 1);
        mNewsListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mNewsListView.setAdapter(noteInfoAdapter);

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
                currentClickIndex = position;

                if (!App.getApp().isLogin) {
                    if (loginDialog != null && !loginDialog.isShowing()) {
                        loginDialog.show();
                    }
                    return;
                }

                if (view.getId() == R.id.layout_follow) {
                    followInfoPresenterImp.addFollow(App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "", noteInfoAdapter.getData().get(position).getUserId());
                }
                if (view.getId() == R.id.layout_item_zan) {
                    String messageId = noteInfoAdapter.getData().get(position).getId();
                    addZanPresenterImp.addZan(1, App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "", noteInfoAdapter.getData().get(position).getUserId(), messageId, "", "", 1);
                }
            }
        });

        noteInfoAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener()
        {
            @Override
            public void onLoadMoreRequested() {
                currentPage++;
                noteTypePresenterImp.getNoteTypeData(topicId, currentPage, queryType, App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "");
            }
        }, mNewsListView);

        loginDialog = new LoginDialog(getActivity(), R.style.login_dialog);
        avi.show();

        noteTypePresenterImp = new NoteTypePresenterImp(this, getActivity());
        followInfoPresenterImp = new FollowInfoPresenterImp(this, getActivity());
        addZanPresenterImp = new AddZanPresenterImp(this, getActivity());

        noteTypePresenterImp.getNoteTypeData(topicId, currentPage, queryType, App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "");
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
        if (tData != null && tData.getCode() == Constants.SUCCESS) {
            if (tData instanceof NoteTypeRet) {
                if (((NoteTypeRet) tData).getData() != null && ((NoteTypeRet) tData).getData().getList() != null) {
                    if (currentPage == 1) {
                        noteInfoAdapter.setNewData(((NoteTypeRet) tData).getData().getList());
                    } else {
                        noteInfoAdapter.addData(((NoteTypeRet) tData).getData().getList());
                    }
                }

                if (((NoteTypeRet) tData).getData() != null && ((NoteTypeRet) tData).getData().getList().size() == pageSize) {
                    noteInfoAdapter.loadMoreComplete();
                } else {
                    noteInfoAdapter.loadMoreEnd();
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
            ToastUtils.showLong(StringUtils.isEmpty(tData.getMsg()) ? "操作错误" : tData.getMsg());
        }
    }

    @Override
    public void loadDataError(Throwable throwable) {

    }
}
