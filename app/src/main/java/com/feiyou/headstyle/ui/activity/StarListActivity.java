package com.feiyou.headstyle.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.StarInfo;
import com.feiyou.headstyle.ui.adapter.BlackListAdapter;
import com.feiyou.headstyle.ui.adapter.StarListAdapter;
import com.feiyou.headstyle.ui.base.BaseFragmentActivity;
import com.feiyou.headstyle.utils.StatusBarUtil;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by myflying on 2018/11/23.
 */
public class StarListActivity extends BaseFragmentActivity {

    @BindView(R.id.iv_back)
    ImageView mBackImageView;

    @BindView(R.id.star_list)
    RecyclerView mStarListView;

    @BindView(R.id.layout_star_top)
    RelativeLayout mTopLayout;

    StarListAdapter starListAdapter;

    private String[] starName = {"白羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "摩羯座", "水瓶座", "双鱼座"};

    private String[] starDate = {"3.21~4.19", "4.20~5.20", "5.21~6.21", "6.22~7.22", "7.23~8.22", "8.23~9.22", "9.23~10.23", "10.24~11.22", "11.23~12.21", "12.22~1.19", "1.20~2.18", "2.19~3.20"};

    private Integer[] starImage = {R.mipmap.star1, R.mipmap.star2, R.mipmap.star3, R.mipmap.star4, R.mipmap.star5, R.mipmap.star6, R.mipmap.star7, R.mipmap.star8, R.mipmap.star9, R.mipmap.star10, R.mipmap.star11, R.mipmap.star12};

    @Override
    protected int getContextViewId() {
        return R.layout.activity_star_list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTopBar();
        initData();
    }

    private void initTopBar() {
        QMUIStatusBarHelper.setStatusBarLightMode(this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(48));
        params.setMargins(0, StatusBarUtil.getStatusBarHeight(this), 0, 0);

        mTopLayout.setLayoutParams(params);

        mBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });
    }

    public void initData() {
        List<StarInfo> list = new ArrayList<>();

        for (int i = 0; i < starName.length; i++) {
            StarInfo starInfo = new StarInfo();
            starInfo.setStarName(starName[i]);
            starInfo.setStarDate(starDate[i]);
            starInfo.setStarImage(starImage[i]);
            list.add(starInfo);
        }

        starListAdapter = new StarListAdapter(this, list);
        mStarListView.setLayoutManager(new GridLayoutManager(this, 4));
        mStarListView.setAdapter(starListAdapter);

        starListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(StarListActivity.this, StarDetailActivity.class);
                intent.putExtra("star_index", position);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        popBackStack();
    }
}
