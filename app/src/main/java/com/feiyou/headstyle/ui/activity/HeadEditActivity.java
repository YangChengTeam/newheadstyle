package com.feiyou.headstyle.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.PathUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.base.Addon;
import com.feiyou.headstyle.bean.StickerInfo;
import com.feiyou.headstyle.bean.StickerInfoRet;
import com.feiyou.headstyle.bean.StickerTypeInfo;
import com.feiyou.headstyle.common.Constants;
import com.feiyou.headstyle.presenter.StickerPresenterImp;
import com.feiyou.headstyle.ui.adapter.ImageFilterAdapter;
import com.feiyou.headstyle.ui.adapter.StickerAdapter;
import com.feiyou.headstyle.ui.adapter.StickerTypeAdapter;
import com.feiyou.headstyle.ui.base.BaseFragmentActivity;
import com.feiyou.headstyle.utils.EffectService;
import com.feiyou.headstyle.utils.EffectUtil;
import com.feiyou.headstyle.utils.FilterEffect;
import com.feiyou.headstyle.utils.GPUImageFilterTools;
import com.feiyou.headstyle.utils.MyImageViewDrawableOverlay;
import com.feiyou.headstyle.view.StickerDataView;
import com.orhanobut.logger.Logger;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageView;

/**
 * Created by myflying on 2019/2/19.
 */
public class HeadEditActivity extends BaseFragmentActivity implements StickerDataView {

    @BindView(R.id.topbar)
    QMUITopBar mTopBar;

    ImageView mBackImageView;

    TextView mTitleTv;

    TextView mConfigTv;

    //滤镜图片
    @BindView(R.id.gpuimage)
    GPUImageView mGPUImageView;

    //绘图区域
    @BindView(R.id.drawing_view_container)
    ViewGroup drawArea;

    @BindView(R.id.layout_sticker)
    LinearLayout mStickerLayout;

    @BindView(R.id.layout_filter)
    LinearLayout mFilterLayout;

    @BindView(R.id.tabs_layout)
    TabLayout tabLayout;

    @BindView(R.id.sticker_type_list)
    RecyclerView stickerTypeListView;

    @BindView(R.id.sticker_list_view)
    RecyclerView stickerListView;

    @BindView(R.id.filter_list_view)
    RecyclerView filterListView;

    private MyImageViewDrawableOverlay mImageView;

    //当前选择底部按钮
    private TextView currentBtn;
    //当前图片
    private Bitmap currentBitmap;
    //用于预览的小图片
    private Bitmap smallImageBackgroud;

    private int[] tab_images = new int[]{R.mipmap.sticker_icon, R.mipmap.filter_icon};

    private String[] tab_texts = new String[]{"贴纸", "滤镜"};

    private List<Object> mDefaultDatas;

    private List<Integer> filterDatas;

    private List<FilterEffect> effects;

    StickerTypeAdapter stickerTypeAdapter;

    private StickerAdapter stickerAdapter;

    private ImageFilterAdapter imageFilterAdapter;

    LinearLayoutManager typeManager;

    GridLayoutManager stickerManager;

    LinearLayoutManager filterManager;

    private String imagePath;

    private StickerPresenterImp stickerPresenterImp;

    private int lastTypeIndex = -1;

    private List<List<StickerInfo>> allStickerList;

