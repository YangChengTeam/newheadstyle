package com.feiyou.headstyle.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.PathUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.feiyou.headstyle.App;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.AddCollectionRet;
import com.feiyou.headstyle.bean.HeadInfo;
import com.feiyou.headstyle.bean.HeadInfoRet;
import com.feiyou.headstyle.bean.HomeDataRet;
import com.feiyou.headstyle.bean.MessageEvent;
import com.feiyou.headstyle.bean.ResultInfo;
import com.feiyou.headstyle.bean.UpdateHeadRet;
import com.feiyou.headstyle.bean.UserInfo;
import com.feiyou.headstyle.common.Constants;
import com.feiyou.headstyle.presenter.AddCollectionPresenterImp;
import com.feiyou.headstyle.presenter.HeadListDataPresenterImp;
import com.feiyou.headstyle.presenter.HomeDataPresenterImp;
import com.feiyou.headstyle.presenter.RecordInfoPresenterImp;
import com.feiyou.headstyle.presenter.UpdateHeadPresenterImp;
import com.feiyou.headstyle.ui.adapter.HeadShowItemAdapter;
import com.feiyou.headstyle.ui.base.BaseFragmentActivity;
import com.feiyou.headstyle.ui.custom.GlideRoundTransform;
import com.feiyou.headstyle.ui.custom.LoginDialog;
import com.feiyou.headstyle.ui.custom.qqhead.BaseUIListener;
import com.feiyou.headstyle.ui.fragment.StickerFragment;
import com.feiyou.headstyle.view.HeadListDataView;
import com.feiyou.headstyle.view.flingswipe.SwipeFlingAdapterView;
import com.orhanobut.logger.Logger;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.tencent.connect.avatar.QQAvatar;
import com.tencent.tauth.Tencent;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import lib.kingja.switchbutton.SwitchMultiButton;

/**
 * Created by myflying on 2018/11/23.
 */
