package com.feiyou.headstyle.ui.adapter;

import android.content.Context;
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
                .setText(R.id.btn_task_state, item.getButton());
        Glide.with(mContext).load(item.getIco()).into((ImageView) helper.getView(R.id.iv_task_icon));
    }
}