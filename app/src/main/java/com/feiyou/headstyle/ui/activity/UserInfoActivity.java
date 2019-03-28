package com.feiyou.headstyle.ui.activity;

import android.animation.ValueAnimator;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.blankj.utilcode.util.PathUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feiyou.headstyle.App;
import com.feiyou.headstyle.GlideApp;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.CollectInfoRet;
import com.feiyou.headstyle.bean.NoteInfoRet;
import com.feiyou.headstyle.bean.ResultInfo;
import com.feiyou.headstyle.bean.UpdateHeadRet;
import com.feiyou.headstyle.bean.UserInfo;
import com.feiyou.headstyle.bean.UserInfoRet;
import com.feiyou.headstyle.common.Constants;
import com.feiyou.headstyle.presenter.CollectDataPresenterImp;
import com.feiyou.headstyle.presenter.DeleteNotePresenterImp;
import com.feiyou.headstyle.presenter.NoteDataPresenterImp;
import com.feiyou.headstyle.presenter.UpdateHeadPresenterImp;
import com.feiyou.headstyle.presenter.UserInfoPresenterImp;
import com.feiyou.headstyle.ui.adapter.CommonImageAdapter;
import com.feiyou.headstyle.ui.adapter.HeadInfoAdapter;
import com.feiyou.headstyle.ui.adapter.NoteInfoAdapter;
import com.feiyou.headstyle.ui.base.BaseFragmentActivity;
import com.feiyou.headstyle.ui.custom.ConfigDialog;
import com.feiyou.headstyle.ui.custom.Glide4Engine;
import com.feiyou.headstyle.ui.custom.GlideCircleTransformWithBorder;
import com.feiyou.headstyle.ui.custom.GlideRoundTransform;
import com.feiyou.headstyle.view.CollectDataView;
import com.feiyou.headstyle.view.NoteDataView;
import com.feiyou.headstyle.view.UserInfoView;
import com.orhanobut.logger.Logger;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUICollapsingTopBarLayout;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.wang.avi.AVLoadingIndicatorView;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by myflying on 2018/11/23.
 */
public class UserInfoActivity extends BaseFragmentActivity implements UserInfoView, View.OnClickListener, ConfigDialog.ConfigListener {

    private static final int REQUEST_CODE_CHOOSE = 23;

    private static final int TAKE_BIG_PICTURE = 1000;

    private static final int CROP_SMALL_PICTURE = 1003;

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

    @BindView(R.id.layout_no_data)
    LinearLayout mNoDataLayout;

    @BindView(R.id.tv_no_data_title)
    TextView mNoDataTitleTv;

    @BindView(R.id.tv_send_note)
    TextView mSendNoteTv;

    @BindView(R.id.avi)
    AVLoadingIndicatorView avi;

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

    @BindView(R.id.layout_sign)
    LinearLayout mSignLayout;

    @BindView(R.id.layout_photos)
    RelativeLayout mPhotoLayout;

    @BindView(R.id.top_bg_layout)
    LinearLayout mTopBgLayout;

    ImageView mBackImageView;

    NoteInfoAdapter noteInfoAdapter;

    private int currentPage = 1;

    private int pageSize = 20;

    BottomSheetDialog bottomSheetDialog;

    CommonImageAdapter commonImageAdapter;

    private UserInfo userInfo;

    private List<Object> photoList;

    UserInfoPresenterImp userInfoPresenterImp;

    UpdateHeadPresenterImp updateHeadPresenterImp;

    DeleteNotePresenterImp deleteNotePresenterImp;

    private String userId;

    private boolean isMyInfo;

    BottomSheetDialog updateBgDialog;

    LinearLayout mTopItemLayout;

    TextView mTopItemTv;

    LinearLayout mUpdateCancelLayout;

    RequestOptions options;

    BottomSheetDialog updateHeadBottomSheetDialog;

    LinearLayout takePhotoLayout;

    LinearLayout localPhotoLayout;

    LinearLayout cancelPhotoLayout;

    private File outputImage;

    private Uri imageUri;

    private ProgressDialog progressDialog = null;

    ConfigDialog configDialog;

    private boolean isMakeDelete;

    private int currentItemPos = -1;

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

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在保存");

        configDialog = new ConfigDialog(this, R.style.login_dialog, 1, "确认删除吗?", "请你确认是否删除当前帖子?");
        configDialog.setConfigListener(this);

