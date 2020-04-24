package com.feiyou.headstyle.ui.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.ScreenUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.NoteInfo;
import com.feiyou.headstyle.bean.TopicInfo;

import java.util.List;

/**
 * Created by admin on 2018/1/8.
 */

public class TopicAdapter extends BaseQuickAdapter<TopicInfo, BaseViewHolder> {

    private Context mContext;

    public TopicAdapter(Context context, List<TopicInfo> datas) {
        super(R.layout.topic_item, datas);
        this.mContext = context;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final TopicInfo item) {

        LinearLayout itemLayout = helper.itemView.findViewById(R.id.layout_top_item);
        itemLayout.setLayoutParams(new LinearLayout.LayoutParams(ScreenUtils.getScreenWidth() / 5, LinearLayout.LayoutParams.MATCH_PARENT));

        RequestOptions options = new RequestOptions().skipMemoryCache(true);
        Glide.with(mContext).load(item.getLocalIcoRes()).apply(options).into((ImageView) helper.getView(R.id.iv_topic_img));
        helper.setText(R.id.tv_topic_name, item.getName());
    }
}