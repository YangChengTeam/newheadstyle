package com.feiyou.headstyle.ui.custom;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.feiyou.headstyle.R;

public class ChargeDialog extends Dialog implements View.OnClickListener {

    private Context mContext;

    private EditText mAccountEt;

    private Button mChargeBtn;

    private ImageView mCloseIv;

    public interface BindListener {
        void bindAccount(String account);

        void cancelBind();
    }

    public BindListener bindListener;

    public void setBindListener(BindListener bindListener) {
        this.bindListener = bindListener;
    }

    public ChargeDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public ChargeDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.charge_dialog_view);
        setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView() {
        mAccountEt = findViewById(R.id.et_account_number);
        mChargeBtn = findViewById(R.id.btn_charge);
        mCloseIv = findViewById(R.id.iv_close);
        mChargeBtn.setOnClickListener(this);
        mCloseIv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_charge:
                if (StringUtils.isEmpty(mAccountEt.getText())) {
                    ToastUtils.showLong("请输入绑定的账号");
                    return;
                }
                bindListener.bindAccount(mAccountEt.getText().toString());
                dismiss();
                break;
            case R.id.iv_close:
                dismiss();
                break;
            default:
                break;
        }
    }
}