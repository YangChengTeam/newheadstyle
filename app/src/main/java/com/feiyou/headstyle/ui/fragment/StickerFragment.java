package com.feiyou.headstyle.ui.fragment;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.ui.adapter.ImageFilterAdapter;
import com.feiyou.headstyle.utils.EffectService;
import com.feiyou.headstyle.utils.FilterEffect;
import com.feiyou.headstyle.utils.GPUImageFilterTools;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.tabs.TabLayout;
import com.orhanobut.logger.Logger;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageView;

/**
 * Created by myflying on 2019/2/18.
 */
public class StickerFragment extends BottomSheetDialogFragment {

    //滤镜图片
    GPUImageView mGPUImageView;

    //绘图区域
    ViewGroup drawArea;

    RecyclerView recyclerView;

    TabLayout mTabLayout;

    private BottomSheetBehavior mBehavior;

    //当前图片
    private Bitmap currentBitmap;

    private List<FilterEffect> effects;

    private ImageFilterAdapter imageFilterAdapter;

    private List<Integer> filterDatas;

    // 构造方法
    public static StickerFragment newInstance(String imgUrl) {
        Bundle args = new Bundle();
        args.putString("img_url", imgUrl);
        StickerFragment fragment = new StickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View stickerView = View.inflate(getContext(), R.layout.fragment_sticker, null);

        mGPUImageView = stickerView.findViewById(R.id.gpuimage);
        drawArea = stickerView.findViewById(R.id.drawing_view_container);
        //recyclerView = stickerView.findViewById(R.id.recycler_view);
        mTabLayout = stickerView.findViewById(R.id.tab_layout);

        stickerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ScreenUtils.getScreenHeight()));
        dialog.setContentView(stickerView);
        initData();
        mBehavior = BottomSheetBehavior.from((View) stickerView.getParent());
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        //默认全屏展开
        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        //setPeekHeight,设置弹出窗口的高度为全屏的状态.
        mBehavior.setPeekHeight(ScreenUtils.getScreenHeight() - BarUtils.getStatusBarHeight());
    }

    public void initData() {

        Bundle bundle = getArguments();
        Logger.i("img_url--->" + bundle.getString("img_url"));

        mTabLayout.addTab(mTabLayout.newTab().setText("滤镜"));
        mTabLayout.addTab(mTabLayout.newTab().setText("贴纸"));
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                ToastUtils.showLong("选择了" + tab.getText());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        filterDatas = new ArrayList<Integer>();
        for (int i = 1; i <= 15; i++) {
            try {
                Field field = R.mipmap.class.getDeclaredField("filter" + i);
                filterDatas.add(Integer.parseInt(field.get(null).toString()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        effects = EffectService.getInst().getLocalFilters();
        imageFilterAdapter = new ImageFilterAdapter(getActivity(), filterDatas, effects);

        currentBitmap = BitmapFactory.decodeResource(this.getContext().getResources(), R.drawable.app_share);
        mGPUImageView.setImage(currentBitmap);

        GPUImageFilter filter = GPUImageFilterTools.createFilterForType(getActivity(), effects.get(0).getType());
        mGPUImageView.setFilter(filter);

        imageFilterAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                GPUImageFilter filter = GPUImageFilterTools.createFilterForType(getActivity(), effects.get(position).getType());
                mGPUImageView.setFilter(filter);
                GPUImageFilterTools.FilterAdjuster mFilterAdjuster = new GPUImageFilterTools.FilterAdjuster(filter);
                //可调节颜色的滤镜
                if (mFilterAdjuster.canAdjust()) {
                    //mFilterAdjuster.adjust(100); 给可调节的滤镜选一个合适的值
                }

            }
        });

//        Glide.with(getActivity())
//                .asBitmap() //指定格式为Bitmap
//                .load(bundle.getString("img_url"))
//                .listener(new RequestListener<Bitmap>() {
//                    @Override
//                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
//                        //加载失败
//                        return false;
//                    }
//                    @Override
//                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
//                        //加载成功，resource为加载到的bitmap
//                        mGPUImageView.setImage(resource);
//                        return false;
//                    }
//                })
//                .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);//加载原图大小

    }

    public void doClick(View v) {
        //点击任意布局关闭
        mBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }
}
