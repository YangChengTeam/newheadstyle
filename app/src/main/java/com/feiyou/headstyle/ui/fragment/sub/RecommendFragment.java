package com.feiyou.headstyle.ui.fragment.sub;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.SizeUtils;
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
import com.feiyou.headstyle.bean.HeadInfo;
import com.feiyou.headstyle.bean.MessageEvent;
import com.feiyou.headstyle.bean.NoteInfo;
import com.feiyou.headstyle.bean.NoteInfoRet;
import com.feiyou.headstyle.bean.ResultInfo;
import com.feiyou.headstyle.bean.UserInfo;
import com.feiyou.headstyle.bean.ZanResultRet;
import com.feiyou.headstyle.common.Constants;
import com.feiyou.headstyle.presenter.AddZanPresenterImp;
import com.feiyou.headstyle.presenter.FollowInfoPresenterImp;
import com.feiyou.headstyle.presenter.NoteDataPresenterImp;
import com.feiyou.headstyle.ui.activity.AddFriendsActivity;
import com.feiyou.headstyle.ui.activity.CommunityArticleActivity;
import com.feiyou.headstyle.ui.activity.CommunityType1Activity;
import com.feiyou.headstyle.ui.activity.PushNoteActivity;
import com.feiyou.headstyle.ui.activity.UserInfoActivity;
import com.feiyou.headstyle.ui.adapter.NoteMultipleAdapter;
import com.feiyou.headstyle.ui.adapter.TopicAdapter;
import com.feiyou.headstyle.ui.base.BaseFragment;
import com.feiyou.headstyle.ui.custom.LoginDialog;
import com.feiyou.headstyle.utils.TTAdManagerHolder;
import com.feiyou.headstyle.view.NoteDataView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

/**
 * Created by myflying on 2018/11/26.
 */
