package com.feiyou.headstyle.ui.adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;
import android.widget.VideoView;

import com.blankj.utilcode.util.AppUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.HeadInfo;
import com.feiyou.headstyle.ui.custom.GlideRoundTransform;

import java.util.List;

/**
 * Created by admin on 2018/1/8.
 */

public class VideoItemAdapter extends BaseQuickAdapter<Integer, BaseViewHolder> {

    private Context mContext;

    private int[] videos;

    public VideoItemAdapter(Context context, List<Integer> list, int[] videos) {
        super(R.layout.item_view_pager, list);
        this.mContext = context;
        this.videos = videos;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final Integer item) {
        Glide.with(mContext).load(item).into((ImageView) helper.itemView.findViewById(R.id.img_thumb));
        VideoView videoView = helper.itemView.findViewById(R.id.video_view);
        videoView.setVideoURI(Uri.parse("android.resource://" + AppUtils.getAppPackageName() + "/" + videos[helper.getAdapterPosition() % 5]));
        //helper.addOnClickListener(R.id.iv_back);
    }
}