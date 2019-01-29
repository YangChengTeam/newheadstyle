package com.feiyou.headstyle.ui.activity;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.bumptech.glide.Glide;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.HeadInfo;
import com.feiyou.headstyle.bean.NoteInfoDetailRet;
import com.feiyou.headstyle.common.Constants;
import com.feiyou.headstyle.presenter.NoteInfoDetailDataPresenterImp;
import com.feiyou.headstyle.ui.adapter.DetailFragmentAdapter;
import com.feiyou.headstyle.ui.adapter.HeadInfoAdapter;
import com.feiyou.headstyle.ui.base.BaseFragmentActivity;
import com.feiyou.headstyle.ui.fragment.sub.VideoFragment;
import com.feiyou.headstyle.ui.fragment.sub.WonderfulFragment;
import com.feiyou.headstyle.view.NoteInfoDetailDataView;
import com.orhanobut.logger.Logger;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by myflying on 2018/11/28.
 */
public class CommunityArticleActivity extends BaseFragmentActivity implements NoteInfoDetailDataView {

    @BindView(R.id.iv_back)
    ImageView mBackImageView;

    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

    @BindView(R.id.appBarLayout)
    AppBarLayout appBarLayout;

    @BindView(R.id.collapsingToolbarLayout)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.top_content_layout)
    LinearLayout mTopContentLayout;

    @BindView(R.id.iv_user_head)
    ImageView mUserHeadImageView;

    @BindView(R.id.tv_user_nick_name)
    TextView mNickNameTextView;

    @BindView(R.id.tv_topic_name)
    TextView mTopicNameTextView;

    @BindView(R.id.tv_add_date)
    TextView mAddDateTextView;

    @BindView(R.id.tv_note_content)
    TextView mNoteContentTextView;

    @BindView(R.id.note_img_list)
    RecyclerView mNoteImageListView;

    @BindView(R.id.tv_message_count)
    TextView mMessageCountTextView;

    @BindView(R.id.tv_zan_count)
    TextView mZanCountTextView;

    List<String> mTitleDataList;

    private String newsId;

    private NoteInfoDetailDataPresenterImp noteInfoDetailDataPresenterImp;

    private HeadInfoAdapter headInfoAdapter;

    @Override
    protected int getContextViewId() {
        return R.layout.activity_article_detail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
        initFragments();
    }

    public void initViews() {
        QMUIStatusBarHelper.setStatusBarLightMode(this);
        mTopContentLayout.setLayoutParams(new CollapsingToolbarLayout.LayoutParams(CollapsingToolbarLayout.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(392)));

        headInfoAdapter = new HeadInfoAdapter(this, null);
        mNoteImageListView.setLayoutManager(new GridLayoutManager(this, 3));
        mNoteImageListView.setAdapter(headInfoAdapter);

        noteInfoDetailDataPresenterImp = new NoteInfoDetailDataPresenterImp(this, this);
        noteInfoDetailDataPresenterImp.getNoteInfoDetailData("110600");
    }

    public void initFragments() {
        Fragment[] fragments = new Fragment[]{new WonderfulFragment(), new VideoFragment()};
        mTitleDataList = new ArrayList<>();
        mTitleDataList.add("精彩评论");
        mTitleDataList.add("最新评论");

        if (mTitleDataList.size() > 0) {
            DetailFragmentAdapter viewPageAdapter = new DetailFragmentAdapter(getSupportFragmentManager(), fragments, mTitleDataList);
            mViewPager.setAdapter(viewPageAdapter);

            tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
            tabLayout.setupWithViewPager(mViewPager);
        }
    }

    @OnClick(R.id.iv_back)
    void back() {
        popBackStack();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        popBackStack();
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void dismissProgress() {

    }

    @Override
    public void loadDataSuccess(NoteInfoDetailRet tData) {
        Logger.i(JSONObject.toJSONString(tData));

        if (tData != null && tData.getCode() == Constants.SUCCESS) {
            if (tData.getData() != null) {
                Glide.with(this).load(tData.getData().getUserimg()).into(mUserHeadImageView);
                mNickNameTextView.setText(tData.getData().getNickname());
                mTopicNameTextView.setText(tData.getData().getName());
                mAddDateTextView.setText(TimeUtils.millis2String(tData.getData().getAddTime() * 1000));
                mNoteContentTextView.setText(tData.getData().getContent());

                mMessageCountTextView.setText(tData.getData().getCommentNum() + "");
                mZanCountTextView.setText(tData.getData().getZanNum() + "");

                //设置帖子图片
                List<HeadInfo> headInfos = new ArrayList<>();
                String[] tempImg = tData.getData().getImageArr();

                final ArrayList<String> imageUrls = new ArrayList<>();
                for (int i = 0; i < tempImg.length; i++) {
                    HeadInfo headInfo = new HeadInfo();
                    headInfo.setImgurl(tempImg[i]);
                    headInfos.add(headInfo);
                    imageUrls.add(tempImg[i]);
                }

                headInfoAdapter.setNewData(headInfos);
            }
        }
    }

    @Override
    public void loadDataError(Throwable throwable) {

    }
}
