package com.feiyou.headstyle.ui.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.ui.custom.GlideRoundTransform;

import java.util.List;

/**
 * Created by admin on 2018/1/8.
 */

public class AddNoteImageAdapter extends BaseQuickAdapter<Object, BaseViewHolder> {

    private Context mContext;

    public AddNoteImageAdapter(Context context, List<Object> datas) {
        super(R.layout.add_note_image_item, datas);
        this.mContext = context;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final Object url) {
        Glide.with(mContext).load(url).into((ImageView) helper.itemView.findViewById(R.id.iv_photo));
        helper.addOnClickListener(R.id.iv_close);
        if (url instanceof Integer) {
            if (Integer.parseInt(url.toString()) == R.mipmap.add_my_photo) {
                helper.setVisible(R.id.iv_close, false);
            }
        } else {
            helper.setVisible(R.id.iv_close, true);
        }

    }
}