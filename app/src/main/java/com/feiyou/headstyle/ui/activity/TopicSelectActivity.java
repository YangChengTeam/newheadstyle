package com.feiyou.headstyle.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feiyou.headstyle.App;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.MessageEvent;
import com.feiyou.headstyle.bean.TopicInfo;
import com.feiyou.headstyle.ui.adapter.BlackListAdapter;
import com.feiyou.headstyle.ui.adapter.TopicSelectListAdapter;
import com.feiyou.headstyle.ui.base.BaseFragmentActivity;
import com.feiyou.headstyle.ui.custom.NormalDecoration;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by myflying on 2018/11/23.
 */
public class TopicSelectActivity extends BaseFragmentActivity {

    @BindView(R.id.topbar)
    QMUITopBar mTopBar;

    ImageView mBackImageView;

    @BindView(R.id.topic_list)
    RecyclerView mTopicListView;

    TopicSelectListAdapter topicSelectListAdapter;

    private int defTopicIndex = -1;

    private int lastPosition = -1;

    @Override
    protected int getContextViewId() {
        return R.layout.activity_topic_select_list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTopBar();
        initData();
    }

    private void initTopBar() {
        QMUIStatusBarHelper.setStatusBarLightMode(this);

        View topicView = getLayoutInflater().inflate(R.layout.common_top_config, null);
        mTopBar.setCenterView(topicView);
        topicView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(48)));
        TextView titleTv = topicView.findViewById(R.id.tv_config_title);
        titleTv.setText("话题选择");

        TextView config = topicView.findViewById(R.id.tv_config);
        config.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lastPosition == -1) {
                    ToastUtils.showLong("请选择话题");
                    return;
                }

                Intent intent = new Intent();
                intent.putExtra("topic_select_index", lastPosition);
                intent.putExtra("topic_id", topicSelectListAdapter.getData().get(lastPosition).getId());
                intent.putExtra("topic_name", topicSelectListAdapter.getData().get(lastPosition).getName());
                setResult(PushNoteActivity.RESULT_TOPIC_CODE, intent);

                finish();
            }
        });

        mBackImageView = topicView.findViewById(R.id.iv_back);
        mBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });
    }

    public void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.getInt("topic_select_index") > -1) {
            defTopicIndex = bundle.getInt("topic_select_index");
        }
        List<TopicInfo> topicInfoList = null;
        if (App.topicInfoList != null && App.topicInfoList.size() > 0) {
            //重置选中状态
            topicInfoList = App.topicInfoList;
            for (TopicInfo item : topicInfoList) {
                item.setSelected(false);
            }
        }

        topicSelectListAdapter = new TopicSelectListAdapter(this, topicInfoList);
        mTopicListView.setLayoutManager(new LinearLayoutManager(this));
        mTopicListView.addItemDecoration(new NormalDecoration(ContextCompat.getColor(this, R.color.line_color), 1));
        mTopicListView.setAdapter(topicSelectListAdapter);

        topicSelectListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (lastPosition == position) {
                    return;
                }
                if (lastPosition > -1) {
                    topicSelectListAdapter.getData().get(lastPosition).setSelected(false);
                }
                topicSelectListAdapter.getData().get(position).setSelected(true);
                topicSelectListAdapter.notifyDataSetChanged();
                lastPosition = position;
            }
        });

        if (defTopicIndex > -1) {
            topicSelectListAdapter.getData().get(defTopicIndex).setSelected(true);
            lastPosition = defTopicIndex;
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(MessageEvent messageEvent) {
        if (messageEvent.getMessage().equals("topic_list")) {
            topicSelectListAdapter.setNewData(App.topicInfoList);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onBackPressed() {
        popBackStack();
    }
}
