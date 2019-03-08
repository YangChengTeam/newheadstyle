package com.feiyou.headstyle.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.ui.base.BaseFragmentActivity;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by myflying on 2018/11/23.
 */
public class HeadSaveActivity extends BaseFragmentActivity {

    @BindView(R.id.topbar)
    QMUITopBar mTopBar;

    ImageView mBackImageView;

    TextView mTitleTv;

    TextView mConfigTv;

    BottomSheetDialog bottomSheetDialog;

    ImageView mCloseImageView;

    @BindView(R.id.iv_result)
    ImageView mResultImageView;

    private String tempFilePath;

    @Override
    protected int getContextViewId() {
        return R.layout.activity_head_save;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTopBar();
        initData();
    }

    private void initTopBar() {
        QMUIStatusBarHelper.setStatusBarLightMode(this);

        View topSaveView = getLayoutInflater().inflate(R.layout.common_top_config, null);
        topSaveView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(48)));
        mTitleTv = topSaveView.findViewById(R.id.tv_config_title);
        mConfigTv = topSaveView.findViewById(R.id.tv_config);
        mTitleTv.setText("已保存");
        mConfigTv.setVisibility(View.INVISIBLE);

        mTopBar.setCenterView(topSaveView);
        mBackImageView = topSaveView.findViewById(R.id.iv_back);
        mBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });

    }

    public void initData() {
        ToastUtils.showLong("已保存到图库");

        Bundle bundle = getIntent().getExtras();
        if (bundle != null && !StringUtils.isEmpty(bundle.getString("file_path"))) {
            tempFilePath = bundle.getString("file_path");
            Glide.with(this).load(new File(tempFilePath)).into(mResultImageView);
        }

        bottomSheetDialog = new BottomSheetDialog(this);
        View shareView = LayoutInflater.from(this).inflate(R.layout.share_dialog_view, null);
        mCloseImageView = shareView.findViewById(R.id.iv_close_share);
        bottomSheetDialog.setContentView(shareView);

        mCloseImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bottomSheetDialog != null && bottomSheetDialog.isShowing()) {
                    bottomSheetDialog.dismiss();
                }
            }
        });

        bottomSheetDialog.show();
    }

    @OnClick(R.id.layout_setting)
    void settingHead() {

    }

    @OnClick(R.id.layout_home)
    void home() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.layout_add_note)
    void addNote() {
        Intent intent = new Intent(this, PushNoteActivity.class);
        intent.putExtra("file_path", tempFilePath);
        startActivity(intent);
    }

    @OnClick(R.id.btn_share)
    public void openShareDialog() {
        if (bottomSheetDialog != null && !bottomSheetDialog.isShowing()) {
            bottomSheetDialog.show();
        }
    }

    @Override
    public void onBackPressed() {
        popBackStack();
    }
}
