package com.feiyou.headstyle.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feiyou.headstyle.App;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.ResultInfo;
import com.feiyou.headstyle.bean.TaskRecordInfoRet;
import com.feiyou.headstyle.bean.TestDetailInfoRet;
import com.feiyou.headstyle.bean.TestDetailInfoWrapper;
import com.feiyou.headstyle.bean.TestMsgInfo;
import com.feiyou.headstyle.bean.TestResultInfoRet;
import com.feiyou.headstyle.bean.TestResultParams;
import com.feiyou.headstyle.bean.UserInfo;
import com.feiyou.headstyle.common.Constants;
import com.feiyou.headstyle.presenter.TaskRecordInfoPresenterImp;
import com.feiyou.headstyle.presenter.TestDetailInfoPresenterImp;
import com.feiyou.headstyle.presenter.TestResultInfoPresenterImp;
import com.feiyou.headstyle.ui.adapter.TestChatImageListAdapter;
import com.feiyou.headstyle.ui.base.BaseFragmentActivity;
import com.feiyou.headstyle.view.TestDetailInfoView;
import com.jakewharton.rxbinding.view.RxView;
import com.orhanobut.logger.Logger;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import rx.functions.Action1;

/**
 * Created by myflying on 2019/2/20.
 */
public class TestImageDetailActivity extends BaseFragmentActivity implements TestDetailInfoView {
    @BindView(R.id.topbar)
    QMUITopBar mTopBar;

    ImageView mBackImageView;

    @BindView(R.id.chat_list)
    RecyclerView mChatListView;

    @BindView(R.id.layout_comment)
    LinearLayout mCommentLayout;

    @BindView(R.id.layout_input)
    LinearLayout mInputLayout;

    @BindView(R.id.layout_input_user_name)
    LinearLayout mInputUserNameLayout;

    @BindView(R.id.tv_comment)
    TextView mCommentTextView;

    @BindView(R.id.rg_sex_all)
    RadioGroup mRadioGroup;

    @BindView(R.id.rb_sex_boy)
    RadioButton mRbBoy;

    @BindView(R.id.rb_sex_girl)
    RadioButton mRbGirl;

    @BindView(R.id.layout_config)
    LinearLayout mConfigLayout;

    @BindView(R.id.et_input_user_name)
    EditText mInputUserNameEt;

    private TestChatImageListAdapter chatListAdapter;

    private List<TestMsgInfo> msgList = new ArrayList<>();

    private TestDetailInfoPresenterImp testDetailInfoPresenterImp;

    private TestResultInfoPresenterImp testResultInfoPresenterImp;

    private ProgressDialog progressDialog = null;

    private boolean isShowSex;

    private boolean isOver;

    private String selectSexValue;

    private int selectSexKey;

    private int inputStep = 1; //1表示在选择性别，2表示在输入姓名

    private String tid;

    private UserInfo userInfo;

    TextView titleTv;

    TestMsgInfo resultMsgInfo;

    private String taskId = "5";

    private int goldNum = 0;

    TaskRecordInfoPresenterImp taskRecordInfoPresenterImp;

    private boolean isAddTaskRecord;

    private String recordId;

    private int isFromTask = 0;

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
        MobclickAgent.onEvent(this, "test_start", AppUtils.getAppVersionName());

        Bundle bundle = getIntent().getExtras();
        if (bundle != null && !StringUtils.isEmpty(bundle.getString("tid"))) {
            tid = bundle.getString("tid");
            isFromTask = bundle.getInt("is_from_task", 0);
        }
        userInfo = App.getApp().getmUserInfo();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在提交");
        progressDialog.setCanceledOnTouchOutside(false);

