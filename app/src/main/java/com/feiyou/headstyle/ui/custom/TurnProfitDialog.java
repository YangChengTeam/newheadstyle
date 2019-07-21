package com.feiyou.headstyle.ui.custom;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.feiyou.headstyle.R;

public class TurnProfitDialog extends Dialog implements View.OnClickListener {

    private Context mContext;

    private LinearLayout mSeeVideoLayout;

    private TextView mCloseTv;

    private SignSuccessListener signSuccessListener;

    public interface SignSuccessListener {
        void seeVideo();
    }

    public void setSignSuccessListener(SignSuccessListener signSuccessListener) {
        this.signSuccessListener = signSuccessListener;
    }

    public TurnProfitDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public TurnProfitDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.turn_profit_view);
        setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView() {
        mSeeVideoLayout = findViewById(R.id.layout_see_video);
        mCloseTv = findViewById(R.id.tv_close);
        mSeeVideoLayout.setOnClickListener(this);
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