package com.feiyou.headstyle.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.PhoneUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.feiyou.headstyle.App;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.base.IBaseView;
import com.feiyou.headstyle.bean.EveryDayHbRet;
import com.feiyou.headstyle.bean.EveryDayHbWrapper;
import com.feiyou.headstyle.bean.MessageEvent;
import com.feiyou.headstyle.bean.ReceiveUserInfo;
import com.feiyou.headstyle.bean.TaskRecordInfoRet;
import com.feiyou.headstyle.bean.UserInfo;
import com.feiyou.headstyle.common.Constants;
import com.feiyou.headstyle.presenter.EveryDayHongBaoPresenterImp;
import com.feiyou.headstyle.presenter.TaskRecordInfoPresenterImp;
import com.feiyou.headstyle.ui.adapter.ReceiveUserListAdapter;
import com.feiyou.headstyle.ui.base.BaseFragmentActivity;
import com.feiyou.headstyle.ui.custom.LoginDialog;
import com.feiyou.headstyle.utils.RandomUtils;
import com.feiyou.headstyle.utils.RomUtils;
import com.orhanobut.logger.Logger;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.umeng.socialize.UMShareAPI;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class EveryDayHongBaoActivity extends BaseFragmentActivity implements IBaseView, LoginDialog.LoginCloseListener {

    @BindView(R.id.topbar)
    QMUITopBar mTopBar;

    ImageView mBackImageView;

    @BindView(R.id.every_day_list)
    RecyclerView mReceiveUserListView;

    TextView mMoneyTv;

    ImageView mMoneyMaskIv;

    ImageView mCashMoneyIv;

    TextView mRandomNumTv;

    View topView;

    View footView;

    ReceiveUserListAdapter receiveUserListAdapter;

    private String recordId;

    private String[] seeVideoMoneys;

    private double seeVideoMoney;//看视频可得到的收益

    private EveryDayHongBaoPresenterImp everyDayHongBaoPresenterImp;

    private TaskRecordInfoPresenterImp taskRecordInfoPresenterImp;

    private UserInfo mUserInfo;

    LoginDialog loginDialog;

    @Override
    protected int getContextViewId() {
        return R.layout.activity_every_day_hongbao;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        initTopBar();
        initData();
    }

    private void initTopBar() {
        QMUIStatusBarHelper.setStatusBarLightMode(this);
        View aboutView = getLayoutInflater().inflate(R.layout.common_top_back, null);
        aboutView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(48)));
        TextView titleTv = aboutView.findViewById(R.id.tv_title);
        titleTv.setText("天天领红包");

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
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            recordId = bundle.getString("record_id");
        }

        if (!StringUtils.isEmpty(SPUtils.getInstance().getString(Constants.USER_INFO))) {
            Logger.i(SPUtils.getInstance().getString(Constants.USER_INFO));
            mUserInfo = JSON.parseObject(SPUtils.getInstance().getString(Constants.USER_INFO), new TypeReference<UserInfo>() {
            });
            App.getApp().setmUserInfo(mUserInfo);
            App.getApp().setLogin(true);
        }


        topView = LayoutInflater.from(this).inflate(R.layout.every_day_top_view, null);
        footView = LayoutInflater.from(this).inflate(R.layout.every_day_foot_view, null);
        mMoneyTv = topView.findViewById(R.id.tv_money);
        mMoneyMaskIv = topView.findViewById(R.id.layout_money_mask);
        mCashMoneyIv = topView.findViewById(R.id.iv_cash_money);
        mRandomNumTv = topView.findViewById(R.id.tv_random_num);

        Glide.with(this).load(R.drawable.iv_get_money).into(mCashMoneyIv);

        loginDialog = new LoginDialog(this, R.style.login_dialog);
        loginDialog.setLoginCloseListener(this);
        everyDayHongBaoPresenterImp = new EveryDayHongBaoPresenterImp(this, this);
        taskRecordInfoPresenterImp = new TaskRecordInfoPresenterImp(this, this);

        receiveUserListAdapter = new ReceiveUserListAdapter(this, null);
        mReceiveUserListView.setLayoutManager(new LinearLayoutManager(this));
        mReceiveUserListView.setAdapter(receiveUserListAdapter);
        receiveUserListAdapter.addHeaderView(topView);
        receiveUserListAdapter.addFooterView(footView);

        mCashMoneyIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mUserInfo == null) {
                    mMoneyTv.setVisibility(View.GONE);
                    mMoneyMaskIv.setVisibility(View.VISIBLE);
                    if (loginDialog != null && !loginDialog.isShowing()) {
                        loginDialog.show();
                    }
                    return;
                }

                Intent intent = new Intent(EveryDayHongBaoActivity.this, CashActivity.class);
                startActivity(intent);
            }
        });

        try {
            everyDayHongBaoPresenterImp.everyDayHongBaoInfo(mUserInfo != null ? mUserInfo.getId() : "0", mUserInfo != null ? mUserInfo.getOpenid() : "", PhoneUtils.getIMEI());
        } catch (SecurityException e) {
            e.printStackTrace();
        }

        if (mUserInfo == null) {
            mMoneyTv.setVisibility(View.GONE);
            mMoneyMaskIv.setVisibility(View.VISIBLE);
            if (loginDialog != null && !loginDialog.isShowing()) {
                loginDialog.show();
            }
            return;
        } else {
            mMoneyTv.setVisibility(View.VISIBLE);
            mMoneyMaskIv.setVisibility(View.GONE);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(MessageEvent messageEvent) {
        if (messageEvent.getMessage().equals("login_success")) {
            mUserInfo = App.getApp().mUserInfo;
            try {
                mMoneyMaskIv.setVisibility(View.GONE);
                mMoneyTv.setVisibility(View.VISIBLE);
                everyDayHongBaoPresenterImp.everyDayHongBaoInfo(mUserInfo.getId(), mUserInfo.getOpenid(), PhoneUtils.getIMEI());
            } catch (SecurityException e) {
                e.printStackTrace();
            }
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

        Logger.i("every day --->" + JSON.toJSONString(tData));

        if (tData != null) {
            if (tData instanceof EveryDayHbRet) {
                if (((EveryDayHbRet) tData).getCode() == Constants.SUCCESS) {
                    if (((EveryDayHbRet) tData).getData() != null) {
                        seeVideoMoneys = ((EveryDayHbRet) tData).getData().getCashindex().split("/");
                        randomMoney();
                        mMoneyTv.setText(seeVideoMoney + "");

                        if (SPUtils.getInstance().getInt(Constants.GET_MONEY_NUM, 0) == 0) {
                            SPUtils.getInstance().put(Constants.GET_MONEY_NUM, RomUtils.randomNum());
                        }

                        mRandomNumTv.setText("已领取" + SPUtils.getInstance().getInt(Constants.GET_MONEY_NUM) + "个");
                        receiveUserListAdapter.setNewData(((EveryDayHbRet) tData).getData().receiveUserList);
                        Logger.i("recordId--->" + recordId);
                        if (!StringUtils.isEmpty(recordId) && mUserInfo != null) {
                            try {
                                taskRecordInfoPresenterImp.addHomeTaskRecord(mUserInfo.getId(), mUserInfo.getOpenid(), PhoneUtils.getIMEI(), seeVideoMoney, 1, recordId);
                            } catch (SecurityException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }

            if (tData instanceof TaskRecordInfoRet) {
                if (((TaskRecordInfoRet) tData).getCode() == Constants.SUCCESS) {
                    if (((TaskRecordInfoRet) tData).getData() != null) {
                        if (SPUtils.getInstance().getInt(Constants.GET_MONEY_NUM, 0) > 0) {
                            SPUtils.getInstance().put(Constants.GET_MONEY_NUM, SPUtils.getInstance().getInt(Constants.GET_MONEY_NUM, 0) + 1);
                        }
                        mRandomNumTv.setText("已领取" + SPUtils.getInstance().getInt(Constants.GET_MONEY_NUM) + "个");
                        ToastUtils.showLong("获得收益" + seeVideoMoney + "元");
                    }
                } else {
                    //finish();
                    Logger.i("task error--->");
                }
            }
        }
    }

    @Override
    public void loadDataError(Throwable throwable) {

    }

    public void randomMoney() {
        Logger.i("videoMoneys--->" + JSON.toJSONString(seeVideoMoneys));
        double temp = RandomUtils.nextDouble(Double.parseDouble(seeVideoMoneys[0]), Double.parseDouble(seeVideoMoneys[1]));
        BigDecimal tempBd = new BigDecimal(temp);
        seeVideoMoney = tempBd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        Logger.i("see video money value --->" + seeVideoMoney);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void loginClose() {
        finish();
    }
}
