package com.feiyou.headstyle.ui.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.PathUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
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
import com.feiyou.headstyle.bean.SignDoneInfoRet;
import com.feiyou.headstyle.bean.TaskInfoRet;
import com.feiyou.headstyle.bean.TaskRecordInfoRet;
import com.feiyou.headstyle.bean.UserInfo;
import com.feiyou.headstyle.bean.WelfareInfo;
import com.feiyou.headstyle.common.Constants;
import com.feiyou.headstyle.presenter.SignDoneInfoPresenterImp;
import com.feiyou.headstyle.presenter.TaskInfoPresenterImp;
import com.feiyou.headstyle.presenter.TaskRecordInfoPresenterImp;
import com.feiyou.headstyle.ui.adapter.SignInListAdapter;
import com.feiyou.headstyle.ui.adapter.TaskListAdapter;
import com.feiyou.headstyle.ui.base.BaseFragmentActivity;
import com.feiyou.headstyle.ui.custom.DownFileDialog;
import com.feiyou.headstyle.ui.custom.NormalDecoration;
import com.feiyou.headstyle.ui.custom.ReceiveHongBaoDialog;
import com.feiyou.headstyle.ui.custom.SignSuccessDialog;
import com.feiyou.headstyle.ui.custom.WeiXinTaskDialog;
import com.feiyou.headstyle.utils.GoToScoreUtils;
import com.feiyou.headstyle.utils.RandomUtils;
import com.feiyou.headstyle.utils.TTAdManagerHolder;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.orhanobut.logger.Logger;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

public class GoldTaskActivity extends BaseFragmentActivity implements IBaseView, WeiXinTaskDialog.OpenWeixinListener, DownFileDialog.DownListener, ReceiveHongBaoDialog.OpenHongBaoListener {

    @BindView(R.id.iv_back)
    ImageView mBackImageView;

    @BindView(R.id.tv_title)
    TextView mTitleTv;

    @BindView(R.id.layout_top)
    RelativeLayout mTopLayout;

    @BindView(R.id.layout_task)
    LinearLayout mTaskLayout;

    @BindView(R.id.task_list_view)
    RecyclerView mTaskListView;

    private View signView;

    TextView mSignDaysTv;

    RecyclerView mSingInListView;

    ImageView mSignInIv;

    TaskListAdapter taskListAdapter;

    private String taskId = "";

    private int goldNum = 0;

    TaskRecordInfoPresenterImp taskRecordInfoPresenterImp;

    private boolean isAddTaskRecord;

    private String recordId;

    private String followPublicUrl;

    private String followPublicName;

    private WeiXinTaskDialog weiXinTaskDialog;

    private boolean isAccord;

    CountDownTimer followCountDownTimer;

    CountDownTimer marketTimer;

    //ProgressDialog downApkDialog;

    BaseDownloadTask task;

    private String downFilePath;

    private String downFilePageName;

    private int miniGoldNum;

    private TTAdNative mTTAdNative;

    private TTRewardVideoAd mttRewardVideoAd;

    TaskInfoPresenterImp taskInfoPresenterImp;

    DownFileDialog downFileDialog;

    private UserInfo mUserInfo;

    SignInListAdapter signInListAdapter;

    private int signDays;//连续签到的天数

    private String[] hongbaoMoney;

    private double randomHongbao;

    private int isSignToday;

    ReceiveHongBaoDialog receiveHongBaoDialog;

    private SignDoneInfoPresenterImp signDoneInfoPresenterImp;

    List<WelfareInfo.SignSetInfo> signList;

    private int getGoldNum;//任务完成获得的金币数

    SignSuccessDialog signSuccessDialog;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    int progress = (Integer) msg.obj;
                    downFileDialog.setProgress(progress);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected int getContextViewId() {
        return R.layout.activity_gold_task;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FileDownloader.setup(this);
        initTopBar();
        initData();
    }

    private void initTopBar() {
        FrameLayout.LayoutParams paramsTask = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        paramsTask.setMargins(0, BarUtils.getStatusBarHeight() + SizeUtils.dp2px(10), 0, 0);
        paramsTask.topMargin = BarUtils.getStatusBarHeight();
        mTaskLayout.setLayoutParams(paramsTask);
        mTaskLayout.setGravity(Gravity.TOP);
    }

