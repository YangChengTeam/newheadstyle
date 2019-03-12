package com.feiyou.headstyle.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.feiyou.headstyle.App;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.FollowInfoRet;
import com.feiyou.headstyle.bean.NoteTypeRet;
import com.feiyou.headstyle.bean.NoteTypeWrapper;
import com.feiyou.headstyle.bean.ResultInfo;
import com.feiyou.headstyle.common.Constants;
import com.feiyou.headstyle.presenter.FollowInfoPresenterImp;
import com.feiyou.headstyle.presenter.NoteTypePresenterImp;
import com.feiyou.headstyle.ui.adapter.DetailFragmentAdapter;
import com.feiyou.headstyle.ui.base.BaseFragmentActivity;
import com.feiyou.headstyle.ui.custom.JudgeNestedScrollView;
import com.feiyou.headstyle.ui.custom.LoginDialog;
import com.feiyou.headstyle.ui.fragment.sub.FollowFragment;
import com.feiyou.headstyle.ui.fragment.sub.NewFragment;
import com.feiyou.headstyle.ui.fragment.sub.RecommendFragment;
import com.feiyou.headstyle.ui.fragment.sub.VideoFragment;
import com.feiyou.headstyle.ui.fragment.sub.WonderfulFragment;
import com.feiyou.headstyle.view.NoteTypeView;
import com.jcodecraeer.xrecyclerview.AppBarStateChangeListener;
import com.orhanobut.logger.Logger;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by myflying on 2018/11/28.
 */
public class CommunityTypeActivity extends BaseFragmentActivity implements NoteTypeView {

    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.toolbar_iv_image)
    ImageView mTopBarImageView;

    @BindView(R.id.iv_back)
    ImageView mBackImageView;

    @BindView(R.id.tv_title)
    TextView mTitleTextView;

    @BindView(R.id.iv_top_share)
    ImageView mShareImageView;

    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;

    @BindView(R.id.view_pager)
    ViewPager viewPager;

