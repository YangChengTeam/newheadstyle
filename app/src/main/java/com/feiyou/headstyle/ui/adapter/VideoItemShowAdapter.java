package com.feiyou.headstyle.ui.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.VideoInfo;
import com.feiyou.headstyle.ui.custom.GlideRoundTransform;

import java.util.List;

import cn.jzvd.JZDataSource;
import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;

/**
 * Created by admin on 2018/1/8.
 */

public class VideoItemShowAdapter extends BaseQuickAdapter<VideoInfo, BaseViewHolder> {

    private Context mContext;

    public VideoItemShowAdapter(Context context, List<VideoInfo> datas) {
        super(R.layout.item_video_show, datas);
        this.mContext = context;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final VideoInfo item) {
        JzvdStd jzvdStd = helper.getView(R.id.videoplayer);
        JZDataSource jzDataSource = new JZDataSource(item.getVideoPath());
        jzDataSource.looping = true;

        jzvdStd.setUp(jzDataSource, Jzvd.SCREEN_FULLSCREEN);
        if (helper.getAdapterPosition() == 0) {
            jzvdStd.startVideo();
        }

        helper.setText(R.id.tv_user_nick_name, item.getUserHeadName())
                .setText(R.id.tv_video_content, StringUtils.isEmpty(item.getName()) ? item.getTopic() : item.getName())
                .setText(R.id.tv_comment_num, item.getCommentNum() + "")
                .setText(R.id.tv_collect_num, item.getCollectNum() + "");

        RequestOptions headOptions = new RequestOptions().skipMemoryCache(true);
        headOptions.transform(new GlideRoundTransform(mContext, SizeUtils.dp2px(10)));
        Glide.with(mContext).load(item.getUserHeadImg()).apply(headOptions).into((ImageView) helper.itemView.findViewById(R.id.iv_user_head));

        helper.addOnClickListener(R.id.btn_is_follow).addOnClickListener(R.id.et_video_item);

        Drawable isCollect = ContextCompat.getDrawable(mContext, R.mipmap.video_is_follow_icon);
        Drawable notCollect = ContextCompat.getDrawable(mContext, R.mipmap.follow_count_icon);

        TextView isCollectTv = helper.itemView.findViewById(R.id.tv_collect_num);

        if (item.getIsCollect() == 0) {
            isCollectTv.setCompoundDrawablesWithIntrinsicBounds(notCollect, null, null, null);
        } else {
            isCollectTv.setCompoundDrawablesWithIntrinsicBounds(isCollect, null, null, null);
        }
        helper.addOnClickListener(R.id.tv_collect_num);
        helper.addOnClickListener(R.id.tv_comment_num);
        helper.addOnClickListener(R.id.layout_share).addOnClickListener(R.id.iv_user_head);

    }
}