        chatListAdapter = new TestChatImageListAdapter(this, null);
        mChatListView.setLayoutManager(new LinearLayoutManager(this));
        mChatListView.setAdapter(chatListAdapter);

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkId) {
                if (checkId == R.id.rb_sex_boy) {
                    selectSexValue = "男";
                    selectSexKey = 0;
                }
                if (checkId == R.id.rb_sex_girl) {
                    selectSexValue = "女";
                    selectSexKey = 1;
                }
            }
        });

        chatListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.layout_left_result) {
                    Logger.i(chatListAdapter.getData().get(position).getResultImageUrl());

                    Intent intent = new Intent(TestImageDetailActivity.this, TestResultActivity.class);
                    intent.putExtra("from_type", 2);
                    intent.putExtra("record_id", recordId);
                    intent.putExtra("image_url", chatListAdapter.getData().get(position).getCodeImageUrl());
                    intent.putExtra("nocode_image_url", chatListAdapter.getData().get(position).getResultImageUrl());
                    startActivity(intent);
                    finish();
                }
            }
        });

        //设置过滤器，
        InputFilter[] filters = {new NameLengthFilter(10)};
        mInputUserNameEt.setFilters(filters);

        testDetailInfoPresenterImp = new TestDetailInfoPresenterImp(this, this);
        testResultInfoPresenterImp = new TestResultInfoPresenterImp(this, this);
        taskRecordInfoPresenterImp = new TaskRecordInfoPresenterImp(this, this);

        //发布
        RxView.clicks(mConfigLayout).throttleFirst(300, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                config();
            }
        });

        testDetailInfoPresenterImp.getTestDetail(tid, 2);

        if (isFromTask == 1) {
            String openid = App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getOpenid() : "";
            taskRecordInfoPresenterImp.addTaskRecord(App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "", openid, taskId, goldNum, 0, 0, "0");
        }
    }

    @OnClick(R.id.layout_comment)
    void commentEvent() {
        if (isOver) {
            isOver = false;
            //再测一次
            Intent intent = new Intent(TestImageDetailActivity.this, TestResultActivity.class);
            intent.putExtra("record_id", recordId);
            intent.putExtra("from_type", 2);
            intent.putExtra("image_url", resultMsgInfo != null ? resultMsgInfo.getCodeImageUrl() : "");
            intent.putExtra("nocode_image_url", resultMsgInfo != null ? resultMsgInfo.getResultImageUrl() : "");
            startActivity(intent);
            finish();
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
        testMsgInfo.setContent(isShowSex ? "请告诉我你的性别?" : "请告诉我你的名字");
        chatListAdapter.addData(testMsgInfo);

        mInputLayout.setVisibility(View.VISIBLE);

        if (isShowSex) {
            mRadioGroup.setVisibility(View.VISIBLE);
            mInputUserNameLayout.setVisibility(View.GONE);
            inputStep = 1;
        } else {
            mRadioGroup.setVisibility(View.GONE);
            //显示输入用户姓名
            mInputUserNameLayout.setVisibility(View.VISIBLE);
            inputStep = 2;
        }

        chatListAdapter.notifyItemInserted(chatListAdapter.getData().size() - 1);
        mChatListView.scrollToPosition(chatListAdapter.getData().size() - 1);
    }

    @OnClick(R.id.layout_config)
    void config() {
        if (inputStep == 1) {
            if (StringUtils.isEmpty(selectSexValue)) {
                Toasty.normal(this, "请选择性别").show();
                return;
            }
            checkSex(selectSexValue);
            inputStep = 2;
        } else {
            if (StringUtils.isEmpty(mInputUserNameEt.getText())) {
                Toasty.normal(this, "请输入用户姓名").show();
                return;
            }

            //隐藏输入框
            KeyboardUtils.hideSoftInput(this);

            //显示用户输入的内容
            TestMsgInfo inputName = new TestMsgInfo();
            inputName.setType(TestMsgInfo.TYPE_SENT);
            inputName.setImgUrl(userInfo != null ? userInfo.getUserimg() : "");
            inputName.setContent(mInputUserNameEt.getText().toString());
            chatListAdapter.addData(inputName);

            //显示等待提示
            TestMsgInfo waitInfo = new TestMsgInfo();
            waitInfo.setType(TestMsgInfo.TYPE_RECEIVED);
            waitInfo.setContent("正在为您分析结果，请耐心等待几秒...");
            chatListAdapter.addData(waitInfo);

            //刷新列表页面
            chatListAdapter.notifyItemInserted(chatListAdapter.getData().size() - 1);
            mChatListView.scrollToPosition(chatListAdapter.getData().size() - 1);

            if (progressDialog != null && !progressDialog.isShowing()) {
                progressDialog.show();
            }

            TestResultParams params = new TestResultParams();
            params.setId(tid);
            params.setTestType("2");
            params.setNickname(mInputUserNameEt.getText().toString());
            params.setHeadimg(userInfo != null ? userInfo.getUserimg() : "");
            params.setSex(selectSexKey);
            params.setUserId(userInfo != null ? userInfo.getId() : "0");

            MobclickAgent.onEvent(this, "test_commit", AppUtils.getAppVersionName());
            testResultInfoPresenterImp.createImage(params);
        }
    }

    public void checkSex(String sexValue) {

        //回复选择的答案
        TestMsgInfo sexInfo = new TestMsgInfo();
        sexInfo.setType(TestMsgInfo.TYPE_SENT);
        sexInfo.setImgUrl(userInfo != null ? userInfo.getUserimg() : "");
        sexInfo.setContent(sexValue);
        chatListAdapter.addData(sexInfo);

        TestMsgInfo testMsgInfo = new TestMsgInfo();
        testMsgInfo.setType(TestMsgInfo.TYPE_RECEIVED);
        testMsgInfo.setContent("请告诉我你的名字");
        chatListAdapter.addData(testMsgInfo);

        //显示输入用户姓名
        mRadioGroup.setVisibility(View.GONE);
        mInputUserNameLayout.setVisibility(View.VISIBLE);

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

        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

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
                    isShowSex = ((TestDetailInfoRet) tData).getData().getSex() == 1 ? true : false;
                    //isShowSex = true;
                }
            }
            if (tData instanceof TestResultInfoRet) {
                isOver = true;
                if (((TestResultInfoRet) tData).getData() != null) {

                    //显示模糊的结果图片
                    resultMsgInfo = new TestMsgInfo();
                    resultMsgInfo.setType(TestMsgInfo.TYPE_RECEIVED);
                    resultMsgInfo.setImgUrl("");
                    resultMsgInfo.setCodeImageUrl(((TestResultInfoRet) tData).getData().getImage());
                    resultMsgInfo.setResultImageUrl(((TestResultInfoRet) tData).getData().getImageNocode());
                    chatListAdapter.addData(resultMsgInfo);

                    //显示：查看结果提醒
                    TestMsgInfo lastResultInfo = new TestMsgInfo();
                    lastResultInfo.setType(TestMsgInfo.TYPE_RECEIVED);
                    lastResultInfo.setContent("点击图片查看结果");
                    chatListAdapter.addData(lastResultInfo);

                    //刷新列表页面
                    chatListAdapter.notifyItemInserted(chatListAdapter.getData().size() - 1);
                    mChatListView.scrollToPosition(chatListAdapter.getData().size() - 1);

                    mInputLayout.setVisibility(View.GONE);
                    mCommentLayout.setVisibility(View.VISIBLE);
                    mCommentTextView.setText("查看结果");

                    KeyboardUtils.hideSoftInput(this);

                }
            }

            if (tData instanceof TaskRecordInfoRet) {
                if (((TaskRecordInfoRet) tData).getCode() == Constants.SUCCESS) {
                    if (StringUtils.isEmpty(recordId)) {
                        isAddTaskRecord = true;
                        if (((TaskRecordInfoRet) tData).getData() != null) {
                            recordId = ((TaskRecordInfoRet) tData).getData().getInfoid();
                        }
                    }
                } else {
                    //finish();
                    Logger.i("task error--->");
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

    private class NameLengthFilter implements InputFilter {
        int MAX_EN;// 最大英文/数字长度 一个汉字算两个字母
        String regEx = "[\\u4e00-\\u9fa5]"; // unicode编码，判断是否为汉字

        public NameLengthFilter(int mAX_EN) {
            super();
            MAX_EN = mAX_EN;
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
            int destCount = dest.toString().length()
                    + getChineseCount(dest.toString());
            int sourceCount = source.toString().length()
                    + getChineseCount(source.toString());
            if (destCount + sourceCount > MAX_EN) {
                Toasty.normal(TestImageDetailActivity.this, "字数达到上限").show();
                return "";
            } else {
                return source;
            }
        }

        private int getChineseCount(String str) {
            int count = 0;

            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher(str);
            while (m.find()) {
                for (int i = 0; i <= m.groupCount(); i++) {
                    count = count + 1;
                }
            }
            return count;
        }
    }
}
