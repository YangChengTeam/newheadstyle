package com.feiyou.headstyle.ui.custom;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.feiyou.headstyle.R;

public class SeeVideoDialog extends Dialog implements View.OnClickListener {

    private Context mContext;

    private Button mGetMoneyBtn;

    private ImageView mCloseTv;

    private GetMoneyListener getMoneyListener;

    public interface GetMoneyListener {
        void getMoney();
    }

    public void setGetMoneyListener(GetMoneyListener getMoneyListener) {
        this.getMoneyListener = getMoneyListener;
    }

    public SeeVideoDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public SeeVideoDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.see_video_dialog);
        setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView() {
        mCloseTv = findViewById(R.id.iv_close);
        mGetMoneyBtn = findViewById(R.id.btn_get_money);
        mCloseTv.setOnClickListener(this);
        mGetMoneyBtn.setOnClickListener(this);
        setCanceledOnTouchOutside(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_get_money:
                this.getMoneyListener.getMoney();
                break;
            case R.id.iv_close:
                this.dismiss();
                break;
            default:
                break;
        }
    }
}