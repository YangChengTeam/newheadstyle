package com.feiyou.headstyle.ui.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.PathUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feiyou.headstyle.App;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.base.IBaseView;
import com.feiyou.headstyle.bean.TaskRecordInfoRet;
import com.feiyou.headstyle.bean.TestInfoRet;
import com.feiyou.headstyle.common.Constants;
import com.feiyou.headstyle.presenter.TaskRecordInfoPresenterImp;
import com.feiyou.headstyle.presenter.TestInfoPresenterImp;
import com.feiyou.headstyle.ui.adapter.TestInfoAdapter;
import com.feiyou.headstyle.ui.base.BaseFragmentActivity;
import com.feiyou.headstyle.ui.custom.Glide4Engine;
import com.feiyou.headstyle.ui.custom.NormalDecoration;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.jakewharton.rxbinding.view.RxView;
import com.orhanobut.logger.Logger;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;

import java.io.File;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;
import rx.functions.Action1;

/**
 * Created by myflying on 2018/11/23.
 */
@RuntimePermissions
public class TestResultActivity extends BaseFragmentActivity implements IBaseView, View.OnClickListener {

    @BindView(R.id.topbar)
    QMUITopBar mTopBar;

    ImageView mBackImageView;

    @BindView(R.id.recommend_list)
    RecyclerView mRecommendListView;

    @BindView(R.id.iv_test_result)
    ImageView mTestResultImageView;

    @BindView(R.id.btn_share)
    Button mShareButton;

    @BindView(R.id.btn_save)
    Button mSaveButton;

    @BindView(R.id.btn_test_again)
    Button mTestAgainButton;

    @BindView(R.id.tv_more)
    TextView mMoreTv;

    private String imageUrl;

    private String noCodeImageUrl;

    private TestInfoPresenterImp testInfoPresenterImp;

    private TestInfoAdapter testInfoAdapter;

    private ShareAction shareAction;

    BottomSheetDialog shareDialog;

    private String filePath;

    private Bitmap tempBitmap;

    private int fromType = 1;

    private ProgressDialog progressDialog = null;

    private String taskId = "5";

    private int goldNum = 0;

    TaskRecordInfoPresenterImp taskRecordInfoPresenterImp;

    private String recordId;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        TestResultActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void showReadStorage() {
        save();
    }

    @OnPermissionDenied(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void onReadStorageDenied() {
        Toasty.normal(this, "请授权存储权限后保存图片").show();
    }

    @OnShowRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void showRationaleForReadStorage(PermissionRequest request) {
        request.proceed();
    }

    @OnNeverAskAgain(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void onReadStorageNeverAskAgain() {
        Toasty.normal(this, "请手动开启存储权限后保存图片").show();
    }


    @Override
    protected int getContextViewId() {
        return R.layout.activity_test_result;
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
        titleTv.setText("测试结果");

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
        MobclickAgent.onEvent(this, "test_result", AppUtils.getAppVersionName());

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            fromType = bundle.getInt("from_type", 1);

            if (bundle.getString("image_url") != null) {
                imageUrl = bundle.getString("image_url");
            }

            if (bundle.getString("nocode_image_url") != null) {
                noCodeImageUrl = bundle.getString("nocode_image_url");
            }

            if (bundle.getString("record_id") != null) {
                recordId = bundle.getString("record_id");
            }
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在保存");
        progressDialog.setCanceledOnTouchOutside(false);

        if (!StringUtils.isEmpty(noCodeImageUrl)) {
//            RequestOptions options = new RequestOptions();
//            options.transform(new GlideRoundTransform(this, 12));
//            Glide.with(this).load(noCodeImageUrl).into(mTestResultImageView);

            Glide.with(this).asBitmap().load(noCodeImageUrl).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                    Logger.i("www--->" + resource.getWidth() + "hhh--->" + resource.getHeight());

                    double rw = resource.getWidth();
                    double rh = resource.getHeight();

                    double oh = ScreenUtils.getScreenHeight() * 0.6;
                    double temp = rw / rh * oh;
                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions.override((int) temp, (int) oh);

                    Glide.with(TestResultActivity.this).load(resource).apply(requestOptions).into(mTestResultImageView);
                }
            });

        }

