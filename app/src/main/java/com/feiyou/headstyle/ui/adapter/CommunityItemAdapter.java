package com.feiyou.headstyle.ui.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.HeadInfo;
import com.feiyou.headstyle.ui.custom.GlideRoundTransform;

import java.util.List;

/**
 * Created by admin on 2018/1/8.
 */

public class CommunityItemAdapter extends BaseQuickAdapter<HeadInfo, BaseViewHolder> {

    private Context mContext;

    private int showType;

    public CommunityItemAdapter(Context context, List<HeadInfo> datas, int showType) {
        super(R.layout.community_top_item, datas);
        this.mContext = context;
        this.showType = showType;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final HeadInfo item) {
        int tempWidth = (ScreenUtils.getScreenWidth() - SizeUtils.dp2px(showType == 1 ? 24 : 48)) / 3;
        LinearLayout itemLayout = helper.itemView.findViewById(R.id.top_item_layout);
        LinearLayout.LayoutParams itemParams = new LinearLayout.LayoutParams(tempWidth, tempWidth);
        itemLayout.setLayoutParams(itemParams);

        RequestOptions options = new RequestOptions();
        options.placeholder(R.mipmap.image_def).error(R.mipmap.image_def);
        options.override(tempWidth - 8, tempWidth - 8);
        //options.centerCrop();

        Glide.with(mContext).load(item.getImgurl()).apply(options).into((ImageView) helper.itemView.findViewById(R.id.iv_top_img));
    }
}