package com.feiyou.headstyle.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
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
import com.feiyou.headstyle.bean.MyAtMessage;
import com.feiyou.headstyle.bean.MyCommentInfo;
import com.feiyou.headstyle.ui.custom.GlideRoundTransform;
import com.feiyou.headstyle.view.MyClickText;

import java.util.List;

/**
 * Created by admin on 2018/1/8.
 */

public class MyAtMessageAdapter extends BaseQuickAdapter<MyAtMessage, BaseViewHolder> {

    private Context mContext;

    public MyAtMessageAdapter(Context context, List<MyAtMessage> datas) {
        super(R.layout.my_at_message_item, datas);
        this.mContext = context;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final MyAtMessage item) {
        String tempAt = StringUtils.isEmpty(item.getContent()) ? "" : item.getContent();

        //@的内容
        RequestOptions options = new RequestOptions();
        options.transform(new GlideRoundTransform(mContext, 24));
        Glide.with(mContext).load(item.getUserimg()).apply(options).into((ImageView) helper.getView(R.id.iv_reply_head));
        helper.setText(R.id.tv_reply_nick_name, item.getNickname())
                .setText(R.id.tv_reply_content, Html.fromHtml(tempAt));

        String tempMessageContent = StringUtils.isEmpty(item.getMessageContent()) ? "" : item.getMessageContent();
        //帖子内容
        Glide.with(mContext).load(item.getImage()).into((ImageView) helper.getView(R.id.iv_note_img));
        helper.setText(R.id.tv_note_title, Html.fromHtml(tempMessageContent))
                .setText(R.id.tv_reply_date, TimeUtils.millis2String(item.getAddTime() != null ? item.getAddTime() * 1000 : 0));
    }
}