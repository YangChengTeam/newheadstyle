package com.feiyou.headstyle.ui.fragment;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feiyou.headstyle.App;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.TestInfo;
import com.feiyou.headstyle.bean.TestInfoRet;
import com.feiyou.headstyle.common.Constants;
import com.feiyou.headstyle.common.GlideImageLoader;
import com.feiyou.headstyle.presenter.TestInfoPresenterImp;
import com.feiyou.headstyle.ui.activity.MoreTestActivity;
import com.feiyou.headstyle.ui.activity.StarDetailActivity;
import com.feiyou.headstyle.ui.activity.StarListActivity;
import com.feiyou.headstyle.ui.activity.TestActivity;
import com.feiyou.headstyle.ui.activity.TestCategoryActivity;
import com.feiyou.headstyle.ui.activity.TestDetailActivity;
import com.feiyou.headstyle.ui.activity.TestImageDetailActivity;
import com.feiyou.headstyle.ui.adapter.TestInfoAdapter;
import com.feiyou.headstyle.ui.base.BaseFragment;
import com.feiyou.headstyle.ui.custom.LoginDialog;
import com.feiyou.headstyle.ui.custom.NormalDecoration;
import com.feiyou.headstyle.ui.custom.OpenDialog;
import com.feiyou.headstyle.view.TestInfoView;
import com.orhanobut.logger.Logger;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;
import com.wang.avi.AVLoadingIndicatorView;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by myflying on 2019/2/20.
 */
public class TestFragment extends BaseFragment implements TestInfoView, View.OnClickListener, OpenDialog.ConfigListener {

    @BindView(R.id.hot_list)
    RecyclerView mHotTestListView;

    @BindView(R.id.avi)
    AVLoadingIndicatorView avi;

    @BindView(R.id.layout_no_data)
    LinearLayout mNoDataLayout;

    @BindView(R.id.tv_reload)
    TextView mReLoadTv;

    LinearLayout mBannerLayout;

    LinearLayout mFunTestLayout;

    LinearLayout mIQTestLayout;

    LinearLayout mXinliTestLayout;

    LinearLayout mTDTestLayout;

    LinearLayout mStarLayout;

    TextView mMoreTv;

    private View topView;

    Banner mBanner;

    TestInfoAdapter testInfoAdapter;

    private TestInfoPresenterImp testInfoPresenterImp;

    LoginDialog loginDialog;

    OpenDialog openDialog;

