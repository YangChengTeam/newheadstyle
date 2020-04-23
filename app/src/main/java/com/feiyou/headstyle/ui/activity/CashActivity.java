package com.feiyou.headstyle.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.PhoneUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feiyou.headstyle.App;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.base.IBaseView;
import com.feiyou.headstyle.bean.CashInfoRet;
import com.feiyou.headstyle.bean.CashMoneyInfoRet;
import com.feiyou.headstyle.bean.PlayGameInfo;
import com.feiyou.headstyle.bean.SeeVideoInfo;
import com.feiyou.headstyle.bean.UserInfo;
import com.feiyou.headstyle.bean.UserInfoRet;
import com.feiyou.headstyle.common.Constants;
import com.feiyou.headstyle.presenter.CashInfoPresenterImp;
import com.feiyou.headstyle.presenter.CashMoneyInfoPresenterImp;
import com.feiyou.headstyle.presenter.UserInfoPresenterImp;
import com.feiyou.headstyle.ui.adapter.PriceListAdapter;
import com.feiyou.headstyle.ui.base.BaseFragmentActivity;
import com.feiyou.headstyle.ui.custom.ConfigDialog;
import com.feiyou.headstyle.ui.custom.LackDialog;
import com.feiyou.headstyle.ui.custom.LoginDialog;
import com.orhanobut.logger.Logger;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareConfig;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

public class CashActivity extends BaseFragmentActivity implements ConfigDialog.ConfigListener, IBaseView, LackDialog.LackListener {

    @BindView(R.id.topbar)
    QMUITopBar mTopBar;

    @BindView(R.id.price_list_view)
    RecyclerView mPriceListView;

    @BindView(R.id.layout_weixin_pay)
    LinearLayout mWixinPayBtn;

    @BindView(R.id.tv_profit_money)
    TextView mProfitMoneyTv;

    @BindView(R.id.tv_cash_weixin)
    TextView mCashWeixinTv;

    @BindView(R.id.tv_cash_remark)
    TextView mCashRemarkTv;

    @BindView(R.id.tv_change_number)
    TextView mChangeNumberTv;

    PriceListAdapter priceListAdapter;

    ImageView mBackImageView;

    ConfigDialog configDialog;

    ConfigDialog bingPhoneDialog;

    public int clickStep = 1;

    private UMShareAPI mShareAPI = null;

    private ProgressDialog progressDialog = null;

    private CashMoneyInfoPresenterImp cashMoneyInfoPresenterImp;

    private CashInfoPresenterImp cashInfoPresenterImp;

    private String openId;

    private String txNickName;

    private double myProfitMoney;

    private int lastSelectIndex;

    private int realCashMoney;

    private int cashType = 2;

    UserInfoPresenterImp userInfoPresenterImp;

    private UserInfo mUserInfo;

    private boolean quickTxIsClose;

    LackDialog lackDialog;//收益不足

    private PlayGameInfo playGameInfo;

    private SeeVideoInfo gameSeeVideoInfo;

    private String tempUserId = "";

    LoginDialog loginDialog;

    @Override
    protected int getContextViewId() {
        return R.layout.activity_cash;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTopBar();
        initData();
    }

