package com.feiyou.headstyle.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdManager;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feiyou.headstyle.App;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.FollowInfoRet;
import com.feiyou.headstyle.bean.MessageEvent;
import com.feiyou.headstyle.bean.NoteInfo;
import com.feiyou.headstyle.bean.NoteInfoRet;
import com.feiyou.headstyle.bean.NoteTypeRet;
import com.feiyou.headstyle.bean.NoteTypeWrapper;
import com.feiyou.headstyle.bean.ResultInfo;
import com.feiyou.headstyle.bean.ZanResultRet;
import com.feiyou.headstyle.common.Constants;
import com.feiyou.headstyle.presenter.AddZanPresenterImp;
import com.feiyou.headstyle.presenter.FollowInfoPresenterImp;
import com.feiyou.headstyle.presenter.NoteTypePresenterImp;
import com.feiyou.headstyle.ui.adapter.NoteMultipleAdapter;
import com.feiyou.headstyle.ui.base.BaseFragmentActivity;
import com.feiyou.headstyle.ui.custom.LoginDialog;
import com.feiyou.headstyle.utils.TTAdManagerHolder;
import com.feiyou.headstyle.view.NoteTypeView;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.orhanobut.logger.Logger;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
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
 * Created by myflying on 2018/11/28.
 */
public class CommunityType1Activity extends BaseFragmentActivity implements NoteTypeView, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private final static int REQUEST_CODE = 1; // 返回的结果码

    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mRefreshLayout;

    @BindView(R.id.avi)
    AVLoadingIndicatorView avi;

    @BindView(R.id.layout_no_data)
    LinearLayout mNoDataLayout;

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

    @BindView(R.id.layout_community_follow)
    FrameLayout mCommunityFollowLayout;

    @BindView(R.id.tv_community_follow_txt)
    TextView mCommunityFollowTv;

    @BindView(R.id.community_type_list)
    RecyclerView mCommunityTypeListView;

    //noteMultipleAdapter noteMultipleAdapter;

    NoteMultipleAdapter noteMultipleAdapter;

    private int currentPage = 1;

    private int pageSize = 6;

    private FollowInfoPresenterImp followInfoPresenterImp;

    private NoteTypePresenterImp noteTypePresenterImp;

    private AddZanPresenterImp addZanPresenterImp;

    LoginDialog loginDialog;

    private String noteId;

    private String topicId;

    private int currentClickIndex;

    private BottomSheetDialog commonShareDialog;

    //分享弹窗页面
    private View mCommonShareView;

    LinearLayout mWeixinLayout;

    LinearLayout mCircleLayout;

    LinearLayout mQQLayout;

    LinearLayout mQQzoneLayout;

    LinearLayout mReportLayout;

    LinearLayout mBackHomeLayout;

    ImageView mCloseIv;

    private ShareAction shareAction;

    /**
     * 标识 关注话题/用户
     */
    private boolean followTopic;

    BottomSheetDialog normalShareDialog;

    private int topicDefIndex = -1;

    private TTAdNative mTTAdNative;

    @Override
    protected int getContextViewId() {
        return R.layout.activity_community_type1;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        initShareDialog();
        initData();
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

        //普通的分享弹窗
        normalShareDialog = new BottomSheetDialog(this);
        View shareNormalView = LayoutInflater.from(this).inflate(R.layout.share_normal_dialog_view, null);
        ImageView mCloseNormalImageView = shareNormalView.findViewById(R.id.iv_normal_close_share);
        LinearLayout weixinNormalLayout = shareNormalView.findViewById(R.id.layout_normal_weixin);
        LinearLayout circleNormalLayout = shareNormalView.findViewById(R.id.layout_normal_circle);
        LinearLayout qqNormalLayout = shareNormalView.findViewById(R.id.layout_normal_qq_friends);
        LinearLayout qzoneNormalLayout = shareNormalView.findViewById(R.id.layout_normal_qzone);
        weixinNormalLayout.setOnClickListener(this);
        circleNormalLayout.setOnClickListener(this);
        qqNormalLayout.setOnClickListener(this);
        qzoneNormalLayout.setOnClickListener(this);
        mCloseNormalImageView.setOnClickListener(this);
        normalShareDialog.setContentView(shareNormalView);
    }

    public void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            topicDefIndex = bundle.getInt("topic_index", -1);
        }

        if (bundle != null && bundle.getString("topic_id") != null) {
            topicId = bundle.getString("topic_id");
        }

        //step1:初始化sdk
        TTAdManager ttAdManager = TTAdManagerHolder.get();
        mTTAdNative = ttAdManager.createAdNative(this);

        mRefreshLayout.setOnRefreshListener(this);
        //设置进度View样式的大小，只有两个值DEFAULT和LARGE
        //设置进度View下拉的起始点和结束点，scale 是指设置是否需要放大或者缩小动画
        mRefreshLayout.setProgressViewOffset(true, -0, 200);
        //设置进度View下拉的结束点，scale 是指设置是否需要放大或者缩小动画
        mRefreshLayout.setProgressViewEndTarget(true, 180);
        //设置进度View的组合颜色，在手指上下滑时使用第一个颜色，在刷新中，会一个个颜色进行切换
        mRefreshLayout.setColorSchemeColors(ContextCompat.getColor(this, R.color.colorPrimary), Color.RED, Color.YELLOW, Color.BLUE);

        //设置触发刷新的距离
        mRefreshLayout.setDistanceToTriggerSync(200);
        //如果child是自己自定义的view，可以通过这个回调，告诉mSwipeRefreshLayoutchild是否可以滑动
        mRefreshLayout.setOnChildScrollUpCallback(null);

        loginDialog = new LoginDialog(this, R.style.login_dialog);
        if (shareAction == null) {
            shareAction = new ShareAction(this);
            shareAction.setCallback(shareListener);//回调监听器
        }
        followInfoPresenterImp = new FollowInfoPresenterImp(this, this);
        noteTypePresenterImp = new NoteTypePresenterImp(this, this);
        addZanPresenterImp = new AddZanPresenterImp(this, this);

        noteMultipleAdapter = new NoteMultipleAdapter(null, 1);
        mCommunityTypeListView.setLayoutManager(new LinearLayoutManager(this));
        mCommunityTypeListView.setAdapter(noteMultipleAdapter);

        noteMultipleAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(CommunityType1Activity.this, CommunityArticleActivity.class);
                intent.putExtra("msg_id", noteMultipleAdapter.getData().get(position).getId());
                startActivity(intent);
            }
        });

        noteMultipleAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                if (!App.getApp().isLogin && view.getId() != R.id.layout_note_share) {
                    if (loginDialog != null && !loginDialog.isShowing()) {
                        loginDialog.show();
                    }
                    return;
                }

                currentClickIndex = position;

                if (view.getId() == R.id.layout_follow) {
                    followTopic = false;
                    followInfoPresenterImp.addFollow(App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "", noteMultipleAdapter.getData().get(position).getUserId());
                }
                if (view.getId() == R.id.layout_item_zan) {
                    String messageId = noteMultipleAdapter.getData().get(position).getId();
                    addZanPresenterImp.addZan(1, App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "", noteMultipleAdapter.getData().get(position).getUserId(), messageId, "", "", 1);
                }
                if (view.getId() == R.id.iv_user_head) {
                    Intent intent = new Intent(CommunityType1Activity.this, UserInfoActivity.class);
                    intent.putExtra("user_id", noteMultipleAdapter.getData().get(position).getUserId());
                    startActivity(intent);
                }

                //分享
                if (view.getId() == R.id.layout_note_share) {
                    if (normalShareDialog != null && !normalShareDialog.isShowing()) {
                        normalShareDialog.show();
                    }
                }
            }
        });

        noteMultipleAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                currentPage++;
                noteTypePresenterImp.getNoteTypeData(topicId, currentPage, pageSize, 1, App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "");
            }
        }, mCommunityTypeListView);

        mCommunityTypeListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int topRowVerticalPosition = (recyclerView == null || recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();
                mRefreshLayout.setEnabled(topRowVerticalPosition >= 0);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        noteTypePresenterImp.getNoteTypeData(topicId, currentPage, pageSize, 1, App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "");
    }

    @OnClick(R.id.fab)
    void fabButton() {
        if (!App.getApp().isLogin) {
            if (loginDialog != null && !loginDialog.isShowing()) {
                loginDialog.show();
            }
            return;
        }

        Intent intent = new Intent(this, PushNoteActivity.class);
        intent.putExtra("from_note_type", 2);
        intent.putExtra("topic_index", topicDefIndex);
        intent.putExtra("topic_id", topicId);
        startActivity(intent);
    }

    @OnClick(R.id.iv_top_share)
    void shareInfo() {
        if (commonShareDialog != null && !commonShareDialog.isShowing()) {
            commonShareDialog.show();
        }
    }

    @OnClick(R.id.tv_top1_note_name)
    void topNote() {
        Intent intent = new Intent(this, CommunityArticleActivity.class);
        intent.putExtra("msg_id", noteId);
        intent.putExtra("if_from_stick", true);
        startActivity(intent);
    }

    @OnClick(R.id.iv_back)
    void back() {
        popBackStack();
    }

    @OnClick(R.id.layout_community_follow)
    void followTopic() {
        if (!App.getApp().isLogin) {
            if (loginDialog != null && !loginDialog.isShowing()) {
                loginDialog.show();
            }
            return;
        }
        followTopic = true;
        followInfoPresenterImp.followTopic(App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "", topicId);
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void dismissProgress() {
        avi.hide();
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void loadDataSuccess(ResultInfo tData) {
        Logger.i("community type data --->" + JSON.toJSONString(tData));
        avi.hide();
        mRefreshLayout.setRefreshing(false);
        if (tData != null && tData.getCode() == Constants.SUCCESS) {
            mNoDataLayout.setVisibility(View.GONE);
            mCommunityTypeListView.setVisibility(View.VISIBLE);

            if (tData instanceof NoteTypeRet) {
                NoteTypeWrapper noteTypeWrapper = ((NoteTypeRet) tData).getData();
                if (noteTypeWrapper != null && noteTypeWrapper.getTopicArr() != null) {
                    //TODO 暂时去掉背景
                    //RequestOptions options = new RequestOptions();
                    //options.error(R.mipmap.community_type_top);
                    //Glide.with(this).load(noteTypeWrapper.getTopicArr().getBackground()).into(mTopBarImageView);
                    mTopicNameTv.setText(noteTypeWrapper.getTopicArr().getName());
                    mTitleTextView.setText(StringUtils.isEmpty(noteTypeWrapper.getTopicArr().getName()) ? "帖子分类" : noteTypeWrapper.getTopicArr().getName());
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

                        List<NoteInfo> tempList = noteTypeWrapper.getList();
                        for (NoteInfo noteInfo : tempList) {
                            noteInfo.setItemType(NoteInfo.NOTE_NORMAL);
                        }

                        NoteInfo tempNoteInfo = new NoteInfo(NoteInfo.NOTE_AD);
                        tempList.add(tempNoteInfo);

                        noteMultipleAdapter.setNewData(tempList);
                    } else {
                        List<NoteInfo> tempList = noteTypeWrapper.getList();
                        for (NoteInfo noteInfo : tempList) {
                            noteInfo.setItemType(NoteInfo.NOTE_NORMAL);
                        }

                        noteMultipleAdapter.addData(tempList);

                        NoteInfo tempNoteInfo = new NoteInfo(NoteInfo.NOTE_AD);
                        noteMultipleAdapter.getData().add(tempNoteInfo);
                        noteMultipleAdapter.notifyItemChanged(noteMultipleAdapter.getData().size() - 1);
                    }
                }

                if (currentPage == 1) {
                    if (noteTypeWrapper != null && noteTypeWrapper.getList().size() == pageSize + 1) {
                        noteMultipleAdapter.loadMoreComplete();
                    } else {
                        noteMultipleAdapter.loadMoreEnd(true);
                    }
                } else {
                    if (noteTypeWrapper != null && noteTypeWrapper.getList().size() == pageSize) {
                        noteMultipleAdapter.loadMoreComplete();
                    } else {
                        noteMultipleAdapter.loadMoreEnd(true);
                    }
                }

                //TODO，加载广告
                loadListAd();

                if (noteTypeWrapper.getIsGuan() == 0) {
                    mCommunityFollowLayout.setBackgroundResource(R.mipmap.not_follow_topic);
                    mCommunityFollowTv.setTextColor(ContextCompat.getColor(this, R.color.white));
                } else {
                    mCommunityFollowLayout.setBackgroundResource(R.mipmap.is_follow_icon);
                    mCommunityFollowTv.setTextColor(ContextCompat.getColor(this, R.color.is_follow_topic_bg_color));
                    mCommunityFollowTv.setText("已关注");
                }
            }

            if (tData instanceof FollowInfoRet) {
                if (((FollowInfoRet) tData).getData() != null) {
                    //关注话题
                    if (followTopic) {
                        if (((FollowInfoRet) tData).getData().getIsGuan() == 0) {
                            ToastUtils.showLong("已取消");
                            mCommunityFollowLayout.setBackgroundResource(R.mipmap.not_follow_topic);
                            mCommunityFollowTv.setTextColor(ContextCompat.getColor(this, R.color.white));
                        } else {
                            ToastUtils.showLong("已关注");
                            mCommunityFollowLayout.setBackgroundResource(R.mipmap.is_follow_icon);
                            mCommunityFollowTv.setTextColor(ContextCompat.getColor(this, R.color.is_follow_topic_bg_color));
                            mCommunityFollowTv.setText("已关注");
                        }
                    } else {
                        //关注用户
                        int isGuan = ((FollowInfoRet) tData).getData().getIsGuan();
                        Toasty.normal(this, isGuan == 0 ? "已取消" : "已关注").show();
                        noteMultipleAdapter.getData().get(currentClickIndex).setIsGuan(isGuan);
                        String gUserId = noteMultipleAdapter.getData().get(currentClickIndex).getUserId();
                        for (NoteInfo noteInfo : noteMultipleAdapter.getData()) {
                            if (noteInfo.getUserId().equals(gUserId)) {
                                noteInfo.setIsGuan(isGuan);
                            }
                        }
                        noteMultipleAdapter.notifyDataSetChanged();
                    }
                } else {
                    ToastUtils.showLong(StringUtils.isEmpty(tData.getMsg()) ? "操作错误" : tData.getMsg());
                }
            }

            if (tData instanceof ZanResultRet) {
                int tempNum = noteMultipleAdapter.getData().get(currentClickIndex).getZanNum();
                if (((ZanResultRet) tData).getData().getIsZan() == 0) {
                    tempNum = tempNum - 1;
                } else {
                    tempNum = tempNum + 1;
                }

                noteMultipleAdapter.getData().get(currentClickIndex).setZanNum(tempNum);
                noteMultipleAdapter.getData().get(currentClickIndex).setIsZan(((ZanResultRet) tData).getData().getIsZan());
                noteMultipleAdapter.notifyDataSetChanged();
            }
        } else {

            if (tData instanceof NoteTypeRet) {
                mNoDataLayout.setVisibility(View.VISIBLE);
                mCommunityTypeListView.setVisibility(View.GONE);
            }

            if (tData instanceof FollowInfoRet) {
                ToastUtils.showLong(StringUtils.isEmpty(tData.getMsg()) ? "操作错误" : tData.getMsg());
            } else {
                Logger.i("error--->" + tData.getMsg());
            }
        }
    }

    @Override
    public void loadDataError(Throwable throwable) {
        avi.hide();
        mRefreshLayout.setRefreshing(false);
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
            Toast.makeText(CommunityType1Activity.this, "分享成功", Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            dismissShareView();
            Toast.makeText(CommunityType1Activity.this, "分享失败", Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            dismissShareView();
            Toast.makeText(CommunityType1Activity.this, "取消分享", Toast.LENGTH_LONG).show();
        }
    };

    @Override
    public void onClick(View view) {
        String shareTitle = "一位神秘人士对你发出邀请";
        String shareContent = "这里的老哥老姐个个都是人才，说话又好听，我超喜欢这里...";
        String tempStr = "一位神秘人士对你发出邀请";
        if (noteMultipleAdapter.getData() != null && noteMultipleAdapter.getData().size() > 0) {
            tempStr = StringUtils.isEmpty(noteMultipleAdapter.getData().get(currentClickIndex).getContent()) ? "一位神秘人士对你发出邀请" : noteMultipleAdapter.getData().get(currentClickIndex).getContent();
        }

        UMWeb web = new UMWeb("http://gx.qqtn.com");
        if (shareAction != null) {
            UMImage image = new UMImage(CommunityType1Activity.this, R.drawable.app_share);
            image.compressStyle = UMImage.CompressStyle.QUALITY;
            web.setThumb(image);  //缩略图
            web.setDescription(shareContent);//描述
        }

        UMImage normalImage = null;
        if (noteMultipleAdapter.getData() != null && noteMultipleAdapter.getData().size() > 0) {
            if (noteMultipleAdapter.getData().get(currentClickIndex).getImageArr() != null && noteMultipleAdapter.getData().get(currentClickIndex).getImageArr().length > 0) {
                normalImage = new UMImage(CommunityType1Activity.this, noteMultipleAdapter.getData().get(currentClickIndex).getImageArr()[0]);
                normalImage.compressStyle = UMImage.CompressStyle.QUALITY;
            } else {
                normalImage = new UMImage(CommunityType1Activity.this, R.drawable.app_share);
                normalImage.compressStyle = UMImage.CompressStyle.QUALITY;
            }
        } else {
            normalImage = new UMImage(CommunityType1Activity.this, R.drawable.app_share);
            normalImage.compressStyle = UMImage.CompressStyle.QUALITY;
        }

        switch (view.getId()) {
            case R.id.iv_close_share:
                dismissShareView();
                break;
            case R.id.layout_weixin:
                web.setTitle(shareTitle);//标题
                shareAction.withMedia(web).setPlatform(SHARE_MEDIA.WEIXIN).share();
                break;
            case R.id.layout_circle:
                web.setTitle("@你加入我们，这里的老哥老姐个个都是人才，说话超好听");//标题
                shareAction.withMedia(web).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE).share();
                break;
            case R.id.layout_qq:
                web.setTitle(shareTitle);//标题
                shareAction.withMedia(web).setPlatform(SHARE_MEDIA.QQ).share();
                break;
            case R.id.layout_qzone:
                web.setTitle(shareTitle);//标题
                shareAction.withMedia(web).setPlatform(SHARE_MEDIA.QZONE).share();
                break;
            case R.id.layout_report:

                if (!App.getApp().isLogin) {
                    if (loginDialog != null && !loginDialog.isShowing()) {
                        loginDialog.show();
                    }
                    return;
                }

                if (commonShareDialog != null && commonShareDialog.isShowing()) {
                    commonShareDialog.dismiss();
                }
                Intent intent = new Intent(this, ReportInfoActivity.class);
                intent.putExtra("rid", "-1");
                intent.putExtra("report_type", 2);
                startActivity(intent);
                break;
            case R.id.layout_to_home:
                Intent intent1 = new Intent(this, MainActivity.class);
                startActivity(intent1);
                finish();
                break;

            case R.id.iv_normal_close_share:
                dismissShareView();
                break;
            case R.id.layout_normal_weixin:
                web.setTitle(tempStr);//标题
                web.setDescription(tempStr);
                web.setThumb(normalImage);
                shareAction.withMedia(web).setPlatform(SHARE_MEDIA.WEIXIN).share();
                break;
            case R.id.layout_normal_circle:
                web.setTitle(tempStr);//标题
                web.setDescription(tempStr);
                web.setThumb(normalImage);
                shareAction.withMedia(web).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE).share();
                break;
            case R.id.layout_normal_qq_friends:
                web.setTitle(tempStr);//标题
                web.setDescription(tempStr);
                web.setThumb(normalImage);
                shareAction.withMedia(web).setPlatform(SHARE_MEDIA.QQ).share();
                break;
            case R.id.layout_normal_qzone:
                web.setTitle(tempStr);//标题
                web.setDescription(tempStr);
                web.setThumb(normalImage);
                shareAction.withMedia(web).setPlatform(SHARE_MEDIA.QZONE).share();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(MessageEvent messageEvent) {
        if (messageEvent.getMessage().equals("add_note_type")) {
            //发帖后添加到首页
            //noteMultipleAdapter.addData(0, messageEvent.getAddNoteInfo());
            //mCommunityTypeListView.scrollToPosition(0);


            //topicId = messageEvent.getTopicId();
            //currentPage = 1;
            //noteTypePresenterImp.getNoteTypeData(topicId, currentPage, 1, App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "");

            noteMultipleAdapter.addData(0, messageEvent.getAddNoteInfo());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 加载feed广告
     */
    private void loadListAd() {
        Logger.i("dpi--->" + ScreenUtils.getScreenDensityDpi() + "density--->" + ScreenUtils.getScreenDensity());
        float expressViewWidth = ScreenUtils.getScreenWidth() / ScreenUtils.getScreenDensity();
        float expressViewHeight = 0;

        //step4:创建feed广告请求类型参数AdSlot,具体参数含义参考文档
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId("945144473")
                .setSupportDeepLink(true)
                .setExpressViewAcceptedSize(expressViewWidth, expressViewHeight) //期望模板广告view的size,单位dp
                .setAdCount(1) //请求广告数量为1到3条
                .build();
        //step5:请求广告，调用feed广告异步请求接口，加载到广告后，拿到广告素材自定义渲染
        mTTAdNative.loadNativeExpressAd(adSlot, new TTAdNative.NativeExpressAdListener() {
            @Override
            public void onError(int code, String message) {
                Logger.i("feed error--->" + code + "---message--->" + message);
            }

            @Override
            public void onNativeExpressAdLoad(List<TTNativeExpressAd> ads) {
                if (ads == null || ads.isEmpty()) {
                    Logger.i("on FeedAdLoaded: ad is null!");
                    return;
                }
                bindAdListener(ads);
            }
        });
    }

    private void bindAdListener(final List<TTNativeExpressAd> ads) {
        for (int i = 0; i < ads.size(); i++) {
            final TTNativeExpressAd adTmp = ads.get(i);
            int tempIndex = currentPage * pageSize * (i + 1);
            if (tempIndex >= noteMultipleAdapter.getData().size()) {
                return;
            }

            NoteInfo tempHeadInfo = noteMultipleAdapter.getData().get(tempIndex);
            tempHeadInfo.setTtNativeExpressAd(adTmp);
            noteMultipleAdapter.getData().set(tempIndex, tempHeadInfo);

            adTmp.setExpressInteractionListener(new TTNativeExpressAd.ExpressAdInteractionListener() {
                @Override
                public void onAdClicked(View view, int type) {
                    //TToast.show(NativeExpressListActivity.this, "广告被点击");
                }

                @Override
                public void onAdShow(View view, int type) {
                    //TToast.show(NativeExpressListActivity.this, "广告展示");
                }

                @Override
                public void onRenderFail(View view, String msg, int code) {
                    //TToast.show(NativeExpressListActivity.this, msg + " code:" + code);
                }

                @Override
                public void onRenderSuccess(View view, float width, float height) {
                    Logger.i("feed render success--->" + tempIndex);
                    noteMultipleAdapter.notifyItemChanged(tempIndex);
                }
            });
            adTmp.render();
        }

        Logger.i("total size--->" + noteMultipleAdapter.getData().size());
    }


    /**
     * 关闭分享窗口
     */
    public void dismissShareView() {
        if (commonShareDialog != null && commonShareDialog.isShowing()) {
            commonShareDialog.dismiss();
        }
        if (normalShareDialog != null && normalShareDialog.isShowing()) {
            normalShareDialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        popBackStack();
    }

    @Override
    public void onRefresh() {
        mRefreshLayout.setRefreshing(true);
        currentPage = 1;
        noteTypePresenterImp.getNoteTypeData(topicId, currentPage, pageSize, 1, App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "");
    }
}
