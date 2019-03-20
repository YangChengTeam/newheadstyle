package com.feiyou.headstyle.ui.activity;

import android.os.Bundle;
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
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.feiyou.headstyle.App;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.TestInfoRet;
import com.feiyou.headstyle.common.Constants;
import com.feiyou.headstyle.presenter.TestInfoPresenterImp;
import com.feiyou.headstyle.ui.adapter.BlackListAdapter;
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
import com.umeng.socialize.media.UMWeb;

import java.util.ArrayList;
import java.util.List;

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


        testInfoPresenterImp = new TestInfoPresenterImp(this, this);
        testInfoAdapter = new TestInfoAdapter(this, null);
        mRecommendListView.setLayoutManager(new LinearLayoutManager(this));
        mRecommendListView.setAdapter(testInfoAdapter);
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

    }

    @OnClick(R.id.btn_test_again)
    void testAgain() {

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
        String shareTitle = "一个最近很火的心理测试，准到可怕！";
        String shareContent = "最近很火的心理测试，据说非常准，测过的人都说可怕...";
        String shareImageUrl = "";
        if (App.getApp().getTestInfo() != null) {
            if (App.getApp().getTestInfo().getShare_title() != null && App.getApp().getTestInfo().getShare_title().length > 0) {
                shareTitle = App.getApp().getTestInfo().getShare_title()[0];
            }
            if (!StringUtils.isEmpty(App.getApp().getTestInfo().getImage())) {
                shareImageUrl = App.getApp().getTestInfo().getImage();
            }
            if (!StringUtils.isEmpty(App.getApp().getTestInfo().getDesc())) {
                shareContent = App.getApp().getTestInfo().getDesc();
            }
        }

        UMWeb web = new UMWeb("http://gx.qqtn.com");
        if (shareAction != null) {
            UMImage image = new UMImage(TestResultActivity.this, R.drawable.app_share);
            if (!StringUtils.isEmpty(shareImageUrl)) {
                image = new UMImage(TestResultActivity.this, shareImageUrl);
                image.compressStyle = UMImage.CompressStyle.QUALITY;
            }
            web.setTitle(shareTitle);//标题
            web.setThumb(image);  //缩略图
            web.setDescription(shareContent);//描述
        }

        switch (view.getId()) {
            case R.id.layout_weixin:
                shareAction.withMedia(web).setPlatform(SHARE_MEDIA.WEIXIN).share();
                break;
            case R.id.layout_circle:
                shareAction.withMedia(web).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE).share();
                break;
            case R.id.layout_qq_friends:
                shareAction.withMedia(web).setPlatform(SHARE_MEDIA.QQ).share();
                break;
            case R.id.layout_qzone:
                shareAction.withMedia(web).setPlatform(SHARE_MEDIA.QZONE).share();
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
