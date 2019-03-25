package com.feiyou.headstyle.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.PathUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feiyou.headstyle.App;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.TestInfoRet;
import com.feiyou.headstyle.common.Constants;
import com.feiyou.headstyle.presenter.TestInfoPresenterImp;
import com.feiyou.headstyle.ui.adapter.TestInfoAdapter;
import com.feiyou.headstyle.ui.base.BaseFragmentActivity;
import com.feiyou.headstyle.ui.custom.GlideRoundTransform;
import com.feiyou.headstyle.view.TestInfoView;
import com.orhanobut.logger.Logger;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import java.io.File;
import java.io.FileNotFoundException;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by myflying on 2018/11/23.
 */
public class TestResultActivity extends BaseFragmentActivity implements TestInfoView, View.OnClickListener {

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

    private String imageUrl;

    private String noCodeImageUrl;

    private TestInfoPresenterImp testInfoPresenterImp;

    private TestInfoAdapter testInfoAdapter;

    private ShareAction shareAction;

    BottomSheetDialog shareDialog;

    private String filePath;

    private Bitmap tempBitmap;

    private int fromType = 1;

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
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            fromType = bundle.getInt("from_type", 1);
        }

        if (bundle != null && bundle.getString("image_url") != null) {
            imageUrl = bundle.getString("image_url");
        }

        if (bundle != null && bundle.getString("nocode_image_url") != null) {
            noCodeImageUrl = bundle.getString("nocode_image_url");
        }

        if (!StringUtils.isEmpty(noCodeImageUrl)) {
            RequestOptions options = new RequestOptions();
            options.transform(new GlideRoundTransform(this, 12));
            Glide.with(this).load(noCodeImageUrl).into(mTestResultImageView);
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
        testInfoAdapter = new TestInfoAdapter(this, null);
        mRecommendListView.setLayoutManager(new LinearLayoutManager(this));
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


        //推荐列表
        testInfoPresenterImp.getHotAndRecommendList(2);
    }

    @OnClick(R.id.btn_share)
    void share() {
        if (shareDialog != null && !shareDialog.isShowing()) {
            shareDialog.show();
        }
    }

    @OnClick(R.id.btn_save)
    void save() {
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
                MediaStore.Images.Media.insertImage(getContentResolver(),
                        filePath, filePath.substring(filePath.lastIndexOf("/") + 1, filePath.length()), null);

                MediaScannerConnection.scanFile(TestResultActivity.this, new String[]{filePath}, null, null);
                // 最后通知图库更新
                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + filePath)));
                ToastUtils.showLong("已保存到图库");
            } else {
                flag = false;
            }
        } catch (FileNotFoundException e) {
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
    public void loadDataSuccess(TestInfoRet tData) {
        Logger.i("test result --->" + JSON.toJSONString(tData));

        if (tData != null && tData.getCode() == Constants.SUCCESS) {
            if (tData.getData() != null && tData.getData().size() > 6) {
                testInfoAdapter.setNewData(tData.getData().subList(0, 6));
            } else {
                testInfoAdapter.setNewData(tData.getData());
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
}
