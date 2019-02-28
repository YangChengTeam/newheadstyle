package com.feiyou.headstyle.ui.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.LoginRequest;
import com.feiyou.headstyle.bean.UserInfo;
import com.feiyou.headstyle.bean.UserInfoRet;
import com.feiyou.headstyle.common.Constants;
import com.feiyou.headstyle.presenter.UserInfoPresenterImp;
import com.feiyou.headstyle.ui.activity.MyMessageActivity;
import com.feiyou.headstyle.ui.activity.MyNoteActivity;
import com.feiyou.headstyle.ui.activity.SettingActivity;
import com.feiyou.headstyle.ui.activity.UserInfoActivity;
import com.feiyou.headstyle.ui.base.BaseFragment;
import com.feiyou.headstyle.ui.custom.GlideRoundTransform;
import com.feiyou.headstyle.ui.custom.LoginDialog;
import com.feiyou.headstyle.view.UserInfoView;
import com.orhanobut.logger.Logger;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by myflying on 2019/1/24.
 */
public class MyFragment extends BaseFragment implements UserInfoView {

    @BindView(R.id.layout_my_info_top)
    RelativeLayout mMyInfoTopLayout;

    @BindView(R.id.layout_user_info)
    LinearLayout mUserInfoLayout;

    @BindView(R.id.iv_user_head)
    ImageView mUserHeadImageView;

    @BindView(R.id.tv_user_nick_name)
    TextView mUserNickNameTv;

    @BindView(R.id.tv_user_id)
    TextView mUserIdTv;

    @BindView(R.id.tv_follow_count)
    TextView mFollowTv;

    @BindView(R.id.tv_fans_count)
    TextView mFansCountTv;

    @BindView(R.id.tv_keep_count)
    TextView mKeepCountTv;

    LoginDialog loginDialog;

    LoginDialog.LoginWayListener loginWayListener;

    private UMShareAPI mShareAPI = null;

    private ProgressDialog progressDialog = null;

    private UserInfoPresenterImp userInfoPresenterImp;

    private int loginType = 2; //1，qq，2微信

    private UserInfo userInfo;

