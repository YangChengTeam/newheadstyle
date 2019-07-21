package com.feiyou.headstyle.ui.custom;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.feiyou.headstyle.R;

import org.w3c.dom.Text;

public class SignSuccessDialog extends Dialog implements View.OnClickListener {

    private Context mContext;

    private Button mSeeVideoBtn;

    TextView mAddGoldNumTv;

    TextView mSignDaysTv;

    private TextView mCloseTv;

    private SignSuccessListener signSuccessListener;

    public interface SignSuccessListener {
        void seeVideo();
    }

    public void setSignSuccessListener(SignSuccessListener signSuccessListener) {
        this.signSuccessListener = signSuccessListener;
    }

    public SignSuccessDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public SignSuccessDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    public void setSignInfo(int addGold, int days) {
        mAddGoldNumTv.setText("+" + addGold);
        mSignDaysTv.setText(days + "");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_success_view);
        setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView() {
        mSeeVideoBtn = findViewById(R.id.btn_see_video);
        mAddGoldNumTv = findViewById(R.id.tv_add_gold_num);
        mSignDaysTv = findViewById(R.id.tv_sign_day);

        mCloseTv = findViewById(R.id.tv_close);
        mSeeVideoBtn.setOnClickListener(this);
        mCloseTv.setOnClickListener(this);
        setCanceledOnTouchOutside(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_see_video:
                signSuccessListener.seeVideo();
                this.dismiss();
                break;
            case R.id.tv_close:
                this.dismiss();
                break;
            default:
                break;
        }
    }
}