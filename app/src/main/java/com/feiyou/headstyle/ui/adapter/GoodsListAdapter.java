package com.feiyou.headstyle.ui.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.AnswerInfo;
import com.feiyou.headstyle.bean.GoodInfo;
import com.feiyou.headstyle.ui.custom.LeftRightCornersTransform;

import java.util.List;

/**
 * Created by admin on 2018/1/8.
 */

public class GoodsListAdapter extends BaseQuickAdapter<GoodInfo, BaseViewHolder> {

    private Context mContext;

    public GoodsListAdapter(Context context, List<GoodInfo> datas) {
        super(R.layout.good_item_view, datas);
        this.mContext = context;
    }

    @Override
    protected void convert(final BaseViewHolder helper, GoodInfo item) {
        helper.setText(R.id.tv_good_name, item.getGoodsname())
                .setText(R.id.tv_good_price, item.getCashprice() > 0 ? item.getGoldprice() + "+" + item.getCashprice() + "元" : item.getGoldprice() + "")
                .setText(R.id.tv_sale_num, "已售:" + item.getFalsenum() + "件");
        String coverUrl = null;
        if (!StringUtils.isEmpty(item.getSmallimg())) {
            String[] preImages = item.getSmallimg().split(",");
            if (preImages != null && preImages.length > 0) {
                coverUrl = preImages[0];
            }
        }

        LeftRightCornersTransform transform = new LeftRightCornersTransform(mContext, SizeUtils.dp2px(6));
        transform.setNeedCorner(true, true, false, false);
        RequestOptions options = new RequestOptions();
        options.override(SizeUtils.dp2px(164), SizeUtils.dp2px(120));
        options.placeholder(R.mipmap.good_def);
        options.error(R.mipmap.good_def);
        options.transform(transform);
        Glide.with(mContext).asBitmap().load(coverUrl).apply(options).into((ImageView) helper.getView(R.id.iv_good_cover));
    }
}