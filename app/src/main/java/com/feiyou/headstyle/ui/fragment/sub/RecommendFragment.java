package com.feiyou.headstyle.ui.fragment.sub;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feiyou.headstyle.App;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.HeadInfo;
import com.feiyou.headstyle.bean.HeadType;
import com.feiyou.headstyle.bean.MessageEvent;
import com.feiyou.headstyle.bean.NoteInfo;
import com.feiyou.headstyle.bean.NoteInfoRet;
import com.feiyou.headstyle.common.Constants;
import com.feiyou.headstyle.presenter.NoteDataPresenterImp;
import com.feiyou.headstyle.ui.activity.CommunityArticleActivity;
import com.feiyou.headstyle.ui.activity.CommunityTypeActivity;
import com.feiyou.headstyle.ui.adapter.NoteInfoAdapter;
import com.feiyou.headstyle.ui.adapter.TopicAdapter;
import com.feiyou.headstyle.ui.base.BaseFragment;
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

    @BindView(R.id.topic_list_view)
    RecyclerView mTopicListView;

    @BindView(R.id.recommend_list)
    RecyclerView mRecommendListView;

    TopicAdapter topicAdapter;

    NoteInfoAdapter noteInfoAdapter;

    private NoteDataPresenterImp noteDataPresenterImp;

    private int currentPage = 1;

    private int pageSize = 20;

    public static RecommendFragment getInstance() {
        return new RecommendFragment();
    }

    @Override
    protected View onCreateView() {
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_tab_recommend, null);
        ButterKnife.bind(this, root);
        initData();
        return root;
    }

    public void initData() {
        noteDataPresenterImp = new NoteDataPresenterImp(this, getActivity());

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

        noteInfoAdapter = new NoteInfoAdapter(getActivity(), null);
        mRecommendListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecommendListView.setAdapter(noteInfoAdapter);

        noteInfoAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getActivity(), CommunityArticleActivity.class);
                startActivity(intent);
            }
        });

        noteInfoAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                currentPage++;
                noteDataPresenterImp.getNoteData(currentPage, 2, "");
            }
        }, mRecommendListView);

        noteDataPresenterImp.getNoteData(currentPage, 2, "");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(MessageEvent messageEvent) {
        topicAdapter.setNewData(App.topicInfoList);
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
    public void loadDataSuccess(NoteInfoRet tData) {
        if (tData != null) {
            if (tData.getCode() == Constants.SUCCESS) {

                if (currentPage == 1) {
                    noteInfoAdapter.setNewData(tData.getData());
                } else {
                    noteInfoAdapter.addData(tData.getData());
                }

                if (tData.getData().size() == pageSize) {
                    noteInfoAdapter.loadMoreComplete();
                } else {
                    noteInfoAdapter.loadMoreEnd();
                }
            } else {
                //error
                //noteInfoAdapter.loadMoreEnd();
            }
        }
    }

    @Override
    public void loadDataError(Throwable throwable) {

    }
}
