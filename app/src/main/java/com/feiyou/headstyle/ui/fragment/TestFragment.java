package com.feiyou.headstyle.ui.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.TestInfoRet;
import com.feiyou.headstyle.common.Constants;
import com.feiyou.headstyle.presenter.TestInfoPresenterImp;
import com.feiyou.headstyle.ui.activity.TestCategoryActivity;
import com.feiyou.headstyle.ui.activity.TestDetailActivity;
import com.feiyou.headstyle.ui.adapter.TestInfoAdapter;
import com.feiyou.headstyle.ui.base.BaseFragment;
import com.feiyou.headstyle.view.TestInfoView;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.youth.banner.Banner;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by myflying on 2019/2/20.
 */
public class TestFragment extends BaseFragment implements TestInfoView, View.OnClickListener {

    @BindView(R.id.hot_list)
    RecyclerView mHotTestListView;

    LinearLayout mFunTestLayout;

    private View topView;

    Banner mBanner;

    TestInfoAdapter testInfoAdapter;

    private TestInfoPresenterImp testInfoPresenterImp;

    @Override
    protected View onCreateView() {
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_test, null);
        ButterKnife.bind(this, root);
        QMUIStatusBarHelper.translucent(getActivity());
        initViews();
        return root;
    }

    public void initViews() {
        topView = LayoutInflater.from(getActivity()).inflate(R.layout.test_top, null);
        mBanner = topView.findViewById(R.id.banner);
        mFunTestLayout = topView.findViewById(R.id.layout_funs);
        mFunTestLayout.setOnClickListener(this);


        testInfoPresenterImp = new TestInfoPresenterImp(this, getActivity());
        testInfoAdapter = new TestInfoAdapter(getActivity(), null);
        mHotTestListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mHotTestListView.setAdapter(testInfoAdapter);
        testInfoAdapter.setHeaderView(topView);

        testInfoPresenterImp.getDataList();
    }

    public void funTest() {
        Intent intent = new Intent(getActivity(), TestDetailActivity.class);
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
        if (tData != null && tData.getCode() == Constants.SUCCESS) {
            testInfoAdapter.setNewData(tData.getData());
        }
    }

    @Override
    public void loadDataError(Throwable throwable) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_funs:
                funTest();
                break;
        }
    }
}
