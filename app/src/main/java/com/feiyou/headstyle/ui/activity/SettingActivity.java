package com.feiyou.headstyle.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.CacheDiskUtils;
import com.blankj.utilcode.util.CacheDoubleUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feiyou.headstyle.App;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.UserInfo;
import com.feiyou.headstyle.common.Constants;
import com.feiyou.headstyle.ui.adapter.CommonImageAdapter;
import com.feiyou.headstyle.ui.base.BaseFragmentActivity;
import com.feiyou.headstyle.ui.custom.ConfigDialog;
import com.feiyou.headstyle.ui.custom.VersionUpdateDialog;
import com.feiyou.headstyle.utils.GlideCacheUtil;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

/**
 * Created by myflying on 2018/11/23.
 */
public class SettingActivity extends BaseFragmentActivity implements ConfigDialog.ConfigListener {

    @BindView(R.id.topbar)
    QMUITopBar mTopBar;

    ImageView mBackImageView;

    @BindView(R.id.layout_my_info)
    RelativeLayout mMyInfoLayout;

    @BindView(R.id.layout_login_out)
    LinearLayout mLoginOutLayout;

    @BindView(R.id.tv_user_id)
    TextView mUserIdTv;

    @BindView(R.id.tv_user_phone)
    TextView mUserPhoneTv;

    @BindView(R.id.photo_list)
    RecyclerView mPhotoListView;

    @BindView(R.id.layout_wrapper)
    LinearLayout mWrapperLayout;

    @BindView(R.id.layout_no_photo)
    LinearLayout mNoPhotoLayout;

    @BindView(R.id.layout_photos)
    RelativeLayout mPhotosLayout;

    @BindView(R.id.tv_total_count)
    TextView mTotalCountTv;

    @BindView(R.id.layout_clear_cache)
    RelativeLayout mClearCacheLayout;

    @BindView(R.id.tv_cache_count)
    TextView mCacheTv;

    CommonImageAdapter commonImageAdapter;

    private List<Object> photoList;

    VersionUpdateDialog updateDialog;

    private VersionUpdateDialog.UpdateListener listener;

    UserInfo userInfo;

    ConfigDialog configDialog;

    @Override
    protected int getContextViewId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTopBar();
        initData();
        initDialog();
    }

    private void initTopBar() {
        QMUIStatusBarHelper.setStatusBarLightMode(this);
        View topSearchView = getLayoutInflater().inflate(R.layout.common_top_back, null);
        topSearchView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(48)));
        TextView titleTv = topSearchView.findViewById(R.id.tv_title);
        titleTv.setText("设置");

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
        configDialog = new ConfigDialog(this, R.style.login_dialog, 1, "确认退出吗?", "请你确认是否退出当前账号，退出后无法获取更多消息哦!");
        configDialog.setConfigListener(this);

        commonImageAdapter = new CommonImageAdapter(this, null, 48);
        mPhotoListView.setLayoutManager(new GridLayoutManager(this, 3));
        mPhotoListView.setAdapter(commonImageAdapter);
        commonImageAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(SettingActivity.this, PhotoWallActivity.class);
                startActivity(intent);
            }
        });
        mCacheTv.setText(GlideCacheUtil.getInstance().getCacheSize(this) + "");
    }

    @Override
    protected void onResume() {
        super.onResume();

        userInfo = App.getApp().getmUserInfo();
        if (userInfo != null) {
            mUserIdTv.setText(userInfo.getId() + "");
            if (!StringUtils.isEmpty(userInfo.getPhone())) {
                mUserPhoneTv.setText(userInfo.getPhone() + "");
            }

            if (userInfo.getImageWall() != null && userInfo.getImageWall().length > 0) {
                mNoPhotoLayout.setVisibility(View.GONE);
                mPhotosLayout.setVisibility(View.VISIBLE);

                String[] tempPhotos = userInfo.getImageWall();
                mTotalCountTv.setText(tempPhotos.length + "张");
                photoList = new ArrayList<>();
                for (int i = 0; i < tempPhotos.length; i++) {
                    photoList.add(tempPhotos[i]);
                }
                if (photoList.size() > 3) {
                    photoList = photoList.subList(0, 3);
                }

                //获取值后重新设置
                commonImageAdapter.setNewData(photoList);
            } else {
                mNoPhotoLayout.setVisibility(View.VISIBLE);
                mPhotosLayout.setVisibility(View.GONE);
            }
        }
    }

    public void initDialog() {
        listener = new VersionUpdateDialog.UpdateListener() {
            @Override
            public void update(Dialog dialog) {

            }
        };
        updateDialog = new VersionUpdateDialog(this, R.style.login_dialog, listener);
    }

    @OnClick({R.id.layout_no_photo, R.id.layout_photos})
    void photoList() {
        Intent intent = new Intent(SettingActivity.this, PhotoWallActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.layout_bind_phone)
    void bindPhone() {
        Intent intent = new Intent(this, BindPhoneActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.layout_my_info)
    public void myInfo() {
        Intent intent = new Intent(this, EditUserInfoActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.layout_update_version)
    void updateVersion() {
        if (updateDialog != null && !updateDialog.isShowing()) {
            updateDialog.show();
        }
    }

    @OnClick(R.id.layout_login_out)
    void loginOut() {
        if (configDialog != null && !configDialog.isShowing()) {
            configDialog.show();
        }
    }

    @OnClick(R.id.layout_clear_cache)
    void clearCache() {
        GlideCacheUtil.getInstance().clearImageDiskCache(this);
        mCacheTv.setText("");
        Toasty.normal(this, "缓存已清除").show();
    }

    @Override
    public void onBackPressed() {
        popBackStack();
    }

    @Override
    public void config() {
        App.getApp().setmUserInfo(null);
        App.getApp().setLogin(false);
        //移除存储的对象
        SPUtils.getInstance().remove(Constants.USER_INFO);
        finish();
    }

    @Override
    public void cancel() {
        if (configDialog != null && configDialog.isShowing()) {
            configDialog.dismiss();
        }
    }
}
