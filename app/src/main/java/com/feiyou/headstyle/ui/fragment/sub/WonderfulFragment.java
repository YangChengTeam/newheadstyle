package com.feiyou.headstyle.ui.fragment.sub;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.NoteCommentRet;
import com.feiyou.headstyle.common.Constants;
import com.feiyou.headstyle.presenter.NoteCommentDataPresenterImp;
import com.feiyou.headstyle.ui.adapter.CommentAdapter;
import com.feiyou.headstyle.ui.base.BaseFragment;
import com.feiyou.headstyle.view.NoteCommentDataView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by myflying on 2018/11/26.
 */
public class WonderfulFragment extends BaseFragment implements NoteCommentDataView {

    @BindView(R.id.wonderful_list)
    RecyclerView mWonderfulListView;

    private CommentAdapter commentAdapter;

    private NoteCommentDataPresenterImp noteCommentDataPresenterImp;

    public static WonderfulFragment getInstance() {
        return new WonderfulFragment();
    }

    @Override
    protected View onCreateView() {
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_wonderful, null);
        ButterKnife.bind(this, root);
        initData();
        return root;
    }

    public void initData() {
        mWonderfulListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        commentAdapter = new CommentAdapter(getActivity(), null);
        mWonderfulListView.setAdapter(commentAdapter);

        noteCommentDataPresenterImp = new NoteCommentDataPresenterImp(this,getActivity());
        noteCommentDataPresenterImp.getNoteDetailData(1, "110600", 1);
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void dismissProgress() {

    }

    @Override
    public void loadDataSuccess(NoteCommentRet tData) {
        if (tData != null && tData.getCode() == Constants.SUCCESS) {
            if (tData.getData() != null) {
                commentAdapter.setNewData(tData.getData());
            }
        }
    }

    @Override
    public void loadDataError(Throwable throwable) {

    }
}
