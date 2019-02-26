package com.feiyou.headstyle.ui.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.VideoInfo;
import com.feiyou.headstyle.bean.WordInfo;
import com.feiyou.headstyle.ui.custom.GlideRoundTransform;
import com.feiyou.headstyle.ui.custom.GlideRoundedCornersTransform;

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

        //设置视频封面
        RequestOptions options = new RequestOptions();
        options.optionalTransform(new GlideRoundedCornersTransform(10f, GlideRoundedCornersTransform.CornerType.TOP));
        Glide.with(mContext).load(item.getVideoCover()).apply(options).into((ImageView) helper.itemView.findViewById(R.id.iv_video_cover));

        helper.setText(R.id.tv_user_nick_name, item.getUserHeadName());
        helper.setText(R.id.tv_see_count, item.getClickNum() + "");

        RequestOptions headOptions = new RequestOptions();
        headOptions.transform(new GlideRoundTransform(mContext, SizeUtils.dp2px(10)));
        Glide.with(mContext).load(item.getUserHeadImg()).apply(headOptions).into((ImageView) helper.itemView.findViewById(R.id.iv_user_head));
    }
}