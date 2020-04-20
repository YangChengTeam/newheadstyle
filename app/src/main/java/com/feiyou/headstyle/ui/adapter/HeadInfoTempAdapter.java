package com.feiyou.headstyle.ui.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.HeadInfo;
import com.feiyou.headstyle.ui.custom.RoundedCornersTransformation;

import java.util.List;

/**
 * Created by admin on 2018/1/8.
 */

public class HeadInfoTempAdapter extends BaseQuickAdapter<HeadInfo, BaseViewHolder> {

    private Context mContext;
    private int tempWidth;

    public HeadInfoTempAdapter(Context context, List<HeadInfo> datas) {
        super(R.layout.head_info_temp_item, datas);
        this.mContext = context;
        tempWidth = (ScreenUtils.getScreenWidth() - SizeUtils.dp2px(24)) / 3;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final HeadInfo item) {
        /*LinearLayout itemLayout = helper.itemView.findViewById(R.id.head_item_layout);
        LinearLayout.LayoutParams itemParams = new LinearLayout.LayoutParams(tempWidth, tempWidth);
        itemLayout.setLayoutParams(itemParams);

        RequestOptions options = new RequestOptions().skipMemoryCache(true);
        options.placeholder(R.mipmap.image_def).error(R.mipmap.image_def);
        options.override(tempWidth - 8, tempWidth - 8);
        //options.transform(new GlideRoundTransform(mContext, 5));
        options.transform(new RoundedCornersTransformation(SizeUtils.dp2px(5),0));*/
        /*if (item.getItemType() == HeadInfo.HEAD_AD) {
            helper.setVisible(R.id.iv_listitem_express, true);
            helper.setVisible(R.id.iv_head_info, false);
        } else {
            helper.setVisible(R.id.iv_listitem_express, false);
            helper.setVisible(R.id.iv_head_info, true);

            Glide.with(mContext).load(item.getImgurl()).into((ImageView) helper.getView(R.id.iv_head_info));
        }*/
        Glide.with(mContext).load(item.getImgurl()).into((ImageView) helper.getView(R.id.iv_head_info));
    }
}