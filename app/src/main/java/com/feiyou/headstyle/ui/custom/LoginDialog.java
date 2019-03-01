package com.feiyou.headstyle.ui.custom;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.feiyou.headstyle.App;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.LoginRequest;
import com.feiyou.headstyle.bean.MessageEvent;
import com.feiyou.headstyle.bean.UserInfo;
import com.feiyou.headstyle.bean.UserInfoRet;
import com.feiyou.headstyle.common.Constants;
import com.feiyou.headstyle.presenter.UserInfoPresenterImp;
import com.feiyou.headstyle.view.UserInfoView;
import com.orhanobut.logger.Logger;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

public class LoginDialog extends Dialog implements View.OnClickListener, UserInfoView {

    private Context mContext;

    private LinearLayout mWeiXinLayout;

    private LinearLayout mQQLayout;

    private ImageView mCloseImageView;

    private UserInfoPresenterImp userInfoPresenterImp;

    private UMShareAPI mShareAPI = null;

    private ProgressDialog progressDialog = null;

    public LoginDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public LoginDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_dialog_view);
        setCanceledOnTouchOutside(false);

        mShareAPI = UMShareAPI.get(mContext);
        userInfoPresenterImp = new UserInfoPresenterImp(this, mContext);
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("正在登录");

        initView();
    }

    private void initView() {
        mWeiXinLayout = findViewById(R.id.layout_weixin);
        mQQLayout = findViewById(R.id.layout_qq);
        mCloseImageView = findViewById(R.id.iv_close);
        mWeiXinLayout.setOnClickListener(this);
        mQQLayout.setOnClickListener(this);
        mCloseImageView.setOnClickListener(this);
        setCanceledOnTouchOutside(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_weixin:
                if (progressDialog != null && !progressDialog.isShowing()) {
                    progressDialog.show();
                }

                mShareAPI.getPlatformInfo((Activity) mContext, SHARE_MEDIA.WEIXIN, authListener);
                this.dismiss();
                break;
            case R.id.layout_qq:
                if (progressDialog != null && !progressDialog.isShowing()) {
                    progressDialog.show();
                }

                mShareAPI.getPlatformInfo((Activity) mContext, SHARE_MEDIA.QQ, authListener);
                this.dismiss();
                break;
            case R.id.iv_close:
                this.dismiss();
                break;
        }
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
    public void loadDataSuccess(UserInfoRet tData) {
        if (tData != null && tData.getCode() == Constants.SUCCESS) {
            UserInfo userInfo = tData.getData();
            ToastUtils.showLong("登录成功");
            App.getApp().setmUserInfo(userInfo);
            App.getApp().setLogin(true);
            SPUtils.getInstance().put(Constants.USER_INFO, JSONObject.toJSONString(tData.getData()));
            EventBus.getDefault().post(new MessageEvent("login_success"));
        } else {
            ToastUtils.showLong(StringUtils.isEmpty(tData.getMsg()) ? "登录失败" : tData.getMsg());
        }
    }

    @Override
    public void loadDataError(Throwable throwable) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
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

            if (data != null) {
                if (progressDialog != null && !progressDialog.isShowing()) {
                    progressDialog.show();
                }

                LoginRequest loginRequest = new LoginRequest();
                loginRequest.setOpenid(data.get("uid").toUpperCase());//openid全部大写
                loginRequest.setType(2 + "");
                loginRequest.setNickname(data.get("name"));
                loginRequest.setSex(data.get("gender").equals("男") ? "1" : "2");
                loginRequest.setUserimg(data.get("iconurl"));
                loginRequest.setImeil(DeviceUtils.getAndroidID());
                userInfoPresenterImp.login(loginRequest);
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
            Toast.makeText(mContext, "授权失败：" + t.getMessage(), Toast.LENGTH_LONG).show();
        }

        /**
         * @desc 授权取消的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         */
        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Toast.makeText(mContext, "授权取消了", Toast.LENGTH_LONG).show();
        }
    };

}