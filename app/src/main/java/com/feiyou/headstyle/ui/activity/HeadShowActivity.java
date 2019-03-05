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
import com.blankj.utilcode.util.StringUtils;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.HeadInfo;
import com.feiyou.headstyle.bean.HeadInfoRet;
import com.feiyou.headstyle.bean.HomeDataRet;
import com.feiyou.headstyle.bean.ResultInfo;
import com.feiyou.headstyle.bean.VideoInfoRet;
import com.feiyou.headstyle.common.Constants;
import com.feiyou.headstyle.presenter.HeadListDataPresenterImp;
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

    private HeadListDataPresenterImp headListDataPresenterImp;

    private int currentPage = 1;

    private int pageSize = 30;

    private int startPosition;

    private StickerFragment stickerFragment;

    private boolean isFirstLoad = true;

    private String tagId;

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

        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.getInt("jump_page") > 0) {
            currentPage = bundle.getInt("jump_page");
        }
        if (bundle != null && bundle.getInt("jump_position") > 0) {
            startPosition = bundle.getInt("jump_position");
        }

        if (bundle != null && !StringUtils.isEmpty(bundle.getString("tag_id"))) {
            tagId = bundle.getString("tag_id");
        }

        Logger.i("head show page--->" + currentPage + "---head start position--->" + startPosition);

        adapter = new HeadShowItemAdapter(HeadShowActivity.this, null, showShape);
        swipeView.setAdapter(adapter);

        swipeView.setIsNeedSwipe(true);
        swipeView.setFlingListener(this);
        swipeView.setOnItemClickListener(this);

        homeDataPresenterImp = new HomeDataPresenterImp(this, this);
        headListDataPresenterImp = new HeadListDataPresenterImp(this, this);
        if (StringUtils.isEmpty(tagId)) {
            homeDataPresenterImp.getData(currentPage + "", "", "", 1);
        } else {
            headListDataPresenterImp.getDataByTagId(tagId, currentPage, pageSize);
        }
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
        Intent intent = new Intent(HeadShowActivity.this, HeadEditActivity.class);
        intent.putExtra("image_url", adapter.getHeads().get(0).getImgurl());
        startActivity(intent);
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
        Logger.i("total size--->" + adapter.getCount() + "---" + adapter.getHeads().get(0).getId());

        if (adapter.getCount() < 6) {
            currentPage++;

            if (StringUtils.isEmpty(tagId)) {
                homeDataPresenterImp.getData(currentPage + "", "", "", 1);
            } else {
                headListDataPresenterImp.getDataByTagId(tagId, currentPage, pageSize);
            }
        }

        adapter.remove(0);
        //Logger.i(adapter.getHeads().get(0).getImgurl());
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
                if (isFirstLoad) {
                    if (startPosition < ((HomeDataRet) tData).getData().getImagesList().size()) {
                        adapter.addDatas(((HomeDataRet) tData).getData().getImagesList().subList(startPosition, ((HomeDataRet) tData).getData().getImagesList().size() - 1));
                    } else {
                        adapter.addDatas(((HomeDataRet) tData).getData().getImagesList());
                    }
                    isFirstLoad = false;
                } else {
                    adapter.addDatas(((HomeDataRet) tData).getData().getImagesList());
                }

                adapter.notifyDataSetChanged();
            }

            if (tData instanceof HeadInfoRet) {
                if (isFirstLoad) {
                    if (startPosition < ((HeadInfoRet) tData).getData().size()) {
                        adapter.addDatas(((HeadInfoRet) tData).getData().subList(startPosition, ((HeadInfoRet) tData).getData().size() - 1));
                    } else {
                        adapter.addDatas(((HeadInfoRet) tData).getData());
                    }
                    isFirstLoad = false;
                } else {
                    adapter.addDatas(((HeadInfoRet) tData).getData());
                }

                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void loadDataError(Throwable throwable) {

    }
}
