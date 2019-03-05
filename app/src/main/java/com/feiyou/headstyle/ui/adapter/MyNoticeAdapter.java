package com.feiyou.headstyle.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.MyCommentInfo;
import com.feiyou.headstyle.ui.custom.GlideRoundTransform;
import com.feiyou.headstyle.view.MyClickText;

import java.util.List;

/**
 * Created by admin on 2018/1/8.
 */

public class MyNoticeAdapter extends BaseQuickAdapter<MyCommentInfo, BaseViewHolder> {

    private Context mContext;

    public MyNoticeAdapter(Context context, List<MyCommentInfo> datas) {
        super(R.layout.my_notice_item, datas);
        this.mContext = context;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final MyCommentInfo item) {
//        RequestOptions options = new RequestOptions();
//        options.transform(new GlideRoundTransform(mContext, 24));
//        Glide.with(mContext).load(item.getRepeatUserimg()).apply(options).into((ImageView) helper.itemView.findViewById(R.id.iv_reply_head));


    }
}