package com.feiyou.headstyle.ui.fragment;

import android.content.Intent;
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
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.ArticleInfo;
import com.feiyou.headstyle.bean.BannerInfo;
import com.feiyou.headstyle.bean.HomeDataRet;
import com.feiyou.headstyle.bean.HomeDataWrapper;
import com.feiyou.headstyle.bean.NoteInfoDetailRet;
import com.feiyou.headstyle.bean.ResultInfo;
import com.feiyou.headstyle.common.Constants;
import com.feiyou.headstyle.common.GlideImageLoader;
import com.feiyou.headstyle.presenter.HomeDataPresenterImp;
import com.feiyou.headstyle.ui.activity.Collection2Activity;
import com.feiyou.headstyle.ui.activity.CommunityArticleActivity;
import com.feiyou.headstyle.ui.activity.FriendListActivity;
import com.feiyou.headstyle.ui.activity.HeadListActivity;
import com.feiyou.headstyle.ui.activity.HeadShowActivity;
import com.feiyou.headstyle.ui.activity.MoreTypeActivity;
import com.feiyou.headstyle.ui.activity.SearchActivity;
import com.feiyou.headstyle.ui.activity.ShowImageListActivity;
import com.feiyou.headstyle.ui.adapter.CommunityHeadAdapter;
import com.feiyou.headstyle.ui.adapter.HeadInfoAdapter;
import com.feiyou.headstyle.ui.adapter.HeadTypeAdapter;
import com.feiyou.headstyle.ui.base.BaseFragment;
import com.feiyou.headstyle.ui.custom.GlideRoundTransform;
import com.feiyou.headstyle.ui.custom.GridSpacingItemDecoration;
import com.feiyou.headstyle.view.HomeDataView;
import com.orhanobut.logger.Logger;
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

    LinearLayout mRecommendLayout;

    RecyclerView mCommunityHeadList;

    @BindView(R.id.layout_top_refresh1)
    LinearLayout refreshLayout1;

    LinearLayout refreshLayout2;

    RelativeLayout floatLayout;

    View mLineView;

    ImageView mAdImageView;

    LinearLayout mAdLayout;

    ImageView mUserHeadImageView;

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

    private int randomPage;

    private int currentPage = 1;

    private int pageSize = 30;

    private boolean isFirstLoad = true;

    private List<BannerInfo> bannerInfos;

    private View topView;

    private ArrayList<String> articleImages;

    ArticleInfo articleInfo;

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
        mRecommendLayout = topView.findViewById(R.id.layout_recommend);
        mUserHeadImageView = topView.findViewById(R.id.iv_comment_user_head);
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
                    intent.putExtra("tag_name", headTypeAdapter.getData().get(position).getTagsname());
                    intent.putExtra("tag_id", headTypeAdapter.getData().get(position).getId());
                    startActivity(intent);
                }
            }
        });

        communityHeadAdapter = new CommunityHeadAdapter(getActivity(), null, 72, true);
        mCommunityHeadList.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        mCommunityHeadList.setAdapter(communityHeadAdapter);
        communityHeadAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (articleImages != null && articleImages.size() > 0) {
                    Intent intent = new Intent(getActivity(), ShowImageListActivity.class);
                    intent.putExtra("image_index", position);
                    intent.putStringArrayListExtra("image_list", articleImages);
                    getActivity().startActivity(intent);
                }
            }
        });

        mRecommendLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CommunityArticleActivity.class);
                intent.putExtra("msg_id", articleInfo.getId());
                startActivity(intent);
            }
        });

        mFollowLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtils.showLong("点击了关注");
            }
        });

        headInfoAdapter = new HeadInfoAdapter(getActivity(), null);
        mHeadInfoListView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        mHeadInfoListView.setAdapter(headInfoAdapter);
        headInfoAdapter.addHeaderView(topView);

        headInfoAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Logger.i("video_id--->" + headInfoAdapter.getData().get(position).getId());

                int jumpPage = randomPage + position / pageSize;

                int jumpPosition = position % pageSize;

                Intent intent = new Intent(getActivity(), HeadShowActivity.class);
                intent.putExtra("jump_page", jumpPage);
                intent.putExtra("jump_position", jumpPosition);
                startActivity(intent);
            }
        });

        headInfoAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                currentPage++;
                homeDataPresenterImp.getData(currentPage + "", "", "", 0);
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
                    //refreshLayout2.setVisibility(View.INVISIBLE);
                } else {
                    refreshLayout1.setVisibility(View.INVISIBLE);
                    //refreshLayout2.setVisibility(View.VISIBLE);
                }
            }
        });

        homeDataPresenterImp.getData("", "", "", 0);
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
    public void loadDataSuccess(ResultInfo tData) {
        if (tData != null && tData.getCode() == Constants.SUCCESS) {

            HomeDataWrapper homeDataRet = null;
            if (tData instanceof HomeDataRet) {
                homeDataRet = ((HomeDataRet) tData).getData();
                if (homeDataRet != null) {
                    currentPage = homeDataRet.getPage();
                    randomPage = currentPage;

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

                    if (homeDataRet.getMessageList() != null && homeDataRet.getMessageList().size() > 0) {

                        articleInfo = homeDataRet.getMessageList().get(0);
                        articleImages = new ArrayList<>();
                        if (articleInfo != null) {

                            mUserNickNameTv.setText(StringUtils.isEmpty(articleInfo.getNickname()) ? "火星用户" : articleInfo.getNickname());
                            mTopicNameTv.setText(articleInfo.getTopicName());
                            mArticleDateTv.setText(TimeUtils.millis2String(articleInfo.getAddTime() * 1000));
                            mArticleContentTv.setText(articleInfo.getContent());

                            RequestOptions options = new RequestOptions();
                            options.transform(new GlideRoundTransform(getActivity(), 21));
                            options.placeholder(R.mipmap.head_def).error(R.mipmap.head_def);
                            Glide.with(this).load(articleInfo.getUserimg()).apply(options).into(mUserHeadImageView);

                            if (articleInfo.getImageArr() != null) {
                                for (int i = 0; i < articleInfo.getImageArr().length; i++) {
                                    articleImages.add(articleInfo.getImageArr()[i]);
                                }
                            }
                        }
                        communityHeadAdapter.setNewData(articleImages);
                    }

                    if (homeDataRet.getImagesList() != null && homeDataRet.getImagesList().size() > 0) {
                        if (isFirstLoad) {
                            headInfoAdapter.setNewData(homeDataRet.getImagesList());
                            isFirstLoad = false;
                        } else {
                            headInfoAdapter.addData(homeDataRet.getImagesList());
                        }

                        if (homeDataRet.getImagesList().size() < pageSize) {
                            headInfoAdapter.loadMoreEnd();
                        } else {
                            headInfoAdapter.loadMoreComplete();
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
            default:
                break;
        }
    }

    @Override
    public void loadDataError(Throwable throwable) {

    }

    void refresh() {
        isFirstLoad = true;
        homeDataPresenterImp.getData("", "", "1", 0);
    }
}
