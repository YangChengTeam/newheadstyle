package com.feiyou.headstyle.ui.custom;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.feiyou.headstyle.R;


public class WeiXinTaskDialog extends Dialog {

    private Context context;

    private Dialog dialog;

    ImageView mPublicIv;

    public interface OpenWeixinListener {
        void startOpen();

        void closeDialog();
    }

    public OpenWeixinListener openWeixinListener;

    public void setOpenWeixinListener(OpenWeixinListener openWeixinListener) {
        this.openWeixinListener = openWeixinListener;
    }

    public WeiXinTaskDialog(Context context) {
        super(context, R.style.login_dialog);
        this.context = context;
    }

    public void setPublicImage(String url) {
        Glide.with(context).load(url).into(mPublicIv);
    }

    public void setLocalImage(int localUrl) {
        Glide.with(context).load(localUrl).into(mPublicIv);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    public void init() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.weixin_task_dialog, null);
        Button openButton = view.findViewById(R.id.btn_open_weixin);
        ImageView closeImageView = view.findViewById(R.id.iv_close);
        mPublicIv = view.findViewById(R.id.iv_public_image);

        setContentView(view);

        openButton.setOnClickListener(new clickListener());
        closeImageView.setOnClickListener(new clickListener());

        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = (int) (d.widthPixels * 0.8); // 高度设置为屏幕的0.7
        dialogWindow.setAttributes(lp);
    }

    private class clickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.btn_open_weixin:
                    openWeixinListener.startOpen();
                    dismiss();
                    break;
                case R.id.iv_close:
                    openWeixinListener.closeDialog();
                    dismiss();
                    break;
                default:
                    break;
            }
        }
    }
}