    @Override
    protected View onCreateView() {
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_test, null);
        ButterKnife.bind(this, root);
        initViews();
        return root;
    }

    public void initViews() {
        MobclickAgent.onEvent(getActivity(), "click_ceshi", AppUtils.getAppVersionName());
        loginDialog = new LoginDialog(getActivity(), R.style.login_dialog);
        openDialog = new OpenDialog(getActivity(), R.style.login_dialog);
        openDialog.setConfigListener(this);

        topView = LayoutInflater.from(getActivity()).inflate(R.layout.test_top, null);
        mBannerLayout = topView.findViewById(R.id.layout_banner);
        mBanner = topView.findViewById(R.id.test_banner);
        mFunTestLayout = topView.findViewById(R.id.layout_funs);
        mIQTestLayout = topView.findViewById(R.id.layout_iq);
        mXinliTestLayout = topView.findViewById(R.id.layout_xinli);
        mTDTestLayout = topView.findViewById(R.id.layout_tuodan);
        mStarLayout = topView.findViewById(R.id.layout_star);
        mMoreTv = topView.findViewById(R.id.tv_more);

        mFunTestLayout.setOnClickListener(this);
        mIQTestLayout.setOnClickListener(this);
        mXinliTestLayout.setOnClickListener(this);
        mTDTestLayout.setOnClickListener(this);
        mStarLayout.setOnClickListener(this);
        mMoreTv.setOnClickListener(this);

        testInfoPresenterImp = new TestInfoPresenterImp(this, getActivity());
        testInfoAdapter = new TestInfoAdapter(getActivity(), null);
        mHotTestListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mHotTestListView.setAdapter(testInfoAdapter);
        mHotTestListView.addItemDecoration(new NormalDecoration(ContextCompat.getColor(getActivity(), R.color.line_color), 1));

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        params.setMargins(0, 0, 0, SizeUtils.dp2px(48));
        mHotTestListView.setLayoutParams(params);

        LinearLayout.LayoutParams bannerParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(112));
        bannerParams.setMargins(0, BarUtils.getStatusBarHeight(), 0, 0);
        mBannerLayout.setLayoutParams(bannerParams);

        List<Integer> urls = new ArrayList<>();
        urls.add(R.mipmap.test_banner1);
        urls.add(R.mipmap.test_banner2);
        //设置图片加载器
        mBanner.setImageLoader(new GlideImageLoader()).setImages(urls).start();

        mBanner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                if (position == 0) {
                    if (!App.getApp().isLogin) {
                        if (loginDialog != null && !loginDialog.isShowing()) {
                            loginDialog.show();
                        }
                        return;
                    }

                    int tempIndex = SPUtils.getInstance().getInt("star_index", -1);
                    if (tempIndex > -1) {
                        Intent intent = new Intent(getActivity(), StarDetailActivity.class);
                        intent.putExtra("star_index", tempIndex);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(getActivity(), StarListActivity.class);
                        startActivity(intent);
                    }
                }

                if (position == 1) {
                    openDialog.setTitle("打开提示");
                    openDialog.setContent("即将打开\"头像达人\"小程序");
                    openDialog.show();
                }
            }
        });

        testInfoAdapter.setHeaderView(topView);
        testInfoPresenterImp.getHotAndRecommendList(1);

        testInfoAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                if (!App.getApp().isLogin) {
                    if (loginDialog != null && !loginDialog.isShowing()) {
                        loginDialog.show();
                    }
                    return;
                }

                if (testInfoAdapter.getData().get(position).getTestType() == 1) {
                    Intent intent = new Intent(getActivity(), TestDetailActivity.class);
                    intent.putExtra("tid", testInfoAdapter.getData().get(position).getId());
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), TestImageDetailActivity.class);
                    intent.putExtra("tid", testInfoAdapter.getData().get(position).getId());
                    startActivity(intent);
                }

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

    public void testCategory(String cid, String title) {
        Intent intent = new Intent(getActivity(), TestCategoryActivity.class);
        intent.putExtra("cid", cid);
        intent.putExtra("title", title);
        startActivity(intent);
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void dismissProgress() {
        avi.hide();
    }

    @Override
    public void loadDataSuccess(TestInfoRet tData) {
        Logger.i("test list data --->" + JSON.toJSONString(tData));
        avi.hide();
        if (tData != null && tData.getCode() == Constants.SUCCESS) {
            if (tData.getData() != null) {
                mNoDataLayout.setVisibility(View.GONE);
                mHotTestListView.setVisibility(View.VISIBLE);
                App.testInfoList = tData.getData();
                List<TestInfo> tempDateList = tData.getData();
                int maxLength = tempDateList.size() > 20 ? 20 : tempDateList.size();
                testInfoAdapter.setNewData(tempDateList.subList(0, maxLength));
            } else {
                mNoDataLayout.setVisibility(View.VISIBLE);
                mHotTestListView.setVisibility(View.GONE);
            }
        } else {
            mNoDataLayout.setVisibility(View.VISIBLE);
            mHotTestListView.setVisibility(View.GONE);
        }
    }

    @Override
    public void loadDataError(Throwable throwable) {
        mNoDataLayout.setVisibility(View.VISIBLE);
        mHotTestListView.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_funs:
                testCategory("1", "趣味测试");
                break;
            case R.id.layout_iq:
                testCategory("2", "智商测试");
                break;
            case R.id.layout_xinli:
                testCategory("3", "心理测试");
                break;
            case R.id.layout_tuodan:
                testCategory("4", "脱单测试");
                break;
            case R.id.tv_more:
                Intent intentMore = new Intent(getActivity(), MoreTestActivity.class);
                startActivity(intentMore);
                break;
            case R.id.layout_star:
                if (!App.getApp().isLogin) {
                    if (loginDialog != null && !loginDialog.isShowing()) {
                        loginDialog.show();
                    }
                    return;
                }
                int tempIndex = SPUtils.getInstance().getInt("star_index", -1);
                if (tempIndex > -1) {
                    Intent intent = new Intent(getActivity(), StarDetailActivity.class);
                    intent.putExtra("star_index", tempIndex);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), StarListActivity.class);
                    startActivity(intent);
                }
                break;
        }
    }

    @Override
    public void openConfig() {
        String appId = "wxd1112ca9a216aeda"; // 填应用AppId
        IWXAPI api = WXAPIFactory.createWXAPI(getActivity(), appId);
        WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
        req.userName = "gh_c7bbf594c99b\n"; // 填小程序原始idx
        req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE;// 可选打开 开发版，体验版和正式版
        api.sendReq(req);
    }

    @Override
    public void openCancel() {
        if (openDialog != null && openDialog.isShowing()) {
            openDialog.dismiss();
        }
    }

    @OnClick(R.id.tv_reload)
    void reload() {
        mNoDataLayout.setVisibility(View.GONE);
        avi.show();
        testInfoPresenterImp.getHotAndRecommendList(1);
    }
}
