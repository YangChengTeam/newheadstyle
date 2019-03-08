package com.feiyou.headstyle.utils;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.SizeUtils;
import com.feiyou.headstyle.R;

/**
 * Created by myflying on 2019/3/8.
 */
public class MyToastUtils {
    /**
     * 将Toast封装在一个方法中，在其它地方使用时直接输入要弹出的内容即可
     */
    public static void showToast(Activity context, int msgType, String messages) {
        LayoutInflater inflater = context.getLayoutInflater();//调用Activity的getLayoutInflater()
        View view = inflater.inflate(R.layout.my_toast_view, null); //加載layout下的布局
        //view.setLayoutParams(new LinearLayout.LayoutParams(SizeUtils.dp2px(150), SizeUtils.dp2px(100)));
        ImageView toastImg = view.findViewById(R.id.iv_toast_img);
        toastImg.setImageResource(msgType == 0 ? R.mipmap.toast_success : R.mipmap.toast_error);//显示的图片

        TextView toastTxt = view.findViewById(R.id.tv_toast_txt);
        toastTxt.setText(messages); //toast内容
        Toast toast = new Toast(context);
        toast.setGravity(Gravity.CENTER, 12, 20);//setGravity用来设置Toast显示的位置，相当于xml中的android:gravity或android:layout_gravity
        toast.setDuration(Toast.LENGTH_LONG);//setDuration方法：设置持续时间，以毫秒为单位。该方法是设置补间动画时间长度的主要方法
        toast.setView(view); //添加视图文件
        toast.show();
    }
}
