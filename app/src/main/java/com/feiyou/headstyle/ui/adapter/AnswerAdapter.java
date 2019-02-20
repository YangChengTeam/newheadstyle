package com.feiyou.headstyle.ui.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.AnswerInfo;
import com.feiyou.headstyle.bean.StickerTypeInfo;

import java.util.List;

/**
 * Created by admin on 2018/1/8.
 */

public class AnswerAdapter extends BaseQuickAdapter<AnswerInfo, BaseViewHolder> {

    private Context mContext;

    public AnswerAdapter(Context context, List<AnswerInfo> datas) {
        super(R.layout.answer_item, datas);
        this.mContext = context;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final AnswerInfo item) {
        helper.setText(R.id.tv_answer, item.getAnswerName());
        if (item.isSelected()) {
            helper.setBackgroundColor(R.id.layout_answer_item, ContextCompat.getColor(mContext, R.color.tab_select_color));
            helper.setTextColor(R.id.tv_answer, ContextCompat.getColor(mContext, R.color.white));
        } else {
            helper.setBackgroundColor(R.id.layout_answer_item, ContextCompat.getColor(mContext, R.color.white));
            helper.setTextColor(R.id.tv_answer, ContextCompat.getColor(mContext, R.color.black4));
        }
    }
}