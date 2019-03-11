package com.feiyou.headstyle.ui.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.HeadInfo;
import com.feiyou.headstyle.bean.VideoInfo;
import com.feiyou.headstyle.ui.custom.FullScreenVideoView;
import com.feiyou.headstyle.ui.custom.GlideRoundTransform;

import java.util.List;

/**
 * Created by admin on 2018/1/8.
 */

public class VideoItemAdapter extends BaseQuickAdapter<VideoInfo, BaseViewHolder> {

    private Context mContext;

    public VideoItemAdapter(Context context, List<VideoInfo> list) {
        super(R.layout.item_view_pager, list);
        this.mContext = context;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final VideoInfo item) {
        //封面
        ImageView coverImageView = helper.itemView.findViewById(R.id.img_thumb);

        //视频
        FullScreenVideoView videoView = helper.itemView.findViewById(R.id.video_view);
        //视频缩放到屏幕宽度
        double realHeight = (double) ScreenUtils.getScreenWidth() / ((double) item.getWidth() / (double) item.getHeight());
        videoView.setVideoSize(ScreenUtils.getScreenWidth(), (int) realHeight);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ScreenUtils.getScreenWidth(), (int) realHeight);
        params.setMargins(0, (ScreenUtils.getScreenHeight() - (int) realHeight) / 2, 0, 0);
        videoView.setLayoutParams(params);
        videoView.setVideoPath(item.getVideoPath());

        coverImageView.setLayoutParams(params);
        Glide.with(mContext).load(item.getVideoCover()).into(coverImageView);

        helper.setText(R.id.tv_user_nick_name, item.getUserHeadName())
                .setText(R.id.tv_video_content, StringUtils.isEmpty(item.getName()) ? item.getTopic() : item.getName())
                .setText(R.id.tv_comment_num, item.getCommentNum() + "")
                .setText(R.id.tv_collect_num, item.getCollectNum() + "");

        RequestOptions headOptions = new RequestOptions();
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

        helper.addOnClickListener(R.id.tv_comment_num);
    }
}