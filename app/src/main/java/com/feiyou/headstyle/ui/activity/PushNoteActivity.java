package com.feiyou.headstyle.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feiyou.headstyle.App;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.AddNoteRet;
import com.feiyou.headstyle.bean.MessageEvent;
import com.feiyou.headstyle.common.Constants;
import com.feiyou.headstyle.presenter.AddNotePresenterImp;
import com.feiyou.headstyle.ui.adapter.AddNoteImageAdapter;
import com.feiyou.headstyle.ui.base.BaseFragmentActivity;
import com.feiyou.headstyle.ui.custom.Glide4Engine;
import com.feiyou.headstyle.ui.custom.MsgEditText;
import com.feiyou.headstyle.view.AddNoteView;
import com.jakewharton.rxbinding.view.RxView;
import com.orhanobut.logger.Logger;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import rx.functions.Action1;

/**
 * Created by myflying on 2018/11/23.
 */
public class PushNoteActivity extends BaseFragmentActivity implements AddNoteView {

    public static final int REQUEST_CODE_CHOOSE = 23;

    public static final int REQUEST_FRIENDS = 0;

    public static final int RESULT_FRIEND_CODE = 2;

    public static final int RESULT_TOPIC_CODE = 3;

    public final static String MASK_STR = "@";

    public final static String SPLIT_STR = "*#";

    @BindView(R.id.topbar)
    QMUITopBar mTopBar;

    @BindView(R.id.add_note_image_list)
    RecyclerView mNoteImageListView;

    @BindView(R.id.layout_choose_image)
    LinearLayout mImageLayout;

    @BindView(R.id.layout_topic)
    LinearLayout mTopicLayout;

    @BindView(R.id.layout_friends)
    LinearLayout mFriendsLayout;

    @BindView(R.id.tv_topic_txt)
    TextView mTopicTv;

    @BindView(R.id.et_input_note_content)
    MsgEditText mInputNoteEditText;

    TextView mCancelTv;

    Button mAddNoteBtn;

    AddNoteImageAdapter addNoteImageAdapter;

    private int maxTotal = 9;

    AddNotePresenterImp addNotePresenterImp;

    private Map<String, String> friendsMap;

    private StringBuffer ids;

    private int topicSelectIndex = -1;

    private String topicId;

    private String sendContent;

    private String tempFilePath;

    private Drawable notAddTopicDw;

    private Drawable isAddTopicDw;

    //最大输入长度
    final int maxInputLength = 500;

    private int fromNoteType = 1;

    private int topicDefIndex = -1;

    private ProgressDialog progressDialog = null;

    @Override
    protected int getContextViewId() {
        return R.layout.activity_add_note;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTopBar();
        initData();
    }

