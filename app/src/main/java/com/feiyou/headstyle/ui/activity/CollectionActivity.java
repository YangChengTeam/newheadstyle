package com.feiyou.headstyle.ui.activity;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.SizeUtils;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.HeadInfo;
import com.feiyou.headstyle.ui.adapter.HeadInfoAdapter;
import com.feiyou.headstyle.ui.base.BaseFragmentActivity;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by myflying on 2018/11/23.
 */
public class CollectionActivity extends BaseFragmentActivity {

    @BindView(R.id.layout_top_back)
    RelativeLayout mTopBackLayout;

    @BindView(R.id.collection_list)
    RecyclerView mCollectionListView;

    HeadInfoAdapter headInfoAdapter;

    @Override
    protected int getContextViewId() {
        return R.layout.activity_collection;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        QMUIStatusBarHelper.translucent(this);

        List<HeadInfo> headInfoList = new ArrayList<>();

        for (int m = 0; m < 60; m++) {
            headInfoList.add(new HeadInfo());
        }

        headInfoAdapter = new HeadInfoAdapter(this, headInfoList);
        mCollectionListView.setLayoutManager(new GridLayoutManager(this, 3));
        mCollectionListView.setAdapter(headInfoAdapter);

        View topView = LayoutInflater.from(this).inflate(R.layout.collection_head, null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(128));
        params.setMargins(0,0,0,SizeUtils.dp2px(12));
        topView.setLayoutParams(params);
        headInfoAdapter.setHeaderView(topView);
    }

}
