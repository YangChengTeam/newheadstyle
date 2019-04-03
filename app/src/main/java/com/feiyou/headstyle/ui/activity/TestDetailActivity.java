package com.feiyou.headstyle.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.feiyou.headstyle.App;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.QuestionJumpInfo;
import com.feiyou.headstyle.bean.ResultInfo;
import com.feiyou.headstyle.bean.TestDetailInfoRet;
import com.feiyou.headstyle.bean.TestDetailInfoWrapper;
import com.feiyou.headstyle.bean.TestMsgInfo;
import com.feiyou.headstyle.bean.TestResultInfoRet;
import com.feiyou.headstyle.bean.TestResultParams;
import com.feiyou.headstyle.bean.UserInfo;
import com.feiyou.headstyle.common.Constants;
import com.feiyou.headstyle.presenter.TestDetailInfoPresenterImp;
import com.feiyou.headstyle.presenter.TestResultInfoPresenterImp;
import com.feiyou.headstyle.ui.adapter.MsgAdapter;
import com.feiyou.headstyle.ui.adapter.StickerTypeAdapter;
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
public class TestDetailActivity extends BaseFragmentActivity implements TestDetailInfoView, TestChatListAdapter.AnswerItemClick {
    @BindView(R.id.topbar)
    QMUITopBar mTopBar;

    ImageView mBackImageView;

    @BindView(R.id.chat_list)
    RecyclerView mChatListView;

    @BindView(R.id.layout_comment)
    LinearLayout mCommentLayout;

    @BindView(R.id.tv_comment)
    TextView mCommentTextView;

    private TestChatListAdapter chatListAdapter;

    private TestDetailInfoPresenterImp testDetailInfoPresenterImp;

    private TestResultInfoPresenterImp testResultInfoPresenterImp;

    private List<List<String>> answer;

    private List<String> question;

    private List<QuestionJumpInfo> jump;

    private int currentSubjectIndex;

    private boolean isLastSubject;

    private ProgressDialog progressDialog = null;

    private String selectResultIndex;

    private String tid;

    private UserInfo userInfo;

    TextView titleTv;

    @Override
    protected int getContextViewId() {
        return R.layout.activity_test_detail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTopBar();
        initData();
    }

