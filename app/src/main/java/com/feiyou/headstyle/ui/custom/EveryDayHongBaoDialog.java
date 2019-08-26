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

import com.blankj.utilcode.util.AppUtils;
import com.bumptech.glide.Glide;
import com.feiyou.headstyle.R;
import com.umeng.analytics.MobclickAgent;

public class EveryDayHongBaoDialog extends Dialog implements View.OnClickListener {

    private Context mContext;

    private ImageView mOpenHongBaoIv;

    private ImageView mCloseIv;

    LinearLayout mReceiveBeforeLayout;

    public EveryDayHongBaoListener everyDayHongBaoListener;

    private boolean isClickAnyWhere;

    public interface EveryDayHongBaoListener {
        void openEveryDayHongBao();

        void closeEveryDayHongBao();
    }

    public void setEveryDayHongBaoListener(EveryDayHongBaoListener everyDayHongBaoListener) {
        this.everyDayHongBaoListener = everyDayHongBaoListener;
    }

    public EveryDayHongBaoDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public EveryDayHongBaoDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.everyday_hongbao_view);
        setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView() {
        MobclickAgent.onEvent(mContext, "alert_hongbao", AppUtils.getAppVersionName());

        mOpenHongBaoIv = findViewById(R.id.iv_open_hongbao);
        mCloseIv = findViewById(R.id.iv_close);
        mReceiveBeforeLayout = findViewById(R.id.layout_receive_before);

        mOpenHongBaoIv.setOnClickListener(this);
        mCloseIv.setOnClickListener(this);
        mReceiveBeforeLayout.setOnClickListener(this);
        Glide.with(mContext).load(R.drawable.every_day_open_hb).into(mOpenHongBaoIv);
    }

    public void setClickAnyWhere(boolean clickAnyWhere) {
        isClickAnyWhere = clickAnyWhere;
        if (!isClickAnyWhere) {
            mReceiveBeforeLayout.setClickable(false);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_open_hongbao:
                everyDayHongBaoListener.openEveryDayHongBao();
                dismiss();
                break;
            case R.id.layout_receive_before:
                if (isClickAnyWhere) {
                    everyDayHongBaoListener.openEveryDayHongBao();
                    dismiss();
                }
            case R.id.iv_close:
                everyDayHongBaoListener.closeEveryDayHongBao();
                dismiss();
                break;
            default:
                break;
        }
    }

}