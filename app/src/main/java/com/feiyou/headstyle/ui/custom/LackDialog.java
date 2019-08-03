package com.feiyou.headstyle.ui.custom;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.feiyou.headstyle.R;

public class LackDialog extends Dialog implements View.OnClickListener {

    private Context mContext;

    private LinearLayout mConfigLayout;

    private LinearLayout mCancelLayout;

    ImageView mTopImageView;

    TextView mTitleTv;

    TextView mContentTv;

    TextView mCancelTv;

    TextView mConfigTv;

    private LackListener lackListener;

    public interface LackListener {
        void lackConfig();

        void lackCancel();
    }

    public void setLackListener(LackListener lackListener) {
        this.lackListener = lackListener;
    }

    public LackDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public LackDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    public void setLackInfo(String title, String content) {
        mTitleTv.setText(title);
        mContentTv.setText(content);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lack_dialog_view);
        setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView() {
        mConfigLayout = findViewById(R.id.layout_config);
        mCancelLayout = findViewById(R.id.layout_cancel);
        mTopImageView = findViewById(R.id.iv_config_img);
        mTitleTv = findViewById(R.id.tv_config_title);
        mContentTv = findViewById(R.id.tv_config_content);

        mConfigLayout.setOnClickListener(this);
        mCancelLayout.setOnClickListener(this);
        setCanceledOnTouchOutside(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_config:
                lackListener.lackConfig();
                this.dismiss();
                break;
            case R.id.layout_cancel:
                lackListener.lackCancel();
                this.dismiss();
                break;
            case R.id.iv_close:
                this.dismiss();
                break;
        }
    }
}