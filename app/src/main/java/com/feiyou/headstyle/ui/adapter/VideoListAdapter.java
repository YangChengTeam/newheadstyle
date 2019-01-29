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
import com.feiyou.headstyle.bean.VideoInfo;
import com.feiyou.headstyle.bean.WordInfo;
import com.feiyou.headstyle.ui.custom.GlideRoundTransform;

import java.util.List;

/**
 * Created by admin on 2018/1/8.
 */

public class VideoListAdapter extends BaseQuickAdapter<VideoInfo, BaseViewHolder> {

    private Context mContext;

    public VideoListAdapter(Context context, List<VideoInfo> datas) {
        super(R.layout.video_list_item, datas);
        this.mContext = context;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final VideoInfo item) {
        LinearLayout itemLayout = helper.itemView.findViewById(R.id.layout_video_item);
        itemLayout.setLayoutParams(new LinearLayout.LayoutParams(ScreenUtils.getScreenWidth() / 2, LinearLayout.LayoutParams.WRAP_CONTENT));

        Glide.with(mContext).asBitmap().load(R.mipmap.video_def).into((ImageView) helper.itemView.findViewById(R.id.iv_video_cover));
    }
}