package com.feiyou.headstyle.ui.activity;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ResourceUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.ui.base.BaseFragmentActivity;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;

import butterknife.BindView;

public class PrivaryActivity extends BaseFragmentActivity {

    @BindView(R.id.topbar)
    QMUITopBar mTopBar;

    ImageView mBackImageView;

    @BindView(R.id.tv_about_privary)
    TextView mAboutPrivaryTv;

    private int showType =1;

    @Override
    protected int getContextViewId() {
        return R.layout.activity_privary;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTopBar();
        initData();
    }

    private void initTopBar() {
        showType = getIntent().getExtras().getInt("show_type",1);

        QMUIStatusBarHelper.setStatusBarLightMode(this);
        mTopBar.setTitle(showType == 1 ? "用户协议":"隐私政策");
        View topSearchView = getLayoutInflater().inflate(R.layout.common_top_back, null);
        topSearchView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(48)));
        TextView titleTv = topSearchView.findViewById(R.id.tv_title);
        titleTv.setText(showType == 1 ? "用户协议":"隐私政策");

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
        String temp = ResourceUtils.readAssets2String(showType == 1 ? "xieyi.txt":"privary.txt");
        mAboutPrivaryTv.setText(Html.fromHtml(temp));
    }

    @Override
    public void onBackPressed() {
        popBackStack();
    }
}
