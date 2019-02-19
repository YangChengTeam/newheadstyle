package com.feiyou.headstyle.ui.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.StickerInfo;

import java.util.List;

/**
 * Created by admin on 2018/1/8.
 */

public class StickerAdapter extends BaseQuickAdapter<StickerInfo, BaseViewHolder> {

    private Context mContext;

    public StickerAdapter(Context context, List<StickerInfo> datas) {
        super(R.layout.sticker_item, datas);
        this.mContext = context;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final StickerInfo item) {
        Glide.with(mContext).load(item.getIco()).into((ImageView) helper.itemView.findViewById(R.id.iv_sticker));
    }
}
