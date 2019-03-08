package com.feiyou.headstyle.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feiyou.headstyle.App;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.base.IBaseView;
import com.feiyou.headstyle.bean.ResultInfo;
import com.feiyou.headstyle.common.Constants;
import com.feiyou.headstyle.presenter.AddNotePresenterImp;
import com.feiyou.headstyle.ui.adapter.AddNoteImageAdapter;
import com.feiyou.headstyle.ui.base.BaseFragmentActivity;
import com.feiyou.headstyle.ui.custom.Glide4Engine;
import com.feiyou.headstyle.ui.custom.MsgEditText;
import com.feiyou.headstyle.utils.MyToastUtils;
import com.orhanobut.logger.Logger;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by myflying on 2018/11/23.
 */
public class PushNoteActivity extends BaseFragmentActivity implements IBaseView {

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

    private ProgressDialog progressDialog = null;

    private Map<String, String> friendsMap;

    private StringBuffer ids;

    private int topicSelectIndex = -1;

    private String topicId;

    private String sendContent;

    private String tempFilePath;

    private Drawable notAddTopicDw;

    private Drawable isAddTopicDw;

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

        mAddNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tempInputContent = mInputNoteEditText.getText().toString();
                if (StringUtils.isEmpty(tempInputContent)) {
                    ToastUtils.showLong("请输入内容");
                    return;
                }

                List<Object> tempList = addNoteImageAdapter.getData();
                List<String> result = new ArrayList<>();
                for (int i = 0; i < tempList.size(); i++) {
                    //排除+号
                    if (tempList.get(i) instanceof String) {
                        result.add(tempList.get(i).toString());
                    }
                }

                if (result.size() == 0) {
                    ToastUtils.showLong("请选择图片");
                    return;
                }

                if (topicSelectIndex == -1) {
                    ToastUtils.showLong("请选择话题");
                    return;
                }

                String tempIds = setFriendIds(tempInputContent);
                if (progressDialog != null && !progressDialog.isShowing()) {
                    progressDialog.show();
                }

                //TODO,此方法有问题
                Logger.i("sendContent--->" + sendContent);
                addNotePresenterImp.addNote(App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "", tempInputContent, "1", tempIds, result);
            }
        });

        Bundle bundle = getIntent().getExtras();
        if (bundle != null && !StringUtils.isEmpty(bundle.getString("file_path"))) {
            tempFilePath = bundle.getString("file_path");
        }

        List<Object> list = new ArrayList<>();
        if (!StringUtils.isEmpty(tempFilePath)) {
            list.add(tempFilePath);
            maxTotal = maxTotal - 1;
        }
        list.add(R.mipmap.add_my_photo);

        addNoteImageAdapter = new AddNoteImageAdapter(this, list);
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
        notAddTopicDw = ContextCompat.getDrawable(this, R.mipmap.not_add_note_topic);
        isAddTopicDw = ContextCompat.getDrawable(this, R.mipmap.is_add_topic_icon);

        friendsMap = new HashMap<>();
        ids = new StringBuffer("");

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在发布");

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
                    sendContent = sendContent.replace(val.toString(), "*#" + val + "*#");
                }
            }
            tempStr = ids.substring(0, ids.length() - 1);
        } else {
            tempStr = "";
        }

        Logger.i("result ids --->" + tempStr);
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
                    friendsMap.put(ids.get(i), names.get(i));
                    mInputNoteEditText.addAtSpan(MASK_STR, names.get(i), 100000);
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

    }

    @Override
    public void dismissProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void loadDataSuccess(Object tData) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        Logger.i(JSON.toJSONString(tData));
        if (tData instanceof ResultInfo) {
            if (((ResultInfo) tData).getCode() == Constants.SUCCESS) {
                MyToastUtils.showToast(this, 0, "发帖成功");
                finish();
            } else {
                MyToastUtils.showToast(this, 1, "发帖失败");
            }
        }
    }

    @Override
    public void loadDataError(Throwable throwable) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
