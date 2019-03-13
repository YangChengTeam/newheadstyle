package com.feiyou.headstyle.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.StringUtils;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.ui.activity.FriendListActivity;
import com.feiyou.headstyle.ui.custom.MsgEditText;
import com.orhanobut.logger.Logger;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2017/11/28.
 */

public class CommentDialog extends DialogFragment implements MsgEditText.BackListener {

    private Context mContext;

    private Dialog dialog;

    Handler handler;

    private MsgEditText mInputEditText;

    private TextView mSendTextView;

    private ImageView mAddFriendsIv;

    ProgressDialog progressDialog;

    private int cType;

    public final static String MASK_STR = "@";

    private Map<String, String> friendsMap;

    private List<String> friendIds;

    private StringBuffer ids;

    private String sendContent;

    public CommentDialog() {
    }

    @SuppressLint("ValidFragment")
    public CommentDialog(Context context, int type) {
        this.mContext = context;
        this.cType = type;
    }

    public interface SendBackListener {
        void sendContent(String ids, String content, int type);
    }

    private SendBackListener sendBackListener;

    public void setSendBackListener(SendBackListener sendBackListener) {
        this.sendBackListener = sendBackListener;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        friendsMap = new HashMap<>();
        ids = new StringBuffer("");

        // 使用不带Theme的构造器, 获得的dialog边框距离屏幕仍有几毫米的缝隙。
        dialog = new Dialog(getActivity(), R.style.BottomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置Content前设定
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        View contentView = View.inflate(getActivity(), R.layout.comment_input_dialog, null);
        dialog.setContentView(contentView);
        dialog.setCanceledOnTouchOutside(true); // 外部点击取消
        // 设置宽度为屏宽, 靠近屏幕底部。
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM; // 紧贴底部
        lp.alpha = 1;
        lp.dimAmount = 0.5f;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
        window.setAttributes(lp);
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        handler = new Handler();

        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    Logger.e("dialog 1111--->");
                    dialog.dismiss();
                }
                return false;
            }
        });
        mAddFriendsIv = (ImageView) contentView.findViewById(R.id.iv_add_friends);
        mInputEditText = (MsgEditText) contentView.findViewById(R.id.et_comment);
        mSendTextView = (TextView) contentView.findViewById(R.id.tv_send_commit);
        mSendTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.isEmpty(mInputEditText.getText().toString())) {
                    Toast.makeText(getActivity(), "请输入内容", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setMessage("正在发布");
                    if (progressDialog != null && !progressDialog.isShowing()) {
                        progressDialog.show();
                    }

                    String tempIds = setFriendIds(mInputEditText.getText().toString());
                    sendBackListener.sendContent(tempIds, StringUtils.isEmpty(sendContent) ? mInputEditText.getText().toString() : sendContent, cType);
                }
            }
        });

        mAddFriendsIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, FriendListActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        mInputEditText.setFocusable(true);
        mInputEditText.setFocusableInTouchMode(true);
        mInputEditText.requestFocus();

        mInputEditText.setBackListener(this);

        return dialog;
    }

    public String setFriendIds(String input) {
        sendContent = input;
        String tempStr;
        if (friendsMap != null && friendsMap.size() > 0) {
            Iterator iterator = friendsMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                Object key = entry.getKey();
                Object val = entry.getValue();

                if (input.indexOf(val.toString()) > -1) {
                    ids.append(key).append(",");

                    //实际传递到后台的内容值
                    sendContent = sendContent.replace(val.toString(), "<span style='color:#4383ff;'>" + val + "</span>");
                }
            }
            tempStr = ids.substring(0, ids.length() - 1);
        } else {
            tempStr = "";
        }

        Logger.i("commentdialog userids --->" + tempStr + "--->content--->" + sendContent);
        return tempStr;
    }

    @Override
    public void onDismiss(final DialogInterface dialog) {
        super.onDismiss(dialog);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Logger.e("dialog 2222--->");
                hideSoftKeyboard();
            }
        }, 200);
    }

    public void hideSoftKeyboard() {
        try {
            ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (NullPointerException e) {
            Logger.e(e.getMessage());
        }
    }

    public void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    public void setAtUserNames(List<String> friendIds, List<String> userNameList) {
        this.friendIds = friendIds;
        for (int i = 0; i < userNameList.size(); i++) {
            friendsMap.put(friendIds.get(i), MASK_STR + userNameList.get(i));
            mInputEditText.addAtSpan(MASK_STR, userNameList.get(i), 100000, ContextCompat.getColor(mContext, R.color.set_qq_bg_color));
        }
    }

    @Override
    public void back() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }
}
