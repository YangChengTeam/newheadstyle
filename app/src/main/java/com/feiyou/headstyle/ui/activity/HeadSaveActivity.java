package com.feiyou.headstyle.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.PathUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.feiyou.headstyle.App;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.AddCollectionRet;
import com.feiyou.headstyle.bean.ResultInfo;
import com.feiyou.headstyle.bean.UpdateHeadRet;
import com.feiyou.headstyle.bean.UserInfo;
import com.feiyou.headstyle.common.Constants;
import com.feiyou.headstyle.presenter.UpdateHeadPresenterImp;
import com.feiyou.headstyle.ui.base.BaseFragmentActivity;
import com.feiyou.headstyle.ui.custom.qqhead.BaseUIListener;
import com.feiyou.headstyle.view.HeadListDataView;
import com.orhanobut.logger.Logger;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.tencent.connect.avatar.QQAvatar;
import com.tencent.tauth.Tencent;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by myflying on 2018/11/23.
 */
public class HeadSaveActivity extends BaseFragmentActivity implements HeadListDataView {

    @BindView(R.id.topbar)
    QMUITopBar mTopBar;

    ImageView mBackImageView;

    TextView mTitleTv;

    TextView mConfigTv;

    BottomSheetDialog bottomSheetDialog;

    ImageView mCloseImageView;

    @BindView(R.id.iv_result)
    ImageView mResultImageView;

    private String tempFilePath;

    BottomSheetDialog settingDialog;

    private int loginType = 1;

    private ProgressDialog progressDialog = null;

    private Tencent mTencent;

    private UMShareAPI mShareAPI = null;

    UpdateHeadPresenterImp updateHeadPresenterImp;

    private UserInfo userInfo;

    @Override
    protected int getContextViewId() {
        return R.layout.activity_head_save;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTopBar();
        initDialog();
        initData();
    }

