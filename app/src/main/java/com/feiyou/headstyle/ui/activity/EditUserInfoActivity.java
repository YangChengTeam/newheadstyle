package com.feiyou.headstyle.ui.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.PathUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feiyou.headstyle.R;
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

    ImageView mBackImageView;

    BottomSheetDialog bottomSheetDialog;

    LinearLayout mBoyLayout;

    LinearLayout mGirlLayout;

    LinearLayout mCancelLayout;

    CommonImageAdapter commonImageAdapter;

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
        List<Object> list = new ArrayList<>();
        list.add(R.mipmap.add_my_photo);

        commonImageAdapter = new CommonImageAdapter(this, list);
        mPhotoListView.setLayoutManager(new GridLayoutManager(this, 3));
        mPhotoListView.setAdapter(commonImageAdapter);
        commonImageAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Matisse.from(EditUserInfoActivity.this)
                        .choose(MimeType.ofImage())
                        .countable(true)
                        .maxSelectable(5)
                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                        .thumbnailScale(0.85f)
                        .imageEngine(new Glide4Engine())
                        .forResult(REQUEST_CODE_CHOOSE);
            }
        });
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {

                case REQUEST_CODE_CHOOSE:
                    Logger.i(JSONObject.toJSONString(Matisse.obtainPathResult(data)));
                    if (Matisse.obtainResult(data) != null && Matisse.obtainResult(data).size() > 0) {
                        commonImageAdapter.addData(Matisse.obtainPathResult(data));
                    }
                    break;

            }
        }

    }
}
