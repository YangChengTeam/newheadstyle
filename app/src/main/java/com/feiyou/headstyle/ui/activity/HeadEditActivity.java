package com.feiyou.headstyle.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.HeadType;
import com.feiyou.headstyle.ui.adapter.MoreHeadTypeAdapter;
import com.feiyou.headstyle.ui.base.BaseFragmentActivity;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by myflying on 2018/11/23.
 */
public class HeadEditActivity extends BaseFragmentActivity {

    @BindView(R.id.topbar)
    QMUITopBar mTopBar;

    ImageView mBackImageView;

    @BindView(R.id.layout_share)
    LinearLayout mShareLayout;

    @BindView(R.id.layout_edit)
    LinearLayout mEditLayout;

    @BindView(R.id.layout_keep)
    LinearLayout mAddKeepLayout;

    @BindView(R.id.layout_down)
    LinearLayout mDownLayout;

    TextView mTitleTv;

    TextView mConfigTv;

    private boolean isEdit;

    @Override
    protected int getContextViewId() {
        return R.layout.activity_head_edit;
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
        mTitleTv = topSearchView.findViewById(R.id.tv_config_title);
        mConfigTv = topSearchView.findViewById(R.id.tv_config);
        mTitleTv.setText("头像预览");
        mConfigTv.setVisibility(View.INVISIBLE);

        mTopBar.setCenterView(topSearchView);
        mBackImageView = topSearchView.findViewById(R.id.iv_back);
        mBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });

        //保存图片
        mConfigTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HeadEditActivity.this,HeadSaveActivity.class);
                startActivity(intent);
            }
        });
    }

    public void initData() {

    }

    @OnClick(R.id.layout_edit)
    public void editImage() {
        isEdit = true;
        if (isEdit) {
            mConfigTv.setVisibility(View.VISIBLE);
            mConfigTv.setText("保存/分享");
        }
    }

    @Override
    public void onBackPressed() {
        popBackStack();
    }
}
