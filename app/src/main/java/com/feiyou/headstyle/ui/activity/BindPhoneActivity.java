package com.feiyou.headstyle.ui.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.ui.base.BaseFragmentActivity;
import com.orhanobut.logger.Logger;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;

import butterknife.BindView;
import butterknife.OnClick;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * Created by myflying on 2018/11/23.
 */
public class BindPhoneActivity extends BaseFragmentActivity {

    public static String COUNTRY_CODE = "86";

    @BindView(R.id.topbar)
    QMUITopBar mTopBar;

    @BindView(R.id.et_user_phone)
    EditText mMobileEditText;

    @BindView(R.id.et_sms_code)
    EditText mValidateCodeEditText;

    @BindView(R.id.btn_get_code)
    Button mGetCodeButton;

    @BindView(R.id.btn_bind_phone)
    Button mBindPhoneButton;

    ImageView mBackImageView;

    TextView mTitleTv;

    private EventHandler eventHandler;

    private int seconds = 60;

    Runnable runnable;

    private ProgressDialog progressDialog = null;

    private int pageType = 1;//1:新用户注册，2:忘记密码

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected int getContextViewId() {
        return R.layout.activity_bind_phone;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTopBar();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        eventHandler = new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                // afterEvent会在子线程被调用，因此如果后续有UI相关操作，需要将数据发送到UI线程
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                new Handler(Looper.getMainLooper(), new Handler.Callback() {
                    @Override
                    public boolean handleMessage(Message msg) {
                        int event = msg.arg1;
                        int result = msg.arg2;
                        Object data = msg.obj;
                        if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                            if (result == SMSSDK.RESULT_COMPLETE) {
                                smsButtonRefresh();
                                mValidateCodeEditText.setFocusable(true);
                                Logger.i("send code success--->");
                                // TODO 处理成功得到验证码的结果
                                // 请注意，此时只是完成了发送验证码的请求，验证码短信还需要几秒钟之后才送达
                            } else {
                                Logger.i("send code fail--->");
                                // TODO 处理错误的结果
                                ((Throwable) data).printStackTrace();
                            }
                        } else if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                            if (result == SMSSDK.RESULT_COMPLETE) {
                                //register();
                                ToastUtils.showLong("绑定成功");
                                if (progressDialog != null && progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }
                                finish();
                                Logger.i("validate code success--->");
                                // TODO 处理验证码验证通过的结果
                            } else {

                                if (progressDialog != null && progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }
                                ToastUtils.showLong("短信验证码错误，请重试");
                                Logger.i("validate code fail--->");
                                // TODO 处理错误的结果
                                ((Throwable) data).printStackTrace();
                            }
                        }
                        // TODO 其他接口的返回结果也类似，根据event判断当前数据属于哪个接口
                        return false;
                    }
                }).sendMessage(msg);
            }
        };

        // 注册一个事件回调，用于处理SMSSDK接口请求的结果
        SMSSDK.registerEventHandler(eventHandler);
    }

    private void initTopBar() {
        QMUIStatusBarHelper.setStatusBarLightMode(this);
        mTopBar.setTitle(getResources().getString(R.string.app_name));
        View topSearchView = getLayoutInflater().inflate(R.layout.common_top_back, null);
        topSearchView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(48)));
        mTitleTv = topSearchView.findViewById(R.id.tv_title);
        mTitleTv.setText("绑定手机");

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
        progressDialog.setMessage("正在登录");

        mMobileEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (StringUtils.isEmpty(charSequence.toString())) {
                    mGetCodeButton.setBackgroundResource(R.drawable.validate_code_normal_bg);
                    mGetCodeButton.setTextColor(ContextCompat.getColor(BindPhoneActivity.this, R.color.white));
                    mBindPhoneButton.setBackgroundResource(R.drawable.bindphone_normal_bg);
                } else {
                    mGetCodeButton.setBackgroundResource(R.drawable.validate_code_bg);
                    mGetCodeButton.setTextColor(ContextCompat.getColor(BindPhoneActivity.this, R.color.white));
                    mBindPhoneButton.setBackgroundResource(R.drawable.bindphone_done_bg);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    /**
     * 刷新验证码倒计时
     */
    private void smsButtonRefresh() {
        runnable = new Runnable() {
            @Override
            public void run() {
                if (seconds-- <= 0) {
                    mGetCodeButton.setEnabled(true);
                    mGetCodeButton.setText("获取验证码");
                    mGetCodeButton.setBackgroundResource(R.drawable.validate_code_bg);
                    mGetCodeButton.setTextColor(ContextCompat.getColor(BindPhoneActivity.this, R.color.white));
                    return;
                }
                mGetCodeButton.setEnabled(false);
                mGetCodeButton.setText(seconds + "s");
                mGetCodeButton.setBackgroundResource(R.drawable.validate_code_normal_bg);
                mGetCodeButton.setTextColor(ContextCompat.getColor(BindPhoneActivity.this, R.color.black3));
                handler.postDelayed(this, 1000);
            }
        };
        handler.postDelayed(runnable, 0);
    }

    @OnClick(R.id.btn_get_code)
    void getValidateCode() {
        if (!RegexUtils.isMobileExact(mMobileEditText.getText().toString())) {
            ToastUtils.showLong("请输入有效的手机号");
            return;
        }
        SMSSDK.getVerificationCode(COUNTRY_CODE, mMobileEditText.getText().toString());
    }

    @OnClick(R.id.btn_bind_phone)
    public void bindPhone() {

        if (StringUtils.isEmpty(mMobileEditText.getText())) {
            ToastUtils.showLong("请输入手机号");
            return;
        }

        if (StringUtils.isEmpty(mValidateCodeEditText.getText())) {
            ToastUtils.showLong("请输入验证码");
            return;
        }

        progressDialog.show();
        SMSSDK.submitVerificationCode(COUNTRY_CODE, mMobileEditText.getText().toString(), mValidateCodeEditText.getText().toString());
    }

    @Override
    public void onBackPressed() {
        popBackStack();
    }
}
