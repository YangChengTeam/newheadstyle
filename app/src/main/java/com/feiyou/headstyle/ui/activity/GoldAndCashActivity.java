package com.feiyou.headstyle.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.bumptech.glide.Glide;
import com.feiyou.headstyle.App;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.PlayGameInfo;
import com.feiyou.headstyle.bean.SeeVideoInfo;
import com.feiyou.headstyle.ui.adapter.GoldAndCashAdapter;
import com.feiyou.headstyle.ui.adapter.MyCommentFragmentAdapter;
import com.feiyou.headstyle.ui.base.BaseFragmentActivity;
import com.feiyou.headstyle.ui.fragment.CashDetailFragment;
import com.feiyou.headstyle.ui.fragment.GoldDetailFragment;
import com.feiyou.headstyle.ui.fragment.sub.MyAtMessageFragment;
import com.feiyou.headstyle.ui.fragment.sub.MyCommentFragment;
import com.feiyou.headstyle.ui.fragment.sub.SystemInfoFragment;
import com.orhanobut.logger.Logger;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by myflying on 2018/11/23.
 */
public class GoldAndCashActivity extends BaseFragmentActivity implements ViewPager.OnPageChangeListener, TabHost.OnTabChangeListener {

    @BindView(R.id.topbar)
    QMUITopBar mTopBar;

    ImageView mBackImageView;

    private LayoutInflater layoutInflater;

    @BindView(android.R.id.tabhost)
    FragmentTabHost mTabHost;

    @BindView(R.id.view_pager)
    ViewPager viewPager;

    @BindView(R.id.tv_today_gold_num)
    TextView mTodayGoldNumTv;

    @BindView(R.id.tv_get_total_gold_num)
    TextView mGetTotalGoldNumTv;

    @BindView(R.id.tv_my_gold_num)
    TextView mMyGoldNumTv;

    @BindView(R.id.tv_type_title)
    TextView mTypeTitleTv;

    @BindView(R.id.iv_gold_icon)
    ImageView mGoldIv;

    @BindView(R.id.btn_task)
    Button mTypeTaskBtn;

    @BindView(R.id.layout_gold_total)
    LinearLayout mGoldTotalLayout;

    @BindView(R.id.layout_cash_total)
    LinearLayout mCashTotalLayout;

    @BindView(R.id.tv_turn_scale)
    TextView mTurnInfoTv;

    //Tab选项卡的文字
    private String[] mTextviewArray = {"金币收益", "现金收益"};

    private View lastTabView;

    public int totalGoldNum;

    public int todayGoldNum;

    public int totalGetGold;//累计赚取

    public double cashMoney;

    public double scaleGoldNum;

    public double scaleCashNum;

    private PlayGameInfo playGameInfo;

    private SeeVideoInfo gameSeeVideoInfo;

    @Override
    protected int getContextViewId() {
        return R.layout.activity_gold_and_cash;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTopBar();
        initData();
        initTabs();
    }

