package com.feiyou.headstyle.ui.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.TaskInfo;

import java.util.List;

/**
 * Created by admin on 2018/1/8.
 */

public class TaskListAdapter extends BaseQuickAdapter<TaskInfo, BaseViewHolder> {

    private Context mContext;

    public TaskListAdapter(Context context, List<TaskInfo> datas) {
        super(R.layout.task_item_view, datas);
        this.mContext = context;
    }

    @Override
    protected void convert(final BaseViewHolder helper, TaskInfo item) {
        helper.setText(R.id.tv_task_title, item.getTitle())
                .setText(R.id.tv_task_remark, item.getContent())
                .setText(R.id.tv_add_gold_num, "+" + item.getGoldnum())
                .setText(R.id.btn_task_state, item.getIsFinish() == 1 ? "已完成" : item.getButton());
        Glide.with(mContext).load(item.getIco()).into((ImageView) helper.getView(R.id.iv_task_icon));

        helper.setBackgroundRes(R.id.btn_task_state, item.getIsFinish() == 1 ? R.drawable.task_done_btn_bg : R.drawable.task_btn_bg);
        helper.setTextColor(R.id.btn_task_state, ContextCompat.getColor(mContext, item.getIsFinish() == 1 ? R.color.black3 : R.color.task_border_color));
        helper.setVisible(R.id.tv_add_gold_num, item.getId() == 7 ? false : true);
    }
}