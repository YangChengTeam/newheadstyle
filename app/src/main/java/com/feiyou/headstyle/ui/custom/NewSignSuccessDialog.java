package com.feiyou.headstyle.ui.custom;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTImage;
import com.bytedance.sdk.openadsdk.TTNativeAd;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.utils.TTAdManagerHolder;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

public class NewSignSuccessDialog extends Dialog implements View.OnClickListener {

    private Context mContext;

    private FrameLayout mBannerContainer;

    private TextView mTitleTv;

    private TextView mCloseTv;

    private TextView mProfitTv;

    private TTAdNative mTTAdNative;

    private Button mPlayGameBtn;

    private Button mSeeVideoBtn;

    public NewSignListener newSignListener;

    public void setNewSignListener(NewSignListener newSignListener) {
        this.newSignListener = newSignListener;
    }

    public interface NewSignListener {
        void newSignSeeVideo();

        void newSignPlayGame();
    }

    public NewSignSuccessDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public NewSignSuccessDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_sign_success_view);
        setCanceledOnTouchOutside(false);
        initView();
    }

    public void setSignInfo(int addGold) {
        mProfitTv.setText("+" + addGold);
    }

    private void initView() {
        //step2:创建TTAdNative对象，createAdNative(Context context) banner广告context需要传入Activity对象
        mTTAdNative = TTAdManagerHolder.get().createAdNative(mContext);
        mBannerContainer = findViewById(R.id.layout_ad);
        mPlayGameBtn = findViewById(R.id.btn_play_game);
        mSeeVideoBtn = findViewById(R.id.btn_see_video);

        mCloseTv = findViewById(R.id.tv_close);
        mCloseTv.setOnClickListener(this);
        mTitleTv = findViewById(R.id.tv_title);
        mProfitTv = findViewById(R.id.tv_add_gold_num);
        mPlayGameBtn.setOnClickListener(this);
        mSeeVideoBtn.setOnClickListener(this);

        setCanceledOnTouchOutside(true);
        //loadBannerAd("920819188");
    }

    public void updateSignAdView(View view) {
        if (view != null) {
            mBannerContainer.removeAllViews();
            mBannerContainer.addView(view);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_play_game:
                this.newSignListener.newSignPlayGame();
                dismiss();
                break;
            case R.id.btn_see_video:
                this.newSignListener.newSignSeeVideo();
                dismiss();
                break;
            case R.id.tv_close:
                this.dismiss();
                break;
            default:
                break;
        }
    }
}