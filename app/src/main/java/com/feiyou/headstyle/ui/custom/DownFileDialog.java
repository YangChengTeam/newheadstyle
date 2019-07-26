package com.feiyou.headstyle.ui.custom;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.view.UpdataAPPProgressBar;

public class DownFileDialog extends Dialog implements View.OnClickListener {

    private Context mContext;

    private ImageView mCloseIv;

    UpdataAPPProgressBar updataAPPProgressBar;

    public interface DownListener {
        void downCancel();
    }

    public DownListener downListener;

    public void setDownListener(DownListener downListener) {
        this.downListener = downListener;
    }

    public DownFileDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public DownFileDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.down_file_dialog_view);
        setCanceledOnTouchOutside(false);
        initView();
    }

    public void setProgress(int progress) {
        updataAPPProgressBar.setProgress(progress);
    }

    private void initView() {
        mCloseIv = findViewById(R.id.iv_down_close);
        updataAPPProgressBar = findViewById(R.id.down_progress);
        mCloseIv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_down_close:
                downListener.downCancel();
                dismiss();
                break;
            default:
                break;
        }
    }
}