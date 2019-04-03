package com.feiyou.headstyle.ui.custom;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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

    LinearLayout closeLayout;

    ImageView mCloseImageView;

    private String versionCode;

    private String versionContent;

    private int isForceUpdate;//0:不强制，1：强制

    public interface UpdateListener {
        void update();

        void updateCancel();
    }

    public UpdateListener updateListener;

    public void setUpdateListener(UpdateListener updateListener) {
        this.updateListener = updateListener;
    }

    public VersionUpdateDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public VersionUpdateDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public void setIsForceUpdate(int isForceUpdate) {
        this.isForceUpdate = isForceUpdate;
    }

    public void setVersionContent(String versionContent) {
        this.versionContent = versionContent;
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
        closeLayout = findViewById(R.id.layout_close);

        mVersionCodeTv.setText(versionCode);
        mContentTv.setText(versionContent);

        mUpdateVersionBtn.setOnClickListener(this);
        mCloseImageView.setOnClickListener(this);
        closeLayout.setVisibility(isForceUpdate == 1 ? View.GONE : View.VISIBLE);
        setCanceledOnTouchOutside(isForceUpdate == 1 ? false : true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_update_version:
                updateListener.update();
                this.dismiss();
                break;
            case R.id.iv_close:
                updateListener.updateCancel();
                this.dismiss();
                break;
        }
    }

    @Override
    public void setOnKeyListener(DialogInterface.OnKeyListener onKeyListener) {
        super.setOnKeyListener(onKeyListener);
    }
}