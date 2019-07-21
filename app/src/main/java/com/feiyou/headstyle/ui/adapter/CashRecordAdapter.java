package com.feiyou.headstyle.ui.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.blankj.utilcode.util.TimeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.AnswerInfo;
import com.feiyou.headstyle.bean.CashRecord;

import java.util.List;

/**
 * Created by admin on 2018/1/8.
 */

public class CashRecordAdapter extends BaseQuickAdapter<CashRecord, BaseViewHolder> {

    private Context mContext;

    public CashRecordAdapter(Context context, List<CashRecord> datas) {
        super(R.layout.cash_record_item, datas);
        this.mContext = context;
    }

    @Override
    protected void convert(final BaseViewHolder helper, CashRecord item) {
        String cashWayTxt = item.getOutway() == 1 ? "微信提现" : "支付宝提现";
        String cashStateTxt = "";
        TextView mCashStateTv = helper.getView(R.id.tv_cash_state);
        switch (item.getOutstatus()) {
            case 1:
                cashStateTxt = "待审核";
                mCashStateTv.setText(cashStateTxt);
                mCashStateTv.setTextColor(ContextCompat.getColor(mContext,R.color.search_number_color3));
                break;
            case 2:
                cashStateTxt = "已到账";
                mCashStateTv.setText(cashStateTxt);
                mCashStateTv.setTextColor(ContextCompat.getColor(mContext,R.color.set_weixin_bg_color));
                break;
            case 99:
                cashStateTxt = "提现失败";
                mCashStateTv.setText(cashStateTxt);
                mCashStateTv.setTextColor(ContextCompat.getColor(mContext,R.color.receive_txt_color));
                break;
            default:
                break;
        }
        helper.setText(R.id.tv_cash_money, cashWayTxt + item.getAmount() + "元")
                .setText(R.id.tv_cash_date, TimeUtils.millis2String(item.getAddtime() * 1000));
    }
}