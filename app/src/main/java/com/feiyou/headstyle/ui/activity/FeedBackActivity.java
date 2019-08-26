package com.feiyou.headstyle.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feiyou.headstyle.App;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.FeedBackRet;
import com.feiyou.headstyle.common.Constants;
import com.feiyou.headstyle.presenter.FeedBackPresenterImp;
import com.feiyou.headstyle.ui.adapter.AddNoteImageAdapter;
import com.feiyou.headstyle.ui.adapter.BlackListAdapter;
import com.feiyou.headstyle.ui.base.BaseFragmentActivity;
import com.feiyou.headstyle.ui.custom.Glide4Engine;
import com.feiyou.headstyle.view.FeedBackView;
import com.orhanobut.logger.Logger;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

/**
 * Created by myflying on 2018/11/23.
 */
public class FeedBackActivity extends BaseFragmentActivity implements FeedBackView {

    public static final int REQUEST_CODE_CHOOSE = 23;

    public static final int REQUEST_FRIENDS = 0;

    public static final int RESULT_FRIEND_CODE = 2;

    public static final int RESULT_TOPIC_CODE = 3;

    @BindView(R.id.topbar)
    QMUITopBar mTopBar;

    @BindView(R.id.add_image_list)
    RecyclerView mImageListView;

    @BindView(R.id.et_content)
    EditText mContentEt;

    @BindView(R.id.et_input_phone_email)
    EditText mPhoneEmailEt;

    ImageView mBackImageView;

    AddNoteImageAdapter addNoteImageAdapter;

    private int maxTotal = 3;

    FeedBackPresenterImp feedBackPresenterImp;

    private ProgressDialog progressDialog = null;

    @Override
    protected int getContextViewId() {
        return R.layout.activity_feed_back;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTopBar();
        initData();
    }

    private void initTopBar() {
        QMUIStatusBarHelper.setStatusBarLightMode(this);
        View topSearchView = getLayoutInflater().inflate(R.layout.common_top_back, null);
        topSearchView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(48)));
        TextView titleTv = topSearchView.findViewById(R.id.tv_title);
        titleTv.setText("意见反馈");

        mTopBar.setCenterView(topSearchView);
        mBackImageView = topSearchView.findViewById(R.id.iv_back);
        mBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });
    }

    public void initData() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在提交");

        List<Object> list = new ArrayList<>();
        list.add(R.mipmap.add_my_photo);

        addNoteImageAdapter = new AddNoteImageAdapter(this, list, 2);
        mImageListView.setLayoutManager(new GridLayoutManager(this, 3));
        mImageListView.setAdapter(addNoteImageAdapter);
        addNoteImageAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                Matisse.from(FeedBackActivity.this)
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

        feedBackPresenterImp = new FeedBackPresenterImp(this, this);
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
                        if (addNoteImageAdapter.getData().size() < 3) {
                            addNoteImageAdapter.addData(R.mipmap.add_my_photo);
                        }
                    }
                    break;

            }
        }
    }

    @OnClick(R.id.btn_submit)
    void submit() {

        String tempInputContent = mContentEt.getText().toString();
        String phoneMail = mPhoneEmailEt.getText().toString();
        if (StringUtils.isEmpty(tempInputContent)) {
            Toasty.normal(this, "请输入反馈内容").show();
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

        if(result.size() == 0){
            Toasty.normal(this, "请上传图片").show();
            return;
        }

        if (StringUtils.isEmpty(phoneMail)) {
            Toasty.normal(this, "请输入你的QQ号").show();
            return;
        }

        if (progressDialog != null && !progressDialog.isShowing()) {
            progressDialog.show();
        }
        feedBackPresenterImp.addFeedBack(App.getApp().getmUserInfo().getId(), mContentEt.getText().toString(), mPhoneEmailEt.getText().toString(), result);
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
    public void loadDataSuccess(FeedBackRet tData) {
        Logger.i("json result--->" + JSON.toJSONString(tData));
        if (tData != null && tData.getCode() == Constants.SUCCESS) {
            Toasty.normal(this, "反馈成功").show();
            finish();
        } else {
            Toasty.normal(this, StringUtils.isEmpty(tData.getMsg()) ? "操作错误" : tData.getMsg()).show();
        }
    }

    @Override
    public void loadDataError(Throwable throwable) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
