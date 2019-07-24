package com.feiyou.headstyle.ui.fragment;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.PathUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdManager;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTRewardVideoAd;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feiyou.headstyle.App;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.base.IBaseView;
import com.feiyou.headstyle.bean.MessageEvent;
import com.feiyou.headstyle.bean.SignDoneInfoRet;
import com.feiyou.headstyle.bean.TaskInfo;
import com.feiyou.headstyle.bean.TaskRecordInfoRet;
import com.feiyou.headstyle.bean.UserInfo;
import com.feiyou.headstyle.bean.WelfareInfo;
import com.feiyou.headstyle.bean.WelfareInfoRet;
import com.feiyou.headstyle.common.Constants;
import com.feiyou.headstyle.presenter.SignDoneInfoPresenterImp;
import com.feiyou.headstyle.presenter.TaskRecordInfoPresenterImp;
import com.feiyou.headstyle.presenter.WelfareInfoPresenterImp;
import com.feiyou.headstyle.ui.activity.AboutGoldActivity;
import com.feiyou.headstyle.ui.activity.AdActivity;
import com.feiyou.headstyle.ui.activity.CashActivity;
import com.feiyou.headstyle.ui.activity.CommunityArticleActivity;
import com.feiyou.headstyle.ui.activity.GoldDetailActivity;
import com.feiyou.headstyle.ui.activity.GoldMailActivity;
import com.feiyou.headstyle.ui.activity.GoldTaskActivity;
import com.feiyou.headstyle.ui.activity.GoodDetailActivity;
import com.feiyou.headstyle.ui.activity.Main1Activity;
import com.feiyou.headstyle.ui.activity.PushNoteActivity;
import com.feiyou.headstyle.ui.activity.TestDetailActivity;
import com.feiyou.headstyle.ui.activity.TestImageDetailActivity;
import com.feiyou.headstyle.ui.adapter.GoodsListAdapter;
import com.feiyou.headstyle.ui.adapter.SignInListAdapter;
import com.feiyou.headstyle.ui.adapter.TaskListAdapter;
import com.feiyou.headstyle.ui.base.BaseFragment;
import com.feiyou.headstyle.ui.custom.DownFileDialog;
import com.feiyou.headstyle.ui.custom.GlideRoundTransform;
import com.feiyou.headstyle.ui.custom.LoginDialog;
import com.feiyou.headstyle.ui.custom.NormalDecoration;
import com.feiyou.headstyle.ui.custom.ReceiveHongBaoDialog;
import com.feiyou.headstyle.ui.custom.SeeVideoDialog;
import com.feiyou.headstyle.ui.custom.SignSuccessDialog;
import com.feiyou.headstyle.ui.custom.TurnProfitDialog;
import com.feiyou.headstyle.ui.custom.WeiXinTaskDialog;
import com.feiyou.headstyle.utils.GoToScoreUtils;
import com.feiyou.headstyle.utils.MyTimeUtil;
import com.feiyou.headstyle.utils.RandomUtils;
import com.feiyou.headstyle.utils.TTAdManagerHolder;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.orhanobut.logger.Logger;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.sunfusheng.marqueeview.MarqueeView;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

/**
 * Created by myflying on 2019/3/12.
 */
public class Create1Fragment extends BaseFragment implements View.OnClickListener, SignSuccessDialog.SignSuccessListener, IBaseView, ReceiveHongBaoDialog.OpenHongBaoListener, SeeVideoDialog.GetMoneyListener, SwipeRefreshLayout.OnRefreshListener, TurnProfitDialog.TurnListener, WeiXinTaskDialog.OpenWeixinListener, DownFileDialog.DownListener {

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mRefreshLayout;

    @BindView(R.id.topbar)
    QMUITopBar mTopBar;

    @BindView(R.id.goods_list_view)
    RecyclerView mGoodListView;

    RecyclerView mSingInListView;

    RecyclerView mTaskListView;

    ImageView mSignInIv;

    GoodsListAdapter goodsListAdapter;

    SignInListAdapter signInListAdapter;

    TaskListAdapter taskListAdapter;

    View topView;

    SignSuccessDialog signSuccessDialog;

    ReceiveHongBaoDialog receiveHongBaoDialog;

    TurnProfitDialog turnProfitDialog;

    Button mTurnProfitBtn;

    Button mCashBtn;

    LinearLayout mGoldDetailLayout;

    LinearLayout mGoldTaskLayout;

    LinearLayout mGoldMailLayout;

    FrameLayout mGetMoneyLayout;

    RelativeLayout mAboutGoldLayout;

    LoginDialog loginDialog;

    ImageView mUserHeadIv;

    TextView mUserNameTv;

    TextView mGoldBalanceTv;

    TextView mMyProfitTv;

    TextView mScaleRemarkTv;

    TextView mSignDaysTv;

    TextView mMoreTaskTv;

    TextView mGoodMoreTv;

    ImageView mGetMoneyBgIv;

    FrameLayout mCountDownLayout;

    ImageView mMaskBgIv;

    TextView mCountDownTv;

    TextView mTurnTableTv;

    MarqueeView marqueeView;

    private UserInfo userInfo;

    private WelfareInfoPresenterImp welfareInfoPresenterImp;

    private SignDoneInfoPresenterImp signDoneInfoPresenterImp;

    private int signDays;//连续签到的天数

    private String[] hongbaoMoney;

    private double randomHongbao;

    private TTAdNative mTTAdNative;

    private TTRewardVideoAd mttRewardVideoAd;

    private SeeVideoDialog seeVideoDialog;

    private List<TaskInfo> allTaskInfoList;

    private int isFromTaskSign;

    private double myProfitMoney;