    private void initTopBar() {
        QMUIStatusBarHelper.setStatusBarLightMode(this);
        View aboutView = getLayoutInflater().inflate(R.layout.common_top_back, null);
        aboutView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(48)));
        TextView titleTv = aboutView.findViewById(R.id.tv_title);
        titleTv.setText("金币明细");

        mTopBar.setCenterView(aboutView);
        mBackImageView = aboutView.findViewById(R.id.iv_back);
        mBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });
    }

    public void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            cashMoney = bundle.getDouble("cash_money");
            scaleGoldNum = bundle.getDouble("scale_gold_num");
            scaleCashNum = bundle.getDouble("scale_cash_num");
        }

        playGameInfo = (PlayGameInfo) getIntent().getSerializableExtra("play_game_info");
        gameSeeVideoInfo = (SeeVideoInfo) getIntent().getSerializableExtra("game_see_video");

        layoutInflater = LayoutInflater.from(this);
        //初始化TabHost
        mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);
        mTabHost.getTabWidget().setDividerDrawable(null);

        //为每一个Tab按钮设置图标、文字和内容
        TabHost.TabSpec commentTab = mTabHost.newTabSpec(mTextviewArray[0]).setIndicator(getTabItemView(0));
        //将Tab按钮添加进Tab选项卡中
        mTabHost.addTab(commentTab, GoldDetailFragment.getInstance().getClass(), null);

        //为每一个Tab按钮设置图标、文字和内容
        TabHost.TabSpec atTab = mTabHost.newTabSpec(mTextviewArray[1]).setIndicator(getTabItemView(1));
        //将Tab按钮添加进Tab选项卡中
        mTabHost.addTab(atTab, CashDetailFragment.getInstance().getClass(), null);

        setCurrentTab(0);

        mTabHost.setOnTabChangedListener(this);
        viewPager.addOnPageChangeListener(this);

        //设置默认值
        View firstView = mTabHost.getCurrentTabView();
        lastTabView = firstView;
        View lineView = firstView.findViewById(R.id.tab_line);
        TextView tabText = firstView.findViewById(R.id.tv_tab_text);
        lineView.setVisibility(View.VISIBLE);
        tabText.setTextColor(ContextCompat.getColor(this, R.color.tab_select_color));
        tabText.setTextSize(16);

        mTurnInfoTv.setText("转换汇率：" + scaleGoldNum + "金币≈" + scaleCashNum + "元");

        showInfo(0);
    }

    private void initTabs() {
        // 这里的添加顺序对 tab 页的先后顺序有影响
        viewPager.setAdapter(new GoldAndCashAdapter(getSupportFragmentManager()));
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
        ivRemind.setVisibility(View.INVISIBLE);
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
        showInfo(position);
    }

    public void showInfo(int type) {
        if (type == 0) {
            mTypeTitleTv.setText("金币收益(个)");
            Glide.with(this).load(R.mipmap.shouyi_gold).into(mGoldIv);
            mTypeTaskBtn.setText("去提现");
            mGoldTotalLayout.setVisibility(View.VISIBLE);
            mCashTotalLayout.setVisibility(View.GONE);
        } else {
            mTypeTitleTv.setText("现金收益(元)");
            Glide.with(this).load(R.mipmap.shouyi_rmb).into(mGoldIv);
            mTypeTaskBtn.setText("去提现");
            mMyGoldNumTv.setText(cashMoney + "");
            mGoldTotalLayout.setVisibility(View.GONE);
            mCashTotalLayout.setVisibility(View.VISIBLE);
            mTurnInfoTv.setText("转换汇率：" + scaleGoldNum + "金币≈" + scaleCashNum + "元");
        }
        Logger.i("current tab--->" + mTabHost.getCurrentTab());
    }

    @OnClick(R.id.btn_task)
    public void taskBtn() {
        Intent intent = new Intent(this, CashActivity.class);
        intent.putExtra("play_game_info", playGameInfo);
        intent.putExtra("game_see_video", gameSeeVideoInfo);
        startActivity(intent);
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
        tabText.setTextColor(ContextCompat.getColor(this, R.color.tab_select_color));
        tabText.setTextSize(16);
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

    public int getTotalGoldNum() {
        return totalGoldNum;
    }

    public void setTotalGoldNum(int totalGoldNum) {
        this.totalGoldNum = totalGoldNum;
        mMyGoldNumTv.setText(totalGoldNum + "");
    }

    public int getTodayGoldNum() {
        return todayGoldNum;
    }

    public void setTodayGoldNum(int todayGoldNum) {
        this.todayGoldNum = todayGoldNum;
        mTodayGoldNumTv.setText(todayGoldNum + "");
    }

    public int getTotalGetGold() {
        return totalGetGold;
    }

    public void setTotalGetGold(int totalGetGold) {
        this.totalGetGold = totalGetGold;
        mGetTotalGoldNumTv.setText(totalGetGold + "");
    }

    @Override
    public void onBackPressed() {
        popBackStack();
    }
}
