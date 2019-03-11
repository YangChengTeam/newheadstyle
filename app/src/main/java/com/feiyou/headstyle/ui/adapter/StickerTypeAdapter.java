package com.feiyou.headstyle.ui.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.HeadInfo;
import com.feiyou.headstyle.bean.StickerTypeInfo;
import com.feiyou.headstyle.ui.custom.GlideRoundTransform;

import java.util.List;

/**
 * Created by admin on 2018/1/8.
 */

public class StickerTypeAdapter extends BaseQuickAdapter<StickerTypeInfo, BaseViewHolder> {

    private Context mContext;

    public StickerTypeAdapter(Context context, List<StickerTypeInfo> datas) {
        super(R.layout.sticker_type_item, datas);
        this.mContext = context;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final StickerTypeInfo item) {
        helper.setText(R.id.tv_type_name, item.getTypeName());
        if (item.isSelected()) {
            helper.setBackgroundColor(R.id.layout_type_item, ContextCompat.getColor(mContext, R.color.white));
            helper.setBackgroundColor(R.id.left_type_line, ContextCompat.getColor(mContext, R.color.tab_select_color));
        } else {
            helper.setBackgroundColor(R.id.layout_type_item, ContextCompat.getColor(mContext, R.color.sticker_item_bg_color));
            helper.setBackgroundColor(R.id.left_type_line, ContextCompat.getColor(mContext, R.color.sticker_item_bg_color));
        }
    }
}