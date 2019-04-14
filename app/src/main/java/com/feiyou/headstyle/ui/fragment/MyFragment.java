package com.feiyou.headstyle.ui.fragment;

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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
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
import com.feiyou.headstyle.ui.activity.AboutActivity;
import com.feiyou.headstyle.ui.activity.FeedBackActivity;
import com.feiyou.headstyle.ui.activity.MyCollectionActivity;
import com.feiyou.headstyle.ui.activity.MyFollowActivity;
import com.feiyou.headstyle.ui.activity.MyMessageActivity;
import com.feiyou.headstyle.ui.activity.MyNoteActivity;
import com.feiyou.headstyle.ui.activity.SettingActivity;
import com.feiyou.headstyle.ui.activity.UserInfoActivity;
import com.feiyou.headstyle.ui.base.BaseFragment;
import com.feiyou.headstyle.ui.custom.GlideRoundTransform;
import com.feiyou.headstyle.ui.custom.LoginDialog;
import com.feiyou.headstyle.ui.custom.PraiseDialog;
import com.feiyou.headstyle.ui.custom.WeiXinFollowDialog;
import com.feiyou.headstyle.utils.AppUtils;
import com.feiyou.headstyle.utils.GoToScoreUtils;
import com.feiyou.headstyle.view.UserInfoView;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

/**
 * Created by myflying on 2019/1/24.
 */
public class MyFragment extends BaseFragment implements UserInfoView, PraiseDialog.PraiseListener {

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

    @BindView(R.id.iv_guan_remind)
    ImageView mGuanRemindIv;

    @BindView(R.id.iv_fen_remind)
    ImageView mFenRemindIv;

    @BindView(R.id.iv_my_remind)
    ImageView mMyRemindIv;

    LoginDialog loginDialog;

    private UserInfo userInfo;

    private UserInfoPresenterImp userInfoPresenterImp;

    PraiseDialog praiseDialog;

    private boolean isRunResume;

