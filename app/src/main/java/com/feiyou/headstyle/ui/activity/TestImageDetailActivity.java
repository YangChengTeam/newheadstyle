package com.feiyou.headstyle.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.QuestionJumpInfo;
import com.feiyou.headstyle.bean.ResultInfo;
import com.feiyou.headstyle.bean.TestDetailInfoRet;
import com.feiyou.headstyle.bean.TestMsgInfo;
import com.feiyou.headstyle.bean.TestResultInfoRet;
import com.feiyou.headstyle.bean.TestResultParams;
import com.feiyou.headstyle.common.Constants;
import com.feiyou.headstyle.presenter.TestDetailInfoPresenterImp;
import com.feiyou.headstyle.presenter.TestResultInfoPresenterImp;
import com.feiyou.headstyle.ui.adapter.TestChatImageListAdapter;
import com.feiyou.headstyle.ui.adapter.TestChatListAdapter;
import com.feiyou.headstyle.ui.base.BaseFragmentActivity;
import com.feiyou.headstyle.view.TestDetailInfoView;
import com.orhanobut.logger.Logger;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by myflying on 2019/2/20.
 */
public class TestImageDetailActivity extends BaseFragmentActivity implements TestDetailInfoView, TestChatImageListAdapter.AnswerItemClick {
    @BindView(R.id.topbar)
    QMUITopBar mTopBar;

    ImageView mBackImageView;

    @BindView(R.id.chat_list)
    RecyclerView mChatListView;

    @BindView(R.id.layout_comment)
    LinearLayout mCommentLayout;

    @BindView(R.id.tv_comment)
    TextView mCommentTextView;

    private TestChatImageListAdapter chatListAdapter;

    private List<TestMsgInfo> msgList = new ArrayList<>();

    private TestDetailInfoPresenterImp testDetailInfoPresenterImp;

    private TestResultInfoPresenterImp testResultInfoPresenterImp;

    private ProgressDialog progressDialog = null;

    private boolean isShowSex;

    private boolean isOver;

    @Override
    protected int getContextViewId() {
        return R.layout.activity_test_image_detail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTopBar();
        initData();
    }

    private void initTopBar() {
        QMUIStatusBarHelper.setStatusBarLightMode(this);
        mTopBar.setTitle(getResources().getString(R.string.app_name));
        View topSearchView = getLayoutInflater().inflate(R.layout.common_top_back, null);
        topSearchView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(48)));
        TextView titleTv = topSearchView.findViewById(R.id.tv_title);
        titleTv.setText("测试详情");

        mTopBar.setCenterView(topSearchView);
        mBackImageView = topSearchView.findViewById(R.id.iv_back);
        mBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });
    }

    public void initData() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在提交");

        chatListAdapter = new TestChatImageListAdapter(this, null);
        mChatListView.setLayoutManager(new LinearLayoutManager(this));
        mChatListView.setAdapter(chatListAdapter);
        chatListAdapter.setAnswerItemClick(this);

        testDetailInfoPresenterImp = new TestDetailInfoPresenterImp(this, this);
        testResultInfoPresenterImp = new TestResultInfoPresenterImp(this, this);

        testDetailInfoPresenterImp.getTestDetail("164", 2);
    }

    @OnClick(R.id.layout_comment)
    void commentEvent() {
        if (isOver) {
            if (progressDialog != null && !progressDialog.isShowing()) {
                progressDialog.show();
            }

            TestResultParams params = new TestResultParams();
            params.setId("122");
            params.setTestType("1");
            params.setNickname("我是忍者");
            params.setHeadimg("http://thirdwx.qlogo.cn/mmopen/vi_32/g8lk9icgk6QfZLib2awxgnibnU4RTeRzobJNWc3ZxziabI0CncNfgUQG1godEgqGI3wfqqqSCr4kAlv9LOKiad2NEFw/132");
            params.setSex("0");
            params.setUserId("1021601");

            testResultInfoPresenterImp.createImage(params);
        } else {
            mCommentLayout.setVisibility(View.GONE);
            TestMsgInfo startInfo = new TestMsgInfo("", "开始测试", TestMsgInfo.TYPE_SENT);
            chatListAdapter.addData(startInfo);
            chatListAdapter.notifyItemInserted(chatListAdapter.getData().size() - 1);
            mChatListView.scrollToPosition(chatListAdapter.getData().size() - 1);
            showSubject();
        }
    }

    void showSubject() {
        TestMsgInfo testMsgInfo = new TestMsgInfo();
        testMsgInfo.setType(TestMsgInfo.TYPE_RECEIVED);
        testMsgInfo.setContent(isShowSex ? "请告诉我你的性别?" : "请告诉我你的名字");
        chatListAdapter.addData(testMsgInfo);

        chatListAdapter.notifyItemInserted(chatListAdapter.getData().size() - 1);
        mChatListView.scrollToPosition(chatListAdapter.getData().size() - 1);
    }

    @Override
    public void answerClick(int pos, String answerName) {

        //回复选择的答案
        TestMsgInfo answerInfo = new TestMsgInfo();
        answerInfo.setType(TestMsgInfo.TYPE_SENT);
        answerInfo.setContent(answerName);
        chatListAdapter.addData(answerInfo);

        mCommentLayout.setVisibility(View.VISIBLE);
        mCommentTextView.setText("提交");

        //刷新列表页面
        chatListAdapter.notifyItemInserted(chatListAdapter.getData().size() - 1);
        mChatListView.scrollToPosition(chatListAdapter.getData().size() - 1);
    }

    @Override
    public void onBackPressed() {
        popBackStack();
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void dismissProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void loadDataSuccess(ResultInfo tData) {
        Logger.i("detail--->" + JSONObject.toJSONString(tData));

        if (tData != null && tData.getCode() == Constants.SUCCESS) {
            if (tData instanceof TestDetailInfoRet) {
                if (((TestDetailInfoRet) tData).getData() != null) {
                    TestMsgInfo guideInfo = new TestMsgInfo(((TestDetailInfoRet) tData).getData().getImage(), ((TestDetailInfoRet) tData).getData().getDesc(), TestMsgInfo.TYPE_RECEIVED);
                    chatListAdapter.addData(guideInfo);
                    chatListAdapter.notifyDataSetChanged();
                    isShowSex = ((TestDetailInfoRet) tData).getData().getSex() == 1 ? true : false;
                }
            }
            if (tData instanceof TestResultInfoRet) {
                if (((TestResultInfoRet) tData).getData() != null) {
                    Intent intent = new Intent(this, TestResultActivity.class);
                    intent.putExtra("image_url", ((TestResultInfoRet) tData).getData().getImage());
                    intent.putExtra("nocode_image_url", ((TestResultInfoRet) tData).getData().getImageNocode());
                    startActivity(intent);
                }
            }
        }
    }

    @Override
    public void loadDataError(Throwable throwable) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
