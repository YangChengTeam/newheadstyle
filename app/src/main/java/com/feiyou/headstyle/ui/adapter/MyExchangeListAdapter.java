package com.feiyou.headstyle.ui.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.ExchangeInfo;

import java.util.List;

/**
 * Created by admin on 2018/1/8.
 */

public class MyExchangeListAdapter extends BaseQuickAdapter<ExchangeInfo, BaseViewHolder> {

    private Context mContext;

    public MyExchangeListAdapter(Context context, List<ExchangeInfo> datas) {
        super(R.layout.my_exchange_item, datas);
        this.mContext = context;
    }

    @Override
    protected void convert(final BaseViewHolder helper, ExchangeInfo item) {
        helper.setText(R.id.tv_good_name, item.getGoodsname())
                .setText(R.id.tv_exchange_date, TimeUtils.millis2String(item.getGoodstime() * 1000));

        int state = item.getStatus();
        String tempStateStr = "兑换成功";
        switch (state) {
            case 0:
                tempStateStr = "兑换成功";
                break;
            case 1:
                tempStateStr = "待充值";
                break;
            case 2:
                tempStateStr = "已到账";
                break;
            case 99:
                tempStateStr = "已过期";
                break;
            default:
                break;
        }

        helper.setText(R.id.tv_exchange_state, tempStateStr);

        String coverUrl = null;
        if (!StringUtils.isEmpty(item.getSmallimg())) {
            String[] preImages = item.getSmallimg().split(",");
            if (preImages != null && preImages.length > 0) {
                coverUrl = preImages[0];
            }
        }

        Glide.with(mContext).load(coverUrl).into((ImageView) helper.getView(R.id.iv_good_cover));
    }
}