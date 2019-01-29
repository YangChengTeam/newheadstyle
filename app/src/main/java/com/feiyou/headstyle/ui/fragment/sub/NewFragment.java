package com.feiyou.headstyle.ui.fragment.sub;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.blankj.utilcode.util.StringUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.NoteTypeRet;
import com.feiyou.headstyle.common.Constants;
import com.feiyou.headstyle.presenter.NoteTypePresenterImp;
import com.feiyou.headstyle.ui.adapter.NoteInfoAdapter;
import com.feiyou.headstyle.ui.base.BaseFragment;
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

    public static NewFragment newInstance(String topId) {
        NewFragment fragment = new NewFragment();
        Bundle bundle = new Bundle();
        bundle.putString("topic_id", topId);
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

        noteInfoAdapter = new NoteInfoAdapter(getActivity(), null);
        mNewsListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mNewsListView.setAdapter(noteInfoAdapter);

        avi.show();
        noteTypePresenterImp = new NoteTypePresenterImp(this, getActivity());
        noteTypePresenterImp.getNoteTypeData(topicId, currentPage, 1, "");
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void dismissProgress() {
        avi.hide();
    }

    @Override
    public void loadDataSuccess(NoteTypeRet tData) {
        if (tData != null && tData.getCode() == Constants.SUCCESS) {
            if (tData.getData() != null && tData.getData().getList() != null) {
                noteInfoAdapter.setNewData(tData.getData().getList());
            }
        }
    }

    @Override
    public void loadDataError(Throwable throwable) {

    }
}