public class RecommendFragment extends BaseFragment implements NoteDataView, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mRefreshLayout;

    @BindView(R.id.recommend_list)
    RecyclerView mRecommendListView;

    @BindView(R.id.layout_no_data)
    LinearLayout noDataLayout;

    @BindView(R.id.tv_no_data)
    TextView mNoDataTiltTv;

    @BindView(R.id.tv_follow_user)
    TextView mFollowUserTv;

    @BindView(R.id.avi)
    AVLoadingIndicatorView avi;

    @BindView(R.id.fab)
    FloatingActionButton mFabButton;

    View topView;

    RecyclerView mTopicListView;

    TopicAdapter topicAdapter;

    //NoteInfoAdapter noteInfoAdapter;

    NoteMultipleAdapter noteMultipleAdapter;

    private NoteDataPresenterImp noteDataPresenterImp;

    private FollowInfoPresenterImp followInfoPresenterImp;

    private AddZanPresenterImp addZanPresenterImp;

    private int currentPage = 1;

    private int pageSize = 20;

    private int currentClickIndex;

    private UserInfo userInfo;

    LoginDialog loginDialog;

    private int communityType = 2;

    private ShareAction shareAction;

    BottomSheetDialog bottomSheetDialog;

    private int errorType = 1;

    private TTAdNative mTTAdNative;

    public static RecommendFragment getInstance() {
        return new RecommendFragment();
    }

    public static RecommendFragment newInstance(int type) {
        RecommendFragment fragment = new RecommendFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("community_type", type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected View onCreateView() {
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_tab_recommend, null);

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        ButterKnife.bind(this, root);
        initData();
        return root;
    }

    public void initData() {
        //step1:初始化sdk
        TTAdManager ttAdManager = TTAdManagerHolder.get();
        mTTAdNative = ttAdManager.createAdNative(getActivity());

        mRefreshLayout.setOnRefreshListener(this);
        //设置进度View样式的大小，只有两个值DEFAULT和LARGE
        //设置进度View下拉的起始点和结束点，scale 是指设置是否需要放大或者缩小动画
        mRefreshLayout.setProgressViewOffset(true, -0, 200);
        //设置进度View下拉的结束点，scale 是指设置是否需要放大或者缩小动画
        mRefreshLayout.setProgressViewEndTarget(true, 180);
        //设置进度View的组合颜色，在手指上下滑时使用第一个颜色，在刷新中，会一个个颜色进行切换
        mRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getActivity(), R.color.colorPrimary), Color.RED, Color.YELLOW, Color.BLUE);

        //设置触发刷新的距离
        mRefreshLayout.setDistanceToTriggerSync(200);
        //如果child是自己自定义的view，可以通过这个回调，告诉mSwipeRefreshLayoutchild是否可以滑动
        mRefreshLayout.setOnChildScrollUpCallback(null);

        if (shareAction == null) {
            shareAction = new ShareAction(getActivity());
            shareAction.setCallback(shareListener);//回调监听器
        }

        bottomSheetDialog = new BottomSheetDialog(getActivity());
        View shareView = LayoutInflater.from(getActivity()).inflate(R.layout.share_dialog_view, null);
        ImageView mCloseImageView = shareView.findViewById(R.id.iv_close_share);
        LinearLayout weixinLayout = shareView.findViewById(R.id.layout_weixin);
        LinearLayout circleLayout = shareView.findViewById(R.id.layout_circle);
        LinearLayout qqLayout = shareView.findViewById(R.id.layout_qq_friends);
        LinearLayout qzoneLayout = shareView.findViewById(R.id.layout_qzone);
        weixinLayout.setOnClickListener(this);
        circleLayout.setOnClickListener(this);
        qqLayout.setOnClickListener(this);
        qzoneLayout.setOnClickListener(this);
        mCloseImageView.setOnClickListener(this);
        bottomSheetDialog.setContentView(shareView);

        Bundle bundle = getArguments();
        if (bundle != null && bundle.getInt("community_type") > 0) {
            communityType = bundle.getInt("community_type");
        }

        Logger.i("communityType" + communityType);

        noteMultipleAdapter = new NoteMultipleAdapter(null, 1);
        mRecommendListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecommendListView.setAdapter(noteMultipleAdapter);

        if (communityType == 2) {
            //顶部view
            topView = LayoutInflater.from(getActivity()).inflate(R.layout.recommend_top, null);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(80));
            topView.setLayoutParams(params);

            mTopicListView = topView.findViewById(R.id.topic_list_view);
            topicAdapter = new TopicAdapter(getActivity(), null);
            mTopicListView.setLayoutManager(new GridLayoutManager(getActivity(), 5));
            mTopicListView.setAdapter(topicAdapter);
            topicAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    Intent intent = new Intent(getActivity(), CommunityType1Activity.class);
                    intent.putExtra("topic_id", topicAdapter.getData().get(position).getId());
                    intent.putExtra("topic_index", position);
                    startActivity(intent);
                }
            });
            noteMultipleAdapter.setHeaderView(topView);
        }

        FrameLayout.LayoutParams listParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        listParams.setMargins(0, 0, 0, SizeUtils.dp2px(48));
        mRefreshLayout.setLayoutParams(listParams);

        noteMultipleAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getActivity(), CommunityArticleActivity.class);
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
                    followInfoPresenterImp.addFollow(App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "", noteMultipleAdapter.getData().get(position).getUserId());
                }
                if (view.getId() == R.id.layout_item_zan) {
                    String messageId = noteMultipleAdapter.getData().get(position).getId();
                    addZanPresenterImp.addZan(1, App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "", noteMultipleAdapter.getData().get(position).getUserId(), messageId, "", "", 1);
                }
                if (view.getId() == R.id.iv_user_head) {
                    if (view.getId() == R.id.iv_user_head) {
                        Intent intent = new Intent(getActivity(), UserInfoActivity.class);
                        intent.putExtra("user_id", noteMultipleAdapter.getData().get(position).getUserId());
                        startActivity(intent);
                    }
                }

                //分享
                if (view.getId() == R.id.layout_note_share) {
                    if (bottomSheetDialog != null && !bottomSheetDialog.isShowing()) {
                        bottomSheetDialog.show();
                    }
                }
            }
        });

        noteMultipleAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                currentPage++;
                noteDataPresenterImp.getNoteData(currentPage, 2, userInfo != null ? userInfo.getId() : "");
            }
        }, mRecommendListView);

        mRecommendListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int topRowVerticalPosition = (recyclerView == null || recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();

                Logger.i("recomment height--->" + topRowVerticalPosition);

                mRefreshLayout.setEnabled(topRowVerticalPosition >= 0);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        loginDialog = new LoginDialog(getActivity(), R.style.login_dialog);
        noteDataPresenterImp = new NoteDataPresenterImp(this, getActivity());
        followInfoPresenterImp = new FollowInfoPresenterImp(this, getActivity());
        addZanPresenterImp = new AddZanPresenterImp(this, getActivity());

        noteDataPresenterImp.getNoteData(currentPage, 2, userInfo != null ? userInfo.getId() : "");
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!StringUtils.isEmpty(SPUtils.getInstance().getString(Constants.USER_INFO))) {
            Logger.i(SPUtils.getInstance().getString(Constants.USER_INFO));
            userInfo = JSON.parseObject(SPUtils.getInstance().getString(Constants.USER_INFO), new TypeReference<UserInfo>() {
            });
        }

        if (loginDialog != null && loginDialog.isShowing()) {
            loginDialog.dismiss();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(MessageEvent messageEvent) {
        if (messageEvent.getMessage().equals("login_success")) {
            onResume();
        } else if (messageEvent.getMessage().equals("add_note")) {
            //发帖后添加到首页
            noteMultipleAdapter.addData(0, messageEvent.getAddNoteInfo());
        } else {
            if (communityType == 2) {
                topicAdapter.setNewData(App.topicInfoList);
            }
        }
    }

    @OnClick(R.id.tv_follow_user)
    void addFriends() {
        if (errorType == 1) {
            Intent intent = new Intent(getActivity(), AddFriendsActivity.class);
            startActivity(intent);
        } else {
            noDataLayout.setVisibility(View.GONE);
            avi.show();
            onRefresh();
        }
    }

    @OnClick(R.id.fab)
    void fabButton() {
        if (!App.getApp().isLogin) {
            if (loginDialog != null && !loginDialog.isShowing()) {
                loginDialog.show();
            }
            return;
        }

        Intent intent = new Intent(getActivity(), PushNoteActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void dismissProgress() {
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void loadDataSuccess(ResultInfo tData) {
        errorType = 1;
        avi.hide();
        mRefreshLayout.setRefreshing(false);
        if (tData != null && tData.getCode() == Constants.SUCCESS) {
            if (tData instanceof NoteInfoRet) {
                Logger.i("note size--->" + ((NoteInfoRet) tData).getData().size());
                if (((NoteInfoRet) tData).getData() != null && ((NoteInfoRet) tData).getData().size() > 0) {
                    //获取第一个帖子的ID，为后面的“评论任务”提供帖子ID
                    App.getApp().setRandomNoteId(((NoteInfoRet) tData).getData().get(0).getId());
                    mRecommendListView.setVisibility(View.VISIBLE);
                    noDataLayout.setVisibility(View.GONE);
                    if (currentPage == 1) {

                        List<NoteInfo> tempList = ((NoteInfoRet) tData).getData();
                        for (NoteInfo noteInfo : tempList) {
                            noteInfo.setItemType(NoteInfo.NOTE_NORMAL);
                        }

                        //在集合后添加广告位的对象
                        for (int i = 0; i < 3; i++) {
                            NoteInfo tempNoteInfo = new NoteInfo(NoteInfo.NOTE_AD);
                            tempList.add(currentPage * 6 * (i + 1), tempNoteInfo);
                        }

                        noteMultipleAdapter.setNewData(tempList);
                    } else {
                        List<NoteInfo> tempList = ((NoteInfoRet) tData).getData();
                        for (NoteInfo noteInfo : tempList) {
                            noteInfo.setItemType(NoteInfo.NOTE_NORMAL);
                        }

                        noteMultipleAdapter.addData(tempList);
                        //在集合后添加广告位的对象

                        for (int i = 0; i < 3; i++) {
                            NoteInfo tempNoteInfo = new NoteInfo(NoteInfo.NOTE_AD);
                            noteMultipleAdapter.getData().add(currentPage * 6 * (i + 1), tempNoteInfo);
                            noteMultipleAdapter.notifyItemChanged(currentPage * 6 * (i + 1));
                        }
                    }

                    if (currentPage == 1) {
                        if (((NoteInfoRet) tData).getData().size() == pageSize + 3) {
                            noteMultipleAdapter.loadMoreComplete();
                        } else {
                            noteMultipleAdapter.loadMoreEnd(true);
                        }
                    } else {
                        if (((NoteInfoRet) tData).getData().size() == pageSize) {
                            noteMultipleAdapter.loadMoreComplete();
                        } else {
                            noteMultipleAdapter.loadMoreEnd(true);
                        }
                    }
                    loadListAd();

                } else {
                    mRecommendListView.setVisibility(View.GONE);
                    noDataLayout.setVisibility(View.VISIBLE);
                    mNoDataTiltTv.setText(communityType == 1 ? "暂无数据" : "还没有关注任何人");
                }

            }

            if (tData instanceof FollowInfoRet) {
                if (((FollowInfoRet) tData).getData() != null) {
                    int isGuan = ((FollowInfoRet) tData).getData().getIsGuan();
                    Toasty.normal(getActivity(), isGuan == 0 ? "已取消" : "已关注").show();
                    noteMultipleAdapter.getData().get(currentClickIndex).setIsGuan(isGuan);
                    String gUserId = noteMultipleAdapter.getData().get(currentClickIndex).getUserId();
                    for (NoteInfo noteInfo : noteMultipleAdapter.getData()) {
                        if (noteInfo.getUserId().equals(gUserId)) {
                            noteInfo.setIsGuan(isGuan);
                        }
                    }
                    noteMultipleAdapter.notifyDataSetChanged();
                } else {
                    Toasty.normal(getActivity(), StringUtils.isEmpty(tData.getMsg()) ? "操作错误" : tData.getMsg()).show();
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
            if (tData instanceof NoteInfoRet) {
                if (currentPage == 1) {
                    mRecommendListView.setVisibility(View.GONE);
                    noDataLayout.setVisibility(View.VISIBLE);
                    mNoDataTiltTv.setText(communityType == 1 ? "暂无数据" : "还没有关注任何人");
                }
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
        errorType = 2;
        avi.hide();
        mRefreshLayout.setRefreshing(false);
        if (currentPage == 1) {
            mRecommendListView.setVisibility(View.GONE);
            noDataLayout.setVisibility(View.VISIBLE);
            mNoDataTiltTv.setText("数据加载失败\n请检查网络后重试");
            mFollowUserTv.setText("重新加载");
            mFollowUserTv.setTextColor(ContextCompat.getColor(getActivity(), R.color.search_number_color2));
            mFollowUserTv.setTextSize(16);
        }
    }

    @Override
    public void onRefresh() {
        mRefreshLayout.setRefreshing(true);
        currentPage = 1;
        noteDataPresenterImp.getNoteData(currentPage, 2, userInfo != null ? userInfo.getId() : "");
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
            Toast.makeText(getActivity(), "分享成功", Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            dismissShareView();
            Toast.makeText(getActivity(), "分享失败", Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            dismissShareView();
            Toast.makeText(getActivity(), "取消分享", Toast.LENGTH_LONG).show();
        }
    };

    @Override
    public void onClick(View view) {

        NoteInfo tempNoteInfo = noteMultipleAdapter.getData().get(currentClickIndex);
        String shareContent = StringUtils.isEmpty(tempNoteInfo.getContent()) ? "快来试试炫酷的头像吧" : tempNoteInfo.getContent();
        UMWeb web = new UMWeb("http://gx.qqtn.com");
        if (shareAction != null) {
            UMImage image = new UMImage(getActivity(), R.drawable.app_share);
            if (tempNoteInfo.getImageArr() != null && tempNoteInfo.getImageArr().length > 0) {
                image = new UMImage(getActivity(), tempNoteInfo.getImageArr()[0]);
                image.compressStyle = UMImage.CompressStyle.QUALITY;
            }
            web.setTitle(shareContent);//标题
            web.setThumb(image);  //缩略图
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
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(getActivity()).onActivityResult(requestCode, resultCode, data);
    }


    /**
     * 加载feed广告
     */
    private void loadListAd() {
        Logger.i("dpi--->" + ScreenUtils.getScreenDensityDpi() + "density--->" + ScreenUtils.getScreenDensity());
        float expressViewWidth = ScreenUtils.getScreenDensityDpi() <= 320 ? 340 : ScreenUtils.getScreenDensityDpi();
        float expressViewHeight = 0;

        //step4:创建feed广告请求类型参数AdSlot,具体参数含义参考文档
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId("945144473")
                .setSupportDeepLink(true)
                .setExpressViewAcceptedSize(expressViewWidth, expressViewHeight) //期望模板广告view的size,单位dp
                .setAdCount(3) //请求广告数量为1到3条
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
            int tempIndex = currentPage * 6 * (i + 1);
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
                    Logger.i("feed render success--->");
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
        if (bottomSheetDialog != null && bottomSheetDialog.isShowing()) {
            bottomSheetDialog.dismiss();
        }
    }
}
