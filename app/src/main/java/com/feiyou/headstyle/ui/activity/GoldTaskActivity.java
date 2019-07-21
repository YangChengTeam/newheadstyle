package com.feiyou.headstyle.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
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

import com.blankj.utilcode.util.PathUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feiyou.headstyle.App;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.base.IBaseView;
import com.feiyou.headstyle.bean.TaskInfo;
import com.feiyou.headstyle.bean.TaskRecordInfoRet;
import com.feiyou.headstyle.common.Constants;
import com.feiyou.headstyle.presenter.TaskRecordInfoPresenterImp;
import com.feiyou.headstyle.ui.adapter.CashRecordAdapter;
import com.feiyou.headstyle.ui.adapter.TaskListAdapter;
import com.feiyou.headstyle.ui.base.BaseFragmentActivity;
import com.feiyou.headstyle.ui.custom.NormalDecoration;
import com.feiyou.headstyle.ui.custom.WeiXinTaskDialog;

import com.blankj.utilcode.util.AppUtils;
import com.feiyou.headstyle.utils.GoToScoreUtils;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.orhanobut.logger.Logger;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import es.dmoral.toasty.Toasty;

public class GoldTaskActivity extends BaseFragmentActivity implements IBaseView, WeiXinTaskDialog.OpenWeixinListener {

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

    ProgressDialog downApkDialog;

    BaseDownloadTask task;

    private String downFilePath;

    private String downFilePageName;

    private String miniGoldNum = "0";

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    int progress = (Integer) msg.obj;
                    downApkDialog.setProgress(progress);
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
                popBackStack();
            }
        });
    }

    public void initData() {
        taskRecordInfoPresenterImp = new TaskRecordInfoPresenterImp(this, this);

        weiXinTaskDialog = new WeiXinTaskDialog(GoldTaskActivity.this);
        weiXinTaskDialog.setOpenWeixinListener(this);

        List<TaskInfo> tempList = (List<TaskInfo>) getIntent().getSerializableExtra("task_info_list");
        taskListAdapter = new TaskListAdapter(this, tempList);
        mTaskListView.setLayoutManager(new LinearLayoutManager(this));
        mTaskListView.addItemDecoration(new NormalDecoration(ContextCompat.getColor(this, R.color.line_color), 1));
        mTaskListView.setAdapter(taskListAdapter);

        taskListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
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
                        Intent intent1 = new Intent(GoldTaskActivity.this, Main1Activity.class);
                        intent1.putExtra("home_index", 1);
                        startActivity(intent1);
                        break;
                    case 5:
                        Intent intent2 = new Intent(GoldTaskActivity.this, Main1Activity.class);
                        intent2.putExtra("home_index", 3);
                        startActivity(intent2);
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
                        intent4.putExtra("is_from_task_sign",1);
                        startActivity(intent4);
                        break;
                    case 8:
                        taskId = "8";
                        recordId = "";
                        downFilePageName = taskListAdapter.getData().get(position).getWeburl();
                        downApkDialog = new ProgressDialog(GoldTaskActivity.this);
                        //依次设置标题,内容,是否用取消按钮关闭,是否显示进度
                        downApkDialog.setTitle("下载" + taskListAdapter.getData().get(position).getTitle());
                        downApkDialog.setMessage("正在下载,请稍后...");
                        downApkDialog.setCancelable(true);
                        //这里是设置进度条的风格,HORIZONTAL是水平进度条,SPINNER是圆形进度条
                        downApkDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        downApkDialog.setIndeterminate(false);
                        //调用show()方法将ProgressDialog显示出来
                        downApkDialog.show();
                        downAppFile(taskListAdapter.getData().get(position).getDownaddress());
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
                        taskRecordInfoPresenterImp.addTaskRecord(App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "", taskId, goldNum, 0, 1, "0");
                        openMiniApp(taskListAdapter.getData().get(position).getOldid());
                        break;
                    default:
                        break;
                }
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();

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
        followCountDownTimer = new CountDownTimer(30 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Logger.i("剩余时间--->" + millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                isAccord = true;
            }
        }.start();
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
}
