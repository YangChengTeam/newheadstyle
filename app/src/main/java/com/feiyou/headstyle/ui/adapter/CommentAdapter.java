package com.feiyou.headstyle.ui.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.NoteItem;

import java.util.ArrayList;
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
        helper.setText(R.id.tv_comment_content, item.getCommentContent());
        List<NoteItem.NoteComment> tempComments = item.getComment();

        List<NoteItem.NoteComment> results = new ArrayList<>();

        for (int i = 0; i < tempComments.size(); i++) {
            NoteItem.NoteComment noteComment = tempComments.get(i);
            noteComment.setItemType(NoteItem.NoteComment.TYPE_COMMENT);
            results.add(noteComment);
            if (noteComment.getRepeat() != null) {

                for (int j = 0; j < noteComment.getRepeat().size(); j++) {
                    NoteItem tempItem = new NoteItem();
                    NoteItem.NoteComment repeatComment = tempItem.new NoteComment(NoteItem.NoteComment.TYPE_REPEAT);
                    repeatComment.setNickname(noteComment.getRepeat().get(j).getNickname());
                    repeatComment.setRepeatNickName(noteComment.getRepeat().get(j).getRepeatNickname());
                    repeatComment.setContent(noteComment.getRepeat().get(j).getContent());

                    results.add(repeatComment);
                }
            }
        }

        RecyclerView mCommentReplyListView = helper.itemView.findViewById(R.id.comment_reply_list);
        mCommentReplyListView.setLayoutManager(new LinearLayoutManager(mContext));
        final CommentReplyAdapter commentReplyAdapter = new CommentReplyAdapter(mContext, results);
        mCommentReplyListView.setAdapter(commentReplyAdapter);

        LinearLayout moreLayout = helper.itemView.findViewById(R.id.layout_more);
        moreLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //commentReplyAdapter.setReplyInfos(item.getReplyList());
                //commentReplyAdapter.notifyDataSetChanged();
            }
        });
    }
}