        if (shareAction == null) {
            shareAction = new ShareAction(this);
            shareAction.setCallback(shareListener);//回调监听器
        }
        //初始化分享弹窗
        shareDialog = new BottomSheetDialog(this);
        View shareView = LayoutInflater.from(this).inflate(R.layout.share_dialog_view, null);
        ImageView closeDialog = shareView.findViewById(R.id.iv_close_share);
        LinearLayout weixinLayout = shareView.findViewById(R.id.layout_weixin);
        LinearLayout circleLayout = shareView.findViewById(R.id.layout_circle);
        LinearLayout qqLayout = shareView.findViewById(R.id.layout_qq_friends);
        LinearLayout qzoneLayout = shareView.findViewById(R.id.layout_qzone);
        weixinLayout.setOnClickListener(this);
        circleLayout.setOnClickListener(this);
        qqLayout.setOnClickListener(this);
        qzoneLayout.setOnClickListener(this);
        closeDialog.setOnClickListener(this);
        shareDialog.setContentView(shareView);

        Glide.with(this).asBitmap().load(imageUrl).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                tempBitmap = resource;
            }
        });

        testInfoPresenterImp = new TestInfoPresenterImp(this, this);
        taskRecordInfoPresenterImp = new TaskRecordInfoPresenterImp(this, this);

        testInfoAdapter = new TestInfoAdapter(this, null);
        mRecommendListView.setLayoutManager(new LinearLayoutManager(this));
        mRecommendListView.addItemDecoration(new NormalDecoration(ContextCompat.getColor(this, R.color.line_color), 1));
        mRecommendListView.setAdapter(testInfoAdapter);
        testInfoAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                if (testInfoAdapter.getData().get(position).getTestType() == 1) {
                    Intent intent = new Intent(TestResultActivity.this, TestDetailActivity.class);
                    intent.putExtra("tid", testInfoAdapter.getData().get(position).getId());
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(TestResultActivity.this, TestImageDetailActivity.class);
                    intent.putExtra("tid", testInfoAdapter.getData().get(position).getId());
                    startActivity(intent);
                    finish();
                }
            }
        });

        //保存海报
        RxView.clicks(mSaveButton).throttleFirst(300, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                TestResultActivityPermissionsDispatcher.showReadStorageWithPermissionCheck(TestResultActivity.this);
            }
        });

        //推荐列表
        testInfoPresenterImp.getHotAndRecommendList(2);

        if (!StringUtils.isEmpty(recordId)) {
            String openid = App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getOpenid() : "";
            taskRecordInfoPresenterImp.addTaskRecord(App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "", openid, taskId, goldNum, 0, 1, recordId);
        }
    }

    @OnClick(R.id.tv_more)
    void moreTest() {
        Intent intent = new Intent(this, MoreTestActivity.class);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.btn_share)
    void share() {
        if (shareDialog != null && !shareDialog.isShowing()) {
            shareDialog.show();
        }
    }

    //保存图片
    public void save() {
        if (progressDialog != null && !progressDialog.isShowing()) {
            progressDialog.show();
        }

        Glide.with(this).asBitmap().load(imageUrl).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                //加载成功，resource为加载到的bitmap
                filePath = PathUtils.getExternalPicturesPath() + File.separator + TimeUtils.getNowMills() + ".jpg";
                Logger.i("save test result --->" + filePath);
                boolean isSave = ImageUtils.save(resource, filePath, Bitmap.CompressFormat.JPEG);
                if (isSave) {
                    saveImageToGallery();
                }
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        });
    }

    @OnClick(R.id.btn_test_again)
    void testAgain() {
        if (fromType == 1) {
            Intent intent = new Intent(this, TestDetailActivity.class);
            intent.putExtra("tid", App.getApp().getTestInfo().getTestId());
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(this, TestImageDetailActivity.class);
            intent.putExtra("tid", App.getApp().getTestInfo().getTestId());
            startActivity(intent);
            finish();
        }
    }

    // 其次把文件插入到系统图库
    public boolean saveImageToGallery() {
        boolean flag = true;
        try {
            if (!StringUtils.isEmpty(filePath)) {
//                MediaStore.Images.Media.insertImage(getContentResolver(),
//                        filePath, filePath.substring(filePath.lastIndexOf("/") + 1, filePath.length()), null);

                MediaScannerConnection.scanFile(TestResultActivity.this, new String[]{filePath}, null, null);
                // 最后通知图库更新
                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + filePath)));
                ToastUtils.showLong("已保存到图库");
            } else {
                flag = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        }
        return flag;
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
    public void loadDataSuccess(Object tData) {
        Logger.i("test result --->" + JSON.toJSONString(tData));

        if (tData != null) {
            if (tData instanceof TestInfoRet) {
                if (((TestInfoRet) tData).getCode() == Constants.SUCCESS) {
                    if (((TestInfoRet) tData).getData() != null && ((TestInfoRet) tData).getData().size() > 6) {
                        testInfoAdapter.setNewData(((TestInfoRet) tData).getData().subList(0, 6));
                    } else {
                        testInfoAdapter.setNewData(((TestInfoRet) tData).getData());
                    }
                }
            }

            if (tData instanceof TaskRecordInfoRet) {
                if (((TaskRecordInfoRet) tData).getCode() == Constants.SUCCESS) {
                    if (((TaskRecordInfoRet) tData).getData() != null) {
                        ToastUtils.showLong("领取成功 +" + ((TaskRecordInfoRet) tData).getData().getGoldnum() + "金币");
                    }
                } else {
                    finish();
                }
            }
        }
    }

    @Override
    public void loadDataError(Throwable throwable) {

    }

    private UMShareListener shareListener = new UMShareListener() {
        /**
         * @descrption 分享开始的回调
         * @param platform 平台类型
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        /**
         * @descrption 分享成功的回调
         * @param platform 平台类型
         */
        @Override
        public void onResult(SHARE_MEDIA platform) {
            dismissShareView();
            Toast.makeText(TestResultActivity.this, "分享成功", Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            dismissShareView();
            Toast.makeText(TestResultActivity.this, "分享失败", Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            dismissShareView();
            Toast.makeText(TestResultActivity.this, "取消分享", Toast.LENGTH_LONG).show();
        }
    };


    @Override
    public void onClick(View view) {
        UMImage noImage = new UMImage(TestResultActivity.this, R.drawable.app_share);
        if (StringUtils.isEmpty(imageUrl)) {
            shareAction.withMedia(noImage);
        } else {
            UMImage image = new UMImage(TestResultActivity.this, imageUrl);
            if (tempBitmap != null) {
                UMImage scaImage = new UMImage(TestResultActivity.this, ImageUtils.compressByScale(tempBitmap, 300, 300));
                image.setThumb(scaImage);
            } else {
                image.setThumb(noImage);
            }
            shareAction.withMedia(image);
        }

        switch (view.getId()) {
            case R.id.layout_weixin:
                shareAction.setPlatform(SHARE_MEDIA.WEIXIN).share();
                break;
            case R.id.layout_circle:
                shareAction.setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE).share();
                break;
            case R.id.layout_qq_friends:
                shareAction.setPlatform(SHARE_MEDIA.QQ).share();
                break;
            case R.id.layout_qzone:
                shareAction.setPlatform(SHARE_MEDIA.QZONE).share();
                break;
            case R.id.iv_close_share:
                dismissShareView();
                break;
            default:
                break;
        }
    }

    public void dismissShareView() {
        if (shareDialog != null && shareDialog.isShowing()) {
            shareDialog.dismiss();
        }
    }

    public class TransformationUtils extends ImageViewTarget<Bitmap> {

        private ImageView target;

        public TransformationUtils(ImageView target) {
            super(target);
            this.target = target;
        }

        @Override
        protected void setResource(Bitmap resource) {
            view.setImageBitmap(resource);

            //获取原图的宽高
            int width = resource.getWidth();
            int height = resource.getHeight();

            //获取imageView的宽
            int imageViewWidth = target.getWidth();

            //计算缩放比例
            float sy = (float) (imageViewWidth * 0.1) / (float) (width * 0.1);

            //计算图片等比例放大后的高
            int imageViewHeight = (int) (height * sy);
            ViewGroup.LayoutParams params = target.getLayoutParams();
            params.height = imageViewHeight;
            target.setLayoutParams(params);
        }
    }
}
