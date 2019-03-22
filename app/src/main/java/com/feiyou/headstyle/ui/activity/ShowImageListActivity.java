package com.feiyou.headstyle.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.PathUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.ui.adapter.BasePagerAdapter;
import com.feiyou.headstyle.ui.adapter.UrlPagerAdapter;
import com.feiyou.headstyle.ui.base.BaseFragmentActivity;
import com.feiyou.headstyle.ui.custom.GalleryViewPager;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ShowImageListActivity extends BaseFragmentActivity {

    @BindView(R.id.view_pager)
    GalleryViewPager viewPager;

    @BindView(R.id.iv_down)
    ImageView mDownImageView;

    @BindView(R.id.tv_current_img_index)
    TextView mCurrentTextView;

    @BindView(R.id.tv_total_img_count)
    TextView mTotalCountTextView;

    private int currentPosition;

    private List<String> imgUrlList;

    UrlPagerAdapter pagerAdapter;

    private int imageIndex;

    private String filePath;

    @Override
    protected int getContextViewId() {
        return R.layout.activity_show_image_list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadData();
    }

    @OnClick(R.id.iv_down)
    void downImage() {
        if (imgUrlList != null && imgUrlList.size() > 0) {
            Glide.with(this).asBitmap().load(imgUrlList.get(currentPosition)).into(new SimpleTarget<Bitmap>() {
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
        } else {
            ToastUtils.showLong("图片加载错误");
        }
    }

    public void loadData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.getInt("image_index") >= 0) {
                imageIndex = bundle.getInt("image_index");
            }

            if (bundle.getStringArrayList("image_list") != null) {
                imgUrlList = bundle.getStringArrayList("image_list");
                pagerAdapter = new UrlPagerAdapter(this, imgUrlList);
                pagerAdapter.setOnItemChangeListener(new BasePagerAdapter.OnItemChangeListener() {
                    @Override
                    public void onItemChange(int position) {
                        currentPosition = position;
                        mCurrentTextView.setText((currentPosition + 1) + "");
                    }
                });

                viewPager.setOffscreenPageLimit(3);
                viewPager.setAdapter(pagerAdapter);
                viewPager.setCurrentItem(imageIndex);
                mTotalCountTextView.setText("/" + imgUrlList.size());
                mCurrentTextView.setText((imageIndex + 1) + "");
            }
        } else {
            ToastUtils.showLong("图片地址有误，请稍后重试");
        }

        viewPager.setOnItemClickListener(new GalleryViewPager.OnItemClickListener() {
            @Override
            public void onItemClicked(View view, int position) {
                ShowImageListActivity.this.finish();
            }
        });
    }

    // 其次把文件插入到系统图库
    public boolean saveImageToGallery() {
        boolean flag = true;
        try {
            if (!StringUtils.isEmpty(filePath)) {
                MediaStore.Images.Media.insertImage(getContentResolver(),
                        filePath, filePath.substring(filePath.lastIndexOf("/") + 1, filePath.length()), null);

                MediaScannerConnection.scanFile(ShowImageListActivity.this, new String[]{filePath}, null, null);
                // 最后通知图库更新
                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + filePath)));
                ToastUtils.showLong("已保存到图库");
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
        super.onBackPressed();
        popBackStack();
    }

}
