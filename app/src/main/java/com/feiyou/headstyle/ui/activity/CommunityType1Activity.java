package com.feiyou.headstyle.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feiyou.headstyle.App;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.FollowInfoRet;
import com.feiyou.headstyle.bean.NoteTypeRet;
import com.feiyou.headstyle.bean.NoteTypeWrapper;
import com.feiyou.headstyle.bean.ResultInfo;
import com.feiyou.headstyle.bean.ZanResultRet;
import com.feiyou.headstyle.common.Constants;
import com.feiyou.headstyle.presenter.AddZanPresenterImp;
import com.feiyou.headstyle.presenter.FollowInfoPresenterImp;
import com.feiyou.headstyle.presenter.NoteTypePresenterImp;
import com.feiyou.headstyle.ui.adapter.DetailFragmentAdapter;
import com.feiyou.headstyle.ui.adapter.NoteInfoAdapter;
import com.feiyou.headstyle.ui.base.BaseFragmentActivity;
import com.feiyou.headstyle.ui.custom.LoginDialog;
import com.feiyou.headstyle.ui.fragment.sub.NewFragment;
import com.feiyou.headstyle.view.NoteTypeView;
import com.orhanobut.logger.Logger;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by myflying on 2018/11/28.
 */
public class CommunityType1Activity extends BaseFragmentActivity implements NoteTypeView {

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

    @BindView(R.id.community_type_list)
    RecyclerView mCommunityTypeListView;

    NoteInfoAdapter noteInfoAdapter;
    
    private int currentPage = 1;

    private int pageSize = 20;

    private FollowInfoPresenterImp followInfoPresenterImp;

    private NoteTypePresenterImp noteTypePresenterImp;

    private AddZanPresenterImp addZanPresenterImp;

    LoginDialog loginDialog;

    private String noteId;

    private String topicId;

    private int currentClickIndex;

    @Override
    protected int getContextViewId() {
        return R.layout.activity_community_type1;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        QMUIStatusBarHelper.setStatusBarDarkMode(CommunityType1Activity.this);
        initData();
    }

    public void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.getString("topic_id") != null) {
            topicId = bundle.getString("topic_id");
        }

        loginDialog = new LoginDialog(this, R.style.login_dialog);

        followInfoPresenterImp = new FollowInfoPresenterImp(this, this);
        noteTypePresenterImp = new NoteTypePresenterImp(this, this);
        addZanPresenterImp = new AddZanPresenterImp(this, this);

        noteInfoAdapter = new NoteInfoAdapter(this, null, 1);
        mCommunityTypeListView.setLayoutManager(new LinearLayoutManager(this));
        mCommunityTypeListView.setAdapter(noteInfoAdapter);

        noteInfoAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(CommunityType1Activity.this, CommunityArticleActivity.class);
                intent.putExtra("msg_id", noteInfoAdapter.getData().get(position).getId());
                startActivity(intent);
            }
        });

        noteInfoAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                currentClickIndex = position;

                if (!App.getApp().isLogin) {
                    if (loginDialog != null && !loginDialog.isShowing()) {
                        loginDialog.show();
                    }
                    return;
                }

                if (view.getId() == R.id.layout_follow) {
                    followInfoPresenterImp.addFollow(App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "", noteInfoAdapter.getData().get(position).getUserId());
                }
                if (view.getId() == R.id.layout_item_zan) {
                    String messageId = noteInfoAdapter.getData().get(position).getId();
                    addZanPresenterImp.addZan(1, App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "", noteInfoAdapter.getData().get(position).getUserId(), messageId, "", "", 1);
                }
                if (view.getId() == R.id.iv_user_head) {
                    Intent intent = new Intent(CommunityType1Activity.this, UserInfoActivity.class);
                    intent.putExtra("user_id", noteInfoAdapter.getData().get(position).getUserId());
                    startActivity(intent);
                }
            }
        });

        noteInfoAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                currentPage++;
                noteTypePresenterImp.getNoteTypeData(topicId, currentPage, 1, App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "");
            }
        }, mCommunityTypeListView);

        mCommunityTypeListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int topRowVerticalPosition = (recyclerView == null || recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();
                //mRefreshLayout.setEnabled(topRowVerticalPosition >= 0);
            }
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        noteTypePresenterImp.getNoteTypeData(topicId, currentPage, 1, App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "");
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

                if (noteTypeWrapper != null && noteTypeWrapper.getList() != null) {
                    if (currentPage == 1) {
                        noteInfoAdapter.setNewData(noteTypeWrapper.getList());
                    } else {
                        noteInfoAdapter.addData(noteTypeWrapper.getList());
                    }
                }

                if (noteTypeWrapper != null && noteTypeWrapper.getList().size() == pageSize) {
                    noteInfoAdapter.loadMoreComplete();
                } else {
                    noteInfoAdapter.loadMoreEnd();
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

            if (tData instanceof ZanResultRet) {
                int tempNum = noteInfoAdapter.getData().get(currentClickIndex).getZanNum();
                if (((ZanResultRet) tData).getData().getIsZan() == 0) {
                    tempNum = tempNum - 1;
                } else {
                    tempNum = tempNum + 1;
                }

                noteInfoAdapter.getData().get(currentClickIndex).setZanNum(tempNum);
                noteInfoAdapter.getData().get(currentClickIndex).setIsZan(((ZanResultRet) tData).getData().getIsZan());
                noteInfoAdapter.notifyDataSetChanged();
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
}
