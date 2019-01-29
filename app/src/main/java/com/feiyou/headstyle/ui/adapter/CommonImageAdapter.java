package com.feiyou.headstyle.ui.adapter;

import android.content.Context;
import android.widget.ImageView;

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

    String baseUrl = "http://192.168.80.97:8888/words/";

    public CommonImageAdapter(Context context, List<Object> datas) {
        super(R.layout.common_image_item, datas);
        this.mContext = context;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final Object url) {
        RequestOptions options = new RequestOptions();
        options.transform(new GlideRoundTransform(mContext, 5));
        options.placeholder(R.mipmap.empty_icon).error(R.mipmap.empty_icon);
        Glide.with(mContext).load(url).apply(options).into((ImageView) helper.itemView.findViewById(R.id.iv_photo));
    }
}