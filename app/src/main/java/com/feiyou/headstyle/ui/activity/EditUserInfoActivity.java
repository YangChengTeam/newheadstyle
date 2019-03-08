package com.feiyou.headstyle.ui.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.PathUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feiyou.headstyle.App;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.UserInfo;
import com.feiyou.headstyle.bean.UserInfoRet;
import com.feiyou.headstyle.common.Constants;
import com.feiyou.headstyle.presenter.UserInfoPresenterImp;
import com.feiyou.headstyle.ui.adapter.CommonImageAdapter;
import com.feiyou.headstyle.ui.base.BaseFragmentActivity;
import com.feiyou.headstyle.ui.custom.Glide4Engine;
import com.feiyou.headstyle.ui.custom.GlideRoundTransform;
import com.feiyou.headstyle.utils.MyToastUtils;
import com.feiyou.headstyle.view.UserInfoView;
import com.orhanobut.logger.Logger;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by myflying on 2018/11/23.
 */
public class EditUserInfoActivity extends BaseFragmentActivity implements View.OnClickListener, UserInfoView {

    private static final int REQUEST_CODE_CHOOSE = 23;

    @BindView(R.id.topbar)
    QMUITopBar mTopBar;

    @BindView(R.id.tv_sex_label)
    TextView mSexLabelTv;

    @BindView(R.id.photo_list)
    RecyclerView mPhotoListView;

    @BindView(R.id.layout_wrapper)
    LinearLayout mWrapperLayout;

    @BindView(R.id.layout_no_photo)
    LinearLayout mNoPhotoLayout;

    @BindView(R.id.layout_photos)
    RelativeLayout mPhotosLayout;

    @BindView(R.id.tv_total_count)
    TextView mTotalCountTv;

    @BindView(R.id.et_user_nick_name)
    EditText mUserNickNameInputEt;

    @BindView(R.id.et_intro)
    EditText mIntroInputEt;

    @BindView(R.id.iv_user_head)
    ImageView mUserHeadIv;

    @BindView(R.id.tv_birthday)
    TextView mBirthdayTv;

    ImageView mBackImageView;

    BottomSheetDialog bottomSheetDialog;

    LinearLayout mBoyLayout;

    LinearLayout mGirlLayout;

    LinearLayout mCancelLayout;

    CommonImageAdapter commonImageAdapter;

    private UserInfo userInfo;

    private List<Object> photoList;

    UserInfoPresenterImp userInfoPresenterImp;

    private ProgressDialog progressDialog = null;

    @Override
    protected int getContextViewId() {
        return R.layout.activity_edit_user_info;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTopBar();
        initData();
    }

