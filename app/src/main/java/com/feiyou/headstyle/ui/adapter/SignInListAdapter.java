package com.feiyou.headstyle.ui.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.WelfareInfo;

import java.util.List;

/**
 * Created by admin on 2018/1/8.
 */

public class SignInListAdapter extends BaseQuickAdapter<WelfareInfo.SignSetInfo, BaseViewHolder> {

    private Context mContext;

    private int totalSignDay;

    private int loop;

    private int signDoneDay;

    public SignInListAdapter(Context context, List<WelfareInfo.SignSetInfo> datas) {
        super(R.layout.sign_in_item_view, datas);
        this.mContext = context;
    }

    public void setTotalSignDay(int totalSignDay) {
        this.totalSignDay = totalSignDay;
        loop = this.totalSignDay / 7;
        signDoneDay = this.totalSignDay % 7;
    }

    @Override
    protected void convert(final BaseViewHolder helper, WelfareInfo.SignSetInfo item) {


        if(signDoneDay == 0 && loop > 0){
            helper.setText(R.id.tv_sign_day_txt, "第" + ((loop-1) * 7 + item.getDays()) + "天");
        }else{
            helper.setText(R.id.tv_sign_day_txt, "第" + (loop * 7 + item.getDays()) + "天");
        }

        if (helper.getAdapterPosition() < signDoneDay || (signDoneDay == 0 && loop > 0)) {
            helper.setText(R.id.tv_sign_state, "已领");
            Glide.with(mContext).load(R.mipmap.sign_done_gold_icon).into((ImageView) helper.getView(R.id.iv_gold_icon));
            helper.setTextColor(R.id.tv_sign_state, ContextCompat.getColor(mContext, R.color.sign_done_color));
        } else {
            helper.setText(R.id.tv_sign_state, "+" + item.getGold());
            Glide.with(mContext).load(R.mipmap.gold_icon).into((ImageView) helper.getView(R.id.iv_gold_icon));
            helper.setTextColor(R.id.tv_sign_state, ContextCompat.getColor(mContext, R.color.profit_color));
        }

        if (helper.getAdapterPosition() > 0 && helper.getAdapterPosition() % 6 == 0) {
            if(signDoneDay == 0 && loop > 0){
                Glide.with(mContext).load(R.mipmap.seven_day_sign_done).into((ImageView) helper.getView(R.id.iv_gold_icon));
            }else{
                Glide.with(mContext).load(R.mipmap.small_red).into((ImageView) helper.getView(R.id.iv_gold_icon));
            }

            helper.setText(R.id.tv_sign_state, "现金");
            helper.setTextColor(R.id.tv_sign_state, ContextCompat.getColor(mContext, R.color.profit_color));
        }
    }
}