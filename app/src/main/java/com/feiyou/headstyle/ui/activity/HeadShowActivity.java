package com.feiyou.headstyle.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.HeadInfo;
import com.feiyou.headstyle.bean.HomeDataRet;
import com.feiyou.headstyle.bean.ResultInfo;
import com.feiyou.headstyle.common.Constants;
import com.feiyou.headstyle.presenter.HomeDataPresenterImp;
import com.feiyou.headstyle.ui.adapter.HeadShowItemAdapter;
import com.feiyou.headstyle.ui.base.BaseFragmentActivity;
import com.feiyou.headstyle.ui.fragment.StickerFragment;
import com.feiyou.headstyle.view.HeadListDataView;
import com.feiyou.headstyle.view.flingswipe.SwipeFlingAdapterView;
import com.orhanobut.logger.Logger;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by myflying on 2018/11/23.
 */
public class HeadShowActivity extends BaseFragmentActivity implements SwipeFlingAdapterView.onFlingListener,
        SwipeFlingAdapterView.OnItemClickListener, HeadListDataView {

    @BindView(R.id.topbar)
    QMUITopBar mTopBar;

    ImageView mBackImageView;

    @BindView(R.id.layout_share)
    LinearLayout mShareLayout;

    @BindView(R.id.layout_edit)
    LinearLayout mEditLayout;

    @BindView(R.id.layout_keep)
    LinearLayout mAddKeepLayout;

    @BindView(R.id.layout_down)
    LinearLayout mDownLayout;

    @BindView(R.id.swipe_view)
    SwipeFlingAdapterView swipeView;

    TextView mTitleTv;

    TextView mConfigTv;

    private boolean isEdit;

    HeadShowItemAdapter adapter;

    private int showShape = 1; //展示的形状.1,正方形,2圆形

    private HomeDataPresenterImp homeDataPresenterImp;

    private int currentPage = 1;

    private int pageSize = 30;

    private StickerFragment stickerFragment;

    @Override
    protected int getContextViewId() {
        return R.layout.activity_head_show;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTopBar();
        initData();
    }

    private void initTopBar() {
        QMUIStatusBarHelper.setStatusBarLightMode(this);
        mTopBar.setTitle(getResources().getString(R.string.app_name));
        View topSearchView = getLayoutInflater().inflate(R.layout.common_top_config, null);
        topSearchView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(48)));
        mTitleTv = topSearchView.findViewById(R.id.tv_config_title);
        mConfigTv = topSearchView.findViewById(R.id.tv_config);
        mTitleTv.setText("头像预览");
        mConfigTv.setVisibility(View.INVISIBLE);

        mTopBar.setCenterView(topSearchView);
        mBackImageView = topSearchView.findViewById(R.id.iv_back);
        mBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });

        //保存图片
        mConfigTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HeadShowActivity.this, HeadSaveActivity.class);
                startActivity(intent);
            }
        });
    }

    public void initData() {

        List<HeadInfo> temps = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            HeadInfo headInfo = new HeadInfo();
            headInfo.setImgurl("http://img4.duitang.com/uploads/item/201411/08/20141108120621_ziUZK.thumb.700_0.jpeg");
            temps.add(headInfo);
            headInfo = new HeadInfo();
            headInfo.setImgurl("http://cdn.duitang.com/uploads/item/201602/23/20160223124339_d2NkX.jpeg");
            temps.add(headInfo);
            headInfo = new HeadInfo();
            headInfo.setImgurl("http://img5.duitang.com/uploads/item/201409/26/20140926200335_hGkmk.jpeg");
            temps.add(headInfo);
        }

        adapter = new HeadShowItemAdapter(HeadShowActivity.this, null, showShape);
        swipeView.setAdapter(adapter);

        swipeView.setIsNeedSwipe(true);
        swipeView.setFlingListener(this);
        swipeView.setOnItemClickListener(this);

        homeDataPresenterImp = new HomeDataPresenterImp(this, this);
        homeDataPresenterImp.getData(currentPage + "", "", "");

    }

    @OnClick(R.id.btn_square)
    void square() {
        showShape = 1;
        if (adapter != null) {
            adapter.setShowShape(showShape);
        }
    }

    @OnClick(R.id.btn_circle)
    void circle() {
        showShape = 2;
        if (adapter != null) {
            adapter.setShowShape(showShape);
        }
    }

    @OnClick(R.id.layout_edit)
    public void editImage() {
        isEdit = !isEdit;
        if (isEdit) {
            mConfigTv.setVisibility(View.VISIBLE);
            mConfigTv.setText("保存/分享");
        } else {
            mConfigTv.setVisibility(View.GONE);
        }
        if (adapter != null && adapter.getHeads().size() > 0) {
            stickerFragment = StickerFragment.newInstance(adapter.getHeads().get(0).getImgurl());
            stickerFragment.show(getSupportFragmentManager(), "sticker");
        }
    }

    @Override
    public void onBackPressed() {
        popBackStack();
    }

    @Override
    public void onItemClicked(MotionEvent event, View v, Object dataObject) {

    }

    @Override
    public void removeFirstObjectInAdapter() {
        adapter.remove(0);
        Logger.i(adapter.getHeads().get(0).getImgurl());
    }

    @Override
    public void onLeftCardExit(Object dataObject) {

    }

    @Override
    public void onRightCardExit(Object dataObject) {

    }

    @Override
    public void onAdapterAboutToEmpty(int itemsInAdapter) {

    }

    @Override
    public void onScroll(float progress, float scrollXProgress) {

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
            if (tData instanceof HomeDataRet) {
                adapter.addDatas(((HomeDataRet) tData).getData().getImagesList());
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void loadDataError(Throwable throwable) {

    }
}
