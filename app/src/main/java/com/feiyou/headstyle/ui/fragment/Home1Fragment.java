package com.feiyou.headstyle.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
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

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feiyou.headstyle.App;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.BannerInfo;
import com.feiyou.headstyle.bean.HomeDataRet;
import com.feiyou.headstyle.bean.HomeDataWrapper;
import com.feiyou.headstyle.bean.ResultInfo;
import com.feiyou.headstyle.common.Constants;
import com.feiyou.headstyle.common.GlideImageLoader;
import com.feiyou.headstyle.presenter.HomeDataPresenterImp;
import com.feiyou.headstyle.ui.activity.Collection2Activity;
import com.feiyou.headstyle.ui.activity.HeadListActivity;
import com.feiyou.headstyle.ui.activity.HeadShowActivity;
import com.feiyou.headstyle.ui.activity.MoreTypeActivity;
import com.feiyou.headstyle.ui.activity.SearchActivity;
import com.feiyou.headstyle.ui.adapter.HeadInfoAdapter;
import com.feiyou.headstyle.ui.adapter.HeadTypeAdapter;
import com.feiyou.headstyle.ui.base.BaseFragment;
import com.feiyou.headstyle.view.HomeDataView;
import com.orhanobut.logger.Logger;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by myflying on 2019/1/3.
 */
public class Home1Fragment extends BaseFragment implements HomeDataView, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    LinearLayout mSearchLayout;

    LinearLayout mSearchWrapperLayout;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mRefreshLayout;

    @BindView(R.id.layout_top_refresh1)
    RelativeLayout refreshLayout1;

    @BindView(R.id.home_head_list)
    RecyclerView mHeadInfoListView;

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

    @Override
    protected View onCreateView() {
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment1, null);
        ButterKnife.bind(this, root);
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
        mRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getActivity(),R.color.colorPrimary), Color.RED, Color.YELLOW, Color.BLUE);

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

        //广告模块
        LinearLayout adLayout = topView.findViewById(R.id.layout_ad);

        //加载更多动画
        mLoadingView = footView.findViewById(R.id.iv_loading);
        Glide.with(getActivity()).load(R.drawable.list_loading).into(mLoadingView);
    }

    public void initData() {
        homeDataPresenterImp = new HomeDataPresenterImp(this, getActivity());

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
        headInfoAdapter = new HeadInfoAdapter(getActivity(), null);
        mHeadInfoListView.setLayoutManager(gridLayoutManager);
        headInfoAdapter.addHeaderView(topView);
        mHeadInfoListView.setAdapter(headInfoAdapter);


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
                currentPage++;
                homeDataPresenterImp.getData(App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "", currentPage + "", "", "", 0);
            }
        }, mHeadInfoListView);

        homeDataPresenterImp.getData(App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "", "", "", "", 0);
    }

    public void initBanner() {
        mBanner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                Intent intent = new Intent(getActivity(), Collection2Activity.class);
                intent.putExtra("banner_id", bannerInfos.get(position).getId());
                startActivity(intent);
            }
        });
    }

    @OnClick(R.id.tv_refresh1)
    void refresh() {
        isFirstLoad = true;
        homeDataPresenterImp.getData(App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "", "", "", "1", 0);
        //scrollView.smoothScrollTo(0, SizeUtils.dp2px(510 - 48));
        mHeadInfoListView.scrollTo(0, SizeUtils.dp2px(200));
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
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void loadDataSuccess(ResultInfo tData) {
        mRefreshLayout.setRefreshing(false);
        if (tData != null && tData.getCode() == Constants.SUCCESS) {

            HomeDataWrapper homeDataRet = null;
            if (tData instanceof HomeDataRet) {
                homeDataRet = ((HomeDataRet) tData).getData();
                if (homeDataRet != null) {
                    currentPage = homeDataRet.getPage();
                    if (isFirstLoad) {
                        randomPage = currentPage;

                        //此处的信息，只需要设置一次
                        if (homeDataRet.getBannerList() != null && homeDataRet.getBannerList().size() > 0) {
                            bannerInfos = homeDataRet.getBannerList();
                            List<String> urls = new ArrayList<>();
                            for (int i = 0; i < bannerInfos.size(); i++) {
                                urls.add(bannerInfos.get(i).getIco());
                            }
                            //设置图片加载器
                            mBanner.setImageLoader(new GlideImageLoader()).setImages(urls).start();
                        }

                        if (homeDataRet.getCategoryInfoList() != null && homeDataRet.getCategoryInfoList().size() > 0) {
                            headTypeAdapter.setNewData(homeDataRet.getCategoryInfoList());
                        }

                        if (homeDataRet.getAdList() != null && homeDataRet.getAdList().size() > 0) {
                            Glide.with(getActivity()).load(homeDataRet.getAdList().get(0).getIco()).into(mAdImageView);
                        } else {
                            mAdLayout.setVisibility(View.GONE);
                        }
                    }

                    Logger.i("random page--->" + randomPage);

                    if (homeDataRet.getImagesList() != null && homeDataRet.getImagesList().size() > 0) {
                        if (isFirstLoad) {
                            headInfoAdapter.setNewData(homeDataRet.getImagesList());
                            isFirstLoad = false;
                        } else {
                            headInfoAdapter.addData(homeDataRet.getImagesList());
                        }

                        if (homeDataRet.getImagesList().size() == pageSize) {
                            headInfoAdapter.loadMoreComplete();
                        } else {
                            headInfoAdapter.loadMoreEnd();
                        }
                    }
                }
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

    @Override
    public void loadDataError(Throwable throwable) {
        mRefreshLayout.setRefreshing(false);
    }


    @Override
    public void onRefresh() {
        refresh();
    }
}
