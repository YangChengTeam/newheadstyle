package com.feiyou.headstyle.ui.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.CategoryInfo;
import com.feiyou.headstyle.bean.HeadType;
import com.feiyou.headstyle.bean.WordInfo;
import com.feiyou.headstyle.ui.custom.GlideRoundTransform;

import java.util.List;

/**
 * Created by admin on 2018/1/8.
 */

public class HeadTypeAdapter extends BaseQuickAdapter<CategoryInfo, BaseViewHolder> {

    private Context mContext;

    public HeadTypeAdapter(Context context, List<CategoryInfo> datas) {
        super(R.layout.head_type_item, datas);
        this.mContext = context;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final CategoryInfo item) {
        helper.setText(R.id.iv_type_name,item.getTagsname());
        Glide.with(mContext).load(item.getTagsimg()).into((ImageView) helper.itemView.findViewById(R.id.iv_type_img));
    }
}