    private void initTopBar() {
        QMUIStatusBarHelper.setStatusBarLightMode(this);
        mTopBar.setTitle(getResources().getString(R.string.app_name));
        View pushNoteView = getLayoutInflater().inflate(R.layout.push_note_top_back, null);
        pushNoteView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(48)));

        mTopBar.setCenterView(pushNoteView);
        mCancelTv = pushNoteView.findViewById(R.id.iv_cancel);
        mAddNoteBtn = pushNoteView.findViewById(R.id.btn_push);
        mCancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });

        //设置过滤器，
        InputFilter[] filters = {new NameLengthFilter(maxInputLength)};
        mInputNoteEditText.setFilters(filters);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null && !StringUtils.isEmpty(bundle.getString("file_path"))) {
            tempFilePath = bundle.getString("file_path");
        }

        if (bundle != null) {
            topicDefIndex = bundle.getInt("topic_index", -1);
        }

        if (bundle != null) {
            fromNoteType = bundle.getInt("from_note_type", 1);
        }

        //发布
        RxView.clicks(mAddNoteBtn).throttleFirst(300, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                String tempInputContent = mInputNoteEditText.getText().toString();
                if (StringUtils.isEmpty(tempInputContent)) {
                    Toasty.normal(PushNoteActivity.this, "你还没有输入内容哦").show();
                    return;
                }

                Logger.i("send input --->" + mInputNoteEditText.getText());

                List<Object> tempList = addNoteImageAdapter.getData();
                List<String> result = new ArrayList<>();
                for (int i = 0; i < tempList.size(); i++) {
                    //排除+号
                    if (tempList.get(i) instanceof String) {
                        result.add(tempList.get(i).toString());
                    }
                }

                if (result.size() == 0) {
                    Toasty.normal(PushNoteActivity.this, "配图可以让更多人注意到你").show();
                    return;
                }

                if (topicSelectIndex == -1) {
                    Toasty.normal(PushNoteActivity.this, "别忘了选择话题，否则大家看不到喔").show();
                    return;
                }

                String tempIds = setFriendIds(tempInputContent);

                //TODO,此方法有问题
                Logger.i("sendContent--->" + sendContent);
                addNotePresenterImp.addNote(App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "", StringUtils.isEmpty(sendContent) ? tempInputContent : sendContent, topicId, tempIds, result);
            }
        });

        notAddTopicDw = ContextCompat.getDrawable(this, R.mipmap.not_add_note_topic);
        isAddTopicDw = ContextCompat.getDrawable(this, R.mipmap.is_add_topic_icon);

        friendsMap = new HashMap<>();
        ids = new StringBuffer("");

        //设置默认值
        if (topicDefIndex > -1) {
            mTopicTv.setCompoundDrawablesWithIntrinsicBounds(isAddTopicDw, null, null, null);
            mTopicTv.setTextColor(ContextCompat.getColor(this, R.color.tab_select_color));
            topicSelectIndex = topicDefIndex;
            topicId = bundle.getString("topic_id");
            mTopicTv.setText(App.topicInfoList.get(topicDefIndex).getName());
        }

        List<Object> list = new ArrayList<>();
        if (!StringUtils.isEmpty(tempFilePath)) {
            list.add(tempFilePath);
            maxTotal = maxTotal - 1;
        }
        list.add(R.mipmap.add_my_photo);

        addNoteImageAdapter = new AddNoteImageAdapter(this, list, 1);
        mNoteImageListView.setLayoutManager(new GridLayoutManager(this, 3));
        mNoteImageListView.setAdapter(addNoteImageAdapter);
        addNoteImageAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                Matisse.from(PushNoteActivity.this)
                        .choose(MimeType.ofImage())
                        .countable(true)
                        .maxSelectable(maxTotal)
                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                        .thumbnailScale(0.85f)
                        .imageEngine(new Glide4Engine())
                        .forResult(REQUEST_CODE_CHOOSE);
            }
        });

        addNoteImageAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.iv_close) {
                    addNoteImageAdapter.remove(position);
                    //不够9张时，增加+号
                    if (maxTotal == 0) {
                        addNoteImageAdapter.addData(R.mipmap.add_my_photo);
                    }
                    maxTotal = maxTotal + 1;
                }
            }
        });
    }

    public void initData() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在发布");
        progressDialog.setCanceledOnTouchOutside(false);

        addNotePresenterImp = new AddNotePresenterImp(this, this);
    }

    public String setFriendIds(String input) {
        sendContent = input;
        String tempStr;
        if (friendsMap.size() > 0) {
            Iterator iterator = friendsMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                Object key = entry.getKey();
                Object val = entry.getValue();

                if (input.indexOf(val.toString()) > -1) {
                    ids.append(key).append(",");

                    //实际传递到后台的内容值
                    sendContent = sendContent.replace(val.toString(), "<font color='#4b79ad'>" + val + "</font>");
                }
            }
            tempStr = ids.substring(0, ids.length() - 1);
        } else {
            tempStr = "";
        }

        Logger.i("result ids --->" + tempStr + "--->content--->" + sendContent);
        return tempStr;
    }

    @OnClick(R.id.layout_choose_image)
    void chooseImage() {
        Matisse.from(PushNoteActivity.this)
                .choose(MimeType.ofImage())
                .countable(true)
                .maxSelectable(maxTotal)
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(new Glide4Engine())
                .forResult(REQUEST_CODE_CHOOSE);
    }

    @OnClick(R.id.layout_topic)
    void addTopic() {
        Intent intent = new Intent(this, TopicSelectActivity.class);
        intent.putExtra("topic_select_index", topicSelectIndex);
        startActivityForResult(intent, REQUEST_FRIENDS);
    }

    @OnClick(R.id.layout_friends)
    void addFriends() {
        Intent intent = new Intent(this, FriendListActivity.class);
        startActivityForResult(intent, REQUEST_FRIENDS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_CHOOSE:
                    Logger.i(JSONObject.toJSONString(Matisse.obtainPathResult(data)));
                    if (Matisse.obtainPathResult(data) != null && Matisse.obtainPathResult(data).size() > 0) {

                        List<String> tempList = Matisse.obtainPathResult(data);
                        List<Object> result = new ArrayList<>();
                        for (int i = 0; i < tempList.size(); i++) {
                            result.add(tempList.get(i));
                        }

                        //剩余可选的数量
                        maxTotal = maxTotal - result.size();

                        if (addNoteImageAdapter.getData().size() > 1) {
                            addNoteImageAdapter.remove(addNoteImageAdapter.getData().size() - 1);
                            addNoteImageAdapter.addData(result);
                        } else {
                            addNoteImageAdapter.setNewData(result);
                        }

                        //不够9张时，增加+号
                        if (addNoteImageAdapter.getData().size() < 9) {
                            addNoteImageAdapter.addData(R.mipmap.add_my_photo);
                        }
                    }
                    break;

            }
        }

        //选择了好友时返回
        if (resultCode == RESULT_FRIEND_CODE) {
            if (requestCode == REQUEST_FRIENDS) {
                Logger.i("select ids--->" + JSONObject.toJSONString(data.getIntegerArrayListExtra("friend_ids")));
                Logger.i("select names--->" + JSONObject.toJSONString(data.getStringArrayListExtra("friend_names")));
                ArrayList<String> ids = data.getStringArrayListExtra("friend_ids");
                ArrayList<String> names = data.getStringArrayListExtra("friend_names");
                for (int i = 0; i < names.size(); i++) {
                    friendsMap.put(ids.get(i), MASK_STR + names.get(i));
                    mInputNoteEditText.addAtSpan(MASK_STR, names.get(i), 100000, ContextCompat.getColor(this, R.color.set_qq_bg_color));
                }
            }
        }

        //选择了话题时返回
        if (resultCode == RESULT_TOPIC_CODE) {
            if (requestCode == REQUEST_FRIENDS) {
                mTopicTv.setCompoundDrawablesWithIntrinsicBounds(isAddTopicDw, null, null, null);
                mTopicTv.setTextColor(ContextCompat.getColor(this, R.color.tab_select_color));

                topicSelectIndex = data.getIntExtra("topic_select_index", -1);
                topicId = data.getStringExtra("topic_id");

                Logger.i("topic info--->" + topicId + "---" + data.getStringExtra("topic_name"));
                mTopicTv.setText(data.getStringExtra("topic_name"));
            } else {
                mTopicTv.setCompoundDrawablesWithIntrinsicBounds(notAddTopicDw, null, null, null);
                mTopicTv.setTextColor(ContextCompat.getColor(this, R.color.add_topic_color));
            }
        }
    }

    @Override
    public void onBackPressed() {
        popBackStack();
    }

    @Override
    public void showProgress() {
        if (progressDialog != null && !progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    @Override
    public void dismissProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void loadDataSuccess(AddNoteRet tData) {
        Logger.i(JSON.toJSONString(tData));

        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

        if (tData.getCode() == Constants.SUCCESS) {
            if (tData.getData() != null) {
                //发帖完成，可以弹出打分
                SPUtils.getInstance().put(Constants.IS_OPEN_SCORE, true);
                if (fromNoteType == 1) {
                    MessageEvent addMessage = new MessageEvent("add_note");
                    addMessage.setAddNoteInfo(tData.getData());
                    EventBus.getDefault().post(addMessage);
                    finish();
                }

                if (fromNoteType == 2) {
                    MessageEvent addMessage = new MessageEvent("add_note_type");
                    addMessage.setTopicId(topicId);
                    EventBus.getDefault().post(addMessage);
                    finish();
                }

                if (fromNoteType == 3) {
                    Intent intent = new Intent(this, Main1Activity.class);
                    intent.putExtra("home_index", 1);
                    startActivity(intent);
                    //App.getApp().setTempAddNoteInfo(tData.getData());
                    finish();
                }
            }

        } else {
            Toasty.normal(this, StringUtils.isEmpty(tData.getMsg()) ? "发帖失败" : tData.getMsg()).show();
        }
    }

    @Override
    public void loadDataError(Throwable throwable) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        Toasty.normal(this, "发帖失败").show();
    }

    private class NameLengthFilter implements InputFilter {
        int MAX_EN;// 最大英文/数字长度 一个汉字算两个字母
        String regEx = "[\\u4e00-\\u9fa5]"; // unicode编码，判断是否为汉字

        public NameLengthFilter(int mAX_EN) {
            super();
            MAX_EN = mAX_EN;
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
            int destCount = dest.toString().length()
                    + getChineseCount(dest.toString());
            int sourceCount = source.toString().length()
                    + getChineseCount(source.toString());
            if (destCount + sourceCount > MAX_EN) {
                Toasty.normal(PushNoteActivity.this, "字数达到上限").show();
                return "";
            } else {
                return source;
            }
        }

        private int getChineseCount(String str) {
            int count = 0;
            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher(str);
            while (m.find()) {
                for (int i = 0; i <= m.groupCount(); i++) {
                    count = count + 1;
                }
            }
            return count;
        }
    }
}
