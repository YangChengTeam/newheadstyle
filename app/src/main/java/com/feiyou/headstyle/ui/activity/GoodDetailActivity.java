package com.feiyou.headstyle.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.feiyou.headstyle.App;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.base.IBaseView;
import com.feiyou.headstyle.bean.ExchangeInfoRet;
import com.feiyou.headstyle.bean.GoodDetailInfo;
import com.feiyou.headstyle.bean.GoodDetailInfoRet;
import com.feiyou.headstyle.common.Constants;
import com.feiyou.headstyle.common.GlideImageLoader;
import com.feiyou.headstyle.common.GoodImageLoader;
import com.feiyou.headstyle.presenter.ExchangeInfoPresenterImp;
import com.feiyou.headstyle.presenter.GoodDetailInfoPresenterImp;
import com.feiyou.headstyle.ui.adapter.ExchangeListAdapter;
import com.feiyou.headstyle.ui.adapter.GoodImageAdapter;
import com.feiyou.headstyle.ui.base.BaseFragmentActivity;
import com.feiyou.headstyle.ui.custom.ConfigDialog;
import com.feiyou.headstyle.ui.custom.LackDialog;
import com.feiyou.headstyle.view.GoodDetailInfoView;
import com.feiyou.headstyle.view.GoodInfoView;
import com.orhanobut.logger.Logger;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class GoodDetailActivity extends BaseFragmentActivity implements ConfigDialog.ConfigListener, IBaseView, LackDialog.LackListener {

    @BindView(R.id.topbar)
    QMUITopBar mTopBar;

    @BindView(R.id.good_image_list_view)
    RecyclerView mGoodImageListView;

    @BindView(R.id.btn_exchange)
    Button mExchangeBtn;

    TextView mGoodTitleTv;

    TextView mGoldNumTv;

    TextView mGetUserNumTv;

    TextView mExchangeRemarkTv;

    Banner mBanner;

    RecyclerView mExchangeListView;

    ImageView mBackImageView;

    GoodImageAdapter goodImageAdapter;

    ExchangeListAdapter exchangeListAdapter;

    View topView;

    ConfigDialog configDialog;

    LackDialog lackDialog;//金币不足

    GoodDetailInfoPresenterImp goodDetailInfoPresenterImp;

    ExchangeInfoPresenterImp exchangeInfoPresenterImp;

    private String gid;

    private ProgressDialog progressDialog = null;

    private double goodGoldNum;

    @Override
    protected int getContextViewId() {
        return R.layout.activity_good_detail;
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
        titleTv.setText("商品详情");

        mTopBar.setCenterView(aboutView);
        mBackImageView = aboutView.findViewById(R.id.iv_back);
        mBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在兑换");

        lackDialog = new LackDialog(this, R.style.login_dialog);
        lackDialog.setLackListener(this);
    }

    public void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && !StringUtils.isEmpty(bundle.getString("gid"))) {
            gid = bundle.getString("gid");
        }

        goodImageAdapter = new GoodImageAdapter(this, null);
        mGoodImageListView.setLayoutManager(new LinearLayoutManager(this));
        mGoodImageListView.setAdapter(goodImageAdapter);

        topView = LayoutInflater.from(this).inflate(R.layout.good_detail_top_view, null);
        mBanner = topView.findViewById(R.id.banner);

        mGoodTitleTv = topView.findViewById(R.id.tv_good_title);
        mGoldNumTv = topView.findViewById(R.id.tv_gold_num);
        mGetUserNumTv = topView.findViewById(R.id.tv_get_user_num);
        mExchangeRemarkTv = topView.findViewById(R.id.tv_exchange_remark);

        mExchangeListView = topView.findViewById(R.id.exchange_list_view);
        goodImageAdapter.addHeaderView(topView);

        exchangeListAdapter = new ExchangeListAdapter(this, null);
        mExchangeListView.setLayoutManager(new LinearLayoutManager(this));
        mExchangeListView.setAdapter(exchangeListAdapter);

        goodDetailInfoPresenterImp = new GoodDetailInfoPresenterImp(this, this);
        goodDetailInfoPresenterImp.getGoodDetail(gid, App.getApp().mUserInfo != null ? App.getApp().mUserInfo.getId() : "");
        exchangeInfoPresenterImp = new ExchangeInfoPresenterImp(this, this);
    }

    @OnClick(R.id.btn_exchange)
    void exchangeNow() {
        if (configDialog != null && !configDialog.isShowing()) {
            configDialog.show();
        }
    }

    @Override
    public void config() {

        if (App.getApp().getUserGoldNum() < goodGoldNum) {
            if (lackDialog != null && !lackDialog.isShowing()) {
                lackDialog.show();
            }
            return;
        }

        if (progressDialog != null && !progressDialog.isShowing()) {
            progressDialog.show();
        }
        exchangeInfoPresenterImp.exchangeGood(gid, App.getApp().mUserInfo != null ? App.getApp().mUserInfo.getId() : "", 1);
    }

    @Override
    public void cancel() {

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
        Logger.i("detail--->" + JSON.toJSONString(tData));

        if (tData != null) {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            if (tData instanceof GoodDetailInfoRet) {
                if (tData != null && ((GoodDetailInfoRet) tData).getCode() == Constants.SUCCESS) {
                    if (((GoodDetailInfoRet) tData).getData() != null) {

                        GoodDetailInfo goodDetailInfo = ((GoodDetailInfoRet) tData).getData();

                        if (goodDetailInfo != null) {
                            goodGoldNum = goodDetailInfo.getGoodInfo().getGoldprice();
                            configDialog = new ConfigDialog(this, R.style.login_dialog, R.mipmap.turn_profit_icon, "兑换提示", "是否确认使用" + (int) goodGoldNum + "金币兑换？");
                            configDialog.setConfigListener(this);

                            mGoodTitleTv.setText(goodDetailInfo.getGoodInfo().getGoodsname());
                            mGoldNumTv.setText(goodDetailInfo.getGoodInfo().getGoldprice() + "");
                            mGetUserNumTv.setText(goodDetailInfo.getGoodInfo().getFalsenum() + "人已0元拿");
                            mExchangeRemarkTv.setText(goodDetailInfo.getGoodInfo().getExchange());

                            if (!StringUtils.isEmpty(goodDetailInfo.getGoodInfo().getSmallimg())) {
                                String[] preImages = goodDetailInfo.getGoodInfo().getSmallimg().split(",");
                                if (preImages != null && preImages.length > 0) {
                                    //设置图片加载器
                                    List<String> bannerList = new ArrayList<>(Arrays.asList(preImages));
                                    mBanner.setImageLoader(new GoodImageLoader()).setImages(bannerList).start();
                                }
                            }

                            if (!StringUtils.isEmpty(goodDetailInfo.getGoodInfo().getDetailimg())) {
                                String[] detailImages = goodDetailInfo.getGoodInfo().getDetailimg().split(",");
                                if (detailImages != null && detailImages.length > 0) {
                                    //设置图片加载器
                                    List<String> detailImagesList = new ArrayList<>(Arrays.asList(detailImages));
                                    goodImageAdapter.setNewData(detailImagesList);
                                }
                            }

                            if (goodDetailInfo.getRecordList() != null && goodDetailInfo.getRecordList().size() > 0) {
                                exchangeListAdapter.setNewData(goodDetailInfo.getRecordList());
                            }

                            if (goodDetailInfo.getIsExchange() == 0) {
                                mExchangeBtn.setBackgroundResource(R.drawable.common_gray_bg);
                                mExchangeBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_aaa));
                                mExchangeBtn.setText("");
                                mExchangeBtn.setClickable(false);
                            }
                        }
                    }
                }
            }

            if (tData instanceof ExchangeInfoRet) {
                if (tData != null && ((ExchangeInfoRet) tData).getCode() == Constants.SUCCESS) {
                    ToastUtils.showLong("兑换成功");
                    if (((ExchangeInfoRet) tData).getData() != null && ((ExchangeInfoRet) tData).getData().size() > 0) {
                        Intent intent = new Intent(this, ExchangeDetailActivity.class);
                        //intent.putExtra("order_number", ((ExchangeInfoRet) tData).getData().get(0).getGoodsorder());
                        //intent.putExtra("order_state",((ExchangeInfoRet) tData).getData().get(0).getStatus());

                        intent.putExtra("order_item", ((ExchangeInfoRet) tData).getData().get(0));
                        startActivity(intent);
                    }
                } else {
                    ToastUtils.showLong(((ExchangeInfoRet) tData).getMsg());
                }
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
    public void onBackPressed() {
        popBackStack();
    }

    @Override
    public void lackConfig() {
        Intent intent = new Intent(this, GoldTaskActivity.class);
        startActivity(intent);
    }

    @Override
    public void lackCancel() {

    }
}
