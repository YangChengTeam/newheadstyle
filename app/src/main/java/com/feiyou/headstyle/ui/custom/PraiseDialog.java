package com.feiyou.headstyle.ui.custom;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.feiyou.headstyle.R;

public class PraiseDialog extends Dialog implements View.OnClickListener {

    private Context mContext;

    private LinearLayout mConfigLayout;

    private LinearLayout mCancelLayout;

    private PraiseListener praiseListener;

    public interface PraiseListener {
        void config();

        void cancel();
    }

    public void setPraiseListener(PraiseListener praiseListener) {
        this.praiseListener = praiseListener;
    }

    public PraiseDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public PraiseDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.praise_dialog_view);
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
                praiseListener.config();
                this.dismiss();
                break;
            case R.id.layout_cancel:
                praiseListener.cancel();
                this.dismiss();
                break;
        }
    }
}