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

public class PrivacyDialog extends Dialog implements View.OnClickListener {

    private Context mContext;

    private LinearLayout mConfigLayout;

    private LinearLayout mNotAgreeLayout;

    public PrivacyListener privacyListener;

    private TextView mPrivaryTv;

    public interface PrivacyListener {
        void agree();

        void notAgree();
    }

    public void setPrivacyListener(PrivacyListener privacyListener) {
        this.privacyListener = privacyListener;
    }

    public PrivacyDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public PrivacyDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.privacy_dialog_view);
        setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView() {
        mPrivaryTv = findViewById(R.id.tv_config_content);
        mConfigLayout = findViewById(R.id.layout_config);
        mNotAgreeLayout = findViewById(R.id.layout_not_agree);

        String temp = "欢迎使用个性头像！我们非常重视用户的隐私和个人信息保护。在您使用个性头像时，我们可能会获取部分必要信息，以提供基本服务。<br /><br />" +
                "1.您在登录和使用个性头像时，将会提供与使用服务相关的个人信息。<br />" +
                "2.未经您同意，我们不会出售或出租您的任何信息。<br />" +
                "3.您可以对上述信息进行访问、修改及删除。<br /><br />更多详细信息，欢迎您点击查看用户协议以及隐私政策，感谢您的信任!";

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
                //Toast.makeText(mContext, "触发点击事件!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext, PrivaryActivity.class);
                intent.putExtra("show_type",2);
                mContext.startActivity(intent);
            }
        };

        style.setSpan(clickableSpan, temp.length() - 42, temp.length() -38, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mPrivaryTv.setText(style);

        //设置部分文字颜色
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor("#ff5555"));
        style.setSpan(foregroundColorSpan, temp.length() - 42, temp.length() - 38, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        //配置给TextView
        mPrivaryTv.setMovementMethod(LinkMovementMethod.getInstance());
        mPrivaryTv.setText(style);


        //用户协议设置部分文字点击事件
        ClickableSpan xieyiSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent = new Intent(mContext, PrivaryActivity.class);
                intent.putExtra("show_type",1);
                mContext.startActivity(intent);
            }
        };
        style.setSpan(xieyiSpan, temp.length() - 48, temp.length() -44, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mPrivaryTv.setText(style);

        //设置部分文字颜色
        ForegroundColorSpan xieyiForeSpan = new ForegroundColorSpan(Color.parseColor("#ff5555"));
        style.setSpan(xieyiForeSpan, temp.length() - 48, temp.length() - 44, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

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
                this.privacyListener.agree();
                this.dismiss();
                break;
            case R.id.layout_not_agree:
                this.privacyListener.notAgree();
                this.dismiss();
                break;
            default:
                break;
        }
    }
}