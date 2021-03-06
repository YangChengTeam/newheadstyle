package com.feiyou.headstyle.ui.activity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ScreenUtils;
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
import com.feiyou.headstyle.bean.TaskRecordInfoRet;
import com.feiyou.headstyle.bean.UserInfo;
import com.feiyou.headstyle.bean.ZanResultRet;
import com.feiyou.headstyle.common.Constants;
import com.feiyou.headstyle.presenter.AddZanPresenterImp;
import com.feiyou.headstyle.presenter.FollowInfoPresenterImp;
import com.feiyou.headstyle.presenter.NoteInfoDetailDataPresenterImp;
import com.feiyou.headstyle.presenter.RecordInfoPresenterImp;
import com.feiyou.headstyle.presenter.ReplyCommentPresenterImp;
import com.feiyou.headstyle.presenter.TaskRecordInfoPresenterImp;
import com.feiyou.headstyle.ui.adapter.CommunityItemAdapter;
import com.feiyou.headstyle.ui.base.BaseFragmentActivity;
import com.feiyou.headstyle.ui.custom.GlideRoundTransform;
import com.feiyou.headstyle.ui.custom.LoginDialog;
import com.feiyou.headstyle.ui.custom.MyWebView;
import com.feiyou.headstyle.ui.custom.RoundedCornersTransformation;
import com.feiyou.headstyle.ui.fragment.sub.WonderfulFragment;
import com.feiyou.headstyle.view.CommentDialog;
import com.feiyou.headstyle.view.NoteInfoDetailDataView;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.orhanobut.logger.Logger;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

/**
 * Created by myflying on 2018/11/28.
 */
public class CommunityArticleActivity extends BaseFragmentActivity implements NoteInfoDetailDataView, CommentDialog.SendBackListener, View.OnClickListener {

    @BindView(R.id.iv_back)
    ImageView mBackImageView;

    @BindView(R.id.iv_share)
    ImageView mShareImageView;

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

    @BindView(R.id.iv_system_user)
    ImageView mSystemUserIv;

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

    @BindView(R.id.layout_ad)
    LinearLayout mAdLayout;

    @BindView(R.id.iv_ad)
    ImageView mAdImageView;

    List<String> mTitleDataList;

    private String newsId;

    private NoteInfoDetailDataPresenterImp noteInfoDetailDataPresenterImp;

    private AddZanPresenterImp addZanPresenterImp;

    private FollowInfoPresenterImp followInfoPresenterImp;

    private CommunityItemAdapter communityItemAdapter;

    CommentDialog commentDialog;

    private ProgressDialog progressDialog = null;

    private ReplyCommentPresenterImp replyCommentPresenterImp;

    private RecordInfoPresenterImp recordInfoPresenterImp;

    private int commentNum;

    private String messageId;

    private NoteInfo currentNoteInfo;

    private ArrayList<String> imageUrls;

    private boolean isFollow;

    LoginDialog loginDialog;

    MyWebView mWebView;

    BottomSheetDialog commonShareDialog;

    //分享弹窗页面
    View mCommonShareView;

    LinearLayout mWeixinLayout;

    LinearLayout mCircleLayout;

    LinearLayout mQQLayout;

    LinearLayout mQQzoneLayout;

    LinearLayout mReportLayout;

    LinearLayout mBackHomeLayout;

    ImageView mCloseIv;

    private ShareAction shareAction;

    private boolean isFromStick;

    private UserInfo userInfo;

    private boolean isMyNote;

    private String taskId = "3";

    private int goldNum = 0;

    TaskRecordInfoPresenterImp taskRecordInfoPresenterImp;

    private boolean isAddTaskRecord;

    private String recordId;

    private int isFromTask = 0;

    @Override
    protected int getContextViewId() {
        return R.layout.activity_article_detail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        initShareDialog();
        initViews();
    }

    public void initShareDialog() {
        mCommonShareView = LayoutInflater.from(this).inflate(R.layout.common_dialog_view, null);

        mCloseIv = mCommonShareView.findViewById(R.id.iv_close_share);
        mWeixinLayout = mCommonShareView.findViewById(R.id.layout_weixin);
        mCircleLayout = mCommonShareView.findViewById(R.id.layout_circle);
        mQQLayout = mCommonShareView.findViewById(R.id.layout_qq);
        mQQzoneLayout = mCommonShareView.findViewById(R.id.layout_qzone);

        mReportLayout = mCommonShareView.findViewById(R.id.layout_report);
        mBackHomeLayout = mCommonShareView.findViewById(R.id.layout_to_home);

        mCloseIv.setOnClickListener(this);
        mWeixinLayout.setOnClickListener(this);
        mCircleLayout.setOnClickListener(this);
        mQQLayout.setOnClickListener(this);
        mQQzoneLayout.setOnClickListener(this);
        mReportLayout.setOnClickListener(this);
        mBackHomeLayout.setOnClickListener(this);
        commonShareDialog = new BottomSheetDialog(this);
        commonShareDialog.setContentView(mCommonShareView);
    }

