package com.feiyou.headstyle.ui.activity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feiyou.headstyle.App;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.FollowInfoRet;
import com.feiyou.headstyle.bean.HeadInfo;
import com.feiyou.headstyle.bean.MessageEvent;
import com.feiyou.headstyle.bean.NoteInfo;
import com.feiyou.headstyle.bean.NoteInfoDetailRet;
import com.feiyou.headstyle.bean.ReplyParams;
import com.feiyou.headstyle.bean.ReplyResultInfoRet;
import com.feiyou.headstyle.bean.ResultInfo;
import com.feiyou.headstyle.bean.ZanResultRet;
import com.feiyou.headstyle.common.Constants;
import com.feiyou.headstyle.presenter.AddZanPresenterImp;
import com.feiyou.headstyle.presenter.FollowInfoPresenterImp;
import com.feiyou.headstyle.presenter.NoteInfoDetailDataPresenterImp;
import com.feiyou.headstyle.presenter.ReplyCommentPresenterImp;
import com.feiyou.headstyle.ui.adapter.CommunityHeadAdapter;
import com.feiyou.headstyle.ui.adapter.CommunityItemAdapter;
import com.feiyou.headstyle.ui.base.BaseFragmentActivity;
import com.feiyou.headstyle.ui.custom.GlideRoundTransform;
import com.feiyou.headstyle.ui.custom.LoginDialog;
import com.feiyou.headstyle.ui.custom.MyWebView;
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

    @BindView(R.id.note_img_list)
    RecyclerView mNoteImageListView;

    @BindView(R.id.tv_message_count)
    TextView mMessageCountTextView;

    @BindView(R.id.tv_zan_count)
    TextView mZanCountTextView;

    @BindView(R.id.layout_fragment)
    FrameLayout mFragmentLayout;

    @BindView(R.id.layout_follow)
    FrameLayout mFollowLayout;

    @BindView(R.id.tv_follow_txt)
    TextView mFollowTv;

    @BindView(R.id.layout_web_view)
    LinearLayout mWebViewLayout;

    List<String> mTitleDataList;

    private String newsId;

    private NoteInfoDetailDataPresenterImp noteInfoDetailDataPresenterImp;

    private AddZanPresenterImp addZanPresenterImp;

    private FollowInfoPresenterImp followInfoPresenterImp;

    private CommunityItemAdapter communityItemAdapter;

    CommentDialog commentDialog;

    private ProgressDialog progressDialog = null;

    private ReplyCommentPresenterImp replyCommentPresenterImp;

    private int commentNum;

    private String messageId;

    private NoteInfo currentNoteInfo;

    private ArrayList<String> imageUrls;

    private boolean isFollow;

    LoginDialog loginDialog;

    MyWebView mWebView;

    @Override
    protected int getContextViewId() {
        return R.layout.activity_article_detail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
    }

    public void initViews() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && !StringUtils.isEmpty(bundle.getString("msg_id"))) {
            messageId = bundle.getString("msg_id");
        }

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.layout_fragment, WonderfulFragment.newInstance(messageId));
        transaction.commit();

        QMUIStatusBarHelper.setStatusBarLightMode(this);
        mTopContentLayout.setLayoutParams(new CollapsingToolbarLayout.LayoutParams(CollapsingToolbarLayout.LayoutParams.MATCH_PARENT, CollapsingToolbarLayout.LayoutParams.WRAP_CONTENT));

        communityItemAdapter = new CommunityItemAdapter(this, null);
        mNoteImageListView.setLayoutManager(new GridLayoutManager(this, 3));
        mNoteImageListView.setAdapter(communityItemAdapter);

        communityItemAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (imageUrls != null && imageUrls.size() > 0) {
                    Intent intent = new Intent(CommunityArticleActivity.this, ShowImageListActivity.class);
                    intent.putExtra("image_index", position);
                    intent.putStringArrayListExtra("image_list", imageUrls);
                    CommunityArticleActivity.this.startActivity(intent);
                }
            }
        });

        noteInfoDetailDataPresenterImp = new NoteInfoDetailDataPresenterImp(this, this);
        addZanPresenterImp = new AddZanPresenterImp(this, this);
        followInfoPresenterImp = new FollowInfoPresenterImp(this, this);

        replyCommentPresenterImp = new ReplyCommentPresenterImp(this, this);

        noteInfoDetailDataPresenterImp.getNoteInfoDetailData(App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "", messageId);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("回复中");

        loginDialog = new LoginDialog(this, R.style.login_dialog);
    }

    public void showDialog() {
        commentDialog = new CommentDialog(this, 1);
        commentDialog.setSendBackListener(this);
        commentDialog.show(getFragmentManager(), "dialog");
    }

    @OnClick(R.id.layout_follow)
    void addFollow() {
        if (!App.getApp().isLogin) {
            if (loginDialog != null && !loginDialog.isShowing()) {
                loginDialog.show();
            }
            return;
        }

        followInfoPresenterImp.addFollow(App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "", currentNoteInfo != null ? currentNoteInfo.getUserId() : "");
    }

    @OnClick(R.id.layout_message_count)
    void addComment() {
        if (!App.getApp().isLogin) {
            if (loginDialog != null && !loginDialog.isShowing()) {
                loginDialog.show();
            }
            return;
        }

        showDialog();
    }

    @OnClick(R.id.layout_zan)
    void addZan() {
        if (!App.getApp().isLogin) {
            if (loginDialog != null && !loginDialog.isShowing()) {
                loginDialog.show();
            }
            return;
        }

        addZanPresenterImp.addZan(1, App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "", currentNoteInfo.getUserId(), messageId, "", "", 1);
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

        Drawable isZan = ContextCompat.getDrawable(this, R.mipmap.is_zan);
        Drawable notZan = ContextCompat.getDrawable(this, R.mipmap.note_zan);

        if (tData != null && tData.getCode() == Constants.SUCCESS) {

            if (tData instanceof NoteInfoDetailRet) {
                currentNoteInfo = ((NoteInfoDetailRet) tData).getData();

                commentNum = currentNoteInfo.getCommentNum();
                RequestOptions options = new RequestOptions();
                options.transform(new GlideRoundTransform(this, 21));
                options.placeholder(R.mipmap.empty_icon).error(R.mipmap.empty_icon);
                Glide.with(this).load(currentNoteInfo.getUserimg()).apply(options).into(mUserHeadImageView);

                mNickNameTextView.setText(currentNoteInfo.getNickname());
                mTopicNameTextView.setText(currentNoteInfo.getName());
                mAddDateTextView.setText(TimeUtils.millis2String(currentNoteInfo.getAddTime() != null ? currentNoteInfo.getAddTime() * 1000 : 0));

                //设置帖子内容
                //mNoteContentTextView.setText(Html.fromHtml(currentNoteInfo.getContent()));
                mWebView = new MyWebView(CommunityArticleActivity.this);
                mWebView.setScrollbarFadingEnabled(true);
                mWebView.loadData(Html.fromHtml(currentNoteInfo.getContent()).toString(), "text/html; charset=UTF-8", null);
                mWebViewLayout.addView(mWebView);

                mMessageCountTextView.setText(commentNum > 0 ? commentNum + "" : "");
                mZanCountTextView.setText(currentNoteInfo.getZanNum() + "");


                if (currentNoteInfo.getIsZan() == 0) {
                    mZanCountTextView.setCompoundDrawablesWithIntrinsicBounds(notZan, null, null, null);
                } else {
                    mZanCountTextView.setCompoundDrawablesWithIntrinsicBounds(isZan, null, null, null);
                }
                mZanCountTextView.setCompoundDrawablePadding(SizeUtils.dp2px(4));

                //设置帖子图片
                List<HeadInfo> headInfos = new ArrayList<>();
                String[] tempImg = currentNoteInfo.getImageArr();

                imageUrls = new ArrayList<>();

                for (int i = 0; i < tempImg.length; i++) {
                    HeadInfo headInfo = new HeadInfo();
                    headInfo.setImgurl(tempImg[i]);
                    headInfos.add(headInfo);
                    imageUrls.add(tempImg[i]);
                }
                int temp = imageUrls.size() % 3 == 0 ? imageUrls.size() / 3 : imageUrls.size() / 3 + 1;
                mNoteImageListView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(112 * temp)));
                communityItemAdapter.setNewData(headInfos);

                int tempResult = currentNoteInfo.getIsGuan();

                mFollowLayout.setBackgroundResource(tempResult == 0 ? R.drawable.into_bg : R.drawable.is_follow_bg);
                mFollowTv.setTextColor(ContextCompat.getColor(this, tempResult == 0 ? R.color.tab_select_color : R.color.black2));
                mFollowTv.setText(tempResult == 0 ? "+关注" : "已关注");
            }

            if (tData instanceof ReplyResultInfoRet) {
                ToastUtils.showLong("回复成功");
                commentNum++;
                mMessageCountTextView.setText(commentNum > 0 ? commentNum + "" : "");

                EventBus.getDefault().post(new MessageEvent("更新精彩评论"));
            }

            if (tData instanceof ZanResultRet) {
                if (((ZanResultRet) tData).getData().getIsZan() == 0) {
                    mZanCountTextView.setText((((ZanResultRet) tData).getData().getZanNum()) + "");
                    mZanCountTextView.setCompoundDrawablesWithIntrinsicBounds(notZan, null, null, null);
                    mZanCountTextView.setCompoundDrawablePadding(SizeUtils.dp2px(4));
                } else {
                    mZanCountTextView.setText((((ZanResultRet) tData).getData().getZanNum()) + "");
                    mZanCountTextView.setCompoundDrawablesWithIntrinsicBounds(isZan, null, null, null);
                    mZanCountTextView.setCompoundDrawablePadding(SizeUtils.dp2px(4));
                }
            }

            if (tData instanceof FollowInfoRet) {
                int tempResult = ((FollowInfoRet) tData).getData().getIsGuan();

                ToastUtils.showLong(tempResult == 0 ? "已取消" : "已关注");
                mFollowLayout.setBackgroundResource(tempResult == 0 ? R.drawable.into_bg : R.drawable.is_follow_bg);
                mFollowTv.setTextColor(ContextCompat.getColor(this, tempResult == 0 ? R.color.tab_select_color : R.color.black2));
                mFollowTv.setText(tempResult == 0 ? "+关注" : "已关注");
            }

        } else {
            int tempResult = 0;
            mFollowLayout.setBackgroundResource(tempResult == 0 ? R.drawable.into_bg : R.drawable.is_follow_bg);
            mFollowTv.setTextColor(ContextCompat.getColor(this, tempResult == 0 ? R.color.tab_select_color : R.color.black2));
            mFollowTv.setText(tempResult == 0 ? "+关注" : "已关注");
            if (tData instanceof NoteInfoDetailRet) {
                Logger.i(StringUtils.isEmpty(tData.getMsg()) ? "操作失败" : tData.getMsg());
            } else {
                ToastUtils.showLong(StringUtils.isEmpty(tData.getMsg()) ? "操作失败" : tData.getMsg());
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
        replyParams.setModelType(1);
        replyParams.setType(1);
        replyParams.setContent(content);
        replyParams.setRepeatUserId(App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "");
        replyParams.setMessageId(messageId);

        replyCommentPresenterImp.addReplyInfo(replyParams);

        if (content != null) {
            if (commentDialog != null) {
                commentDialog.hideProgressDialog();
                commentDialog.dismiss();
            }
        }
    }
}
