package com.feiyou.headstyle.ui.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.blankj.utilcode.util.SizeUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.ArticleInfo;
import com.feiyou.headstyle.bean.HeadInfo;
import com.feiyou.headstyle.bean.HeadType;
import com.feiyou.headstyle.ui.custom.GlideRoundTransform;

import java.util.List;

/**
 * Created by admin on 2018/1/8.
 */

public class CommunityHeadAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    private Context mContext;

    private int mSize;

    private boolean showRound;

    public CommunityHeadAdapter(Context context, List<String> datas, int size,boolean show) {
        super(R.layout.community_head_item, datas);
        this.mContext = context;
        this.mSize = size;
        this.showRound = show;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final String item) {
        RequestOptions options = new RequestOptions();
        if(showRound){
            options.transform(new GlideRoundTransform(mContext, 5));
        }
        options.override(SizeUtils.dp2px(mSize), SizeUtils.dp2px(mSize));
        Glide.with(mContext).load(item).apply(options).into((ImageView) helper.itemView.findViewById(R.id.iv_article_img));
    }
}