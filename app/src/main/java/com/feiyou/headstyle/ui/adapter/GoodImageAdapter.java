package com.feiyou.headstyle.ui.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feiyou.headstyle.R;

import java.util.List;

/**
 * Created by admin on 2018/1/8.
 */

public class GoodImageAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    private Context mContext;

    public GoodImageAdapter(Context context, List<String> datas) {
        super(R.layout.good_image_item, datas);
        this.mContext = context;
    }

    @Override
    protected void convert(final BaseViewHolder helper, String url) {
        Glide.with(mContext).load(url).into((ImageView) helper.getView(R.id.iv_good_item));
    }
}