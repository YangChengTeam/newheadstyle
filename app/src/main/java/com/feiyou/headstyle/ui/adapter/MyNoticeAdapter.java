package com.feiyou.headstyle.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.MyCommentInfo;
import com.feiyou.headstyle.bean.SystemInfo;
import com.feiyou.headstyle.ui.custom.GlideRoundTransform;
import com.feiyou.headstyle.ui.custom.GlideRoundedCornersTransform;
import com.feiyou.headstyle.ui.custom.RoundedCornersTransformation;
import com.feiyou.headstyle.view.MyClickText;

import java.util.List;

/**
 * Created by admin on 2018/1/8.
 */

public class MyNoticeAdapter extends BaseQuickAdapter<SystemInfo, BaseViewHolder> {

    private Context mContext;

    public MyNoticeAdapter(Context context, List<SystemInfo> datas) {
        super(R.layout.my_notice_item, datas);
        this.mContext = context;
    }

    @Override
    protected void convert(final BaseViewHolder helper, SystemInfo item) {
        RequestOptions options = new RequestOptions().skipMemoryCache(true);
        options.transform(new RoundedCornersTransformation(SizeUtils.dp2px(22), 0));
        Glide.with(mContext).load(item.getUserimg()).apply(options).into((ImageView) helper.getView(R.id.iv_user_img));

        helper.setText(R.id.tv_notice_nick_name, item.getNickname())
                .setText(R.id.tv_note_type_content, item.getType() == 1 ? "赞了你的评论" : "关注了你")
                .setText(R.id.tv_note_content, item.getContent())
                .setText(R.id.tv_notice_date, TimeUtils.millis2String(item.getAddTime() != null ? item.getAddTime() * 1000 : 0));
    }
}