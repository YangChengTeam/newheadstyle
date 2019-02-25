package com.feiyou.headstyle.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.SizeUtils;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.ForecastInfoRet;
import com.feiyou.headstyle.common.Constants;
import com.feiyou.headstyle.presenter.ForecastPresenterImp;
import com.feiyou.headstyle.ui.adapter.ForecastListAdapter;
import com.feiyou.headstyle.ui.base.BaseFragmentActivity;
import com.feiyou.headstyle.utils.StatusBarUtil;
import com.feiyou.headstyle.view.ForecastView;
import com.orhanobut.logger.Logger;
import com.willy.ratingbar.ScaleRatingBar;

import butterknife.BindView;

/**
 * Created by myflying on 2018/11/23.
 */
public class StarDetailActivity extends BaseFragmentActivity implements ForecastView {

    @BindView(R.id.iv_back)
    ImageView mBackImageView;

    @BindView(R.id.forecast_list)
    RecyclerView mForeCastListView;

    @BindView(R.id.layout_star_top)
    RelativeLayout mTopLayout;

    @BindView(R.id.simpleRatingBar1)
    ScaleRatingBar ratingBar1;

    @BindView(R.id.simpleRatingBar2)
    ScaleRatingBar ratingBar2;

    @BindView(R.id.simpleRatingBar3)
    ScaleRatingBar ratingBar3;

    @BindView(R.id.simpleRatingBar4)
    ScaleRatingBar ratingBar4;

    @BindView(R.id.tv_speed_star)
    TextView mSpeedStarTv;

    @BindView(R.id.tv_good_color)
    TextView mGoodColorTv;

    @BindView(R.id.tv_health_num)
    TextView mHealthNumTv;

    @BindView(R.id.tv_good_num)
    TextView mGoodNumTv;

    ForecastListAdapter forecastListAdapter;

    private String[] starName = {"白羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "摩羯座", "水瓶座", "双鱼座"};

    private String[] starDate = {"3.21~4.19", "4.20~5.20", "5.21~6.21", "6.22~7.22", "7.23~8.22", "8.23~9.22", "9.23~10.23", "10.24~11.22", "11.23~12.21", "12.22~1.19", "1.20~2.18", "2.19~3.20"};

    private Integer[] starImage = {R.mipmap.star1, R.mipmap.star2, R.mipmap.star3, R.mipmap.star4, R.mipmap.star5, R.mipmap.star6, R.mipmap.star7, R.mipmap.star8, R.mipmap.star9, R.mipmap.star10, R.mipmap.star11, R.mipmap.star12};

    private ForecastPresenterImp forecastPresenterImp;

    @Override
    protected int getContextViewId() {
        return R.layout.activity_star_detail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTopBar();
        initData();
    }

    private void initTopBar() {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(48));
        params.setMargins(0, StatusBarUtil.getStatusBarHeight(this), 0, 0);
        mTopLayout.setLayoutParams(params);

        mBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });
    }

    public void initData() {
        forecastPresenterImp = new ForecastPresenterImp(this, this);

        forecastListAdapter = new ForecastListAdapter(this, null);
        mForeCastListView.setLayoutManager(new LinearLayoutManager(this));
        mForeCastListView.setAdapter(forecastListAdapter);
        mForeCastListView.setNestedScrollingEnabled(false);//禁止滑动

        forecastPresenterImp.getForecastData("处女座", "today");
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
    public void loadDataSuccess(ForecastInfoRet tData) {
        Logger.i(JSONObject.toJSONString(tData));
        if (tData != null && tData.getCode() == Constants.SUCCESS) {
            if (tData.getData() != null) {
                forecastListAdapter.setNewData(tData.getData().getList());

                ratingBar1.setRating(changeRating(tData.getData().getNumberInfo().getZhzs()));
                ratingBar2.setRating(changeRating(tData.getData().getNumberInfo().getAqzs()));
                ratingBar3.setRating(changeRating(tData.getData().getNumberInfo().getSyzs()));
                ratingBar4.setRating(changeRating(tData.getData().getNumberInfo().getCfzs()));

                mSpeedStarTv.setText(tData.getData().getNumberInfo().getSpxz());
                mGoodColorTv.setText(tData.getData().getNumberInfo().getXyys());
                mHealthNumTv.setText(tData.getData().getNumberInfo().getJkzs() + "%");
                mGoodNumTv.setText(tData.getData().getNumberInfo().getXysz() + "");
            }
        }
    }

    public int changeRating(int num) {

        int result = 0;
        if (num % 20 > 0) {
            result = num / 20 + 1;
        } else {
            result = num / 20;
        }

        return result;
    }

    @Override
    public void loadDataError(Throwable throwable) {

    }
}
