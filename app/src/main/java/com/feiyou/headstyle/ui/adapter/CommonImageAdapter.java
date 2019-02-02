package com.feiyou.headstyle.ui.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.SizeUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.HeadInfo;
import com.feiyou.headstyle.ui.custom.GlideRoundTransform;

import java.util.List;

/**
 * Created by admin on 2018/1/8.
 */

public class CommonImageAdapter extends BaseQuickAdapter<Object, BaseViewHolder> {

    private Context mContext;

    private int imageSize;

    public CommonImageAdapter(Context context, List<Object> datas, int size) {
        super(R.layout.common_image_item, datas);
        this.mContext = context;
        this.imageSize = size;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final Object url) {
        RequestOptions options = new RequestOptions();
        options.transform(new GlideRoundTransform(mContext, 3));
        options.placeholder(R.mipmap.empty_icon).error(R.mipmap.empty_icon);
        options.override(SizeUtils.dp2px(imageSize), SizeUtils.dp2px(imageSize));
        helper.itemView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,SizeUtils.dp2px(imageSize)));
        Glide.with(mContext).load(url).apply(options).into((ImageView) helper.itemView.findViewById(R.id.iv_photo));
    }
}