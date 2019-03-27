package com.feiyou.headstyle.ui.adapter;

import android.content.Context;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.SizeUtils;
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

    private int type;

    public AddNoteImageAdapter(Context context, List<Object> datas,int type) {
        super(R.layout.add_note_image_item, datas);
        this.mContext = context;
        this.type = type;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final Object url) {
        int tempWidth = type == 1 ?(ScreenUtils.getScreenWidth() - SizeUtils.dp2px(24)) / 3 : (ScreenUtils.getScreenWidth() - SizeUtils.dp2px(48)) / 3;
        FrameLayout itemLayout = helper.itemView.findViewById(R.id.layout_item);
        FrameLayout.LayoutParams itemParams = new FrameLayout.LayoutParams(tempWidth, tempWidth);
        itemLayout.setLayoutParams(itemParams);

        RequestOptions options = new RequestOptions();
        options.placeholder(R.mipmap.image_def).error(R.mipmap.image_def);
        options.override(tempWidth - 8, tempWidth - 8);

        Glide.with(mContext).load(url).apply(options).into((ImageView) helper.itemView.findViewById(R.id.iv_photo));

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