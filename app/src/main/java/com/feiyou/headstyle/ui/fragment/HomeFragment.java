package com.feiyou.headstyle.ui.fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.PathUtils;
import com.blankj.utilcode.util.PhoneUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdManager;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.bytedance.sdk.openadsdk.TTRewardVideoAd;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feiyou.headstyle.App;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.base.IBaseView;
import com.feiyou.headstyle.bean.AdInfo;
import com.feiyou.headstyle.bean.BannerInfo;
import com.feiyou.headstyle.bean.EveryDayHbRet;
import com.feiyou.headstyle.bean.HeadInfo;
import com.feiyou.headstyle.bean.HeadInfoRet;
import com.feiyou.headstyle.bean.HomeDataRet;
import com.feiyou.headstyle.bean.HomeDataWrapper;
import com.feiyou.headstyle.bean.HongBaoInfoRet;
import com.feiyou.headstyle.bean.MessageEvent;
import com.feiyou.headstyle.bean.PlayGameInfo;
import com.feiyou.headstyle.bean.ReceiveUserInfo;
import com.feiyou.headstyle.bean.SeeVideoInfo;
import com.feiyou.headstyle.bean.TaskRecordInfoRet;
import com.feiyou.headstyle.bean.UserInfo;
import com.feiyou.headstyle.bean.VersionInfo;
import com.feiyou.headstyle.bean.VersionInfoRet;
import com.feiyou.headstyle.common.Constants;
import com.feiyou.headstyle.common.GlideImageLoader;
import com.feiyou.headstyle.presenter.EveryDayHongBaoPresenterImp;
import com.feiyou.headstyle.presenter.HomeDataPresenterImp;
import com.feiyou.headstyle.presenter.HongBaoInfoPresenterImp;
import com.feiyou.headstyle.presenter.RecordInfoPresenterImp;
import com.feiyou.headstyle.presenter.TaskRecordInfoPresenterImp;
import com.feiyou.headstyle.presenter.VersionPresenterImp;
import com.feiyou.headstyle.ui.activity.AdActivity;
import com.feiyou.headstyle.ui.activity.Collection2Activity;
import com.feiyou.headstyle.ui.activity.CommunityArticleActivity;
import com.feiyou.headstyle.ui.activity.EveryDayHongBaoActivity;
import com.feiyou.headstyle.ui.activity.HeadListActivity;
import com.feiyou.headstyle.ui.activity.HeadShowActivity;
import com.feiyou.headstyle.ui.activity.MoreTypeActivity;
import com.feiyou.headstyle.ui.activity.SearchActivity;
import com.feiyou.headstyle.ui.adapter.HeadInfoAdapter;
import com.feiyou.headstyle.ui.adapter.HeadMultipleAdapter;
import com.feiyou.headstyle.ui.adapter.HeadTypeAdapter;
import com.feiyou.headstyle.ui.base.BaseFragment;
import com.feiyou.headstyle.ui.custom.EveryDayHongBaoDialog;
import com.feiyou.headstyle.ui.custom.HongBaoDialog;
import com.feiyou.headstyle.ui.custom.OpenDialog;
import com.feiyou.headstyle.ui.custom.PrivacyDialog;
import com.feiyou.headstyle.ui.custom.RoundedCornersTransformation;
import com.feiyou.headstyle.ui.custom.VersionUpdateDialog;
import com.feiyou.headstyle.ui.custom.WarmDialog;
import com.feiyou.headstyle.utils.AppContextUtil;
import com.feiyou.headstyle.utils.MyTimeUtil;
import com.feiyou.headstyle.utils.RandomUtils;
import com.feiyou.headstyle.utils.TTAdManagerHolder;
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

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by myflying on 2019/1/3.
 */

@RuntimePermissions
public class HomeFragment extends BaseFragment implements IBaseView, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, OpenDialog.ConfigListener, CardWindowFragment.AdDismissListener, EveryDayHongBaoDialog.EveryDayHongBaoListener, VersionUpdateDialog.UpdateListener, HongBaoDialog.HongBaoListener, PrivacyDialog.PrivacyListener, WarmDialog.WarmListener {

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

    //HeadInfoAdapter headInfoAdapter;

    private int searchLayoutTop;

    private HomeDataPresenterImp homeDataPresenterImp;

    private RecordInfoPresenterImp recordInfoPresenterImp;

    private int randomPage = -1;

    private int currentPage = 1;

    private int pageSize = 21;

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

    private boolean isAlertHongBao;

    VersionPresenterImp versionPresenterImp;

