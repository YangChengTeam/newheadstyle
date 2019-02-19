package com.feiyou.headstyle.ui.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.ui.adapter.BlackListAdapter;
import com.feiyou.headstyle.ui.base.BaseFragmentActivity;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import jp.co.cyberagent.android.gpuimage.GPUImageView;

/**
 * Created by myflying on 2018/11/23.
 */
public class TestActivity extends BaseFragmentActivity {

    @BindView(R.id.topbar)
    QMUITopBar mTopBar;

    @BindView(R.id.gpuimage)
    GPUImageView mGPUImageView;

    Bitmap currentBitmap;

    @Override
    protected int getContextViewId() {
        return R.layout.activity_test;
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
        View topSearchView = getLayoutInflater().inflate(R.layout.common_top_back, null);
        topSearchView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(48)));
        TextView titleTv = topSearchView.findViewById(R.id.tv_title);
        titleTv.setText("黑名单");

        mTopBar.setCenterView(topSearchView);

    }

    public void initData() {
        currentBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
        mGPUImageView.setImage(currentBitmap);
    }

    @Override
    public void onBackPressed() {
        popBackStack();
    }
}
