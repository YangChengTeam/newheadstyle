package com.feiyou.headstyle.ui.custom;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.feiyou.headstyle.R;

public class SignOutDialog extends Dialog implements View.OnClickListener {

    private Context mContext;

    private LinearLayout mConfigLayout;

    private LinearLayout mCancelLayout;

    private SignOutListener signOutListener;

    public interface SignOutListener {
        void configSignOut();

        void cancelSignOut();
    }

    public void setSignOutListener(SignOutListener signOutListener) {
        this.signOutListener = signOutListener;
    }

    public SignOutDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public SignOutDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_out_dialog_view);
        setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView() {
        mConfigLayout = findViewById(R.id.layout_config);
        mCancelLayout = findViewById(R.id.layout_cancel);

        mConfigLayout.setOnClickListener(this);
        mCancelLayout.setOnClickListener(this);
        setCanceledOnTouchOutside(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_config:
                signOutListener.configSignOut();
                this.dismiss();
                break;
            case R.id.layout_cancel:
                signOutListener.cancelSignOut();
                this.dismiss();
                break;
            case R.id.iv_close:
                this.dismiss();
                break;
        }
    }
}