    @Override
    protected int getContextViewId() {
        return R.layout.activity_image_edit;
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
        mTitleTv.setText("头像编辑");
        mConfigTv.setText("保存/分享");

        mTopBar.setCenterView(topSearchView);
        mBackImageView = topSearchView.findViewById(R.id.iv_back);
        mBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });

        mConfigTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePicture();
            }
        });
    }

    public void initData() {

        EffectUtil.clear();
        stickerPresenterImp = new StickerPresenterImp(this, this);
        Intent intent = getIntent();

        if (intent.getExtras() != null) {
            imagePath = intent.getExtras().getString("image_url");
        }

        RequestOptions options = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true);

        Glide.with(this).asBitmap().apply(options).load(imagePath).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                currentBitmap = resource;
                smallImageBackgroud = resource;
                mGPUImageView.setImage(currentBitmap);
            }
        });

        //画布高度
        int canvasHeight = (int) (ScreenUtils.getScreenWidth() * 0.8);
        //int canvasHeight = ScreenUtils.getHeight(this) - SizeUtils.dp2px(this, 174) - NavgationBarUtils.getNavigationBarHeight(this);
        //添加贴纸水印的画布
        View overlay = LayoutInflater.from(HeadEditActivity.this).inflate(
                R.layout.view_drawable_overlay, null);
        mImageView = (MyImageViewDrawableOverlay) overlay.findViewById(R.id.drawable_overlay);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(canvasHeight, canvasHeight);
        mImageView.setLayoutParams(params);
        overlay.setLayoutParams(params);
        drawArea.addView(overlay);

        //初始化滤镜图片
        RelativeLayout.LayoutParams bgParams = new RelativeLayout.LayoutParams(canvasHeight, canvasHeight);
        mGPUImageView.setLayoutParams(bgParams);
        mGPUImageView.getGPUImage().setScaleType(GPUImage.ScaleType.CENTER_INSIDE);

        tabLayout.addTab(tabLayout.newTab().setCustomView(getTabView(0)));
        tabLayout.addTab(tabLayout.newTab().setCustomView(getTabView(1)));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    mStickerLayout.setVisibility(View.VISIBLE);
                    mFilterLayout.setVisibility(View.GONE);
                    stickerListView.setAdapter(stickerAdapter);
                }
                if (tab.getPosition() == 1) {
                    mStickerLayout.setVisibility(View.GONE);
                    mFilterLayout.setVisibility(View.VISIBLE);
                    initFilterToolBar();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mDefaultDatas = new ArrayList<Object>();
        mDefaultDatas.add(R.mipmap.s1);
        mDefaultDatas.add(R.mipmap.s2);
        mDefaultDatas.add(R.mipmap.s3);
        mDefaultDatas.add(R.mipmap.s4);
        mDefaultDatas.add(R.mipmap.s5);
        mDefaultDatas.add(R.mipmap.s6);
        mDefaultDatas.add(R.mipmap.s7);
        mDefaultDatas.add(R.mipmap.s8);
        mDefaultDatas.add(R.mipmap.s9);
        mDefaultDatas.add(R.mipmap.s10);
        mDefaultDatas.add(R.mipmap.s11);
        mDefaultDatas.add(R.mipmap.s12);
        mDefaultDatas.add(R.mipmap.s13);
        filterDatas = new ArrayList<Integer>();
        for (int i = 1; i <= 15; i++) {
            try {
                Field field = R.mipmap.class.getDeclaredField("filter" + i);
                filterDatas.add(Integer.parseInt(field.get(null).toString()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //贴纸类型参数
        stickerTypeAdapter = new StickerTypeAdapter(this, null);
        typeManager = new LinearLayoutManager(this);
        stickerTypeListView.setLayoutManager(typeManager);
        stickerTypeListView.setAdapter(stickerTypeAdapter);

        //设置贴纸参数
        stickerManager = new GridLayoutManager(this, 4);
        stickerAdapter = new StickerAdapter(this, null);
        stickerListView.setLayoutManager(stickerManager);
        stickerListView.setAdapter(stickerAdapter);

        //初始化滤镜参数
        effects = EffectService.getInst().getLocalFilters();
        imageFilterAdapter = new ImageFilterAdapter(this, filterDatas, effects);
        filterManager = new LinearLayoutManager(this);
        filterManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        filterListView.setLayoutManager(filterManager);
        filterListView.setAdapter(imageFilterAdapter);

        stickerTypeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (position != lastTypeIndex) {
                    stickerTypeAdapter.getData().get(position).setSelected(true);
                    if (lastTypeIndex > -1) {
                        stickerTypeAdapter.getData().get(lastTypeIndex).setSelected(false);
                    }
                    lastTypeIndex = position;
                    stickerTypeAdapter.notifyDataSetChanged();
                    if (allStickerList != null && allStickerList.size() > lastTypeIndex) {
                        stickerAdapter.setNewData(allStickerList.get(lastTypeIndex));
                    }
                }
            }
        });

        stickerAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                StickerInfo stickerInfo = stickerAdapter.getData().get(position);
                Glide.with(HeadEditActivity.this).asBitmap().load(stickerInfo.getIco()).into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        Addon sticker = new Addon(resource);
                        EffectUtil.addStickerImage(mImageView, HeadEditActivity.this, sticker,
                                new EffectUtil.StickerCallback() {
                                    @Override
                                    public void onRemoveSticker(Addon sticker) {

                                    }
                                });
                    }
                });
            }
        });

        imageFilterAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                GPUImageFilter filter = GPUImageFilterTools.createFilterForType(
                        HeadEditActivity.this, effects.get(position).getType());
                mGPUImageView.setFilter(filter);
                GPUImageFilterTools.FilterAdjuster mFilterAdjuster = new GPUImageFilterTools.FilterAdjuster(filter);
                //可调节颜色的滤镜
                if (mFilterAdjuster.canAdjust()) {
                    //mFilterAdjuster.adjust(100); 给可调节的滤镜选一个合适的值
                }

            }
        });

        stickerPresenterImp.getDataList();
    }

    //初始化滤镜
    private void initFilterToolBar() {
        filterListView.setAdapter(imageFilterAdapter);
    }

    /**
     * 自定义Tab
     *
     * @param position
     * @return
     */
    public View getTabView(int position) {
        View view = LayoutInflater.from(this).inflate(R.layout.tab_item, null);
        TextView tabTitle = view.findViewById(R.id.txt_title);
        tabTitle.setText(tab_texts[position]);
        return view;
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void dismissProgress() {

    }

    @Override
    public void loadDataSuccess(StickerInfoRet tData) {
        if (tData != null && tData.getCode() == Constants.SUCCESS) {

            lastTypeIndex = 0;

            if (tData.getData().getType() != null) {
                List<StickerTypeInfo> tempList = new ArrayList<>();
                for (int i = 0; i < tData.getData().getType().size(); i++) {
                    StickerTypeInfo stickerTypeInfo = new StickerTypeInfo();
                    stickerTypeInfo.setTypeName(tData.getData().getType().get(i));
                    if (i == 0) {
                        stickerTypeInfo.setSelected(true);
                    }
                    tempList.add(stickerTypeInfo);
                }
                stickerTypeAdapter.setNewData(tempList);
            }

            if (tData.getData().getList().size() > 0) {
                allStickerList = tData.getData().getList();
                stickerAdapter.setNewData(allStickerList.get(lastTypeIndex));
            }
        }
    }

    @Override
    public void loadDataError(Throwable throwable) {

    }

    //保存图片
    private void savePicture() {
        Logger.e("w" + mImageView.getWidth() + "--h--->" + mImageView.getHeight());
        //加滤镜
        final Bitmap newBitmap = Bitmap.createBitmap(mImageView.getWidth(), mImageView.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas cv = new Canvas(newBitmap);
        RectF dst = new RectF(0, 0, mImageView.getWidth(), mImageView.getHeight());
        try {
            cv.drawBitmap(mGPUImageView.capture(), null, dst, null);
        } catch (InterruptedException e) {
            e.printStackTrace();
            cv.drawBitmap(currentBitmap, null, dst, null);
        }
        //加贴纸水印
        EffectUtil.applyOnSave(cv, mImageView);

        new SavePicToFileTask().execute(newBitmap);
    }

    private String filePath;

    private class SavePicToFileTask extends AsyncTask<Bitmap, Void, Boolean> {
        Bitmap bitmap;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Bitmap... params) {
            Boolean isSave = false;
            try {
                bitmap = params[0];
                String picName = TimeUtils.getNowMills() + ".jpg";
                filePath = PathUtils.getExternalPicturesPath() + File.separator + picName;
                Logger.i("edit save path --->" + filePath);
                isSave = ImageUtils.save(bitmap, filePath, Bitmap.CompressFormat.JPEG);
            } catch (Exception e) {
                e.printStackTrace();
                ToastUtils.showLong("图片处理错误，请退出相机并重试");
            }
            return isSave;
        }

        @Override
        protected void onPostExecute(Boolean isSave) {
            super.onPostExecute(isSave);

            if (isSave) {
                saveImageToGallery();
            } else {
                ToastUtils.showLong("保存失败!");
            }
        }
    }

    // 其次把文件插入到系统图库
    public boolean saveImageToGallery() {
        boolean flag = true;
        try {
            if (!StringUtils.isEmpty(filePath)) {
                MediaStore.Images.Media.insertImage(getContentResolver(),
                        filePath, filePath.substring(filePath.lastIndexOf("/") + 1, filePath.length()), null);

                MediaScannerConnection.scanFile(HeadEditActivity.this, new String[]{filePath}, null, null);
                // 最后通知图库更新
                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + filePath)));
                //ToastUtils.showLong("保存成功!");

                Intent intent = new Intent(this, HeadSaveActivity.class);
                intent.putExtra("file_path",filePath);
                startActivity(intent);
            } else {
                flag = false;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            flag = false;
        }
        return flag;
    }

    @Override
    public void onBackPressed() {
        popBackStack();
    }
}
