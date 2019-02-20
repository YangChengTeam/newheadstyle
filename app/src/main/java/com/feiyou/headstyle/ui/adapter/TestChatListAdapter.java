package com.feiyou.headstyle.ui.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.blankj.utilcode.util.StringUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.AnswerInfo;
import com.feiyou.headstyle.bean.StickerTypeInfo;
import com.feiyou.headstyle.bean.TestMsgInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2018/1/8.
 */

public class TestChatListAdapter extends BaseQuickAdapter<TestMsgInfo, BaseViewHolder> {

    private Context mContext;

    int lastIndex = -1;

    public TestChatListAdapter(Context context, List<TestMsgInfo> datas) {
        super(R.layout.msg_item, datas);
        this.mContext = context;
    }

    public interface AnswerItemClick {
        void answerClick(String itemValue);
    }

    public AnswerItemClick answerItemClick;

    public void setAnswerItemClick(AnswerItemClick answerItemClick) {
        this.answerItemClick = answerItemClick;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final TestMsgInfo item) {

        switch (item.getType()) {
            case TestMsgInfo.TYPE_RECEIVED:
                helper.setVisible(R.id.left_layout, true);

                if (!StringUtils.isEmpty(item.getImgUrl())) {
                    helper.setVisible(R.id.layout_left_img, true);
                    Glide.with(mContext).load(item.getImgUrl()).into((ImageView) helper.itemView.findViewById(R.id.left_img));
                }
                if (item.getAnswer() != null && item.getAnswer().size() > 0) {

                    List<AnswerInfo> answerList = new ArrayList<>();

                    for (int i = 0; i < item.getAnswer().size(); i++) {
                        AnswerInfo answerInfo = new AnswerInfo();
                        answerInfo.setAnswerName(item.getAnswer().get(i));
                        answerList.add(answerInfo);
                    }
                    helper.setVisible(R.id.layout_answer, true);
                    AnswerAdapter answerAdapter = new AnswerAdapter(mContext, answerList);
                    RecyclerView recyclerView = helper.itemView.findViewById(R.id.answer_list);
                    recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
                    recyclerView.setAdapter(answerAdapter);

                    answerAdapter.setOnItemClickListener(new OnItemClickListener() {
                        @Override
                        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                            if (position != lastIndex) {
                                answerAdapter.getData().get(position).setSelected(true);
                                if (lastIndex > -1) {
                                    answerAdapter.getData().get(lastIndex).setSelected(false);
                                }
                                lastIndex = position;
                                answerAdapter.notifyDataSetChanged();
                                answerItemClick.answerClick(answerAdapter.getItem(position).getAnswerName());
                            }
                        }
                    });
                }

                helper.setVisible(R.id.right_layout, false);
                helper.setText(R.id.left_msg, item.getContent());
                break;
            case TestMsgInfo.TYPE_RECEIVED_IMAGE:
                helper.setVisible(R.id.left_layout, true);
                helper.setVisible(R.id.iv_left_head, true);
                helper.setVisible(R.id.left_msg, false);
                helper.setVisible(R.id.left_img, true);
                helper.setVisible(R.id.right_layout, false);
                break;
            case TestMsgInfo.TYPE_SENT:
                helper.setVisible(R.id.left_layout, false);
                helper.setVisible(R.id.right_layout, true);
                helper.setText(R.id.right_msg, item.getContent());
                break;
            default:
                break;
        }

    }
}