public class HeadShowActivity extends BaseFragmentActivity implements SwipeFlingAdapterView.onFlingListener,
        SwipeFlingAdapterView.OnItemClickListener, HeadListDataView, View.OnClickListener {

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

    @BindView(R.id.layout_float_ad)
    FrameLayout mAdLayout;

    @BindView(R.id.float_iv)
    ImageView floatGifImage;

    @BindView(R.id.iv_float_close)
    ImageView mCloseFloat;

    TextView mTitleTv;

    TextView mConfigTv;

    private boolean isEdit;

    HeadShowItemAdapter adapter;

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

    private String imageId = "";

    private String filePath;

    BottomSheetDialog bottomSheetDialog;

    ImageView mCloseImageView;

    private int loginType = 1;

    private int showShape = 1; //展示的形状.1,正方形,2圆形

    private Tencent mTencent;

    UpdateHeadPresenterImp updateHeadPresenterImp;

    private ProgressDialog progressDialog = null;

    private UMShareAPI mShareAPI = null;

    BottomSheetDialog shareDialog;

    private ShareAction shareAction;

    private String keyWord;

    UMImage defUmImage;

    private boolean pageSizeLastClick;

    private boolean isLastImage;

    private HeadInfo lastHeadInfo;

    private RecordInfoPresenterImp recordInfoPresenterImp;

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

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在设置");

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
                if (bottomSheetDialog != null && bottomSheetDialog.isShowing()) {
                    bottomSheetDialog.dismiss();
                }
                if (loginType == 2) {
                    downImage();
                } else {
                    if (App.isLoginAuth) {
                        Glide.with(HeadShowActivity.this).asBitmap().load(currentImageUrl).into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), resource, null, null));
                                doSetAvatar(uri);
                            }
                        });
                    } else {
                        if (progressDialog != null && !progressDialog.isShowing()) {
                            progressDialog.show();
                        }
                        mShareAPI.getPlatformInfo(HeadShowActivity.this, SHARE_MEDIA.QQ, authListener);
                    }
                }
            }
        });
        mSettingAppLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bottomSheetDialog != null && bottomSheetDialog.isShowing()) {
                    bottomSheetDialog.dismiss();
                }
                if (progressDialog != null && !progressDialog.isShowing()) {
                    progressDialog.show();
                }

                Glide.with(HeadShowActivity.this).asBitmap().load(currentImageUrl).into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        //加载成功，resource为加载到的bitmap
                        filePath = PathUtils.getExternalPicturesPath() + File.separator + TimeUtils.getNowMills() + ".jpg";
                        Logger.i("setting my head --->" + filePath);
                        boolean isSave = ImageUtils.save(resource, filePath, Bitmap.CompressFormat.JPEG);
                        if (isSave) {
                            updateHeadPresenterImp.updateHead(userInfo != null ? userInfo.getId() : "", filePath);
                        }
                    }
                });

            }
        });

        defUmImage = new UMImage(this, R.drawable.app_share);

        //初始化分享弹窗
        shareDialog = new BottomSheetDialog(this);
        View shareView = LayoutInflater.from(this).inflate(R.layout.share_dialog_view, null);
        mCloseImageView = shareView.findViewById(R.id.iv_close_share);
        LinearLayout weixinLayout = shareView.findViewById(R.id.layout_weixin);
        LinearLayout circleLayout = shareView.findViewById(R.id.layout_circle);
        LinearLayout qqLayout = shareView.findViewById(R.id.layout_qq_friends);
        LinearLayout qzoneLayout = shareView.findViewById(R.id.layout_qzone);
        weixinLayout.setOnClickListener(this);
        circleLayout.setOnClickListener(this);
        qqLayout.setOnClickListener(this);
        qzoneLayout.setOnClickListener(this);
        mCloseImageView.setOnClickListener(this);
        shareDialog.setContentView(shareView);
    }

    public void initData() {
        mShareAPI = UMShareAPI.get(this);
        mTencent = Tencent.createInstance("1105592461", this.getApplicationContext());

        userInfo = App.getApp().getmUserInfo();

        showShape = SPUtils.getInstance().getInt(Constants.SHOW_SHAPE, 1);
        switchMultiButton.setSelectedTab(showShape == 1 ? 0 : 1);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null && bundle.getInt("from_type") > 0) {
            fromType = bundle.getInt("from_type");
        }

        if (bundle != null && bundle.getInt("jump_page") > 0) {
            currentPage = bundle.getInt("jump_page");
        }
        if (bundle != null && bundle.getInt("jump_position") > 0) {
            startPosition = bundle.getInt("jump_position");
        }

        if (bundle != null && !StringUtils.isEmpty(bundle.getString("tag_id"))) {
            tagId = bundle.getString("tag_id");
        }

        if (bundle != null && !StringUtils.isEmpty(bundle.getString("key_word"))) {
            keyWord = bundle.getString("key_word");
        }

        if (fromType == 2 && bundle != null && !StringUtils.isEmpty(bundle.getString("collection_list"))) {
            String temp = bundle.getString("collection_list");
            collectionList = JSON.parseObject(temp, new TypeReference<List<HeadInfo>>() {
            });
        }

        if (App.getApp().getSuspendInfo() != null && !StringUtils.isEmpty(App.getApp().getSuspendInfo().getId()) && App.getApp().isShowFloatAd()) {
            mAdLayout.setVisibility(View.VISIBLE);
            Glide.with(this).load(App.getApp().getSuspendInfo().getIco()).into(floatGifImage);
        } else {
            mAdLayout.setVisibility(View.GONE);
        }

        Logger.i("head show page--->" + currentPage + "---head start position--->" + startPosition);

        isCollection = ContextCompat.getDrawable(this, R.mipmap.is_keep);
        notCollection = ContextCompat.getDrawable(this, R.mipmap.add_keep);

        if (shareAction == null) {
            shareAction = new ShareAction(this);
            shareAction.setCallback(shareListener);//回调监听器
        }

        loginDialog = new LoginDialog(this, R.style.login_dialog);

        adapter = new HeadShowItemAdapter(HeadShowActivity.this, null, showShape);
        swipeView.setAdapter(adapter);

        swipeView.setIsNeedSwipe(true);
        swipeView.setFlingListener(this);
        swipeView.setOnItemClickListener(this);

        updateHeadPresenterImp = new UpdateHeadPresenterImp(this, this);
        homeDataPresenterImp = new HomeDataPresenterImp(this, this);
        headListDataPresenterImp = new HeadListDataPresenterImp(this, this);
        addCollectionPresenterImp = new AddCollectionPresenterImp(this, this);
        recordInfoPresenterImp = new RecordInfoPresenterImp(this, this);

        if (fromType == 1) {
            if (StringUtils.isEmpty(tagId)) {
                homeDataPresenterImp.getData(App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "", currentPage + "", "", "", 1);
            } else {
                headListDataPresenterImp.getDataByTagId(App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "", tagId, currentPage, pageSize);
            }
        }

        if (fromType == 2) {
            if (startPosition == collectionList.size() - 1) {
                pageSizeLastClick = true;
                isLastImage = true;
                lastHeadInfo = collectionList.get(0);
            }

            if (startPosition < collectionList.size()) {
                adapter.addDatas(collectionList.subList(startPosition, collectionList.size()));
            } else {
                adapter.addDatas(collectionList);
            }
            currentImageUrl = adapter.getHeads().get(0).getImgurl();
            imageId = adapter.getHeads().get(0).getId();
        }

        if (fromType == 3) {
            headListDataPresenterImp.userCollection(currentPage, App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "");
        }

        if (fromType == 4) {
            headListDataPresenterImp.getSearchList(currentPage, StringUtils.isEmpty(keyWord) ? "" : keyWord, "");
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

    @OnClick(R.id.float_iv)
    void floatAd() {
        String aid = App.getApp().getSuspendInfo() != null ? App.getApp().getSuspendInfo().getId() : "";
        recordInfoPresenterImp.adClickInfo(App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "", aid);

        Intent intent = new Intent(this, AdActivity.class);
        intent.putExtra("ad_title", App.getApp().getSuspendInfo() != null ? App.getApp().getSuspendInfo().getName() : "精选推荐");
        intent.putExtra("open_url", App.getApp().getSuspendInfo() != null ? App.getApp().getSuspendInfo().getJumpPath() : "http://gx.qqtn.com");
        startActivity(intent);

    }

    @OnClick(R.id.iv_float_close)
    void closeFloat() {
        mAdLayout.setVisibility(View.GONE);
        App.getApp().setShowFloatAd(false);
    }

    public void addHeadRecord() {
        recordInfoPresenterImp.headSetInfo(App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "", imageId);
    }

    void square() {
        showShape = 1;
        SPUtils.getInstance().put(Constants.SHOW_SHAPE, 1);
        if (adapter != null) {
            adapter.setShowShape(showShape);
        }
        adapter.notifyDataSetChanged();
        try {
            ImageView currentIv = swipeView.getSelectedView().findViewById(R.id.iv_show_head);
            RequestOptions options = new RequestOptions().skipMemoryCache(true);
            Glide.with(this).load(currentImageUrl).apply(options).into(currentIv);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void circle() {
        showShape = 2;
        SPUtils.getInstance().put(Constants.SHOW_SHAPE, 2);
        if (adapter != null) {
            adapter.setShowShape(showShape);
        }
        adapter.notifyDataSetChanged();

        ImageView currentIv = swipeView.getSelectedView().findViewById(R.id.iv_show_head);
        RequestOptions options = new RequestOptions().skipMemoryCache(true);
        options.transform(new GlideRoundTransform(this, SizeUtils.dp2px(117)));
        Glide.with(this).load(currentImageUrl).apply(options).into(currentIv);
    }

    @OnClick(R.id.layout_share)
    public void share() {
        if (shareDialog != null && !shareDialog.isShowing()) {
            shareDialog.show();
        }
    }

    @OnClick(R.id.layout_edit)
    public void editImage() {
        isEdit = !isEdit;

        if (adapter.getHeads() != null && adapter.getHeads().size() > 0) {
            Intent intent = new Intent(HeadShowActivity.this, HeadEditActivity.class);
            intent.putExtra("image_url", adapter.getHeads().get(0).getImgurl());
            startActivity(intent);
        } else {
            Toasty.normal(this, "图片加载错误，请重试").show();
        }
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
                Toasty.normal(this, "系统错误，请重试").show();
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
        if (!App.getApp().isLogin) {
            if (loginDialog != null && !loginDialog.isShowing()) {
                loginDialog.show();
            }
            return;
        } else {
            if (bottomSheetDialog != null && !bottomSheetDialog.isShowing()) {
                bottomSheetDialog.show();
            }
        }
    }

    // 其次把文件插入到系统图库
    public boolean saveImageToGallery() {
        boolean flag = true;
        try {
            if (!StringUtils.isEmpty(filePath)) {
//                MediaStore.Images.Media.insertImage(getContentResolver(),
//                        filePath, filePath.substring(filePath.lastIndexOf("/") + 1, filePath.length()), null);

                //MediaScannerConnection.scanFile(HeadShowActivity.this, new String[]{filePath}, null, null);
                // 最后通知图库更新
                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + filePath)));
                Toasty.normal(this, loginType == 2 && bottomSheetDialog.isShowing() ? "已保存到相册，打开微信更换头像" : "已保存到相册").show();
            } else {
                flag = false;
            }
        } catch (Exception e) {
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
        if (isLastImage) {
            adapter.addItemData(lastHeadInfo);
            adapter.notifyDataSetChanged();
            Toasty.normal(this, "已经是最后一张了").show();
            return;
        }
        if (fromType != 2) {
            if (adapter.getCount() < 6) {
                currentPage++;
                if (fromType == 1) {
                    if (StringUtils.isEmpty(tagId)) {
                        homeDataPresenterImp.getData(App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "", currentPage + "", "", "", 1);
                    } else {
                        headListDataPresenterImp.getDataByTagId(App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "", tagId, currentPage, pageSize);
                    }
                }

                if (fromType == 3) {
                    headListDataPresenterImp.userCollection(currentPage, App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "");
                }

                if (fromType == 4) {
                    headListDataPresenterImp.getSearchList(currentPage, StringUtils.isEmpty(keyWord) ? "" : keyWord, "");
                }
            }

            //滑动时,移除最上面一个图片
            adapter.remove(0);

            Logger.i("last count--->" + adapter.getCount());

            if (adapter.getCount() == 1 && fromType > 1) {
                isLastImage = true;
                lastHeadInfo = adapter.getHeads().get(0);
                Logger.i("已经是最后一张了");
            }

            if (adapter.getHeads().size() > 0) {

                currentImageUrl = adapter.getHeads() != null && adapter.getHeads().size() > 0 ? adapter.getHeads().get(0).getImgurl() : "";
                imageId = adapter.getHeads().get(0).getId();
                if (shareAction != null) {
                    if (!StringUtils.isEmpty(currentImageUrl)) {
                        UMImage image = new UMImage(HeadShowActivity.this, currentImageUrl);
                        shareAction.withMedia(image);
                    } else {
                        shareAction.withMedia(defUmImage);
                    }
                }

                if (adapter.getHeads().get(0).getIsCollect() == 0) {
                    mKeepTextView.setCompoundDrawablesWithIntrinsicBounds(null, notCollection, null, null);
                } else {
                    mKeepTextView.setCompoundDrawablesWithIntrinsicBounds(null, isCollection, null, null);
                }
            }

            //我的收藏列表页面，设置所有的图片为收藏
            if (fromType == 3) {
                mKeepTextView.setCompoundDrawablesWithIntrinsicBounds(null, isCollection, null, null);
            }

        } else {
            //滑动时,移除最上面一个图片
            adapter.remove(0);
            Logger.i("last count--->" + adapter.getCount());
            if (adapter.getCount() == 1) {
                isLastImage = true;
                lastHeadInfo = adapter.getHeads().get(0);
                Logger.i("已经是最后一张了");
            }

            if (adapter.getHeads().size() > 0) {
                currentImageUrl = adapter.getHeads().get(0).getImgurl();
                imageId = adapter.getHeads().get(0).getId();
                if (shareAction != null) {
                    if (!StringUtils.isEmpty(currentImageUrl)) {
                        UMImage image = new UMImage(HeadShowActivity.this, currentImageUrl);
                        shareAction.withMedia(image);
                    } else {
                        shareAction.withMedia(defUmImage);
                    }
                }
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
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void loadDataSuccess(ResultInfo tData) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

        if (tData != null && tData.getCode() == Constants.SUCCESS) {
            if (tData instanceof HomeDataRet) {
                if (isFirstLoad) {
                    if (startPosition == ((HomeDataRet) tData).getData().getImagesList().size() - 1) {
                        pageSizeLastClick = true;
                        isLastImage = true;
                        lastHeadInfo = ((HomeDataRet) tData).getData().getImagesList().get(0);
                    }
                    if (startPosition < ((HomeDataRet) tData).getData().getImagesList().size()) {
                        adapter.addDatas(((HomeDataRet) tData).getData().getImagesList().subList(startPosition, ((HomeDataRet) tData).getData().getImagesList().size()));
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
                        imageId = adapter.getHeads().get(0).getId();
                        if (shareAction != null) {
                            if (!StringUtils.isEmpty(currentImageUrl)) {
                                UMImage image = new UMImage(HeadShowActivity.this, currentImageUrl);
                                image.setThumb(image);
                                shareAction.withMedia(image);
                            } else {
                                shareAction.withMedia(defUmImage);
                            }
                        }
                    }

                    if (fromType == 3) {
                        mKeepTextView.setCompoundDrawablesWithIntrinsicBounds(null, isCollection, null, null);
                    }

                } else {
                    if (pageSizeLastClick && adapter.getCount() > 0) {
                        pageSizeLastClick = false;
                        adapter.remove(0);
                    }

                    if (fromType == 4) {
                        if (startPosition == ((HomeDataRet) tData).getData().getImagesList().size() - 1) {
                            pageSizeLastClick = true;
                            isLastImage = true;
                            lastHeadInfo = ((HomeDataRet) tData).getData().getImagesList().get(0);
                        }
                    }

                    adapter.addDatas(((HomeDataRet) tData).getData().getImagesList());
                }

                adapter.notifyDataSetChanged();
            }

            if (tData instanceof HeadInfoRet) {
                if (isFirstLoad) {
                    if (startPosition == ((HeadInfoRet) tData).getData().size() - 1) {
                        pageSizeLastClick = true;
                        isLastImage = true;
                        lastHeadInfo = ((HeadInfoRet) tData).getData().get(0);
                    }

                    if (startPosition < ((HeadInfoRet) tData).getData().size()) {
                        adapter.addDatas(((HeadInfoRet) tData).getData().subList(startPosition, ((HeadInfoRet) tData).getData().size()));
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

                        currentImageUrl = adapter.getHeads().get(0).getImgurl();
                        imageId = adapter.getHeads().get(0).getId();
                        if (shareAction != null) {
                            if (!StringUtils.isEmpty(currentImageUrl)) {
                                UMImage image = new UMImage(HeadShowActivity.this, currentImageUrl);
                                shareAction.withMedia(image);
                            } else {
                                shareAction.withMedia(defUmImage);
                            }
                        }
                    }

                    if (fromType == 3) {
                        mKeepTextView.setCompoundDrawablesWithIntrinsicBounds(null, isCollection, null, null);
                    }
                } else {
                    if (pageSizeLastClick && adapter.getCount() > 0) {
                        pageSizeLastClick = false;
                        adapter.remove(0);
                    }

                    if (fromType == 4) {
                        if (startPosition == ((HeadInfoRet) tData).getData().size() - 1) {
                            pageSizeLastClick = true;
                            isLastImage = true;
                            lastHeadInfo = ((HeadInfoRet) tData).getData().get(0);
                        }
                    }
                    adapter.addDatas(((HeadInfoRet) tData).getData());
                }
                adapter.notifyDataSetChanged();
            }

            if (tData instanceof AddCollectionRet) {
                if (((AddCollectionRet) tData).getData().getIsCollect() == 0) {
                    Toasty.normal(this, "取消收藏").show();
                    mKeepTextView.setCompoundDrawablesWithIntrinsicBounds(null, notCollection, null, null);
                } else {
                    Toasty.normal(this, "收藏成功").show();
                    mKeepTextView.setCompoundDrawablesWithIntrinsicBounds(null, isCollection, null, null);

                    //收藏成功，可以弹出打分
                    SPUtils.getInstance().put(Constants.IS_OPEN_SCORE, true);
                }
            }

            if (tData instanceof UpdateHeadRet) {
                if (!StringUtils.isEmpty(((UpdateHeadRet) tData).getData().getImage())) {
                    Toasty.normal(this, "设置成功").show();
                    userInfo.setUserimg(((UpdateHeadRet) tData).getData().getImage());

                    //头像设置完成，可以弹出打分
                    SPUtils.getInstance().put(Constants.IS_OPEN_SCORE, true);
                    addHeadRecord();

                    App.getApp().setmUserInfo(userInfo);
                    App.getApp().setLogin(true);
                    SPUtils.getInstance().put(Constants.USER_INFO, JSONObject.toJSONString(userInfo));
                }
            }

        } else {
            if (tData instanceof AddCollectionRet) {
                Toasty.normal(this, tData.getMsg() != null ? tData.getMsg() : "操作失败").show();
            }

            if (tData instanceof UpdateHeadRet) {
                Toasty.normal(this, "设置失败").show();
            }
        }
    }

    @Override
    public void loadDataError(Throwable throwable) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }


    private void doSetAvatar(Uri uri) {
        QQAvatar qqAvatar = new QQAvatar(mTencent.getQQToken());
        qqAvatar.setAvatar(this, uri, new BaseUIListener(this), R.anim.zoomout);
    }

    UMAuthListener authListener = new UMAuthListener() {
        /**
         * @desc 授权开始的回调
         * @param platform 平台名称
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        /**
         * @desc 授权成功的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         * @param data 用户资料返回
         */
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            Logger.i(JSONObject.toJSONString(data));
            //Toast.makeText(mContext, "授权成功了", Toast.LENGTH_LONG).show();
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            App.isLoginAuth = true;
            Glide.with(HeadShowActivity.this).asBitmap().load(currentImageUrl).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), resource, null, null));
                    doSetAvatar(uri);
                }
            });
        }

        /**
         * @desc 授权失败的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            Toast.makeText(HeadShowActivity.this, "授权失败：" + t.getMessage(), Toast.LENGTH_LONG).show();
        }

        /**
         * @desc 授权取消的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         */
        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Toast.makeText(HeadShowActivity.this, "授权取消了", Toast.LENGTH_LONG).show();
        }
    };

    private UMShareListener shareListener = new UMShareListener() {
        /**
         * @descrption 分享开始的回调
         * @param platform 平台类型
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        /**
         * @descrption 分享成功的回调
         * @param platform 平台类型
         */
        @Override
        public void onResult(SHARE_MEDIA platform) {
            dismissShareView();
            //Toast.makeText(HeadShowActivity.this, "分享成功", Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            dismissShareView();
            //Toast.makeText(HeadShowActivity.this, "分享失败", Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            dismissShareView();
            //Toast.makeText(HeadShowActivity.this, "取消分享", Toast.LENGTH_LONG).show();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        //头像设置完成，可以弹出打分
        SPUtils.getInstance().put(Constants.IS_OPEN_SCORE, true);
        addHeadRecord();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_weixin:
                shareAction.setPlatform(SHARE_MEDIA.WEIXIN).share();
                break;
            case R.id.layout_circle:
                shareAction.setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE).share();
                break;
            case R.id.layout_qq_friends:
                shareAction.setPlatform(SHARE_MEDIA.QQ).share();
                break;
            case R.id.layout_qzone:
                shareAction.setPlatform(SHARE_MEDIA.QZONE).share();
                break;
            case R.id.iv_close_share:
                dismissShareView();
                break;
            default:
                break;
        }
    }

    public void dismissShareView() {
        if (shareDialog != null && shareDialog.isShowing()) {
            shareDialog.dismiss();
        }
    }
}