//    @BindView(R.id.scroll_view)
//    JudgeNestedScrollView scrollView;

    @BindView(R.id.tv_topic_name)
    TextView mTopicNameTv;

    @BindView(R.id.tv_fans_count)
    TextView mFansCountTv;

    @BindView(R.id.tv_note_count)
    TextView mNoteCountTv;

    @BindView(R.id.tv_top1_note_name)
    TextView mTop1NoteNameTv;

    @BindView(R.id.layout_is_follow)
    FrameLayout mFollowLayout;

    @BindView(R.id.tv_follow_txt)
    TextView mFollowTv;

    int toolBarPositionY = 0;

    List<String> mTitleDataList;

    private NoteTypePresenterImp noteTypePresenterImp;

    String topicId;

    private int currentPage = 1;

    private int pageSize = 30;

    FollowInfoPresenterImp followInfoPresenterImp;

    LoginDialog loginDialog;

    private String noteId;

    @Override
    protected int getContextViewId() {
        return R.layout.activity_community_type;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    public void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.getString("topic_id") != null) {
            topicId = bundle.getString("topic_id");
        }

        QMUIStatusBarHelper.setStatusBarDarkMode(CommunityTypeActivity.this);

        appBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if (state == State.EXPANDED) {//展开状态
                    isExpand(true);
                } else if (state == State.COLLAPSED) {
                    isExpand(false);
                } else {
                    //中间状态
                }
            }
        });

        loginDialog = new LoginDialog(this, R.style.login_dialog);

        mTitleDataList = new ArrayList<>();
        mTitleDataList.add("最新");
        mTitleDataList.add("热门");

        Fragment[] fragments = new Fragment[]{NewFragment.newInstance(topicId, 1), NewFragment.newInstance(topicId, 2)};

        DetailFragmentAdapter viewPageAdapter = new DetailFragmentAdapter(getSupportFragmentManager(), fragments, mTitleDataList);
        viewPager.setAdapter(viewPageAdapter);

        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        mTabLayout.setupWithViewPager(viewPager);

        followInfoPresenterImp = new FollowInfoPresenterImp(this, this);
        noteTypePresenterImp = new NoteTypePresenterImp(this, this);
        noteTypePresenterImp.getNoteTypeData(topicId, currentPage, 1, App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "");
    }

    //设置折叠展开状态
    public void isExpand(boolean expand) {
        if (expand) {
            mShareImageView.setImageResource(R.mipmap.top_share_white);
            mBackImageView.setImageResource(R.mipmap.back_icon);
            mTitleTextView.setTextColor(ContextCompat.getColor(this, R.color.white));
            mTitleTextView.setVisibility(View.GONE);
            QMUIStatusBarHelper.setStatusBarDarkMode(CommunityTypeActivity.this);
        } else {
            mBackImageView.setImageResource(R.mipmap.common_back_icon);
            mShareImageView.setImageResource(R.mipmap.top_share_black);
            mTitleTextView.setTextColor(ContextCompat.getColor(this, R.color.black));
            mTitleTextView.setVisibility(View.VISIBLE);
            QMUIStatusBarHelper.setStatusBarLightMode(CommunityTypeActivity.this);
        }
    }

    @OnClick(R.id.tv_top1_note_name)
    void topNote() {
        Intent intent = new Intent(this, CommunityArticleActivity.class);
        intent.putExtra("msg_id", noteId);
        startActivity(intent);
    }

    @OnClick(R.id.iv_back)
    void back() {
        popBackStack();
    }

    @OnClick(R.id.layout_is_follow)
    void followTopic() {

        if (!App.getApp().isLogin) {
            if (loginDialog != null && !loginDialog.isShowing()) {
                loginDialog.show();
            }
            return;
        }

        followInfoPresenterImp.followTopic(App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "", topicId);
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void dismissProgress() {

    }

    @Override
    public void loadDataSuccess(ResultInfo tData) {
        if (tData != null && tData.getCode() == Constants.SUCCESS) {

            if (tData instanceof NoteTypeRet) {
                NoteTypeWrapper noteTypeWrapper = ((NoteTypeRet) tData).getData();
                if (noteTypeWrapper != null && noteTypeWrapper.getTopicArr() != null) {
                    RequestOptions options = new RequestOptions();
                    options.error(R.mipmap.community_type_top);
                    Glide.with(this).load(noteTypeWrapper.getTopicArr().getBackground()).into(mTopBarImageView);
                    mTopicNameTv.setText(noteTypeWrapper.getTopicArr().getName());
                }

                mFansCountTv.setText("关注：" + noteTypeWrapper.getGuanNum());
                mNoteCountTv.setText("贴子：" + noteTypeWrapper.getMessageNum());

                if (noteTypeWrapper != null && noteTypeWrapper.getNoticeList() != null) {
                    if (noteTypeWrapper.getNoticeList().size() > 0) {
                        noteId = noteTypeWrapper.getNoticeList().get(0).getId();
                        mTop1NoteNameTv.setText(noteTypeWrapper.getNoticeList().get(0).getTitle());
                    }
                }

                if (noteTypeWrapper.getIsGuan() == 0) {
                    mFollowLayout.setBackgroundResource(R.mipmap.not_follow_topic);
                    mFollowTv.setTextColor(ContextCompat.getColor(this, R.color.white));
                } else {
                    mFollowLayout.setBackgroundResource(R.mipmap.is_follow_icon);
                    mFollowTv.setTextColor(ContextCompat.getColor(this, R.color.is_follow_topic_bg_color));
                    mFollowTv.setText("已关注");
                }
            }

            if (tData instanceof FollowInfoRet) {
                if (((FollowInfoRet) tData).getData() != null) {
                    if (((FollowInfoRet) tData).getData().getIsGuan() == 0) {
                        ToastUtils.showLong("已取消");
                        mFollowLayout.setBackgroundResource(R.mipmap.not_follow_topic);
                        mFollowTv.setTextColor(ContextCompat.getColor(this, R.color.white));
                    } else {
                        ToastUtils.showLong("已关注");
                        mFollowLayout.setBackgroundResource(R.mipmap.is_follow_icon);
                        mFollowTv.setTextColor(ContextCompat.getColor(this, R.color.is_follow_topic_bg_color));
                        mFollowTv.setText("已关注");
                    }
                } else {
                    ToastUtils.showLong(StringUtils.isEmpty(tData.getMsg()) ? "操作错误" : tData.getMsg());
                }
            }
        } else {

            if (tData instanceof FollowInfoRet) {
                ToastUtils.showLong(StringUtils.isEmpty(tData.getMsg()) ? "操作错误" : tData.getMsg());
            } else {
                Logger.i("error--->" + tData.getMsg());
            }
        }
    }

    @Override
    public void loadDataError(Throwable throwable) {

    }

    public static abstract class AppBarStateChangeListener implements AppBarLayout.OnOffsetChangedListener {
        public enum State {
            EXPANDED,
            COLLAPSED,
            IDLE
        }

        private State mCurrentState = State.IDLE;

        @Override
        public final void onOffsetChanged(AppBarLayout appBarLayout, int i) {
            Logger.i("onOffset--->" + i);
            if (i == 0) {
                if (mCurrentState != State.EXPANDED) {
                    onStateChanged(appBarLayout, State.EXPANDED);
                }
                mCurrentState = State.EXPANDED;
            } else if (Math.abs(i) >= appBarLayout.getTotalScrollRange()) {
                if (mCurrentState != State.COLLAPSED) {
                    onStateChanged(appBarLayout, State.COLLAPSED);
                }
                mCurrentState = State.COLLAPSED;
            } else {
                if (mCurrentState != State.IDLE) {
                    onStateChanged(appBarLayout, State.IDLE);
                }
                mCurrentState = State.IDLE;
            }
        }

        public abstract void onStateChanged(AppBarLayout appBarLayout, State state);
    }

    @OnClick(R.id.layout_detail)
    void detail() {
        Intent intent = new Intent(this, CommunityArticleActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        popBackStack();
    }
}