    private void initTopBar() {
        QMUIStatusBarHelper.setStatusBarLightMode(this);
        mTopBar.setTitle(getResources().getString(R.string.app_name));
        View topSearchView = getLayoutInflater().inflate(R.layout.common_top_config, null);
        topSearchView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(48)));

        TextView titleTv = topSearchView.findViewById(R.id.tv_config_title);
        TextView saveTv = topSearchView.findViewById(R.id.tv_config);
        titleTv.setText("编辑个人资料");
        saveTv.setText("保存");

        mTopBar.setCenterView(topSearchView);
        mBackImageView = topSearchView.findViewById(R.id.iv_back);
        mBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });

        saveTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (progressDialog != null && !progressDialog.isShowing()) {
                    progressDialog.show();
                }

                if (StringUtils.isEmpty(mUserNickNameInputEt.getText())) {
                    ToastUtils.showLong("请填写昵称");
                    return;
                }

                userInfo.setNickname(mUserNickNameInputEt.getText().toString());
                userInfo.setBirthday(mBirthdayTv.getText().toString());
                userInfo.setIntro(mIntroInputEt.getText().toString());
                userInfoPresenterImp.updateUserInfo(userInfo);
            }
        });

    }

    public void initData() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在保存");

        bottomSheetDialog = new BottomSheetDialog(this);
        View sexView = LayoutInflater.from(this).inflate(R.layout.view_sex_dialog, null);
        mBoyLayout = sexView.findViewById(R.id.layout_sex_boy);
        mGirlLayout = sexView.findViewById(R.id.layout_sex_girl);
        mCancelLayout = sexView.findViewById(R.id.layout_cancel);
        mBoyLayout.setOnClickListener(this);
        mGirlLayout.setOnClickListener(this);
        mCancelLayout.setOnClickListener(this);
        bottomSheetDialog.setContentView(sexView);

        commonImageAdapter = new CommonImageAdapter(this, null, 60);
        mPhotoListView.setLayoutManager(new GridLayoutManager(this, 3));
        mPhotoListView.setAdapter(commonImageAdapter);
        commonImageAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(EditUserInfoActivity.this, PhotoWallActivity.class);
                startActivity(intent);
            }
        });
        userInfoPresenterImp = new UserInfoPresenterImp(this, this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        userInfo = App.getApp().getmUserInfo();

        if (userInfo != null) {
            mUserNickNameInputEt.setText(userInfo.getNickname());
            RequestOptions options = new RequestOptions();
            options.transform(new GlideRoundTransform(this, 30));
            Glide.with(this).load(userInfo.getUserimg()).apply(options).into(mUserHeadIv);
            mIntroInputEt.setText(userInfo.getIntro());
            mBirthdayTv.setText(userInfo.getBirthday());
            mSexLabelTv.setText(userInfo.getSex() == 1 ? "男" : "女");

            if (userInfo.getImageWall() != null && userInfo.getImageWall().length > 0) {
                mNoPhotoLayout.setVisibility(View.GONE);
                mPhotosLayout.setVisibility(View.VISIBLE);

                String[] tempPhotos = userInfo.getImageWall();
                mTotalCountTv.setText(tempPhotos.length + "张");
                photoList = new ArrayList<>();
                for (int i = 0; i < tempPhotos.length; i++) {
                    photoList.add(tempPhotos[i]);
                }
                if (photoList.size() > 3) {
                    photoList = photoList.subList(0, 3);
                }

                //获取值后重新设置
                commonImageAdapter.setNewData(photoList);
            } else {
                mNoPhotoLayout.setVisibility(View.VISIBLE);
                mPhotosLayout.setVisibility(View.GONE);
            }
        }
    }

    @OnClick({R.id.layout_no_photo, R.id.layout_photos})
    void photoList() {
        Intent intent = new Intent(EditUserInfoActivity.this, PhotoWallActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.layout_sex)
    public void chooseSex() {
        if (bottomSheetDialog != null && !bottomSheetDialog.isShowing()) {
            bottomSheetDialog.show();
        }
    }

    @OnClick(R.id.layout_birthday)
    public void birthday() {
        Calendar cal = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Logger.i("year--->" + year + "---month--->" + (monthOfYear + 1) + "---day--->" + dayOfMonth);
                mBirthdayTv.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
            }
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));

        dialog.show();
    }

    @Override
    public void onBackPressed() {
        popBackStack();
    }

    @Override
    public void onClick(View view) {
        if (bottomSheetDialog != null && bottomSheetDialog.isShowing()) {
            bottomSheetDialog.dismiss();
        }
        switch (view.getId()) {
            case R.id.layout_sex_boy:
                mSexLabelTv.setText("男");
                userInfo.setSex(1);
                break;
            case R.id.layout_sex_girl:
                mSexLabelTv.setText("女");
                userInfo.setSex(2);
                break;
            case R.id.layout_cancel:
                break;
            default:
                break;
        }
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
    public void loadDataSuccess(UserInfoRet tData) {
        KeyboardUtils.hideSoftInput(this);
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

        Logger.i(JSON.toJSONString(tData));

        if (tData != null && tData.getCode() == Constants.SUCCESS) {
            MyToastUtils.showToast(this, 0, "修改成功");

            userInfo.setNickname(tData.getData().getNickname());
            userInfo.setIntro(tData.getData().getIntro());
            userInfo.setBirthday(tData.getData().getBirthday());
            userInfo.setSex(tData.getData().getSex());

            App.getApp().setmUserInfo(userInfo);
            App.getApp().setLogin(true);
            SPUtils.getInstance().put(Constants.USER_INFO, JSONObject.toJSONString(userInfo));
            finish();
        } else {
            MyToastUtils.showToast(this, 1, "修改失败");
            Logger.i(tData.getMsg() != null ? tData.getMsg() : "操作错误");
        }
    }

    @Override
    public void loadDataError(Throwable throwable) {
        KeyboardUtils.hideSoftInput(this);
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
