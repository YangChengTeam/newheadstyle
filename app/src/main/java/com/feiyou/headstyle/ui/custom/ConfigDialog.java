package com.feiyou.headstyle.ui.custom;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.feiyou.headstyle.R;

public class ConfigDialog extends Dialog implements View.OnClickListener {

    private Context mContext;

    private LinearLayout mConfigLayout;

    private LinearLayout mCancelLayout;

    ImageView mTopImageView;

    TextView mTitleTv;

    TextView mContentTv;

    TextView mCancelTv;

    TextView mConfigTv;

    private String title;

    private String content;

    private ConfigListener configListener;

    private String cancelTxt;

    private String configTxt;

    private int topImageRes;

    public interface ConfigListener {
        void config();

        void cancel();
    }

    public void setConfigListener(ConfigListener configListener) {
        this.configListener = configListener;
    }

    public void setCancelTxt(String cancelTxt) {
        this.cancelTxt = cancelTxt;
        mCancelTv.setText(cancelTxt);
    }

    public void setConfigTxt(String configTxt) {
        this.configTxt = configTxt;
        mConfigTv.setText(configTxt);
    }

    public ConfigDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public ConfigDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    public ConfigDialog(Context context, int themeResId, int imgRes, String titleStr, String contentStr) {
        super(context, themeResId);
        this.mContext = context;
        this.topImageRes = imgRes;
        this.title = titleStr;
        this.content = contentStr;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.config_dialog_view);
        setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView() {
        mConfigLayout = findViewById(R.id.layout_config);
        mCancelLayout = findViewById(R.id.layout_cancel);
        mTopImageView = findViewById(R.id.iv_config_img);
        mTitleTv = findViewById(R.id.tv_config_title);
        mContentTv = findViewById(R.id.tv_config_content);
        mCancelTv = findViewById(R.id.tv_cancel);
        mConfigTv = findViewById(R.id.tv_config);

        mConfigLayout.setOnClickListener(this);
        mCancelLayout.setOnClickListener(this);
        setCanceledOnTouchOutside(true);
        if (topImageRes > 0) {
            Glide.with(mContext).load(topImageRes).into(mTopImageView);
        }
        mTitleTv.setText(title);
        mContentTv.setText(content);
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