package com.feiyou.headstyle.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.PathUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.feiyou.headstyle.App;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.AddCollectionRet;
import com.feiyou.headstyle.bean.HeadInfo;
import com.feiyou.headstyle.bean.HeadInfoRet;
import com.feiyou.headstyle.bean.HomeDataRet;
import com.feiyou.headstyle.bean.LoginRequest;
import com.feiyou.headstyle.bean.MessageEvent;
import com.feiyou.headstyle.bean.ResultInfo;
import com.feiyou.headstyle.bean.UserInfo;
import com.feiyou.headstyle.bean.VideoInfoRet;
import com.feiyou.headstyle.common.Constants;
import com.feiyou.headstyle.presenter.AddCollectionPresenterImp;
import com.feiyou.headstyle.presenter.HeadListDataPresenterImp;
import com.feiyou.headstyle.presenter.HomeDataPresenterImp;
import com.feiyou.headstyle.ui.adapter.HeadShowItemAdapter;
import com.feiyou.headstyle.ui.base.BaseFragmentActivity;
import com.feiyou.headstyle.ui.custom.GlideRoundTransform;
import com.feiyou.headstyle.ui.custom.LoginDialog;
import com.feiyou.headstyle.ui.fragment.StickerFragment;
import com.feiyou.headstyle.view.HeadListDataView;
import com.feiyou.headstyle.view.flingswipe.SwipeFlingAdapterView;
import com.orhanobut.logger.Logger;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import lib.kingja.switchbutton.SwitchMultiButton;

/**
 * Created by myflying on 2018/11/23.
 */
