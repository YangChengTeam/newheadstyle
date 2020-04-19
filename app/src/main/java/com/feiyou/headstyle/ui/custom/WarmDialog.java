package com.feiyou.headstyle.ui.custom;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.feiyou.headstyle.R;
import com.feiyou.headstyle.ui.activity.PrivaryActivity;

public class WarmDialog extends Dialog implements View.OnClickListener {

    private Context mContext;

    private LinearLayout mConfigLayout;

    private LinearLayout mNotAgreeLayout;

    public WarmListener warmListener;

    private TextView mPrivaryTv;

    public interface WarmListener {
        void warnAgree();

        void warnNotAgree();
    }

    public void setWarmListener(WarmListener warmListener) {
        this.warmListener = warmListener;
    }

    public WarmDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public WarmDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.warm_dialog_view);
        setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView() {
        mPrivaryTv = findViewById(R.id.tv_config_content);
        mConfigLayout = findViewById(R.id.layout_config);
        mNotAgreeLayout = findViewById(R.id.layout_not_agree);

        String temp = "在您使用个性头像产品和服务前，请您务必同意隐私政策，感谢您的信任！" +
                "若您仍不同意本用户协议和隐私政策，很遗憾我们将无法为您提供服务，谢谢您的理解！";

        mPrivaryTv.setText(Html.fromHtml(temp));

        mConfigLayout.setOnClickListener(this);
        mNotAgreeLayout.setOnClickListener(this);
        setCanceledOnTouchOutside(false);

        final SpannableStringBuilder style = new SpannableStringBuilder();

        //设置文字
        style.append(Html.fromHtml(temp));

        //设置部分文字点击事件
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent = new Intent(mContext, PrivaryActivity.class);
                intent.putExtra("show_type",2);
                mContext.startActivity(intent);
            }
        };

        style.setSpan(clickableSpan, temp.length() - 27, temp.length() - 23, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mPrivaryTv.setText(style);

        //设置部分文字颜色
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor("#ff5555"));
        style.setSpan(foregroundColorSpan, temp.length() - 27, temp.length() - 23, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


        //设置部分文字点击事件
        ClickableSpan xieyiSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent = new Intent(mContext, PrivaryActivity.class);
                intent.putExtra("show_type",1);
                mContext.startActivity(intent);
            }
        };

        style.setSpan(xieyiSpan, temp.length() - 32, temp.length() - 28, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mPrivaryTv.setText(style);

        //设置部分文字颜色
        ForegroundColorSpan xieyiSpanforegroundColorSpan = new ForegroundColorSpan(Color.parseColor("#ff5555"));
        style.setSpan(xieyiSpanforegroundColorSpan, temp.length() - 32, temp.length() - 28, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


        //配置给TextView
        mPrivaryTv.setMovementMethod(LinkMovementMethod.getInstance());
        mPrivaryTv.setText(style);

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_config:
                this.warmListener.warnAgree();
                this.dismiss();
                break;
            case R.id.layout_not_agree:
                this.warmListener.warnNotAgree();
                this.dismiss();
                break;
            default:
                break;
        }
    }
}