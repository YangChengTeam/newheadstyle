package com.feiyou.headstyle.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.PathUtils;
import com.blankj.utilcode.util.PhoneUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdManager;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTRewardVideoAd;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feiyou.headstyle.App;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.AdInfo;
import com.feiyou.headstyle.bean.BannerInfo;
import com.feiyou.headstyle.bean.EveryDayHbRet;
import com.feiyou.headstyle.bean.HomeDataRet;
import com.feiyou.headstyle.bean.HomeDataWrapper;
import com.feiyou.headstyle.bean.MessageEvent;
import com.feiyou.headstyle.bean.PlayGameInfo;
import com.feiyou.headstyle.bean.ReceiveUserInfo;
import com.feiyou.headstyle.bean.ResultInfo;
import com.feiyou.headstyle.bean.SeeVideoInfo;
import com.feiyou.headstyle.bean.TaskRecordInfoRet;
import com.feiyou.headstyle.bean.UserInfo;
import com.feiyou.headstyle.common.Constants;
import com.feiyou.headstyle.common.GlideImageLoader;
import com.feiyou.headstyle.presenter.EveryDayHongBaoPresenterImp;
import com.feiyou.headstyle.presenter.HomeDataPresenterImp;
import com.feiyou.headstyle.presenter.RecordInfoPresenterImp;
import com.feiyou.headstyle.presenter.TaskRecordInfoPresenterImp;
import com.feiyou.headstyle.ui.activity.AdActivity;
import com.feiyou.headstyle.ui.activity.Collection2Activity;
import com.feiyou.headstyle.ui.activity.CommunityArticleActivity;
import com.feiyou.headstyle.ui.activity.EveryDayHongBaoActivity;
import com.feiyou.headstyle.ui.activity.HeadListActivity;
import com.feiyou.headstyle.ui.activity.HeadShowActivity;
import com.feiyou.headstyle.ui.activity.Main1Activity;
import com.feiyou.headstyle.ui.activity.MoreTypeActivity;
import com.feiyou.headstyle.ui.activity.SearchActivity;
import com.feiyou.headstyle.ui.adapter.HeadInfoAdapter;
import com.feiyou.headstyle.ui.adapter.HeadTypeAdapter;
import com.feiyou.headstyle.ui.base.BaseFragment;
import com.feiyou.headstyle.ui.custom.EveryDayHongBaoDialog;
import com.feiyou.headstyle.ui.custom.OpenDialog;
import com.feiyou.headstyle.ui.custom.RoundedCornersTransformation;
import com.feiyou.headstyle.utils.MyTimeUtil;
import com.feiyou.headstyle.utils.RandomUtils;
import com.feiyou.headstyle.utils.TTAdManagerHolder;
import com.feiyou.headstyle.view.HomeDataView;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.orhanobut.logger.Logger;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;
import com.wang.avi.AVLoadingIndicatorView;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

/**
 * Created by myflying on 2019/1/3.
 */
public class Home1Fragment extends BaseFragment implements HomeDataView, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, OpenDialog.ConfigListener, CardWindowFragment.AdDismissListener, EveryDayHongBaoDialog.EveryDayHongBaoListener {

    LinearLayout mSearchLayout;

    LinearLayout mSearchWrapperLayout;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mRefreshLayout;

    @BindView(R.id.layout_top_refresh1)
    RelativeLayout refreshLayout1;

    @BindView(R.id.home_head_list)
    RecyclerView mHeadInfoListView;

    @BindView(R.id.avi)
    AVLoadingIndicatorView avi;

    @BindView(R.id.layout_no_data)
    LinearLayout mNoDataLayout;

    @BindView(R.id.tv_reload)
    TextView mReloadTv;

    @BindView(R.id.iv_home_float)
    ImageView mHomeFloatIv;

    @BindView(R.id.layout_float_hb)
    FrameLayout mFloatHbLayout;

    @BindView(R.id.iv_close_float_hb)
    ImageView mCloseFloatHbIv;

    Banner mBanner;

    RecyclerView mHeadTypeList;

    LinearLayout mRecommendLayout;

    RecyclerView mCommunityHeadList;

    LinearLayout refreshLayout2;

    RelativeLayout floatLayout;

    View mLineView;

    ImageView mAdImageView;

    LinearLayout mAdLayout;

    HeadTypeAdapter headTypeAdapter;

    HeadInfoAdapter headInfoAdapter;

    private int searchLayoutTop;

    private HomeDataPresenterImp homeDataPresenterImp;

    private RecordInfoPresenterImp recordInfoPresenterImp;

    private int randomPage = -1;