        //设置白色边框的图片
        options = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL);
        options.placeholder(R.mipmap.head_def);
        options.transform(new GlideCircleTransformWithBorder(this, 2, ContextCompat.getColor(this, R.color.white)));

        commonImageAdapter = new CommonImageAdapter(this, null, 32);
        mPhotoListView.setLayoutManager(new GridLayoutManager(this, 4));
        mPhotoListView.setAdapter(commonImageAdapter);

        if (isMyInfo) {
            userInfo = App.getApp().getmUserInfo();
            userId = userInfo.getId();

            Glide.with(this).load(userInfo.getUserimg()).apply(options).into(mUserHeadIv);

            mFollowCountTv.setText(userInfo.getFollowNum() + "");
            mFansCountTv.setText(userInfo.getFollowerNum() + "");
            mNickNameTv.setText(StringUtils.isEmpty(userInfo.getNickname()) ? "" : userInfo.getNickname());
            mUserIdTv.setText(StringUtils.isEmpty(userInfo.getId()) ? "" : userInfo.getId() + "");
            mUserAgeTv.setText(userInfo.getAge() + "岁");
            mUserSignTv.setText(userInfo.getIntro());
            mUserSexIv.setVisibility(userInfo.getSex() > 0 ? View.VISIBLE : View.GONE);
            Glide.with(this).load(userInfo.getSex() == 1 ? R.mipmap.sex_boy : R.mipmap.sex_girl).into(mUserSexIv);
            mUserStarTv.setText(userInfo.getStar());
            mSignLayout.setVisibility(StringUtils.isEmpty(userInfo.getIntro()) ? View.GONE : View.VISIBLE);
            //设置照片墙
            if (userInfo.getImageWall() != null && userInfo.getImageWall().length > 0) {
                mPhotoLayout.setVisibility(View.VISIBLE);
                String[] tempPhotos = userInfo.getImageWall();
                photoList = new ArrayList<>();
                for (int i = 0; i < tempPhotos.length; i++) {
                    photoList.add(tempPhotos[i]);
                }
                if (photoList.size() > 4) {
                    photoList = photoList.subList(0, 4);
                }
                //TODO
                commonImageAdapter.setNewData(photoList);
            } else {
                mPhotoLayout.setVisibility(View.GONE);
            }
        } else {
            if (bundle != null && !StringUtils.isEmpty(bundle.getString("user_id"))) {
                userId = bundle.getString("user_id");
            }
        }

        userInfoPresenterImp = new UserInfoPresenterImp(this, this);
        updateHeadPresenterImp = new UpdateHeadPresenterImp(this, this);
        deleteNotePresenterImp = new DeleteNotePresenterImp(this, this);

        mGuanFenLayout.setVisibility(isMyInfo ? View.INVISIBLE : View.VISIBLE);

        bottomSheetDialog = new BottomSheetDialog(this);
        View deleteDialogView = LayoutInflater.from(this).inflate(R.layout.note_delete_dialog, null);
        LinearLayout cancelLayout = deleteDialogView.findViewById(R.id.layout_cancel);
        LinearLayout deleteLayout = deleteDialogView.findViewById(R.id.layout_delete);
        deleteLayout.setOnClickListener(this);
        cancelLayout.setOnClickListener(this);

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

        initPhotoView();

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
                userInfoPresenterImp.getUserInfo(App.getApp().getmUserInfo().getId(), userId);
            }
        }, mNoteListView);

        noteInfoAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.layout_operation) {
                    currentItemPos = position;
                    if (bottomSheetDialog != null && !bottomSheetDialog.isShowing()) {
                        bottomSheetDialog.show();
                    }
                }
            }
        });
        userInfoPresenterImp.getUserInfo(App.getApp().getmUserInfo().getId(), userId);
    }

    public void initPhotoView() {
        updateHeadBottomSheetDialog = new BottomSheetDialog(this);
        //头像
        View headSelectView = LayoutInflater.from(this).inflate(R.layout.photo_select_view, null);
        takePhotoLayout = headSelectView.findViewById(R.id.layout_camera);
        localPhotoLayout = headSelectView.findViewById(R.id.layout_local_photo);
        cancelPhotoLayout = headSelectView.findViewById(R.id.layout_head_select_cancel);
        takePhotoLayout.setOnClickListener(this);
        localPhotoLayout.setOnClickListener(this);
        cancelPhotoLayout.setOnClickListener(this);

        updateHeadBottomSheetDialog.setContentView(headSelectView);
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

    @OnClick(R.id.layout_photos)
    void photoWall() {
        Intent intent = new Intent(UserInfoActivity.this, PhotoWallActivity.class);
        startActivity(intent);
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void dismissProgress() {
        avi.hide();
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void loadDataSuccess(ResultInfo tData) {
        Logger.i("other user info--->" + JSON.toJSONString(tData));
        avi.hide();
        if (tData != null && tData.getCode() == Constants.SUCCESS) {
            if (tData instanceof UserInfoRet) {
                userInfo = ((UserInfoRet) tData).getData();

                Glide.with(this).load(userInfo.getUserimg()).apply(options).into(mUserHeadIv);

                mFollowCountTv.setText(userInfo.getGuanNum() + "");
                mFansCountTv.setText(userInfo.getFenNum() + "");
                mNickNameTv.setText(StringUtils.isEmpty(userInfo.getNickname()) ? "" : userInfo.getNickname());
                mUserIdTv.setText(StringUtils.isEmpty(userInfo.getId()) ? "" : userInfo.getId() + "");
                mUserAgeTv.setText(userInfo.getAge() + "岁");
                mUserSignTv.setText(userInfo.getIntro());
                mUserSexIv.setVisibility(userInfo.getSex() > 0 ? View.VISIBLE : View.GONE);
                Glide.with(this).load(userInfo.getSex() == 1 ? R.mipmap.sex_boy : R.mipmap.sex_girl).into(mUserSexIv);
                mUserStarTv.setText(userInfo.getStar());
                mSignLayout.setVisibility(StringUtils.isEmpty(userInfo.getIntro()) ? View.GONE : View.VISIBLE);

                if (userInfo.getImageWall() != null && userInfo.getImageWall().length > 0) {
                    mPhotoLayout.setVisibility(View.VISIBLE);
                } else {
                    mPhotoLayout.setVisibility(View.GONE);
                }

                if (userInfo.getNoteList() != null && userInfo.getNoteList().size() > 0) {
                    mNoDataLayout.setVisibility(View.GONE);
                    mNoteListView.setVisibility(View.VISIBLE);
                    if (currentPage == 1) {
                        noteInfoAdapter.setNewData(userInfo.getNoteList());
                    } else {
                        noteInfoAdapter.addData(userInfo.getNoteList());
                    }

                    if (userInfo.getNoteList().size() == pageSize) {
                        noteInfoAdapter.loadMoreComplete();
                    } else {
                        noteInfoAdapter.loadMoreEnd(true);
                    }
                } else {
                    mNoDataLayout.setVisibility(View.VISIBLE);
                    mNoteListView.setVisibility(View.GONE);
                    mNoDataTitleTv.setText(isMyInfo ? "要想火，先发帖" : "这个人很神秘，什么都没有留下");
                    mSendNoteTv.setVisibility(isMyInfo ? View.VISIBLE : View.GONE);
                }

                Logger.i("background--->" + userInfo.getBackground());

                if (StringUtils.isEmpty(userInfo.getBackground())) {
                    mTopBgLayout.setBackgroundResource(R.mipmap.user_info_top_bg);
                } else {
                    Glide.with(this).asBitmap().load(userInfo.getBackground()).into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                            mTopBgLayout.setBackground(new BitmapDrawable(getResources(), resource));
                        }
                    });
                }
            }

            if (tData instanceof UpdateHeadRet) {
                if (!StringUtils.isEmpty(((UpdateHeadRet) tData).getData().getBackground())) {
                    userInfo.setBackground(((UpdateHeadRet) tData).getData().getBackground());
                    App.getApp().setmUserInfo(userInfo);
                    App.getApp().setLogin(true);
                    SPUtils.getInstance().put(Constants.USER_INFO, JSONObject.toJSONString(userInfo));
                    if (StringUtils.isEmpty(userInfo.getBackground())) {
                        mTopBgLayout.setBackgroundResource(R.mipmap.user_info_top_bg);
                    }
                    Glide.with(this).asBitmap().load(userInfo.getBackground()).into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                            mTopBgLayout.setBackground(new BitmapDrawable(getResources(), resource));
                        }
                    });
                }
            }

            if (isMakeDelete) {
                ToastUtils.showLong("删除成功");
                currentPage = 1;
                isMakeDelete = false;
                userInfoPresenterImp.getUserInfo(App.getApp().getmUserInfo().getId(), userId);
            }
        } else {
            ToastUtils.showLong(StringUtils.isEmpty(tData.getMsg()) ? "操作错误" : tData.getMsg());
        }
    }

    @Override
    public void loadDataError(Throwable throwable) {
        avi.hide();
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        popBackStack();
    }

    /**
     * 使用相机
     */
    private void takeCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        outputImage = new File(PathUtils.getExternalAppPicturesPath(), TimeUtils.getNowMills() + ".png");
        outputImage.getParentFile().mkdirs();

        Uri uri = null;
        Logger.i("currentApiVersion--->" + android.os.Build.VERSION.SDK_INT);
        if (android.os.Build.VERSION.SDK_INT < 24) {
            uri = Uri.fromFile(outputImage);
            imageUri = uri;
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(intent, TAKE_BIG_PICTURE);
        } else {
            ContentValues contentValues = new ContentValues(1);
            contentValues.put(MediaStore.Images.Media.DATA, outputImage.getAbsolutePath());
            uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            imageUri = uri;
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(intent, TAKE_BIG_PICTURE);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case TAKE_BIG_PICTURE:
                    if (imageUri == null) {
                        ToastUtils.showLong("数据异常，请重试");
                        break;
                    }
                    cropImageUri(imageUri, 1080, 390, CROP_SMALL_PICTURE);
                    break;
                case REQUEST_CODE_CHOOSE:
                    Logger.i(JSONObject.toJSONString(Matisse.obtainPathResult(data)));
                    if (Matisse.obtainResult(data) != null && Matisse.obtainResult(data).size() > 0) {
                        outputImage = new File(PathUtils.getExternalAppPicturesPath(), TimeUtils.getNowMills() + ".png");
                        imageUri = Uri.fromFile(outputImage);
                        cropImageUri(Matisse.obtainResult(data).get(0), 1080, 390, CROP_SMALL_PICTURE);
                    }
                    break;
                case CROP_SMALL_PICTURE:
                    Logger.i("crop out path--->" + outputImage.getAbsolutePath());

                    if (updateHeadBottomSheetDialog != null && updateHeadBottomSheetDialog.isShowing()) {
                        updateHeadBottomSheetDialog.dismiss();
                    }
                    if (progressDialog != null && !progressDialog.isShowing()) {
                        progressDialog.show();
                    }
                    updateHeadPresenterImp.updateBackground(userInfo != null ? userInfo.getId() : "", outputImage.getAbsolutePath());
                    break;
            }
        }

    }

    private void cropImageUri(Uri uri, int outputX, int outputY, int requestCode) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        //是否裁剪
        intent.putExtra("crop", "true");
        //设置xy的裁剪比例
        intent.putExtra("aspectX", 36);
        intent.putExtra("aspectY", 13);
        //设置输出的宽高
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        //是否缩放
        intent.putExtra("scale", false);
        //输入图片的Uri，指定以后，可以在这个uri获得图片
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        //是否返回图片数据，可以不用，直接用uri就可以了
        intent.putExtra("return-data", false);
        //设置输入图片格式
        intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());
        //是否关闭面部识别
        intent.putExtra("noFaceDetection", true); // no face detection
        //启动
        startActivityForResult(intent, requestCode);
    }


    @Override
    public void onClick(View view) {

        if (updateBgDialog != null && updateBgDialog.isShowing()) {
            updateBgDialog.dismiss();
        }

        if (updateHeadBottomSheetDialog != null && updateHeadBottomSheetDialog.isShowing()) {
            updateHeadBottomSheetDialog.dismiss();
        }

        switch (view.getId()) {
            case R.id.layout_top_item:
                if (isMyInfo) {
                    //ToastUtils.showLong("更换背景图");
                    if (updateHeadBottomSheetDialog != null && !updateHeadBottomSheetDialog.isShowing()) {
                        updateHeadBottomSheetDialog.show();
                    }
                } else {
                    Intent intent = new Intent(this, ReportInfoActivity.class);
                    intent.putExtra("rid", userId);
                    intent.putExtra("report_type", 1);
                    startActivity(intent);
                }
                break;
            case R.id.layout_update_cancel:

                break;

            case R.id.layout_camera:
                takeCamera();
                break;
            case R.id.layout_local_photo:
                Matisse.from(UserInfoActivity.this)
                        .choose(MimeType.ofImage())
                        .countable(true)
                        .maxSelectable(1)
                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                        .thumbnailScale(0.85f)
                        .imageEngine(new Glide4Engine())
                        .forResult(REQUEST_CODE_CHOOSE);
                break;
            case R.id.layout_cancel:
                if (bottomSheetDialog != null && bottomSheetDialog.isShowing()) {
                    bottomSheetDialog.dismiss();
                }
                break;
            case R.id.layout_delete:
                if (bottomSheetDialog != null && bottomSheetDialog.isShowing()) {
                    bottomSheetDialog.dismiss();
                }

                if (configDialog != null && !configDialog.isShowing()) {
                    configDialog.show();
                }

                break;
            default:
                break;
        }
    }

    @Override
    public void config() {
        isMakeDelete = true;
        deleteNotePresenterImp.deleteNote(userInfo != null ? userInfo.getId() : "", noteInfoAdapter.getData().get(currentItemPos).getId());
    }

    @Override
    public void cancel() {
        if (configDialog != null && configDialog.isShowing()) {
            configDialog.dismiss();
        }
    }
}
