package com.feiyou.headstyle.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
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
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.dingmouren.layoutmanagergroup.viewpager.OnViewPagerListener;
import com.dingmouren.layoutmanagergroup.viewpager.ViewPagerLayoutManager;
import com.feiyou.headstyle.App;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.MessageEvent;
import com.feiyou.headstyle.bean.NoteItem;
import com.feiyou.headstyle.bean.NoteSubComment;
import com.feiyou.headstyle.bean.NoteSubCommentRet;
import com.feiyou.headstyle.bean.ReplyParams;
import com.feiyou.headstyle.bean.ReplyResultInfoRet;
import com.feiyou.headstyle.bean.ResultInfo;
import com.feiyou.headstyle.bean.UserInfo;
import com.feiyou.headstyle.bean.VideoCommentRet;
import com.feiyou.headstyle.bean.VideoInfoRet;
import com.feiyou.headstyle.bean.ZanResultRet;
import com.feiyou.headstyle.common.Constants;
import com.feiyou.headstyle.presenter.AddZanPresenterImp;
import com.feiyou.headstyle.presenter.NoteSubCommentDataPresenterImp;
import com.feiyou.headstyle.presenter.ReplyCommentPresenterImp;
import com.feiyou.headstyle.presenter.VideoCommentPresenterImp;
import com.feiyou.headstyle.presenter.VideoInfoPresenterImp;
import com.feiyou.headstyle.ui.adapter.CommentAdapter;
import com.feiyou.headstyle.ui.adapter.CommentReplyAdapter;
import com.feiyou.headstyle.ui.adapter.VideoItemAdapter;
import com.feiyou.headstyle.ui.base.BaseFragmentActivity;
import com.feiyou.headstyle.ui.custom.LoginDialog;
import com.feiyou.headstyle.view.CommentDialog;
import com.feiyou.headstyle.view.VideoInfoView;
import com.orhanobut.logger.Logger;
import com.wang.avi.AVLoadingIndicatorView;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by myflying on 2018/11/23.
 */
public class VideoShowActivity extends BaseFragmentActivity implements VideoInfoView, CommentDialog.SendBackListener {

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

    LoginDialog loginDialog;

    private int videoPage = 1;

    private int commentPage = 1;

    private int subCommentPage = 1;

    private UserInfo userInfo;

    @Override
    protected int getContextViewId() {
        return R.layout.activity_video_show;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        isZan = ContextCompat.getDrawable(this, R.mipmap.is_zan);
        notZan = ContextCompat.getDrawable(this, R.mipmap.note_zan);

        mLayoutManager = new ViewPagerLayoutManager(this, OrientationHelper.VERTICAL);
        mVideoAdapter = new VideoItemAdapter(this, null);
        mVideoListView.setLayoutManager(mLayoutManager);
        mVideoListView.setAdapter(mVideoAdapter);

        videoInfoPresenterImp = new VideoInfoPresenterImp(this, this);
        videoCommentPresenterImp = new VideoCommentPresenterImp(this, this);
        replyCommentPresenterImp = new ReplyCommentPresenterImp(this, this);
        noteSubCommentDataPresenterImp = new NoteSubCommentDataPresenterImp(this, this);
        addZanPresenterImp = new AddZanPresenterImp(this, this);

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
                if (view.getId() == R.id.tv_comment_num) {
                    currentVideoId = mVideoAdapter.getData().get(position).getId();
                    videoCommentPresenterImp.getCommentList(commentPage, currentVideoId, userInfo != null ? userInfo.getId() : "");
                    if (popupWindow != null && !popupWindow.isShowing()) {
                        avi.show();
                        // 设置popupWindow的显示位置，此处是在手机屏幕底部且水平居中的位置
                        popupWindow.showAtLocation(mVideoShowLayout, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                    }
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

        LinearLayout addMessageLayout = commentView.findViewById(R.id.layout_add_message);

        addMessageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchType = 1;
                showInputDialog();
            }
        });

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
                    Glide.with(VideoShowActivity.this).load(noteItem.getCommentUserimg()).into(topUserHeadImageView);
                    nickNameTv.setText(noteItem.getCommentNickname());
                    addDateTv.setText(TimeUtils.millis2String(noteItem.getAddTime() != null ? noteItem.getAddTime() * 1000 : 0));
                    commentContentTv.setText(noteItem.getCommentContent());
                    zanCountTv.setText(noteItem.getZanNum() + "");
                    if (noteItem.getIsZan() == 0) {
                        zanCountTv.setCompoundDrawablesWithIntrinsicBounds(notZan, null, null, null);
                    } else {
                        zanCountTv.setCompoundDrawablesWithIntrinsicBounds(isZan, null, null, null);
                    }
                    zanCountTv.setCompoundDrawablePadding(SizeUtils.dp2px(4));

