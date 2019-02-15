package com.feiyou.headstyle.ui.activity;

import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.HeadInfo;
import com.feiyou.headstyle.bean.MessageEvent;
import com.feiyou.headstyle.bean.NoteInfoDetailRet;
import com.feiyou.headstyle.bean.ReplyParams;
import com.feiyou.headstyle.bean.ReplyResultInfoRet;
import com.feiyou.headstyle.bean.ResultInfo;
import com.feiyou.headstyle.common.Constants;
import com.feiyou.headstyle.presenter.AddZanPresenterImp;
import com.feiyou.headstyle.presenter.NoteInfoDetailDataPresenterImp;
import com.feiyou.headstyle.presenter.ReplyCommentPresenterImp;
import com.feiyou.headstyle.ui.adapter.DetailFragmentAdapter;
import com.feiyou.headstyle.ui.adapter.HeadInfoAdapter;
import com.feiyou.headstyle.ui.base.BaseFragmentActivity;
import com.feiyou.headstyle.ui.fragment.sub.VideoFragment;
import com.feiyou.headstyle.ui.fragment.sub.WonderfulFragment;
import com.feiyou.headstyle.view.CommentDialog;
import com.feiyou.headstyle.view.NoteInfoDetailDataView;
import com.orhanobut.logger.Logger;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by myflying on 2018/11/28.
 */
public class CommunityArticleActivity extends BaseFragmentActivity implements NoteInfoDetailDataView, CommentDialog.SendBackListener {

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

    private AddZanPresenterImp addZanPresenterImp;

    private HeadInfoAdapter headInfoAdapter;

    CommentDialog commentDialog;

    private ProgressDialog progressDialog = null;

    private ReplyCommentPresenterImp replyCommentPresenterImp;

    private int commentNum;

    private String messageId;

    private boolean isFirstLoad = true;

    private boolean isZaned;

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
        addZanPresenterImp = new AddZanPresenterImp(this, this);

        replyCommentPresenterImp = new ReplyCommentPresenterImp(this, this);

        noteInfoDetailDataPresenterImp.getNoteInfoDetailData("1021601", "110600");

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("回复中");
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

    public void showDialog() {
        commentDialog = new CommentDialog(this, 1);
        commentDialog.setSendBackListener(this);
        commentDialog.show(getFragmentManager(), "dialog");
    }

    @OnClick(R.id.layout_message_count)
    void addComment() {
        showDialog();
    }

    @OnClick(R.id.layout_zan)
    void addZan() {
        if (!isZaned) {
            isFirstLoad = false;
            addZanPresenterImp.addZan(1, "1021601", messageId, "", "");
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
    public void loadDataSuccess(ResultInfo tData) {

        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

        Logger.i(JSONObject.toJSONString(tData));

        if (tData != null && tData.getCode() == Constants.SUCCESS) {

            Drawable isZan = ContextCompat.getDrawable(this, R.mipmap.is_zan);
            Drawable notZan = ContextCompat.getDrawable(this, R.mipmap.note_zan);

            if (tData instanceof NoteInfoDetailRet) {
                messageId = ((NoteInfoDetailRet) tData).getData().getId();

                commentNum = ((NoteInfoDetailRet) tData).getData().getCommentNum();

                Glide.with(this).load(((NoteInfoDetailRet) tData).getData().getUserimg()).into(mUserHeadImageView);
                mNickNameTextView.setText(((NoteInfoDetailRet) tData).getData().getNickname());
                mTopicNameTextView.setText(((NoteInfoDetailRet) tData).getData().getName());
                mAddDateTextView.setText(TimeUtils.millis2String(((NoteInfoDetailRet) tData).getData().getAddTime() * 1000));
                mNoteContentTextView.setText(((NoteInfoDetailRet) tData).getData().getContent());

                mMessageCountTextView.setText(commentNum > 0 ? commentNum + "" : "");
                mZanCountTextView.setText(((NoteInfoDetailRet) tData).getData().getZanNum() + "");


                if (((NoteInfoDetailRet) tData).getData().getIsZan() == 0) {
                    isZaned = false;
                    mZanCountTextView.setCompoundDrawablesWithIntrinsicBounds(notZan, null, null, null);
                } else {
                    isZaned = true;
                    mZanCountTextView.setCompoundDrawablesWithIntrinsicBounds(isZan, null, null, null);
                }
                mZanCountTextView.setCompoundDrawablePadding(SizeUtils.dp2px(4));

                //设置帖子图片
                List<HeadInfo> headInfos = new ArrayList<>();
                String[] tempImg = ((NoteInfoDetailRet) tData).getData().getImageArr();

                final ArrayList<String> imageUrls = new ArrayList<>();

                for (int i = 0; i < tempImg.length; i++) {
                    HeadInfo headInfo = new HeadInfo();
                    headInfo.setImgurl(tempImg[i]);
                    headInfos.add(headInfo);
                    imageUrls.add(tempImg[i]);
                }

                headInfoAdapter.setNewData(headInfos);
            }

            if (tData instanceof ReplyResultInfoRet) {
                ToastUtils.showLong("回复成功");
                commentNum++;
                mMessageCountTextView.setText(commentNum > 0 ? commentNum + "" : "");

                EventBus.getDefault().post(new MessageEvent("更新精彩评论"));
            }

            if (tData instanceof ResultInfo && !isFirstLoad) {
                mZanCountTextView.setText((((NoteInfoDetailRet) tData).getData().getZanNum() + 1) + "");
                mZanCountTextView.setCompoundDrawablesWithIntrinsicBounds(isZan, null, null, null);
                mZanCountTextView.setCompoundDrawablePadding(SizeUtils.dp2px(4));
            }
        }
    }

    @Override
    public void loadDataError(Throwable throwable) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void sendContent(String content, int type) {

        if (progressDialog != null && !progressDialog.isShowing()) {
            progressDialog.show();
        }

        ReplyParams replyParams = new ReplyParams();
        replyParams.setType(1);
        replyParams.setContent("我是帖子的一级回复");
        replyParams.setRepeatUserId("1021601");
        replyParams.setMessageId("110634");

        replyCommentPresenterImp.addReplyInfo(replyParams);

        if (content != null) {
            if (commentDialog != null) {
                commentDialog.hideProgressDialog();
                commentDialog.dismiss();
            }
        }
    }
}
