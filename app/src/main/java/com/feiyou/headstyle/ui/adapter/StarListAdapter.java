package com.feiyou.headstyle.ui.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.StarInfo;

import java.util.List;

/**
 * Created by admin on 2018/1/8.
 */

public class StarListAdapter extends BaseQuickAdapter<StarInfo, BaseViewHolder> {

    private Context mContext;

    public StarListAdapter(Context context, List<StarInfo> datas) {
        super(R.layout.star_list_item, datas);
        this.mContext = context;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final StarInfo item) {
        helper.setText(R.id.tv_star_name, item.getStarName())
                .setText(R.id.tv_star_date, item.getStarDate());
        Glide.with(mContext).load(item.getStarImage()).into((ImageView) helper.itemView.findViewById(R.id.iv_star_img));
    }
}