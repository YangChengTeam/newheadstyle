package com.feiyou.headstyle.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.HeadInfo;
import com.feiyou.headstyle.bean.HeadType;
import com.feiyou.headstyle.ui.adapter.HeadInfoAdapter;
import com.feiyou.headstyle.ui.adapter.MoreHeadTypeAdapter;
import com.feiyou.headstyle.ui.base.BaseFragmentActivity;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by myflying on 2018/11/23.
 */
public class MoreHeadTypeActivity extends BaseFragmentActivity {

    @BindView(R.id.topbar)
    QMUITopBar mTopBar;

    @BindView(R.id.more_list)
    RecyclerView mMoreListView;

    ImageView mBackImageView;

    MoreHeadTypeAdapter moreHeadTypeAdapter;

    @Override
    protected int getContextViewId() {
        return R.layout.activity_more_head_type;
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
        titleTv.setText("更多头像");

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
        List<HeadType> typeList = new ArrayList<>();

        for (int m = 0; m < 60; m++) {
            typeList.add(new HeadType());
        }

        moreHeadTypeAdapter = new MoreHeadTypeAdapter(this, typeList);
        mMoreListView.setLayoutManager(new LinearLayoutManager(this));
        mMoreListView.setAdapter(moreHeadTypeAdapter);
    }

    @Override
    public void onBackPressed() {
        popBackStack();
    }
}
