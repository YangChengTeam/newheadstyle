package com.feiyou.headstyle.ui.fragment;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.BannerInfo;
import com.feiyou.headstyle.bean.HeadInfo;
import com.feiyou.headstyle.bean.HeadType;
import com.feiyou.headstyle.bean.HomeDataRet;
import com.feiyou.headstyle.common.Constants;
import com.feiyou.headstyle.common.GlideImageLoader;
import com.feiyou.headstyle.presenter.HomeDataPresenterImp;
import com.feiyou.headstyle.ui.activity.Collection1Activity;
import com.feiyou.headstyle.ui.activity.Collection2Activity;
import com.feiyou.headstyle.ui.activity.CollectionActivity;
import com.feiyou.headstyle.ui.activity.MainActivity;
import com.feiyou.headstyle.ui.adapter.CommunityHeadAdapter;
import com.feiyou.headstyle.ui.adapter.HeadInfoAdapter;
import com.feiyou.headstyle.ui.adapter.HeadTypeAdapter;
import com.feiyou.headstyle.ui.base.BaseFragment;
import com.feiyou.headstyle.ui.custom.MyScrollView;
import com.feiyou.headstyle.view.HomeDataView;
import com.orhanobut.logger.Logger;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by iflying on 2018/2/6.
 */

public class HomeFragment extends BaseFragment implements MyScrollView.OnScrollListener, MainActivity.IOnFocusListener, HomeDataView {

    @BindView(R.id.scroll_view)
    MyScrollView myScrollView;

    @BindView(R.id.topbar)
    QMUITopBar mTopBar;

    @BindView(R.id.banner)
    Banner mBanner;

    @BindView(R.id.head_type_list)
    RecyclerView mHeadTypeList;

    @BindView(R.id.community_head_list)
    RecyclerView mCommunityHeadList;

    @BindView(R.id.head_info_list)
    RecyclerView mHeadInfoList;

    @BindView(R.id.layout_top_refresh1)
    LinearLayout refreshLayout1;

    @BindView(R.id.layout_top_refresh2)
    LinearLayout refreshLayout2;

    @BindView(R.id.float_layout)
    RelativeLayout floatLayout;

    @BindView(R.id.main_line_view)
    View mLineView;

    @BindView(R.id.iv_home_ad)
    ImageView mAdImageView;

    @BindView(R.id.layout_ad)
    LinearLayout mAdLayout;

    HeadTypeAdapter headTypeAdapter;

    CommunityHeadAdapter communityHeadAdapter;

    HeadInfoAdapter headInfoAdapter;

    private int searchLayoutTop;

    private HomeDataPresenterImp homeDataPresenterImp;

    private int currentPage = 1;

    private int pageSize = 30;

    private boolean isFirstLoad = true;

    private List<BannerInfo> bannerInfos;

    @Override
    protected View onCreateView() {
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_home, null);
        ButterKnife.bind(this, root);
        initData();
        return root;
    }

    public void initData() {
        initBanner();
        homeDataPresenterImp = new HomeDataPresenterImp(this, getActivity());

        View topSearchView = getLayoutInflater().inflate(R.layout.main_top_view, null);
        mTopBar.setCenterView(topSearchView);

        List<HeadInfo> communityHeadList = new ArrayList<>();

        for (int i = 0; i < 50; i++) {
            if (i < 4) {
                communityHeadList.add(new HeadInfo());
            }
        }

        headTypeAdapter = new HeadTypeAdapter(getActivity(), null);

        mHeadTypeList.setLayoutManager(new GridLayoutManager(getActivity(), 4) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        mHeadTypeList.setAdapter(headTypeAdapter);

        communityHeadAdapter = new CommunityHeadAdapter(getActivity(), null);
        mCommunityHeadList.setLayoutManager(new GridLayoutManager(getActivity(), 4) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        mCommunityHeadList.setAdapter(communityHeadAdapter);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };

        headInfoAdapter = new HeadInfoAdapter(getActivity(), null);
        mHeadInfoList.setLayoutManager(gridLayoutManager);
        mHeadInfoList.setAdapter(headInfoAdapter);
        mHeadInfoList.setHasFixedSize(true);
        mHeadInfoList.setNestedScrollingEnabled(false);
        myScrollView.setOnScrollListener(this);

        homeDataPresenterImp.getData("", "", "");

        headInfoAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                currentPage++;
                homeDataPresenterImp.getData(currentPage + "", "", "");
            }
        }, mHeadInfoList);

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
    public void onScroll(int scrollY) {
        if (scrollY >= searchLayoutTop) {
            if (floatLayout.getParent() != refreshLayout1) {
                refreshLayout2.removeView(floatLayout);
                refreshLayout1.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));
                refreshLayout1.addView(floatLayout);
            }
        } else {
            if (floatLayout.getParent() != refreshLayout2) {
                refreshLayout1.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.transparent));
                refreshLayout1.removeView(floatLayout);
                refreshLayout2.addView(floatLayout);
            }
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        Logger.i("onWindowFocusChanged--->");
        if (hasFocus) {
            searchLayoutTop = mLineView.getBottom();// 获取line的顶部位置
        }
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void dismissProgress() {

    }

    @Override
    public void loadDataSuccess(HomeDataRet tData) {
        if (tData != null && tData.getCode() == Constants.SUCCESS) {
            if (tData.getData() != null) {
                currentPage = tData.getData().getPage();

                if (tData.getData().getBannerList() != null && tData.getData().getBannerList().size() > 0) {
                    bannerInfos = tData.getData().getBannerList();
                    List<String> urls = new ArrayList<>();
                    for (int i = 0; i < bannerInfos.size(); i++) {
                        urls.add(bannerInfos.get(i).getIco());
                    }
                    //设置图片加载器
                    mBanner.setImageLoader(new GlideImageLoader()).setImages(urls).start();
                }

                if (tData.getData().getCategoryInfoList() != null && tData.getData().getCategoryInfoList().size() > 0) {
                    headTypeAdapter.setNewData(tData.getData().getCategoryInfoList());
                }

                if (tData.getData().getAdList() != null && tData.getData().getAdList().size() > 0) {
                    Glide.with(getActivity()).load(tData.getData().getAdList().get(0).getIco()).into(mAdImageView);
                } else {
                    mAdLayout.setVisibility(View.GONE);
                }

                if (tData.getData().getImagesList() != null && tData.getData().getImagesList().size() > 0) {
                    if (isFirstLoad) {
                        headInfoAdapter.setNewData(tData.getData().getImagesList());
                        isFirstLoad = false;
                    } else {
                        headInfoAdapter.addData(tData.getData().getImagesList());
                    }

                    if (tData.getData().getImagesList().size() < pageSize) {
                        headInfoAdapter.loadMoreEnd();
                    } else {
                        headInfoAdapter.loadMoreComplete();
                    }
                }
            }
        }
    }

    @Override
    public void loadDataError(Throwable throwable) {

    }

    @OnClick({R.id.tv_refresh, R.id.iv_refresh})
    void refresh() {
        isFirstLoad = true;
        homeDataPresenterImp.getData("", "", "1");
    }
}
