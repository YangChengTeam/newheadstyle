package com.feiyou.headstyle.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.NoteSubComment;
import com.feiyou.headstyle.view.MyClickText;
import com.orhanobut.logger.Logger;

import java.util.List;

/**
 * Created by myflying on 2018/12/25.
 */
public class CommentReplyAdapter extends BaseQuickAdapter<NoteSubComment, BaseViewHolder> {

    private Context mContext;

    public CommentReplyAdapter(Context context, List<NoteSubComment> list) {
        super(R.layout.comment_reply_item, list);
        this.mContext = context;
    }

    @Override
    protected void convert(final BaseViewHolder helper, NoteSubComment item) {
        Logger.i("image url --->" + item.getRepeatUserimg());
        RequestOptions options = new RequestOptions();
        options.error(R.mipmap.head_def);
        options.placeholder(R.mipmap.empty_icon);
        Glide.with(mContext).load(item.getRepeatUserimg()).apply(options).into((ImageView) helper.itemView.findViewById(R.id.iv_user_head));

        helper.setText(R.id.tv_nick_name, item.getRepeatNickname())
                .setText(R.id.tv_comment_date, TimeUtils.millis2String(item.getAddTime() != null ? item.getAddTime() * 1000 : 0))
                .setText(R.id.btn_reply_count, "回复")
                .setText(R.id.tv_comment_content, item.getRepeatContent());

        LinearLayout oldLayout = helper.itemView.findViewById(R.id.layout_old_content);

        if (StringUtils.isEmpty(item.getOldContent())) {
            helper.setVisible(R.id.layout_old_content, false);
            oldLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0));
        } else {
            helper.setVisible(R.id.layout_old_content, true);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(SizeUtils.dp2px(60), 0, 0, 0);
            oldLayout.setLayoutParams(params);

            TextView replyTv = helper.itemView.findViewById(R.id.tv_old_content);
            if (!StringUtils.isEmpty(item.getOldNickname())) {
                SpannableString tempStr = new SpannableString(item.getOldNickname() + "：" + item.getOldContent());
                tempStr.setSpan(new MyClickText(mContext), 0, item.getOldNickname().length() + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                //当然这里也可以通过setSpan来设置哪些位置的文本哪些颜色
                replyTv.setText(tempStr);
                replyTv.setMovementMethod(LinkMovementMethod.getInstance());//不设置 没有点击事件
                replyTv.setHighlightColor(Color.TRANSPARENT); //设置点击后的颜色为透明
            }
        }
    }
}