    @Override
    protected View onCreateView() {
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_my, null);
        ButterKnife.bind(this, root);
        initViews();
        return root;
    }

    @OnClick(R.id.tv_setting)
    public void setting() {
        Intent intent = new Intent(getActivity(), SettingActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.iv_user_head)
    public void click() {
        if (userInfo == null && loginDialog != null && !loginDialog.isShowing()) {
            loginDialog.show();
        } else {
            Intent intent = new Intent(getActivity(), UserInfoActivity.class);
            startActivity(intent);
        }
    }

    @OnClick(R.id.layout_my_note)
    public void myNote() {
        Intent intent = new Intent(getActivity(), MyNoteActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.layout_my_message)
    public void myMessage() {
        Intent intent = new Intent(getActivity(), MyMessageActivity.class);
        startActivity(intent);
    }

    public void initViews() {
        FrameLayout.LayoutParams searchParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(36));
        searchParams.setMargins(0, BarUtils.getStatusBarHeight(), 0, 0);
        mMyInfoTopLayout.setLayoutParams(searchParams);

        FrameLayout.LayoutParams userInfoParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        userInfoParams.setMargins(0, BarUtils.getStatusBarHeight() + SizeUtils.dp2px(8), 0, 0);
        mUserInfoLayout.setLayoutParams(userInfoParams);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("正在登录");

        initDialog();
        mShareAPI = UMShareAPI.get(getActivity());
        userInfoPresenterImp = new UserInfoPresenterImp(this, getActivity());

    }

    @Override
    public void onResume() {
        super.onResume();

        if (!StringUtils.isEmpty(SPUtils.getInstance().getString(Constants.USER_INFO))) {
            Logger.i(SPUtils.getInstance().getString(Constants.USER_INFO));
            userInfo = JSON.parseObject(SPUtils.getInstance().getString(Constants.USER_INFO), new TypeReference<UserInfo>() {
            });
        }

        if (userInfo != null) {
            RequestOptions options = new RequestOptions();
            options.transform(new GlideRoundTransform(getActivity(), 30));
            Glide.with(getActivity()).load(userInfo.getUserimg()).apply(options).into(mUserHeadImageView);
            mUserNickNameTv.setText(userInfo.getNickname());
            mUserIdTv.setText("ID：" + userInfo.getId());

            mFollowTv.setText(userInfo.getGuanNum() + "");
            mFansCountTv.setText(userInfo.getFenNum() + "");
            mKeepCountTv.setText(userInfo.getCollectNum() + "");
        }
    }

    public void initDialog() {
        loginWayListener = new LoginDialog.LoginWayListener() {
            @Override
            public void loginWay(Dialog dialog, int type) {
                ToastUtils.showLong("type--->" + type);
                if (type == 1) {
                    loginType = 2;
                    wxLogin();
                } else {
                    loginType = 1;
                    qqLogin();
                }
            }
        };
        loginDialog = new LoginDialog(getActivity(), R.style.login_dialog, loginWayListener);
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
                loginRequest.setOpenid(data.get("uid"));
                loginRequest.setType(loginType + "");
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
            //Toast.makeText(mContext, "授权失败：" + t.getMessage(), Toast.LENGTH_LONG).show();
        }

        /**
         * @desc 授权取消的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         */
        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            //Toast.makeText(mContext, "授权取消了", Toast.LENGTH_LONG).show();
        }
    };

    public void wxLogin() {
        mShareAPI.getPlatformInfo(getActivity(), SHARE_MEDIA.WEIXIN, authListener);
    }

    public void qqLogin() {
        mShareAPI.getPlatformInfo(getActivity(), SHARE_MEDIA.QQ, authListener);
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void dismissProgress() {

    }

    @Override
    public void loadDataSuccess(UserInfoRet tData) {
        Logger.i(JSONObject.toJSONString(tData));
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

        if (tData != null) {
            if (tData.getCode() == Constants.SUCCESS && tData.getData() != null) {
                userInfo = tData.getData();

                ToastUtils.showLong("登录成功");
                RequestOptions options = new RequestOptions();
                options.transform(new GlideRoundTransform(getActivity(), 30));
                Glide.with(getActivity()).load(userInfo.getUserimg()).apply(options).into(mUserHeadImageView);

                mUserNickNameTv.setText(userInfo.getNickname());
                mUserIdTv.setText("ID：" + userInfo.getId());
                mFollowTv.setText(userInfo.getGuanNum() + "");
                mFansCountTv.setText(userInfo.getFenNum() + "");
                mKeepCountTv.setText(userInfo.getCollectNum() + "");

                SPUtils.getInstance().put(Constants.USER_INFO, JSONObject.toJSONString(tData.getData()));
            }

            if (tData.getCode() == Constants.FAIL) {
                ToastUtils.showLong(StringUtils.isEmpty(tData.getMsg()) ? "登录失败" : tData.getMsg());
            }
        }

    }

    @Override
    public void loadDataError(Throwable throwable) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    public void setUserInfoToIM() {

        //初始化参数，修改昵称为"cat"
//        TIMFriendshipManager.ModifyUserProfileParam param = new TIMFriendshipManager.ModifyUserProfileParam();
//        param.setNickname("cat");
//
//        TIMFriendshipManager.getInstance().modifyProfile(param, new TIMCallBack() {
//            @Override
//            public void onError(int code, String desc) {
//                //错误码 code 和错误描述 desc，可用于定位请求失败原因
//                //错误码 code 列表请参见错误码表
//                Log.e(tag, "modifyProfile failed: " + code + " desc" + desc);
//            }
//
//            @Override
//            public void onSuccess() {
//                Log.e(tag, "modifyProfile succ");
//            }
//        });
    }

}
