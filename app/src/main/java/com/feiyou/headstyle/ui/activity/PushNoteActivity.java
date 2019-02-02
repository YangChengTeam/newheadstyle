package com.feiyou.headstyle.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.base.IBaseView;
import com.feiyou.headstyle.bean.ResultInfo;
import com.feiyou.headstyle.common.Constants;
import com.feiyou.headstyle.presenter.AddNotePresenterImp;
import com.feiyou.headstyle.ui.adapter.AddNoteImageAdapter;
import com.feiyou.headstyle.ui.base.BaseFragmentActivity;
import com.feiyou.headstyle.ui.custom.Glide4Engine;
import com.orhanobut.logger.Logger;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by myflying on 2018/11/23.
 */
public class PushNoteActivity extends BaseFragmentActivity implements IBaseView {

    private static final int REQUEST_CODE_CHOOSE = 23;

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

    TextView mCancelTv;

    Button mAddNoteBtn;

    AddNoteImageAdapter addNoteImageAdapter;

    private int maxTotal = 9;

    AddNotePresenterImp addNotePresenterImp;

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

        mAddNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                List<Object> tempList = addNoteImageAdapter.getData();
                List<String> result = new ArrayList<>();
                for (int i = 0; i < tempList.size(); i++) {
                    //排除+号
                    if (tempList.get(i) instanceof String) {
                        result.add(tempList.get(i).toString());
                    }
                }
                if (progressDialog != null && !progressDialog.isShowing()) {
                    progressDialog.show();
                }

                addNotePresenterImp.addNote("1", "测试发帖的消息", "1", "2,3", result);
            }
        });

        List<Object> list = new ArrayList<>();
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
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在发布");

        addNotePresenterImp = new AddNotePresenterImp(this, this);
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
                ToastUtils.showLong("发布成功");
                finish();
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
