package com.feiyou.headstyle.ui.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.BindAccountInfoRet;
import com.feiyou.headstyle.bean.ExchangeInfo;
import com.feiyou.headstyle.common.Constants;
import com.feiyou.headstyle.presenter.BindAccountPresenterImp;
import com.feiyou.headstyle.ui.base.BaseFragmentActivity;
import com.feiyou.headstyle.ui.custom.ChargeDialog;
import com.feiyou.headstyle.ui.custom.ConfigDialog;
import com.feiyou.headstyle.view.BindAccountInfoView;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.youth.banner.Banner;

import butterknife.BindView;
import butterknife.OnClick;

public class ExchangeDetailActivity extends BaseFragmentActivity implements BindAccountInfoView, ChargeDialog.BindListener {

    @BindView(R.id.topbar)
    QMUITopBar mTopBar;

    @BindView(R.id.layout_exchange_state)
    LinearLayout mExchangeStateLayout;

    @BindView(R.id.tv_exchange_state)
    TextView mExchangeStateTv;

    @BindView(R.id.btn_use)
    Button mExchangeBtn;

    @BindView(R.id.iv_good_cover)
    ImageView mGoodCoverIv;

    @BindView(R.id.tv_good_name)
    TextView mGoodNameTv;

    @BindView(R.id.tv_gold_num)
    TextView mGoodNumTv;

    @BindView(R.id.tv_order_number)
    TextView mOrderNumberTv;

    @BindView(R.id.tv_order_date)
    TextView mOrderDateTv;

    @BindView(R.id.tv_order_account)
    TextView mOrderAccountTv;

    @BindView(R.id.tv_order_remark)
    TextView mOrderRemarkTv;

    ImageView mBackImageView;

    ChargeDialog chargeDialog;

    BindAccountPresenterImp bindAccountPresenterImp;

    private ProgressDialog progressDialog = null;

    private String orderNumber;

    private int orderState;

    private ExchangeInfo exchangeInfo;

    @Override
    protected int getContextViewId() {
        return R.layout.activity_exchange_detail;
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
        titleTv.setText("兑换详情");

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

        chargeDialog = new ChargeDialog(this, R.style.login_dialog);
        chargeDialog.setBindListener(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在绑定");

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            exchangeInfo = (ExchangeInfo) bundle.getSerializable("order_item");

            if (exchangeInfo != null) {
                orderNumber = exchangeInfo.getGoodsorder();
                orderState = exchangeInfo.getStatus();

                mGoodNameTv.setText(exchangeInfo.getGoodsname());
                mGoodNumTv.setText(StringUtils.isEmpty(exchangeInfo.getGoldprice()) ? "0" : exchangeInfo.getGoldprice() + "");
                mOrderNumberTv.setText(exchangeInfo.getGoodsorder());
                mOrderDateTv.setText(TimeUtils.millis2String(exchangeInfo.getGoodstime() * 1000));
                mOrderAccountTv.setText(exchangeInfo.getAccount() + "");

                String coverUrl = null;
                if (!StringUtils.isEmpty(exchangeInfo.getSmallimg())) {
                    String[] preImages = exchangeInfo.getSmallimg().split(",");
                    if (preImages != null && preImages.length > 0) {
                        coverUrl = preImages[0];
                    }
                }
                Glide.with(this).load(coverUrl).into(mGoodCoverIv);
            }
        }

        switch (orderState) {
            case 0:
                mExchangeStateLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.exchange_success_color));
                mExchangeBtn.setBackgroundResource(R.drawable.use_now_bg);
                mExchangeBtn.setText("立即使用");
                mExchangeStateTv.setText("兑换成功");
                break;
            case 1:
                mExchangeStateLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.wait_charge_color));
                mExchangeBtn.setBackgroundResource(R.drawable.wait_charge_bg);
                mExchangeBtn.setText("提醒发货");
                mExchangeStateTv.setText("待充值");
                break;
            case 2:
                mExchangeStateLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_aaa));
                mExchangeBtn.setBackgroundResource(R.drawable.common_gray_bg);
                mExchangeBtn.setText("已到账");
                mExchangeStateTv.setText("已到账");
                break;
            case 99:
                mExchangeStateLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.overdue_color));
                mExchangeBtn.setBackgroundResource(R.drawable.overdue_bg);
                mExchangeBtn.setText("已过期");
                mExchangeStateTv.setText("已过期");
                break;
            default:
                break;
        }

        bindAccountPresenterImp = new BindAccountPresenterImp(this, this);
    }

    @OnClick(R.id.btn_use)
    void useNow() {
        if (orderState == 0) {
            if (chargeDialog != null && !chargeDialog.isShowing()) {
                chargeDialog.show();
            }
        }
        if (orderState == 1) {
            ToastUtils.showLong("已发送提醒");
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
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void loadDataSuccess(BindAccountInfoRet tData) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        if (tData != null && tData.getCode() == Constants.SUCCESS) {
            if (tData.getData() != null) {
                ToastUtils.showLong("绑定成功");
            }
        }
    }

    @Override
    public void loadDataError(Throwable throwable) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void bindAccount(String account) {
        if (progressDialog != null && !progressDialog.isShowing()) {
            progressDialog.show();
        }

        bindAccountPresenterImp.bindAccount("11", account, orderNumber);
    }

    @Override
    public void cancelBind() {

    }
}