public class HeadShowActivity extends BaseFragmentActivity implements SwipeFlingAdapterView.onFlingListener,
        SwipeFlingAdapterView.OnItemClickListener, HeadListDataView {

    @BindView(R.id.topbar)
    QMUITopBar mTopBar;

    ImageView mBackImageView;

    @BindView(R.id.switch_type_btn)
    SwitchMultiButton switchMultiButton;

    @BindView(R.id.layout_share)
    LinearLayout mShareLayout;

    @BindView(R.id.layout_edit)
    LinearLayout mEditLayout;

    @BindView(R.id.layout_keep)
    LinearLayout mAddKeepLayout;

    @BindView(R.id.tv_keep)
    TextView mKeepTextView;

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

    private AddCollectionPresenterImp addCollectionPresenterImp;

    private int currentPage = 1;

    private int pageSize = 30;

    private int startPosition;

    private StickerFragment stickerFragment;

    private boolean isFirstLoad = true;

    private String tagId;

    LoginDialog loginDialog;

    private UserInfo userInfo;

    Drawable isCollection;

    Drawable notCollection;

    private int fromType = 1; //1,来自分类及首页,2,来自首页合集

    private List<HeadInfo> collectionList;

    private String currentImageUrl;

    private String filePath;

    BottomSheetDialog bottomSheetDialog;

    ImageView mCloseImageView;

    private int loginType = 1;

    @Override
    protected int getContextViewId() {
        return R.layout.activity_head_show;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTopBar();
        initDialog();
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

    public void initDialog() {
        loginType = App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getLoginType() : 1;

        bottomSheetDialog = new BottomSheetDialog(this);
        View setView = LayoutInflater.from(this).inflate(R.layout.set_head_dialog, null);
        setView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(210)));
        mCloseImageView = setView.findViewById(R.id.iv_close_setting);

        LinearLayout mSettingTypeLayout = setView.findViewById(R.id.layout_setting_type);
        LinearLayout mSettingAppLayout = setView.findViewById(R.id.layout_set_app_head);

        TextView mSettingTv = setView.findViewById(R.id.tv_setting_type_name);

        if (loginType == 2) {
            mSettingTypeLayout.setBackgroundResource(R.drawable.setting_weixin_bg);
            mSettingTv.setText("设为微信头像");
        } else {
            mSettingTypeLayout.setBackgroundResource(R.drawable.setting_qq_bg);
            mSettingTv.setText("设为QQ头像");
        }

        bottomSheetDialog.setContentView(setView);
        mCloseImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bottomSheetDialog != null && bottomSheetDialog.isShowing()) {
                    bottomSheetDialog.dismiss();
                }
            }
        });

        mSettingTypeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (loginType == 2) {
                    downImage();
                    if (bottomSheetDialog != null && bottomSheetDialog.isShowing()) {
                        bottomSheetDialog.dismiss();
                    }
                } else {

                }
            }
        });
        mSettingAppLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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

        if (bundle != null && bundle.getInt("from_type") > 0) {
            fromType = bundle.getInt("from_type");
        }

        if (fromType == 2 && bundle != null && !StringUtils.isEmpty(bundle.getString("collection_list"))) {
            String temp = bundle.getString("collection_list");
            collectionList = JSON.parseObject(temp, new TypeReference<List<HeadInfo>>() {
            });
        }

        Logger.i("head show page--->" + currentPage + "---head start position--->" + startPosition);

        isCollection = ContextCompat.getDrawable(this, R.mipmap.is_keep);
        notCollection = ContextCompat.getDrawable(this, R.mipmap.add_keep);

        loginDialog = new LoginDialog(this, R.style.login_dialog);

        adapter = new HeadShowItemAdapter(HeadShowActivity.this, null, showShape);
        swipeView.setAdapter(adapter);

        swipeView.setIsNeedSwipe(true);
        swipeView.setFlingListener(this);
        swipeView.setOnItemClickListener(this);

        homeDataPresenterImp = new HomeDataPresenterImp(this, this);
        headListDataPresenterImp = new HeadListDataPresenterImp(this, this);
        addCollectionPresenterImp = new AddCollectionPresenterImp(this, this);

        if (fromType == 1) {
            if (StringUtils.isEmpty(tagId)) {
                homeDataPresenterImp.getData(App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "", currentPage + "", "", "", 1);
            } else {
                headListDataPresenterImp.getDataByTagId(App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "", tagId, currentPage, pageSize);
            }
        } else {
            if (startPosition < collectionList.size()) {
                adapter.addDatas(collectionList.subList(startPosition, collectionList.size() - 1));
            } else {
                adapter.addDatas(collectionList);
            }
        }

        switchMultiButton.setOnSwitchListener(new SwitchMultiButton.OnSwitchListener() {
            @Override
            public void onSwitch(int position, String tabText) {
                if (position == 0) {
                    square();
                } else {
                    circle();
                }
            }
        });
    }

    void square() {
        showShape = 1;
        if (adapter != null) {
            adapter.setShowShape(showShape);
        }
    }

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

    @OnClick(R.id.layout_keep)
    public void addCollection() {
        if (!App.getApp().isLogin) {
            if (loginDialog != null && !loginDialog.isShowing()) {
                loginDialog.show();
            }
        } else {
            if (adapter.getHeads().size() > 0) {
                addCollectionPresenterImp.addCollection(App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "", adapter.getHeads().get(0).getId());
            } else {
                ToastUtils.showLong("系统错误，请重试");
            }
        }
    }

    @OnClick(R.id.layout_down)
    void downImage() {
        Glide.with(this).asBitmap().load(currentImageUrl).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                //加载成功，resource为加载到的bitmap
                filePath = PathUtils.getExternalPicturesPath() + File.separator + TimeUtils.getNowMills() + ".jpg";
                Logger.i("show page save path --->" + filePath);
                boolean isSave = ImageUtils.save(resource, filePath, Bitmap.CompressFormat.JPEG);
                if (isSave) {
                    saveImageToGallery();
                }
            }
        });

    }

    @OnClick(R.id.layout_setting_head)
    void setting() {
        if (bottomSheetDialog != null && !bottomSheetDialog.isShowing()) {
            bottomSheetDialog.show();
        }
    }

    // 其次把文件插入到系统图库
    public boolean saveImageToGallery() {
        boolean flag = true;
        try {
            if (!StringUtils.isEmpty(filePath)) {
                MediaStore.Images.Media.insertImage(getContentResolver(),
                        filePath, filePath.substring(filePath.lastIndexOf("/") + 1, filePath.length()), null);

                MediaScannerConnection.scanFile(HeadShowActivity.this, new String[]{filePath}, null, null);
                // 最后通知图库更新
                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + filePath)));
                ToastUtils.showLong(loginType == 2 && bottomSheetDialog.isShowing() ? "已保存，请在微信中修改" : "已保存到图库");
            } else {
                flag = false;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            flag = false;
        }
        return flag;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(MessageEvent messageEvent) {
        if (messageEvent.getMessage().equals("login_success")) {
            if (!StringUtils.isEmpty(SPUtils.getInstance().getString(Constants.USER_INFO))) {
                Logger.i(SPUtils.getInstance().getString(Constants.USER_INFO));
                userInfo = JSON.parseObject(SPUtils.getInstance().getString(Constants.USER_INFO), new TypeReference<UserInfo>() {
                });
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
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
        if (fromType == 1) {
            if (adapter.getCount() < 6) {
                currentPage++;
                if (StringUtils.isEmpty(tagId)) {
                    homeDataPresenterImp.getData(App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "", currentPage + "", "", "", 1);
                } else {
                    headListDataPresenterImp.getDataByTagId(App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "", tagId, currentPage, pageSize);
                }
            }

            if (adapter.getCount() <= 1) {
                ToastUtils.showLong("已经是最后一张了");
            } else {
                //滑动时,移除最上面一个图片
                adapter.remove(0);

                currentImageUrl = adapter.getHeads().get(0).getImgurl();
            }

            if (adapter.getHeads().size() > 0) {
                if (adapter.getHeads().get(0).getIsCollect() == 0) {
                    mKeepTextView.setCompoundDrawablesWithIntrinsicBounds(null, notCollection, null, null);
                } else {
                    mKeepTextView.setCompoundDrawablesWithIntrinsicBounds(null, isCollection, null, null);
                }
            }
        } else {
            if (adapter.getCount() <= 1) {
                ToastUtils.showLong("已经是最后一张了");
            } else {
                //滑动时,移除最上面一个图片
                adapter.remove(0);

                currentImageUrl = adapter.getHeads().get(0).getImgurl();
            }
        }
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

                    if (adapter.getHeads() != null && adapter.getHeads().size() > 0) {
                        if (adapter.getHeads().get(0).getIsCollect() == 0) {
                            mKeepTextView.setCompoundDrawablesWithIntrinsicBounds(null, notCollection, null, null);
                        } else {
                            mKeepTextView.setCompoundDrawablesWithIntrinsicBounds(null, isCollection, null, null);
                        }
                        currentImageUrl = adapter.getHeads().get(0).getImgurl();
                    }

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

                    if (adapter.getHeads() != null && adapter.getHeads().size() > 0) {
                        if (adapter.getHeads().get(0).getIsCollect() == 0) {
                            mKeepTextView.setCompoundDrawablesWithIntrinsicBounds(null, notCollection, null, null);
                        } else {
                            mKeepTextView.setCompoundDrawablesWithIntrinsicBounds(null, isCollection, null, null);
                        }
                    }

                } else {
                    adapter.addDatas(((HeadInfoRet) tData).getData());
                }
                adapter.notifyDataSetChanged();
            }

            if (tData instanceof AddCollectionRet) {
                if (((AddCollectionRet) tData).getData().getIsCollect() == 0) {
                    ToastUtils.showLong("已取消");
                    mKeepTextView.setCompoundDrawablesWithIntrinsicBounds(null, notCollection, null, null);
                } else {
                    ToastUtils.showLong("已收藏");
                    mKeepTextView.setCompoundDrawablesWithIntrinsicBounds(null, isCollection, null, null);
                }
            }
        } else {
            if (tData instanceof AddCollectionRet) {
                ToastUtils.showLong(tData.getMsg() != null ? tData.getMsg() : "操作失败");
            }
        }
    }

    @Override
    public void loadDataError(Throwable throwable) {

    }
}
