package com.feiyou.headstyle.ui.custom;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.feiyou.headstyle.R;

public class OpenDialog extends Dialog implements View.OnClickListener {

    private Context mContext;

    private LinearLayout mConfigLayout;

    private LinearLayout mCancelLayout;

    TextView mTitleTv;

    TextView mContentTv;

    private String title;

    private String content;

    private ConfigListener configListener;

    public interface ConfigListener {
        void config();

        void cancel();
    }

    public void setConfigListener(ConfigListener configListener) {
        this.configListener = configListener;
    }

    public OpenDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public OpenDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    public OpenDialog(Context context, int themeResId, String titleStr, String contentStr) {
        super(context, themeResId);
        this.mContext = context;
        this.title = titleStr;
        this.content = contentStr;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open_dialog_view);
        setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView() {
        mConfigLayout = findViewById(R.id.layout_config);
        mCancelLayout = findViewById(R.id.layout_cancel);
        mTitleTv = findViewById(R.id.tv_config_title);
        mContentTv = findViewById(R.id.tv_config_content);
        mConfigLayout.setOnClickListener(this);
        mCancelLayout.setOnClickListener(this);
        setCanceledOnTouchOutside(true);

        mTitleTv.setText(title);
        mContentTv.setText(content);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_config:
                configListener.config();
                this.dismiss();
                break;
            case R.id.layout_cancel:
                configListener.cancel();
                this.dismiss();
                break;
            case R.id.iv_close:
                this.dismiss();
                break;
        }
    }
}