    private ProgressDialog progressDialog = null;

    private VersionInfo versionInfo;

    VersionUpdateDialog updateDialog;

    private boolean isUpdateVersion;

    //新人红包/登录红包
    HongBaoDialog hongBaoDialog;

    HongBaoInfoPresenterImp hongBaoInfoPresenterImp;

    private String todayDate;

    private PrivacyDialog privacyDialog;

    private WarmDialog warmDialog;

    private String[] seeVideoMoneys;

    private double seeVideoMoney;//看视频可得到的收益

    private int showHBType;

    private String newPersonId;

    HeadMultipleAdapter headMultipleAdapter;

    private int loadPage = 1;

    private int firstIndex = 9;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    if (hongBaoDialog != null && hongBaoDialog.isShowing()) {
                        hongBaoDialog.updateHBState(1, seeVideoMoney);
                    } else {
                        //请求版本更新
                        versionPresenterImp.getVersionInfo(com.feiyou.headstyle.utils.AppUtils.getMetaDataValue(getActivity(), "UMENG_CHANNEL"));
                    }
                    break;
                case 1:
                    int progress = (Integer) msg.obj;
                    updateDialog.setProgress(progress);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        HomeFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @NeedsPermission(Manifest.permission.READ_PHONE_STATE)
    public void showReadPhone() {
        App.imei = PhoneUtils.getIMEI();
        App.androidId = DeviceUtils.getAndroidID();
        Logger.i("imei --->" + App.imei + "---androidId--->" + App.androidId);
        HomeFragmentPermissionsDispatcher.showReadStorageWithPermissionCheck(this);
    }

    @OnPermissionDenied(Manifest.permission.READ_PHONE_STATE)
    public void onReadPhoneDenied() {
        App.androidId = DeviceUtils.getAndroidID();
        HomeFragmentPermissionsDispatcher.showReadStorageWithPermissionCheck(this);
    }

    @OnShowRationale(Manifest.permission.READ_PHONE_STATE)
    public void showRationaleForReadPhone(PermissionRequest request) {
        request.proceed();
    }

    @OnNeverAskAgain(Manifest.permission.READ_PHONE_STATE)
    public void onReadPhoneNeverAskAgain() {

    }

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void showReadStorage() {
        HomeFragmentPermissionsDispatcher.showReadLocationWithPermissionCheck(this);
    }

