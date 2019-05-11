package com.feiyou.headstyle.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.MoreTypeInfo;
import com.feiyou.headstyle.bean.MoreTypeInfoRet;
import com.feiyou.headstyle.common.Constants;
import com.feiyou.headstyle.presenter.HeadListDataPresenterImp;
import com.feiyou.headstyle.presenter.MoreTypeDataPresenterImp;
import com.feiyou.headstyle.ui.adapter.HeadInfoAdapter;
import com.feiyou.headstyle.ui.adapter.MoreTypeAdapter;
import com.feiyou.headstyle.ui.base.BaseFragmentActivity;
import com.feiyou.headstyle.ui.custom.NormalDecoration;
import com.feiyou.headstyle.ui.custom.OpenDialog;
import com.feiyou.headstyle.utils.StatusBarUtil;
import com.feiyou.headstyle.view.HeadListDataView;
import com.feiyou.headstyle.view.MoreTypeDataView;
import com.orhanobut.logger.Logger;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.wang.avi.AVLoadingIndicatorView;

import butterknife.BindView;

/**
 * Created by myflying on 2019/1/11.
 */
public class MoreTypeActivity extends BaseFragmentActivity implements MoreTypeDataView, OpenDialog.ConfigListener {

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

    OpenDialog openDialog;

    MoreTypeInfo moreTypeInfo;

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
        TextView titleTv = topSearchView.findViewById(R.id.tv_title);
        titleTv.setText("更多头像");

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
        mTypeListView.addItemDecoration(new NormalDecoration(ContextCompat.getColor(this, R.color.line_color), 1));
        mTypeListView.setAdapter(moreTypeAdapter);
        avi.show();
        moreTypeDataPresenterImp.getMoreTypeList();
        moreTypeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                moreTypeInfo = moreTypeAdapter.getData().get(position);

                if (moreTypeInfo.getType() == 4) {
                    if (openDialog != null && !openDialog.isShowing()) {
                        openDialog.setTitle("打开提示");
                        openDialog.setContent("即将打开\"" + moreTypeInfo.getTagsname() + "\"小程序");
                        openDialog.show();
                    }
                } else {
                    Intent intent = new Intent(MoreTypeActivity.this, HeadListActivity.class);
                    intent.putExtra("tag_name", moreTypeAdapter.getData().get(position).getTagsname());
                    intent.putExtra("tag_id", moreTypeAdapter.getData().get(position).getId());
                    startActivity(intent);
                }
            }
        });

        openDialog = new OpenDialog(this, R.style.login_dialog);
        openDialog.setConfigListener(this);

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
    public void openConfig() {
        String appId = "wxd1112ca9a216aeda"; // 填应用AppId
        IWXAPI api = WXAPIFactory.createWXAPI(this, appId);

        WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
        req.userName = moreTypeInfo.getOriginId(); // 填小程序原始id
        req.path = moreTypeInfo.getJumpPath(); //拉起小程序页面的可带参路径，不填默认拉起小程序首页
        req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE;// 可选打开 开发版，体验版和正式版
        api.sendReq(req);
    }

    @Override
    public void openCancel() {

    }

    @Override
    public void onBackPressed() {
        popBackStack();
    }
}
