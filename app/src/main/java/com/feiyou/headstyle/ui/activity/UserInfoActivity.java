package com.feiyou.headstyle.ui.activity;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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
import com.feiyou.headstyle.bean.CollectInfoRet;
import com.feiyou.headstyle.bean.NoteInfoRet;
import com.feiyou.headstyle.bean.ResultInfo;
import com.feiyou.headstyle.bean.UserInfo;
import com.feiyou.headstyle.bean.UserInfoRet;
import com.feiyou.headstyle.common.Constants;
import com.feiyou.headstyle.presenter.CollectDataPresenterImp;
import com.feiyou.headstyle.presenter.NoteDataPresenterImp;
import com.feiyou.headstyle.presenter.UserInfoPresenterImp;
import com.feiyou.headstyle.ui.adapter.CommonImageAdapter;
import com.feiyou.headstyle.ui.adapter.HeadInfoAdapter;
import com.feiyou.headstyle.ui.adapter.NoteInfoAdapter;
import com.feiyou.headstyle.ui.base.BaseFragmentActivity;
import com.feiyou.headstyle.ui.custom.GlideRoundTransform;
import com.feiyou.headstyle.view.CollectDataView;
import com.feiyou.headstyle.view.NoteDataView;
import com.feiyou.headstyle.view.UserInfoView;
import com.orhanobut.logger.Logger;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUICollapsingTopBarLayout;
import com.qmuiteam.qmui.widget.QMUITopBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by myflying on 2018/11/23.
 */
public class UserInfoActivity extends BaseFragmentActivity implements UserInfoView, View.OnClickListener {

    @BindView(R.id.collapsing_topbar_layout)
    QMUICollapsingTopBarLayout mCollapsingTopBarLayout;

    @BindView(R.id.topbar)
    QMUITopBar mTopBar;

    @BindView(R.id.iv_user_head)
    ImageView mUserHeadIv;

    @BindView(R.id.layout_guan_fen)
    RelativeLayout mGuanFenLayout;

    @BindView(R.id.note_list)
    RecyclerView mNoteListView;

    @BindView(R.id.photo_list)
    RecyclerView mPhotoListView;

    @BindView(R.id.tv_follow_count)
    TextView mFollowCountTv;

    @BindView(R.id.tv_fans_count)
    TextView mFansCountTv;

    @BindView(R.id.tv_user_nick_name)
    TextView mNickNameTv;

    @BindView(R.id.tv_user_id)
    TextView mUserIdTv;

    @BindView(R.id.tv_user_age)
    TextView mUserAgeTv;

    @BindView(R.id.iv_user_sex)
    ImageView mUserSexIv;

    @BindView(R.id.tv_user_star)
    TextView mUserStarTv;

    @BindView(R.id.tv_user_sign)
    TextView mUserSignTv;

    @BindView(R.id.layout_photos)
    RelativeLayout mPhotoLayout;

    ImageView mBackImageView;

    NoteInfoAdapter noteInfoAdapter;

    private int currentPage = 1;

    private int pageSize = 20;

    BottomSheetDialog bottomSheetDialog;

    CommonImageAdapter commonImageAdapter;

    private UserInfo userInfo;

    private List<Object> photoList;

    private UserInfoPresenterImp userInfoPresenterImp;

    private String userId;

    private boolean isMyInfo;

    BottomSheetDialog updateBgDialog;

    LinearLayout mTopItemLayout;

    TextView mTopItemTv;

