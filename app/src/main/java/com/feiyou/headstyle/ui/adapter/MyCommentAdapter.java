package com.feiyou.headstyle.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
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
import com.feiyou.headstyle.ui.custom.GlideRoundTransform;
import com.feiyou.headstyle.view.MyClickText;

import java.util.List;

/**
 * Created by admin on 2018/1/8.
 */

public class MyCommentAdapter extends BaseQuickAdapter<MyCommentInfo, BaseViewHolder> {

    private Context mContext;

    public MyCommentAdapter(Context context, List<MyCommentInfo> datas) {
        super(R.layout.my_message_comment_item, datas);
        this.mContext = context;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final MyCommentInfo item) {
        RequestOptions options = new RequestOptions();
        options.transform(new GlideRoundTransform(mContext, 24));
        Glide.with(mContext).load(item.getRepeatUserimg()).apply(options).into((ImageView) helper.itemView.findViewById(R.id.iv_reply_head));

        helper.setText(R.id.tv_reply_nick_name, item.getRepeatNickname())
                .setText(R.id.tv_reply_date, TimeUtils.millis2String(item.getAddTime() != null ? item.getAddTime() * 1000 : 0));

        //回复的内容
        TextView replyTv = helper.itemView.findViewById(R.id.tv_reply_content);
        if (!StringUtils.isEmpty(item.getRepeatContent())) {
            String replyStr = item.getType() == 1 ? "评论我：" : "回复我：";
            SpannableString replySpan = new SpannableString(replyStr + item.getRepeatContent());
            replySpan.setSpan(new MyClickText(mContext), 0, replyStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            //当然这里也可以通过setSpan来设置哪些位置的文本哪些颜色
            replyTv.setText(Html.fromHtml(replySpan.toString()));
            replyTv.setMovementMethod(LinkMovementMethod.getInstance());//不设置 没有点击事件
            replyTv.setHighlightColor(Color.TRANSPARENT); //设置点击后的颜色为透明
        }

        //原内容
        TextView olgTv = helper.itemView.findViewById(R.id.tv_old_content);
        if (!StringUtils.isEmpty(item.getContent())) {
            String preStr = item.getType() == 1 ? "我的帖子：" : "我的评论：";
            SpannableString tempStr = new SpannableString(preStr + item.getContent());
            tempStr.setSpan(new MyClickText(mContext), 0, preStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            //当然这里也可以通过setSpan来设置哪些位置的文本哪些颜色
            olgTv.setText(Html.fromHtml(tempStr.toString()));
            olgTv.setMovementMethod(LinkMovementMethod.getInstance());//不设置 没有点击事件
            olgTv.setHighlightColor(Color.TRANSPARENT); //设置点击后的颜色为透明
        }
        helper.addOnClickListener(R.id.iv_reply_head).addOnClickListener(R.id.tv_reply_content).addOnClickListener(R.id.tv_old_content);

        if (item.getUserId().equals("1")) {
            helper.setVisible(R.id.iv_system_user, true);
        } else {
            helper.setVisible(R.id.iv_system_user, false);
        }
    }
}