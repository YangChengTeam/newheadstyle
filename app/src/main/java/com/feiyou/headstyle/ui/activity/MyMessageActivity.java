package com.feiyou.headstyle.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.feiyou.headstyle.App;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.ui.adapter.MyCommentFragmentAdapter;
import com.feiyou.headstyle.ui.base.BaseFragmentActivity;
import com.feiyou.headstyle.ui.fragment.sub.MyAtMessageFragment;
import com.feiyou.headstyle.ui.fragment.sub.MyCommentFragment;
import com.feiyou.headstyle.ui.fragment.sub.SystemInfoFragment;
import com.orhanobut.logger.Logger;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by myflying on 2018/11/23.
 */
public class MyMessageActivity extends BaseFragmentActivity implements ViewPager.OnPageChangeListener, TabHost.OnTabChangeListener {

    private LayoutInflater layoutInflater;

    @BindView(R.id.iv_back)
    ImageView mBackImageView;

    @BindView(android.R.id.tabhost)
    FragmentTabHost mTabHost;

    @BindView(R.id.view_pager)
    ViewPager viewPager;

    //Tab选项卡的文字
    private String[] mTextviewArray = {"评论", "@我", "通知"};

    private View lastTabView;

    @Override
    protected int getContextViewId() {
        return R.layout.activity_my_message;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        QMUIStatusBarHelper.setStatusBarLightMode(this);
        initData();
        initTabs();
    }

    public void initData() {

        layoutInflater = LayoutInflater.from(this);
        //初始化TabHost
        mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);
        mTabHost.getTabWidget().setDividerDrawable(null);

        //为每一个Tab按钮设置图标、文字和内容
        TabHost.TabSpec commentTab = mTabHost.newTabSpec(mTextviewArray[0]).setIndicator(getTabItemView(0));
        //将Tab按钮添加进Tab选项卡中
        mTabHost.addTab(commentTab, MyCommentFragment.getInstance().getClass(), null);

        //为每一个Tab按钮设置图标、文字和内容
        TabHost.TabSpec atTab = mTabHost.newTabSpec(mTextviewArray[1]).setIndicator(getTabItemView(1));
        //将Tab按钮添加进Tab选项卡中
        mTabHost.addTab(atTab, MyAtMessageFragment.getInstance().getClass(), null);

        //为每一个Tab按钮设置图标、文字和内容
        TabHost.TabSpec systemTab = mTabHost.newTabSpec(mTextviewArray[2]).setIndicator(getTabItemView(2));
        //将Tab按钮添加进Tab选项卡中
        mTabHost.addTab(systemTab, SystemInfoFragment.getInstance().getClass(), null);

        setCurrentTab(0);

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
        viewPager.setAdapter(new MyCommentFragmentAdapter(getSupportFragmentManager()));
        mTabHost.getTabWidget().setDividerDrawable(null);
        viewPager.setCurrentItem(0);
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
        ImageView ivRemind = view.findViewById(R.id.iv_remind);
        tabText.setText(mTextviewArray[index]);
        switch (index) {
            case 0:
                if (App.isRemindComment) {
                    ivRemind.setVisibility(View.VISIBLE);
                }
                break;
            case 1:
                if (App.isRemindAt) {
                    ivRemind.setVisibility(View.VISIBLE);
                }
                break;
            case 2:
                if (App.isRemindNotice) {
                    ivRemind.setVisibility(View.VISIBLE);
                }
                break;
            default:
                break;
        }
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
        switch (position) {
            case 0:
                App.isRemindComment = false;
                break;
            case 1:
                App.isRemindAt = false;
                break;
            case 2:
                App.isRemindNotice = false;
                break;
            default:
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    @Override
    public void onTabChanged(String tabId) {
        Logger.e("tabId---" + tabId);
        viewPager.setCurrentItem(mTabHost.getCurrentTab());
        setTabStyle(tabId);
    }

    public void setTabStyle(String tabId) {
        View layout = mTabHost.getCurrentTabView();
        View lineView = layout.findViewById(R.id.tab_line);
        TextView tabText = layout.findViewById(R.id.tv_tab_text);
        ImageView ivRemind = layout.findViewById(R.id.iv_remind);
        tabText.setTextColor(ContextCompat.getColor(this, R.color.black));
        tabText.setTextSize(20);
        lineView.setVisibility(View.VISIBLE);
        ivRemind.setVisibility(View.GONE);

        //恢复上一个
        View lastLineView = lastTabView.findViewById(R.id.tab_line);
        TextView lastTabText = lastTabView.findViewById(R.id.tv_tab_text);
        ImageView lastIv = lastTabView.findViewById(R.id.iv_remind);
        lastIv.setVisibility(View.GONE);
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
