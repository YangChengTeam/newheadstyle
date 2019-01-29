package com.feiyou.headstyle.ui.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.blankj.utilcode.util.StringUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.WordInfo;
import com.feiyou.headstyle.ui.custom.GlideRoundTransform;

import java.util.List;

/**
 * Created by admin on 2018/1/8.
 */

public class WordReadAdapter extends BaseQuickAdapter<WordInfo, BaseViewHolder> {

    private Context mContext;

    String baseUrl = "http://192.168.80.97:8888/words/";

    public WordReadAdapter(Context context, List<WordInfo> datas) {
        super(R.layout.word_read_item, datas);
        this.mContext = context;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final WordInfo item) {

        helper.setText(R.id.tv_word_type_name, item.getWord()).setText(R.id.tv_word_type_cn_name, item.getWordMean());

        RequestOptions options = new RequestOptions();
        options.transform(new GlideRoundTransform(mContext, 5));

        Glide.with(mContext).asBitmap().load(baseUrl + item.getWordImage()).apply(options).into((ImageView) helper.itemView.findViewById(R.id.iv_word_type_img));

    }
}