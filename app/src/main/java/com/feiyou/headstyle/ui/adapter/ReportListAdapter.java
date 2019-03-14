package com.feiyou.headstyle.ui.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.ReportInfo;

import java.util.List;

/**
 * Created by admin on 2018/1/8.
 */

public class ReportListAdapter extends BaseQuickAdapter<ReportInfo, BaseViewHolder> {

    private Context mContext;

    public ReportListAdapter(Context context, List<ReportInfo> datas) {
        super(R.layout.report_list_item, datas);
        this.mContext = context;
    }

    @Override
    protected void convert(final BaseViewHolder helper, ReportInfo item) {
        helper.setText(R.id.tv_report_name, item.getReportTypeName());
        Glide.with(mContext).load(item.isSelected() ? R.mipmap.friend_select : R.mipmap.friend_normal).into((ImageView) helper.getView(R.id.iv_report_select));
    }
}