    public void initViews() {
        MobclickAgent.onEvent(this, "note_detail", AppUtils.getAppVersionName());

        Bundle bundle = getIntent().getExtras();
        if (bundle != null && !StringUtils.isEmpty(bundle.getString("msg_id"))) {
            messageId = bundle.getString("msg_id");
            isFromTask = bundle.getInt("is_from_task", 0);
        }

        if (bundle != null) {
            isFromStick = bundle.getBoolean("if_from_stick", false);
        }

        userInfo = App.getApp().getmUserInfo();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.layout_fragment, WonderfulFragment.newInstance(messageId));
        transaction.commit();

        QMUIStatusBarHelper.setStatusBarLightMode(this);
        mTopContentLayout.setLayoutParams(new CollapsingToolbarLayout.LayoutParams(CollapsingToolbarLayout.LayoutParams.MATCH_PARENT, CollapsingToolbarLayout.LayoutParams.WRAP_CONTENT));

        communityItemAdapter = new CommunityItemAdapter(this, null, 1);
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
        taskRecordInfoPresenterImp = new TaskRecordInfoPresenterImp(this, this);

        replyCommentPresenterImp = new ReplyCommentPresenterImp(this, this);

        noteInfoDetailDataPresenterImp.getNoteInfoDetailData(userInfo != null ? userInfo.getId() : "", messageId);
        recordInfoPresenterImp = new RecordInfoPresenterImp(this, this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("回复中");

        loginDialog = new LoginDialog(this, R.style.login_dialog);

        if (shareAction == null) {
            shareAction = new ShareAction(this);
            shareAction.setCallback(shareListener);//回调监听器
        }

        if (isFromTask == 1) {
            //当天没有签到
            String openid = App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getOpenid() : "";
            taskRecordInfoPresenterImp.addTaskRecord(App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "", openid, taskId, goldNum, 0, 0, "0");
        }
    }

    public void showDialog() {
        commentDialog = new CommentDialog(this, 1);
        commentDialog.setSendBackListener(this);
        commentDialog.show(getFragmentManager(), "dialog");
    }

    @OnClick(R.id.iv_user_head)
    public void currentUserInfo() {
        if (!App.getApp().isLogin) {
            if (loginDialog != null && !loginDialog.isShowing()) {
                loginDialog.show();
            }
            return;
        }

        if (currentNoteInfo != null) {
            Intent intent = new Intent(this, UserInfoActivity.class);
            intent.putExtra("user_id", currentNoteInfo.getUserId());
            startActivity(intent);
        }
    }

    @OnClick(R.id.layout_follow)
    void addFollow() {
        if (!App.getApp().isLogin) {
            if (loginDialog != null && !loginDialog.isShowing()) {
                loginDialog.show();
            }
            return;
        }
        if (userInfo == null || StringUtils.isEmpty(userInfo.getId())) {
            if (!StringUtils.isEmpty(SPUtils.getInstance().getString(Constants.USER_INFO))) {
                Logger.i(SPUtils.getInstance().getString(Constants.USER_INFO));
                userInfo = JSON.parseObject(SPUtils.getInstance().getString(Constants.USER_INFO), new TypeReference<UserInfo>() {
                });
            }
        }
        followInfoPresenterImp.addFollow(userInfo != null ? userInfo.getId() : "", currentNoteInfo != null ? currentNoteInfo.getUserId() : "");
    }

