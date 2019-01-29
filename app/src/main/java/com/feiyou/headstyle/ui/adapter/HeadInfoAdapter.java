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

public class HeadInfoAdapter extends BaseQuickAdapter<HeadInfo, BaseViewHolder> {

    private Context mContext;

    String baseUrl = "http://192.168.80.97:8888/words/";

    public HeadInfoAdapter(Context context, List<HeadInfo> datas) {
        super(R.layout.head_info_item, datas);
        this.mContext = context;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final HeadInfo item) {
        RequestOptions options = new RequestOptions();
        options.transform(new GlideRoundTransform(mContext, 5));
        options.placeholder(R.mipmap.empty_icon).error(R.mipmap.empty_icon);
        Glide.with(mContext).load(item.getImgurl()).apply(options).into((ImageView) helper.itemView.findViewById(R.id.iv_head_info));
    }
}