    private int currentPage = 1;

    private int pageSize = 30;

    private boolean isFirstLoad = true;

    private List<BannerInfo> bannerInfos;

    private View topView;

    private View footView;

    private ImageView mLoadingView;

    private int marginTopHeight;

    GridLayoutManager gridLayoutManager;

    private String isChange = "";//默认是""

    private AdInfo adInfo;

    OpenDialog openDialog;

    private BannerInfo bannerInfo;

    private int clickType = 1;//1：banner，2：广告

    BaseDownloadTask task;

    CardWindowFragment cardWindowFragment;

    AdInfo openAdInfo;

    EveryDayHongBaoDialog everyDayHongBaoDialog;

    private TTAdNative mTTAdNative;

    private TTRewardVideoAd mttRewardVideoAd;

    TaskRecordInfoPresenterImp taskRecordInfoPresenterImp;

    EveryDayHongBaoPresenterImp everyDayHongBaoPresenterImp;

    private String taskId = "";

    private String recordId = "";

    private UserInfo mUserInfo;

    private boolean isRefreshHongBao;

    private List<ReceiveUserInfo> receiveUserList;

    private PlayGameInfo playGameInfo;

    private SeeVideoInfo gameSeeVideoInfo;

    private boolean clickAnyWhere;

    @Override
    protected View onCreateView() {
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment1, null);
        ButterKnife.bind(this, root);
        EventBus.getDefault().register(this);
        FileDownloader.setup(getActivity());
        initTopView();
        initBanner();
        initData();
        return root;
    }

    public void initTopView() {
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

        topView = LayoutInflater.from(getActivity()).inflate(R.layout.home_top, null);
        footView = LayoutInflater.from(getActivity()).inflate(R.layout.list_bottom, null);

        mBanner = topView.findViewById(R.id.banner);
        mHeadTypeList = topView.findViewById(R.id.head_type_list);
        mCommunityHeadList = topView.findViewById(R.id.community_head_list);
        refreshLayout2 = topView.findViewById(R.id.layout_top_refresh2);
        floatLayout = topView.findViewById(R.id.float_layout);
        mLineView = topView.findViewById(R.id.main_line_view);
        mAdImageView = topView.findViewById(R.id.iv_home_ad);
        mAdLayout = topView.findViewById(R.id.layout_ad);
        TextView tvRefresh2 = topView.findViewById(R.id.tv_refresh2);

        mSearchWrapperLayout = topView.findViewById(R.id.layout_search_wrapper);
        mSearchLayout = topView.findViewById(R.id.layout_search);

        mSearchLayout.setOnClickListener(this);
        tvRefresh2.setOnClickListener(this);
        LinearLayout.LayoutParams searchParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(48));
        searchParams.setMargins(0, BarUtils.getStatusBarHeight(), 0, 0);
        mSearchWrapperLayout.setLayoutParams(searchParams);

        FrameLayout.LayoutParams refreshParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(48) + BarUtils.getStatusBarHeight());
        refreshParams.setMargins(0, 0, 0, 0);
        refreshLayout1.setLayoutParams(refreshParams);


        openDialog = new OpenDialog(getActivity(), R.style.login_dialog);
        openDialog.setConfigListener(this);

        recordInfoPresenterImp = new RecordInfoPresenterImp(this, getActivity());
        everyDayHongBaoPresenterImp = new EveryDayHongBaoPresenterImp(this, getActivity());

        //广告模块
        LinearLayout adLayout = topView.findViewById(R.id.layout_ad);
        adLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickType = 2;
                if (adInfo != null) {
                    switch (adInfo.getType()) {
                        case 1:
                            addRecord(adInfo.getId());
                            Intent intent = new Intent(getActivity(), AdActivity.class);
                            intent.putExtra("open_url", adInfo.getJumpPath());
                            startActivity(intent);
                            break;
                        case 2:
                            if (task != null && task.isRunning()) {
                                Toasty.normal(getActivity(), "正在下载打开请稍后...").show();
                            } else {
                                if (NetworkUtils.isMobileData()) {
                                    openDialog.setTitle("温馨提示");
                                    openDialog.setContent("当前是移动网络，是否继续下载？");
                                } else {
                                    openDialog.setTitle("打开提示");
                                    openDialog.setContent("即将下载" + adInfo.getName());
                                }
                                openDialog.show();
                            }
                            break;
                        case 3:
                            openDialog.setTitle("打开提示");
                            openDialog.setContent("即将打开\"" + adInfo.getName() + "\"小程序");
                            openDialog.show();
                            break;
                        default:
                            break;
                    }
                }
            }
        });

        //加载更多动画
        mLoadingView = footView.findViewById(R.id.iv_loading);
        Glide.with(getActivity()).load(R.drawable.list_loading).into(mLoadingView);
    }

    public void initData() {
        Logger.i("home fragment init data--->");

        mUserInfo = App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo() : new UserInfo();

        TTAdManager ttAdManager = TTAdManagerHolder.get();

        //step3:创建TTAdNative对象,用于调用广告请求接口
        mTTAdNative = ttAdManager.createAdNative(getActivity());

        //初始化加载红包信息
        loadAd("920819401", TTAdConstant.VERTICAL, 100);
        try {
            everyDayHongBaoPresenterImp.everyDayHongBaoInfo(StringUtils.isEmpty(mUserInfo.getId()) ? "0" : mUserInfo.getId(), StringUtils.isEmpty(mUserInfo.getOpenid()) ? "0" : mUserInfo.getOpenid(), PhoneUtils.getIMEI());
        } catch (SecurityException e) {
            e.printStackTrace();
        }

        everyDayHongBaoDialog = new EveryDayHongBaoDialog(getActivity(), R.style.login_dialog);
        everyDayHongBaoDialog.setEveryDayHongBaoListener(this);

        homeDataPresenterImp = new HomeDataPresenterImp(this, getActivity());
        taskRecordInfoPresenterImp = new TaskRecordInfoPresenterImp(this, getActivity());

        headTypeAdapter = new HeadTypeAdapter(getActivity(), null);
        mHeadTypeList.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        mHeadTypeList.setAdapter(headTypeAdapter);
        headTypeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (position == headTypeAdapter.getData().size() - 1) {
                    Intent intent = new Intent(getActivity(), MoreTypeActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), HeadListActivity.class);
                    intent.putExtra("tag_name", headTypeAdapter.getData().get(position).getTagsname());
                    intent.putExtra("tag_id", headTypeAdapter.getData().get(position).getId());
                    startActivity(intent);
                }
            }
        });
        //加载悬浮的红包
        Glide.with(getActivity()).load(R.drawable.home_float).into(mHomeFloatIv);

        gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        headInfoAdapter = new HeadInfoAdapter(getActivity(), null);
        mHeadInfoListView.setLayoutManager(gridLayoutManager);
        headInfoAdapter.addHeaderView(topView);
        mHeadInfoListView.setAdapter(headInfoAdapter);

        FrameLayout.LayoutParams listParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        listParams.setMargins(0, 0, 0, SizeUtils.dp2px(48));
        mRefreshLayout.setLayoutParams(listParams);

        mHeadInfoListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //在此做处理
                if (null != gridLayoutManager) {
                    //当前条目索引
                    int position = gridLayoutManager.findFirstVisibleItemPosition();

//
//                    int position = gridLayoutManager.findFirstVisibleItemPosition();
//                    //根据当前条目索引做判断处理。例如：如果在索引是0，
//                    //隐藏显示某个布局，索引大于0显示出来
//                    if (position > 0) {
//                        //做显示布局操作
//                        //view.setVisibility(View.VISIBLE);
//                        refreshLayout1.setVisibility(View.VISIBLE);
//                        refreshLayout2.setVisibility(View.INVISIBLE);
//                    } else {
//                        //做隐藏布局操作
//                        //view.setVisibility(View.GONE);
//                        refreshLayout1.setVisibility(View.INVISIBLE);
//                        refreshLayout2.setVisibility(View.VISIBLE);
//                    }


                    //根据索引来获取对应的itemView
                    View firstVisiableChildView = gridLayoutManager.findViewByPosition(position);
                    //获取当前显示条目的高度
                    int itemHeight = firstVisiableChildView.getHeight();
                    //获取当前Recyclerview 偏移量
                    int flag = position * itemHeight - firstVisiableChildView.getTop() + SizeUtils.dp2px(48) + BarUtils.getActionBarHeight();
                    if (flag >= itemHeight) {
                        marginTopHeight = flag;
                        //做显示布局操作
                        refreshLayout1.setVisibility(View.VISIBLE);
                        refreshLayout2.setVisibility(View.INVISIBLE);
                    } else {
                        //做隐藏布局操作
                        refreshLayout1.setVisibility(View.INVISIBLE);
                        refreshLayout2.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        mHeadInfoListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

        headInfoAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                int jumpPage = randomPage + position / pageSize;
                int jumpPosition = position % pageSize;

                Logger.i("jumpPage page--->" + jumpPage + "---jumpPosition--->" + jumpPosition);

                Intent intent = new Intent(getActivity(), HeadShowActivity.class);
                intent.putExtra("from_type", 1);
                intent.putExtra("jump_page", jumpPage);
                intent.putExtra("jump_position", jumpPosition);
                startActivity(intent);
            }
        });

        headInfoAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                isFirstLoad = false;
                isChange = "";
                currentPage++;
                homeDataPresenterImp.getData(mUserInfo.getId(), currentPage + "", "", "", 0);
            }
        }, mHeadInfoListView);

        homeDataPresenterImp.getData(mUserInfo.getId(), "", "", "", 0);

        if (cardWindowFragment == null) {
            cardWindowFragment = new CardWindowFragment();
        }
        cardWindowFragment.setAdDismissListener(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(MessageEvent messageEvent) {
        if (messageEvent.getMessage().equals("permission_use")) {
            try {
                mUserInfo = App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo() : new UserInfo();
                everyDayHongBaoPresenterImp.everyDayHongBaoInfo(StringUtils.isEmpty(mUserInfo.getId()) ? "0" : mUserInfo.getId(), StringUtils.isEmpty(mUserInfo.getOpenid()) ? "0" : mUserInfo.getOpenid(), PhoneUtils.getIMEI());
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }

        if (messageEvent.getMessage().equals("login_success")) {
            isRefreshHongBao = true;
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getContext() != null && isVisibleToUser) {
            loadAd("920819401", TTAdConstant.VERTICAL, 100);

            Logger.i("home fragment setUserVisibleHint--->");
            try {
                mUserInfo = App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo() : new UserInfo();
                everyDayHongBaoPresenterImp.everyDayHongBaoInfo(StringUtils.isEmpty(mUserInfo.getId()) ? "0" : mUserInfo.getId(), StringUtils.isEmpty(mUserInfo.getOpenid()) ? "0" : mUserInfo.getOpenid(), PhoneUtils.getIMEI());
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Logger.i("home fragment onResume--->" + isFirstLoad);
        mUserInfo = App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo() : new UserInfo();

        if(!isFirstLoad){
            String smallKey = StringUtils.isEmpty(mUserInfo.getId()) ? Constants.SMALL_HONG_BAO_IS_CLOSE : Constants.SMALL_HONG_BAO + mUserInfo.getId();
            String lastCloseSmallDate = SPUtils.getInstance().getString(smallKey, "");

            if (!lastCloseSmallDate.equals(MyTimeUtil.getYearAndDay())) {
                mFloatHbLayout.setVisibility(View.VISIBLE);
            } else {
                mFloatHbLayout.setVisibility(View.GONE);
            }
        }
    }

    public void initBanner() {
        mBanner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                clickType = 1;
                bannerInfo = bannerInfos.get(position);
                if (bannerInfo.getType() == 1) {
                    Intent intent = new Intent(getActivity(), Collection2Activity.class);
                    intent.putExtra("banner_id", bannerInfo.getId());
                    startActivity(intent);
                }
                if (bannerInfo.getType() == 2) {
                    Intent intent = new Intent(getActivity(), CommunityArticleActivity.class);
                    intent.putExtra("msg_id", bannerInfo.getJumpPath());
                    startActivity(intent);
                }
                if (bannerInfo.getType() == 3) {
                    openDialog.setTitle("打开小程序");
                    openDialog.setContent("是否打开\"" + bannerInfo.getName() + "\"小程序?");
                    openDialog.show();
                }
                if (bannerInfo.getType() == 4) {
                    addRecord(bannerInfo.getAdId());
                    Intent intent = new Intent(getActivity(), AdActivity.class);
                    intent.putExtra("ad_title", bannerInfo.getName());
                    intent.putExtra("open_url", bannerInfo.getJumpPath());
                    startActivity(intent);
                }
                if (bannerInfo.getType() == 5) {
                    if (task != null && task.isRunning()) {
                        Toasty.normal(getActivity(), "正在下载打开请稍后...").show();
                    } else {
                        if (NetworkUtils.isMobileData()) {
                            openDialog.setTitle("温馨提示");
                            openDialog.setContent("当前是移动网络，是否继续下载？");
                        } else {
                            openDialog.setTitle("打开提示");
                            openDialog.setContent("即将下载" + bannerInfo.getName());
                        }
                        openDialog.show();
                    }
                }
            }
        });
    }

    @OnClick(R.id.tv_refresh1)
    void refresh() {
        mRefreshLayout.setRefreshing(true);
        isFirstLoad = false;
        isChange = "1";
        homeDataPresenterImp.getData(mUserInfo.getId(), "", "", isChange, 0);
        gridLayoutManager.scrollToPosition(0);
    }

    @OnClick(R.id.tv_reload)
    void reLoad() {
        mRefreshLayout.setRefreshing(true);
        isFirstLoad = true;
        isChange = "1";
        homeDataPresenterImp.getData(mUserInfo.getId(), "", "", isChange, 0);
        gridLayoutManager.scrollToPosition(0);
    }

    @Override
    public void onStart() {
        super.onStart();
        mBanner.startAutoPlay();
    }

    @Override
    public void onStop() {
        super.onStop();
        mBanner.stopAutoPlay();
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
        Logger.i("home data--->" + JSON.toJSONString(tData));
        avi.hide();
        mRefreshLayout.setRefreshing(false);
        if (tData != null && tData.getCode() == Constants.SUCCESS) {
            mNoDataLayout.setVisibility(View.GONE);
            mHeadInfoListView.setVisibility(View.VISIBLE);

            HomeDataWrapper homeDataRet = null;
            if (tData instanceof HomeDataRet) {
                homeDataRet = ((HomeDataRet) tData).getData();
                if (homeDataRet != null) {
                    currentPage = homeDataRet.getPage();

                    //刷新或者第一次加载时，需要重新获取随机数
                    if (isFirstLoad || isChange.equals("1")) {
                        randomPage = currentPage;
                        Logger.i("random page--->" + randomPage);

                        //设置悬浮广告
                        Logger.i("setting ad--->" + JSON.toJSONString(homeDataRet.getSuspendAdInfo()));
                        App.getApp().setSuspendInfo(homeDataRet.getSuspendAdInfo());

                        openAdInfo = homeDataRet.getOpenAdInfo();
                        App.getApp().setMessageAdInfo(homeDataRet.getMessageAdInfo());
                    }

                    if (isFirstLoad) {
                        if (openAdInfo != null && SPUtils.getInstance().getBoolean("show_ad_window", true)) {
                            cardWindowFragment.setPopImageUrl(openAdInfo.getIco());
                            cardWindowFragment.setJumpPath(openAdInfo.getJumpPath());
                            cardWindowFragment.setAdTitle(openAdInfo.getName());
                            cardWindowFragment.show(getActivity().getFragmentManager(), "card_dialog");
                        }

                        //此处的信息，只需要设置一次
                        if (homeDataRet.getCategoryInfoList() != null && homeDataRet.getCategoryInfoList().size() > 0) {
                            headTypeAdapter.setNewData(homeDataRet.getCategoryInfoList());
                        }

                        if (homeDataRet.getAdList() != null && homeDataRet.getAdList().size() > 0) {
                            adInfo = homeDataRet.getAdList().get(0);
                            RequestOptions adOptions = new RequestOptions().skipMemoryCache(true);
                            adOptions.transform(new RoundedCornersTransformation(SizeUtils.dp2px(8), 1));
                            adOptions.override(ScreenUtils.getScreenWidth() - SizeUtils.dp2px(24), SizeUtils.dp2px(83));
                            Glide.with(getActivity()).load(adInfo.getIco()).apply(adOptions).into(mAdImageView);
                        } else {
                            mAdLayout.setVisibility(View.GONE);
                        }

                        if (homeDataRet.getBannerList() != null && homeDataRet.getBannerList().size() > 0) {
                            bannerInfos = homeDataRet.getBannerList();
                            List<String> urls = new ArrayList<>();
                            for (int i = 0; i < bannerInfos.size(); i++) {
                                urls.add(bannerInfos.get(i).getIco());
                            }
                            //设置图片加载器
                            mBanner.setImageLoader(new GlideImageLoader()).setImages(urls).start();
                        }
                    }

                    if (SPUtils.getInstance().getInt(Constants.TOTAL_COUNT, 0) > 0) {
                        if (homeDataRet.getMyTotalNum() > SPUtils.getInstance().getInt(Constants.TOTAL_COUNT, 0)) {
                            EventBus.getDefault().post(new MessageEvent("home_message_remind"));
                        }
                    }
                    SPUtils.getInstance().put(Constants.TOTAL_COUNT, homeDataRet.getMyTotalNum());

                    if (homeDataRet.getImagesList() != null && homeDataRet.getImagesList().size() > 0) {
                        if (isFirstLoad || isChange.equals("1")) {
                            headInfoAdapter.setNewData(homeDataRet.getImagesList());
                            isFirstLoad = false;
                            //刷新或者加载完数据后，设置ischange =""
                            isChange = "";
                        } else {
                            headInfoAdapter.addData(homeDataRet.getImagesList());
                        }

                        if (homeDataRet.getImagesList().size() == pageSize) {
                            headInfoAdapter.loadMoreComplete();
                        } else {
                            headInfoAdapter.loadMoreEnd(true);
                        }
                    }
                } else {
                    mNoDataLayout.setVisibility(View.VISIBLE);
                    mHeadInfoListView.setVisibility(View.GONE);
                }
            }

            //红包记录
            if (tData instanceof EveryDayHbRet) {

                //玩游戏的初始化数据
                if (((EveryDayHbRet) tData).getData().getGameInfo() != null) {
                    playGameInfo = ((EveryDayHbRet) tData).getData().getGameInfo();
                }

                //游戏有关看视频得金币的任务信息
                if (((EveryDayHbRet) tData).getData().getSeeVideoInfo() != null) {
                    gameSeeVideoInfo = ((EveryDayHbRet) tData).getData().getSeeVideoInfo();
                }

                if (((EveryDayHbRet) tData).getData().getIsFinish() == 0) {
                    clickAnyWhere = ((EveryDayHbRet) tData).getData().getHbvideo() == 1 ? true : false;

                    String smallKey = StringUtils.isEmpty(mUserInfo.getId()) ? Constants.SMALL_HONG_BAO_IS_CLOSE : Constants.SMALL_HONG_BAO + mUserInfo.getId();
                    String lastCloseSmallDate = SPUtils.getInstance().getString(smallKey, "");
                    if (((EveryDayHbRet) tData).getData().getIsFirst() == 0) {

                        String bigKey = StringUtils.isEmpty(mUserInfo.getId()) ? Constants.BIG_HONG_BAO_IS_CLOSE : Constants.BIG_HONG_BAO + mUserInfo.getId();
                        String lastCloseBigDate = SPUtils.getInstance().getString(bigKey, "");
                        if (!lastCloseBigDate.equals(MyTimeUtil.getYearAndDay())) {
                            if (everyDayHongBaoDialog != null && !everyDayHongBaoDialog.isShowing()) {
                                mFloatHbLayout.setVisibility(View.GONE);
                                everyDayHongBaoDialog.show();
                                everyDayHongBaoDialog.setClickAnyWhere(clickAnyWhere);
                            }
                        } else {
                            if (!lastCloseSmallDate.equals(MyTimeUtil.getYearAndDay())) {
                                mFloatHbLayout.setVisibility(View.VISIBLE);
                            } else {
                                mFloatHbLayout.setVisibility(View.GONE);
                            }
                        }
                    } else {

                        if (!lastCloseSmallDate.equals(MyTimeUtil.getYearAndDay())) {
                            mFloatHbLayout.setVisibility(View.VISIBLE);
                        } else {
                            mFloatHbLayout.setVisibility(View.GONE);
                        }
                    }
                } else {
                    mFloatHbLayout.setVisibility(View.GONE);
                    SPUtils.getInstance().put(StringUtils.isEmpty(mUserInfo.getId()) ? Constants.SMALL_HONG_BAO_IS_CLOSE : Constants.SMALL_HONG_BAO + mUserInfo.getId(), MyTimeUtil.getYearAndDay());
                    SPUtils.getInstance().put(StringUtils.isEmpty(mUserInfo.getId()) ? Constants.BIG_HONG_BAO_IS_CLOSE : Constants.BIG_HONG_BAO + mUserInfo.getId(), MyTimeUtil.getYearAndDay());
                }
            }

            if (tData instanceof TaskRecordInfoRet) {
                if (StringUtils.isEmpty(recordId)) {
                    if (((TaskRecordInfoRet) tData).getData() != null) {
                        recordId = ((TaskRecordInfoRet) tData).getData().getInfoid();
                    }
                    Logger.i("recordId--->" + recordId);
                }
            }
        } else {
            if (isFirstLoad) {
                mNoDataLayout.setVisibility(View.VISIBLE);
                mHeadInfoListView.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_search:
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_refresh2:
                refresh();
                break;
            default:
                break;
        }
    }

    public void addRecord(String aid) {
        recordInfoPresenterImp.adClickInfo(mUserInfo.getId(), aid);
    }

    @Override
    public void loadDataError(Throwable throwable) {
        avi.hide();
        mRefreshLayout.setRefreshing(false);
        if (isFirstLoad) {
            mNoDataLayout.setVisibility(View.VISIBLE);
            mHeadInfoListView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRefresh() {
        refresh();
    }

    @Override
    public void openConfig() {

        String appId = "wxd1112ca9a216aeda"; // 填应用AppId
        IWXAPI api = WXAPIFactory.createWXAPI(getActivity(), appId);

        if (clickType == 1) {
            switch (bannerInfo.getType()) {
                case 1:
                    //网页
                    break;
                case 2:

                    break;
                case 3:
                    WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
                    req.userName = bannerInfo.getOriginId(); // 填小程序原始id
                    req.path = bannerInfo.getJumpPath(); //拉起小程序页面的可带参路径，不填默认拉起小程序首页
                    req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE;// 可选打开 开发版，体验版和正式版
                    api.sendReq(req);
                    break;
                default:
                    break;
            }
        }

        if (clickType == 2) {
            addRecord(adInfo.getId());

            switch (adInfo.getType()) {
                case 1:
                    break;
                case 2:
                    downAppFile("http://zs.qqtn.com/zbsq/Apk/tnzbsq_LIURENJUN1.apk");
                    break;
                case 3:
                    WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
                    req.userName = adInfo.getOriginId(); // 填小程序原始id
                    req.path = adInfo.getJumpPath(); //拉起小程序页面的可带参路径，不填默认拉起小程序首页
                    req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE;// 可选打开 开发版，体验版和正式版
                    api.sendReq(req);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void openCancel() {
        if (openDialog != null && openDialog.isShowing()) {
            openDialog.dismiss();
        }
    }

    public void downAppFile(String downUrl) {
        final String filePath = PathUtils.getExternalAppFilesPath() + "/temp_app.apk";
        Logger.i("down app path --->" + filePath);

        task = FileDownloader.getImpl().create(downUrl)
                .setPath(filePath)
                .setListener(new FileDownloadListener() {
                    @Override
                    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        Toasty.normal(getActivity(), "正在下载打开请稍后...").show();
                    }

                    @Override
                    protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
                    }

                    @Override
                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                    }

                    @Override
                    protected void blockComplete(BaseDownloadTask task) {
                    }

                    @Override
                    protected void retry(final BaseDownloadTask task, final Throwable ex, final int retryingTimes, final int soFarBytes) {
                    }

                    @Override
                    protected void completed(BaseDownloadTask task) {
                        ToastUtils.showLong("下载完成");
                        //install(filePath);
                        AppUtils.installApp(filePath);
                    }

                    @Override
                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                    }

                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {
                    }

                    @Override
                    protected void warn(BaseDownloadTask task) {
                    }
                });

        task.start();
    }

    private boolean mHasShowDownloadActive = false;

    private void loadAd(String codeId, int orientation, int goldNum) {
        //step4:创建广告请求参数AdSlot,具体参数含义参考文档
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(codeId)
                .setSupportDeepLink(true)
                .setImageAcceptedSize(1080, 1920)
                .setRewardName("金币") //奖励的名称
                .setRewardAmount(goldNum)  //奖励的数量
                .setUserID(App.getApp().mUserInfo != null ? App.getApp().mUserInfo.getId() : "10000" + RandomUtils.nextInt())//用户id,必传参数
                .setMediaExtra("media_extra") //附加参数，可选
                .setOrientation(orientation) //必填参数，期望视频的播放方向：TTAdConstant.HORIZONTAL 或 TTAdConstant.VERTICAL
                .build();

        //step5:请求广告
        mTTAdNative.loadRewardVideoAd(adSlot, new TTAdNative.RewardVideoAdListener() {
            @Override
            public void onError(int code, String message) {
                Logger.i("code--->" + code + "---" + message);
            }

            //视频广告加载后，视频资源缓存到本地的回调，在此回调后，播放本地视频，流畅不阻塞。
            @Override
            public void onRewardVideoCached() {
                Logger.i("rewardVideoAd video cached");
            }

            //视频广告的素材加载完毕，比如视频url等，在此回调后，可以播放在线视频，网络不好可能出现加载缓冲，影响体验。
            @Override
            public void onRewardVideoAdLoad(TTRewardVideoAd ad) {
                Logger.i("rewardVideoAd loaded");

                mttRewardVideoAd = ad;
                mttRewardVideoAd.setRewardAdInteractionListener(new TTRewardVideoAd.RewardAdInteractionListener() {

                    @Override
                    public void onAdShow() {
                        Logger.i("rewardVideoAd show");
                    }

                    @Override
                    public void onAdVideoBarClick() {
                        Logger.i("rewardVideoAd bar click");
                    }

                    @Override
                    public void onAdClose() {
                        Logger.i("rewardVideoAd close");
                        Intent intent = new Intent(getActivity(), EveryDayHongBaoActivity.class);
                        intent.putExtra("record_id", recordId);
                        intent.putExtra("play_game_info", playGameInfo);
                        intent.putExtra("game_see_video", gameSeeVideoInfo);
                        startActivity(intent);
                    }

                    //视频播放完成回调
                    @Override
                    public void onVideoComplete() {
                        Logger.i("rewardVideoAd complete");
                    }

                    @Override
                    public void onVideoError() {
                        Logger.i("rewardVideoAd error");
                    }

                    //视频播放完成后，奖励验证回调，rewardVerify：是否有效，rewardAmount：奖励梳理，rewardName：奖励名称
                    @Override
                    public void onRewardVerify(boolean rewardVerify, int rewardAmount, String rewardName) {
                        if (rewardVerify) {

                        }
                    }

                    @Override
                    public void onSkippedVideo() {
                        Logger.i("rewardVideoAd has onSkippedVideo");
                    }
                });
                mttRewardVideoAd.setDownloadListener(new TTAppDownloadListener() {
                    @Override
                    public void onIdle() {
                        mHasShowDownloadActive = false;
                    }

                    @Override
                    public void onDownloadActive(long totalBytes, long currBytes, String fileName, String appName) {
                        if (!mHasShowDownloadActive) {
                            mHasShowDownloadActive = true;
                            //ToastUtils.showLong("下载中，点击下载区域暂停", Toast.LENGTH_LONG);
                        }
                    }

                    @Override
                    public void onDownloadPaused(long totalBytes, long currBytes, String fileName, String appName) {
                        //ToastUtils.showLong("下载暂停，点击下载区域继续", Toast.LENGTH_LONG);
                    }

                    @Override
                    public void onDownloadFailed(long totalBytes, long currBytes, String fileName, String appName) {
                        //ToastUtils.showLong("下载失败，点击下载区域重新下载", Toast.LENGTH_LONG);
                    }

                    @Override
                    public void onDownloadFinished(long totalBytes, String fileName, String appName) {
                        //ToastUtils.showLong("下载完成，点击下载区域重新下载", Toast.LENGTH_LONG);
                    }

                    @Override
                    public void onInstalled(String fileName, String appName) {
                        //ToastUtils.showLong("安装完成，点击下载区域打开", Toast.LENGTH_LONG);
                    }
                });
            }
        });
    }

    @Override
    public void openEveryDayHongBao() {
        if(mttRewardVideoAd != null){
            //step6:在获取到广告后展示
            mttRewardVideoAd.showRewardVideoAd(getActivity());
            mttRewardVideoAd = null;
            MobclickAgent.onEvent(getActivity(), "open_hongbao", AppUtils.getAppVersionName());
            try {
                recordId = "";
                taskRecordInfoPresenterImp.addHomeTaskRecord(StringUtils.isEmpty(mUserInfo.getId()) ? "0" : mUserInfo.getId(), StringUtils.isEmpty(mUserInfo.getOpenid()) ? "0" : mUserInfo.getOpenid(), PhoneUtils.getIMEI(), 0, 0, "0");
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void closeEveryDayHongBao() {
        MobclickAgent.onEvent(getActivity(), "close_hongbao", AppUtils.getAppVersionName());
        mFloatHbLayout.setVisibility(View.VISIBLE);
        SPUtils.getInstance().put(StringUtils.isEmpty(mUserInfo.getId()) ? Constants.BIG_HONG_BAO_IS_CLOSE : Constants.BIG_HONG_BAO + mUserInfo.getId(), MyTimeUtil.getYearAndDay());
    }

    @Override
    public void adClick() {
        Logger.i("open ad click--->");
        if (openAdInfo != null && !StringUtils.isEmpty(openAdInfo.getId())) {
            addRecord(openAdInfo.getId());
        }
    }

    @OnClick(R.id.layout_float_hb)
    void floatHb() {
        //点击悬浮图时，打开大的领取红包图
        if (everyDayHongBaoDialog != null && !everyDayHongBaoDialog.isShowing()) {
            loadAd("920819401", TTAdConstant.VERTICAL, 100);
            mFloatHbLayout.setVisibility(View.GONE);
            everyDayHongBaoDialog.show();
            everyDayHongBaoDialog.setClickAnyWhere(clickAnyWhere);
        }
    }

    @OnClick(R.id.iv_close_float_hb)
    void closeFloatHb() {
        mFloatHbLayout.setVisibility(View.GONE);
        SPUtils.getInstance().put(StringUtils.isEmpty(mUserInfo.getId()) ? Constants.SMALL_HONG_BAO_IS_CLOSE : Constants.SMALL_HONG_BAO + mUserInfo.getId(), MyTimeUtil.getYearAndDay());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
