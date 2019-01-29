package com.feiyou.headstyle.ui.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.ui.adapter.BasePagerAdapter;
import com.feiyou.headstyle.ui.adapter.UrlPagerAdapter;
import com.feiyou.headstyle.ui.base.BaseFragmentActivity;
import com.feiyou.headstyle.ui.custom.GalleryViewPager;

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
            //GlideHelper.downLoadImage(ShowImageListActivity.this, imgUrlList.get(currentPosition));
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
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        popBackStack();
    }

}
