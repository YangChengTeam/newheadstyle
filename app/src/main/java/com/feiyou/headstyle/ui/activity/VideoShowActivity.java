package com.feiyou.headstyle.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.dingmouren.layoutmanagergroup.viewpager.OnViewPagerListener;
import com.dingmouren.layoutmanagergroup.viewpager.ViewPagerLayoutManager;
import com.feiyou.headstyle.App;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.AddCollectionRet;
import com.feiyou.headstyle.bean.FollowInfoRet;
import com.feiyou.headstyle.bean.MessageEvent;
import com.feiyou.headstyle.bean.NoteCommentRet;
import com.feiyou.headstyle.bean.NoteItem;
import com.feiyou.headstyle.bean.NoteSubCommentRet;
import com.feiyou.headstyle.bean.ReplyParams;
import com.feiyou.headstyle.bean.ReplyResultInfoRet;
import com.feiyou.headstyle.bean.ResultInfo;
import com.feiyou.headstyle.bean.UserInfo;
import com.feiyou.headstyle.bean.VideoCommentRet;
import com.feiyou.headstyle.bean.VideoInfoRet;
import com.feiyou.headstyle.bean.ZanResultRet;
import com.feiyou.headstyle.common.Constants;
import com.feiyou.headstyle.presenter.AddCollectionPresenterImp;
import com.feiyou.headstyle.presenter.AddZanPresenterImp;
import com.feiyou.headstyle.presenter.FollowInfoPresenterImp;
import com.feiyou.headstyle.presenter.NoteSubCommentDataPresenterImp;
import com.feiyou.headstyle.presenter.ReplyCommentPresenterImp;
import com.feiyou.headstyle.presenter.VideoCommentPresenterImp;
import com.feiyou.headstyle.presenter.VideoInfoPresenterImp;
import com.feiyou.headstyle.ui.adapter.CommentAdapter;
import com.feiyou.headstyle.ui.adapter.CommentReplyAdapter;
import com.feiyou.headstyle.ui.adapter.VideoItemAdapter;
import com.feiyou.headstyle.ui.base.BaseFragmentActivity;
import com.feiyou.headstyle.ui.custom.GlideRoundTransform;
import com.feiyou.headstyle.ui.custom.LoginDialog;
import com.feiyou.headstyle.view.CommentDialog;
import com.feiyou.headstyle.view.VideoInfoView;
import com.orhanobut.logger.Logger;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.wang.avi.AVLoadingIndicatorView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

/**
 * Created by myflying on 2018/11/23.
 */
public class VideoShowActivity extends BaseFragmentActivity implements VideoInfoView, CommentDialog.SendBackListener, View.OnClickListener {

    private static final String TAG = "VideoShowActivity";

    @BindView(R.id.layout_video_show)
    FrameLayout mVideoShowLayout;

    AVLoadingIndicatorView avi;

    @BindView(R.id.recycler)
    RecyclerView mVideoListView;

    LinearLayout mNoDataLayout;

    ImageView mCloseComment;

    private VideoItemAdapter mVideoAdapter;

    private ViewPagerLayoutManager mLayoutManager;

    private VideoInfoPresenterImp videoInfoPresenterImp;

    private VideoCommentPresenterImp videoCommentPresenterImp;

    private NoteSubCommentDataPresenterImp noteSubCommentDataPresenterImp;

    private int pageSize = 30;

    private int startPosition;

    private boolean isFirstLoad = true;

    private String currentVideoId;

    PopupWindow popupWindow;

    private View commentView;

    private RecyclerView commentListView;

    BottomSheetDialog commentDialog;

    private CommentAdapter commentAdapter;

    private View replyView;

    ImageView topUserHeadImageView;

    TextView nickNameTv;

    TextView addDateTv;

    TextView commentContentTv;

    TextView zanCountTv;

    private RecyclerView replyListView;

    BottomSheetDialog commitReplyDialog;

    private CommentReplyAdapter commentReplyAdapter;

    CommentDialog inputDialog;

    private ReplyCommentPresenterImp replyCommentPresenterImp;

    private Drawable isZan;

    private Drawable notZan;

    private int switchType;

    private String commentId;

    private String repeatId;

    private String repeatCommentUserId;

    private int currentCommentPos = -1;

    private int currentReplyPos = -1;

    private AddZanPresenterImp addZanPresenterImp;

    private FollowInfoPresenterImp followInfoPresenterImp;

