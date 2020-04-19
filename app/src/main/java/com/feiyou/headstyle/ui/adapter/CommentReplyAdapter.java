package com.feiyou.headstyle.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import androidx.core.content.ContextCompat;
import android.text.Html;
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
import com.feiyou.headstyle.ui.custom.GlideRoundTransform;
import com.feiyou.headstyle.utils.MyTimeUtil;
import com.feiyou.headstyle.view.MyClickText;
import com.orhanobut.logger.Logger;

import java.util.Date;
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
        RequestOptions options = new RequestOptions().skipMemoryCache(true);
        options.transform(new GlideRoundTransform(mContext, 18));
        options.placeholder(R.mipmap.image_def);
        Glide.with(mContext).load(item.getRepeatUserimg()).apply(options).into((ImageView) helper.itemView.findViewById(R.id.iv_user_head));

        Date currentDate = TimeUtils.millis2Date(item.getAddTime() != null ? item.getAddTime() * 1000 : 0);
        String tempDateStr = MyTimeUtil.isOutMouth(currentDate) ? TimeUtils.millis2String(item.getAddTime() != null ? item.getAddTime() * 1000 : 0) : MyTimeUtil.getTimeFormatText(currentDate);

        helper.setText(R.id.tv_nick_name, item.getRepeatNickname())
                .setText(R.id.tv_comment_date, tempDateStr)
                .setText(R.id.btn_reply_count, "回复")
                .setText(R.id.tv_comment_content, StringUtils.isEmpty(item.getRepeatContent()) ? "" : Html.fromHtml(item.getRepeatContent().replace("\n", "<br>")))
                .setText(R.id.tv_is_zan, item.getZanNum() + "");

        TextView isZanTv = helper.itemView.findViewById(R.id.tv_is_zan);
        Drawable isZan = ContextCompat.getDrawable(mContext, R.mipmap.is_zan);
        Drawable notZan = ContextCompat.getDrawable(mContext, R.mipmap.note_zan);

        if (item.getIsZan() == 0) {
            isZanTv.setCompoundDrawablesWithIntrinsicBounds(notZan, null, null, null);
        } else {
            isZanTv.setCompoundDrawablesWithIntrinsicBounds(isZan, null, null, null);
        }
        helper.addOnClickListener(R.id.layout_zan);
        helper.addOnClickListener(R.id.btn_reply_count).addOnClickListener(R.id.iv_user_head);

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
                SpannableString tempStr = new SpannableString(item.getOldNickname() + "：" + item.getOldContent().replace("\n", "<br>"));
                tempStr.setSpan(new MyClickText(mContext), 0, item.getOldNickname().length() + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                //当然这里也可以通过setSpan来设置哪些位置的文本哪些颜色
                replyTv.setText(tempStr);
                replyTv.setMovementMethod(LinkMovementMethod.getInstance());//不设置 没有点击事件
                replyTv.setHighlightColor(Color.TRANSPARENT); //设置点击后的颜色为透明
            }
        }

        if (item.getRepeatUserId().equals("1")) {
            helper.setVisible(R.id.iv_system_user, true);
        } else {
            helper.setVisible(R.id.iv_system_user, false);
        }
    }
}