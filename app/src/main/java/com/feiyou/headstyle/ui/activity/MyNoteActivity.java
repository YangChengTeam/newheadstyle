package com.feiyou.headstyle.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.HeadType;
import com.feiyou.headstyle.bean.NoteInfoRet;
import com.feiyou.headstyle.bean.ResultInfo;
import com.feiyou.headstyle.common.Constants;
import com.feiyou.headstyle.presenter.NoteDataPresenterImp;
import com.feiyou.headstyle.ui.adapter.MoreHeadTypeAdapter;
import com.feiyou.headstyle.ui.adapter.NoteInfoAdapter;
import com.feiyou.headstyle.ui.base.BaseFragmentActivity;
import com.feiyou.headstyle.ui.custom.NormalDecoration;
import com.feiyou.headstyle.view.NoteDataView;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by myflying on 2018/11/23.
 */
public class MyNoteActivity extends BaseFragmentActivity implements NoteDataView {

    @BindView(R.id.topbar)
    QMUITopBar mTopBar;

    @BindView(R.id.my_note_list)
    RecyclerView mNoteListView;

    ImageView mBackImageView;

    NoteInfoAdapter noteInfoAdapter;

    private NoteDataPresenterImp noteDataPresenterImp;

    private int currentPage = 1;

    private int pageSize = 20;

    BottomSheetDialog bottomSheetDialog;

    @Override
    protected int getContextViewId() {
        return R.layout.activity_my_note;
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
        titleTv.setText("我的发帖");

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
        bottomSheetDialog = new BottomSheetDialog(this);
        View deleteDialogView = LayoutInflater.from(this).inflate(R.layout.note_delete_dialog, null);
        bottomSheetDialog.setContentView(deleteDialogView);

        noteDataPresenterImp = new NoteDataPresenterImp(this, this);

        noteInfoAdapter = new NoteInfoAdapter(this, null, 2);
        mNoteListView.setLayoutManager(new LinearLayoutManager(this));
        mNoteListView.setAdapter(noteInfoAdapter);
        //mNoteListView.addItemDecoration(new NormalDecoration(ContextCompat.getColor(this, R.color.line_color), SizeUtils.dp2px(8)));
        noteInfoAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(MyNoteActivity.this, CommunityArticleActivity.class);
                startActivity(intent);
            }
        });

        noteInfoAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                currentPage++;
                noteDataPresenterImp.getNoteData(currentPage, 2, "");
            }
        }, mNoteListView);

        noteInfoAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.layout_operation) {
                    if (bottomSheetDialog != null && !bottomSheetDialog.isShowing()) {
                        bottomSheetDialog.show();
                    }
                }
            }
        });

        noteDataPresenterImp.getNoteData(currentPage, 2, "");
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

        } else {
            ToastUtils.showLong(StringUtils.isEmpty(tData.getMsg()) ? "操作错误" : tData.getMsg());
        }
    }

    @Override
    public void loadDataError(Throwable throwable) {

    }
}