    private AddCollectionPresenterImp addCollectionPresenterImp;

    LoginDialog loginDialog;

    private int videoPage = 1;

    private int commentPage = 1;

    private int subCommentPage = 1;

    private UserInfo userInfo;

    private int selectVideoIndex;

    private String replyTopUserId;

    FrameLayout replyFollowLayout;

    TextView mReplyFollowTv;

    LinearLayout replyZanLayout;

    BottomSheetDialog shareDialog;

    private ShareAction shareAction;

    @Override
    protected int getContextViewId() {
        return R.layout.activity_video_show;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        initView();
        initCommentPop();
        initReplyView();
        initListener();
    }

    private void initView() {

        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.getInt("jump_page") > 0) {
            videoPage = bundle.getInt("jump_page");
        }

        if (bundle != null && bundle.getInt("jump_position") > 0) {
            startPosition = bundle.getInt("jump_position");
        }

        userInfo = App.getApp().getmUserInfo();

        loginDialog = new LoginDialog(this, R.style.login_dialog);

        if (shareAction == null) {
            shareAction = new ShareAction(this);
            shareAction.setCallback(shareListener);//回调监听器
        }
        //初始化分享弹窗
        shareDialog = new BottomSheetDialog(this);
        View shareView = LayoutInflater.from(this).inflate(R.layout.common_dialog_view, null);
        ImageView closeDialog = shareView.findViewById(R.id.iv_close_share);
        LinearLayout weixinLayout = shareView.findViewById(R.id.layout_weixin);
        LinearLayout circleLayout = shareView.findViewById(R.id.layout_circle);
        LinearLayout qqLayout = shareView.findViewById(R.id.layout_qq);
        LinearLayout qzoneLayout = shareView.findViewById(R.id.layout_qzone);
        LinearLayout mReportLayout = shareView.findViewById(R.id.layout_report);
        LinearLayout mBackHomeLayout = shareView.findViewById(R.id.layout_to_home);

        weixinLayout.setOnClickListener(this);
        circleLayout.setOnClickListener(this);
        qqLayout.setOnClickListener(this);
        qzoneLayout.setOnClickListener(this);
        closeDialog.setOnClickListener(this);
        mReportLayout.setOnClickListener(this);
        mBackHomeLayout.setOnClickListener(this);
        shareDialog.setContentView(shareView);

        isZan = ContextCompat.getDrawable(this, R.mipmap.is_zan);
        notZan = ContextCompat.getDrawable(this, R.mipmap.note_zan);

        mLayoutManager = new ViewPagerLayoutManager(this, OrientationHelper.VERTICAL);
        mVideoAdapter = new VideoItemAdapter(this, null);
        mVideoListView.setLayoutManager(mLayoutManager);
        mVideoListView.setAdapter(mVideoAdapter);

        addCollectionPresenterImp = new AddCollectionPresenterImp(this, this);
        videoInfoPresenterImp = new VideoInfoPresenterImp(this, this);
        videoCommentPresenterImp = new VideoCommentPresenterImp(this, this);
        replyCommentPresenterImp = new ReplyCommentPresenterImp(this, this);
        noteSubCommentDataPresenterImp = new NoteSubCommentDataPresenterImp(this, this);
        addZanPresenterImp = new AddZanPresenterImp(this, this);
        followInfoPresenterImp = new FollowInfoPresenterImp(this, this);

