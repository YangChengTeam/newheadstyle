package com.feiyou.headstyle.ui.custom;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.feiyou.headstyle.R;

public class VersionUpdateDialog extends Dialog implements View.OnClickListener {

    private Context mContext;

    TextView mVersionCodeTv;

    TextView mContentTv;

    Button mUpdateVersionBtn;

    ImageView mCloseImageView;

    private UpdateListener listener;

    public VersionUpdateDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public VersionUpdateDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    public VersionUpdateDialog(Context context, int themeResId, UpdateListener listener) {
        super(context, themeResId);
        this.mContext = context;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.version_update_view);
        setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView() {
        mVersionCodeTv = findViewById(R.id.tv_version_code);
        mContentTv = findViewById(R.id.tv_content);
        mUpdateVersionBtn = findViewById(R.id.btn_update_version);
        mCloseImageView = findViewById(R.id.iv_close);

        mVersionCodeTv.setOnClickListener(this);
        mContentTv.setOnClickListener(this);
        mUpdateVersionBtn.setOnClickListener(this);
        mCloseImageView.setOnClickListener(this);

        setCanceledOnTouchOutside(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_update_version:
                if (listener != null) {
                    listener.update(this);
                }
                this.dismiss();
                break;
            case R.id.iv_close:
                this.dismiss();
                break;
        }
    }

    public interface UpdateListener {
        void update(Dialog dialog);
    }
}