    private void initTopBar() {
        QMUIStatusBarHelper.setStatusBarLightMode(this);
        View topSearchView = getLayoutInflater().inflate(R.layout.common_top_back, null);
        topSearchView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(48)));
        titleTv = topSearchView.findViewById(R.id.tv_title);
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

        Bundle bundle = getIntent().getExtras();
        if (bundle != null && !StringUtils.isEmpty(bundle.getString("tid"))) {
            tid = bundle.getString("tid");
        }
        userInfo = App.getApp().getmUserInfo();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在提交");
        progressDialog.setCanceledOnTouchOutside(false);

        chatListAdapter = new TestChatListAdapter(this, null);
        mChatListView.setLayoutManager(new LinearLayoutManager(this));
        mChatListView.setAdapter(chatListAdapter);
        chatListAdapter.setAnswerItemClick(this);

        testDetailInfoPresenterImp = new TestDetailInfoPresenterImp(this, this);
        testResultInfoPresenterImp = new TestResultInfoPresenterImp(this, this);

        testDetailInfoPresenterImp.getTestDetail(tid, 1);
    }

    @OnClick(R.id.layout_comment)
    void commentEvent() {
        if (isLastSubject) {
            if (progressDialog != null && !progressDialog.isShowing()) {
                progressDialog.show();
            }

            TestResultParams params = new TestResultParams();
            params.setId(tid);
            params.setTestType("1");
            params.setNickname(userInfo != null ? userInfo.getNickname() : "火星用户");
            params.setHeadimg(userInfo != null ? userInfo.getUserimg() : "");
            params.setResultId(selectResultIndex);
            params.setSex(userInfo != null ? userInfo.getSex() : 1);
            params.setUserId(userInfo != null ? userInfo.getId() : "0");

            testResultInfoPresenterImp.createImage(params);
        } else {
            mCommentLayout.setVisibility(View.GONE);
            TestMsgInfo startInfo = new TestMsgInfo("", "开始测试", TestMsgInfo.TYPE_SENT);
            startInfo.setImgUrl(userInfo != null ? userInfo.getUserimg() : "");
            chatListAdapter.addData(startInfo);
            chatListAdapter.notifyItemInserted(chatListAdapter.getData().size() - 1);
            mChatListView.scrollToPosition(chatListAdapter.getData().size() - 1);
            showSubject();
        }
    }

    void showSubject() {
        TestMsgInfo testMsgInfo = new TestMsgInfo();
        testMsgInfo.setType(TestMsgInfo.TYPE_RECEIVED);
        testMsgInfo.setImgUrl("");
        testMsgInfo.setAnswer(answer.get(currentSubjectIndex));

        testMsgInfo.setContent(question.get(currentSubjectIndex));
        chatListAdapter.addData(testMsgInfo);
    }

    @Override
    public void answerClick(int pos, String answerName) {

        if (!isLastSubject) {
            //回复选择的答案
            TestMsgInfo answerInfo = new TestMsgInfo();
            answerInfo.setType(TestMsgInfo.TYPE_SENT);
            answerInfo.setImgUrl(userInfo != null ? userInfo.getUserimg() : "");
            answerInfo.setContent(answerName);
            chatListAdapter.addData(answerInfo);

            //回复答案后继续下一题
            if (jump != null) {
                if (jump.get(currentSubjectIndex).getJumpType() != 2) {
                    currentSubjectIndex = Integer.parseInt(jump.get(currentSubjectIndex).getJumpQuestion()[pos]) - 1;
                    Logger.i("currentSubjectIndex--->" + currentSubjectIndex);

                    if (currentSubjectIndex < question.size()) {
                        showSubject();
                    }
                } else {
                    isLastSubject = true;

                    selectResultIndex = jump.get(currentSubjectIndex).getJumpAnswer()[pos];
                    mCommentLayout.setVisibility(View.VISIBLE);
                    mCommentTextView.setText("提交");
                }
            }
        } else {
            selectResultIndex = jump.get(currentSubjectIndex).getJumpAnswer()[pos];
            //ToastUtils.showLong("最后一题,选择的答案是--->" + selectResultIndex);
            //回复选择的答案
            TestMsgInfo answerInfo = new TestMsgInfo();
            answerInfo.setType(TestMsgInfo.TYPE_SENT);
            answerInfo.setImgUrl(userInfo != null ? userInfo.getUserimg() : "");
            answerInfo.setContent(answerName);
            chatListAdapter.addData(answerInfo);

            mCommentLayout.setVisibility(View.VISIBLE);
            mCommentTextView.setText("提交");
        }

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
                    //设置全局的测试信息
                    TestDetailInfoWrapper testInfo = ((TestDetailInfoRet) tData).getData();
                    testInfo.setTestId(tid);
                    App.getApp().setTestInfo(testInfo);

                    String title = StringUtils.isEmpty(((TestDetailInfoRet) tData).getData().getTitle()) ? "测试详情" : ((TestDetailInfoRet) tData).getData().getTitle();
                    titleTv.setText(title);
                    TestMsgInfo guideInfo = new TestMsgInfo(((TestDetailInfoRet) tData).getData().getImage(), ((TestDetailInfoRet) tData).getData().getDesc(), TestMsgInfo.TYPE_RECEIVED);
                    chatListAdapter.addData(guideInfo);
                    chatListAdapter.notifyDataSetChanged();

                    if (((TestDetailInfoRet) tData).getData().getList() != null) {
                        question = ((TestDetailInfoRet) tData).getData().getList().getQuestion();
                        answer = ((TestDetailInfoRet) tData).getData().getList().getAnswer();
                        jump = ((TestDetailInfoRet) tData).getData().getList().getJump();
                    }
                }
            }
            if (tData instanceof TestResultInfoRet) {
                if (((TestResultInfoRet) tData).getData() != null) {
                    Intent intent = new Intent(this, TestResultActivity.class);
                    intent.putExtra("from_type", 1);
                    intent.putExtra("image_url", ((TestResultInfoRet) tData).getData().getImage());
                    intent.putExtra("nocode_image_url", ((TestResultInfoRet) tData).getData().getImageNocode());
                    startActivity(intent);
                    finish();
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
