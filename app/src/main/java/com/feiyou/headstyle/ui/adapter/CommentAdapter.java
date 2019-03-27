package com.feiyou.headstyle.ui.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.NoteItem;
import com.feiyou.headstyle.ui.custom.GlideRoundTransform;
import com.feiyou.headstyle.ui.custom.RoundedCornersTransformation;
import com.orhanobut.logger.Logger;

import java.util.List;

/**
 * Created by myflying on 2018/12/25.
 */
public class CommentAdapter extends BaseQuickAdapter<NoteItem, BaseViewHolder> {

    private Context mContext;

    public CommentAdapter(Context context, List<NoteItem> list) {
        super(R.layout.comment_item, list);
        this.mContext = context;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final NoteItem item) {
        if (item != null) {
            RequestOptions options = new RequestOptions();
            options.error(R.mipmap.head_def);
            options.placeholder(R.mipmap.empty_icon);
            options.transform(new RoundedCornersTransformation(SizeUtils.dp2px(18), 0));
            Glide.with(mContext).load(item.getCommentUserimg()).apply(options).into((ImageView) helper.itemView.findViewById(R.id.iv_user_head));

            helper.setText(R.id.tv_nick_name, item.getCommentNickname())
                    .setText(R.id.tv_comment_date, TimeUtils.millis2String(item.getAddTime() != null ? item.getAddTime() * 1000 : 0))
                    .setText(R.id.tv_comment_content, Html.fromHtml(item.getCommentContent()))
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

            isZanTv.setCompoundDrawablePadding(SizeUtils.dp2px(4));

            helper.setText(R.id.btn_reply_count, item.getListNum() > 0 ? item.getListNum() + "条回复>" : "回复");
            helper.addOnClickListener(R.id.btn_reply_count).addOnClickListener(R.id.iv_user_head);
        }
    }
}