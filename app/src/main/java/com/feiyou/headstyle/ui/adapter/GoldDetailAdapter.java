package com.feiyou.headstyle.ui.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.blankj.utilcode.util.TimeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.GoldDetailInfo;

import java.util.List;

/**
 * Created by admin on 2018/1/8.
 */

public class GoldDetailAdapter extends BaseQuickAdapter<GoldDetailInfo, BaseViewHolder> {

    private Context mContext;

    public GoldDetailAdapter(Context context, List<GoldDetailInfo> datas) {
        super(R.layout.gold_detail_item, datas);
        this.mContext = context;
    }

    @Override
    protected void convert(final BaseViewHolder helper, GoldDetailInfo item) {
        TextView mGoldNumTv = helper.getView(R.id.tv_gold_num);
        helper.setText(R.id.tv_gold_name, item.getTaskname())
                .setText(R.id.tv_gold_date, TimeUtils.millis2String(item.getAddtime() * 1000));
        mGoldNumTv.setText(item.getStatus() == 1 ? "+" + item.getGoldnum() : "-" + item.getGoldnum());
        mGoldNumTv.setTextColor(ContextCompat.getColor(mContext, item.getStatus() == 1 ? R.color.set_weixin_bg_color : R.color.task_border_color));
    }
}