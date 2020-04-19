package com.feiyou.headstyle.ui.activity;

import android.os.Bundle;

import com.feiyou.headstyle.R;
import com.feiyou.headstyle.ui.base.BaseFragmentActivity;

/**
 * Created by myflying on 2018/11/23.
 */
public class Collection1Activity extends BaseFragmentActivity {


    @Override
    protected int getContextViewId() {
        return R.layout.activity_collection1;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        QMUIStatusBarHelper.translucent(this);
//
//        List<HeadInfo> headInfoList = new ArrayList<>();
//
//        for (int m = 0; m < 60; m++) {
//            headInfoList.add(new HeadInfo());
//        }
//
//        headInfoAdapter = new HeadInfoAdapter(this, headInfoList);
//        mCollectionListView.setLayoutManager(new GridLayoutManager(this, 3));
//        mCollectionListView.setAdapter(headInfoAdapter);
//
//        View topView = LayoutInflater.from(this).inflate(R.layout.collection_head, null);
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(128));
//        params.setMargins(0,0,0,SizeUtils.dp2px(12));
//        topView.setLayoutParams(params);
//        headInfoAdapter.setHeaderView(topView);
    }

}
