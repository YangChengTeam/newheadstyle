package com.feiyou.headstyle.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdManager;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feiyou.headstyle.App;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.HeadInfo;
import com.feiyou.headstyle.bean.HeadInfoRet;
import com.feiyou.headstyle.bean.ResultInfo;
import com.feiyou.headstyle.common.Constants;
import com.feiyou.headstyle.presenter.HeadListDataPresenterImp;
import com.feiyou.headstyle.ui.adapter.HeadInfoAdapter;
import com.feiyou.headstyle.ui.adapter.HeadMultipleAdapter;
import com.feiyou.headstyle.ui.base.BaseFragmentActivity;
import com.feiyou.headstyle.utils.StatusBarUtil;
import com.feiyou.headstyle.utils.TTAdManagerHolder;
import com.feiyou.headstyle.view.HeadListDataView;
import com.orhanobut.logger.Logger;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by myflying on 2018/11/23.
 */
public class HeadListActivity extends BaseFragmentActivity implements HeadListDataView, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.topbar)
    QMUITopBar mTopBar;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mRefreshLayout;

    @BindView(R.id.avi)
    AVLoadingIndicatorView avi;

    @BindView(R.id.head_info_list)
    RecyclerView mHeadInfoListView;

    @BindView(R.id.layout_no_data)
    LinearLayout mNoDataLayout;

    @BindView(R.id.tv_reload)
    TextView mReloadTv;

    ImageView mBackImageView;

    //HeadInfoAdapter headInfoAdapter;

    private HeadListDataPresenterImp headListDataPresenterImp;

    private int currentPage = 1;

    private int pageSize = 30;

    private String tagId;

    private String tagName;

    HeadMultipleAdapter headMultipleAdapter;

    private TTAdNative mTTAdNative;

    private int LIST_LINE = 10;//广告加载一页，为10行（暂定）

    private GridLayoutManager gridLayoutManager;

    @Override
    protected int getContextViewId() {
        return R.layout.activity_head_list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    private void initView() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && !StringUtils.isEmpty(bundle.getString("tag_id"))) {
            tagId = bundle.getString("tag_id");
        }

        if (bundle != null && !StringUtils.isEmpty(bundle.getString("tag_name"))) {
            tagName = bundle.getString("tag_name");
        }

        //step1:初始化sdk
        TTAdManager ttAdManager = TTAdManagerHolder.get();
        mTTAdNative = ttAdManager.createAdNative(getApplicationContext());

        View topView = getLayoutInflater().inflate(R.layout.common_top_back, null);
        topView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(48)));

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(48));
        params.setMargins(0, StatusBarUtil.getStatusBarHeight(this), 0, 0);
        mTopBar.setCenterView(topView);
        mBackImageView = topView.findViewById(R.id.iv_back);

        TextView mTitleTv = topView.findViewById(R.id.tv_title);
        mTitleTv.setText(StringUtils.isEmpty(tagName) ? "个性头像" : tagName);

        mBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });

        QMUIStatusBarHelper.setStatusBarLightMode(this);
    }

    public void initData() {
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

        headListDataPresenterImp = new HeadListDataPresenterImp(this, this);

        headMultipleAdapter = new HeadMultipleAdapter(null);
        //headInfoAdapter = new HeadInfoAdapter(this, null);

        gridLayoutManager = new GridLayoutManager(this, 3);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == 9 || (position > 10 && (position - 9) % 10 == 0)) {
                    Logger.i("ad pos--->" + position);
                    return 3;
                }
                return 1;
            }
        });

        mHeadInfoListView.setLayoutManager(gridLayoutManager);
        mHeadInfoListView.setAdapter(headMultipleAdapter);

        /*View topEmptyView = new View(this);
        topEmptyView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(12)));
        headMultipleAdapter.setHeaderView(topEmptyView);*/

        headMultipleAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                int jumpPage = position / pageSize;
                int jumpPosition = position % pageSize;

                Logger.i("jumpPage page--->" + jumpPage + "---jumpPosition--->" + jumpPosition);

                Intent intent = new Intent(HeadListActivity.this, HeadShowActivity.class);
                intent.putExtra("from_type", 1);
                intent.putExtra("tag_id", tagId);
                intent.putExtra("jump_page", jumpPage + 1);
                intent.putExtra("jump_position", jumpPosition);
                startActivity(intent);
            }
        });

        headMultipleAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                currentPage++;
                headListDataPresenterImp.getDataByTagId(App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "", tagId, currentPage, pageSize);
            }
        }, mHeadInfoListView);

        avi.show();
        headListDataPresenterImp.getDataByTagId(App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "", tagId, currentPage, pageSize);
    }

    @Override
    public void onBackPressed() {
        popBackStack();
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
        avi.hide();
        mRefreshLayout.setRefreshing(false);
        if (tData != null && tData.getCode() == Constants.SUCCESS) {
            if (tData instanceof HeadInfoRet) {

                if (((HeadInfoRet) tData).getData() != null && ((HeadInfoRet) tData).getData().size() > 0) {
                    mHeadInfoListView.setVisibility(View.VISIBLE);
                    mNoDataLayout.setVisibility(View.GONE);
                    if (currentPage == 1) {

                        List<HeadInfo> tempList = ((HeadInfoRet) tData).getData();
                        for (HeadInfo headInfo : tempList) {
                            headInfo.setItemType(HeadInfo.HEAD_IMG);
                        }

                        for (int i = 0; i < 3; i++) {
                            int tempIndex = (currentPage - 1) * pageSize + (i + 1) * 9 + i;
                            HeadInfo tempHeadInfo = new HeadInfo(HeadInfo.HEAD_AD);
                            tempList.add(tempIndex, tempHeadInfo);
                        }

                        headMultipleAdapter.setNewData(tempList);
                    } else {
                        List<HeadInfo> tempList = ((HeadInfoRet) tData).getData();
                        for (HeadInfo headInfo : tempList) {
                            headInfo.setItemType(HeadInfo.HEAD_IMG);
                        }

                        headMultipleAdapter.addData((tempList));

                        for (int i = 0; i < 3; i++) {
                            int tempIndex = (currentPage - 1) * pageSize + (i + 1) * 9 + i;
                            HeadInfo tempHeadInfo = new HeadInfo(HeadInfo.HEAD_AD);
                            headMultipleAdapter.getData().add(tempIndex, tempHeadInfo);
                        }
                    }
                    Logger.i("current page--->" + currentPage);

                    if (currentPage == 1) {
                        if (((HeadInfoRet) tData).getData().size() == pageSize + 3) {
                            headMultipleAdapter.loadMoreComplete();
                        } else {
                            headMultipleAdapter.loadMoreEnd(true);
                        }
                    } else {
                        if (((HeadInfoRet) tData).getData().size() == pageSize) {
                            headMultipleAdapter.loadMoreComplete();
                        } else {
                            headMultipleAdapter.loadMoreEnd(true);
                        }
                    }

                    gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                        @Override
                        public int getSpanSize(int position) {
                            if (position == 9 || (position > 10 && (position - 9) % 10 == 0)) {
                                Logger.i("ad pos--->" + position);
                                return 3;
                            }
                            return 1;
                        }
                    });

                    //TODO
                    loadListAd();
                } else {
                    mHeadInfoListView.setVisibility(View.GONE);
                    mNoDataLayout.setVisibility(View.VISIBLE);
                }
            }
        } else {
            if (currentPage == 1) {
                mHeadInfoListView.setVisibility(View.GONE);
                mNoDataLayout.setVisibility(View.VISIBLE);
            }
            Logger.i(StringUtils.isEmpty(tData.getMsg()) ? "加载失败" : tData.getMsg());
        }
    }

    @Override
    public void loadDataError(Throwable throwable) {
        avi.hide();
        mRefreshLayout.setRefreshing(false);
        if (currentPage == 1) {
            mHeadInfoListView.setVisibility(View.GONE);
            mNoDataLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onRefresh() {
        mRefreshLayout.setRefreshing(true);
        currentPage = 1;
        headListDataPresenterImp.getDataByTagId(App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "", tagId, currentPage, pageSize);
    }

    @OnClick(R.id.tv_reload)
    void reLoad() {
        onRefresh();
    }

    /**
     * 加载feed广告
     */
    private void loadListAd() {
        Logger.i("dpi--->" + ScreenUtils.getScreenDensityDpi() + "demnsi" + ScreenUtils.getScreenDensity());
        float expressViewWidth = ScreenUtils.getScreenDensityDpi();
        float expressViewHeight = 0;

        //step4:创建feed广告请求类型参数AdSlot,具体参数含义参考文档
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId("945142340")
                .setSupportDeepLink(true)
                .setExpressViewAcceptedSize(expressViewWidth, expressViewHeight) //期望模板广告view的size,单位dp
                .setAdCount(3) //请求广告数量为1到3条
                .build();
        //step5:请求广告，调用feed广告异步请求接口，加载到广告后，拿到广告素材自定义渲染
        mTTAdNative.loadNativeExpressAd(adSlot, new TTAdNative.NativeExpressAdListener() {
            @Override
            public void onError(int code, String message) {
                Logger.i("feed error--->" + code + "---message--->" + message);
                //TToast.show(NativeExpressListActivity.this, message);
            }

            @Override
            public void onNativeExpressAdLoad(List<TTNativeExpressAd> ads) {
                if (ads == null || ads.isEmpty()) {
                    Logger.i("on FeedAdLoaded: ad is null!");
                    //TToast.show(NativeExpressListActivity.this, "on FeedAdLoaded: ad is null!");
                    return;
                }
                bindAdListener(ads);
            }
        });
    }

    private void bindAdListener(final List<TTNativeExpressAd> ads) {
        Logger.i("feed ads --->" + ads.size());

        for (int i = 0; i < ads.size(); i++) {
            final TTNativeExpressAd adTmp = ads.get(i);
            int tempIndex = (currentPage - 1) * pageSize + (i + 1) * 9 + i;
            HeadInfo tempHeadInfo = headMultipleAdapter.getData().get(tempIndex);
            tempHeadInfo.setTtNativeExpressAd(adTmp);
            headMultipleAdapter.getData().set(tempIndex, tempHeadInfo);

            //headMultipleAdapter.notifyDataSetChanged();

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
                    //返回view的宽高 单位 dp
                    //TToast.show(NativeExpressListActivity.this, "渲染成功");
                    //headMultipleAdapter.notifyDataSetChanged();
                    //刷新指定的item
                    headMultipleAdapter.notifyItemChanged(tempIndex);
                }
            });
            adTmp.render();
        }

        Logger.i("total size--->" + headMultipleAdapter.getData().size());

    }

}
