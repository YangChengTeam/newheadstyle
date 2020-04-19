package com.feiyou.headstyle.ui.fragment;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.blankj.utilcode.util.SPUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.ui.activity.AdActivity;


/**
 * Created by admin on 2017/12/28.
 */

public class CardWindowFragment extends DialogFragment {

    ImageView closeImageView;

    ImageView mLuckBgImageView;

    public String popImageUrl;

    public String jumpPath;

    public String adTitle;

    interface AdDismissListener {
        void adClick();
    }

    public AdDismissListener adDismissListener;

    public void setAdDismissListener(AdDismissListener adDismissListener) {
        this.adDismissListener = adDismissListener;
    }

    public void setPopImageUrl(String popImageUrl) {
        this.popImageUrl = popImageUrl;
    }

    public void setJumpPath(String jumpPath) {
        this.jumpPath = jumpPath;
    }

    public void setAdTitle(String adTitle) {
        this.adTitle = adTitle;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.open_window_dialog, container);

        closeImageView = view.findViewById(R.id.iv_close);
        mLuckBgImageView = view.findViewById(R.id.iv_luck);

        Glide.with(getActivity()).load(popImageUrl).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                closeImageView.setVisibility(View.VISIBLE);
                return false;
            }
        }).into(mLuckBgImageView);

        Window window = getDialog().getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//注意此处
        window.setWindowAnimations(R.style.my_popwindow_anim_style);

        mLuckBgImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();

                adDismissListener.adClick();

                SPUtils.getInstance().put("show_ad_window", false);
                Intent intent = new Intent(getActivity(), AdActivity.class);
                intent.putExtra("open_url", jumpPath);
                intent.putExtra("ad_title", adTitle);
                startActivity(intent);
            }
        });

        closeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        SPUtils.getInstance().put("show_ad_window", false);
        super.onDismiss(dialog);
    }
}
