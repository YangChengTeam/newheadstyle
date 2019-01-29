package com.feiyou.headstyle.ui.activity;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.bumptech.glide.Glide;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.CollectInfoRet;
import com.feiyou.headstyle.bean.HeadInfo;
import com.feiyou.headstyle.presenter.CollectDataPresenterImp;
import com.feiyou.headstyle.ui.adapter.HeadInfoAdapter;
import com.feiyou.headstyle.ui.adapter.QDRecyclerViewAdapter;
import com.feiyou.headstyle.ui.base.BaseFragmentActivity;
import com.feiyou.headstyle.view.CollectDataView;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUICollapsingTopBarLayout;
import com.qmuiteam.qmui.widget.QMUITopBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by myflying on 2018/11/23.
 */
public class Collection2Activity extends BaseFragmentActivity implements CollectDataView {

    @BindView(R.id.collapsing_topbar_layout)
    QMUICollapsingTopBarLayout mCollapsingTopBarLayout;

    @BindView(R.id.topbar)
    QMUITopBar mTopBar;

    @BindView(R.id.collection_list)
    RecyclerView mCollectionListView;

    @BindView(R.id.iv_collect_img)
    ImageView mCollectImageView;

    @BindView(R.id.tv_collect_name)
    TextView mCollectNameTextView;

    @BindView(R.id.tv_collect_content)
    TextView mCollectContentTextView;

    private CollectDataPresenterImp collectDataPresenterImp;

    HeadInfoAdapter headInfoAdapter;

    private String bannerId;

    private String titleName;

    @Override
    protected int getContextViewId() {
        return R.layout.activity_collection2;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        QMUIStatusBarHelper.translucent(this);
        Bundle bundle = getIntent().getExtras();

        if (bundle != null && bundle.getString("banner_id") != null) {
            bannerId = bundle.getString("banner_id");
        }

        collectDataPresenterImp = new CollectDataPresenterImp(this, this);

        initTopBar();

        headInfoAdapter = new HeadInfoAdapter(this, null);
        mCollectionListView.setLayoutManager(new GridLayoutManager(this, 3));
        mCollectionListView.setAdapter(headInfoAdapter);

        mCollapsingTopBarLayout.setScrimUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Log.i("Collection2", "scrim: " + animation.getAnimatedValue());
                if ((int) animation.getAnimatedValue() > 200) {
                    mCollapsingTopBarLayout.setTitle(StringUtils.isEmpty(titleName) ? "头像合集" : titleName);
                } else {
                    mCollapsingTopBarLayout.setTitle("");
                }
            }
        });

        collectDataPresenterImp.getDataById(bannerId);
    }

    private void initTopBar() {
        mTopBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });

    }

    @Override
    public void showProgress() {

    }

    @Override
    public void dismissProgress() {

    }

    @Override
    public void loadDataSuccess(CollectInfoRet tData) {
        if (tData != null && tData.getData() != null) {
            if (tData.getData().getList() != null && tData.getData().getList().size() > 0) {
                headInfoAdapter.setNewData(tData.getData().getList());
            }

            if (tData.getData().getInfo() != null) {
                Glide.with(this).load(tData.getData().getInfo().getIco()).into(mCollectImageView);
                titleName = tData.getData().getInfo().getName();
                mCollectNameTextView.setText(titleName);
                mCollectContentTextView.setText(tData.getData().getInfo().getDesc());
            }
        }
    }

    @Override
    public void loadDataError(Throwable throwable) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        popBackStack();
    }
}
