package com.feiyou.headstyle.ui.fragment;

import android.content.Intent;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
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
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.ArticleInfo;
import com.feiyou.headstyle.bean.BannerInfo;
import com.feiyou.headstyle.bean.HeadInfo;
import com.feiyou.headstyle.bean.HomeDataRet;
import com.feiyou.headstyle.common.Constants;
import com.feiyou.headstyle.common.GlideImageLoader;
import com.feiyou.headstyle.presenter.HomeDataPresenterImp;
import com.feiyou.headstyle.ui.activity.Collection2Activity;
import com.feiyou.headstyle.ui.activity.FriendListActivity;
import com.feiyou.headstyle.ui.activity.HeadEditActivity;
import com.feiyou.headstyle.ui.activity.HeadListActivity;
import com.feiyou.headstyle.ui.activity.MoreTypeActivity;
import com.feiyou.headstyle.ui.activity.SearchActivity;
import com.feiyou.headstyle.ui.adapter.CommunityHeadAdapter;
import com.feiyou.headstyle.ui.adapter.HeadInfoAdapter;
import com.feiyou.headstyle.ui.adapter.HeadTypeAdapter;
import com.feiyou.headstyle.ui.base.BaseFragment;
import com.feiyou.headstyle.view.HomeDataView;
import com.orhanobut.logger.Logger;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by myflying on 2019/1/3.
 */
public class Home1Fragment extends BaseFragment implements HomeDataView, View.OnClickListener {

    LinearLayout mSearchLayout;

    LinearLayout mSearchWrapperLayout;

    @BindView(R.id.home_head_list)
    RecyclerView mHeadInfoListView;

    Banner mBanner;

    RecyclerView mHeadTypeList;

    RecyclerView mCommunityHeadList;

    @BindView(R.id.layout_top_refresh1)
    LinearLayout refreshLayout1;

    LinearLayout refreshLayout2;

    RelativeLayout floatLayout;

    View mLineView;

    ImageView mAdImageView;

    LinearLayout mAdLayout;

    TextView mUserNickNameTv;

    TextView mTopicNameTv;

    TextView mArticleDateTv;

    FrameLayout mFollowLayout;

    TextView mArticleContentTv;

    HeadTypeAdapter headTypeAdapter;

    CommunityHeadAdapter communityHeadAdapter;

    HeadInfoAdapter headInfoAdapter;

    private int searchLayoutTop;

    private HomeDataPresenterImp homeDataPresenterImp;

    private int currentPage = 1;

    private int pageSize = 30;

    private boolean isFirstLoad = true;

    private List<BannerInfo> bannerInfos;

    private View topView;

    @Override
    protected View onCreateView() {
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment2, null);
        ButterKnife.bind(this, root);
        initTopView();
        initBanner();
        initData();
        return root;
    }

    public void initTopView() {


        //QMUIStatusBarHelper.setStatusBarLightMode(getActivity());


        topView = LayoutInflater.from(getActivity()).inflate(R.layout.home_top, null);
        mBanner = topView.findViewById(R.id.banner);
        mHeadTypeList = topView.findViewById(R.id.head_type_list);
        mCommunityHeadList = topView.findViewById(R.id.community_head_list);
        //refreshLayout1 = topView.findViewById(R.id.layout_top_refresh1);
        refreshLayout2 = topView.findViewById(R.id.layout_top_refresh2);
        floatLayout = topView.findViewById(R.id.float_layout);
        mLineView = topView.findViewById(R.id.main_line_view);
        mAdImageView = topView.findViewById(R.id.iv_home_ad);
        mAdLayout = topView.findViewById(R.id.layout_ad);

        mSearchWrapperLayout = topView.findViewById(R.id.layout_search_wrapper);
        mSearchLayout = topView.findViewById(R.id.layout_search);
        mUserNickNameTv = topView.findViewById(R.id.tv_user_nick_name);
        mTopicNameTv = topView.findViewById(R.id.tv_topic_name);
        mArticleDateTv = topView.findViewById(R.id.tv_article_date);
        mFollowLayout = topView.findViewById(R.id.layout_follow);
        mArticleContentTv = topView.findViewById(R.id.tv_article_content);

        mSearchLayout.setOnClickListener(this);

        LinearLayout.LayoutParams searchParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(48));
        searchParams.setMargins(0, BarUtils.getStatusBarHeight(), 0, 0);
        mSearchWrapperLayout.setLayoutParams(searchParams);

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
                    intent.putExtra("tag_id", headTypeAdapter.getData().get(position).getId());
                    startActivity(intent);
                }
            }
        });

        communityHeadAdapter = new CommunityHeadAdapter(getActivity(), null);
        mCommunityHeadList.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        mCommunityHeadList.setAdapter(communityHeadAdapter);

