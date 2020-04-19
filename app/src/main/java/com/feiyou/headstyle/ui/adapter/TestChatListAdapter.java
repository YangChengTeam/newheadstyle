package com.feiyou.headstyle.ui.adapter;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.AnswerInfo;
import com.feiyou.headstyle.bean.TestMsgInfo;
import com.feiyou.headstyle.ui.custom.GlideRoundTransform;

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
        void answerClick(int pos, String itemValue);
    }

    public AnswerItemClick answerItemClick;

    public void setAnswerItemClick(AnswerItemClick answerItemClick) {
        this.answerItemClick = answerItemClick;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final TestMsgInfo item) {

        if ((helper.getAdapterPosition() + 1) % 2 == 0) {
            LinearLayout.LayoutParams itemParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(60));
            helper.itemView.setLayoutParams(itemParams);
        } else {
            LinearLayout.LayoutParams itemParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            itemParams.setMargins(0, 00, 0, SizeUtils.dp2px(10));
            helper.itemView.setLayoutParams(itemParams);
        }

        LinearLayout leftImageLayout = helper.itemView.findViewById(R.id.layout_left_img);

        switch (item.getType()) {
            case TestMsgInfo.TYPE_RECEIVED:
                helper.setVisible(R.id.left_layout, true);

                if (!StringUtils.isEmpty(item.getImgUrl())) {
                    leftImageLayout.setVisibility(View.VISIBLE);
                    Glide.with(mContext).load(item.getImgUrl()).into((ImageView) helper.itemView.findViewById(R.id.left_img));
                } else {
                    leftImageLayout.setVisibility(View.GONE);
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
                    RecyclerView answerListView = helper.itemView.findViewById(R.id.answer_list);
                    answerListView.setLayoutManager(new LinearLayoutManager(mContext));
                    answerListView.setAdapter(answerAdapter);

                    answerAdapter.setOnItemClickListener(new OnItemClickListener() {
                        @Override
                        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                            if (item.isClickable()) {
                                answerAdapter.getData().get(position).setSelected(true);
                                answerAdapter.notifyItemChanged(position);
                                item.setClickable(false);
                                answerItemClick.answerClick(position, answerAdapter.getItem(position).getAnswerName());
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

                RequestOptions options = new RequestOptions();
                options.transform(new GlideRoundTransform(mContext, 24));
                Glide.with(mContext).load(item.getImgUrl()).apply(options).into((ImageView) helper.getView(R.id.iv_user_head));
                break;
            default:
                break;
        }

    }
}