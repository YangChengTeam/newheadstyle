package com.feiyou.headstyle.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.feiyou.headstyle.R;

public class GlideUtil {

    public static void loadImage(Context context, Object imgUrl, SimpleTarget<Drawable> target) {
        Glide.with(context)
                .load(imgUrl)
                .into(target);
    }

    public static void loadImage(Context context, Object imgUrl, ImageView imageView) {
        Glide.with(context)
                .load(imgUrl)
                .into(imageView);
    }
}