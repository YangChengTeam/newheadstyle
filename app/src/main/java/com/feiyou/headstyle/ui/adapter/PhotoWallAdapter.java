package com.feiyou.headstyle.ui.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.PhotoInfo;
import com.feiyou.headstyle.ui.custom.GlideRoundTransform;

import java.util.List;

/**
 * Created by admin on 2018/1/8.
 */

public class PhotoWallAdapter extends BaseQuickAdapter<PhotoInfo, BaseViewHolder> {

    private Context mContext;

    private boolean openEdit;

    public PhotoWallAdapter(Context context, List<PhotoInfo> datas) {
        super(R.layout.photo_wall_item, datas);
        this.mContext = context;
    }

    public void setOpenEdit(boolean openEdit) {
        this.openEdit = openEdit;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final PhotoInfo item) {
        RequestOptions options = new RequestOptions();
        options.transform(new GlideRoundTransform(mContext, 3));
        options.placeholder(R.mipmap.empty_icon).error(R.mipmap.empty_icon);
        Glide.with(mContext).load(item.getUrl()).into((ImageView) helper.itemView.findViewById(R.id.iv_photo));
        helper.addOnClickListener(R.id.iv_choose);

        if (openEdit) {
            helper.setVisible(R.id.iv_choose, true);
            if (item.isSelected()) {
                helper.setImageResource(R.id.iv_choose, R.mipmap.photo_selected);
            } else {
                helper.setImageResource(R.id.iv_choose, R.mipmap.photo_normal);
            }
        } else {
            helper.setVisible(R.id.iv_choose, false);
        }

    }
}