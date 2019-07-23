package com.feiyou.headstyle.ui.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.CacheDiskUtils;
import com.blankj.utilcode.util.CacheDoubleUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.PathUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feiyou.headstyle.App;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.MessageEvent;
import com.feiyou.headstyle.bean.UserInfo;
import com.feiyou.headstyle.bean.UserInfoRet;
import com.feiyou.headstyle.bean.VersionInfo;
import com.feiyou.headstyle.bean.VersionInfoRet;
import com.feiyou.headstyle.common.Constants;
import com.feiyou.headstyle.presenter.VersionPresenterImp;
import com.feiyou.headstyle.ui.adapter.CommonImageAdapter;
import com.feiyou.headstyle.ui.base.BaseFragmentActivity;
import com.feiyou.headstyle.ui.custom.ConfigDialog;
import com.feiyou.headstyle.ui.custom.VersionUpdateDialog;
import com.feiyou.headstyle.utils.GlideCacheUtil;
import com.feiyou.headstyle.view.VersionView;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.orhanobut.logger.Logger;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

/**
 * Created by myflying on 2018/11/23.
 */
public class SettingActivity extends BaseFragmentActivity implements VersionView, ConfigDialog.ConfigListener, VersionUpdateDialog.UpdateListener {

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

    UserInfo userInfo;

    ConfigDialog configDialog;

    VersionPresenterImp versionPresenterImp;

    private ProgressDialog progressDialog = null;

    private VersionInfo versionInfo;

    BaseDownloadTask task;

    private UMShareAPI mShareAPI = null;

    @Override
    protected int getContextViewId() {
        return R.layout.activity_setting;
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    int progress = (Integer) msg.obj;
                    updateDialog.setProgress(progress);
                    break;
                default:
                    break;
            }
        }
    };

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
        mShareAPI = UMShareAPI.get(this);

        configDialog = new ConfigDialog(this, R.style.login_dialog, 1, "确认退出吗?", "请你确认是否退出当前账号，退出后无法获取更多消息哦!");
        configDialog.setConfigListener(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在检测新版本");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (versionInfo != null && versionInfo.getVersionIsChange() == 1) {
                        return true;//不执行父类点击事件
                    }
                    return false;
                }
                return false;
            }
        });

        commonImageAdapter = new CommonImageAdapter(this, null, 48);
        mPhotoListView.setLayoutManager(new GridLayoutManager(this, 3));
        mPhotoListView.setAdapter(commonImageAdapter);
        commonImageAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(SettingActivity.this, PhotoWallActivity.class);
                intent.putExtra("is_my_info", true);
                startActivity(intent);
            }
        });
        mCacheTv.setText(GlideCacheUtil.getInstance().getCacheSize(this) + "");
        versionPresenterImp = new VersionPresenterImp(this, this);
    }

    @Override
    public void onResume() {
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
        updateDialog = new VersionUpdateDialog(this, R.style.login_dialog);
        updateDialog.setUpdateListener(this);
        updateDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (versionInfo != null && versionInfo.getVersionIsChange() == 1) {
                        return true;//不执行父类点击事件
                    }
                    return false;
                }
                return false;
            }
        });
    }

    @OnClick({R.id.layout_no_photo, R.id.layout_photos})
    void photoList() {
        Intent intent = new Intent(SettingActivity.this, PhotoWallActivity.class);
        intent.putExtra("is_my_info", true);
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
        if (progressDialog != null && !progressDialog.isShowing()) {
            progressDialog.show();
        }
        versionPresenterImp.getVersionInfo(com.feiyou.headstyle.utils.AppUtils.getMetaDataValue(this, "UMENG_CHANNEL"));
    }

    @OnClick(R.id.layout_about_us)
    void aboutUs() {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
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
        mShareAPI.deleteOauth(this, SHARE_MEDIA.QQ, null);
        mShareAPI.deleteOauth(this, SHARE_MEDIA.WEIXIN, null);

        App.getApp().setmUserInfo(null);
        App.getApp().setLogin(false);
        //移除存储的对象
        SPUtils.getInstance().remove(Constants.USER_INFO);
        EventBus.getDefault().post(new MessageEvent("login_out"));
        finish();
    }

    @Override
    public void cancel() {
        if (configDialog != null && configDialog.isShowing()) {
            configDialog.dismiss();
        }
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void dismissProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void loadDataSuccess(VersionInfoRet tData) {
        Logger.i("version info ---> " + JSON.toJSONString(tData));

        if (tData != null && tData.getCode() == Constants.SUCCESS) {
            versionInfo = tData.getData();
            if (versionInfo.getVersionCode() > AppUtils.getAppVersionCode()) {
                if (updateDialog != null && !updateDialog.isShowing()) {
                    updateDialog.setVersionCode(tData.getData().getVersionName());
                    updateDialog.setVersionContent(tData.getData().getVersionDesc());
                    updateDialog.setIsForceUpdate(tData.getData().getVersionIsChange());
                    updateDialog.show();
                }
            } else {
                Toasty.normal(this, "已经是最新版本").show();
            }
        }
    }

    @Override
    public void loadDataError(Throwable throwable) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void update() {
//        if (progressDialog != null && !progressDialog.isShowing()) {
//            progressDialog.setMessage("正在更新版本");
//            progressDialog.show();
//        }
        if (versionInfo != null && !StringUtils.isEmpty(versionInfo.getVersionUrl())) {
            downAppFile(versionInfo.getVersionUrl());
        }
    }

    @Override
    public void updateCancel() {

    }


    public void downAppFile(String downUrl) {
        final String filePath = PathUtils.getExternalAppFilesPath() + "/new_app.apk";
        Logger.i("down app path --->" + filePath);

        task = FileDownloader.getImpl().create(downUrl)
                .setPath(filePath)
                .setListener(new FileDownloadListener() {
                    @Override
                    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        //Toasty.normal(SettingActivity.this, "正在更新版本后...").show();
                    }

                    @Override
                    protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
                    }

                    @Override
                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        int progress = (int) ((soFarBytes * 1.0 / totalBytes) * 100);
                        Logger.i("progress--->" + soFarBytes + "---" + totalBytes + "---" + progress);

                        Message message = new Message();
                        message.what = 0;
                        message.obj = progress;
                        mHandler.sendMessage(message);
                    }

                    @Override
                    protected void blockComplete(BaseDownloadTask task) {
                    }

                    @Override
                    protected void retry(final BaseDownloadTask task, final Throwable ex, final int retryingTimes, final int soFarBytes) {
                    }

                    @Override
                    protected void completed(BaseDownloadTask task) {
                        ToastUtils.showLong("下载完成");
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        AppUtils.installApp(filePath);
                        //install(filePath);
                    }

                    @Override
                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                    }

                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {
                    }

                    @Override
                    protected void warn(BaseDownloadTask task) {
                    }
                });

        task.start();
    }

    private void install(String filePath) {

        File apkFile = new File(filePath);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(SettingActivity.this, "com.feiyou.headstyle.fileprovider", apkFile);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
        }
        startActivity(intent);
    }
}
