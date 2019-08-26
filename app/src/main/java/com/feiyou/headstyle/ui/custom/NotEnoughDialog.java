package com.feiyou.headstyle.ui.custom;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.feiyou.headstyle.R;

public class NotEnoughDialog extends Dialog implements View.OnClickListener {

    private Context mContext;

    private TextView mKnowContentTv;

    private Button iknowButton;

    public NotEnoughDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public NotEnoughDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.not_enough_view);
        setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView() {
        mKnowContentTv = findViewById(R.id.tv_iknow_content);
        iknowButton = findViewById(R.id.btn_iknow);

        mKnowContentTv.setText(Html.fromHtml("游戏时长不足<font color='#ff5555'>30s</font>,无法获得奖励。游戏期间需要活跃操作，否则无法完成任务!"));
        iknowButton.setOnClickListener(this);
        setCanceledOnTouchOutside(true);

        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        window.setGravity(Gravity.CENTER);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_iknow:
                this.dismiss();
                break;
        }
    }
}