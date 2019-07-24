package com.feiyou.headstyle.ui.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.PathUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
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
import com.feiyou.headstyle.bean.TaskInfoRet;
import com.feiyou.headstyle.bean.TaskRecordInfoRet;
import com.feiyou.headstyle.common.Constants;
import com.feiyou.headstyle.presenter.TaskInfoPresenterImp;
import com.feiyou.headstyle.presenter.TaskRecordInfoPresenterImp;
import com.feiyou.headstyle.ui.adapter.TaskListAdapter;
import com.feiyou.headstyle.ui.base.BaseActivity;
import com.feiyou.headstyle.ui.base.BaseFragmentActivity;
import com.feiyou.headstyle.ui.custom.DownFileDialog;
import com.feiyou.headstyle.ui.custom.NormalDecoration;
import com.feiyou.headstyle.ui.custom.WeiXinTaskDialog;
import com.feiyou.headstyle.utils.GoToScoreUtils;
import com.feiyou.headstyle.utils.RandomUtils;
import com.feiyou.headstyle.utils.TTAdManagerHolder;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.orhanobut.logger.Logger;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import butterknife.BindView;
import es.dmoral.toasty.Toasty;

public class GoldTaskActivity extends BaseActivity implements IBaseView, WeiXinTaskDialog.OpenWeixinListener, DownFileDialog.DownListener {

    @BindView(R.id.topbar)
    QMUITopBar mTopBar;

    ImageView mBackImageView;

    @BindView(R.id.task_list_view)
    RecyclerView mTaskListView;

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
    protected int getLayoutId() {
        return R.layout.activity_gold_task;
    }

    @Override
    protected void initVars() {
        FileDownloader.setup(this);
        initTopBar();
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        TTAdManager ttAdManager = TTAdManagerHolder.get();
        //step2:(可选，强烈建议在合适的时机调用):申请部分权限，如read_phone_state,防止获取不了imei时候，下载类广告没有填充的问题。
        TTAdManagerHolder.get().requestPermissionIfNecessary(this);
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
                        taskRecordInfoPresenterImp.addTaskRecord(App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "", taskId, taskListAdapter.getData().get(position).getGoldnum(), 0, 0, "0");
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
                                startActivity(intent2);
                            } else {
                                Intent intent2 = new Intent(GoldTaskActivity.this, TestImageDetailActivity.class);
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
                        GoToScoreUtils.goToMarket(GoldTaskActivity.this, Constants.APP_PACKAGE_NAME);
                        break;
                    case 7:
                        Intent intent4 = new Intent(GoldTaskActivity.this, Main1Activity.class);
                        intent4.putExtra("home_index", 2);
                        intent4.putExtra("is_from_task_sign", 1);
                        startActivity(intent4);
                        break;
                    case 8:

                        taskId = "8";
                        recordId = "";
                        downFilePageName = taskListAdapter.getData().get(position).getWeburl();

                        if (AppUtils.isAppInstalled(downFilePageName)) {
                            //已安装
                            taskRecordInfoPresenterImp.addTaskRecord(App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "", taskId, goldNum, 0, 0, "0");
                            AppUtils.launchApp(GoldTaskActivity.this, downFilePageName, 1);
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
                        taskRecordInfoPresenterImp.addTaskRecord(App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "", taskId, miniGoldNum, 0, 1, "0");
                        openMiniApp(taskListAdapter.getData().get(position).getOldid());
                        break;
                    default:
                        break;
                }
            }
        });

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
    }

    private void initTopBar() {
        QMUIStatusBarHelper.setStatusBarLightMode(this);
        View aboutView = getLayoutInflater().inflate(R.layout.common_top_back, null);
        aboutView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(48)));
        TextView titleTv = aboutView.findViewById(R.id.tv_title);
        titleTv.setText("金币任务");

        mTopBar.setCenterView(aboutView);
        mBackImageView = aboutView.findViewById(R.id.iv_back);
        mBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //popBackStack();
                finish();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Logger.i("gold task onresume--->");

        taskInfoPresenterImp.taskList(App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "");

        if (taskId.equals("2")) {
            if (isAccord) {
                taskRecordInfoPresenterImp.addTaskRecord(App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "", taskId, goldNum, 0, 1, recordId);
            } else {
                if (followCountDownTimer != null) {
                    followCountDownTimer.cancel();
                }
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
            marketTimer = new CountDownTimer(30 * 1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    Logger.i("剩余时间--->" + millisUntilFinished / 1000);
                }

                @Override
                public void onFinish() {
                    isAccord = true;
                }
            }.start();
            taskRecordInfoPresenterImp.addTaskRecord(App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "", taskId, goldNum, 0, 0, "0");
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
        //popBackStack();
        finish();
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
                            loadAd("920819306", TTAdConstant.VERTICAL, ((TaskInfoRet) tData).getData().get(i).getGoldnum());
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
                    }
                    Logger.i("recordId--->" + recordId);
                } else {
                    if (((TaskRecordInfoRet) tData).getData() != null) {
                        ToastUtils.showLong("领取成功 +" + ((TaskRecordInfoRet) tData).getData().getGoldnum() + "金币");
                    }
                }
            } else {
                //finish();
                Logger.i("task error--->");
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
        AppUtils.launchApp(this, "com.tencent.mm", 1);
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
                            taskRecordInfoPresenterImp.addTaskRecord(App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "", taskId, rewardAmount, 0, 1, recordId);
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
    }
}