    TaskRecordInfoPresenterImp taskRecordInfoPresenterImp;

    private String taskId = "";

    private String recordId = "";

    private String[] seeVideoMoneys;

    private double seeVideoMoney;//看视频可得到的收益

    private double scaleGoldNum;//兑换比列中，金币的数量

    private double scaleCashNum;//兑换比列中，现金的数量

    private int leastGoldNum;//最低的转换收益的金币

    private int getGoldNum;//任务完成获得的金币数

    private int userGoldNum;//用户的金币数

    private String followPublicUrl;

    private String followPublicName;

    private WeiXinTaskDialog weiXinTaskDialog;

    CountDownTimer followCountDownTimer;

    CountDownTimer marketTimer;

    CountDownTimer seeVideoTimer;

    DownFileDialog downApkDialog;

    BaseDownloadTask task;

    private String downFilePath;

    private String downFilePageName;

    private boolean isAccord;

    private int goldNum = 0;

    private int miniGoldNum;

    private int adCountDown = 0;

    private String aboutGoldTxt;

    private boolean seeVideoIsFinish;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    int progress = (Integer) msg.obj;
                    downApkDialog.setProgress(progress);
                    break;
                case 1:
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected View onCreateView() {
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_create1, null);
        ButterKnife.bind(this, root);
        EventBus.getDefault().register(this);
        initView();
        initData();
        return root;
    }

    public void initView() {
        QMUIStatusBarHelper.setStatusBarLightMode(getActivity());
        View barView = new View(getActivity());
        barView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, BarUtils.getStatusBarHeight() + SizeUtils.dp2px(6)));
        mTopBar.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));
        mTopBar.setCenterView(barView);

        topView = LayoutInflater.from(getActivity()).inflate(R.layout.welfare_top_view, null);
        mSingInListView = topView.findViewById(R.id.sign_in_list_view);
        mTaskListView = topView.findViewById(R.id.task_list_view);
        mSignInIv = topView.findViewById(R.id.iv_sign_in);
        mTurnProfitBtn = topView.findViewById(R.id.btn_turn_profit);
        mCashBtn = topView.findViewById(R.id.btn_cash);
        mGoldDetailLayout = topView.findViewById(R.id.layout_gold_detail);
        mGoldTaskLayout = topView.findViewById(R.id.layout_gold_task);
        mGoldMailLayout = topView.findViewById(R.id.layout_gold_mail);
        mAboutGoldLayout = topView.findViewById(R.id.layout_about_gold);
        mGetMoneyLayout = topView.findViewById(R.id.layout_get_money);

        marqueeView = topView.findViewById(R.id.marqueeView);

        mCountDownLayout = topView.findViewById(R.id.layout_count_down);
        mMaskBgIv = topView.findViewById(R.id.iv_mask_bg);
        mCountDownTv = topView.findViewById(R.id.tv_count_down_number);
        mGetMoneyBgIv = topView.findViewById(R.id.iv_get_money_bg);
        //用户收益信息
        mUserHeadIv = topView.findViewById(R.id.iv_user_head);
        mUserNameTv = topView.findViewById(R.id.tv_user_name);
        mGoldBalanceTv = topView.findViewById(R.id.tv_gold_balance);
        mMyProfitTv = topView.findViewById(R.id.tv_my_profit);
        mScaleRemarkTv = topView.findViewById(R.id.tv_scale_remark);
        mSignDaysTv = topView.findViewById(R.id.tv_sign_days);
        mMoreTaskTv = topView.findViewById(R.id.tv_more_task);
        mGoodMoreTv = topView.findViewById(R.id.tv_good_more);
        mTurnTableTv = topView.findViewById(R.id.tv_turntable);

        mUserHeadIv.setOnClickListener(this);
        mUserNameTv.setOnClickListener(this);
        mSignInIv.setOnClickListener(this);
        mTurnProfitBtn.setOnClickListener(this);
        mCashBtn.setOnClickListener(this);
        mGoldDetailLayout.setOnClickListener(this);
        mGoldTaskLayout.setOnClickListener(this);
        mGoldMailLayout.setOnClickListener(this);
        mAboutGoldLayout.setOnClickListener(this);
        mGetMoneyLayout.setOnClickListener(this);
        mMoreTaskTv.setOnClickListener(this);
        mGoodMoreTv.setOnClickListener(this);
        mTurnTableTv.setOnClickListener(this);
        mGetMoneyBgIv.setOnClickListener(this);

        signSuccessDialog = new SignSuccessDialog(getActivity(), R.style.login_dialog);
        signSuccessDialog.setSignSuccessListener(this);
        receiveHongBaoDialog = new ReceiveHongBaoDialog(getActivity(), R.style.login_dialog);
        receiveHongBaoDialog.setOpenHongBaoListener(this);

        turnProfitDialog = new TurnProfitDialog(getActivity(), R.style.login_dialog);
        turnProfitDialog.setTurnListener(this);
        loginDialog = new LoginDialog(getActivity(), R.style.login_dialog);
        seeVideoDialog = new SeeVideoDialog(getActivity(), R.style.login_dialog);
        seeVideoDialog.setGetMoneyListener(this);

        downApkDialog = new DownFileDialog(getActivity(), R.style.login_dialog);
        downApkDialog.setDownListener(this);
    }

    public void initData() {

        Logger.i("create init data--->");
        Bundle bundle = getActivity().getIntent().getExtras();
        if (bundle != null) {
            isFromTaskSign = bundle.getInt("is_from_task_sign", 0);
        }

        Logger.i("isFromTaskSign--->" + isFromTaskSign);

        mRefreshLayout.setOnRefreshListener(this);
        //设置进度View样式的大小，只有两个值DEFAULT和LARGE
        //设置进度View下拉的起始点和结束点，scale 是指设置是否需要放大或者缩小动画
        mRefreshLayout.setProgressViewOffset(true, -0, 200);
        //设置进度View下拉的结束点，scale 是指设置是否需要放大或者缩小动画
        mRefreshLayout.setProgressViewEndTarget(true, 180);
        //设置进度View的组合颜色，在手指上下滑时使用第一个颜色，在刷新中，会一个个颜色进行切换
        mRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getActivity(), R.color.colorPrimary), Color.RED, Color.YELLOW, Color.BLUE);

        //设置触发刷新的距离
        mRefreshLayout.setDistanceToTriggerSync(200);
        //如果child是自己自定义的view，可以通过这个回调，告诉mSwipeRefreshLayoutchild是否可以滑动
        mRefreshLayout.setOnChildScrollUpCallback(null);

        TTAdManager ttAdManager = TTAdManagerHolder.get();
        //step2:(可选，强烈建议在合适的时机调用):申请部分权限，如read_phone_state,防止获取不了imei时候，下载类广告没有填充的问题。
        TTAdManagerHolder.get().requestPermissionIfNecessary(getActivity());
        //step3:创建TTAdNative对象,用于调用广告请求接口
        mTTAdNative = ttAdManager.createAdNative(getActivity());

        goodsListAdapter = new GoodsListAdapter(getActivity(), null);
        mGoodListView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mGoodListView.setAdapter(goodsListAdapter);
        goodsListAdapter.addHeaderView(topView);

        goodsListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                if (!App.getApp().isLogin) {
                    if (loginDialog != null && !loginDialog.isShowing()) {
                        loginDialog.show();
                    }
                    return;
                }

                Intent intent = new Intent(getActivity(), GoodDetailActivity.class);
                intent.putExtra("gid", goodsListAdapter.getData().get(position).getId() + "");
                startActivity(intent);
            }
        });

        signInListAdapter = new SignInListAdapter(getActivity(), null);
        mSingInListView.setLayoutManager(new GridLayoutManager(getActivity(), 7));
        mSingInListView.setAdapter(signInListAdapter);

        taskListAdapter = new TaskListAdapter(getActivity(), null);
        mTaskListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mTaskListView.addItemDecoration(new NormalDecoration(ContextCompat.getColor(getActivity(), R.color.gray_aaa), 1));
        mTaskListView.setAdapter(taskListAdapter);

        welfareInfoPresenterImp = new WelfareInfoPresenterImp(this, getActivity());
        signDoneInfoPresenterImp = new SignDoneInfoPresenterImp(this, getActivity());
        taskRecordInfoPresenterImp = new TaskRecordInfoPresenterImp(this, getActivity());

        weiXinTaskDialog = new WeiXinTaskDialog(getActivity());
        weiXinTaskDialog.setOpenWeixinListener(this);

        taskListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                if (!App.getApp().isLogin) {
                    if (loginDialog != null && !loginDialog.isShowing()) {
                        loginDialog.show();
                    }
                    return;
                }

                if (taskListAdapter.getData().get(position).getIsFinish() == 1) {
                    return;
                }
                int id = taskListAdapter.getData().get(position).getId();
                switch (id) {
                    case 1:
                        Intent intent = new Intent(getActivity(), PushNoteActivity.class);
                        intent.putExtra("is_from_task", 1);
                        startActivity(intent);
                        break;
                    case 2:
                        taskId = "2";
                        recordId = "";
                        followPublicUrl = taskListAdapter.getData().get(position).getPubulicimg();
                        followPublicName = taskListAdapter.getData().get(position).getPublicname();

                        weiXinTaskDialog.show();
                        weiXinTaskDialog.setPublicImage(followPublicUrl);

                        break;
                    case 3:
                        Intent intent1 = new Intent(getActivity(), CommunityArticleActivity.class);
                        intent1.putExtra("msg_id", App.getApp().getRandomNoteId());
                        intent1.putExtra("is_from_task", 1);
                        startActivity(intent1);
                        break;
                    case 4:
                        taskId = "4";
                        recordId = "";
                        taskRecordInfoPresenterImp.addTaskRecord(App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "", taskId, taskListAdapter.getData().get(position).getGoldnum(), 0, 0, "0");
                        if (mttRewardVideoAd != null) {
                            //step6:在获取到广告后展示
                            mttRewardVideoAd.showRewardVideoAd(getActivity());
                            mttRewardVideoAd = null;
                        }
                        break;
                    case 5:
                        if (App.testInfoList != null && App.testInfoList.size() > 0) {
                            if (App.testInfoList.get(0).getTestType() == 1) {
                                Intent intent2 = new Intent(getActivity(), TestDetailActivity.class);
                                intent2.putExtra("tid", App.testInfoList.get(0).getId());
                                startActivity(intent2);
                            } else {
                                Intent intent2 = new Intent(getActivity(), TestImageDetailActivity.class);
                                intent2.putExtra("tid", App.testInfoList.get(0).getId());
                                startActivity(intent2);
                            }
                        } else {
                            ToastUtils.showLong("数据异常，请稍后重试");
                            return;
                        }

                        break;
                    case 6:
                        taskId = "6";
                        recordId = "";
                        //打开应用市场
                        GoToScoreUtils.goToMarket(getActivity(), Constants.APP_PACKAGE_NAME);
                        break;
                    case 7:
                        Intent intent4 = new Intent(getActivity(), Main1Activity.class);
                        intent4.putExtra("home_index", 2);
                        intent4.putExtra("is_from_task_sign", 1);
                        startActivity(intent4);
                        break;
                    case 8:
                        taskId = "8";
                        recordId = "";
                        downFilePageName = taskListAdapter.getData().get(position).getWeburl();

                        if (downApkDialog != null && !downApkDialog.isShowing()) {
                            downApkDialog.show();
                        }

                        downAppFile(taskListAdapter.getData().get(position).getDownaddress());
                        break;
                    case 9:
                        Intent intent3 = new Intent(getActivity(), AdActivity.class);
                        intent3.putExtra("open_url", taskListAdapter.getData().get(position).getWeburl());
                        intent3.putExtra("is_from_task", 1);
                        startActivity(intent3);
                        break;
                    case 10:
                        taskId = "10";
                        recordId = "";
                        taskRecordInfoPresenterImp.addTaskRecord(App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "", taskId, goldNum, 0, 1, "0");
                        openMiniApp(taskListAdapter.getData().get(position).getOldid());
                        break;
                    default:
                        break;
                }
            }
        });

        //首页领钱
        followCountDownTimer = new CountDownTimer(30 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Logger.i("剩余时间--->" + millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                isAccord = true;
            }
        };


        marketTimer = new CountDownTimer(30 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Logger.i("剩余时间--->" + millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                isAccord = true;
            }
        };

        Glide.with(getActivity()).load(R.drawable.sign_in).into(mSignInIv);
        Glide.with(getActivity()).load(R.drawable.see_video_normal_gif).into(mGetMoneyBgIv);
    }

    @Override
    public void onResume() {
        super.onResume();
        Logger.i("create onResume--->" + this.getClass().hashCode());

        if (!StringUtils.isEmpty(SPUtils.getInstance().getString(Constants.USER_INFO))) {
            Logger.i(SPUtils.getInstance().getString(Constants.USER_INFO));
            userInfo = JSON.parseObject(SPUtils.getInstance().getString(Constants.USER_INFO), new TypeReference<UserInfo>() {
            });
        }
        if (userInfo == null) {
            userInfo = App.getApp().mUserInfo;
        }

        if (userInfo != null) {
            RequestOptions options = new RequestOptions().skipMemoryCache(true);
            options.placeholder(R.mipmap.head_def);
            options.transform(new GlideRoundTransform(getActivity(), 30));

            Glide.with(getActivity()).load(userInfo.getUserimg()).apply(options).into(mUserHeadIv);
            mUserNameTv.setText(userInfo.getNickname());
        } else {
            RequestOptions options = new RequestOptions().skipMemoryCache(true);
            options.placeholder(R.mipmap.head_def);
            options.transform(new GlideRoundTransform(getActivity(), 30));
            Glide.with(getActivity()).load(R.mipmap.head_def).apply(options).into(mUserHeadIv);
            mUserNameTv.setText("点击登录");
            mGoldBalanceTv.setText("0");
            mMyProfitTv.setText("0.0");
        }

        welfareInfoPresenterImp.getWelfareData(userInfo != null ? userInfo.getId() : "");

        if (taskId.equals("2")) {
            if (isAccord) {
                taskRecordInfoPresenterImp.addTaskRecord(App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "", taskId, goldNum, 0, 1, recordId);
            }
        }

        if (taskId.equals("8")) {
            if (AppUtils.isAppInstalled(downFilePageName)) {
                taskRecordInfoPresenterImp.addTaskRecord(App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "", taskId, goldNum, 0, 1, recordId);
            }
        }

        if (taskId.equals("10")) {
            ToastUtils.showLong("领取成功 +" + miniGoldNum + "金币");
        }
        if (taskId.equals("6")) {
            if (isAccord) {
                taskRecordInfoPresenterImp.addTaskRecord(App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "", taskId, goldNum, 0, 1, recordId);
            } else {
                ToastUtils.showLong("任务失败");
            }
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        if (taskId.equals("6")) {
            /** 倒计时30秒，一次1秒 */
            if (marketTimer != null) {
                marketTimer.start();
            }

            taskRecordInfoPresenterImp.addTaskRecord(App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "", taskId, goldNum, 0, 0, "0");
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(MessageEvent messageEvent) {
        if (messageEvent.getMessage().equals("login_success")) {
            onResume();
        }
        if (messageEvent.getMessage().equals("login_out")) {
            userInfo = null;
            App.getApp().setmUserInfo(null);
            adCountDown = 0;
            mCountDownLayout.setVisibility(View.GONE);
            if (followCountDownTimer != null) {
                followCountDownTimer.cancel();
            }
            if (marketTimer != null) {
                marketTimer.cancel();
            }
            if (seeVideoTimer != null) {
                seeVideoTimer.cancel();
            }
        }
    }

    @Override
    public void onClick(View v) {

        if (!App.getApp().isLogin) {
            if (loginDialog != null && !loginDialog.isShowing()) {
                loginDialog.show();
            }
            return;
        }

        switch (v.getId()) {
            case R.id.iv_sign_in:
                if (signDays > 0 && (signDays + 1) % 7 == 0) {
                    //领取红包
                    if (receiveHongBaoDialog != null && !receiveHongBaoDialog.isShowing()) {
                        receiveHongBaoDialog.show();
                    }
                } else {
                    //提示签到
                    signDoneInfoPresenterImp.signDone(userInfo != null ? userInfo.getId() : "", 0);
                }
                break;
            case R.id.btn_turn_profit:
                if (userGoldNum < leastGoldNum) {
                    ToastUtils.showLong("您的金币余额不足" + leastGoldNum + "，建议多攒点金币！");
                    return;
                }
                if (turnProfitDialog != null && !turnProfitDialog.isShowing()) {
                    //转换收益视频广告
                    loadAd("920819147", TTAdConstant.VERTICAL, userGoldNum);
                    taskId = "11";
                    turnProfitDialog.show();
                    turnProfitDialog.setTurnInfo(userGoldNum + "", userGoldNum / (scaleGoldNum / scaleCashNum) + "");
                }
                break;
            case R.id.btn_cash:
                Intent intent = new Intent(getActivity(), CashActivity.class);
                intent.putExtra("my_profit_money", myProfitMoney);
                startActivity(intent);
                break;
            case R.id.layout_gold_detail:
                Intent intent1 = new Intent(getActivity(), GoldDetailActivity.class);
                startActivity(intent1);
                break;
            case R.id.layout_gold_task:
                Intent intent2 = new Intent(getActivity(), GoldTaskActivity.class);
                //把list强制类型转换成Serializable类型
                intent2.putExtra("task_info_list", (Serializable) allTaskInfoList);
                startActivity(intent2);
                break;
            case R.id.layout_gold_mail:
                Intent intent3 = new Intent(getActivity(), GoldMailActivity.class);
                startActivity(intent3);
                break;
            case R.id.layout_about_gold:
                Intent intent4 = new Intent(getActivity(), AboutGoldActivity.class);
                intent4.putExtra("about_gold", aboutGoldTxt);
                startActivity(intent4);
                break;
            case R.id.iv_get_money_bg:
            case R.id.layout_get_money:
                //倒计时未结束，不可点击
                if (adCountDown > 0) {
                    ToastUtils.showLong("技能冷却中···");
                    return;
                }

                if (seeVideoIsFinish) {
                    ToastUtils.showLong("今日已领完，明天再来！");
                    return;
                }

                //加载首页领钱的广告
                loadAd("920819632", TTAdConstant.VERTICAL, 0);
                taskId = "13";

                if (seeVideoDialog != null && !seeVideoDialog.isShowing()) {
                    seeVideoDialog.show();
                }
                break;
            case R.id.tv_more_task:
                Intent intent5 = new Intent(getActivity(), GoldTaskActivity.class);
                //把list强制类型转换成Serializable类型
                intent5.putExtra("task_info_list", (Serializable) allTaskInfoList);
                startActivity(intent5);
                break;
            case R.id.tv_good_more:
                Intent intent6 = new Intent(getActivity(), GoldMailActivity.class);
                startActivity(intent6);
                break;
            case R.id.tv_turntable:
                ToastUtils.showLong("正在升级，敬请期待！");
                break;
            default:
                break;
        }
    }


    @Override
    public void seeVideo() {
        Logger.i("看视频翻倍奖励 start--->");
        if (signSuccessDialog != null && signSuccessDialog.isShowing()) {
            signSuccessDialog.dismiss();
        }
        recordId = "";
        taskRecordInfoPresenterImp.addTaskRecord(App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "", taskId, getGoldNum, 0, 0, "0");
        //step6:在获取到广告后展示
        mttRewardVideoAd.showRewardVideoAd(getActivity());
        mttRewardVideoAd = null;
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void dismissProgress() {
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void loadDataSuccess(Object tData) {
        Logger.i(JSON.toJSONString(tData));
        mRefreshLayout.setRefreshing(false);
        if (tData != null) {
            if (tData instanceof WelfareInfoRet && ((WelfareInfoRet) tData).getCode() == Constants.SUCCESS) {
                Logger.i("gold home load data success --->");
                if (((WelfareInfoRet) tData).getData() != null) {
                    WelfareInfo welfareInfo = ((WelfareInfoRet) tData).getData();

                    if (welfareInfo.getGoldUser() != null) {
                        userGoldNum = welfareInfo.getGoldUser().getGoldnum();
                        myProfitMoney = welfareInfo.getGoldUser().getCash();
                        mGoldBalanceTv.setText(userGoldNum + "");
                        mMyProfitTv.setText(myProfitMoney + "");
                        App.getApp().setUserGoldNum(userGoldNum);
                    }
                    if (welfareInfo.getCashInfoList() != null && welfareInfo.getCashInfoList().size() > 0) {

                        List<String> temp = new ArrayList<>();
                        for (int i = 0; i < welfareInfo.getCashInfoList().size(); i++) {
                            temp.add(welfareInfo.getCashInfoList().get(i).getNickname() + "提现了" + welfareInfo.getCashInfoList().get(i).getCash() + "元");
                        }

                        marqueeView.startWithList(temp);
                    }
                    //金币兑换比列
                    if (welfareInfo.getGoldScale() != null) {
                        aboutGoldTxt = welfareInfo.getGoldScale().getContent();
                        scaleGoldNum = welfareInfo.getGoldScale().getGoldnum();
                        scaleCashNum = welfareInfo.getGoldScale().getCash();
                        //最低的兑换金币
                        leastGoldNum = (int) (scaleGoldNum / scaleCashNum * 0.01);

                        mScaleRemarkTv.setText((int) scaleGoldNum + "金币≈" + (int) scaleCashNum + "元");
                    }

                    if (welfareInfo.getUserSignInfo() != null) {
                        signDays = welfareInfo.getUserSignInfo().getSigndays();
                        mSignDaysTv.setText(signDays + "");
                    }

                    if (welfareInfo.getSignInfoList() != null && welfareInfo.getSignInfoList().size() > 0) {
                        signInListAdapter.setTotalSignDay(signDays);
                        signInListAdapter.setNewData(welfareInfo.getSignInfoList());
                        hongbaoMoney = welfareInfo.getSignInfoList().get(6).getCash().split("/");
                        Logger.i("hongbaoMoney--->" + JSON.toJSONString(hongbaoMoney));
                        double temp = RandomUtils.nextDouble(Double.parseDouble(hongbaoMoney[0]), Double.parseDouble(hongbaoMoney[1]));
                        BigDecimal tempBd = new BigDecimal(temp);
                        randomHongbao = tempBd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                        Logger.i("next double value --->" + randomHongbao);
                    }

                    if (welfareInfo.getTaskInfoList() != null && welfareInfo.getTaskInfoList().size() > 0) {
                        Logger.i("temp list--->" + JSON.toJSONString(welfareInfo.getTaskInfoList()));

                        List<TaskInfo> tempList = welfareInfo.getTaskInfoList();
                        allTaskInfoList = tempList;

                        for (int i = 0; i < tempList.size(); i++) {
                            if (tempList.get(i).getId() == 4) {
                                loadAd("920819306", TTAdConstant.VERTICAL, tempList.get(i).getGoldnum());
                            }
                            //签到的信息
                            if (tempList.get(i).getId() == 7) {
                                int isSignToday = tempList.get(i).getIsFinish();
                                if (isSignToday == 1) {
                                    Glide.with(getActivity()).load(R.mipmap.sign_done_today).into(mSignInIv);
                                } else {
                                    Glide.with(getActivity()).load(R.drawable.sign_in).into(mSignInIv);
                                }
                            }

                            //首页领钱任务
                            if (tempList.get(i).getId() == 13) {
                                adCountDown = tempList.get(i).getRemainTime();
                                //adCountDown = 60;
                                seeVideoIsFinish = tempList.get(i).getIsFinish() == 1 ? true : false;
                                Logger.i("home count down--->" + adCountDown);
                                seeVideoMoneys = tempList.get(i).getCashindex().split("/");
                                randomMoney();
                                allTaskInfoList.remove(i);
                            }
                        }

                        if (adCountDown > 0) {
                            homeSeeVideoCountDown();
                        }

                        if (allTaskInfoList.size() > 3) {
                            taskListAdapter.setNewData(allTaskInfoList.subList(0, 3));
                        } else {
                            taskListAdapter.setNewData(allTaskInfoList);
                        }
                    }

                    if (welfareInfo.getGoodList() != null && welfareInfo.getGoodList().size() > 0) {
                        if (welfareInfo.getGoodList().size() > 6) {
                            goodsListAdapter.setNewData(welfareInfo.getGoodList().subList(0, 6));
                        } else {
                            goodsListAdapter.setNewData(welfareInfo.getGoodList());
                        }
                    }

                    //如果是从任务列表跳转过来的签到，怎根据规则，打开签到弹窗或者领红包的弹窗
                    if (isFromTaskSign == 1) {
                        if (signDays > 0 && (signDays + 1) % 7 == 0) {
                            //领取红包
                            if (receiveHongBaoDialog != null && !receiveHongBaoDialog.isShowing()) {
                                receiveHongBaoDialog.show();
                            }
                        } else {
                            //提示签到
                            signDoneInfoPresenterImp.signDone(userInfo != null ? userInfo.getId() : "", 0);
                        }
                    }
                }
            }

            if (tData instanceof SignDoneInfoRet) {
                Logger.i("sign done --->" + JSON.toJSONString(tData));
                if (((SignDoneInfoRet) tData).getCode() == Constants.SUCCESS) {
                    Glide.with(getActivity()).load(R.mipmap.sign_done_today).into(mSignInIv);
                    if (signDays > 0 && (signDays + 1) % 7 == 0) {
                        receiveHongBaoDialog.updateDialog(randomHongbao);
                    } else {
                        signInListAdapter.setTotalSignDay(signDays + 1);
                        signInListAdapter.notifyDataSetChanged();

                        if (signSuccessDialog != null && !signSuccessDialog.isShowing()) {

                            getGoldNum = ((SignDoneInfoRet) tData).getData().getGoldnum();
                            //签到看视频翻倍奖励
                            loadAd("920819710", TTAdConstant.VERTICAL, getGoldNum);
                            taskId = "12";

                            signSuccessDialog.show();
                            signSuccessDialog.setSignInfo(((SignDoneInfoRet) tData).getData().getGoldnum(), ((SignDoneInfoRet) tData).getData().getSigndays());
                        }
                    }
                } else {
                    //TODO 待定
                    //ToastUtils.showLong(((SignDoneInfoRet) tData).getMsg());
                    return;
                }
            }

            if (tData instanceof TaskRecordInfoRet) {
                if (((TaskRecordInfoRet) tData).getCode() == Constants.SUCCESS) {
                    if (StringUtils.isEmpty(recordId)) {
                        if (((TaskRecordInfoRet) tData).getData() != null) {
                            recordId = ((TaskRecordInfoRet) tData).getData().getInfoid();
                        }
                        Logger.i("recordId--->" + recordId);

                        if (taskId.equals("10")) {
                            miniGoldNum = ((TaskRecordInfoRet) tData).getData().getGoldnum();
                        }
                    } else {
                        if (((TaskRecordInfoRet) tData).getData() != null) {
                            if (taskId.equals("11")) {
                                ToastUtils.showLong("转换成功");
                            } else if (taskId.equals("13")) {
                                ToastUtils.showLong("获得收益" + seeVideoMoney + "元");
                            } else {
                                if ((signDays + 1) % 7 == 0) {
                                    ToastUtils.showLong("翻倍领取成功");
                                } else {
                                    ToastUtils.showLong("翻倍领取成功");
                                }
                            }
                        }
                    }
                } else {
                    //finish();
                    Logger.i("task error--->");
                }
            }
        }
    }

    public void homeSeeVideoCountDown() {
        if (adCountDown > 0) {
            mCountDownLayout.setVisibility(View.VISIBLE);

            if (seeVideoTimer != null) {
                seeVideoTimer.cancel();
                seeVideoTimer = null;
            }

            //翻倍看视频
            seeVideoTimer = new CountDownTimer(adCountDown * 1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    Logger.i("剩余时间--->" + millisUntilFinished / 1000);
                    mCountDownTv.setText(MyTimeUtil.secToTime((int) millisUntilFinished / 1000) + "");
                }

                @Override
                public void onFinish() {
                    isAccord = true;
                    adCountDown = 0;
                    mCountDownLayout.setVisibility(View.GONE);
                }
            }.start();
        } else {
            mCountDownLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void loadDataError(Throwable throwable) {
        mRefreshLayout.setRefreshing(false);
    }

    public void randomMoney() {
        Logger.i("videoMoneys--->" + JSON.toJSONString(seeVideoMoneys));
        double temp = RandomUtils.nextDouble(Double.parseDouble(seeVideoMoneys[0]), Double.parseDouble(seeVideoMoneys[1]));
        BigDecimal tempBd = new BigDecimal(temp);
        seeVideoMoney = tempBd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        Logger.i("see video money value --->" + seeVideoMoney);
    }

    @Override
    public void openHongbao() {
        if (signDoneInfoPresenterImp != null && randomHongbao > 0) {
            signDoneInfoPresenterImp.signDone(userInfo != null ? userInfo.getId() : "", randomHongbao);
        }
        loadAd("920819710", TTAdConstant.VERTICAL, 0);
    }

    @Override
    public void doubleMoney() {
        if (receiveHongBaoDialog != null && receiveHongBaoDialog.isShowing()) {
            receiveHongBaoDialog.dismiss();
        }

        taskId = "12";
        getGoldNum = 0;
        recordId = "";

        taskRecordInfoPresenterImp.addTaskRecord(App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "", taskId, getGoldNum, randomHongbao, 0, "0");
        //step6:在获取到广告后展示
        mttRewardVideoAd.showRewardVideoAd(getActivity());
        mttRewardVideoAd = null;
    }

    private boolean mHasShowDownloadActive = false;

    private void loadAd(String codeId, int orientation, int goldNum) {
        //step4:创建广告请求参数AdSlot,具体参数含义参考文档
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(codeId)
                .setSupportDeepLink(true)
                .setImageAcceptedSize(1080, 1920)
                .setRewardName("金币") //奖励的名称
                .setRewardAmount(goldNum)  //奖励的数量
                .setUserID(userInfo != null ? userInfo.getId() : "10000" + RandomUtils.nextInt())//用户id,必传参数
                .setMediaExtra("media_extra") //附加参数，可选
                .setOrientation(orientation) //必填参数，期望视频的播放方向：TTAdConstant.HORIZONTAL 或 TTAdConstant.VERTICAL
                .build();

        //step5:请求广告
        mTTAdNative.loadRewardVideoAd(adSlot, new TTAdNative.RewardVideoAdListener() {
            @Override
            public void onError(int code, String message) {
                Logger.i("code--->" + code + "---" + message);
            }

            //视频广告加载后，视频资源缓存到本地的回调，在此回调后，播放本地视频，流畅不阻塞。
            @Override
            public void onRewardVideoCached() {
                Logger.i("rewardVideoAd video cached");
            }

            //视频广告的素材加载完毕，比如视频url等，在此回调后，可以播放在线视频，网络不好可能出现加载缓冲，影响体验。
            @Override
            public void onRewardVideoAdLoad(TTRewardVideoAd ad) {
                Logger.i("rewardVideoAd loaded");

                mttRewardVideoAd = ad;
//                mttRewardVideoAd.setShowDownLoadBar(false);
                mttRewardVideoAd.setRewardAdInteractionListener(new TTRewardVideoAd.RewardAdInteractionListener() {

                    @Override
                    public void onAdShow() {
                        Logger.i("rewardVideoAd show");
                    }

                    @Override
                    public void onAdVideoBarClick() {
                        Logger.i("rewardVideoAd bar click");
                    }

                    @Override
                    public void onAdClose() {
                        Logger.i("rewardVideoAd close");
                    }

                    //视频播放完成回调
                    @Override
                    public void onVideoComplete() {
                        Logger.i("rewardVideoAd complete");
                    }

                    @Override
                    public void onVideoError() {
                        Logger.i("rewardVideoAd error");
                    }

                    //视频播放完成后，奖励验证回调，rewardVerify：是否有效，rewardAmount：奖励梳理，rewardName：奖励名称
                    @Override
                    public void onRewardVerify(boolean rewardVerify, int rewardAmount, String rewardName) {
//                        ToastUtils.showLong("verify:" + rewardVerify + " amount:" + rewardAmount +
//                                " name:" + rewardName);
                        if (rewardVerify) {
                            //ToastUtils.showLong("正常播放完成，奖励有效");
                            if (taskId.equals("11")) {
                                seeVideoMoney = userGoldNum / (scaleGoldNum / scaleCashNum);
                            }

                            if (taskId.equals("12")) {
                                seeVideoMoney = randomHongbao;
                                if ((signDays + 1) % 7 == 0) {
                                    rewardAmount = 0;
                                } else {
                                    seeVideoMoney = 0;
                                }
                            }

                            if (taskId.equals("13")) {
                                rewardAmount = 0;
                            }
                            taskRecordInfoPresenterImp.addTaskRecord(App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "", taskId, rewardAmount, seeVideoMoney, 1, recordId);
                        }
                    }

                    @Override
                    public void onSkippedVideo() {
                        Logger.i("rewardVideoAd has onSkippedVideo");
                    }
                });
                mttRewardVideoAd.setDownloadListener(new TTAppDownloadListener() {
                    @Override
                    public void onIdle() {
                        mHasShowDownloadActive = false;
                    }

                    @Override
                    public void onDownloadActive(long totalBytes, long currBytes, String fileName, String appName) {
                        if (!mHasShowDownloadActive) {
                            mHasShowDownloadActive = true;
                            ToastUtils.showLong("下载中，点击下载区域暂停", Toast.LENGTH_LONG);
                        }
                    }

                    @Override
                    public void onDownloadPaused(long totalBytes, long currBytes, String fileName, String appName) {
                        ToastUtils.showLong("下载暂停，点击下载区域继续", Toast.LENGTH_LONG);
                    }

                    @Override
                    public void onDownloadFailed(long totalBytes, long currBytes, String fileName, String appName) {
                        ToastUtils.showLong("下载失败，点击下载区域重新下载", Toast.LENGTH_LONG);
                    }

                    @Override
                    public void onDownloadFinished(long totalBytes, String fileName, String appName) {
                        ToastUtils.showLong("下载完成，点击下载区域重新下载", Toast.LENGTH_LONG);
                    }

                    @Override
                    public void onInstalled(String fileName, String appName) {
                        ToastUtils.showLong("安装完成，点击下载区域打开", Toast.LENGTH_LONG);
                    }
                });
            }
        });
    }

    @Override
    public void getMoney() {
        if (seeVideoDialog != null && seeVideoDialog.isShowing()) {
            seeVideoDialog.dismiss();
        }
        //如果有之前的倒计时任务，先取消
        if (seeVideoTimer != null) {
            seeVideoTimer.cancel();
        }

        recordId = "";
        getGoldNum = 0;

        taskRecordInfoPresenterImp.addTaskRecord(App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "", taskId, getGoldNum, 0, 0, "0");
        //step6:在获取到广告后展示
        mttRewardVideoAd.showRewardVideoAd(getActivity());
        mttRewardVideoAd = null;
    }

    @Override
    public void configTurn() {
        if (turnProfitDialog != null && turnProfitDialog.isShowing()) {
            turnProfitDialog.dismiss();
        }

        recordId = "";
        getGoldNum = 0;

        taskRecordInfoPresenterImp.addTaskRecord(App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "", taskId, getGoldNum, 0, 0, "0");
        //step6:在获取到广告后展示
        mttRewardVideoAd.showRewardVideoAd(getActivity());
        mttRewardVideoAd = null;
    }

    @Override
    public void onRefresh() {
        mRefreshLayout.setRefreshing(true);
        welfareInfoPresenterImp.getWelfareData(userInfo != null ? userInfo.getId() : "");
    }


    @Override
    public void startOpen() {
        if (!AppUtils.isAppInstalled("com.tencent.mm")) {
            Toasty.normal(getActivity(), "你还未安装微信").show();
            return;
        }

        ClipboardManager cm = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        // 将文本内容放到系统剪贴板里。
        cm.setPrimaryClip(ClipData.newPlainText(null, StringUtils.isEmpty(followPublicName) ? "头像达人" : followPublicName));

        ToastUtils.showLong("公众号已复制,可以关注了");
        AppUtils.launchApp(getActivity(), "com.tencent.mm", 1);
        followPublic();

        taskRecordInfoPresenterImp.addTaskRecord(App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "", taskId, goldNum, 0, 0, "0");
    }

    @Override
    public void closeDialog() {

    }

    public void downAppFile(String downUrl) {
        downFilePath = PathUtils.getExternalAppFilesPath() + "/temp_app.apk";
        Logger.i("down app path --->" + downFilePath);

        task = FileDownloader.getImpl().create(downUrl)
                .setPath(downFilePath)
                .setListener(new FileDownloadListener() {
                    @Override
                    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        //Toasty.normal(GoldTaskActivity.this, "正在下载打开请稍后...").show();
                        taskRecordInfoPresenterImp.addTaskRecord(App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "", taskId, goldNum, 0, 0, "0");
                    }

                    @Override
                    protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
                    }

                    @Override
                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {

                        int progress = (int) ((soFarBytes * 1.0 / totalBytes) * 100);
                        Logger.i("progress--->" + soFarBytes + "---" + totalBytes + "---" + progress);

                        Message message = new Message();
                        message.what = 0;
                        message.obj = progress;
                        mHandler.sendMessage(message);
                    }

                    @Override
                    protected void blockComplete(BaseDownloadTask task) {
                    }

                    @Override
                    protected void retry(final BaseDownloadTask task, final Throwable ex, final int retryingTimes, final int soFarBytes) {
                    }

                    @Override
                    protected void completed(BaseDownloadTask task) {
                        ToastUtils.showLong("下载完成");
                        if (downApkDialog != null && downApkDialog.isShowing()) {
                            downApkDialog.dismiss();
                        }
                        AppUtils.installApp(downFilePath);
                    }

                    @Override
                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                    }

                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {
                        if (downApkDialog != null && downApkDialog.isShowing()) {
                            downApkDialog.dismiss();
                        }
                    }

                    @Override
                    protected void warn(BaseDownloadTask task) {
                    }
                });

        task.start();
    }

    public void openMiniApp(String originId) {
        String appId = "wxd1112ca9a216aeda"; // 填应用AppId
        IWXAPI api = WXAPIFactory.createWXAPI(getActivity(), appId);

        WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
        req.userName = originId; // 填小程序原始id
        req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE;// 可选打开 开发版，体验版和正式版
        api.sendReq(req);
    }

    /**
     * 关注公众号倒计时
     */
    public void followPublic() {
        /** 倒计时30秒，一次1秒 */
        if (followCountDownTimer != null) {
            followCountDownTimer.start();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void downCancel() {

    }
}