        videoInfoPresenterImp.getDataList(videoPage);
        mVideoAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                videoPage++;
                videoInfoPresenterImp.getDataList(videoPage);
            }
        }, mVideoListView);

        mVideoAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (!App.getApp().isLogin) {
                    if (loginDialog != null && !loginDialog.isShowing()) {
                        loginDialog.show();
                    }
                    return;
                }

                selectVideoIndex = position;
                if (view.getId() == R.id.tv_comment_num || view.getId() == R.id.et_video_item) {
                    currentVideoId = mVideoAdapter.getData().get(position).getId();
                    videoCommentPresenterImp.getCommentList(commentPage, currentVideoId, userInfo != null ? userInfo.getId() : "");
                    if (popupWindow != null && !popupWindow.isShowing()) {
                        avi.show();
                        // 设置popupWindow的显示位置，此处是在手机屏幕底部且水平居中的位置
                        popupWindow.showAtLocation(mVideoShowLayout, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                        //TODO, 此处缺少字段，用户是否对视频点赞 getIsCollect 暂时代替
                    }

                }

                if (view.getId() == R.id.btn_is_follow) {
                    followInfoPresenterImp.addFollow(App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "", mVideoAdapter.getData().get(position).getUserId());
                }

                //收藏
                if (view.getId() == R.id.tv_collect_num) {
                    addCollectionPresenterImp.addVideoCollection(App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "", mVideoAdapter.getData().get(position).getId());
                }

                if (view.getId() == R.id.layout_share) {
                    if (shareDialog != null && !shareDialog.isShowing()) {
                        shareDialog.show();
                    }
                }

                if (view.getId() == R.id.iv_user_head) {
                    Intent intent = new Intent(VideoShowActivity.this, UserInfoActivity.class);
                    intent.putExtra("user_id", mVideoAdapter.getData().get(selectVideoIndex).getUserId());
                    startActivity(intent);
                }
            }
        });
    }

    //评论窗口
    public void initCommentPop() {
        commentView = LayoutInflater.from(this).inflate(R.layout.video_comment_dialog, null);
        avi = commentView.findViewById(R.id.avi);
        mNoDataLayout = commentView.findViewById(R.id.layout_no_data);
        mCloseComment = commentView.findViewById(R.id.iv_close);
        commentListView = commentView.findViewById(R.id.video_comment_list);

        commentAdapter = new CommentAdapter(this, null);
        commentListView.setLayoutManager(new LinearLayoutManager(this));
        commentListView.setAdapter(commentAdapter);

        mCloseComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
            }
        });

        EditText etPopInput = commentView.findViewById(R.id.et_pop_input);
        etPopInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchType = 1;
                showInputDialog();
            }
        });

        commentAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                commentPage++;
                videoCommentPresenterImp.getCommentList(commentPage, currentVideoId, userInfo != null ? userInfo.getId() : "");
            }
        }, commentListView);

        commentAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                if (!App.getApp().isLogin) {
                    if (loginDialog != null && !loginDialog.isShowing()) {
                        loginDialog.show();
                    }
                    return;
                }

                currentCommentPos = position;
                commentId = commentAdapter.getData().get(currentCommentPos).getCommentId();

                if (view.getId() == R.id.btn_reply_count) {
                    if (commitReplyDialog != null && !commitReplyDialog.isShowing()) {
                        commitReplyDialog.show();
                    }
                    //设置头部信息
                    NoteItem noteItem = commentAdapter.getData().get(position);

                    replyTopUserId = noteItem.getUserId();

                    RequestOptions options = new RequestOptions();
                    options.transform(new GlideRoundTransform(VideoShowActivity.this, 21));
                    options.error(R.mipmap.head_def);
                    options.placeholder(R.mipmap.head_def);
                    Glide.with(VideoShowActivity.this).load(noteItem.getCommentUserimg()).apply(options).into(topUserHeadImageView);
                    nickNameTv.setText(noteItem.getCommentNickname());
                    addDateTv.setText(TimeUtils.millis2String(noteItem.getAddTime() != null ? noteItem.getAddTime() * 1000 : 0));
                    commentContentTv.setText(Html.fromHtml(noteItem.getCommentContent()));
                    zanCountTv.setText(noteItem.getZanNum() + "");
                    if (noteItem.getIsZan() == 0) {
                        zanCountTv.setCompoundDrawablesWithIntrinsicBounds(notZan, null, null, null);
                    } else {
                        zanCountTv.setCompoundDrawablesWithIntrinsicBounds(isZan, null, null, null);
                    }
                    zanCountTv.setCompoundDrawablePadding(SizeUtils.dp2px(4));

                    noteSubCommentDataPresenterImp.getNoteSubCommentData(subCommentPage, userInfo != null ? userInfo.getId() : "", commentId, 2);
                }

                if (view.getId() == R.id.layout_zan) {
                    switchType = 2;
                    addZanPresenterImp.addZan(2, userInfo != null ? userInfo.getId() : "", "", "", commentId, "", 2);
                }
            }
        });

        //用于PopupWindow的View
        popupWindow = new PopupWindow(commentView, ScreenUtils.getScreenWidth(), SizeUtils.dp2px(420), true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.setFocusable(false);
        popupWindow.setOutsideTouchable(true);
    }

    public void initReplyView() {
        commitReplyDialog = new BottomSheetDialog(this);

        replyView = LayoutInflater.from(this).inflate(R.layout.comment_reply_view, null);
        replyListView = replyView.findViewById(R.id.rv_reply_list);

        LinearLayout contentLayout = replyView.findViewById(R.id.layout_content);
        LinearLayout bottomLayout = replyView.findViewById(R.id.layout_reply_bottom);
        replyFollowLayout = replyView.findViewById(R.id.reply_is_follow);

        replyZanLayout = replyView.findViewById(R.id.layout_comment_zan);
        zanCountTv = replyView.findViewById(R.id.tv_zan_count);
        ImageView replyClose = replyView.findViewById(R.id.iv_close);
        replyClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (commitReplyDialog != null && commitReplyDialog.isShowing()) {
                    commitReplyDialog.dismiss();
                }
            }
        });
        LinearLayout addMessageLayout = replyView.findViewById(R.id.layout_add_message);
        addMessageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchType = 2;
                showInputDialog();
            }
        });

        replyZanLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchType = 1;
                addZanPresenterImp.addZan(2, App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "", commentAdapter.getData().get(currentCommentPos).getUserId(), "", commentId, "", 2);
            }
        });

        replyFollowLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchType = 1;

                if (!App.getApp().isLogin) {
                    if (loginDialog != null && !loginDialog.isShowing()) {
                        loginDialog.show();
                    }
                    return;
                }

                followInfoPresenterImp.addFollow(App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "", StringUtils.isEmpty(replyTopUserId) ? "" : replyTopUserId);
            }
        });

        //顶部个人的回复信息
        topUserHeadImageView = replyView.findViewById(R.id.iv_user_head);
        nickNameTv = replyView.findViewById(R.id.tv_user_nick_name);
        addDateTv = replyView.findViewById(R.id.tv_add_date);
        commentContentTv = replyView.findViewById(R.id.tv_content);

        RelativeLayout.LayoutParams contentParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        int contentMarginHeight = BarUtils.getNavBarHeight() + SizeUtils.dp2px(62);
        contentParams.setMargins(0, SizeUtils.dp2px(49), 0, contentMarginHeight);
        contentLayout.setLayoutParams(contentParams);

        RelativeLayout.LayoutParams bottomParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(49));
        //int tempHeight = ScreenUtils.getScreenHeight() - BarUtils.getNavBarHeight() - BarUtils.getStatusBarHeight() - SizeUtils.dp2px(49);
        bottomParams.setMargins(0, 0, 0, BarUtils.getNavBarHeight() - BarUtils.getStatusBarHeight());
        bottomParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        bottomLayout.setLayoutParams(bottomParams);

        replyView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ScreenUtils.getScreenHeight()));
        commitReplyDialog.setContentView(replyView);

        //setPeekHeight,设置弹出窗口的高度为全屏的状态.
        BottomSheetBehavior mBehavior = BottomSheetBehavior.from((View) replyView.getParent());
        mBehavior.setPeekHeight(ScreenUtils.getScreenHeight() - BarUtils.getStatusBarHeight());

        commentReplyAdapter = new CommentReplyAdapter(this, null);
        replyListView.setLayoutManager(new LinearLayoutManager(this));
        replyListView.setAdapter(commentReplyAdapter);

        commentReplyAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                switchType = 3;
                currentReplyPos = position;
                showInputDialog();
            }
        });

        commentReplyAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                if (!App.getApp().isLogin) {
                    if (loginDialog != null && !loginDialog.isShowing()) {
                        loginDialog.show();
                    }
                    return;
                }

                switchType = 3;
                currentReplyPos = position;
                if (view.getId() == R.id.layout_zan) {
                    repeatId = commentReplyAdapter.getData().get(position).getRepeatId();
                    addZanPresenterImp.addZan(3, userInfo != null ? userInfo.getId() : "", "", "", "", repeatId, 2);
                }

                if (view.getId() == R.id.btn_reply_count) {
                    switchType = 3;
                    showInputDialog();
                }
            }
        });

    }

    public void showInputDialog() {
        inputDialog = new CommentDialog(this, 1);
        inputDialog.setSendBackListener(this);
        inputDialog.show(getFragmentManager(), "dialog");
    }

    private void initListener() {
        mLayoutManager.setOnViewPagerListener(new OnViewPagerListener() {

            @Override
            public void onInitComplete() {
                playVideo(0);
            }

            @Override
            public void onPageRelease(boolean isNext, int position) {
                Log.e(TAG, "释放位置:" + position + " 下一页:" + isNext);
                int index = 0;
                if (isNext) {
                    index = 0;
                } else {
                    index = 1;
                }
                releaseVideo(index);
            }

            @Override
            public void onPageSelected(int position, boolean isBottom) {
                Log.e(TAG, "选中位置:" + position + "  是否是滑动到底部:" + isBottom);
                playVideo(0);
            }

            public void onLayoutComplete() {
                playVideo(0);
            }

        });
    }

    private void playVideo(int position) {
        View itemView = mVideoListView.getChildAt(0);
        final VideoView videoView = itemView.findViewById(R.id.video_view);
        final ImageView imgThumb = itemView.findViewById(R.id.img_thumb);
        final RelativeLayout rootView = itemView.findViewById(R.id.root_view);
        final MediaPlayer[] mediaPlayer = new MediaPlayer[1];
        videoView.start();
        videoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                mediaPlayer[0] = mp;
                Log.e(TAG, "onInfo");
                mp.setLooping(true);
                imgThumb.animate().alpha(0).setDuration(200).start();
                return false;
            }
        });

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                Log.e(TAG, "onPrepared");
            }
        });
    }

    private void releaseVideo(int index) {
        View itemView = mVideoListView.getChildAt(index);
        if (itemView != null) {
            VideoView videoView = itemView.findViewById(R.id.video_view);
            ImageView imgThumb = itemView.findViewById(R.id.img_thumb);
            videoView.stopPlayback();
            imgThumb.animate().alpha(1).start();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(MessageEvent messageEvent) {
        if (messageEvent.getMessage().equals("friend_ids")) {
            if (messageEvent.getMessage().equals("friend_ids")) {
                List<String> friendIds = JSON.parseArray(messageEvent.getFriendIds(), String.class);
                List<String> names = JSON.parseArray(messageEvent.getFriendNames(), String.class);
                Logger.i("user names result--->" + messageEvent.getFriendNames());
                inputDialog.setAtUserNames(friendIds, names);
            }
        }
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void dismissProgress() {

    }

    @Override
    public void loadDataSuccess(ResultInfo tData) {
        Logger.i("data--->" + JSONObject.toJSONString(tData));

        if (avi != null) {
            avi.hide();
        }

        if (tData != null) {
            if (tData instanceof VideoInfoRet) {
                if (tData.getCode() == Constants.SUCCESS && ((VideoInfoRet) tData).getData().getList() != null) {
                    if (isFirstLoad) {
                        if (startPosition < ((VideoInfoRet) tData).getData().getList().size()) {
                            mVideoAdapter.setNewData(((VideoInfoRet) tData).getData().getList().subList(startPosition, ((VideoInfoRet) tData).getData().getList().size()));
                        } else {
                            mVideoAdapter.setNewData(((VideoInfoRet) tData).getData().getList());
                        }
                        isFirstLoad = false;
                    } else {
                        mVideoAdapter.addData(((VideoInfoRet) tData).getData().getList());
                    }

                    if (((VideoInfoRet) tData).getData().getList().size() == pageSize) {
                        mVideoAdapter.loadMoreComplete();
                    } else {
                        mVideoAdapter.loadMoreEnd(true);
                    }
                }
            }

            if (tData instanceof VideoCommentRet) {
                if (tData.getCode() == Constants.SUCCESS && ((VideoCommentRet) tData).getData() != null) {
                    mNoDataLayout.setVisibility(View.INVISIBLE);
                    commentListView.setVisibility(View.VISIBLE);

                    if (commentPage == 1) {
                        commentAdapter.setNewData(((VideoCommentRet) tData).getData());
                    } else {
                        commentAdapter.addData(((VideoCommentRet) tData).getData());
                    }

                    commentAdapter.notifyDataSetChanged();
                } else {
                    mNoDataLayout.setVisibility(View.VISIBLE);
                    commentListView.setVisibility(View.INVISIBLE);
                }

                if (((VideoCommentRet) tData).getData().size() == pageSize) {
                    commentAdapter.loadMoreComplete();
                } else {
                    commentAdapter.loadMoreEnd(true);
                }
            }

            if (tData instanceof NoteSubCommentRet) {
                if (tData.getCode() == Constants.SUCCESS && ((NoteSubCommentRet) tData).getData() != null) {
                    if (subCommentPage == 1) {
                        commentReplyAdapter.setNewData(((NoteSubCommentRet) tData).getData());
                    } else {
                        commentReplyAdapter.addData(((NoteSubCommentRet) tData).getData());
                    }

                    if (((NoteSubCommentRet) tData).getData().size() == pageSize) {
                        commentReplyAdapter.loadMoreComplete();
                    } else {
                        commentReplyAdapter.loadMoreEnd(true);
                    }
                }
            }

            if (tData instanceof ReplyResultInfoRet) {
                if (tData.getCode() == Constants.SUCCESS) {
                    if (switchType == 1) {
                        videoCommentPresenterImp.getCommentList(commentPage, currentVideoId, userInfo != null ? userInfo.getId() : "");
                    }
                    if (switchType == 2 || switchType == 3) {
                        commentReplyAdapter.addData(0, ((ReplyResultInfoRet) tData).getData());
                        commentReplyAdapter.notifyDataSetChanged();
                    }
                } else {
                    ToastUtils.showLong(StringUtils.isEmpty(tData.getMsg()) ? "回复失败" : tData.getMsg());
                }
            }

            if (tData instanceof ZanResultRet) {

                if (switchType == 1) {
                    int tempNum = ((ZanResultRet) tData).getData().getZanNum();
                }

                if (switchType == 2) {
                    int tempNum = commentAdapter.getData().get(currentCommentPos).getZanNum();
                    if (((ZanResultRet) tData).getData().getIsZan() == 0) {
                        tempNum = tempNum - 1 < 0 ? 0 : tempNum - 1;
                    } else {
                        tempNum = tempNum + 1;
                    }
                    commentAdapter.getData().get(currentCommentPos).setZanNum(tempNum);
                    commentAdapter.getData().get(currentCommentPos).setIsZan(((ZanResultRet) tData).getData().getIsZan());
                    commentAdapter.notifyDataSetChanged();
                }

                if (switchType == 3) {
                    int tempNum = commentReplyAdapter.getData().get(currentReplyPos).getZanNum();
                    if (((ZanResultRet) tData).getData().getIsZan() == 0) {
                        tempNum = tempNum - 1 < 0 ? 0 : tempNum - 1;
                    } else {
                        tempNum = tempNum + 1;
                    }
                    commentReplyAdapter.getData().get(currentReplyPos).setZanNum(tempNum);
                    commentReplyAdapter.getData().get(currentReplyPos).setIsZan(((ZanResultRet) tData).getData().getIsZan());
                    commentReplyAdapter.notifyDataSetChanged();
                }
            }

            if (tData instanceof FollowInfoRet) {
                if (tData.getCode() == Constants.SUCCESS) {
                    int tempResult = ((FollowInfoRet) tData).getData().getIsGuan();
                    if (commitReplyDialog != null && commitReplyDialog.isShowing()) {
                        replyFollowLayout.setBackgroundResource(tempResult == 0 ? R.drawable.into_bg : R.drawable.is_follow_bg);
                        mReplyFollowTv.setTextColor(ContextCompat.getColor(this, tempResult == 0 ? R.color.tab_select_color : R.color.black2));
                        mReplyFollowTv.setText(tempResult == 0 ? "+关注" : "已关注");
                    } else {
                        ToastUtils.showLong(tempResult == 0 ? "已取消" : "已关注");
                        mVideoAdapter.getData().get(selectVideoIndex).setIsGuan(tempResult);
                        mVideoAdapter.notifyDataSetChanged();
                    }
                } else {
                    Toasty.normal(this, StringUtils.isEmpty(tData.getMsg()) ? "回复失败" : tData.getMsg()).show();
                }
            }

            if (tData instanceof AddCollectionRet) {

                int tempNum = mVideoAdapter.getData().get(selectVideoIndex).getCollectNum();
                if (((AddCollectionRet) tData).getData().getIsCollect() == 0) {
                    Toasty.normal(this, "已取消").show();
                    tempNum = tempNum - 1;
                } else {
                    Toasty.normal(this, "已收藏").show();
                    tempNum = tempNum + 1;
                }

                mVideoAdapter.getData().get(selectVideoIndex).setCollectNum(tempNum < 0 ? 0 : tempNum);
                mVideoAdapter.getData().get(selectVideoIndex).setIsCollect(((AddCollectionRet) tData).getData().getIsCollect());
                mVideoAdapter.notifyItemChanged(selectVideoIndex);

                if (((AddCollectionRet) tData).getData().getIsCollect() == 0) {
                    Toasty.normal(this, "已取消").show();
                } else {
                    Toasty.normal(this, "已收藏").show();
                }
            }
        }
    }

    @Override
    public void loadDataError(Throwable throwable) {

    }

    @OnClick(R.id.iv_back)
    void back() {
        popBackStack();
    }

    @Override
    public void onBackPressed() {
        popBackStack();
    }

    @Override
    public void sendContent(String userIds, String content, int type) {
        //关闭软键盘
        KeyboardUtils.hideSoftInput(commentView);

        ReplyParams replyParams = new ReplyParams();
        if (switchType == 1) {
            replyParams.setModelType(2);
            replyParams.setType(1);
            replyParams.setContent(content);
            replyParams.setRepeatUserId(userInfo != null ? userInfo.getId() : "");
            replyParams.setMessageId(currentVideoId);
            replyParams.setAtUserIds(userIds);
        }
        if (switchType == 2) {
            commentId = commentAdapter.getData().get(currentCommentPos).getCommentId();
            repeatCommentUserId = commentAdapter.getData().get(currentCommentPos).getUserId();

            replyParams.setModelType(2);
            replyParams.setType(2);
            replyParams.setContent(content);
            replyParams.setRepeatUserId(userInfo != null ? userInfo.getId() : "");
            replyParams.setCommentId(commentId);
            replyParams.setRepeatCommentUserId(repeatCommentUserId);
            replyParams.setAtUserIds(userIds);
        }

        if (switchType == 3) {
            repeatId = commentReplyAdapter.getData().get(currentReplyPos).getRepeatId();
            repeatCommentUserId = commentReplyAdapter.getData().get(currentReplyPos).getOldUserId();

            replyParams.setModelType(2);
            replyParams.setType(3);
            replyParams.setContent(content);
            replyParams.setRepeatUserId(userInfo != null ? userInfo.getId() : "");
            replyParams.setRepeatId(repeatId);
            replyParams.setRepeatCommentUserId(repeatCommentUserId);
            replyParams.setAtUserIds(userIds);
        }

        replyCommentPresenterImp.addReplyInfo(replyParams);

        if (content != null) {
            if (inputDialog != null) {
                inputDialog.hideProgressDialog();
                inputDialog.dismiss();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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
            Toast.makeText(VideoShowActivity.this, "分享成功", Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            dismissShareView();
            Toast.makeText(VideoShowActivity.this, "分享失败", Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            dismissShareView();
            Toast.makeText(VideoShowActivity.this, "取消分享", Toast.LENGTH_LONG).show();
        }
    };


    @Override
    public void onClick(View view) {
        String shareTitle = "这个搞笑沙雕视频，99%的人看了都笑了";
        String shareContent = "超级搞笑的沙雕视频集锦来了，你能忍住不笑，算你牛！";
        String shareImageUrl = "";

        if (mVideoAdapter.getData().get(selectVideoIndex) != null) {
            shareTitle = mVideoAdapter.getData().get(selectVideoIndex).getName();
            shareContent = mVideoAdapter.getData().get(selectVideoIndex).getName();
            shareImageUrl = mVideoAdapter.getData().get(selectVideoIndex).getVideoCover();
        }

        UMWeb web = new UMWeb("http://gx.qqtn.com");
        if (shareAction != null) {
            UMImage image = new UMImage(VideoShowActivity.this, R.drawable.app_share);
            if (!StringUtils.isEmpty(shareImageUrl)) {
                image = new UMImage(VideoShowActivity.this, shareImageUrl);
                image.compressStyle = UMImage.CompressStyle.QUALITY;
            }
            web.setTitle(shareTitle);//标题
            web.setThumb(image);  //缩略图
            web.setDescription(shareContent);//描述
        }

        switch (view.getId()) {
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
            case R.id.iv_close_share:
                dismissShareView();
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
                intent.putExtra("rid", "-1");
                intent.putExtra("report_type", 2);
                startActivity(intent);
                break;
            case R.id.layout_to_home:
                Intent intent1 = new Intent(this, Main1Activity.class);
                startActivity(intent1);
                finish();
                break;
            default:
                break;
        }
    }

    public void dismissShareView() {
        if (shareDialog != null && shareDialog.isShowing()) {
            shareDialog.dismiss();
        }
    }
}
