package com.feiyou.headstyle.ui.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.HeadInfo;
import com.feiyou.headstyle.bean.MoreTypeInfo;
import com.feiyou.headstyle.ui.custom.GlideRoundTransform;

import java.util.List;

/**
 * Created by admin on 2018/1/8.
 */

public class MoreTypeAdapter extends BaseQuickAdapter<MoreTypeInfo, BaseViewHolder> {

    private Context mContext;

    public MoreTypeAdapter(Context context, List<MoreTypeInfo> datas) {
        super(R.layout.more_type_item, datas);
        this.mContext = context;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final MoreTypeInfo item) {
        RequestOptions options = new RequestOptions();
        options.error(R.mipmap.empty_icon).placeholder(R.mipmap.empty_icon);
        Glide.with(mContext).load(item.getTagsimg()).apply(options).into((ImageView) helper.itemView.findViewById(R.id.iv_type_img));
        helper.setText(R.id.tv_type_name, item.getTagsname()).setText(R.id.tv_type_desc, item.getDesc());
    }
}