    public void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            randomHongbao = bundle.getDouble("random_money");
            signDays = bundle.getInt("sign_days");
            isSignToday = bundle.getInt("is_sign_today");
        }

        signList = (List<WelfareInfo.SignSetInfo>) getIntent().getSerializableExtra("sign_list");

        signDoneInfoPresenterImp = new SignDoneInfoPresenterImp(this, this);
        signSuccessDialog = new SignSuccessDialog(this, R.style.login_dialog);
        receiveHongBaoDialog = new ReceiveHongBaoDialog(this, R.style.login_dialog);
        receiveHongBaoDialog.setOpenHongBaoListener(this);

        signView = LayoutInflater.from(this).inflate(R.layout.sign_in_view, null);
        mSingInListView = signView.findViewById(R.id.sign_in_list_view);
        mSignInIv = signView.findViewById(R.id.iv_sign_in);
        mSignDaysTv = signView.findViewById(R.id.tv_sign_days);
        mSignDaysTv.setText(signDays + "");
        mSignInIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSignToday == 0) {
                    if (signDays > 0 && (signDays + 1) % 7 == 0) {
                        //领取红包
                        if (receiveHongBaoDialog != null && !receiveHongBaoDialog.isShowing()) {
                            receiveHongBaoDialog.show();
                        }
                    } else {
                        //提示签到
                        signDoneInfoPresenterImp.signDone(mUserInfo != null ? mUserInfo.getId() : "", mUserInfo != null ? mUserInfo.getOpenid() : "", 0);
                    }
                } else {
                    ToastUtils.showLong("今天已签到");
                    return;
                }
            }
        });

        if (isSignToday == 1) {
            Glide.with(this).load(R.mipmap.sign_done_today).into(mSignInIv);
        } else {
            Glide.with(this).load(R.drawable.sign_in).into(mSignInIv);
        }

        TTAdManager ttAdManager = TTAdManagerHolder.get();
        //step3:创建TTAdNative对象,用于调用广告请求接口
        mTTAdNative = ttAdManager.createAdNative(this);

        taskRecordInfoPresenterImp = new TaskRecordInfoPresenterImp(this, this);
        taskInfoPresenterImp = new TaskInfoPresenterImp(this, this);

        weiXinTaskDialog = new WeiXinTaskDialog(GoldTaskActivity.this);
        weiXinTaskDialog.setOpenWeixinListener(this);

        downFileDialog = new DownFileDialog(this, R.style.login_dialog);
        downFileDialog.setDownListener(this);

        taskListAdapter = new TaskListAdapter(this, null);
        mTaskListView.setLayoutManager(new LinearLayoutManager(this));
        mTaskListView.addItemDecoration(new NormalDecoration(ContextCompat.getColor(this, R.color.line_color), 1));
        mTaskListView.setAdapter(taskListAdapter);
        taskListAdapter.addHeaderView(signView);

        signInListAdapter = new SignInListAdapter(this, signList);
        signInListAdapter.setTotalSignDay(signDays);
        mSingInListView.setLayoutManager(new GridLayoutManager(this, 7));
        mSingInListView.setAdapter(signInListAdapter);

        View footView = new View(this);
        footView.setBackgroundResource(R.drawable.sign_foot_bg);
        ViewGroup.LayoutParams footParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(10));
        footView.setLayoutParams(footParams);
        taskListAdapter.addFooterView(footView);

        taskListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (taskListAdapter.getData().get(position).getIsFinish() == 1) {
                    return;
                }
                int id = taskListAdapter.getData().get(position).getId();
                switch (id) {
                    case 1:
                        Intent intent = new Intent(GoldTaskActivity.this, PushNoteActivity.class);
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
                        Intent intent1 = new Intent(GoldTaskActivity.this, CommunityArticleActivity.class);
                        intent1.putExtra("msg_id", App.getApp().getRandomNoteId());
                        intent1.putExtra("is_from_task", 1);
                        startActivity(intent1);
                        break;
                    case 4:
                        taskId = "4";
                        recordId = "";
                        taskRecordInfoPresenterImp.addTaskRecord(mUserInfo.getId(), mUserInfo.getOpenid(), taskId, taskListAdapter.getData().get(position).getGoldnum(), 0, 0, "0");
                        if (mttRewardVideoAd != null) {
                            //step6:在获取到广告后展示
                            mttRewardVideoAd.showRewardVideoAd(GoldTaskActivity.this);
                            mttRewardVideoAd = null;
                        }
                        break;
                    case 5:
                        if (App.testInfoList != null && App.testInfoList.size() > 0) {
                            if (App.testInfoList.get(0).getTestType() == 1) {
                                Intent intent2 = new Intent(GoldTaskActivity.this, TestDetailActivity.class);
                                intent2.putExtra("tid", App.testInfoList.get(0).getId());
                                intent2.putExtra("is_from_task", 1);
                                startActivity(intent2);
                            } else {
                                Intent intent2 = new Intent(GoldTaskActivity.this, TestImageDetailActivity.class);
                                intent2.putExtra("tid", App.testInfoList.get(0).getId());
                                intent2.putExtra("is_from_task", 1);
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
                        GoToScoreUtils.goToMarket(GoldTaskActivity.this, Constants.APP_PACKAGE_NAME);
                        break;
                    case 7:
//                        Intent intent4 = new Intent(GoldTaskActivity.this, MainActivity.class);
//                        intent4.putExtra("home_index", 2);
//                        intent4.putExtra("is_from_task_sign", 1);
//                        startActivity(intent4);

                        //App.getApp().setIsFromTaskSign(1);
                        //finish();

                        if (signDays > 0 && (signDays + 1) % 7 == 0 && isSignToday == 0) {
                            //领取红包
                            if (receiveHongBaoDialog != null && !receiveHongBaoDialog.isShowing()) {
                                receiveHongBaoDialog.show();
                            }
                        } else {
                            //提示签到
                            signDoneInfoPresenterImp.signDone(mUserInfo != null ? mUserInfo.getId() : "", mUserInfo != null ? mUserInfo.getOpenid() : "", 0);
                        }

                        break;
                    case 8:

                        taskId = "8";
                        recordId = "";
                        downFilePageName = taskListAdapter.getData().get(position).getWeburl();

                        if (AppUtils.isAppInstalled(downFilePageName)) {
                            //已安装
                            taskRecordInfoPresenterImp.addTaskRecord(mUserInfo.getId(), mUserInfo.getOpenid(), taskId, goldNum, 0, 0, "0");
                            AppUtils.launchApp(downFilePageName);
                        } else {
                            if (downFileDialog != null && !downFileDialog.isShowing()) {
                                downFileDialog.show();
                            }

                            downAppFile(taskListAdapter.getData().get(position).getDownaddress());
                        }
                        break;
                    case 9:
                        Intent intent3 = new Intent(GoldTaskActivity.this, AdActivity.class);
                        intent3.putExtra("open_url", taskListAdapter.getData().get(position).getWeburl());
                        intent3.putExtra("is_from_task", 1);
                        startActivity(intent3);
                        break;
                    case 10:
                        taskId = "10";
                        recordId = "";
                        taskRecordInfoPresenterImp.addTaskRecord(mUserInfo.getId(), mUserInfo.getOpenid(), taskId, miniGoldNum, 0, 1, "0");
                        openMiniApp(taskListAdapter.getData().get(position).getOldid());
                        break;
                    default:
                        break;
                }
            }
        });

        followCountDownTimer = new CountDownTimer(20 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                isAccord = false;
                Logger.i("剩余时间--->" + millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                isAccord = true;
            }
        };
    }

    @Override
    public void onResume() {
        super.onResume();
        mUserInfo = App.getApp().mUserInfo != null ? App.getApp().mUserInfo : new UserInfo();

        Logger.i("gold task onresume--->");

        taskInfoPresenterImp.taskList(App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "");

        if (taskId.equals("2")) {
            if (isAccord) {
                taskRecordInfoPresenterImp.addTaskRecord(mUserInfo.getId(), mUserInfo.getOpenid(), taskId, goldNum, 0, 1, recordId);
            } else {
                ToastUtils.showLong("任务失败");
                recordId = "";
                taskId = "";
                isAccord = false;
                if (followCountDownTimer != null) {
                    followCountDownTimer.cancel();
                }
            }
        }

        if (taskId.equals("8")) {
            if (AppUtils.isAppInstalled(downFilePageName)) {
                taskRecordInfoPresenterImp.addTaskRecord(mUserInfo.getId(), mUserInfo.getOpenid(), taskId, goldNum, 0, 1, recordId);
            }
        }

        if (taskId.equals("10")) {
            ToastUtils.showLong("领取成功 +" + miniGoldNum + "金币");
            recordId = "";
            taskId = "";
            isAccord = false;
        }

        if (taskId.equals("6")) {
            if (isAccord) {
                taskRecordInfoPresenterImp.addTaskRecord(mUserInfo.getId(), mUserInfo.getOpenid(), taskId, goldNum, 0, 1, recordId);
            } else {
                ToastUtils.showLong("任务失败");
                recordId = "";
                taskId = "";
                isAccord = false;
                if (marketTimer != null) {
                    marketTimer.cancel();
                }
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Logger.i("gold task onPause");
        if (taskId.equals("6")) {
            /** 倒计时30秒，一次1秒 */
            marketTimer = new CountDownTimer(20 * 1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    isAccord = false;
                    Logger.i("剩余时间--->" + millisUntilFinished / 1000);
                }

                @Override
                public void onFinish() {
                    isAccord = true;
                }
            }.start();
            taskRecordInfoPresenterImp.addTaskRecord(mUserInfo.getId(), mUserInfo.getOpenid(), taskId, goldNum, 0, 0, "0");
        }
    }

    public void openMiniApp(String originId) {
        String appId = "wxd1112ca9a216aeda"; // 填应用AppId
        IWXAPI api = WXAPIFactory.createWXAPI(this, appId);

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

        if (tData instanceof TaskInfoRet) {
            if (((TaskInfoRet) tData).getCode() == Constants.SUCCESS) {
                if (((TaskInfoRet) tData).getData() != null && ((TaskInfoRet) tData).getData().size() > 0) {
                    for (int i = 0; i < ((TaskInfoRet) tData).getData().size(); i++) {
                        if (((TaskInfoRet) tData).getData().get(i).getId() == 4) {
                            //预加载视频广告
                            loadAd("920819888", TTAdConstant.VERTICAL, ((TaskInfoRet) tData).getData().get(i).getGoldnum());
                        }
                        if (((TaskInfoRet) tData).getData().get(i).getId() == 10) {
                            miniGoldNum = ((TaskInfoRet) tData).getData().get(i).getGoldnum();
                        }
                    }
                    taskListAdapter.setNewData(((TaskInfoRet) tData).getData());
                }
            }
        }

        if (tData instanceof TaskRecordInfoRet) {
            if (((TaskRecordInfoRet) tData).getCode() == Constants.SUCCESS) {
                if (StringUtils.isEmpty(recordId)) {
                    isAddTaskRecord = true;
                    if (((TaskRecordInfoRet) tData).getData() != null) {
                        recordId = ((TaskRecordInfoRet) tData).getData().getInfoid();
                    }
                    if (taskId.equals("10")) {
                        miniGoldNum = ((TaskRecordInfoRet) tData).getData().getGoldnum();
                        ToastUtils.showLong("领取成功 +" + miniGoldNum + "金币");
                        recordId = "";
                        taskId = "";
                        isAccord = false;
                    }
                    Logger.i("recordId--->" + recordId);
                } else {
                    if (((TaskRecordInfoRet) tData).getData() != null) {

                        int tempGold = ((TaskRecordInfoRet) tData).getData().getGoldnum();

                        if (tempGold == 0) {
                            ToastUtils.showLong("获得收益 +" + ((TaskRecordInfoRet) tData).getData().getCash() + "元");
                        } else {
                            ToastUtils.showLong("领取成功 +" + ((TaskRecordInfoRet) tData).getData().getGoldnum() + "金币");
                        }

                    }

                    //任务完成后再次刷新页面
                    taskInfoPresenterImp.taskList(App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "");
                }
            } else {
                //finish();
                Logger.i("task error--->");
            }
        }

        if (tData instanceof SignDoneInfoRet) {
            Logger.i("sign done --->" + JSON.toJSONString(tData));
            if (((SignDoneInfoRet) tData).getCode() == Constants.SUCCESS) {
                Glide.with(this).load(R.mipmap.sign_done_today).into(mSignInIv);
                isSignToday = 1;
                if (signDays > 0 && (signDays + 1) % 7 == 0) {
                    receiveHongBaoDialog.updateDialog(randomHongbao);
                } else {
                    signInListAdapter.setTotalSignDay(signDays + 1);
                    signInListAdapter.notifyDataSetChanged();

                    if (signSuccessDialog != null && !signSuccessDialog.isShowing()) {

                        getGoldNum = ((SignDoneInfoRet) tData).getData().getGoldnum();
                        //签到看视频翻倍奖励
                        loadAd("920819888", TTAdConstant.VERTICAL, getGoldNum);
                        taskId = "12";

                        signSuccessDialog.show();
                        signSuccessDialog.setSignInfo(((SignDoneInfoRet) tData).getData().getGoldnum(), ((SignDoneInfoRet) tData).getData().getSigndays());
                    }
                }
            } else {
                //TODO 待定
                ToastUtils.showLong(((SignDoneInfoRet) tData).getMsg());
                return;
            }
        }

    }

    @Override
    public void loadDataError(Throwable throwable) {

    }

    @Override
    public void startOpen() {
        if (!AppUtils.isAppInstalled("com.tencent.mm")) {
            Toasty.normal(this, "你还未安装微信").show();
            return;
        }

        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        // 将文本内容放到系统剪贴板里。
        cm.setPrimaryClip(ClipData.newPlainText(null, StringUtils.isEmpty(followPublicName) ? "头像达人" : followPublicName));

        ToastUtils.showLong("公众号已复制,可以关注了");
        AppUtils.launchApp("com.tencent.mm");
        followPublic();

        taskRecordInfoPresenterImp.addTaskRecord(mUserInfo.getId(), mUserInfo.getOpenid(), taskId, goldNum, 0, 0, "0");
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
                        taskRecordInfoPresenterImp.addTaskRecord(mUserInfo.getId(), mUserInfo.getOpenid(), taskId, goldNum, 0, 0, "0");
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
                        if (downFileDialog != null && downFileDialog.isShowing()) {
                            downFileDialog.dismiss();
                        }
                        AppUtils.installApp(downFilePath);
                    }

                    @Override
                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                    }

                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {
                        if (downFileDialog != null && downFileDialog.isShowing()) {
                            downFileDialog.dismiss();
                        }
                    }

                    @Override
                    protected void warn(BaseDownloadTask task) {
                    }
                });

        task.start();
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
                .setUserID(App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "10000" + RandomUtils.nextInt())//用户id,必传参数
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
                        if (rewardVerify) {
                            taskRecordInfoPresenterImp.addTaskRecord(mUserInfo.getId(), mUserInfo.getOpenid(), taskId, rewardAmount, 0, 1, recordId);
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
                            //ToastUtils.showLong("下载中，点击下载区域暂停", Toast.LENGTH_LONG);
                        }
                    }

                    @Override
                    public void onDownloadPaused(long totalBytes, long currBytes, String fileName, String appName) {
                        //ToastUtils.showLong("下载暂停，点击下载区域继续", Toast.LENGTH_LONG);
                    }

                    @Override
                    public void onDownloadFailed(long totalBytes, long currBytes, String fileName, String appName) {
                        //ToastUtils.showLong("下载失败，点击下载区域重新下载", Toast.LENGTH_LONG);
                    }

                    @Override
                    public void onDownloadFinished(long totalBytes, String fileName, String appName) {
                        //ToastUtils.showLong("下载完成，点击下载区域重新下载", Toast.LENGTH_LONG);
                    }

                    @Override
                    public void onInstalled(String fileName, String appName) {
                        //ToastUtils.showLong("安装完成，点击下载区域打开", Toast.LENGTH_LONG);
                    }
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (followCountDownTimer != null) {
            followCountDownTimer.cancel();
            followCountDownTimer.onFinish();
            followCountDownTimer = null;
        }
        if (marketTimer != null) {
            marketTimer.cancel();
            marketTimer.onFinish();
            marketTimer = null;
        }
    }

    @Override
    public void downCancel() {
        ToastUtils.showLong("任务失败");
        if (task != null) {
            task.pause();
        }
    }

    @Override
    public void openHongbao() {
        if (signDoneInfoPresenterImp != null && randomHongbao > 0) {
            signDoneInfoPresenterImp.signDone(mUserInfo != null ? mUserInfo.getId() : "", mUserInfo != null ? mUserInfo.getOpenid() : "", randomHongbao);
        }
        loadAd("920819888", TTAdConstant.VERTICAL, 0);
    }

    @OnClick(R.id.iv_back)
    void back() {
        popBackStack();
    }

    @Override
    public void doubleMoney() {
        if (receiveHongBaoDialog != null && receiveHongBaoDialog.isShowing()) {
            receiveHongBaoDialog.dismiss();
        }

        taskId = "12";
        recordId = "";
        String openid = App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getOpenid() : "";
        taskRecordInfoPresenterImp.addTaskRecord(App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "", openid, taskId, 0, randomHongbao, 0, "0");
        if (mttRewardVideoAd != null) {
            //step6:在获取到广告后展示
            mttRewardVideoAd.showRewardVideoAd(this);
            mttRewardVideoAd = null;
        }
    }
}
