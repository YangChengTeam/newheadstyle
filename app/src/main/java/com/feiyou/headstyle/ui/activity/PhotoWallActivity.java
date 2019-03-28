package com.feiyou.headstyle.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feiyou.headstyle.App;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.PhotoInfo;
import com.feiyou.headstyle.bean.PhotoWallRet;
import com.feiyou.headstyle.bean.UserInfo;
import com.feiyou.headstyle.common.Constants;
import com.feiyou.headstyle.presenter.UploadPhotoPresenterImp;
import com.feiyou.headstyle.ui.adapter.PhotoWallAdapter;
import com.feiyou.headstyle.ui.base.BaseFragmentActivity;
import com.feiyou.headstyle.ui.custom.Glide4Engine;
import com.feiyou.headstyle.view.PhotoWallDataView;
import com.orhanobut.logger.Logger;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.listener.OnCheckedListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

/**
 * Created by myflying on 2018/11/23.
 */
public class PhotoWallActivity extends BaseFragmentActivity implements View.OnClickListener, PhotoWallDataView {

    private static final int REQUEST_CODE_CHOOSE = 23;

    @BindView(R.id.topbar)
    QMUITopBar mTopBar;

    @BindView(R.id.layout_no_photo)
    LinearLayout mNoPhotoLayout;

    @BindView(R.id.photo_list)
    RecyclerView mPhotoListView;

    @BindView(R.id.btn_upload)
    Button mUploadButton;

    @BindView(R.id.layout_delete)
    LinearLayout mDeleteLayout;

    ImageView mBackImageView;

    PhotoWallAdapter photoWallAdapter;

    private boolean isEdit;

    private UserInfo userInfo;

    private List<PhotoInfo> photoList;

    TextView editTv;

    UploadPhotoPresenterImp uploadPhotoPresenterImp;

    private int totalCount = 9;

    private int maxTotal = 0;

    private ProgressDialog progressDialog = null;

    @Override
    protected int getContextViewId() {
        return R.layout.activity_photo_wall;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTopBar();
        initData();
    }

