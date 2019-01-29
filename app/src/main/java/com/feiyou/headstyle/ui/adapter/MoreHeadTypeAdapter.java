package com.feiyou.headstyle.ui.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.HeadType;

import java.util.List;

/**
 * Created by admin on 2018/1/8.
 */

public class MoreHeadTypeAdapter extends BaseQuickAdapter<HeadType, BaseViewHolder> {

    private Context mContext;

    String baseUrl = "http://192.168.80.97:8888/words/";

    public MoreHeadTypeAdapter(Context context, List<HeadType> datas) {
        super(R.layout.more_head_type_item, datas);
        this.mContext = context;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final HeadType item) {

    }
}