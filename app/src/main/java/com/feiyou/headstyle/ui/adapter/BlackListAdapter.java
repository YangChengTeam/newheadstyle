package com.feiyou.headstyle.ui.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.HeadType;

import java.util.List;

/**
 * Created by admin on 2018/1/8.
 */

public class BlackListAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    private Context mContext;

    public BlackListAdapter(Context context, List<String> datas) {
        super(R.layout.black_list_item, datas);
        this.mContext = context;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final String item) {

    }
}