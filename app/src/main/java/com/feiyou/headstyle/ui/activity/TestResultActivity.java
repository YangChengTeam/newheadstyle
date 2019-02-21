package com.feiyou.headstyle.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.TestInfoRet;
import com.feiyou.headstyle.common.Constants;
import com.feiyou.headstyle.presenter.TestInfoPresenterImp;
import com.feiyou.headstyle.ui.adapter.BlackListAdapter;
import com.feiyou.headstyle.ui.adapter.TestInfoAdapter;
import com.feiyou.headstyle.ui.base.BaseFragmentActivity;
import com.feiyou.headstyle.ui.custom.GlideRoundTransform;
import com.feiyou.headstyle.view.TestInfoView;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by myflying on 2018/11/23.
 */
public class TestResultActivity extends BaseFragmentActivity implements TestInfoView {

    @BindView(R.id.topbar)
    QMUITopBar mTopBar;

    ImageView mBackImageView;

    @BindView(R.id.recommend_list)
    RecyclerView mRecommendListView;

    @BindView(R.id.iv_test_result)
    ImageView mTestResultImageView;

    @BindView(R.id.btn_share)
    Button mShareButton;

    @BindView(R.id.btn_save)
    Button mSaveButton;

    @BindView(R.id.btn_test_again)
    Button mTestAgainButton;

    private String imageUrl;

    private String noCodeImageUrl;

    private TestInfoPresenterImp testInfoPresenterImp;

    private TestInfoAdapter testInfoAdapter;

    @Override
    protected int getContextViewId() {
        return R.layout.activity_test_result;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTopBar();
        initData();
    }

    private void initTopBar() {
        QMUIStatusBarHelper.setStatusBarLightMode(this);

        View topSearchView = getLayoutInflater().inflate(R.layout.common_top_back, null);
        topSearchView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(48)));
        TextView titleTv = topSearchView.findViewById(R.id.tv_title);
        titleTv.setText("测试结果");

        mTopBar.setCenterView(topSearchView);
        mBackImageView = topSearchView.findViewById(R.id.iv_back);
        mBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });
    }

    public void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.getString("image_url") != null) {
            imageUrl = bundle.getString("image_url");
        }

        if (bundle != null && bundle.getString("nocode_image_url") != null) {
            noCodeImageUrl = bundle.getString("nocode_image_url");
        }

        if (!StringUtils.isEmpty(noCodeImageUrl)) {
            RequestOptions options = new RequestOptions();
            options.transform(new GlideRoundTransform(this, 12));
            Glide.with(this).load(noCodeImageUrl).into(mTestResultImageView);
        }

        testInfoPresenterImp = new TestInfoPresenterImp(this, this);
        testInfoAdapter = new TestInfoAdapter(this, null);
        mRecommendListView.setLayoutManager(new LinearLayoutManager(this));
        mRecommendListView.setAdapter(testInfoAdapter);
        //推荐列表
        testInfoPresenterImp.getHotAndRecommendList(2);
    }

    @OnClick(R.id.btn_share)
    void share() {

    }

    @OnClick(R.id.btn_save)
    void save() {

    }

    @OnClick(R.id.btn_test_again)
    void testAgain() {

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

    }

    @Override
    public void loadDataSuccess(TestInfoRet tData) {
        if (tData != null && tData.getCode() == Constants.SUCCESS) {
            testInfoAdapter.setNewData(tData.getData().getHotList());
        }
    }

    @Override
    public void loadDataError(Throwable throwable) {

    }
}
