package com.feiyou.headstyle.ui.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.blankj.utilcode.util.TimeUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.NoteItem;
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
        Logger.i("image url --->" + item.getCommentUserimg());
        RequestOptions options = new RequestOptions();
        options.error(R.mipmap.head_def);
        options.placeholder(R.mipmap.empty_icon);
        Glide.with(mContext).load(item.getCommentUserimg()).apply(options).into((ImageView) helper.itemView.findViewById(R.id.iv_user_head));

        helper.setText(R.id.tv_nick_name, item.getCommentNickname())
                .setText(R.id.tv_comment_date, TimeUtils.millis2String(item.getAddTime() != null ? item.getAddTime() * 1000 : 0))
                .setText(R.id.btn_reply_count, item.getListNum() + " 回复")
                .setText(R.id.tv_comment_content, item.getCommentContent());

        /*List<NoteItem.NoteComment> tempComments = item.getComment();
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
        });*/


    }
}