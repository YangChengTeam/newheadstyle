package com.feiyou.headstyle.ui.fragment.sub;

import android.app.ProgressDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.BarUtils;
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
import com.feiyou.headstyle.bean.MessageEvent;
import com.feiyou.headstyle.bean.NoteCommentRet;
import com.feiyou.headstyle.bean.NoteItem;
import com.feiyou.headstyle.bean.NoteSubCommentRet;
import com.feiyou.headstyle.bean.ReplyParams;
import com.feiyou.headstyle.bean.ReplyResultInfoRet;
import com.feiyou.headstyle.bean.ResultInfo;
import com.feiyou.headstyle.bean.ZanResultRet;
import com.feiyou.headstyle.common.Constants;
import com.feiyou.headstyle.presenter.AddZanPresenterImp;
import com.feiyou.headstyle.presenter.FollowInfoPresenterImp;
import com.feiyou.headstyle.presenter.NoteCommentDataPresenterImp;
import com.feiyou.headstyle.presenter.NoteSubCommentDataPresenterImp;
import com.feiyou.headstyle.presenter.ReplyCommentPresenterImp;
import com.feiyou.headstyle.ui.activity.ReportInfoActivity;
import com.feiyou.headstyle.ui.adapter.CommentAdapter;
import com.feiyou.headstyle.ui.adapter.CommentReplyAdapter;
import com.feiyou.headstyle.ui.base.BaseFragment;
import com.feiyou.headstyle.ui.custom.GlideRoundTransform;
import com.feiyou.headstyle.ui.custom.LoginDialog;
import com.feiyou.headstyle.utils.StatusBarUtil;
import com.feiyou.headstyle.view.CommentDialog;
import com.feiyou.headstyle.view.NoteCommentDataView;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by myflying on 2018/11/26.
 */
public class WonderfulFragment extends BaseFragment implements NoteCommentDataView, CommentDialog.SendBackListener, View.OnClickListener {

    public static final int REQUEST_CODE_CHOOSE = 23;

    public static final int REQUEST_FRIENDS = 0;

    public static final int RESULT_FRIEND_CODE = 2;

    @BindView(R.id.wonderful_list)
    RecyclerView mWonderfulListView;

    @BindView(R.id.layout_no_data)
    LinearLayout mNoDataLayout;

    private CommentAdapter commentAdapter;

    private NoteCommentDataPresenterImp noteCommentDataPresenterImp;

    private NoteSubCommentDataPresenterImp noteSubCommentDataPresenterImp;

    private ReplyCommentPresenterImp replyCommentPresenterImp;

    private AddZanPresenterImp addZanPresenterImp;

    private FollowInfoPresenterImp followInfoPresenterImp;

    BottomSheetDialog commitReplyDialog;

    private View replyView;

    private ImageView mCloseReplyIv;

    private FrameLayout mReplyFollowLayout;

    private TextView mReplyFollowTv;

    private RecyclerView replyListView;

    LinearLayout mReplyNoDataLayout;

    TextView mReplyTitleTv;

    ImageView topUserHeadImageView;

    TextView nickNameTv;

    TextView addDateTv;

    TextView commentContentTv;

    TextView zanCountTv;

    CommentDialog commentDialog;

    private CommentReplyAdapter commentReplyAdapter;

    public static WonderfulFragment getInstance() {
        return new WonderfulFragment();
    }

    private int currentCommentPos = -1;

    private String commentId;

    private String repeatCommentUserId;

    private ProgressDialog progressDialog = null;

    private int currentReplyPos = -1;

    private int switchType;

    private String repeatId;

    private Drawable isZan;

    private Drawable notZan;

    private String messageId;

    private int currentPage = 1;

    private int subCurrentPage = 1;

    LoginDialog loginDialog;

    private String replyTopUserId;

    public final static String MASK_STR = "@";

    public final static String SPLIT_STR = "*#";

    public BottomSheetDialog reportDialog;

    LinearLayout mReportCopyLayout;

    LinearLayout mReportLayout;

    LinearLayout mReportCancelLayout;

    private int longClickType = 1;//1,回复的举报,2回复的回复举报

