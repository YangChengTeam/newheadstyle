package com.feiyou.headstyle.ui.fragment;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feiyou.headstyle.App;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.TestInfoRet;
import com.feiyou.headstyle.common.Constants;
import com.feiyou.headstyle.common.GlideImageLoader;
import com.feiyou.headstyle.presenter.TestInfoPresenterImp;
import com.feiyou.headstyle.ui.activity.MoreTestActivity;
import com.feiyou.headstyle.ui.activity.StarListActivity;
import com.feiyou.headstyle.ui.activity.TestActivity;
import com.feiyou.headstyle.ui.activity.TestCategoryActivity;
import com.feiyou.headstyle.ui.activity.TestDetailActivity;
import com.feiyou.headstyle.ui.activity.TestImageDetailActivity;
import com.feiyou.headstyle.ui.adapter.TestInfoAdapter;
import com.feiyou.headstyle.ui.base.BaseFragment;
import com.feiyou.headstyle.ui.custom.LoginDialog;
import com.feiyou.headstyle.ui.custom.NormalDecoration;
import com.feiyou.headstyle.view.TestInfoView;
import com.orhanobut.logger.Logger;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by myflying on 2019/2/20.
 */
public class TestFragment extends BaseFragment implements TestInfoView, View.OnClickListener {

    LinearLayout mSearchWrapperLayout;

    @BindView(R.id.hot_list)
    RecyclerView mHotTestListView;

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

    @Override
    protected View onCreateView() {
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_test, null);
        ButterKnife.bind(this, root);
        initViews();
        return root;
    }

    public void initViews() {
        loginDialog = new LoginDialog(getActivity(), R.style.login_dialog);

        topView = LayoutInflater.from(getActivity()).inflate(R.layout.test_top, null);
        mSearchWrapperLayout = topView.findViewById(R.id.layout_search_wrapper);
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

        LinearLayout.LayoutParams searchParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(48));
        searchParams.setMargins(0, BarUtils.getStatusBarHeight(), 0, 0);
        mSearchWrapperLayout.setLayoutParams(searchParams);

        List<Integer> urls = new ArrayList<>();
        urls.add(R.mipmap.test_banner1);
        urls.add(R.mipmap.test_banner2);
        //设置图片加载器
        mBanner.setImageLoader(new GlideImageLoader()).setImages(urls).start();

        mBanner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
//                Intent intent = new Intent(getActivity(), Collection2Activity.class);
//                intent.putExtra("banner_id", bannerInfos.get(position).getId());
//                startActivity(intent);
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

    public void testCategory(String cid) {
        Intent intent = new Intent(getActivity(), TestCategoryActivity.class);
        intent.putExtra("cid", cid);
        startActivity(intent);
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void dismissProgress() {

    }

    @Override
    public void loadDataSuccess(TestInfoRet tData) {
        Logger.i("test list data --->" + JSON.toJSONString(tData));
        if (tData != null && tData.getCode() == Constants.SUCCESS) {
            testInfoAdapter.setNewData(tData.getData());
            App.testInfoList = tData.getData();
        }
    }

    @Override
    public void loadDataError(Throwable throwable) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_funs:
                testCategory("1");
                break;
            case R.id.layout_iq:
                testCategory("1");
                break;
            case R.id.layout_xinli:
                testCategory("1");
                break;
            case R.id.layout_tuodan:
                testCategory("1");
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
                Intent intent = new Intent(getActivity(), StarListActivity.class);
                startActivity(intent);
                break;
        }
    }
}