    @OnClick(R.id.layout_message_count)
    void addComment() {
        if (!App.getApp().isLogin) {
            if (loginDialog != null && !loginDialog.isShowing()) {
                loginDialog.show();
            }
            return;
        }
        if (userInfo == null || StringUtils.isEmpty(userInfo.getId())) {
            if (!StringUtils.isEmpty(SPUtils.getInstance().getString(Constants.USER_INFO))) {
                Logger.i(SPUtils.getInstance().getString(Constants.USER_INFO));
                userInfo = JSON.parseObject(SPUtils.getInstance().getString(Constants.USER_INFO), new TypeReference<UserInfo>() {
                });
            }
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
        if (userInfo == null || StringUtils.isEmpty(userInfo.getId())) {
            if (!StringUtils.isEmpty(SPUtils.getInstance().getString(Constants.USER_INFO))) {
                Logger.i(SPUtils.getInstance().getString(Constants.USER_INFO));
                userInfo = JSON.parseObject(SPUtils.getInstance().getString(Constants.USER_INFO), new TypeReference<UserInfo>() {
                });
            }
        }
        addZanPresenterImp.addZan(1, userInfo != null ? userInfo.getId() : "", currentNoteInfo.getUserId(), messageId, "", "", 1);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(MessageEvent messageEvent) {
        Logger.i("event bus activity --->" + messageEvent.getMessage());
        if (messageEvent.getMessage().equals("friend_ids")) {
            List<String> friendIds = JSON.parseArray(messageEvent.getFriendIds(), String.class);
            List<String> names = JSON.parseArray(messageEvent.getFriendNames(), String.class);
            Logger.i("user names result--->" + messageEvent.getFriendNames());
            commentDialog.setAtUserNames(friendIds, names);
        }
    }

    @OnClick({R.id.iv_share, R.id.layout_note_share})
    void commonShare() {
        if (commonShareDialog != null && !commonShareDialog.isShowing()) {
            commonShareDialog.show();
        }
    }

    public static String getNewContent(String htmltext) {
        try {
            if (!StringUtils.isEmpty(htmltext)) {
                htmltext = htmltext.replace("\n", "<br>");
            }
            Document doc = Jsoup.parse(htmltext);
            Elements elements = doc.getElementsByTag("img");
            for (Element element : elements) {
                element.attr("width", "100%").attr("height", "auto");
            }
            return doc.toString();
        } catch (Exception e) {
            return htmltext;
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

        Drawable isZan = ContextCompat.getDrawable(this, R.mipmap.is_zan);
        Drawable notZan = ContextCompat.getDrawable(this, R.mipmap.note_zan);

        if (tData != null) {
            if (tData instanceof NoteInfoDetailRet) {
                if(tData.getCode() == Constants.SUCCESS) {
                    currentNoteInfo = ((NoteInfoDetailRet) tData).getData();

                    commentNum = currentNoteInfo.getCommentNum();
                    RequestOptions options = new RequestOptions();
                    options.transform(new GlideRoundTransform(this, 21));
                    options.placeholder(R.mipmap.head_def).error(R.mipmap.head_def);
                    Glide.with(this).load(currentNoteInfo.getUserimg()).apply(options).into(mUserHeadImageView);

                    if (currentNoteInfo.getUserId() != null && currentNoteInfo.getUserId().equals("1")) {
                        mSystemUserIv.setVisibility(View.VISIBLE);
                    } else {
                        mSystemUserIv.setVisibility(View.GONE);
                    }

                    String nickName = "火星用户";
                    if (!StringUtils.isEmpty(currentNoteInfo.getNickname())) {
                        nickName = currentNoteInfo.getNickname().replace("\r", "").replace("\n", "");
                    }

                    mNickNameTextView.setText(nickName);
                    mTopicNameTextView.setText(currentNoteInfo.getName());
                    mAddDateTextView.setText(TimeUtils.millis2String(currentNoteInfo.getAddTime() != null ? currentNoteInfo.getAddTime() * 1000 : 0));

                    //设置帖子内容
                    //mNoteContentTextView.setText(Html.fromHtml(currentNoteInfo.getContent()));
                    mWebView = new MyWebView(CommunityArticleActivity.this);
                    mWebView.setScrollbarFadingEnabled(true);
                    mWebView.loadData(getNewContent(currentNoteInfo.getContent()), "text/html; charset=UTF-8", null);
                    mWebViewLayout.addView(mWebView);

                    mMessageCountTextView.setText(commentNum > 0 ? commentNum + "" : "");
                    mZanCountTextView.setText(currentNoteInfo.getZanNum() > 0 ? currentNoteInfo.getZanNum() + "" : "0");

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
                        if (!StringUtils.isEmpty(tempImg[i])) {
                            HeadInfo headInfo = new HeadInfo();
                            headInfo.setImgurl(tempImg[i]);
                            headInfos.add(headInfo);
                            imageUrls.add(tempImg[i]);
                        }
                    }
                    if (headInfos.size() > 0) {
                        mNoteImageListView.setVisibility(View.VISIBLE);
                        int temp = imageUrls.size() % 3 == 0 ? imageUrls.size() / 3 : imageUrls.size() / 3 + 1;
                        mNoteImageListView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(112 * temp)));
                        communityItemAdapter.setNewData(headInfos);
                    } else {
                        mNoteImageListView.setVisibility(View.GONE);
                    }

                    int tempResult = currentNoteInfo.getIsGuan();
                    mFollowLayout.setBackgroundResource(tempResult == 0 ? R.drawable.into_bg : R.drawable.is_follow_bg);
                    mFollowTv.setTextColor(ContextCompat.getColor(this, tempResult == 0 ? R.color.tab_select_color : R.color.black2));
                    mFollowTv.setText(tempResult == 0 ? "+关注" : "已关注");

                    //自己对自己发的贴，隐藏相关操作按钮
                    if (userInfo != null && userInfo.getId().equals(currentNoteInfo.getUserId())) {
                        isMyNote = true;
                        mReportLayout.setVisibility(View.GONE);
                        mFollowLayout.setVisibility(View.GONE);
                        mFollowTv.setVisibility(View.GONE);
                    }
                }
            }

            if (tData instanceof ReplyResultInfoRet) {
                if(tData.getCode() == Constants.SUCCESS) {
                    //ToastUtils.showLong("回复成功");
                    commentNum++;
                    mMessageCountTextView.setText(commentNum > 0 ? commentNum + "" : "");

                    EventBus.getDefault().post(new MessageEvent("更新精彩评论"));

                    if (isAddTaskRecord) {
                        String openid = App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getOpenid() : "";
                        taskRecordInfoPresenterImp.addTaskRecord(App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "", openid, taskId, goldNum, 0, 1, recordId);
                    }
                }
            }

            if (tData instanceof ZanResultRet) {
                if(tData.getCode() == Constants.SUCCESS) {
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
            }

            if (tData instanceof FollowInfoRet) {
                if(tData.getCode() == Constants.SUCCESS) {
                    int tempResult = ((FollowInfoRet) tData).getData().getIsGuan();

                    ToastUtils.showLong(tempResult == 0 ? "已取消" : "已关注");
                    mFollowLayout.setBackgroundResource(tempResult == 0 ? R.drawable.into_bg : R.drawable.is_follow_bg);
                    mFollowTv.setTextColor(ContextCompat.getColor(this, tempResult == 0 ? R.color.tab_select_color : R.color.black2));
                    mFollowTv.setText(tempResult == 0 ? "+关注" : "已关注");
                }
            }

            if (tData instanceof TaskRecordInfoRet) {
                if(tData.getCode() == Constants.SUCCESS) {
                    if (StringUtils.isEmpty(recordId)) {
                        isAddTaskRecord = true;
                        if (((TaskRecordInfoRet) tData).getData() != null) {
                            recordId = ((TaskRecordInfoRet) tData).getData().getInfoid();
                        }
                    } else {
                        if (((TaskRecordInfoRet) tData).getData() != null) {
                            ToastUtils.showLong("领取成功 +" + ((TaskRecordInfoRet) tData).getData().getGoldnum() + "金币");
                        }
                    }
                }
            }

        } else {
            int tempResult = 0;
            mFollowLayout.setBackgroundResource(tempResult == 0 ? R.drawable.into_bg : R.drawable.is_follow_bg);
            mFollowTv.setTextColor(ContextCompat.getColor(this, tempResult == 0 ? R.color.tab_select_color : R.color.black2));
            mFollowTv.setText(tempResult == 0 ? "+关注" : "已关注");
            if (tData instanceof NoteInfoDetailRet) {
                Logger.i(StringUtils.isEmpty(tData.getMsg()) ? "数据加载失败" : tData.getMsg());
            } else {
                Toasty.normal(this, StringUtils.isEmpty(tData.getMsg()) ? "操作失败" : tData.getMsg()).show();
            }
        }

        if (App.getApp().getMessageAdInfo() != null && !StringUtils.isEmpty(App.getApp().getMessageAdInfo().getId())) {
            mAdLayout.setVisibility(View.VISIBLE);

            int tempWidth = ScreenUtils.getScreenWidth() - SizeUtils.dp2px(18);
            RequestOptions options = new RequestOptions().skipMemoryCache(true);
            options.transform(new RoundedCornersTransformation(SizeUtils.dp2px(5), 0));
            options.override(tempWidth, SizeUtils.dp2px(80));

            Glide.with(this).load(App.getApp().getMessageAdInfo().getIco()).apply(options).into(mAdImageView);
        } else {
            mAdLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void loadDataError(Throwable throwable) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void sendContent(String userIds, String content, int type) {

        if (progressDialog != null && !progressDialog.isShowing()) {
            progressDialog.show();
        }

        ReplyParams replyParams = new ReplyParams();
        replyParams.setModelType(1);
        replyParams.setType(1);
        replyParams.setContent(content);
        replyParams.setRepeatUserId(userInfo != null ? userInfo.getId() : "");
        replyParams.setMessageId(messageId);
        replyParams.setAtUserIds(userIds);

        replyCommentPresenterImp.addReplyInfo(replyParams);

        if (content != null) {
            if (commentDialog != null) {
                commentDialog.hideProgressDialog();
                commentDialog.dismiss();
            }
        }
    }

    private UMShareListener shareListener = new UMShareListener() {
        /**
         * @descrption 分享开始的回调
         * @param platform 平台类型
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        /**
         * @descrption 分享成功的回调
         * @param platform 平台类型
         */
        @Override
        public void onResult(SHARE_MEDIA platform) {
            dismissShareView();
            Toast.makeText(CommunityArticleActivity.this, "分享成功", Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            dismissShareView();
            Toast.makeText(CommunityArticleActivity.this, "分享失败", Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            dismissShareView();
            Toast.makeText(CommunityArticleActivity.this, "取消分享", Toast.LENGTH_LONG).show();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View view) {

        String shareContent = currentNoteInfo != null ? StringUtils.isEmpty(currentNoteInfo.getContent()) ? "快来试试炫酷的头像吧" : currentNoteInfo.getContent() : "";
        String shareTitle = "一位神秘人士对你发出邀请";
        if (isFromStick) {
            shareContent = "这里的老哥老姐个个都是人才，说话又好听，我超喜欢这里...";
        }
        UMWeb web = new UMWeb("http://gx.qqtn.com");
        if (shareAction != null) {
            UMImage image = new UMImage(CommunityArticleActivity.this, R.drawable.app_share);
            if (imageUrls != null && imageUrls.size() > 0) {
                image = new UMImage(CommunityArticleActivity.this, imageUrls.get(0));
                image.compressStyle = UMImage.CompressStyle.QUALITY;
            }
            web.setTitle(isFromStick ? shareTitle : shareContent);//标题
            web.setThumb(image);//缩略图
            web.setDescription(shareContent);//描述
        }

        switch (view.getId()) {
            case R.id.iv_close_share:
                dismissShareView();
                break;
            case R.id.layout_weixin:
                shareAction.withMedia(web).setPlatform(SHARE_MEDIA.WEIXIN).share();
                break;
            case R.id.layout_circle:
                shareAction.withMedia(web).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE).share();
                break;
            case R.id.layout_qq:
                shareAction.withMedia(web).setPlatform(SHARE_MEDIA.QQ).share();
                break;
            case R.id.layout_qzone:
                shareAction.withMedia(web).setPlatform(SHARE_MEDIA.QZONE).share();
                break;
            case R.id.layout_report:
                if (!App.getApp().isLogin) {
                    if (loginDialog != null && !loginDialog.isShowing()) {
                        loginDialog.show();
                    }
                    return;
                }

                dismissShareView();
                Intent intent = new Intent(this, ReportInfoActivity.class);
                intent.putExtra("rid", currentNoteInfo != null ? currentNoteInfo.getId() : "");
                intent.putExtra("report_type", 2);
                startActivity(intent);
                break;
            case R.id.layout_to_home:
                Intent intent1 = new Intent(this, MainActivity.class);
                startActivity(intent1);
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 关闭分享窗口
     */
    public void dismissShareView() {
        if (commonShareDialog != null && commonShareDialog.isShowing()) {
            commonShareDialog.dismiss();
        }
    }

    @OnClick(R.id.layout_ad)
    void adClick() {
        if (App.getApp().getMessageAdInfo() != null && !StringUtils.isEmpty(App.getApp().getMessageAdInfo().getId())) {
            addRecord(App.getApp().getMessageAdInfo().getId());
            Intent intent = new Intent(this, AdActivity.class);
            intent.putExtra("open_url", App.getApp().getMessageAdInfo().getJumpPath());
            startActivity(intent);
        }
    }

    public void addRecord(String aid) {
        recordInfoPresenterImp.adClickInfo(App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "", aid);
    }
}