    @Override
    protected View onCreateView() {
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_wonderful, null);
        ButterKnife.bind(this, root);
        EventBus.getDefault().register(this);
        initData();
        return root;
    }

    public static WonderfulFragment newInstance(String msgId) {
        WonderfulFragment fragment = new WonderfulFragment();
        Bundle bundle = new Bundle();
        bundle.putString("msg_id", msgId);
        fragment.setArguments(bundle);
        return fragment;
    }

    //初始化回复举报弹窗
    public void initReportDialog() {
        reportDialog = new BottomSheetDialog(getActivity());
        View reportView = LayoutInflater.from(getActivity()).inflate(R.layout.comment_report_view, null);
        mReportCopyLayout = reportView.findViewById(R.id.layout_report_copy);
        mReportLayout = reportView.findViewById(R.id.layout_report);
        mReportCancelLayout = reportView.findViewById(R.id.layout_report_cancel);
        mReportCopyLayout.setOnClickListener(this);
        mReportLayout.setOnClickListener(this);
        mReportCancelLayout.setOnClickListener(this);
        reportDialog.setContentView(reportView);
    }

    public void initReplyDialog() {
        commitReplyDialog = new BottomSheetDialog(getActivity());
        replyView = LayoutInflater.from(getActivity()).inflate(R.layout.comment_reply_view, null);
        mReplyFollowLayout = replyView.findViewById(R.id.reply_is_follow);
        mReplyFollowTv = replyView.findViewById(R.id.tv_reply_follow);
        replyListView = replyView.findViewById(R.id.rv_reply_list);
        mReplyNoDataLayout = replyView.findViewById(R.id.reply_layout_no_data);
        mCloseReplyIv = replyView.findViewById(R.id.iv_close);
        zanCountTv = replyView.findViewById(R.id.tv_zan_count);

        LinearLayout contentLayout = replyView.findViewById(R.id.layout_content);
        LinearLayout bottomLayout = replyView.findViewById(R.id.layout_reply_bottom);
        LinearLayout zanLayout = replyView.findViewById(R.id.layout_comment_zan);

        //顶部个人的回复信息
        mReplyTitleTv = replyView.findViewById(R.id.tv_reply_title);
        topUserHeadImageView = replyView.findViewById(R.id.iv_user_head);
        nickNameTv = replyView.findViewById(R.id.tv_user_nick_name);
        addDateTv = replyView.findViewById(R.id.tv_add_date);
        commentContentTv = replyView.findViewById(R.id.tv_content);
        LinearLayout addMessageLayout = replyView.findViewById(R.id.layout_add_message);

        addMessageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchType = 1;
                showDialog();
            }
        });

        zanLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchType = 1;
                addZanPresenterImp.addZan(2, App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "", commentAdapter.getData().get(currentCommentPos).getUserId(), "", commentId, "", 1);
            }
        });

        mReplyFollowLayout.setOnClickListener(new View.OnClickListener() {
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

        RelativeLayout.LayoutParams contentParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        int contentMarginHeight = BarUtils.getNavBarHeight() + SizeUtils.dp2px(62);
        contentParams.setMargins(0, SizeUtils.dp2px(49), 0, contentMarginHeight);
        contentLayout.setLayoutParams(contentParams);

        RelativeLayout.LayoutParams bottomParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(49));
        //int tempHeight = ScreenUtils.getScreenHeight() - BarUtils.getNavBarHeight() - BarUtils.getStatusBarHeight() - SizeUtils.dp2px(49);
        bottomParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        bottomParams.setMargins(0, 0, 0, BarUtils.getNavBarHeight() - BarUtils.getStatusBarHeight());
        bottomLayout.setLayoutParams(bottomParams);

        replyView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ScreenUtils.getScreenHeight()));
        commitReplyDialog.setContentView(replyView);

        //setPeekHeight,设置弹出窗口的高度为全屏的状态.
        BottomSheetBehavior mBehavior = BottomSheetBehavior.from((View) replyView.getParent());
        mBehavior.setPeekHeight(ScreenUtils.getScreenHeight() - BarUtils.getStatusBarHeight());

        commentReplyAdapter = new CommentReplyAdapter(getActivity(), null);
        replyListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        replyListView.setAdapter(commentReplyAdapter);
    }

    public void initData() {

        Logger.i("wonderful initData --->");

        Bundle bundle = getArguments();
        if (bundle != null && !StringUtils.isEmpty(bundle.getString("msg_id"))) {
            messageId = bundle.getString("msg_id");
        }

        isZan = ContextCompat.getDrawable(getActivity(), R.mipmap.is_zan);
        notZan = ContextCompat.getDrawable(getActivity(), R.mipmap.note_zan);

        initReplyDialog();//回复列表

        initReportDialog();//举报窗口

        mWonderfulListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        commentAdapter = new CommentAdapter(getActivity(), null);
        mWonderfulListView.setAdapter(commentAdapter);

        commentAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (!App.getApp().isLogin) {
                    if (loginDialog != null && !loginDialog.isShowing()) {
                        loginDialog.show();
                    }
                    return;
                }

                switchType = 1;
                currentCommentPos = position;
                showDialog();
            }
        });

        commentAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                longClickType = 1;
                currentCommentPos = position;
                if (reportDialog != null && !reportDialog.isShowing()) {
                    reportDialog.show();
                }
                return false;
            }
        });

        commentAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                switchType = 1;
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
                    options.transform(new GlideRoundTransform(getActivity(), 21));
                    options.error(R.mipmap.head_def);
                    options.placeholder(R.mipmap.head_def);

                    mReplyTitleTv.setText(noteItem.getCommentNum() > 0 ? noteItem.getCommentNum() + "条回复" : "暂无回复");
                    Glide.with(getActivity()).load(noteItem.getCommentUserimg()).apply(options).into(topUserHeadImageView);
                    nickNameTv.setText(noteItem.getCommentNickname());
                    addDateTv.setText(TimeUtils.millis2String(noteItem.getAddTime() != null ? noteItem.getAddTime() * 1000 : 0));
                    commentContentTv.setText(Html.fromHtml(noteItem.getCommentContent()));
                    noteSubCommentDataPresenterImp.getNoteSubCommentData(subCurrentPage, App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "", commentId, 1);

                    zanCountTv.setText(noteItem.getZanNum() + "");

                    if (noteItem.getIsZan() == 0) {
                        zanCountTv.setCompoundDrawablesWithIntrinsicBounds(notZan, null, null, null);
                    } else {
                        zanCountTv.setCompoundDrawablesWithIntrinsicBounds(isZan, null, null, null);
                    }
                    zanCountTv.setCompoundDrawablePadding(SizeUtils.dp2px(4));
                }

                if (view.getId() == R.id.layout_zan) {
                    if (!App.getApp().isLogin) {
                        if (loginDialog != null && !loginDialog.isShowing()) {
                            loginDialog.show();
                        }
                        return;
                    }

                    addZanPresenterImp.addZan(2, App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "", commentAdapter.getData().get(position).getUserId(), "", commentId, "", 1);
                }
            }
        });

        mCloseReplyIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (commitReplyDialog != null && commitReplyDialog.isShowing()) {
                    commitReplyDialog.dismiss();
                }
            }
        });

        commentReplyAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (!App.getApp().isLogin) {
                    if (loginDialog != null && !loginDialog.isShowing()) {
                        loginDialog.show();
                    }
                    return;
                }

                switchType = 2;
                currentReplyPos = position;
                showDialog();
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

                switchType = 2;
                currentReplyPos = position;

                if (view.getId() == R.id.layout_zan || view.getId() == R.id.btn_reply_count) {
                    repeatId = commentReplyAdapter.getData().get(position).getRepeatId();
                    addZanPresenterImp.addZan(3, App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "", commentReplyAdapter.getData().get(position).getRepeatUserId(), "", "", repeatId, 1);
                }
            }
        });

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("回复中");

        loginDialog = new LoginDialog(getActivity(), R.style.login_dialog);

        noteSubCommentDataPresenterImp = new NoteSubCommentDataPresenterImp(this, getActivity());
        addZanPresenterImp = new AddZanPresenterImp(this, getActivity());
        noteCommentDataPresenterImp = new NoteCommentDataPresenterImp(this, getActivity());
        replyCommentPresenterImp = new ReplyCommentPresenterImp(this, getActivity());
        followInfoPresenterImp = new FollowInfoPresenterImp(this, getActivity());
        noteCommentDataPresenterImp.getNoteDetailData(App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "", currentPage, messageId, 1);
    }

    public void showDialog() {
        commentDialog = new CommentDialog(getActivity(), 1);
        commentDialog.setSendBackListener(this);
        commentDialog.show(getActivity().getFragmentManager(), "dialog");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(MessageEvent messageEvent) {

        if (messageEvent.getMessage().equals("friend_ids")) {
            if (messageEvent.getMessage().equals("friend_ids")) {
                List<String> friendIds = JSON.parseArray(messageEvent.getFriendIds(), String.class);
                List<String> names = JSON.parseArray(messageEvent.getFriendNames(), String.class);
                Logger.i("user names result--->" + messageEvent.getFriendNames());
                commentDialog.setAtUserNames(friendIds, names);
            }
        } else {
            noteCommentDataPresenterImp.getNoteDetailData(App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "", currentPage, messageId, 1);
        }
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void dismissProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void loadDataSuccess(ResultInfo tData) {

        Logger.i("tdata--->" + JSONObject.toJSONString(tData));

        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

        if (tData != null && tData.getCode() == Constants.SUCCESS) {
            if (tData instanceof NoteCommentRet) {
                if (((NoteCommentRet) tData).getData() != null) {
                    commentAdapter.setNewData(((NoteCommentRet) tData).getData());
                    mNoDataLayout.setVisibility(View.GONE);
                } else {
                    mNoDataLayout.setVisibility(View.VISIBLE);
                }
            }

            if (tData instanceof NoteSubCommentRet) {
                if (((NoteSubCommentRet) tData).getData() != null) {
                    replyListView.setVisibility(View.VISIBLE);
                    mReplyNoDataLayout.setVisibility(View.GONE);
                    commentReplyAdapter.setNewData(((NoteSubCommentRet) tData).getData());
                } else {
                    replyListView.setVisibility(View.GONE);
                    mReplyNoDataLayout.setVisibility(View.VISIBLE);
                }
            }

            if (tData instanceof ReplyResultInfoRet) {
                //ToastUtils.showLong("回复成功");
                replyListView.setVisibility(View.VISIBLE);
                mReplyNoDataLayout.setVisibility(View.GONE);
                if (switchType == 1) {
                    mNoDataLayout.setVisibility(View.GONE);
                    mWonderfulListView.setVisibility(View.VISIBLE);

                    Integer tempNum = commentAdapter.getData().get(currentCommentPos).getListNum() + 1;
                    commentAdapter.getData().get(currentCommentPos).setListNum(tempNum);
                    commentAdapter.notifyDataSetChanged();

                    //子列表更新
                    commentReplyAdapter.addData(0, ((ReplyResultInfoRet) tData).getData());
                    commentReplyAdapter.notifyDataSetChanged();
                }
                if (switchType == 2) {
                    replyListView.setVisibility(View.VISIBLE);
                    mReplyNoDataLayout.setVisibility(View.GONE);
                    commentReplyAdapter.addData(0, ((ReplyResultInfoRet) tData).getData());
                    commentReplyAdapter.notifyDataSetChanged();
                }
            }

            if (tData instanceof ZanResultRet) {
                if (switchType == 1) {
                    int tempNum = commentAdapter.getData().get(currentCommentPos).getZanNum();

                    if (((ZanResultRet) tData).getData().getIsZan() == 0) {
                        tempNum = tempNum - 1;
                        zanCountTv.setCompoundDrawablesWithIntrinsicBounds(notZan, null, null, null);
                        zanCountTv.setCompoundDrawablePadding(SizeUtils.dp2px(4));
                    } else {
                        tempNum = tempNum + 1;
                        zanCountTv.setCompoundDrawablesWithIntrinsicBounds(isZan, null, null, null);
                        zanCountTv.setCompoundDrawablePadding(SizeUtils.dp2px(4));
                    }

                    commentAdapter.getData().get(currentCommentPos).setZanNum(tempNum);
                    commentAdapter.getData().get(currentCommentPos).setIsZan(((ZanResultRet) tData).getData().getIsZan());
                    commentAdapter.notifyDataSetChanged();

                    zanCountTv.setText(tempNum + "");
                }

                if (switchType == 2) {

                    int tempNum = commentReplyAdapter.getData().get(currentReplyPos).getZanNum();
                    if (((ZanResultRet) tData).getData().getIsZan() == 0) {
                        tempNum = tempNum - 1;
                    } else {
                        tempNum = tempNum + 1;
                    }
                    commentReplyAdapter.getData().get(currentReplyPos).setZanNum(tempNum);
                    commentReplyAdapter.getData().get(currentReplyPos).setIsZan(((ZanResultRet) tData).getData().getIsZan());
                    commentReplyAdapter.notifyDataSetChanged();
                }
            }

            if (tData instanceof FollowInfoRet) {
                int tempResult = ((FollowInfoRet) tData).getData().getIsGuan();

                ToastUtils.showLong(tempResult == 0 ? "已取消" : "已关注");
                mReplyFollowLayout.setBackgroundResource(tempResult == 0 ? R.drawable.into_bg : R.drawable.is_follow_bg);
                mReplyFollowTv.setTextColor(ContextCompat.getColor(getActivity(), tempResult == 0 ? R.color.tab_select_color : R.color.black2));
                mReplyFollowTv.setText(tempResult == 0 ? "+关注" : "已关注");
            }

        } else {
            if (tData instanceof NoteCommentRet) {
                mNoDataLayout.setVisibility(View.VISIBLE);
            }

            if (tData instanceof NoteSubCommentRet) {
                replyListView.setVisibility(View.GONE);
                mReplyNoDataLayout.setVisibility(View.VISIBLE);
            }

            if (tData instanceof FollowInfoRet) {
                ToastUtils.showLong(StringUtils.isEmpty(tData.getMsg()) ? "操作失败" : tData.getMsg());
            }

            if (tData instanceof ReplyResultInfoRet) {
                ToastUtils.showLong(StringUtils.isEmpty(tData.getMsg()) ? "操作失败" : tData.getMsg());
            }
            Logger.i(StringUtils.isEmpty(tData.getMsg()) ? "操作失败" : tData.getMsg());
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
        Logger.i("content--->" + content + "---type--->" + type);

        if (progressDialog != null && !progressDialog.isShowing()) {
            progressDialog.show();
        }

        if (switchType == 1) {
            commentId = commentAdapter.getData().get(currentCommentPos).getCommentId();
            repeatCommentUserId = commentAdapter.getData().get(currentCommentPos).getUserId();

            ReplyParams replyParams = new ReplyParams();
            replyParams.setModelType(1);
            replyParams.setType(2);
            replyParams.setContent(content);
            replyParams.setRepeatUserId(App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "");
            replyParams.setCommentId(commentId);
            replyParams.setRepeatCommentUserId(repeatCommentUserId);
            replyParams.setAtUserIds(userIds);

            replyCommentPresenterImp.addReplyInfo(replyParams);

            if (content != null) {
                if (commentDialog != null) {
                    commentDialog.hideProgressDialog();
                    commentDialog.dismiss();
                }
            }
        }

        if (switchType == 2) {
            repeatId = commentReplyAdapter.getData().get(currentReplyPos).getRepeatId();
            repeatCommentUserId = commentReplyAdapter.getData().get(currentReplyPos).getRepeatUserId();

            ReplyParams replyParams = new ReplyParams();
            replyParams.setModelType(1);
            replyParams.setType(3);
            replyParams.setContent(content);
            replyParams.setRepeatUserId(App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "");
            replyParams.setRepeatId(repeatId);
            replyParams.setRepeatCommentUserId(repeatCommentUserId);
            replyParams.setAtUserIds(userIds);
            replyCommentPresenterImp.addReplyInfo(replyParams);

            if (content != null) {
                if (commentDialog != null) {
                    commentDialog.hideProgressDialog();
                    commentDialog.dismiss();
                }
            }

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_report_copy:
                if (reportDialog != null && reportDialog.isShowing()) {
                    reportDialog.dismiss();
                }
                if (longClickType == 1) {
                    String tempContent = commentAdapter.getData().get(currentCommentPos).getCommentContent();
                    ClipboardManager cmb = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                    cmb.setText(tempContent); //将内容放入粘贴管理器,在别的地方长按选择"粘贴"即可
                    ToastUtils.showLong("已复制");
                }
                break;
            case R.id.layout_report:
                if (reportDialog != null && reportDialog.isShowing()) {
                    reportDialog.dismiss();
                }
                //举报评论
                Intent intent = new Intent(getActivity(), ReportInfoActivity.class);
                intent.putExtra("rid", commentAdapter.getData().get(currentCommentPos).getCommentId());
                intent.putExtra("report_type", 3);
                startActivity(intent);

                break;
            case R.id.layout_report_cancel:
                if (reportDialog != null && reportDialog.isShowing()) {
                    reportDialog.dismiss();
                }
                break;
            default:
                break;
        }
    }
}
