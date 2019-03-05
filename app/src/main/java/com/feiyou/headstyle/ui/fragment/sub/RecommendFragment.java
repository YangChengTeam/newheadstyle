package com.feiyou.headstyle.ui.fragment.sub;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

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
import com.feiyou.headstyle.ui.adapter.NoteInfoAdapter;
import com.feiyou.headstyle.ui.adapter.TopicAdapter;
import com.feiyou.headstyle.ui.base.BaseFragment;
import com.feiyou.headstyle.ui.custom.GlideRoundTransform;
import com.feiyou.headstyle.ui.custom.LoginDialog;
import com.feiyou.headstyle.view.NoteDataView;
import com.orhanobut.logger.Logger;

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
public class RecommendFragment extends BaseFragment implements NoteDataView {

    @BindView(R.id.recommend_list)
    RecyclerView mRecommendListView;

    View topView;

    RecyclerView mTopicListView;

    TopicAdapter topicAdapter;

    NoteInfoAdapter noteInfoAdapter;

    private NoteDataPresenterImp noteDataPresenterImp;

    private FollowInfoPresenterImp followInfoPresenterImp;

    private AddZanPresenterImp addZanPresenterImp;

    private int currentPage = 1;

    private int pageSize = 20;

    private int currentClickIndex;

    public static RecommendFragment getInstance() {
        return new RecommendFragment();
    }

    private UserInfo userInfo;

    LoginDialog loginDialog;

    @Override
    protected View onCreateView() {
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_tab_recommend, null);
        ButterKnife.bind(this, root);
        initData();
        return root;
    }

    public void initData() {
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

        noteInfoAdapter = new NoteInfoAdapter(getActivity(), null, 1);
        mRecommendListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecommendListView.setAdapter(noteInfoAdapter);
        noteInfoAdapter.setHeaderView(topView);

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
                if (App.getApp().isLogin) {
                    if (view.getId() == R.id.layout_follow) {
                        followInfoPresenterImp.addFollow(App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "", noteInfoAdapter.getData().get(position).getUserId());
                    }
                    if (view.getId() == R.id.layout_item_zan) {
                        String messageId = noteInfoAdapter.getData().get(position).getId();
                        addZanPresenterImp.addZan(1, App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "", noteInfoAdapter.getData().get(position).getUserId(), messageId, "", "", 1);
                    }
                } else {
                    if (loginDialog != null && !loginDialog.isShowing()) {
                        loginDialog.show();
                    }
                }
            }
        });

        noteInfoAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener()

        {
            @Override
            public void onLoadMoreRequested() {
                currentPage++;
                noteDataPresenterImp.getNoteData(currentPage, 2, "");
            }
        }, mRecommendListView);

        loginDialog = new

                LoginDialog(getActivity(), R.style.login_dialog);

        noteDataPresenterImp = new NoteDataPresenterImp(this, getActivity());
        followInfoPresenterImp = new FollowInfoPresenterImp(this, getActivity());
        addZanPresenterImp = new AddZanPresenterImp(this, getActivity());
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

        //noteDataPresenterImp.getNoteData(currentPage, 2, userInfo != null ? userInfo.getId() : "");
        noteDataPresenterImp.getNoteData(currentPage, 2, "");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(MessageEvent messageEvent) {
        if (messageEvent.getMessage().equals("login_success")) {
            onResume();
        } else {
            topicAdapter.setNewData(App.topicInfoList);
        }
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

    }

    @Override
    public void loadDataSuccess(ResultInfo tData) {
        if (tData != null && tData.getCode() == Constants.SUCCESS) {
            if (tData instanceof NoteInfoRet) {
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
            }

            if (tData instanceof FollowInfoRet) {
                ToastUtils.showLong(((FollowInfoRet) tData).getData().getIsGuan() == 0 ? "已取消" : "已关注");
                noteInfoAdapter.getData().get(currentClickIndex).setIsGuan(((FollowInfoRet) tData).getData().getIsGuan());
                noteInfoAdapter.notifyDataSetChanged();
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
            if (tData instanceof FollowInfoRet) {
                Logger.i(StringUtils.isEmpty(tData.getMsg()) ? "操作错误" : tData.getMsg());
            }
        }

    }

    @Override
    public void loadDataError(Throwable throwable) {

    }
}
