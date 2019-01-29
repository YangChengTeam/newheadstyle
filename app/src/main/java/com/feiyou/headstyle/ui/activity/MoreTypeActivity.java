package com.feiyou.headstyle.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.SizeUtils;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.MoreTypeInfoRet;
import com.feiyou.headstyle.common.Constants;
import com.feiyou.headstyle.presenter.HeadListDataPresenterImp;
import com.feiyou.headstyle.presenter.MoreTypeDataPresenterImp;
import com.feiyou.headstyle.ui.adapter.HeadInfoAdapter;
import com.feiyou.headstyle.ui.adapter.MoreTypeAdapter;
import com.feiyou.headstyle.ui.base.BaseFragmentActivity;
import com.feiyou.headstyle.utils.StatusBarUtil;
import com.feiyou.headstyle.view.HeadListDataView;
import com.feiyou.headstyle.view.MoreTypeDataView;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.wang.avi.AVLoadingIndicatorView;

import butterknife.BindView;

/**
 * Created by myflying on 2019/1/11.
 */
public class MoreTypeActivity extends BaseFragmentActivity implements MoreTypeDataView {

    @BindView(R.id.topbar)
    QMUITopBar mTopBar;

    @BindView(R.id.avi)
    AVLoadingIndicatorView avi;

    @BindView(R.id.type_list)
    RecyclerView mTypeListView;

    @BindView(R.id.layout_no_data)
    LinearLayout mNoDataLayout;

    ImageView mBackImageView;

    private int currentPage = 1;

    private int pageSize = 30;

    private MoreTypeDataPresenterImp moreTypeDataPresenterImp;

    private MoreTypeAdapter moreTypeAdapter;

    @Override
    protected int getContextViewId() {
        return R.layout.activity_more_type;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTopBar();
        initData();
    }

    private void initTopBar() {
        mTopBar.setTitle(getResources().getString(R.string.app_name));
        View topSearchView = getLayoutInflater().inflate(R.layout.common_top_back, null);
        topSearchView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(48)));
        mTopBar.setCenterView(topSearchView);

        mBackImageView = topSearchView.findViewById(R.id.iv_back);
        mBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });

        QMUIStatusBarHelper.setStatusBarLightMode(this);
    }

    public void initData() {
        moreTypeDataPresenterImp = new MoreTypeDataPresenterImp(this, this);
        moreTypeAdapter = new MoreTypeAdapter(this, null);
        mTypeListView.setLayoutManager(new LinearLayoutManager(this));
        mTypeListView.setAdapter(moreTypeAdapter);
        avi.show();
        moreTypeDataPresenterImp.getMoreTypeList();
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void dismissProgress() {

    }

    @Override
    public void loadDataSuccess(MoreTypeInfoRet tData) {
        avi.hide();
        if (tData != null) {
            if (tData.getCode() == Constants.SUCCESS) {
                mNoDataLayout.setVisibility(View.GONE);
                moreTypeAdapter.setNewData(tData.getData());
            } else {
                mNoDataLayout.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void loadDataError(Throwable throwable) {

    }

    @Override
    public void onBackPressed() {
        popBackStack();
    }
}
