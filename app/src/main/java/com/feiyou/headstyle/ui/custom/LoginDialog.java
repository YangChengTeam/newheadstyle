package com.feiyou.headstyle.ui.custom;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.feiyou.headstyle.R;

public class LoginDialog extends Dialog implements View.OnClickListener {

    private Context mContext;

    private LinearLayout mWeiXinLayout;

    private LinearLayout mQQLayout;

    private ImageView mCloseImageView;

    private LoginWayListener listener;

    public LoginDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public LoginDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    public LoginDialog(Context context, int themeResId, LoginWayListener listener) {
        super(context, themeResId);
        this.mContext = context;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_dialog_view);
        setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView() {
        mWeiXinLayout = findViewById(R.id.layout_weixin);
        mQQLayout = findViewById(R.id.layout_qq);
        mCloseImageView = findViewById(R.id.iv_close);
        mWeiXinLayout.setOnClickListener(this);
        mQQLayout.setOnClickListener(this);
        mCloseImageView.setOnClickListener(this);
        setCanceledOnTouchOutside(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_weixin:
                if (listener != null) {
                    listener.loginWay(this, 1);
                }
                this.dismiss();
                break;
            case R.id.layout_qq:
                if (listener != null) {
                    listener.loginWay(this, 2);
                }
                this.dismiss();
                break;
            case R.id.iv_close:
                this.dismiss();
                break;
        }
    }

    public interface LoginWayListener {
        void loginWay(Dialog dialog, int type);
    }
}