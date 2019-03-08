package com.feiyou.headstyle.ui.adapter;

import android.content.Context;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.StarInfo;
import com.feiyou.headstyle.ui.custom.GlideRoundTransform;

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

        int tempWidth = (ScreenUtils.getScreenWidth() - SizeUtils.dp2px(56)) / 4;
        LinearLayout itemLayout = helper.itemView.findViewById(R.id.layout_star_item);
        LinearLayout.LayoutParams itemParams = new LinearLayout.LayoutParams(tempWidth, SizeUtils.dp2px(108));
        int temp = SizeUtils.dp2px(4);
        itemLayout.setGravity(Gravity.CENTER);
        itemParams.setMargins(temp, temp, temp, temp);
        itemLayout.setLayoutParams(itemParams);

        RequestOptions options = new RequestOptions();
        options.placeholder(R.mipmap.image_def).error(R.mipmap.image_def);
        options.override(tempWidth - SizeUtils.dp2px(14), tempWidth - SizeUtils.dp2px(14));

        helper.setText(R.id.tv_star_name, item.getStarName())
                .setText(R.id.tv_star_date, item.getStarDate());
        Glide.with(mContext).load(item.getStarImage()).apply(options).into((ImageView) helper.itemView.findViewById(R.id.iv_star_img));
    }
}