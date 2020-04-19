package com.feiyou.headstyle.ui.adapter;

import android.content.Context;
import androidx.core.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.CashMoneyInfo;

import java.util.List;

/**
 * Created by admin on 2018/1/8.
 */

public class PriceListAdapter extends BaseQuickAdapter<CashMoneyInfo, BaseViewHolder> {

    private Context mContext;

    private boolean isQuickTx;

    public void setQuickTx(boolean quickTx) {
        isQuickTx = quickTx;
    }

    public PriceListAdapter(Context context, List<CashMoneyInfo> datas) {
        super(R.layout.price_item, datas);
        this.mContext = context;
    }

    @Override
    protected void convert(final BaseViewHolder helper, CashMoneyInfo item) {
        helper.setText(R.id.tv_cash_money, item.getAmount() + "元");
        if (helper.getAdapterPosition() == 0 && item.isSelected()) {
            helper.setBackgroundRes(R.id.tv_cash_money, R.mipmap.one_cash_bg);
            helper.setTextColor(R.id.tv_cash_money, ContextCompat.getColor(mContext, R.color.wait_charge_color));
        } else {
            if (helper.getAdapterPosition() == 0) {
                helper.setBackgroundRes(R.id.tv_cash_money, item.isSelected() ? R.drawable.choose_border_bg : R.mipmap.one_cash_not);
            } else {
                helper.setBackgroundRes(R.id.tv_cash_money, item.isSelected() ? R.drawable.choose_border_bg : R.drawable.line_border_bg);
            }

            helper.setTextColor(R.id.tv_cash_money, ContextCompat.getColor(mContext, item.isSelected() ? R.color.wait_charge_color : R.color.black1));
        }

        if (isQuickTx && helper.getAdapterPosition() == 0) {
            helper.setBackgroundRes(R.id.tv_cash_money, R.mipmap.one_cash_not);
            helper.setTextColor(R.id.tv_cash_money, ContextCompat.getColor(mContext, R.color.gray999));
        }
    }
}