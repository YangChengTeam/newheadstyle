package com.feiyou.headstyle.ui.custom;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.feiyou.headstyle.R;

public class ReceiveHongBaoDialog extends Dialog implements View.OnClickListener {

    private Context mContext;

    private ImageView mOpenHongBaoIv;

    private TextView mCloseTv;

    private TextView mRedNumTv;

    LinearLayout mReceiveBeforeLayout;

    RelativeLayout mReceiveDoneLayout;

    public OpenHongBaoListener openHongBaoListener;

    public interface OpenHongBaoListener {
        void openHongbao();
    }

    public void setOpenHongBaoListener(OpenHongBaoListener openHongBaoListener) {
        this.openHongBaoListener = openHongBaoListener;
    }

    public ReceiveHongBaoDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public ReceiveHongBaoDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.receive_hongbao_view);
        setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView() {
        mOpenHongBaoIv = findViewById(R.id.iv_open_hongbao);
        mCloseTv = findViewById(R.id.tv_close);

        mReceiveBeforeLayout = findViewById(R.id.layout_receive_before);
        mReceiveDoneLayout = findViewById(R.id.layout_receive_done);
        mRedNumTv = findViewById(R.id.tv_red_num);

        mOpenHongBaoIv.setOnClickListener(this);
        mCloseTv.setOnClickListener(this);
        setCanceledOnTouchOutside(true);
        Glide.with(mContext).load(R.drawable.open_hongbao).into(mOpenHongBaoIv);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_open_hongbao:
                openHongBaoListener.openHongbao();
                break;
            case R.id.tv_close:
                this.dismiss();
                break;
            default:
                break;
        }
    }

    public void updateDialog(double redNum) {
        mReceiveDoneLayout.setVisibility(View.VISIBLE);
        mReceiveBeforeLayout.setVisibility(View.GONE);
        mRedNumTv.setText(redNum + "");
    }
}