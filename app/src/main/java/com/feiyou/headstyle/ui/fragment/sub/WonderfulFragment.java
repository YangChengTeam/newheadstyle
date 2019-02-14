package com.feiyou.headstyle.ui.fragment.sub;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.NoteCommentRet;
import com.feiyou.headstyle.bean.ReplyParams;
import com.feiyou.headstyle.bean.ReplyResultInfoRet;
import com.feiyou.headstyle.bean.ResultInfo;
import com.feiyou.headstyle.common.Constants;
import com.feiyou.headstyle.presenter.NoteCommentDataPresenterImp;
import com.feiyou.headstyle.presenter.ReplyCommentPresenterImp;
import com.feiyou.headstyle.ui.adapter.CommentAdapter;
import com.feiyou.headstyle.ui.adapter.CommentReplyAdapter;
import com.feiyou.headstyle.ui.base.BaseFragment;
import com.feiyou.headstyle.view.CommentDialog;
import com.feiyou.headstyle.view.NoteCommentDataView;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by myflying on 2018/11/26.
 */
public class WonderfulFragment extends BaseFragment implements NoteCommentDataView, CommentDialog.SendBackListener {

    @BindView(R.id.wonderful_list)
    RecyclerView mWonderfulListView;

    private CommentAdapter commentAdapter;

    private NoteCommentDataPresenterImp noteCommentDataPresenterImp;

    private ReplyCommentPresenterImp replyCommentPresenterImp;

    BottomSheetDialog commitReplyDialog;

    private View replyView;

    private ImageView mCloseReplyIv;

    private RecyclerView replyListView;

    CommentDialog commentDialog;

    private CommentReplyAdapter commentReplyAdapter;

    public static WonderfulFragment getInstance() {
        return new WonderfulFragment();
    }

    private int currentCommentPos = -1;

    private String commentId;

    private String repeatCommentUserId;

    private ProgressDialog progressDialog = null;

    @Override
    protected View onCreateView() {
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_wonderful, null);
        ButterKnife.bind(this, root);
        initData();
        return root;
    }

    public static WonderfulFragment newInstance(String topId) {
        WonderfulFragment fragment = new WonderfulFragment();
        return fragment;
    }

    public void initData() {

        commitReplyDialog = new BottomSheetDialog(getActivity());
        replyView = LayoutInflater.from(getActivity()).inflate(R.layout.comment_reply_view, null);
        replyListView = replyView.findViewById(R.id.rv_reply_list);
        mCloseReplyIv = replyView.findViewById(R.id.iv_close);

        replyView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ScreenUtils.getScreenHeight()));
        commitReplyDialog.setContentView(replyView);

        //setPeekHeight,设置弹出窗口的高度为全屏的状态.
        BottomSheetBehavior mBehavior = BottomSheetBehavior.from((View) replyView.getParent());
        mBehavior.setPeekHeight(ScreenUtils.getScreenHeight() - BarUtils.getStatusBarHeight());

        mWonderfulListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        commentAdapter = new CommentAdapter(getActivity(), null);
        mWonderfulListView.setAdapter(commentAdapter);

        noteCommentDataPresenterImp = new NoteCommentDataPresenterImp(this, getActivity());
        replyCommentPresenterImp = new ReplyCommentPresenterImp(this, getActivity());

        noteCommentDataPresenterImp.getNoteDetailData(1, "110634", 1);

        commentReplyAdapter = new CommentReplyAdapter(getActivity(), null);
        replyListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        replyListView.setAdapter(commentReplyAdapter);

        commentAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                currentCommentPos = position;
                showDialog();
            }
        });

        commentAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.btn_reply_count) {
                    if (commitReplyDialog != null && !commitReplyDialog.isShowing()) {
                        commitReplyDialog.show();
                    }
                    commentReplyAdapter.setNewData(commentAdapter.getData().get(position).getComment());
                }
            }
        });

        mCloseReplyIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (commitReplyDialog != null && commitReplyDialog.isShowing()) {
                    commitReplyDialog.dismiss();
                }
            }
        });

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("回复中");
    }

    public void showDialog() {
        commentDialog = new CommentDialog(getActivity(), 1);
        commentDialog.setSendBackListener(this);
        commentDialog.show(getActivity().getFragmentManager(), "dialog");
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void dismissProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void loadDataSuccess(ResultInfo tData) {

        Logger.i("tdata--->" + JSONObject.toJSONString(tData));

        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

        if (tData != null && tData.getCode() == Constants.SUCCESS) {
            if (tData instanceof NoteCommentRet) {
                if (((NoteCommentRet) tData).getData() != null) {
                    commentAdapter.setNewData(((NoteCommentRet) tData).getData());
                }
            }
            if (tData instanceof ReplyResultInfoRet) {
                ToastUtils.showLong("回复成功");
                Integer tempNum = commentAdapter.getData().get(currentCommentPos).getListNum() + 1;
                commentAdapter.getData().get(currentCommentPos).setListNum(tempNum);
                commentAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void loadDataError(Throwable throwable) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void sendContent(String content, int type) {
        Logger.i("content--->" + content + "---type--->" + type);

        if (progressDialog != null && !progressDialog.isShowing()) {
            progressDialog.show();
        }

        commentId = commentAdapter.getData().get(currentCommentPos).getCommentId();
        repeatCommentUserId = commentAdapter.getData().get(currentCommentPos).getUserId();

        ReplyParams replyParams = new ReplyParams();
        replyParams.setType(2);
        replyParams.setContent("我只是测试回复内容");
        replyParams.setRepeatUserId("1021601");
        replyParams.setCommentId(commentId);
        replyParams.setRepeatCommentUserId(repeatCommentUserId);

        replyCommentPresenterImp.addReplyInfo(replyParams);

        if (content != null) {
            if (commentDialog != null) {
                commentDialog.hideProgressDialog();
                commentDialog.dismiss();
            }
        }
    }
}
