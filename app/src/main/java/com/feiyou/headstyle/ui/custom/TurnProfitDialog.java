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

    private TextView mGoldNumTv;

    private TextView mCashNumTv;

    private TextView mCloseTv;

    private TurnListener turnListener;

    public interface TurnListener {
        void configTurn();
    }

    public void setTurnListener(TurnListener turnListener) {
        this.turnListener = turnListener;
    }

    public TurnProfitDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public TurnProfitDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    public void setTurnInfo(String goldNum, String cashNum) {
        mGoldNumTv.setText(goldNum);
        mCashNumTv.setText(cashNum);
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
        mGoldNumTv = findViewById(R.id.tv_gold_num);
        mCashNumTv = findViewById(R.id.tv_cash_num);
        mSeeVideoLayout.setOnClickListener(this);
        mCloseTv.setOnClickListener(this);
        setCanceledOnTouchOutside(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_see_video:
                turnListener.configTurn();
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