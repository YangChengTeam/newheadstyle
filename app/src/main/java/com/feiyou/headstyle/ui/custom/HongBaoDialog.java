package com.feiyou.headstyle.ui.custom;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.feiyou.headstyle.R;


public class HongBaoDialog extends Dialog implements View.OnClickListener {

    private Context mContext;

    private FrameLayout mHBBeforeLayout;

    private FrameLayout mHBAfterLayout;

    private TextView mHBTitleTv;

    private TextView mMaxValueTv;

    private TextView mGetMoneyTv;

    private ImageView mCloseHbIv;

    private ImageView mGetMoneyIv;

    private ImageView mCashMoneyIv;

    public int hbState;//红包打开的状态,开启/未开启

    private boolean isClickAnyWhere;

    public interface HongBaoListener {
        void openHB();

        void directClose();//直接关闭
    }

    public HongBaoListener hongBaoListener;

    public void setHongBaoListener(HongBaoListener hongBaoListener) {
        this.hongBaoListener = hongBaoListener;
    }

    public HongBaoDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public HongBaoDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hongbao_dialog);
        setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView() {
        mHBBeforeLayout = findViewById(R.id.layout_hb_show);
        mHBAfterLayout = findViewById(R.id.layout_hb_detail);

        mHBTitleTv = findViewById(R.id.tv_hb_title);
        mMaxValueTv = findViewById(R.id.tv_max_show);
        mGetMoneyTv = findViewById(R.id.tv_get_money);
        mCloseHbIv = findViewById(R.id.tv_close_hb);
        mGetMoneyIv = findViewById(R.id.iv_get_money);
        mCashMoneyIv = findViewById(R.id.iv_to_cash);

        mHBBeforeLayout.setOnClickListener(this);
        mGetMoneyIv.setOnClickListener(this);
        mCashMoneyIv.setOnClickListener(this);
        mCloseHbIv.setOnClickListener(this);

        setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    return true;
                }
                return false;
            }
        });
    }

    public void showDialog() {
        this.show();

        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        lp.height = ViewGroup.LayoutParams.MATCH_PARENT; //注意一定要是MATCH_PARENT
        dialogWindow.setAttributes(lp);
    }

    public void setHBConfigInfo(int hbType,boolean clickAnyWhere) {
        mHBTitleTv.setText(hbType == 1 ? "恭喜收到新人红包" : "恭喜收到登录红包");
        mMaxValueTv.setText(hbType == 1 ? "18.8" : "8.8");

        isClickAnyWhere = clickAnyWhere;
        if (!isClickAnyWhere) {
            mHBBeforeLayout.setClickable(false);
        }
    }

    public void updateHBState(int state, double doubleMoney) {
        hbState = state;
        if(hbState == 1){
            mHBBeforeLayout.setVisibility(View.GONE);
            mHBAfterLayout.setVisibility(View.VISIBLE);
            mGetMoneyTv.setText(doubleMoney+"");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_hb_show:
            case R.id.iv_get_money:
                this.hongBaoListener.openHB();
                break;
            case R.id.tv_close_hb:
                if (hbState == 0) {
                    this.hongBaoListener.directClose();
                } else {
                    dismiss();
                }
                break;
            default:
                break;
        }
    }
}
