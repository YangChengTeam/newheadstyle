package com.feiyou.headstyle.ui.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.blankj.utilcode.util.TimeUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.GoodDetailInfo;
import com.feiyou.headstyle.ui.custom.GlideRoundTransform;

import java.util.List;

/**
 * Created by admin on 2018/1/8.
 */

public class ExchangeListAdapter extends BaseQuickAdapter<GoodDetailInfo.ExchangeRecord, BaseViewHolder> {

    private Context mContext;

    public ExchangeListAdapter(Context context, List<GoodDetailInfo.ExchangeRecord> datas) {
        super(R.layout.exchange_item, datas);
        this.mContext = context;
    }

    @Override
    protected void convert(final BaseViewHolder helper, GoodDetailInfo.ExchangeRecord item) {
        helper.setText(R.id.tv_user_name, item.getNickname())
                .setText(R.id.tv_exchange_date, TimeUtils.millis2String(item.getAddtime()));
        RequestOptions options = new RequestOptions();
        options.transform(new GlideRoundTransform(mContext, 18));
        Glide.with(mContext).load(item.getUserimg()).apply(options).into((ImageView) helper.getView(R.id.iv_user_head));
    }
}