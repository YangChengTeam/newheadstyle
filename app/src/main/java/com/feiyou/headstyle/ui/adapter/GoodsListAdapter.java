package com.feiyou.headstyle.ui.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.AnswerInfo;

import java.util.List;

/**
 * Created by admin on 2018/1/8.
 */

public class GoodsListAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    private Context mContext;

    public GoodsListAdapter(Context context, List<String> datas) {
        super(R.layout.good_item_view, datas);
        this.mContext = context;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final String item) {

    }
}