//        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
//            @Override
//            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                int tempY = SizeUtils.px2dp(scrollY);
//                if (tempY > 520) {
//                    //mSearchWrapperLayout.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
//                } else {
//                    //mSearchWrapperLayout.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.transparent));
//                }
//
//                //判断是否滑动到了底部
//                if (scrollY + SizeUtils.dp2px(48) >= (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
//                    currentPage++;
//                    homeDataPresenterImp.getData(currentPage + "", "", "");
//                }
//            }
//        });

        headInfoAdapter = new HeadInfoAdapter(getActivity(), null);
        mHeadInfoListView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        mHeadInfoListView.setAdapter(headInfoAdapter);
        headInfoAdapter.addHeaderView(topView);

        headInfoAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getActivity(), HeadEditActivity.class);
                startActivity(intent);
            }
        });

        headInfoAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                currentPage++;
                homeDataPresenterImp.getData(currentPage + "", "", "");
            }
        }, mHeadInfoListView);

        mHeadInfoListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int tempY = SizeUtils.px2dp(getScrollYDistance()); //686

                //ToastUtils.showLong("tempY--->" + tempY);

                if (tempY > 686) {
                    refreshLayout1.setVisibility(View.VISIBLE);
                    refreshLayout2.setVisibility(View.INVISIBLE);
                } else {
                    refreshLayout1.setVisibility(View.INVISIBLE);
                    refreshLayout2.setVisibility(View.VISIBLE);
                }
            }
        });

        homeDataPresenterImp.getData("", "", "");
    }

    public int getScrollYDistance() {
        GridLayoutManager gridLayoutManager = (GridLayoutManager) mHeadInfoListView.getLayoutManager();
        //得出spanCount几列或几排
        int itemSpanCount = gridLayoutManager.getSpanCount();
        //得出的position是一排或一列总和
        int position = gridLayoutManager.findFirstVisibleItemPosition();
        //需要算出才是即将移出屏幕Item的position
        int itemPosition = position / itemSpanCount;
        //因为是相同的Item所以取那个都一样
        View firstVisiableChildView = gridLayoutManager.findViewByPosition(position);
        int itemHeight = firstVisiableChildView.getHeight();
        int itemTop = firstVisiableChildView.getTop();
        int iposition = itemPosition * itemHeight;
        int iResult = iposition - itemTop;
        return iResult;
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

                if (tData.getData().getMessageList() != null && tData.getData().getMessageList().size() > 0) {

                    ArticleInfo articleInfo = tData.getData().getMessageList().get(0);
                    List<String> articleImages = new ArrayList<>();
                    if (articleInfo != null) {

                        mUserNickNameTv.setText(articleInfo.getNickname());
                        mTopicNameTv.setText(articleInfo.getTopicName());
                        mArticleDateTv.setText(TimeUtils.millis2String(articleInfo.getAddTime() * 1000));
                        mArticleContentTv.setText(articleInfo.getContent());

                        if (articleInfo.getImageArr() != null) {
                            for (int i = 0; i < articleInfo.getImageArr().length; i++) {
                                articleImages.add(articleInfo.getImageArr()[i]);
                            }
                        }
                    }
                    communityHeadAdapter.setNewData(articleImages);
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_search:
                Intent intent = new Intent(getActivity(), FriendListActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public void loadDataError(Throwable throwable) {

    }

    void refresh() {
        isFirstLoad = true;
        homeDataPresenterImp.getData("", "", "1");
    }
}
