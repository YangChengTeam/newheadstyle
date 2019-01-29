package com.feiyou.headstyle.ui.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.blankj.utilcode.util.StringUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.LetterInfo;
import com.feiyou.headstyle.bean.WordTypeInfo;
import com.feiyou.headstyle.ui.custom.GlideRoundTransform;

import java.util.List;

/**
 * Created by admin on 2018/1/8.
 */

public class WordTypeAdapter extends BaseQuickAdapter<WordTypeInfo, BaseViewHolder> {

    private Context mContext;

    String baseUrl = "http://192.168.80.97:8888/words/";

    public WordTypeAdapter(Context context, List<WordTypeInfo> datas) {
        super(R.layout.word_type_item, datas);
        this.mContext = context;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final WordTypeInfo item) {

        String tempName = item.getTypeName();
        String enName = "";
        String cnName = "";
        if (!StringUtils.isEmpty(tempName)) {
            String[] tempArray = tempName.split("#");
            if (tempArray != null && tempArray.length == 2) {
                enName = tempArray[0];
                cnName = tempArray[1];
            }
        }

        helper.setText(R.id.tv_word_type_name, enName).setText(R.id.tv_word_type_cn_name, cnName);

        RequestOptions options = new RequestOptions();
        options.transform(new GlideRoundTransform(mContext, 5));

        Glide.with(mContext).asBitmap().load(baseUrl + item.getTypeImage()).apply(options).into((ImageView) helper.itemView.findViewById(R.id.iv_word_type_img));

    }
}