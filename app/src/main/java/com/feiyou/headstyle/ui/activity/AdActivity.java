package com.feiyou.headstyle.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.ui.base.BaseFragmentActivity;
import com.just.agentweb.AgentWeb;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;

import butterknife.BindView;

/**
 * Created by myflying on 2018/11/23.
 */


public class AdActivity extends BaseFragmentActivity {

    @BindView(R.id.topbar)
    QMUITopBar mTopBar;

    ImageView mBackImageView;

    @BindView(R.id.layout_ad)
    LinearLayout mAdLayout;

    TextView titleTv;

    AgentWeb mAgentWeb;

    @Override
    protected int getContextViewId() {
        return R.layout.activity_ad;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTopBar();
        initData();
    }

    private void initTopBar() {
        QMUIStatusBarHelper.setStatusBarLightMode(this);
        View topSearchView = getLayoutInflater().inflate(R.layout.common_top_back, null);
        topSearchView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(48)));
        titleTv = topSearchView.findViewById(R.id.tv_title);

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
        String openUrl = "http://gx.qqtn.com";
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.getString("open_url") != null) {
            openUrl = bundle.getString("open_url");
        }

        if (bundle != null && bundle.getString("ad_title") != null) {
            String title = bundle.getString("ad_title");
            titleTv.setText(title);
        } else {
            titleTv.setText("精选推荐");
        }

        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(mAdLayout, new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()
                .createAgentWeb()
                .ready()
                .go(openUrl);
    }

    @Override
    public void onBackPressed() {
        popBackStack();
    }
}