    LinearLayout mUpdateCancelLayout;

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
        ImageView rightIv = topView.findViewById(R.id.iv_right);
        TextView titleTv = topView.findViewById(R.id.tv_title);
        titleTv.setText(isMyInfo ? "我的主页" : "个人主页");
        mBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });

        rightIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (updateBgDialog != null && !updateBgDialog.isShowing()) {
                    updateBgDialog.show();
                }
            }
        });
    }

    public void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            isMyInfo = bundle.getBoolean("is_my_info", false);
        }

        if (isMyInfo) {
            userInfo = App.getApp().getmUserInfo();
            userId = userInfo.getId();

            RequestOptions options = new RequestOptions();
            options.transform(new GlideRoundTransform(this, 30));
            Glide.with(this).load(userInfo.getUserimg()).apply(options).into(mUserHeadIv);

            mFollowCountTv.setText(userInfo.getGuanNum() + "");
            mFansCountTv.setText(userInfo.getFenNum() + "");
            mNickNameTv.setText(StringUtils.isEmpty(userInfo.getNickname()) ? "" : userInfo.getNickname());
            mUserIdTv.setText(userInfo.getId() + "");
            mUserAgeTv.setText(userInfo.getAge() + "岁");
            mUserSignTv.setText(userInfo.getSig());
            mUserSexIv.setVisibility(userInfo.getSex() > 0 ? View.VISIBLE : View.GONE);
            Glide.with(this).load(userInfo.getSex() == 1 ? R.mipmap.sex_boy : R.mipmap.sex_girl).into(mUserSexIv);
            mUserStarTv.setText(userInfo.getStar());

            //设置照片墙
            if (userInfo.getImageWall() != null && userInfo.getImageWall().length > 0) {
                mPhotoLayout.setVisibility(View.VISIBLE);
                mPhotoLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(80)));
                mPhotoListView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(60)));
                String[] tempPhotos = userInfo.getImageWall();
                photoList = new ArrayList<>();
                for (int i = 0; i < tempPhotos.length; i++) {
                    photoList.add(tempPhotos[i]);
                }
                if (photoList.size() > 4) {
                    photoList = photoList.subList(0, 4);
                }

                commonImageAdapter.setNewData(photoList);
            } else {
                mPhotoLayout.setVisibility(View.GONE);
                mPhotoLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0));
                mPhotoListView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 0));
            }
        } else {
            if (bundle != null && !StringUtils.isEmpty(bundle.getString("user_id"))) {
                userId = bundle.getString("user_id");
            }
        }

        userInfoPresenterImp = new UserInfoPresenterImp(this, this);

        mGuanFenLayout.setVisibility(isMyInfo ? View.INVISIBLE : View.VISIBLE);

        bottomSheetDialog = new BottomSheetDialog(this);
        View deleteDialogView = LayoutInflater.from(this).inflate(R.layout.note_delete_dialog, null);
        bottomSheetDialog.setContentView(deleteDialogView);

        //顶部操作栏弹出窗口
        updateBgDialog = new BottomSheetDialog(this);
        View updateBgView = LayoutInflater.from(this).inflate(R.layout.update_info_bg_view, null);
        mTopItemLayout = updateBgView.findViewById(R.id.layout_top_item);
        mUpdateCancelLayout = updateBgView.findViewById(R.id.layout_update_cancel);
        mTopItemTv = updateBgView.findViewById(R.id.tv_top_item);
        mTopItemTv.setText(isMyInfo ? "更换背景图" : "举报");
        mTopItemTv.setTextColor(ContextCompat.getColor(this, isMyInfo ? R.color.black : R.color.tab_select_color));

        mTopItemLayout.setOnClickListener(this);
        mUpdateCancelLayout.setOnClickListener(this);
        updateBgDialog.setContentView(updateBgView);

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
                userInfoPresenterImp.getUserInfo(userId);
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
        userInfoPresenterImp.getUserInfo(userId);
    }

    @OnClick(R.id.tv_follow_count)
    void followCount() {
        Intent intent = new Intent(this, MyFollowActivity.class);
        intent.putExtra("type", 0);
        intent.putExtra("is_my_info", false);
        intent.putExtra("into_user_id", userId);
        startActivity(intent);
    }

    @OnClick(R.id.tv_fans_count)
    void fansCount() {
        Intent intent = new Intent(this, MyFollowActivity.class);
        intent.putExtra("type", 1);
        intent.putExtra("is_my_info", false);
        intent.putExtra("into_user_id", userId);
        startActivity(intent);
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void dismissProgress() {

    }

    @Override
    public void loadDataSuccess(ResultInfo tData) {
        Logger.i("other user info--->" + JSON.toJSONString(tData));

        if (tData != null && tData.getCode() == Constants.SUCCESS) {
            if (tData instanceof UserInfoRet) {
                userInfo = ((UserInfoRet) tData).getData();
                RequestOptions options = new RequestOptions();
                options.transform(new GlideRoundTransform(this, 30));
                Glide.with(this).load(userInfo.getUserimg()).apply(options).into(mUserHeadIv);

                mFollowCountTv.setText(userInfo.getGuanNum() + "");
                mFansCountTv.setText(userInfo.getFenNum() + "");
                mNickNameTv.setText(userInfo.getNickname() + "");
                mUserIdTv.setText(userInfo.getId() + "");
                mUserAgeTv.setText(userInfo.getAge() + "岁");
                mUserSignTv.setText(userInfo.getSig());
                mUserSexIv.setVisibility(userInfo.getSex() > 0 ? View.VISIBLE : View.GONE);
                Glide.with(this).load(userInfo.getSex() == 1 ? R.mipmap.sex_boy : R.mipmap.sex_girl).into(mUserSexIv);
                mUserStarTv.setText(userInfo.getStar());

                if (userInfo.getImageWall() != null && userInfo.getImageWall().length > 0) {
                    mPhotoLayout.setVisibility(View.VISIBLE);
                    mPhotoLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(80)));
                    mPhotoListView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(60)));
                } else {
                    mPhotoLayout.setVisibility(View.GONE);
                    mPhotoLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0));
                    mPhotoListView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 0));
                }

                if (userInfo.getNoteList() != null && userInfo.getNoteList().size() > 0) {
                    if (currentPage == 1) {
                        noteInfoAdapter.setNewData(userInfo.getNoteList());
                    } else {
                        noteInfoAdapter.addData(userInfo.getNoteList());
                    }

                    if (userInfo.getNoteList().size() == pageSize) {
                        noteInfoAdapter.loadMoreComplete();
                    } else {
                        noteInfoAdapter.loadMoreEnd();
                    }
                }
            }

        } else {
            ToastUtils.showLong(StringUtils.isEmpty(tData.getMsg()) ? "操作错误" : tData.getMsg());
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

    @Override
    public void onClick(View view) {

        if (updateBgDialog != null && updateBgDialog.isShowing()) {
            updateBgDialog.dismiss();
        }

        switch (view.getId()) {
            case R.id.layout_top_item:
                if (isMyInfo) {
                    ToastUtils.showLong("更换背景图");
                } else {
                    Intent intent = new Intent(this, ReportInfoActivity.class);
                    intent.putExtra("rid", userId);
                    intent.putExtra("report_type", 1);
                    startActivity(intent);
                }
                break;
            case R.id.layout_update_cancel:

                break;
            default:
                break;
        }
    }
}