    private void initTopBar() {
        QMUIStatusBarHelper.setStatusBarLightMode(this);

        View topSaveView = getLayoutInflater().inflate(R.layout.common_top_config, null);
        topSaveView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(48)));
        mTitleTv = topSaveView.findViewById(R.id.tv_config_title);
        mConfigTv = topSaveView.findViewById(R.id.tv_config);
        mTitleTv.setText("已保存");
        mConfigTv.setVisibility(View.INVISIBLE);

        mTopBar.setCenterView(topSaveView);
        mBackImageView = topSaveView.findViewById(R.id.iv_back);
        mBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });
        userInfo = App.getApp().getmUserInfo();
    }

    public void initDialog() {

        ToastUtils.showLong("图片已保存到图库");

        mShareAPI = UMShareAPI.get(this);
        mTencent = Tencent.createInstance("1105592461", this.getApplicationContext());

        Bundle bundle = getIntent().getExtras();
        if (bundle != null && !StringUtils.isEmpty(bundle.getString("file_path"))) {
            tempFilePath = bundle.getString("file_path");
            Glide.with(this).load(new File(tempFilePath)).into(mResultImageView);
        }

        loginType = App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getLoginType() : 1;

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在设置");

        settingDialog = new BottomSheetDialog(this);
        View setView = LayoutInflater.from(this).inflate(R.layout.set_head_dialog, null);
        setView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(210)));
        mCloseImageView = setView.findViewById(R.id.iv_close_setting);

        LinearLayout mSettingTypeLayout = setView.findViewById(R.id.layout_setting_type);
        LinearLayout mSettingAppLayout = setView.findViewById(R.id.layout_set_app_head);

        TextView mSettingTv = setView.findViewById(R.id.tv_setting_type_name);

        if (loginType == 2) {
            mSettingTypeLayout.setBackgroundResource(R.drawable.setting_weixin_bg);
            mSettingTv.setText("设为微信头像");
        } else {
            mSettingTypeLayout.setBackgroundResource(R.drawable.setting_qq_bg);
            mSettingTv.setText("设为QQ头像");
        }

        settingDialog.setContentView(setView);
        mCloseImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (settingDialog != null && settingDialog.isShowing()) {
                    settingDialog.dismiss();
                }
            }
        });

        mSettingTypeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (settingDialog != null && settingDialog.isShowing()) {
                    settingDialog.dismiss();
                }
                if (loginType == 2) {
                    ToastUtils.showLong("图片已保存到图库");
                } else {
                    if (App.isLoginAuth && !StringUtils.isEmpty(tempFilePath)) {
                        Uri uri = Uri.parse("file://" + tempFilePath);
                        doSetAvatar(uri);
                    } else {
                        if (progressDialog != null && !progressDialog.isShowing()) {
                            progressDialog.show();
                        }
                        mShareAPI.getPlatformInfo(HeadSaveActivity.this, SHARE_MEDIA.QQ, authListener);
                    }
                }
            }
        });

        mSettingAppLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (settingDialog != null && settingDialog.isShowing()) {
                    settingDialog.dismiss();
                }
                if (progressDialog != null && !progressDialog.isShowing()) {
                    progressDialog.show();
                }
                updateHeadPresenterImp.updateHead(userInfo != null ? userInfo.getId() : "", tempFilePath);
            }
        });
    }

    public void initData() {
        bottomSheetDialog = new BottomSheetDialog(this);
        View shareView = LayoutInflater.from(this).inflate(R.layout.share_dialog_view, null);
        mCloseImageView = shareView.findViewById(R.id.iv_close_share);

        bottomSheetDialog.setContentView(shareView);

        mCloseImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bottomSheetDialog != null && bottomSheetDialog.isShowing()) {
                    bottomSheetDialog.dismiss();
                }
            }
        });
        bottomSheetDialog.show();
        updateHeadPresenterImp = new UpdateHeadPresenterImp(this, this);
    }

    @OnClick(R.id.layout_setting)
    void settingHead() {
        if (settingDialog != null && !settingDialog.isShowing()) {
            settingDialog.show();
        }
    }

    @OnClick(R.id.layout_home)
    void home() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.layout_add_note)
    void addNote() {
        Intent intent = new Intent(this, PushNoteActivity.class);
        intent.putExtra("file_path", tempFilePath);
        startActivity(intent);
    }

    @OnClick(R.id.btn_share)
    public void openShareDialog() {
        if (bottomSheetDialog != null && !bottomSheetDialog.isShowing()) {
            bottomSheetDialog.show();
        }
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
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

        if (tData != null && tData.getCode() == Constants.SUCCESS) {
            if (tData instanceof UpdateHeadRet) {
                if (!StringUtils.isEmpty(((UpdateHeadRet) tData).getData().getImage())) {
                    ToastUtils.showLong("设置成功");
                    userInfo.setUserimg(((UpdateHeadRet) tData).getData().getImage());

                    App.getApp().setmUserInfo(userInfo);
                    App.getApp().setLogin(true);
                    SPUtils.getInstance().put(Constants.USER_INFO, JSONObject.toJSONString(userInfo));
                }
            }

        } else {
            if (tData instanceof UpdateHeadRet) {
                ToastUtils.showLong("设置失败");
            }
        }
    }

    @Override
    public void loadDataError(Throwable throwable) {

    }

    private void doSetAvatar(Uri uri) {
        QQAvatar qqAvatar = new QQAvatar(mTencent.getQQToken());
        qqAvatar.setAvatar(this, uri, new BaseUIListener(this), R.anim.zoomout);
    }

    UMAuthListener authListener = new UMAuthListener() {
        /**
         * @desc 授权开始的回调
         * @param platform 平台名称
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        /**
         * @desc 授权成功的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         * @param data 用户资料返回
         */
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            Logger.i(JSONObject.toJSONString(data));
            //Toast.makeText(mContext, "授权成功了", Toast.LENGTH_LONG).show();
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            App.isLoginAuth = true;
            if (!StringUtils.isEmpty(tempFilePath)) {
                Uri uri = Uri.parse("file://" + tempFilePath);
                doSetAvatar(uri);
            }
        }

        /**
         * @desc 授权失败的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            Toast.makeText(HeadSaveActivity.this, "授权失败：" + t.getMessage(), Toast.LENGTH_LONG).show();
        }

        /**
         * @desc 授权取消的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         */
        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Toast.makeText(HeadSaveActivity.this, "授权取消了", Toast.LENGTH_LONG).show();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }


}
