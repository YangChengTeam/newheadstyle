package com.feiyou.headstyle.ui.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.HeadInfo;
import com.feiyou.headstyle.bean.SearchHotWord;
import com.feiyou.headstyle.ui.custom.GlideRoundTransform;

import java.util.List;

/**
 * Created by admin on 2018/1/8.
 */

public class SearchHotWordAdapter extends BaseQuickAdapter<SearchHotWord, BaseViewHolder> {

    private Context mContext;

    public SearchHotWordAdapter(Context context, List<SearchHotWord> datas) {
        super(R.layout.search_hot_item, datas);
        this.mContext = context;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final SearchHotWord item) {
        switch (helper.getAdapterPosition()) {
            case 0:
                helper.setBackgroundColor(R.id.tv_number, ContextCompat.getColor(mContext, R.color.search_number_color1));
                break;
            case 1:
                helper.setBackgroundColor(R.id.tv_number, ContextCompat.getColor(mContext, R.color.search_number_color2));
                break;
            case 2:
                helper.setBackgroundColor(R.id.tv_number, ContextCompat.getColor(mContext, R.color.search_number_color3));
                break;
            default:
                helper.setBackgroundColor(R.id.tv_number, ContextCompat.getColor(mContext, R.color.search_number_def));
                break;
        }
        helper.setText(R.id.tv_number, (helper.getAdapterPosition() + 1) + "").setText(R.id.tv_hot_word, item.getName());
    }
}