    private void initTopBar() {
        QMUIStatusBarHelper.setStatusBarLightMode(this);
        View aboutView = getLayoutInflater().inflate(R.layout.common_top_back, null);
        aboutView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(48)));
        TextView titleTv = aboutView.findViewById(R.id.tv_title);
        titleTv.setText("提现");

        mTopBar.setCenterView(aboutView);
        mBackImageView = aboutView.findViewById(R.id.iv_back);
        mBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在绑定");

        configDialog = new ConfigDialog(this, R.style.login_dialog, 1, "提现提示", "是否确认提现？");
        configDialog.setConfigListener(this);

        bingPhoneDialog = new ConfigDialog(this, R.style.login_dialog, 1, "绑定手机提示", "收益提现需要绑定手机号码");
        bingPhoneDialog.setConfigListener(this);
    }

    public void initData() {
        playGameInfo = (PlayGameInfo) getIntent().getSerializableExtra("play_game_info");
        gameSeeVideoInfo = (SeeVideoInfo) getIntent().getSerializableExtra("game_see_video");

        mUserInfo = App.getApp().mUserInfo;
        mShareAPI = UMShareAPI.get(this);

        loginDialog = new LoginDialog(this, R.style.login_dialog);

        lackDialog = new LackDialog(this, R.style.login_dialog);
        lackDialog.setLackListener(this);

        priceListAdapter = new PriceListAdapter(this, null);
        mPriceListView.setLayoutManager(new GridLayoutManager(this, 3));
        mPriceListView.setAdapter(priceListAdapter);

        userInfoPresenterImp = new UserInfoPresenterImp(this, this);
        cashInfoPresenterImp = new CashInfoPresenterImp(this, this);
        cashMoneyInfoPresenterImp = new CashMoneyInfoPresenterImp(this, this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null && !StringUtils.isEmpty(bundle.getString("temp_user_id"))) {
            tempUserId = bundle.getString("temp_user_id");
        }

        if (mUserInfo != null) {
            tempUserId = mUserInfo.getId();
        }

        try {
            cashMoneyInfoPresenterImp.cashMoneyList(tempUserId, mUserInfo != null ? mUserInfo.getOpenid() : "", App.androidId);
        } catch (SecurityException e) {
            e.printStackTrace();
        }

        priceListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (lastSelectIndex == position) {
                    return;
                }

                if (quickTxIsClose && position == 0) {
                    ToastUtils.showLong("您的极速提现体验次数已用完！");
                    return;
                }

                realCashMoney = priceListAdapter.getData().get(position).getAmount();

                cashType = position > 0 ? 2 : 1;

                priceListAdapter.getData().get(position).setSelected(true);
                priceListAdapter.getData().get(lastSelectIndex).setSelected(false);
                lastSelectIndex = position;

                priceListAdapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        mUserInfo = App.getApp().mUserInfo;
    }

    @OnClick({R.id.tv_cash_weixin, R.id.tv_change_number})
    void bindWeiXin() {

        if (!App.getApp().isLogin) {
            if (loginDialog != null && !loginDialog.isShowing()) {
                loginDialog.show();
            }
            return;
        }

        if (progressDialog != null && !progressDialog.isShowing()) {
            progressDialog.show();
        }

        mUserInfo = App.getApp().mUserInfo;

        UMShareConfig config = new UMShareConfig();
        config.isNeedAuthOnGetUserInfo(true);
        mShareAPI.setShareConfig(config);

        mShareAPI.getPlatformInfo(this, SHARE_MEDIA.WEIXIN, authListener);
    }

    @OnClick(R.id.btn_cash_record)
    void cashRecord() {
        Intent intent = new Intent(this, CashRecordActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_cash_now)
    void cashNow() {

        if (!App.getApp().isLogin) {
            if (loginDialog != null && !loginDialog.isShowing()) {
                loginDialog.show();
            }
            return;
        }

        if (StringUtils.isEmpty(openId)) {
            ToastUtils.showLong("请先绑定微信账号后提现");
            return;
        }

        if (myProfitMoney < realCashMoney) {
            if (lackDialog != null && !lackDialog.isShowing()) {
                lackDialog.show();
                lackDialog.setLackInfo("收益提示", "你的收益不足，请先赚取收益");
            }
            return;
        }

        if (configDialog != null && !configDialog.isShowing()) {
            configDialog.show();
            clickStep = 1;
        }
    }

    @Override
    public void config() {
        try {
            if (progressDialog != null && !progressDialog.isShowing()) {
                progressDialog.setMessage("正在提现");
                progressDialog.show();
            }
            cashInfoPresenterImp.startCash(mUserInfo != null ? mUserInfo.getId() : "", openId, realCashMoney, cashType, App.androidId);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void cancel() {

    }

    @OnClick(R.id.layout_alipay_pay)
    void alipayPay() {
        ToastUtils.showLong("暂未开放，敬请期待!");
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

            if (data != null) {
                openId = data.get("openid");
                txNickName = data.get("name");
                Logger.i("unionid--->" + data.get("uid") + "---" + DeviceUtils.getAndroidID() + "---openid--->" + data.get("openid"));

                userInfoPresenterImp.updateTxOpenId(mUserInfo != null ? mUserInfo.getId() : "", openId, txNickName);
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
            Toasty.normal(CashActivity.this, "授权失败").show();
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }

        /**
         * @desc 授权取消的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         */
        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Toasty.normal(CashActivity.this, "授权取消").show();
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    };

    @Override
    public void onBackPressed() {
        popBackStack();
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
    public void loadDataSuccess(Object tData) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

        if (tData != null) {
            Logger.i("cash bind data--->" + JSON.toJSONString(tData));
            if (tData instanceof UserInfoRet) {
                if (((UserInfoRet) tData).getCode() == Constants.SUCCESS) {
                    ToastUtils.showLong("绑定成功");
                    mCashWeixinTv.setText(txNickName);
                    mChangeNumberTv.setText(StringUtils.isEmpty(txNickName) ? "立即绑定" : "切换账号");
                }
            }

            if (tData instanceof CashMoneyInfoRet) {
                if (((CashMoneyInfoRet) tData).getCode() == Constants.SUCCESS && ((CashMoneyInfoRet) tData).getData() != null) {
                    if (((CashMoneyInfoRet) tData).getData().getCashlist() != null && ((CashMoneyInfoRet) tData).getData().getCashlist().size() > 0) {
                        priceListAdapter.setNewData(((CashMoneyInfoRet) tData).getData().getCashlist());
                    }

                    myProfitMoney = ((CashMoneyInfoRet) tData).getData().getCash();
                    mProfitMoneyTv.setText(myProfitMoney + "");

                    openId = ((CashMoneyInfoRet) tData).getData().getTxopenid();
                    txNickName = ((CashMoneyInfoRet) tData).getData().getTxnickname();
                    mCashWeixinTv.setText(StringUtils.isEmpty(txNickName) ? "绑定微信后可直接提现" : txNickName);
                    mChangeNumberTv.setText(StringUtils.isEmpty(txNickName) ? "立即绑定" : "切换账号");

                    mCashRemarkTv.setText(Html.fromHtml(((CashMoneyInfoRet) tData).getData().getContent()));

                    //极速提现关闭
                    if (((CashMoneyInfoRet) tData).getData().getJstx() == 1) {
                        lastSelectIndex = 1;
                        quickTxIsClose = true;
                        cashType = 2;//默认设置为普通提现
                        priceListAdapter.setQuickTx(true);
                    } else {
                        lastSelectIndex = 0;
                        cashType = 1;
                        quickTxIsClose = false;
                        priceListAdapter.setQuickTx(false);
                    }

                    //默认的金额
                    realCashMoney = priceListAdapter.getData().get(lastSelectIndex).getAmount();
                    priceListAdapter.getData().get(lastSelectIndex).setSelected(true);
                }
            }

            if (tData instanceof CashInfoRet) {
                Logger.i("cash result" + JSON.toJSONString(tData));
                if (((CashInfoRet) tData).getCode() == Constants.SUCCESS) {
                    ToastUtils.showLong("提现已申请");
                    try {
                        cashMoneyInfoPresenterImp.cashMoneyList(mUserInfo != null ? mUserInfo.getId() : "", mUserInfo != null ? mUserInfo.getOpenid() : "", App.androidId);
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    }
                } else {
                    ToastUtils.showLong("提现失败");
                }
            }
        } else {
            if (tData instanceof CashInfoRet) {
                ToastUtils.showLong("提现失败");
            }
        }
    }

    @Override
    public void loadDataError(Throwable throwable) {

    }

    @Override
    public void lackConfig() {
        Intent intent = new Intent(this, GameTestActivity.class);
        intent.putExtra("play_game_info", playGameInfo);
        intent.putExtra("game_see_video", gameSeeVideoInfo);
        startActivity(intent);
    }

    @Override
    public void lackCancel() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}
