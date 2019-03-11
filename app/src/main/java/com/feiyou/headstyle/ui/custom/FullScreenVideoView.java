package com.feiyou.headstyle.ui.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

import com.blankj.utilcode.util.ScreenUtils;
import com.orhanobut.logger.Logger;

/**
 * Created by 钉某人
 * github: https://github.com/DingMouRen
 * email: naildingmouren@gmail.com
 */
public class FullScreenVideoView extends VideoView {

    private int mWidth;

    private int mHeight;

    public FullScreenVideoView(Context context) {
        super(context);
    }

    public FullScreenVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FullScreenVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setVideoSize(int w, int h) {
        this.mWidth = w;
        this.mHeight = h;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //Logger.i("width--->" + ScreenUtils.getScreenWidth() + "---height--->" + tempHeight);
        setMeasuredDimension(mWidth, mHeight);
    }
}