                    noteSubCommentDataPresenterImp.getNoteSubCommentData(subCommentPage,userInfo != null ? userInfo.getId() : "", commentId, 2);
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
        LinearLayout zanLayout = replyView.findViewById(R.id.layout_comment_zan);

        LinearLayout addMessageLayout = replyView.findViewById(R.id.layout_add_message);

        addMessageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchType = 2;
                showInputDialog();
            }
        });

        //顶部个人的回复信息
        topUserHeadImageView = replyView.findViewById(R.id.iv_user_head);
        nickNameTv = replyView.findViewById(R.id.tv_user_nick_name);
        addDateTv = replyView.findViewById(R.id.tv_add_date);
        commentContentTv = replyView.findViewById(R.id.tv_content);
        zanCountTv = replyView.findViewById(R.id.tv_zan_count);

        RelativeLayout.LayoutParams contentParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        int contentMarginHeight = BarUtils.getNavBarHeight() + SizeUtils.dp2px(62);
        contentParams.setMargins(0, SizeUtils.dp2px(49), 0, contentMarginHeight);
        contentLayout.setLayoutParams(contentParams);

        RelativeLayout.LayoutParams bottomParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(49));
        int tempHeight = ScreenUtils.getScreenHeight() - BarUtils.getNavBarHeight() - BarUtils.getStatusBarHeight() - SizeUtils.dp2px(49);
        bottomParams.setMargins(0, tempHeight, 0, BarUtils.getNavBarHeight());
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
        final VideoView videoView = itemView.findViewById(R.id.video_view);
        final ImageView imgThumb = itemView.findViewById(R.id.img_thumb);
        videoView.stopPlayback();
        imgThumb.animate().alpha(1).start();
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
                            mVideoAdapter.setNewData(((VideoInfoRet) tData).getData().getList().subList(startPosition, ((VideoInfoRet) tData).getData().getList().size() - 1));
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
                        mVideoAdapter.loadMoreEnd();
                    }
                }
            }

            if (tData instanceof VideoCommentRet) {
                if (tData.getCode() == Constants.SUCCESS && ((VideoCommentRet) tData).getData() != null) {
                    mNoDataLayout.setVisibility(View.GONE);
                    commentListView.setVisibility(View.VISIBLE);
                    commentAdapter.setNewData(((VideoCommentRet) tData).getData());
                } else {
                    mNoDataLayout.setVisibility(View.VISIBLE);
                    commentListView.setVisibility(View.GONE);
                }
            }

            if (tData instanceof NoteSubCommentRet) {
                if (((NoteSubCommentRet) tData).getData() != null) {
                    commentReplyAdapter.setNewData(((NoteSubCommentRet) tData).getData());
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
    public void sendContent(String content, int type) {
        //关闭软键盘
        KeyboardUtils.hideSoftInput(commentView);

        ReplyParams replyParams = new ReplyParams();
        if (switchType == 1) {
            replyParams.setModelType(2);
            replyParams.setType(1);
            replyParams.setContent(content);
            replyParams.setRepeatUserId(userInfo != null ? userInfo.getId() : "");
            replyParams.setMessageId(currentVideoId);
        }
        if (switchType == 2) {
            commentId = commentAdapter.getData().get(currentCommentPos).getCommentId();
            repeatCommentUserId = commentAdapter.getData().get(currentCommentPos).getUserId();

            replyParams.setModelType(1);
            replyParams.setType(2);
            replyParams.setContent(content);
            replyParams.setRepeatUserId(userInfo != null ? userInfo.getId() : "");
            replyParams.setCommentId(commentId);
            replyParams.setRepeatCommentUserId(repeatCommentUserId);
        }

        if (switchType == 3) {
            repeatId = commentReplyAdapter.getData().get(currentReplyPos).getRepeatId();
            repeatCommentUserId = commentReplyAdapter.getData().get(currentReplyPos).getOldUserId();

            replyParams.setModelType(1);
            replyParams.setType(3);
            replyParams.setContent(content);
            replyParams.setRepeatUserId(userInfo != null ? userInfo.getId() : "");
            replyParams.setRepeatId(repeatId);
            replyParams.setRepeatCommentUserId(repeatCommentUserId);
        }

        replyCommentPresenterImp.addReplyInfo(replyParams);

        if (content != null) {
            if (inputDialog != null) {
                inputDialog.hideProgressDialog();
                inputDialog.dismiss();
            }
        }

    }
}
