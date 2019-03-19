package com.feiyou.headstyle.ui.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.feiyou.headstyle.App;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.LoginRequest;
import com.feiyou.headstyle.bean.MessageEvent;
import com.feiyou.headstyle.bean.ResultInfo;
import com.feiyou.headstyle.bean.UserInfo;
import com.feiyou.headstyle.bean.UserInfoRet;
import com.feiyou.headstyle.common.Constants;
import com.feiyou.headstyle.presenter.UserInfoPresenterImp;
import com.feiyou.headstyle.ui.activity.MainActivity;
import com.feiyou.headstyle.ui.activity.MyCollectionActivity;
import com.feiyou.headstyle.ui.activity.MyFollowActivity;
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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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

    private UserInfo userInfo;

    private UserInfoPresenterImp userInfoPresenterImp;

    @Override
    protected View onCreateView() {
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_my, null);
        ButterKnife.bind(this, root);
        initViews();
        return root;
    }

    public void initViews() {

        mUserIdTv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (!App.getApp().isLogin) {
                    if (loginDialog != null && !loginDialog.isShowing()) {
                        loginDialog.show();
                    }
                } else {
                    ClipboardManager cmb = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                    cmb.setText(userInfo.getId()); //将内容放入粘贴管理器,在别的地方长按选择"粘贴"即可
                    ToastUtils.showLong("已复制");
                }
                return false;
            }
        });

        FrameLayout.LayoutParams searchParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(36));
        searchParams.setMargins(0, BarUtils.getStatusBarHeight(), 0, 0);
        mMyInfoTopLayout.setLayoutParams(searchParams);

        FrameLayout.LayoutParams userInfoParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        userInfoParams.setMargins(0, BarUtils.getStatusBarHeight() + SizeUtils.dp2px(8), 0, 0);
        mUserInfoLayout.setLayoutParams(userInfoParams);

        loginDialog = new LoginDialog(getActivity(), R.style.login_dialog);
        userInfoPresenterImp = new UserInfoPresenterImp(this, getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!StringUtils.isEmpty(SPUtils.getInstance().getString(Constants.USER_INFO))) {
            Logger.i(SPUtils.getInstance().getString(Constants.USER_INFO));
            userInfo = JSON.parseObject(SPUtils.getInstance().getString(Constants.USER_INFO), new TypeReference<UserInfo>() {
            });
        } else {
            userInfo = null;
        }

        if (userInfo != null) {
            RequestOptions options = new RequestOptions().skipMemoryCache(true);
            options.placeholder(R.mipmap.head_def);
            options.transform(new GlideRoundTransform(getActivity(), 30));
            Glide.with(getActivity()).load(userInfo.getUserimg()).apply(options).into(mUserHeadImageView);
            mUserNickNameTv.setText(userInfo.getNickname());
            mUserIdTv.setText("ID：" + userInfo.getId());

            mFollowTv.setText(userInfo.getGuanNum() + "");
            mFansCountTv.setText(userInfo.getFenNum() + "");
            mKeepCountTv.setText(userInfo.getCollectNum() + "");

            //如果用户已经登录，重新获取最新的用户信息
            LoginRequest loginRequest = new LoginRequest();
            loginRequest.setOpenid(userInfo.getOpenid());//openid全部大写
            loginRequest.setType(userInfo.getLoginType());
            loginRequest.setImeil(DeviceUtils.getAndroidID());
            loginRequest.setUserimg(userInfo.getUserimg());
            loginRequest.setSex(userInfo.getSex() + "");
            loginRequest.setNickname(userInfo.getNickname());
            userInfoPresenterImp.login(loginRequest);

        } else {
            mUserHeadImageView.setImageResource(R.mipmap.head_def);

            mUserNickNameTv.setText("未登录");
            mUserIdTv.setText("登录可以换头像哦");

            mFollowTv.setText("0");
            mFansCountTv.setText("0");
            mKeepCountTv.setText("0");
        }

        if (loginDialog != null && loginDialog.isShowing()) {
            loginDialog.dismiss();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(MessageEvent messageEvent) {
        if (messageEvent.getMessage().equals("login_success")) {
            if (!StringUtils.isEmpty(SPUtils.getInstance().getString(Constants.USER_INFO))) {
                Logger.i(SPUtils.getInstance().getString(Constants.USER_INFO));
                userInfo = JSON.parseObject(SPUtils.getInstance().getString(Constants.USER_INFO), new TypeReference<UserInfo>() {
                });
            }
            if (userInfo != null) {

                RequestOptions options = new RequestOptions().skipMemoryCache(true);
                options.placeholder(R.mipmap.head_def);
                options.transform(new GlideRoundTransform(getActivity(), 30));
                Glide.with(getActivity()).load(userInfo.getUserimg()).apply(options).into(mUserHeadImageView);
                mUserNickNameTv.setText(userInfo.getNickname());
                mUserIdTv.setText("ID：" + userInfo.getId());

                mFollowTv.setText(userInfo.getGuanNum() + "");
                mFansCountTv.setText(userInfo.getFenNum() + "");
                mKeepCountTv.setText(userInfo.getCollectNum() + "");

                LoginRequest loginRequest = new LoginRequest();
                loginRequest.setOpenid(userInfo.getOpenid());//openid全部大写
                loginRequest.setType(userInfo.getLoginType());
                loginRequest.setImeil(DeviceUtils.getAndroidID());
                loginRequest.setUserimg(userInfo.getUserimg());
                loginRequest.setSex(userInfo.getSex() + "");
                loginRequest.setNickname(userInfo.getNickname());
                userInfoPresenterImp.login(loginRequest);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @OnClick(R.id.layout_my_guan)
    public void guanzhu() {
        startActivity(1);
    }

    @OnClick(R.id.layout_my_fen)
    public void fensi() {
        startActivity(2);
    }

    @OnClick(R.id.layout_collection)
    void collection() {
        startActivity(5);
    }

    @OnClick(R.id.tv_setting)
    public void setting() {
        startActivity(3);
    }

    @OnClick(R.id.iv_user_head)
    public void click() {
        startActivity(4);
    }

    void startActivity(int type) {
        if (userInfo == null && loginDialog != null && !loginDialog.isShowing()) {
            loginDialog.show();
        } else {
            Intent intent = null;
            switch (type) {
                case 1:
                    intent = new Intent(getActivity(), MyFollowActivity.class);
                    intent.putExtra("type", 0);
                    intent.putExtra("is_my_info", true);
                    break;
                case 2:
                    intent = new Intent(getActivity(), MyFollowActivity.class);
                    intent.putExtra("type", 1);
                    intent.putExtra("is_my_info", true);
                    break;
                case 3:
                    intent = new Intent(getActivity(), SettingActivity.class);
                    break;
                case 4:
                    intent = new Intent(getActivity(), UserInfoActivity.class);
                    intent.putExtra("is_my_info", true);
                    break;
                case 5:
                    intent = new Intent(getActivity(), MyCollectionActivity.class);
                    break;
            }
            startActivity(intent);
        }
    }

    @OnClick(R.id.layout_my_note)
    public void myNote() {
        if (!App.getApp().isLogin) {
            if (loginDialog != null && !loginDialog.isShowing()) {
                loginDialog.show();
            }
            return;
        }
        Intent intent = new Intent(getActivity(), MyNoteActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.layout_my_message)
    public void myMessage() {
        if (!App.getApp().isLogin) {
            if (loginDialog != null && !loginDialog.isShowing()) {
                loginDialog.show();
            }
            return;
        }
        Intent intent = new Intent(getActivity(), MyMessageActivity.class);
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
        Logger.i("my fragment user info --->" + JSONObject.toJSONString(tData));

        if (tData != null && tData.getCode() == Constants.SUCCESS) {
            if (tData instanceof UserInfoRet) {
                userInfo = ((UserInfoRet) tData).getData();

                App.getApp().setmUserInfo(userInfo);
                App.getApp().setLogin(true);
                SPUtils.getInstance().put(Constants.USER_INFO, JSONObject.toJSONString(((UserInfoRet) tData).getData()));

                mUserNickNameTv.setText(userInfo.getNickname());
                mUserIdTv.setText("头像号：" + userInfo.getId());

                mFollowTv.setText(userInfo.getGuanNum() + "");
                mFansCountTv.setText(userInfo.getFenNum() + "");
                mKeepCountTv.setText(userInfo.getCollectNum() + "");
            }
        } else {
            Logger.i(StringUtils.isEmpty(tData.getMsg()) ? "登录失败" : tData.getMsg());
        }
    }

    @Override
    public void loadDataError(Throwable throwable) {

    }
}
