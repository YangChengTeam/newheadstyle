package com.feiyou.headstyle.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.feiyou.headstyle.App;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.base.IBaseView;
import com.feiyou.headstyle.bean.TaskRecordInfoRet;
import com.feiyou.headstyle.common.Constants;
import com.feiyou.headstyle.presenter.TaskRecordInfoPresenterImp;
import com.feiyou.headstyle.ui.base.BaseFragmentActivity;
import com.feiyou.headstyle.ui.custom.SignOutDialog;
import com.just.agentweb.AgentWeb;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;

import butterknife.BindView;

/**
 * Created by myflying on 2018/11/23.
 */


public class AdActivity extends BaseFragmentActivity implements IBaseView, SignOutDialog.SignOutListener {

    @BindView(R.id.topbar)
    QMUITopBar mTopBar;

    ImageView mBackImageView;

    @BindView(R.id.layout_ad)
    LinearLayout mAdLayout;

    @BindView(R.id.tv_count_down)
    TextView mCountDownTv;

    TextView titleTv;

    AgentWeb mAgentWeb;

    private String taskId = "9";

    private int goldNum = 0;

    TaskRecordInfoPresenterImp taskRecordInfoPresenterImp;

    private boolean isAddTaskRecord;

    private String recordId;

    private int isFromTask;

    private SignOutDialog signOutDialog;

    private boolean isFinish;

    CountDownTimer timer;

    @Override
    protected int getContextViewId() {
        return R.layout.activity_ad;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTopBar();
        initData();
    }

    private void initTopBar() {
        QMUIStatusBarHelper.setStatusBarLightMode(this);
        View topSearchView = getLayoutInflater().inflate(R.layout.common_top_back, null);
        topSearchView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(48)));
        titleTv = topSearchView.findViewById(R.id.tv_title);

        mTopBar.setCenterView(topSearchView);
        mBackImageView = topSearchView.findViewById(R.id.iv_back);
        mBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isFinish) {
                    if (signOutDialog != null && !signOutDialog.isShowing()) {
                        signOutDialog.show();
                    }
                } else {
                    popBackStack();
                }
            }
        });
    }

    public void initData() {
        signOutDialog = new SignOutDialog(this, R.style.login_dialog);
        signOutDialog.setSignOutListener(this);

        String openUrl = "http://gx.qqtn.com";
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.getString("open_url") != null) {
            openUrl = bundle.getString("open_url");
        }

        if (bundle != null && bundle.getString("ad_title") != null) {
            String title = bundle.getString("ad_title");
            titleTv.setText(title);
        } else {
            titleTv.setText("精选推荐");
        }

        isFromTask = bundle.getInt("is_from_task");

        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(mAdLayout, new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()
                .setWebChromeClient(mWebChromeClient)
                .setWebViewClient(mWebViewClient)
                .createAgentWeb()
                .ready()
                .go(openUrl);
        taskRecordInfoPresenterImp = new TaskRecordInfoPresenterImp(this, this);
        if (isFromTask > 0) {
            taskRecordInfoPresenterImp.addTaskRecord(App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "", taskId, goldNum, 0, 0, "0");
        }
    }

    private WebViewClient mWebViewClient = new WebViewClient() {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            //do you  work
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            if (isFromTask > 0) {
                mCountDownTv.setVisibility(View.VISIBLE);

                /** 倒计时30秒，一次1秒 */
                timer = new CountDownTimer(30 * 1000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        mCountDownTv.setText("倒计时 " + millisUntilFinished / 1000 + " S");
                    }

                    @Override
                    public void onFinish() {
                        isFinish = true;
                        if (isFromTask > 0 && !StringUtils.isEmpty(recordId)) {
                            mCountDownTv.setVisibility(View.GONE);
                            taskRecordInfoPresenterImp.addTaskRecord(App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "", taskId, goldNum, 0, 1, recordId);
                        }
                    }
                }.start();
            }
        }
    };
    private WebChromeClient mWebChromeClient = new WebChromeClient() {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            //do you work
        }
    };

    @Override
    public void onBackPressed() {
        if (!isFinish) {
            if (signOutDialog != null && !signOutDialog.isShowing()) {
                signOutDialog.show();
            }
        } else {
            popBackStack();
        }
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
                } else {
                    if (((TaskRecordInfoRet) tData).getData() != null) {
                        ToastUtils.showLong("领取成功 +" + ((TaskRecordInfoRet) tData).getData().getGoldnum() + "金币");
                    }
                }
            }
        }
    }

    @Override
    public void loadDataError(Throwable throwable) {

    }

    @Override
    public void configSignOut() {
        ToastUtils.showLong("任务失败");
        popBackStack();
    }

    @Override
    public void cancelSignOut() {

    }

    @Override
    public void onPause() {
        super.onPause();
        if (timer != null) {
            timer.cancel();
        }
    }
}
