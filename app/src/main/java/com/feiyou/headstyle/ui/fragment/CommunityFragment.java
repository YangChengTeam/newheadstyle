package com.feiyou.headstyle.ui.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.feiyou.headstyle.App;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.MessageEvent;
import com.feiyou.headstyle.bean.TopicInfoRet;
import com.feiyou.headstyle.common.Constants;
import com.feiyou.headstyle.presenter.TopicDataPresenterImp;
import com.feiyou.headstyle.ui.adapter.MyFragmentAdapter;
import com.feiyou.headstyle.ui.adapter.SubFragmentAdapter;
import com.feiyou.headstyle.ui.base.BaseFragment;
import com.feiyou.headstyle.ui.fragment.sub.FollowFragment;
import com.feiyou.headstyle.ui.fragment.sub.RecommendFragment;
import com.feiyou.headstyle.ui.fragment.sub.VideoFragment;
import com.feiyou.headstyle.view.TopicDataView;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by iflying on 2018/2/6.
 */

public class CommunityFragment extends BaseFragment implements ViewPager.OnPageChangeListener, TabHost.OnTabChangeListener, TopicDataView {

    @BindView(android.R.id.tabhost)
    FragmentTabHost mTabHost;

    @BindView(R.id.view_pager)
    ViewPager viewPager;

    //定义一个布局
    private LayoutInflater layoutInflater;

    //定义数组来存放Fragment界面
    private Class fragmentArray[] = {FollowFragment.class, RecommendFragment.class, VideoFragment.class};

    //Tab选项卡的文字
    private int mTextviewArray[] = {R.string.community_tab_follow_txt, R.string.community_tab_rec_txt, R.string.community_tab_video_txt};

    private View lastTabView;

    private TopicDataPresenterImp topicDataPresenterImp;

    /**
     * onCreateView
     */
    @Override
    protected View onCreateView() {
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_community, null);
        ButterKnife.bind(this, root);
        initViews();
        initTabs();
        initData();
        return root;
    }

    /**
     * 初始化组件
     */
    public void initViews() {
        //实例化布局对象
        layoutInflater = LayoutInflater.from(getActivity());

        //初始化TabHost
        mTabHost.setup(getActivity(), getChildFragmentManager(), android.R.id.tabcontent);
        mTabHost.getTabWidget().setDividerDrawable(null);

        //得到fragment的个数
        int count = fragmentArray.length;

        for (int i = 0; i < count; i++) {
            //为每一个Tab按钮设置图标、文字和内容
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(getResources().getString(mTextviewArray[i])).setIndicator(getTabItemView(i));
            //将Tab按钮添加进Tab选项卡中
            mTabHost.addTab(tabSpec, fragmentArray[i], null);
        }

        setCurrentTab(1);

        mTabHost.setOnTabChangedListener(this);
        viewPager.addOnPageChangeListener(this);

        //设置默认值
        View firstView = mTabHost.getCurrentTabView();
        lastTabView = firstView;
        View lineView = firstView.findViewById(R.id.tab_line);
        TextView tabText = firstView.findViewById(R.id.tv_tab_text);
        lineView.setVisibility(View.VISIBLE);
        tabText.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
        tabText.setTextSize(20);
    }

    private void initTabs() {
        // 这里的添加顺序对 tab 页的先后顺序有影响
        viewPager.setAdapter(new SubFragmentAdapter(getChildFragmentManager()));
        mTabHost.getTabWidget().setDividerDrawable(null);
        viewPager.setCurrentItem(1);
    }

    public void initData() {
        topicDataPresenterImp = new TopicDataPresenterImp(this, getActivity());
        topicDataPresenterImp.getTopicDataList();
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

    public void setCurrentTab(int tabPosition) {
        mTabHost.setCurrentTab(tabPosition);
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
        tabText.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
        tabText.setTextSize(20);
        lineView.setVisibility(View.VISIBLE);

        //恢复上一个
        View lastLineView = lastTabView.findViewById(R.id.tab_line);
        TextView lastTabText = lastTabView.findViewById(R.id.tv_tab_text);
        lastTabText.setTextColor(ContextCompat.getColor(getActivity(), R.color.gray999));
        lastTabText.setTextSize(16);
        lastLineView.setVisibility(View.INVISIBLE);

        //重新赋值
        lastTabView = mTabHost.getCurrentTabView();
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void dismissProgress() {

    }

    @Override
    public void loadDataSuccess(TopicInfoRet tData) {
        if (tData != null) {
            if (tData.getCode() == Constants.SUCCESS) {
                App.topicInfoList = tData.getData();
                //if (RecommendFragment.getInstance().isVisible()) {
                   // RecommendFragment.getInstance().onRefresh();
                //}
                EventBus.getDefault().post(new MessageEvent("测试"));
            }
        }
    }

    @Override
    public void loadDataError(Throwable throwable) {

    }
}
