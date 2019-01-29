package com.feiyou.headstyle.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.CommentReplyInfo;
import com.feiyou.headstyle.bean.NoteItem;

import java.util.List;

/**
 * Created by myflying on 2018/12/25.
 */
public class CommentReplyAdapter extends BaseMultiItemQuickAdapter<NoteItem.NoteComment, BaseViewHolder> {

    private Context mContext;

    private List<NoteItem.NoteRepeat> replyInfos;

    public void setReplyInfos(List<NoteItem.NoteRepeat> replyInfos) {
        this.replyInfos = replyInfos;
    }

    public CommentReplyAdapter(Context context, List<NoteItem.NoteComment> list) {
        super(list);
        addItemType(NoteItem.NoteComment.TYPE_COMMENT, R.layout.comment_normal_item);
        addItemType(NoteItem.NoteComment.TYPE_REPEAT, R.layout.comment_reply_item);
    }

    @Override
    protected void convert(final BaseViewHolder helper, NoteItem.NoteComment item) {
        switch (helper.getItemViewType()) {
            case NoteItem.NoteComment.TYPE_COMMENT:
                helper.setText(R.id.tv_comment_content, item.getContent());
                break;
            case NoteItem.NoteComment.TYPE_REPEAT:
                helper.setText(R.id.tv_nick_name,item.getNickname())
                        .setText(R.id.tv_repeat_nick_name,item.getRepeatNickName())
                        .setText(R.id.tv_repeat_content,item.getContent());
                break;
        }
    }
}