package com.feiyou.headstyle.ui.activity;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.CollectInfoRet;
import com.feiyou.headstyle.bean.NoteInfoRet;
import com.feiyou.headstyle.bean.UserInfo;
import com.feiyou.headstyle.common.Constants;
import com.feiyou.headstyle.presenter.CollectDataPresenterImp;
import com.feiyou.headstyle.presenter.NoteDataPresenterImp;
import com.feiyou.headstyle.ui.adapter.CommonImageAdapter;
import com.feiyou.headstyle.ui.adapter.HeadInfoAdapter;
import com.feiyou.headstyle.ui.adapter.NoteInfoAdapter;
import com.feiyou.headstyle.ui.base.BaseFragmentActivity;
import com.feiyou.headstyle.view.CollectDataView;
import com.feiyou.headstyle.view.NoteDataView;
import com.orhanobut.logger.Logger;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUICollapsingTopBarLayout;
import com.qmuiteam.qmui.widget.QMUITopBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by myflying on 2018/11/23.
 */
public class UserInfoActivity extends BaseFragmentActivity implements NoteDataView {

    @BindView(R.id.collapsing_topbar_layout)
    QMUICollapsingTopBarLayout mCollapsingTopBarLayout;

    @BindView(R.id.topbar)
    QMUITopBar mTopBar;

    @BindView(R.id.note_list)
    RecyclerView mNoteListView;

    @BindView(R.id.photo_list)
    RecyclerView mPhotoListView;

    ImageView mBackImageView;

    NoteInfoAdapter noteInfoAdapter;

    private NoteDataPresenterImp noteDataPresenterImp;

    private int currentPage = 1;

    private int pageSize = 20;

    BottomSheetDialog bottomSheetDialog;

    CommonImageAdapter commonImageAdapter;

    private UserInfo userInfo;

    private List<Object> photoList;

    @Override
    protected int getContextViewId() {
        return R.layout.activity_user_info;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTopBar();
        initData();
    }

    private void initTopBar() {
        QMUIStatusBarHelper.setStatusBarLightMode(this);
        View topView = getLayoutInflater().inflate(R.layout.common_user_info, null);
        topView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(48)));

        mTopBar.setCenterView(topView);
        mBackImageView = topView.findViewById(R.id.iv_back);
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

        commonImageAdapter = new CommonImageAdapter(this, null, 32);
        mPhotoListView.setLayoutManager(new GridLayoutManager(this, 4));
        mPhotoListView.setAdapter(commonImageAdapter);
        commonImageAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(UserInfoActivity.this, PhotoWallActivity.class);
                startActivity(intent);
            }
        });

        noteDataPresenterImp = new NoteDataPresenterImp(this, this);
        noteInfoAdapter = new NoteInfoAdapter(this, null, 2);
        mNoteListView.setLayoutManager(new LinearLayoutManager(this));
        mNoteListView.setAdapter(noteInfoAdapter);
        //mNoteListView.addItemDecoration(new NormalDecoration(ContextCompat.getColor(this, R.color.line_color), SizeUtils.dp2px(8)));
        noteInfoAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(UserInfoActivity.this, CommunityArticleActivity.class);
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
    protected void onResume() {
        super.onResume();

        if (!StringUtils.isEmpty(SPUtils.getInstance().getString(Constants.USER_INFO))) {
            Logger.i(SPUtils.getInstance().getString(Constants.USER_INFO));
            userInfo = JSON.parseObject(SPUtils.getInstance().getString(Constants.USER_INFO), new TypeReference<UserInfo>() {
            });
        }

        if (userInfo != null && userInfo.getImageWall() != null && userInfo.getImageWall().length > 0) {

            String[] tempPhotos = userInfo.getImageWall();
            photoList = new ArrayList<>();
            for (int i = 0; i < tempPhotos.length; i++) {
                photoList.add(tempPhotos[i]);
            }
            if (photoList.size() > 4) {
                photoList = photoList.subList(0, 4);
            }

            commonImageAdapter.setNewData(photoList);
        }
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        popBackStack();
    }
}
