package com.feiyou.headstyle.ui.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTabHost;
import androidx.viewpager.widget.ViewPager;

import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.MessageEvent;
import com.feiyou.headstyle.ui.adapter.FollowFragmentAdapter;
import com.feiyou.headstyle.ui.base.BaseFragmentActivity;
import com.feiyou.headstyle.ui.fragment.sub.MyFensFragment;
import com.feiyou.headstyle.ui.fragment.sub.MyFriendsFragment;
import com.feiyou.headstyle.utils.StringUtils;
import com.orhanobut.logger.Logger;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by myflying on 2018/11/23.
 * 我的关注/收藏
 */
public class MyFollowActivity extends BaseFragmentActivity implements ViewPager.OnPageChangeListener, TabHost.OnTabChangeListener {

    @BindView(R.id.iv_back)
    ImageView mBackImageView;

    @BindView(R.id.layout_my_follow_top)
    RelativeLayout followTopLayout;

    @BindView(android.R.id.tabhost)
    FragmentTabHost mTabHost;

    @BindView(R.id.view_pager)
    ViewPager viewPager;

    //定义一个布局
    private LayoutInflater layoutInflater;

    //Tab选项卡的文字
    private String[] mTextviewArray = {"关注", "粉丝"};

    private View lastTabView;

    @Override
    protected int getContextViewId() {
        return R.layout.activity_my_follow;
    }

    private Fragment[] fragments;

    private int type = 0;

    private boolean isMyInfo;

    private String intoUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        QMUIStatusBarHelper.setStatusBarLightMode(this);
        initData();
        initTabs();
    }

    public void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.getInt("type") > 0) {
            type = bundle.getInt("type", 0);
        }

        if (bundle != null) {
            isMyInfo = bundle.getBoolean("is_my_info", false);
        }

        if (bundle != null && !StringUtils.isEmpty(bundle.getString("into_user_id"))) {
            intoUserId = bundle.getString("into_user_id");
        }

        fragments = new Fragment[]{new MyFriendsFragment(), new MyFensFragment()};

        layoutInflater = LayoutInflater.from(this);
        //初始化TabHost
        mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);
        mTabHost.getTabWidget().setDividerDrawable(null);

        //为每一个Tab按钮设置图标、文字和内容
        TabHost.TabSpec followTab = mTabHost.newTabSpec("关注").setIndicator(getTabItemView(0));
        //将Tab按钮添加进Tab选项卡中
        mTabHost.addTab(followTab, fragments[0].getClass(), null);

        //为每一个Tab按钮设置图标、文字和内容
        TabHost.TabSpec fenTab = mTabHost.newTabSpec("粉丝").setIndicator(getTabItemView(1));
        //将Tab按钮添加进Tab选项卡中
        mTabHost.addTab(fenTab, fragments[1].getClass(), null);

        setCurrentTab(type);

        mTabHost.setOnTabChangedListener(this);
        viewPager.addOnPageChangeListener(this);

        //设置默认值
        View firstView = mTabHost.getCurrentTabView();
        lastTabView = firstView;
        View lineView = firstView.findViewById(R.id.tab_line);
        TextView tabText = firstView.findViewById(R.id.tv_tab_text);
        lineView.setVisibility(View.VISIBLE);
        tabText.setTextColor(ContextCompat.getColor(this, R.color.black));
        tabText.setTextSize(20);

    }

    private void initTabs() {
        // 这里的添加顺序对 tab 页的先后顺序有影响
        viewPager.setAdapter(new FollowFragmentAdapter(getSupportFragmentManager(), fragments));
        mTabHost.getTabWidget().setDividerDrawable(null);
        viewPager.setCurrentItem(type);
    }

    public void setCurrentTab(int tabPosition) {
        mTabHost.setCurrentTab(tabPosition);
    }

    /**
     * 给Tab按钮设置图标和文字
     */
    private View getTabItemView(int index) {
        View view = layoutInflater.inflate(R.layout.tab_item_view, null);

        TextView tabText = view.findViewById(R.id.tv_tab_text);
        tabText.setText(mTextviewArray[index]);
        return view;
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int position) {
        Logger.i("select position --->" + position);

        TabWidget widget = mTabHost.getTabWidget();
        int oldFocusability = widget.getDescendantFocusability();
        widget.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        mTabHost.setCurrentTab(position);
        widget.setDescendantFocusability(oldFocusability);
    }

    public int getTabIndex() {
        return mTabHost.getCurrentTab() == 0 ? 1 : 2;
    }

    public boolean isMyInfo() {
        return isMyInfo;
    }

    public String getIntoUserId() {
        return intoUserId;
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    @Override
    public void onTabChanged(String tabId) {
        Logger.e("tabId---" + tabId);
        viewPager.setCurrentItem(mTabHost.getCurrentTab());
        setTabStyle(tabId);
        EventBus.getDefault().post(new MessageEvent("load_friend_list"));
    }

    public void setTabStyle(String tabId) {
        View layout = mTabHost.getCurrentTabView();
        View lineView = layout.findViewById(R.id.tab_line);
        TextView tabText = layout.findViewById(R.id.tv_tab_text);
        tabText.setTextColor(ContextCompat.getColor(this, R.color.black));
        tabText.setTextSize(20);
        lineView.setVisibility(View.VISIBLE);

        //恢复上一个
        View lastLineView = lastTabView.findViewById(R.id.tab_line);
        TextView lastTabText = lastTabView.findViewById(R.id.tv_tab_text);
        lastTabText.setTextColor(ContextCompat.getColor(this, R.color.gray999));
        lastTabText.setTextSize(16);
        lastLineView.setVisibility(View.INVISIBLE);

        //重新赋值
        lastTabView = mTabHost.getCurrentTabView();
    }

    @OnClick(R.id.iv_back)
    void back() {
        popBackStack();
    }

    @Override
    public void onBackPressed() {
        popBackStack();
    }
}