    private void initTopBar() {
        QMUIStatusBarHelper.setStatusBarLightMode(this);
        View topSearchView = getLayoutInflater().inflate(R.layout.common_top_config, null);
        topSearchView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(48)));

        TextView titleTv = topSearchView.findViewById(R.id.tv_config_title);
        editTv = topSearchView.findViewById(R.id.tv_config);
        titleTv.setText("照片墙");
        editTv.setText(isEdit ? "取消" : "编辑");

        mTopBar.setCenterView(topSearchView);
        mBackImageView = topSearchView.findViewById(R.id.iv_back);
        mBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });

        editTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isEdit = !isEdit;
                editTv.setText(isEdit ? "完成" : "编辑");
                mDeleteLayout.setVisibility(isEdit ? View.VISIBLE : View.GONE);
                mUploadButton.setVisibility(isEdit ? View.GONE : View.VISIBLE);
                photoWallAdapter.setOpenEdit(isEdit);
                photoWallAdapter.notifyDataSetChanged();
            }
        });
    }

    public void initData() {
        photoList = new ArrayList<>();

        if (!StringUtils.isEmpty(SPUtils.getInstance().getString(Constants.USER_INFO))) {
            Logger.i(SPUtils.getInstance().getString(Constants.USER_INFO));
            userInfo = JSON.parseObject(SPUtils.getInstance().getString(Constants.USER_INFO), new TypeReference<UserInfo>() {
            });
        }

        if (userInfo != null && userInfo.getImageWall() != null && userInfo.getImageWall().length > 0) {
            mNoPhotoLayout.setVisibility(View.GONE);
            mPhotoListView.setVisibility(View.VISIBLE);

            String[] tempPhotos = userInfo.getImageWall();
            for (int i = 0; i < tempPhotos.length; i++) {
                PhotoInfo photoInfo = new PhotoInfo();
                photoInfo.setUrl(tempPhotos[i]);
                photoList.add(photoInfo);
            }
        } else {
            mNoPhotoLayout.setVisibility(View.VISIBLE);
            mPhotoListView.setVisibility(View.GONE);
            editTv.setVisibility(View.GONE);
        }

        //单词最多选择上传3张
        maxTotal = (totalCount - photoList.size()) > 3 ? 3 : totalCount - photoList.size();

        if (maxTotal == 0) {
            mUploadButton.setVisibility(View.GONE);
        } else {
            mUploadButton.setVisibility(View.VISIBLE);
        }

        progressDialog = new ProgressDialog(this);

        uploadPhotoPresenterImp = new UploadPhotoPresenterImp(this, this);

        photoWallAdapter = new PhotoWallAdapter(this, photoList);
        mPhotoListView.setLayoutManager(new GridLayoutManager(this, 3));
        mPhotoListView.setAdapter(photoWallAdapter);
        photoWallAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                if (view.getId() == R.id.iv_choose) {

                    boolean temp = !photoWallAdapter.getData().get(position).isSelected();
                    photoWallAdapter.getData().get(position).setSelected(temp);
                    photoWallAdapter.notifyDataSetChanged();

                    if (!StringUtils.isEmpty(selectItemStr())) {
                        mDeleteLayout.setBackgroundResource(R.drawable.common_red_bg);
                    } else {
                        mDeleteLayout.setBackgroundResource(R.drawable.common_gray_bg);
                    }
                }
            }
        });

        photoWallAdapter.setOpenEdit(isEdit);
    }

    @OnClick(R.id.btn_upload)
    void uploadPhoto() {
        if (maxTotal > 0) {
            Matisse.from(PhotoWallActivity.this)
                    .choose(MimeType.ofImage())
                    .countable(true)
                    .maxSelectable(maxTotal)
                    .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                    .thumbnailScale(0.85f)
                    .imageEngine(new Glide4Engine())
                    .setOnCheckedListener(new OnCheckedListener() {
                        @Override
                        public void onCheck(boolean isChecked) {

                        }
                    })
                    .forResult(REQUEST_CODE_CHOOSE);
        } else {
            ToastUtils.showLong("不能上传更多图片哦");
        }
    }

    @OnClick(R.id.layout_delete)
    void deletePhoto() {
        if (StringUtils.isEmpty(selectItemStr())) {
            ToastUtils.showLong("请选择照片后删除");
        } else {
            String tempStr = selectItemStr();

            if (tempStr.length() > 0) {
                tempStr = tempStr.substring(0, tempStr.length() - 1);
            }

            if (progressDialog != null && !progressDialog.isShowing()) {
                progressDialog.setMessage("正在删除");
                progressDialog.show();
            }

            uploadPhotoPresenterImp.deletePhoto(App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "", tempStr);
        }
    }

    public String selectItemStr() {
        String selectItemStr = "";
        if (photoWallAdapter != null && photoWallAdapter.getData() != null) {
            for (int i = 0; i < photoWallAdapter.getData().size(); i++) {
                if (photoWallAdapter.getData().get(i).isSelected()) {
                    selectItemStr += photoWallAdapter.getData().get(i).getUrl() + ",";
                }
            }
        }
        return selectItemStr;
    }

    //重置数据的状态
    public void resetData() {
        if (photoWallAdapter != null && photoWallAdapter.getData() != null) {
            for (int i = 0; i < photoWallAdapter.getData().size(); i++) {
                photoWallAdapter.getData().get(i).setSelected(false);
            }
        }

        isEdit = false;
        editTv.setText(isEdit ? "取消" : "编辑");
        mDeleteLayout.setVisibility(isEdit ? View.VISIBLE : View.GONE);
        mUploadButton.setVisibility(isEdit ? View.GONE : View.VISIBLE);
        photoWallAdapter.setOpenEdit(isEdit);
        photoWallAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        if (isEdit) {
            resetData();
        } else {
            popBackStack();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_CHOOSE:
                    Logger.i(JSONObject.toJSONString(Matisse.obtainPathResult(data)));
                    if (Matisse.obtainPathResult(data) != null && Matisse.obtainPathResult(data).size() > 0) {

                        if (progressDialog != null && !progressDialog.isShowing()) {
                            progressDialog.setMessage("正在上传");
                            progressDialog.show();
                        }

                        List<String> tempList = Matisse.obtainPathResult(data);

                        uploadPhotoPresenterImp.uploadPhotoWall(App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "", tempList);
                    }
                    break;

            }
        }

    }

    @Override
    public void onClick(View view) {

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
    public void loadDataSuccess(PhotoWallRet tData) {
        Logger.i(JSON.toJSONString(tData));
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

        if (tData.getCode() == Constants.SUCCESS) {

            if (tData.getData() != null && tData.getData().getImageWall() != null && tData.getData().getImageWall().length > 0) {
                Toasty.normal(this, "上传成功").show();
                mNoPhotoLayout.setVisibility(View.GONE);
                mPhotoListView.setVisibility(View.VISIBLE);
                editTv.setVisibility(View.VISIBLE);

                String[] tempPhotos = tData.getData().getImageWall();

                if (photoList != null && photoList.size() > 0) {
                    photoList.clear();
                } else {
                    photoList = new ArrayList<>();
                }

                for (int i = 0; i < tempPhotos.length; i++) {
                    PhotoInfo photoInfo = new PhotoInfo();
                    photoInfo.setUrl(tempPhotos[i]);
                    photoList.add(photoInfo);
                }

                //单词最多选择上传3张
                maxTotal = (totalCount - photoList.size()) > 3 ? 3 : totalCount - photoList.size();

                if (maxTotal == 0) {
                    mUploadButton.setVisibility(View.GONE);
                } else {
                    mUploadButton.setVisibility(View.VISIBLE);
                }

                photoWallAdapter.setNewData(photoList);

                //照片墙更新后重新设置用户的信息
                if (userInfo != null) {
                    userInfo.setImageWall(tempPhotos);
                    App.getApp().setmUserInfo(userInfo);
                    SPUtils.getInstance().put(Constants.USER_INFO, JSONObject.toJSONString(userInfo));
                }

            } else {
                mNoPhotoLayout.setVisibility(View.VISIBLE);
                mPhotoListView.setVisibility(View.GONE);
                editTv.setVisibility(View.GONE);
            }
            resetData();
        } else {
            Toasty.normal(this, "上传失败").show();
            editTv.setVisibility(View.GONE);
            mNoPhotoLayout.setVisibility(View.VISIBLE);
            mPhotoListView.setVisibility(View.GONE);
        }
    }

    @Override
    public void loadDataError(Throwable throwable) {
        Toasty.normal(this, "上传失败").show();
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
