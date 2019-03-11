package com.feiyou.headstyle.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.PathUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.feiyou.headstyle.App;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.ForecastInfoRet;
import com.feiyou.headstyle.bean.ResultInfo;
import com.feiyou.headstyle.bean.StarPosterRet;
import com.feiyou.headstyle.bean.UserInfo;
import com.feiyou.headstyle.common.Constants;
import com.feiyou.headstyle.presenter.ForecastPresenterImp;
import com.feiyou.headstyle.presenter.StarPosterPresenterImp;
import com.feiyou.headstyle.ui.adapter.ForecastListAdapter;
import com.feiyou.headstyle.ui.base.BaseFragmentActivity;
import com.feiyou.headstyle.utils.StatusBarUtil;
import com.feiyou.headstyle.view.ForecastView;
import com.orhanobut.logger.Logger;
import com.willy.ratingbar.ScaleRatingBar;

import java.io.File;
import java.io.FileNotFoundException;

import butterknife.BindView;
import butterknife.OnClick;

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

    @BindView(R.id.tv_star_name)
    TextView mStarNameTv;

    @BindView(R.id.tv_star_date)
    TextView mStarDateTv;

    ForecastListAdapter forecastListAdapter;

    private String[] starName = {"白羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "摩羯座", "水瓶座", "双鱼座"};

    private String[] starDate = {"3.21~4.19", "4.20~5.20", "5.21~6.21", "6.22~7.22", "7.23~8.22", "8.23~9.22", "9.23~10.23", "10.24~11.22", "11.23~12.21", "12.22~1.19", "1.20~2.18", "2.19~3.20"};

    private Integer[] starImage = {R.mipmap.star1, R.mipmap.star2, R.mipmap.star3, R.mipmap.star4, R.mipmap.star5, R.mipmap.star6, R.mipmap.star7, R.mipmap.star8, R.mipmap.star9, R.mipmap.star10, R.mipmap.star11, R.mipmap.star12};

    private ForecastPresenterImp forecastPresenterImp;

    private StarPosterPresenterImp starPosterPresenterImp;

    private int starIndex;

    private String uniqid;

    private UserInfo userInfo;

    private ProgressDialog progressDialog = null;

    private String filePath;

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


        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.getInt("star_index") > -1) {
            starIndex = bundle.getInt("star_index");
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在保存");

        userInfo = App.getApp().getmUserInfo();

        mStarNameTv.setText(starName[starIndex]);
        mStarDateTv.setText(starDate[starIndex]);

        forecastPresenterImp = new ForecastPresenterImp(this, this);
        starPosterPresenterImp = new StarPosterPresenterImp(this, this);

        forecastListAdapter = new ForecastListAdapter(this, null);
        mForeCastListView.setLayoutManager(new LinearLayoutManager(this));
        mForeCastListView.setAdapter(forecastListAdapter);
        mForeCastListView.setNestedScrollingEnabled(false);//禁止滑动

        forecastPresenterImp.getForecastData(starName[starIndex], "today");
    }

    @OnClick(R.id.layout_save_poster)
    void savePoster() {
        if (StringUtils.isEmpty(uniqid) || userInfo == null) {
            ToastUtils.showLong("无法生存，请稍后重试");
            return;
        }

        if (progressDialog != null && !progressDialog.isShowing()) {
            progressDialog.show();
        }

        starPosterPresenterImp.createPoster(userInfo.getNickname(), userInfo.getUserimg(), uniqid);
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
    public void loadDataSuccess(ResultInfo tData) {

        Logger.i(JSONObject.toJSONString(tData));

        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

        if (tData != null && tData.getCode() == Constants.SUCCESS) {

            if (tData instanceof ForecastInfoRet) {
                if (((ForecastInfoRet) tData).getData() != null) {
                    forecastListAdapter.setNewData(((ForecastInfoRet) tData).getData().getList());

                    ratingBar1.setRating(changeRating(((ForecastInfoRet) tData).getData().getNumberInfo().getZhzs()));
                    ratingBar2.setRating(changeRating(((ForecastInfoRet) tData).getData().getNumberInfo().getAqzs()));
                    ratingBar3.setRating(changeRating(((ForecastInfoRet) tData).getData().getNumberInfo().getSyzs()));
                    ratingBar4.setRating(changeRating(((ForecastInfoRet) tData).getData().getNumberInfo().getCfzs()));

                    mSpeedStarTv.setText(((ForecastInfoRet) tData).getData().getNumberInfo().getSpxz());
                    mGoodColorTv.setText(((ForecastInfoRet) tData).getData().getNumberInfo().getXyys());
                    mHealthNumTv.setText(((ForecastInfoRet) tData).getData().getNumberInfo().getJkzs() + "%");
                    mGoodNumTv.setText(((ForecastInfoRet) tData).getData().getNumberInfo().getXysz() + "");

                    uniqid = ((ForecastInfoRet) tData).getData().getUniqid();
                }
            }
            if (tData instanceof StarPosterRet) {
                downImage(((StarPosterRet) tData).getData().getImage());
            }
        } else {
            if (tData instanceof StarPosterRet) {
                ToastUtils.showLong(StringUtils.isEmpty(tData.getMsg()) ? "生成错误" : tData.getMsg());
            }
        }
    }

    void downImage(String imgUrl) {
//        if (StringUtils.isEmpty(imgUrl)) {
//            ToastUtils.showLong("下载失败，请稍后重试");
//            return;
//        }
        Glide.with(this).asBitmap().load(imgUrl).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                //加载成功，resource为加载到的bitmap
                filePath = PathUtils.getExternalPicturesPath() + File.separator + TimeUtils.getNowMills() + ".jpg";
                Logger.i("show page save path --->" + filePath);
                boolean isSave = ImageUtils.save(resource, filePath, Bitmap.CompressFormat.JPEG);
                if (isSave) {
                    saveImageToGallery();
                }
            }

            @Override
            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                super.onLoadFailed(errorDrawable);
                ToastUtils.showLong("生成失败");
            }

        });

    }

    // 其次把文件插入到系统图库
    public boolean saveImageToGallery() {
        boolean flag = true;
        try {
            if (!StringUtils.isEmpty(filePath)) {
                MediaStore.Images.Media.insertImage(getContentResolver(),
                        filePath, filePath.substring(filePath.lastIndexOf("/") + 1, filePath.length()), null);

                MediaScannerConnection.scanFile(StarDetailActivity.this, new String[]{filePath}, null, null);
                // 最后通知图库更新
                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + filePath)));
                ToastUtils.showLong("已保存到图库");
            } else {
                flag = false;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            flag = false;
        }
        return flag;
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
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