    @OnPermissionDenied(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void onReadStorageDenied() {
        HomeFragmentPermissionsDispatcher.showReadLocationWithPermissionCheck(this);
    }

    @OnShowRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void showRationaleForReadStorage(PermissionRequest request) {
        request.proceed();
    }

    @OnNeverAskAgain(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void onReadStorageNeverAskAgain() {
    }

    //读取地理位置权限
    @NeedsPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
    void showReadLocation() {
        //EventBus.getDefault().post(new MessageEvent("permission_use"));
        initHBInfo();
    }

    @OnPermissionDenied(Manifest.permission.ACCESS_COARSE_LOCATION)
    void onReadLocationDenied() {
        //EventBus.getDefault().post(new MessageEvent("permission_use"));
        initHBInfo();
    }

    @OnShowRationale(Manifest.permission.ACCESS_COARSE_LOCATION)
    void showRationaleForReadLocation(PermissionRequest request) {
        request.proceed();
    }

    @OnNeverAskAgain(Manifest.permission.ACCESS_COARSE_LOCATION)
    void onReadLocationNeverAskAgain() {
        //EventBus.getDefault().post(new MessageEvent("permission_use"));
        initHBInfo();
    }

    @Override
    protected View onCreateView() {
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_home, null);
        ButterKnife.bind(this, root);
        EventBus.getDefault().register(this);
        FileDownloader.setup(getActivity());
        initViews();
        initBanner();
        initData();
        return root;
    }

    public void initViews() {
        privacyDialog = new PrivacyDialog(getActivity(), R.style.login_dialog);
        privacyDialog.setPrivacyListener(this);

        warmDialog = new WarmDialog(getActivity(), R.style.login_dialog);
        warmDialog.setWarmListener(this);

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
        hongBaoInfoPresenterImp = new HongBaoInfoPresenterImp(this, getActivity());
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

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("正在检测新版本");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (versionInfo != null && versionInfo.getVersionIsChange() == 1) {
                        return true;//不执行父类点击事件
                    }
                    return false;
                }
                return false;
            }
        });

        updateDialog = new VersionUpdateDialog(getActivity(), R.style.login_dialog);
        updateDialog.setUpdateListener(this);
        updateDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (versionInfo != null && versionInfo.getVersionIsChange() == 1) {
                        return true;//不执行父类点击事件
                    }
                    return false;
                }
                return false;
            }
        });

        //加载更多动画
        mLoadingView = footView.findViewById(R.id.iv_loading);
        Glide.with(getActivity()).load(R.drawable.list_loading).into(mLoadingView);
    }

    public void initHBInfo() {
        try {
            //Logger.i("init hb info--->" + mUserInfo != null ? mUserInfo.getId() : "no user");
            if (mUserInfo == null) {
                Logger.i("init hb info---> no user");
            } else {
                //Logger.i("init hb info--->" + mUserInfo.getId());
                Log.i("init hb info", "init hb info id--->" + mUserInfo.getId());
            }
            hongBaoInfoPresenterImp.getHBInfo(mUserInfo != null ? mUserInfo.getId() : "", mUserInfo != null ? mUserInfo.getOpenid() : "", App.imei);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    public void initData() {
        Logger.i("home fragment init data--->");
        mUserInfo = App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo() : new UserInfo();

        todayDate = TimeUtils.getNowString(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()));

        TTAdManager ttAdManager = TTAdManagerHolder.get();
        //step3:创建TTAdNative对象,用于调用广告请求接口
        mTTAdNative = ttAdManager.createAdNative(getActivity());
        //初始化加载红包信息
        loadAd("920819401", TTAdConstant.VERTICAL, 100);

        everyDayHongBaoDialog = new EveryDayHongBaoDialog(getActivity(), R.style.login_dialog);
        everyDayHongBaoDialog.setEveryDayHongBaoListener(this);

        //新人红包
        hongBaoDialog = new HongBaoDialog(getActivity(), R.style.login_dialog);
        hongBaoDialog.setHongBaoListener(this);

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

        gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        /*gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == 0 || position == 10 || position == 43) {
                    Logger.i("ad pos--->" + position);
                    return 3;
                }
                return 1;
            }
        });*/


        headMultipleAdapter = new HeadMultipleAdapter(null);
        mHeadInfoListView.setLayoutManager(gridLayoutManager);
        headMultipleAdapter.addHeaderView(topView);
        mHeadInfoListView.setAdapter(headMultipleAdapter);

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

        headMultipleAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Logger.i("current pos--->" + position);

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

        headMultipleAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                isFirstLoad = false;
                isChange = "";
                currentPage++;
                loadPage++;
                homeDataPresenterImp.getData(mUserInfo != null ? mUserInfo.getId() : "", currentPage + "", pageSize + "", "", 0);
            }
        }, mHeadInfoListView);

        homeDataPresenterImp.getData(mUserInfo != null ? mUserInfo.getId() : "", "", pageSize + "", "", 0);

        if (cardWindowFragment == null) {
            cardWindowFragment = new CardWindowFragment();
        }
        cardWindowFragment.setAdDismissListener(this);

        versionPresenterImp = new VersionPresenterImp(this, getActivity());

        if (!SPUtils.getInstance().getBoolean(Constants.SHOW_PRIVARY, false)) {
            privacyDialog.show();
            privacyDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent keyEvent) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        return true;//不执行父类点击事件
                    }
                    return false;
                }
            });
        } else {
            HomeFragmentPermissionsDispatcher.showReadPhoneWithPermissionCheck(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(MessageEvent messageEvent) {
        if (messageEvent.getMessage().equals("permission_use")) {
            try {
                hongBaoInfoPresenterImp.getHBInfo(StringUtils.isEmpty(mUserInfo.getId()) ? "" : mUserInfo.getId(), StringUtils.isEmpty(mUserInfo.getOpenid()) ? "" : mUserInfo.getOpenid(), App.imei);
                //TODO
                //everyDayHongBaoPresenterImp.everyDayHongBaoInfo(StringUtils.isEmpty(mUserInfo.getId()) ? "0" : mUserInfo.getId(), StringUtils.isEmpty(mUserInfo.getOpenid()) ? "0" : mUserInfo.getOpenid(), PhoneUtils.getIMEI());
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }

        if (messageEvent.getMessage().equals("login_success")) {
            Logger.i("home login success--->");
            isAlertHongBao = true;
            if (App.getApp().getmUserInfo() != null) {
                Logger.i(JSON.toJSONString(App.getApp().getmUserInfo()));
            }
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (AppContextUtil.isValidContext(getActivity()) && App.getApp().getmUserInfo() != null && isAlertHongBao) {
                //今日是否关闭过红包
                String lastDate = SPUtils.getInstance().getString(Constants.CLOSE_HB_DATA, "");
                boolean todayIsCloseHB = false;
                if (lastDate.equals(todayDate)) {
                    todayIsCloseHB = true;
                }

                if (!todayIsCloseHB) {
                    if (hongBaoDialog != null && !hongBaoDialog.isShowing()) {
                        hongBaoDialog.show();
                        hongBaoDialog.setHBConfigInfo(2, clickAnyWhere);
                    }
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Logger.i("home fragment onResume--->" + isFirstLoad);
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
        loadPage = 1;
        homeDataPresenterImp.getData(mUserInfo != null ? mUserInfo.getId() : "", "", "", isChange, 0);
        gridLayoutManager.scrollToPosition(0);
    }

    @OnClick(R.id.tv_reload)
    void reLoad() {
        mRefreshLayout.setRefreshing(true);
        isFirstLoad = true;
        isChange = "1";
        homeDataPresenterImp.getData(mUserInfo != null ? mUserInfo.getId() : "", "", "", isChange, 0);
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

        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    public void randomMoney() {
        Logger.i("videoMoneys--->" + JSON.toJSONString(seeVideoMoneys));
        double temp = RandomUtils.nextDouble(Double.parseDouble(seeVideoMoneys[0]), Double.parseDouble(seeVideoMoneys[1]));
        BigDecimal tempBd = new BigDecimal(temp);
        seeVideoMoney = tempBd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        Logger.i("see video money value --->" + seeVideoMoney);
    }

    @Override
    public void loadDataSuccess(Object tData) {
        Logger.i("home data--->" + JSON.toJSONString(tData));
        avi.hide();
        mRefreshLayout.setRefreshing(false);
        if (tData != null) {
            mNoDataLayout.setVisibility(View.GONE);
            mHeadInfoListView.setVisibility(View.VISIBLE);

            HomeDataWrapper homeDataRet = null;
            if (tData instanceof HomeDataRet && ((HomeDataRet) tData).getCode() == Constants.SUCCESS) {
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
                    Logger.i("home head pagesize--->" + homeDataRet.getImagesList().size());
                    if (homeDataRet.getImagesList() != null && homeDataRet.getImagesList().size() > 0) {
                        if (isFirstLoad || isChange.equals("1")) {
                            isFirstLoad = false;
                            //刷新或者加载完数据后，设置ischange =""
                            isChange = "";

                            List<HeadInfo> tempList = homeDataRet.getImagesList();
                            for (HeadInfo headInfo : tempList) {
                                headInfo.setItemType(HeadInfo.HEAD_IMG);
                            }
                            //在集合后添加广告位的对象
                            HeadInfo tempHeadInfo = new HeadInfo(HeadInfo.HEAD_AD);
                            tempList.add(firstIndex, tempHeadInfo);
                            headMultipleAdapter.setNewData(tempList);
                        } else {

                            List<HeadInfo> tempList = homeDataRet.getImagesList();
                            for (HeadInfo headInfo : tempList) {
                                headInfo.setItemType(HeadInfo.HEAD_IMG);
                            }
                            headMultipleAdapter.addData((tempList));

                            HeadInfo tempHeadInfo = new HeadInfo(HeadInfo.HEAD_AD);
                            int addIndex = firstIndex + (loadPage - 1) * pageSize + loadPage - 1;
                            headMultipleAdapter.getData().add(addIndex, tempHeadInfo);
                            Logger.i("current index1111--->" + addIndex + "---load page--->" + loadPage);
                            headMultipleAdapter.notifyItemChanged(addIndex);
                        }

                        if (loadPage == 1) {
                            if (homeDataRet.getImagesList().size() == pageSize + 1) {
                                headMultipleAdapter.loadMoreComplete();
                            } else {
                                headMultipleAdapter.loadMoreEnd(true);
                            }
                        } else {
                            if (homeDataRet.getImagesList().size() == pageSize) {
                                headMultipleAdapter.loadMoreComplete();
                            } else {
                                headMultipleAdapter.loadMoreEnd(true);
                            }
                        }

                        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                            @Override
                            public int getSpanSize(int position) {
                                if (position == 0 || position == 10 || position == 22) {
                                    return 3;
                                }
                                return 1;
                            }
                        });

                        loadListAd();
                    }
                } else {
                    mNoDataLayout.setVisibility(View.VISIBLE);
                    mHeadInfoListView.setVisibility(View.GONE);
                }
            }

            //红包记录
            if (tData instanceof EveryDayHbRet && ((EveryDayHbRet) tData).getCode() == Constants.SUCCESS) {

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
                                if (!SPUtils.getInstance().getBoolean(Constants.SHOW_PRIVARY, false)) {
                                    isAlertHongBao = true;
                                } else {
                                    isAlertHongBao = false;
                                    everyDayHongBaoDialog.show();
                                    everyDayHongBaoDialog.setClickAnyWhere(clickAnyWhere);
                                }
                            }
                        }
                    }
                }

                //在不弹出红包的情况下检测版本更新
                if (!isAlertHongBao && SPUtils.getInstance().getBoolean(Constants.SHOW_PRIVARY, false)) {
                    //请求版本更新
                    versionPresenterImp.getVersionInfo(com.feiyou.headstyle.utils.AppUtils.getMetaDataValue(getActivity(), "UMENG_CHANNEL"));
                }
            }

            if (tData instanceof HongBaoInfoRet) {
                Logger.i("hongbao data--->" + JSON.toJSONString(tData));

                seeVideoMoneys = ((HongBaoInfoRet) tData).getData().getCashindex().split("/");
                //根据返回的金额范围，随机产生获得奖励值
                randomMoney();

                //今日是否关闭过红包
                String lastDate = SPUtils.getInstance().getString(Constants.CLOSE_HB_DATA, "");
                boolean todayIsCloseHB = false;
                if (lastDate.equals(todayDate)) {
                    todayIsCloseHB = true;
                }

                if (((HongBaoInfoRet) tData).getCode() == Constants.SUCCESS) {
                    newPersonId = ((HongBaoInfoRet) tData).getData().getUserId();
                    showHBType = ((HongBaoInfoRet) tData).getData().getType();
                    clickAnyWhere = ((HongBaoInfoRet) tData).getData().getHbvideo() == 1 ? true : false;
                    if (showHBType > 0 && !todayIsCloseHB) {
                        if (hongBaoDialog != null && !hongBaoDialog.isShowing()) {
                            hongBaoDialog.show();
                            hongBaoDialog.setHBConfigInfo(showHBType, clickAnyWhere);
                        }
                    }
                }
            }

            if (tData instanceof TaskRecordInfoRet) {
                if (((TaskRecordInfoRet) tData).getCode() == Constants.SUCCESS) {

                }
            }

            //版本更新
            if (tData instanceof VersionInfoRet && ((VersionInfoRet) tData).getCode() == Constants.SUCCESS) {
                versionInfo = ((VersionInfoRet) tData).getData();
                if (versionInfo != null) {
                    SPUtils.getInstance().put("start_type", versionInfo.getStartType());
                }
                if (versionInfo.getVersionCode() > AppUtils.getAppVersionCode()) {
                    if (updateDialog != null && !updateDialog.isShowing()) {
                        isUpdateVersion = true;
                        updateDialog.setVersionCode(versionInfo.getVersionName());
                        updateDialog.setVersionContent(versionInfo.getVersionDesc());
                        updateDialog.setIsForceUpdate(versionInfo.getVersionIsChange());
                        updateDialog.show();
                    }
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
        recordInfoPresenterImp.adClickInfo(mUserInfo != null ? mUserInfo.getId() : "", aid);
    }

    @Override
    public void loadDataError(Throwable throwable) {
        avi.hide();
        mRefreshLayout.setRefreshing(false);
        if (isFirstLoad) {
            mNoDataLayout.setVisibility(View.VISIBLE);
            mHeadInfoListView.setVisibility(View.GONE);
        }
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void update() {
        if (versionInfo != null && !StringUtils.isEmpty(versionInfo.getVersionUrl())) {
            downAppFile(versionInfo.getVersionUrl());
        }
    }

    @Override
    public void updateCancel() {

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
                        if (!isUpdateVersion) {
                            Toasty.normal(getActivity(), "正在下载打开请稍后...").show();
                        }
                    }

                    @Override
                    protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
                    }

                    @Override
                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        if (isUpdateVersion) {
                            int progress = (int) ((soFarBytes * 1.0 / totalBytes) * 100);
                            Logger.i("progress--->" + soFarBytes + "---" + totalBytes + "---" + progress);

                            Message message = new Message();
                            message.what = 1;
                            message.obj = progress;
                            mHandler.sendMessage(message);
                        }
                    }

                    @Override
                    protected void blockComplete(BaseDownloadTask task) {
                    }

                    @Override
                    protected void retry(final BaseDownloadTask task, final Throwable ex, final int retryingTimes, final int soFarBytes) {
                    }

                    @Override
                    protected void completed(BaseDownloadTask task) {
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
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

                        Message message = new Message();
                        message.what = 0;
                        mHandler.sendMessage(message);
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

                        String userId = mUserInfo != null ? mUserInfo.getId() : "";
                        if (showHBType == 1) {
                            userId = newPersonId;
                        }
                        taskRecordInfoPresenterImp.addHomeTaskRecord(userId, mUserInfo != null ? mUserInfo.getOpenid() : "", App.imei, seeVideoMoney, showHBType);
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
        if (mttRewardVideoAd != null) {
            //step6:在获取到广告后展示
            mttRewardVideoAd.showRewardVideoAd(getActivity());
            mttRewardVideoAd = null;
            MobclickAgent.onEvent(getActivity(), "open_hongbao", AppUtils.getAppVersionName());
            try {
                recordId = "";
                //taskRecordInfoPresenterImp.addHomeTaskRecord(StringUtils.isEmpty(mUserInfo.getId()) ? "0" : mUserInfo.getId(), StringUtils.isEmpty(mUserInfo.getOpenid()) ? "0" : mUserInfo.getOpenid(), PhoneUtils.getIMEI(), 0, 0, "0");
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void closeEveryDayHongBao() {
        MobclickAgent.onEvent(getActivity(), "close_hongbao", AppUtils.getAppVersionName());
        SPUtils.getInstance().put(StringUtils.isEmpty(mUserInfo.getId()) ? Constants.BIG_HONG_BAO_IS_CLOSE : Constants.BIG_HONG_BAO + mUserInfo.getId(), MyTimeUtil.getYearAndDay());

        //在不弹出红包的情况下检测版本更新
        if (!isAlertHongBao) {
            //请求版本更新
            versionPresenterImp.getVersionInfo(com.feiyou.headstyle.utils.AppUtils.getMetaDataValue(getActivity(), "UMENG_CHANNEL"));
        }
    }

    @Override
    public void adClick() {
        Logger.i("open ad click--->");
        if (openAdInfo != null && !StringUtils.isEmpty(openAdInfo.getId())) {
            addRecord(openAdInfo.getId());
        }
    }

    @Override
    public void openHB() {
        //ToastUtils.showLong("点击了开启红包");

        if (mttRewardVideoAd != null) {
            //step6:在获取到广告后展示
            mttRewardVideoAd.showRewardVideoAd(getActivity());
            mttRewardVideoAd = null;
            MobclickAgent.onEvent(getActivity(), "open_hongbao", AppUtils.getAppVersionName());
        }
    }

    @Override
    public void directClose() {
        //关闭红包，存储当天的日期
        SPUtils.getInstance().put(Constants.CLOSE_HB_DATA, todayDate);
        if (hongBaoDialog != null && hongBaoDialog.isShowing()) {
            hongBaoDialog.dismiss();
        }
    }


    @Override
    public void agree() {
        SPUtils.getInstance().put(Constants.SHOW_PRIVARY, true);
        HomeFragmentPermissionsDispatcher.showReadPhoneWithPermissionCheck(this);
    }

    @Override
    public void notAgree() {
        if (warmDialog != null && !warmDialog.isShowing()) {
            warmDialog.show();
        }
    }

    @Override
    public void warnAgree() {
        SPUtils.getInstance().put(Constants.SHOW_PRIVARY, true);
        HomeFragmentPermissionsDispatcher.showReadPhoneWithPermissionCheck(this);
    }

    @Override
    public void warnNotAgree() {
        SPUtils.getInstance().put(Constants.SHOW_PRIVARY, false);
        getActivity().finish();
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
                .setCodeId("945142340")
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
            int tempIndex = loadPage == 1 ? firstIndex : firstIndex + loadPage * 10 + loadPage;

            Logger.i("current index2222--->" + tempIndex + "---load page --->" + loadPage);

            if (tempIndex >= headMultipleAdapter.getData().size()) {
                return;
            }

            HeadInfo tempHeadInfo = headMultipleAdapter.getData().get(tempIndex);
            tempHeadInfo.setTtNativeExpressAd(adTmp);
            headMultipleAdapter.getData().set(tempIndex, tempHeadInfo);

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
                    headMultipleAdapter.notifyItemChanged(tempIndex);
                }
            });
            adTmp.render();
        }

        Logger.i("total size--->" + headMultipleAdapter.getData().size());
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