    @Override
    protected View onCreateView() {
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_my, null);
        ButterKnife.bind(this, root);
        initViews();
        return root;
    }

    public void initViews() {
        praiseDialog = new PraiseDialog(getActivity(), R.style.login_dialog);
        praiseDialog.setPraiseListener(this);

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
        isRunResume = true;
        Logger.i("myfragment info onResume --->");
        loadMyInfo();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (getContext() != null && isVisibleToUser) {
            Logger.i("myfragment info isVisible --->");
            loadMyInfo();
        }
    }

    //每次进入页面时加载数据，主要是请求是否有新的消息
    public void loadMyInfo() {
        isRunResume = false;

        if (App.isShowTotalCount) {
            mMyRemindIv.setVisibility(View.VISIBLE);
        }

        if (App.isShowGuan) {
            //mGuanRemindIv.setVisibility(View.VISIBLE);
        }

        if (App.isShowFen) {
            mFenRemindIv.setVisibility(View.VISIBLE);
        }

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
            mUserIdTv.setText("头像号：" + userInfo.getId());

            mFollowTv.setText(userInfo.getGuanNum() > 10000 ? userInfo.getGuanNum() / 10000 + "万" : userInfo.getGuanNum() + "");
            mFansCountTv.setText(userInfo.getFenNum() > 10000 ? userInfo.getFenNum() / 10000 + "万" : userInfo.getFenNum() + "");
            mKeepCountTv.setText(userInfo.getCollectNum() > 10000 ? userInfo.getCollectNum() / 10000 + "万" : userInfo.getCollectNum() + "");

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
        Logger.i("MyFragment messageEvent--->" + messageEvent.getMessage());

        if (messageEvent.getMessage().equals("login_success")) {
            if (!StringUtils.isEmpty(SPUtils.getInstance().getString(Constants.USER_INFO))) {
                Logger.i(SPUtils.getInstance().getString(Constants.USER_INFO));
                userInfo = JSON.parseObject(SPUtils.getInstance().getString(Constants.USER_INFO), new TypeReference<UserInfo>() {
                });
            }
            if (userInfo != null) {

                RequestOptions options = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL);
                options.placeholder(R.mipmap.head_def);
                options.transform(new GlideRoundTransform(getActivity(), 30));
                Glide.with(getActivity()).load(userInfo.getUserimg()).apply(options).into(mUserHeadImageView);
                mUserNickNameTv.setText(userInfo.getNickname());
                mUserIdTv.setText("头像号：" + userInfo.getId());

                mFollowTv.setText(userInfo.getGuanNum() > 10000 ? userInfo.getGuanNum() / 10000 + "万" : userInfo.getGuanNum() + "");
                mFansCountTv.setText(userInfo.getFenNum() > 10000 ? userInfo.getFenNum() / 10000 + "万" : userInfo.getFenNum() + "");
                mKeepCountTv.setText(userInfo.getCollectNum() > 10000 ? userInfo.getCollectNum() / 10000 + "万" : userInfo.getCollectNum() + "");

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

    @OnClick(R.id.layout_about)
    public void about() {
        startActivity(6);
    }

    @OnClick(R.id.layout_feed_back)
    public void feedBack() {
        startActivity(7);
    }

    @OnClick(R.id.layout_weixin_code)
    public void followWeiXin() {
        if (!AppUtils.appInstalled(getActivity(), "com.tencent.mm")) {
            Toasty.normal(getActivity(), "你还未安装微信").show();
            return;
        }

        WeiXinFollowDialog weiXinFollowDialog = new WeiXinFollowDialog(getActivity());
        weiXinFollowDialog.showChargeDialog(weiXinFollowDialog);
    }

    @OnClick(R.id.layout_add_score)
    public void addScore() {
        if (praiseDialog != null && !praiseDialog.isShowing()) {
            praiseDialog.show();
        }
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

                    if (mGuanRemindIv.getVisibility() == View.VISIBLE) {
                        mGuanRemindIv.setVisibility(View.GONE);
                        App.isShowGuan = false;
                    }
                    break;
                case 2:
                    intent = new Intent(getActivity(), MyFollowActivity.class);
                    intent.putExtra("type", 1);
                    intent.putExtra("is_my_info", true);
                    if (mFenRemindIv.getVisibility() == View.VISIBLE) {
                        mFenRemindIv.setVisibility(View.GONE);
                        App.isShowFen = false;
                    }
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
                case 6:
                    intent = new Intent(getActivity(), AboutActivity.class);
                    break;
                case 7:
                    intent = new Intent(getActivity(), FeedBackActivity.class);
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

        if (mMyRemindIv.getVisibility() == View.VISIBLE) {
            mMyRemindIv.setVisibility(View.GONE);
            App.isShowTotalCount = false;
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

                mFollowTv.setText(userInfo.getGuanNum() > 10000 ? userInfo.getGuanNum() / 10000 + "万" : userInfo.getGuanNum() + "");
                mFansCountTv.setText(userInfo.getFenNum() > 10000 ? userInfo.getFenNum() / 10000 + "万" : userInfo.getFenNum() + "");
                mKeepCountTv.setText(userInfo.getCollectNum() > 10000 ? userInfo.getCollectNum() / 10000 + "万" : userInfo.getCollectNum() + "");

                //关注
                if (SPUtils.getInstance().getInt(Constants.GUAN_NUM, 0) >= 0) {
                    if (userInfo.getGuanNum() > SPUtils.getInstance().getInt(Constants.GUAN_NUM, 0)) {
                        App.isShowGuan = true;
                        //mGuanRemindIv.setVisibility(View.VISIBLE);
                    }
                }

                //粉丝
                if (SPUtils.getInstance().getInt(Constants.FEN_NUM, 0) >= 0) {
                    if (userInfo.getFenNum() > SPUtils.getInstance().getInt(Constants.FEN_NUM, 0)) {
                        App.isShowFen = true;
                        mFenRemindIv.setVisibility(View.VISIBLE);
                    }
                }

                //通知消息
                if (SPUtils.getInstance().getInt(Constants.COMMENT_COUNT, 0) > 0) {
                    if (userInfo.getCommentNum() > SPUtils.getInstance().getInt(Constants.COMMENT_COUNT, 0)) {
                        App.isRemindComment = true;
                    }
                }

                if (SPUtils.getInstance().getInt(Constants.AT_COUNT, 0) > 0) {
                    if (userInfo.getAiteNum() > SPUtils.getInstance().getInt(Constants.AT_COUNT, 0)) {
                        App.isRemindAt = true;
                    }
                }

                if (SPUtils.getInstance().getInt(Constants.NOTICE_COUNT, 0) > 0) {
                    if (userInfo.getNoticeNum() > SPUtils.getInstance().getInt(Constants.NOTICE_COUNT, 0)) {
                        App.isRemindNotice = true;
                    }
                }
                if (SPUtils.getInstance().getInt(Constants.TOTAL_COUNT, 0) > 0) {
                    if (userInfo.getMyTotalNum() > SPUtils.getInstance().getInt(Constants.TOTAL_COUNT, 0)) {
                        //EventBus.getDefault().post(new MessageEvent("home_message_remind"));
                        mMyRemindIv.setVisibility(View.VISIBLE);
                    }
                }
                SPUtils.getInstance().put(Constants.GUAN_NUM, userInfo.getGuanNum());
                SPUtils.getInstance().put(Constants.FEN_NUM, userInfo.getFenNum());
                SPUtils.getInstance().put(Constants.COMMENT_COUNT, userInfo.getCommentNum());
                SPUtils.getInstance().put(Constants.COMMENT_COUNT, userInfo.getCommentNum());
                SPUtils.getInstance().put(Constants.AT_COUNT, userInfo.getAiteNum());
                SPUtils.getInstance().put(Constants.NOTICE_COUNT, userInfo.getNoticeNum());
                SPUtils.getInstance().put(Constants.TOTAL_COUNT, userInfo.getMyTotalNum());
            }
        } else {
            Logger.i(StringUtils.isEmpty(tData.getMsg()) ? "登录失败" : tData.getMsg());
        }
    }

    @Override
    public void loadDataError(Throwable throwable) {

    }

    @Override
    public void config() {
        if (praiseDialog != null && praiseDialog.isShowing()) {
            praiseDialog.dismiss();
        }
        GoToScoreUtils.goToMarket(getActivity(), Constants.APP_PACKAGE_NAME);
    }

    @Override
    public void cancel() {
        if (praiseDialog != null && praiseDialog.isShowing()) {
            praiseDialog.dismiss();
        }
    }
}
