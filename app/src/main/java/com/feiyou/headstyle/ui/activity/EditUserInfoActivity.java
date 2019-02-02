package com.feiyou.headstyle.ui.activity;

import android.app.DatePickerDialog;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.blankj.utilcode.util.PathUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.UserInfo;
import com.feiyou.headstyle.common.Constants;
import com.feiyou.headstyle.ui.adapter.CommonImageAdapter;
import com.feiyou.headstyle.ui.base.BaseFragmentActivity;
import com.feiyou.headstyle.ui.custom.Glide4Engine;
import com.feiyou.headstyle.ui.custom.GlideRoundTransform;
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
public class EditUserInfoActivity extends BaseFragmentActivity implements View.OnClickListener {

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

    ImageView mBackImageView;

    BottomSheetDialog bottomSheetDialog;

    LinearLayout mBoyLayout;

    LinearLayout mGirlLayout;

    LinearLayout mCancelLayout;

    CommonImageAdapter commonImageAdapter;

    private UserInfo userInfo;

    private List<Object> photoList;

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
    }

    public void initData() {
        bottomSheetDialog = new BottomSheetDialog(this);
        View sexView = LayoutInflater.from(this).inflate(R.layout.view_sex_dialog, null);
        mBoyLayout = sexView.findViewById(R.id.layout_sex_boy);
        mGirlLayout = sexView.findViewById(R.id.layout_sex_girl);
        mCancelLayout = sexView.findViewById(R.id.layout_cancel);
        mBoyLayout.setOnClickListener(this);
        mGirlLayout.setOnClickListener(this);
        mCancelLayout.setOnClickListener(this);
        bottomSheetDialog.setContentView(sexView);

        commonImageAdapter = new CommonImageAdapter(this, null,60);
        mPhotoListView.setLayoutManager(new GridLayoutManager(this, 3));
        mPhotoListView.setAdapter(commonImageAdapter);
        commonImageAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(EditUserInfoActivity.this, PhotoWallActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!StringUtils.isEmpty(SPUtils.getInstance().getString(Constants.USER_INFO))) {
            Logger.i(SPUtils.getInstance().getString(Constants.USER_INFO));
            userInfo = JSON.parseObject(SPUtils.getInstance().getString(Constants.USER_INFO), new TypeReference<UserInfo>() {
            });
        }

        if (userInfo != null && userInfo.getImageWall() != null && userInfo.getImageWall().length > 0) {
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
                break;
            case R.id.layout_sex_girl:
                mSexLabelTv.setText("女");
                break;
            case R.id.layout_cancel:
                break;
            default:
                break;
        }
    }
}
