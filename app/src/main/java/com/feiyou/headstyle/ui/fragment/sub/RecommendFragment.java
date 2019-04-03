package com.feiyou.headstyle.ui.fragment.sub;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ajguan.library.EasyRefreshLayout;
import com.ajguan.library.LoadModel;
import com.ajguan.library.view.SimpleLoadMoreView;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feiyou.headstyle.App;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.FollowInfoRet;
import com.feiyou.headstyle.bean.HeadInfo;
import com.feiyou.headstyle.bean.HeadType;
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
import com.feiyou.headstyle.ui.activity.CommunityArticleActivity;
import com.feiyou.headstyle.ui.activity.CommunityType1Activity;
import com.feiyou.headstyle.ui.activity.CommunityTypeActivity;
import com.feiyou.headstyle.ui.activity.PushNoteActivity;
import com.feiyou.headstyle.ui.activity.ReportInfoActivity;
import com.feiyou.headstyle.ui.activity.Test1Activity;
import com.feiyou.headstyle.ui.activity.UserInfoActivity;
import com.feiyou.headstyle.ui.adapter.NoteInfoAdapter;
import com.feiyou.headstyle.ui.adapter.TopicAdapter;
import com.feiyou.headstyle.ui.base.BaseFragment;
import com.feiyou.headstyle.ui.custom.GlideRoundTransform;
import com.feiyou.headstyle.ui.custom.LoginDialog;
import com.feiyou.headstyle.view.NoteDataView;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

/**
 * Created by myflying on 2018/11/26.
 */
public class RecommendFragment extends BaseFragment implements NoteDataView, View.OnClickListener {

    @BindView(R.id.easylayout)
    EasyRefreshLayout easyRefreshLayout;

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

    NoteInfoAdapter noteInfoAdapter;

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

    private View topRefreshView;

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

        noteInfoAdapter = new NoteInfoAdapter(getActivity(), null, 1);
        mRecommendListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecommendListView.setAdapter(noteInfoAdapter);

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
            noteInfoAdapter.setHeaderView(topView);
        }

        CoordinatorLayout.LayoutParams listParams = new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.MATCH_PARENT);
        listParams.setMargins(0, 0, 0, SizeUtils.dp2px(48));
        easyRefreshLayout.setLayoutParams(listParams);

        noteInfoAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getActivity(), CommunityArticleActivity.class);
                intent.putExtra("msg_id", noteInfoAdapter.getData().get(position).getId());
                startActivity(intent);
            }
        });

        noteInfoAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
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
                    followInfoPresenterImp.addFollow(App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "", noteInfoAdapter.getData().get(position).getUserId());
                }
                if (view.getId() == R.id.layout_item_zan) {
                    String messageId = noteInfoAdapter.getData().get(position).getId();
                    addZanPresenterImp.addZan(1, App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "", noteInfoAdapter.getData().get(position).getUserId(), messageId, "", "", 1);
                }
                if (view.getId() == R.id.iv_user_head) {
                    if (view.getId() == R.id.iv_user_head) {
                        Intent intent = new Intent(getActivity(), UserInfoActivity.class);
                        intent.putExtra("user_id", noteInfoAdapter.getData().get(position).getUserId());
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

        noteInfoAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                currentPage++;
                noteDataPresenterImp.getNoteData(currentPage, 2, userInfo != null ? userInfo.getId() : "");
            }
        }, mRecommendListView);

        easyRefreshLayout.addEasyEvent(new EasyRefreshLayout.EasyEvent() {
            @Override
            public void onLoadMore() {
                //none
            }

            @Override
            public void onRefreshing() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        onRefresh();
                    }
                }, 200);
            }
        });

        topRefreshView = LayoutInflater.from(getActivity()).inflate(R.layout.refresh_top_view, null);
        easyRefreshLayout.setRefreshHeadView(topRefreshView);
        easyRefreshLayout.setLoadMoreModel(LoadModel.NONE);

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
            noteInfoAdapter.addData(0, messageEvent.getAddNoteInfo());
        } else {
            if (communityType == 2) {
                topicAdapter.setNewData(App.topicInfoList);
            }
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
        easyRefreshLayout.refreshComplete();
        easyRefreshLayout.loadMoreComplete();
    }

    @Override
    public void loadDataSuccess(ResultInfo tData) {
        avi.hide();
        easyRefreshLayout.refreshComplete();
        easyRefreshLayout.loadMoreComplete();
        if (tData != null && tData.getCode() == Constants.SUCCESS) {
            if (tData instanceof NoteInfoRet) {
                if (((NoteInfoRet) tData).getData() != null && ((NoteInfoRet) tData).getData().size() > 0) {
                    mRecommendListView.setVisibility(View.VISIBLE);
                    noDataLayout.setVisibility(View.GONE);
                    if (currentPage == 1) {
                        noteInfoAdapter.setNewData(((NoteInfoRet) tData).getData());
                    } else {
                        noteInfoAdapter.addData(((NoteInfoRet) tData).getData());
                    }

                    if (((NoteInfoRet) tData).getData().size() == pageSize) {
                        noteInfoAdapter.loadMoreComplete();
                    } else {
                        noteInfoAdapter.loadMoreEnd(true);
                    }
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
                    noteInfoAdapter.getData().get(currentClickIndex).setIsGuan(isGuan);
                    String gUserId = noteInfoAdapter.getData().get(currentClickIndex).getUserId();
                    for (NoteInfo noteInfo : noteInfoAdapter.getData()) {
                        if (noteInfo.getUserId().equals(gUserId)) {
                            noteInfo.setIsGuan(isGuan);
                        }
                    }
                    noteInfoAdapter.notifyDataSetChanged();
                } else {
                    Toasty.normal(getActivity(), StringUtils.isEmpty(tData.getMsg()) ? "操作错误" : tData.getMsg()).show();
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
        avi.hide();
        easyRefreshLayout.refreshComplete();
        easyRefreshLayout.loadMoreComplete();
    }

    //TODO
    public void onRefresh() {
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

        NoteInfo tempNoteInfo = noteInfoAdapter.getData().get(currentClickIndex);
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
     * 关闭分享窗口
     */
    public void dismissShareView() {
        if (bottomSheetDialog != null && bottomSheetDialog.isShowing()) {
            bottomSheetDialog.dismiss();
        }
    }
}
