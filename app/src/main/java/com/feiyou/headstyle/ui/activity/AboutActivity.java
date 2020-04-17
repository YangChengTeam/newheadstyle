package com.feiyou.headstyle.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.ui.adapter.BlackListAdapter;
import com.feiyou.headstyle.ui.base.BaseFragmentActivity;
import com.orhanobut.logger.Logger;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
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
public class AboutActivity extends BaseFragmentActivity implements View.OnClickListener {

    @BindView(R.id.topbar)
    QMUITopBar mTopBar;

    ImageView mBackImageView;

    @BindView(R.id.tv_version_name)
    TextView mVersionNameTv;

    private UMShareAPI mShareAPI = null;

    BottomSheetDialog shareDialog;

    private ShareAction shareAction;

    @Override
    protected int getContextViewId() {
        return R.layout.activity_about;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTopBar();
        initData();
    }

    private void initTopBar() {
        QMUIStatusBarHelper.setStatusBarLightMode(this);
        View aboutView = getLayoutInflater().inflate(R.layout.common_top_back, null);
        aboutView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(48)));
        TextView titleTv = aboutView.findViewById(R.id.tv_title);
        titleTv.setText("关于我们");

        mTopBar.setCenterView(aboutView);
        mBackImageView = aboutView.findViewById(R.id.iv_back);
        mBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Logger.i("about onresume--->");
    }

    public void initData() {
        //初始化分享弹窗
        shareDialog = new BottomSheetDialog(this);
        View shareView = LayoutInflater.from(this).inflate(R.layout.share_dialog_view, null);
        ImageView closeIv = shareView.findViewById(R.id.iv_close_share);
        LinearLayout weixinLayout = shareView.findViewById(R.id.layout_weixin);
        LinearLayout circleLayout = shareView.findViewById(R.id.layout_circle);
        LinearLayout qqLayout = shareView.findViewById(R.id.layout_qq_friends);
        LinearLayout qzoneLayout = shareView.findViewById(R.id.layout_qzone);
        weixinLayout.setOnClickListener(this);
        circleLayout.setOnClickListener(this);
        qqLayout.setOnClickListener(this);
        qzoneLayout.setOnClickListener(this);
        closeIv.setOnClickListener(this);
        shareDialog.setContentView(shareView);

        mVersionNameTv.setText("当前版本：" + AppUtils.getAppVersionName());

        if (shareAction == null) {
            shareAction = new ShareAction(this);
            shareAction.setCallback(shareListener);//回调监听器

            UMWeb web = new UMWeb("http://gx.qqtn.com");
            web.setTitle("超好用的个性头像，期待你的加入!");//标题
            web.setThumb(new UMImage(this, R.drawable.app_share));  //缩略图
            web.setDescription("为您提供2019年精选高清头像，有情侣头像、男生头像、女生头像、动漫头像、闺蜜头像等");//描述
            shareAction.withMedia(web);
        }
    }

    @OnClick(R.id.btn_share)
    void share() {
        if (shareDialog != null && !shareDialog.isShowing()) {
            shareDialog.show();
        }
    }

    @OnClick(R.id.btn_join_us)
    void joinUs() {
        joinQQGroup("KFPYG3jYKt9Z482w3KquNogfuz7ioPey");
    }

    /****************
     * 发起添加群流程。群号：个性头像(302826961) 的 key 为： KFPYG3jYKt9Z482w3KquNogfuz7ioPey
     * 调用 joinQQGroup(KFPYG3jYKt9Z482w3KquNogfuz7ioPey) 即可发起手Q客户端申请加群 个性头像(302826961)
     *
     * @param key 由官网生成的key
     * @return 返回true表示呼起手Q成功，返回fals表示呼起失败
     ******************/
    public boolean joinQQGroup(String key) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            startActivity(intent);
            return true;
        } catch (Exception e) {
            // 未安装手Q或安装的版本不支持
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        popBackStack();
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
            //Toast.makeText(HeadShowActivity.this, "分享成功", Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            dismissShareView();
            //Toast.makeText(HeadShowActivity.this, "分享失败", Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            dismissShareView();
            //Toast.makeText(HeadShowActivity.this, "取消分享", Toast.LENGTH_LONG).show();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View view) {
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

    @OnClick(R.id.tv_user_xieyi)
    void xieyi() {
        Intent intent = new Intent(this, PrivaryActivity.class);
        intent.putExtra("show_type",1);
        startActivity(intent);
    }

    @OnClick(R.id.tv_privacy)
    void privacy() {
        Intent intent = new Intent(this, PrivaryActivity.class);
        intent.putExtra("show_type",2);
        startActivity(intent);
    }
}
