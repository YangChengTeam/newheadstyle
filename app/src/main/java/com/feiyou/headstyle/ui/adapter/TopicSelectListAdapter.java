package com.feiyou.headstyle.ui.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.blankj.utilcode.util.SizeUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.TopicInfo;
import com.feiyou.headstyle.ui.custom.GlideRoundTransform;
import com.feiyou.headstyle.ui.custom.RoundedCornersTransformation;

import java.util.List;

/**
 * Created by admin on 2018/1/8.
 */

public class TopicSelectListAdapter extends BaseQuickAdapter<TopicInfo, BaseViewHolder> {

    private Context mContext;

    public TopicSelectListAdapter(Context context, List<TopicInfo> datas) {
        super(R.layout.topic_select_item, datas);
        this.mContext = context;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final TopicInfo item) {
        RequestOptions requestOptions = new RequestOptions().skipMemoryCache(true);
        requestOptions.placeholder(R.mipmap.image_def);
        requestOptions.transform(new RoundedCornersTransformation(SizeUtils.dp2px(5), 0));

        Glide.with(mContext).load(item.getIco()).apply(requestOptions).into((ImageView) helper.itemView.findViewById(R.id.iv_topic_img));
        helper.setText(R.id.tv_topic_name, item.getName())
                .setText(R.id.tv_topic_desc, "今日新增" + item.getNum() + "话题");

        if (item.isSelected()) {
            helper.setImageResource(R.id.iv_topic_selected, R.mipmap.friend_select);
        } else {
            helper.setImageResource(R.id.iv_topic_selected, R.mipmap.friend_normal);
        }
    }
}