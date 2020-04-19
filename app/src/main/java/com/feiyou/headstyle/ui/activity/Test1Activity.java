package com.feiyou.headstyle.ui.activity;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.feiyou.headstyle.R;
import com.feiyou.headstyle.ui.adapter.BlackListAdapter;
import com.feiyou.headstyle.ui.base.BaseFragmentActivity;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by myflying on 2018/11/23.
 */
public class Test1Activity extends BaseFragmentActivity {

    @BindView(R.id.black_list)
    RecyclerView mBlackListView;

    BlackListAdapter blackListAdapter;

    @Override
    protected int getContextViewId() {
        return R.layout.activity_test1;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTopBar();
        initData();
    }

    private void initTopBar() {
        QMUIStatusBarHelper.setStatusBarLightMode(this);
    }

    public void initData() {
        List<String> list = new ArrayList<>();
        list.add("test");
        list.add("test");
        list.add("test");
        list.add("test");
        list.add("test");
        list.add("test");
        list.add("test");
        list.add("test");list.add("test");
        list.add("test");
        list.add("test");
        list.add("test");list.add("test");
        list.add("test");
        list.add("test");
        list.add("test");list.add("test");
        list.add("test");
        list.add("test");
        list.add("test");list.add("test");
        list.add("test");
        list.add("test");
        list.add("test");
        blackListAdapter = new BlackListAdapter(this,list);
        mBlackListView.setLayoutManager(new LinearLayoutManager(this));
        mBlackListView.setAdapter(blackListAdapter);
        mBlackListView.setNestedScrollingEnabled(false);
    }

    @Override
    public void onBackPressed() {
        popBackStack();
    }
}
