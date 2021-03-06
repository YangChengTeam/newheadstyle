package com.feiyou.headstyle.ui.activity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.PathUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.feiyou.headstyle.App;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.ResultInfo;
import com.feiyou.headstyle.bean.UpdateHeadRet;
import com.feiyou.headstyle.bean.UserInfo;
import com.feiyou.headstyle.bean.UserInfoRet;
import com.feiyou.headstyle.common.Constants;
import com.feiyou.headstyle.presenter.UpdateHeadPresenterImp;
import com.feiyou.headstyle.presenter.UserInfoPresenterImp;
import com.feiyou.headstyle.ui.base.BaseFragmentActivity;
import com.feiyou.headstyle.ui.custom.ConfigDialog;
import com.feiyou.headstyle.ui.custom.Glide4Engine;
import com.feiyou.headstyle.ui.custom.GlideRoundTransform;
import com.feiyou.headstyle.view.UserInfoView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.orhanobut.logger.Logger;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by myflying on 2018/11/23.
 */

@RuntimePermissions
public class EditUserInfoActivity extends BaseFragmentActivity implements View.OnClickListener, UserInfoView, ConfigDialog.ConfigListener {

    private static final int REQUEST_CODE_CHOOSE = 23;

    private static final int TAKE_BIG_PICTURE = 1000;

    private static final int CROP_SMALL_PICTURE = 1003;

    @BindView(R.id.topbar)
    QMUITopBar mTopBar;

    @BindView(R.id.tv_sex_label)
    TextView mSexLabelTv;

    @BindView(R.id.et_user_nick_name)
    EditText mUserNickNameInputEt;

    @BindView(R.id.et_intro)
    EditText mIntroInputEt;

    @BindView(R.id.iv_user_head)
    ImageView mUserHeadIv;

    @BindView(R.id.tv_birthday)
    TextView mBirthdayTv;

    @BindView(R.id.layout_update_head)
    FrameLayout mUpdateHeadLayout;

    ImageView mBackImageView;

    BottomSheetDialog bottomSheetDialog;

    LinearLayout mBoyLayout;

    LinearLayout mGirlLayout;

    LinearLayout mCancelLayout;

    LinearLayout takePhotoLayout;

    LinearLayout localPhotoLayout;

    LinearLayout cancelPhotoLayout;

    private UserInfo userInfo;

    UserInfoPresenterImp userInfoPresenterImp;

    UpdateHeadPresenterImp updateHeadPresenterImp;

    private ProgressDialog progressDialog = null;

    BottomSheetDialog updateHeadBottomSheetDialog;

    private File outputImage;

    private Uri imageUri;

    ConfigDialog configDialog;

    private int selectSexItem = 1;

    public static final String[] constellationArray = {"水瓶座", "双鱼座", "白羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "魔羯座"};

    public static final int[] constellationEdgeDay = {20, 19, 21, 21, 21, 22, 23, 23, 23, 23, 22, 22};

    private int maxInputLength = 280;

    private int nickNameMaxLen = 100;

    private String updateBir;

    private String updateStar;

    @Override
    protected int getContextViewId() {
        return R.layout.activity_edit_user_info;
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
        TextView saveTv = topSearchView.findViewById(R.id.tv_config);
        titleTv.setText("编辑个人资料");
        saveTv.setText("保存");

        mTopBar.setCenterView(topSearchView);
        mBackImageView = topSearchView.findViewById(R.id.iv_back);
        mBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });

        //设置过滤器，
        InputFilter[] filters = {new NameLengthFilter(maxInputLength)};
        mIntroInputEt.setFilters(filters);

        InputFilter[] nickFilters = {new NameLengthFilter(nickNameMaxLen)};
        mUserNickNameInputEt.setFilters(nickFilters);

        saveTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (progressDialog != null && !progressDialog.isShowing()) {
                    progressDialog.show();
                }

                if (StringUtils.isEmpty(mUserNickNameInputEt.getText())) {
                    ToastUtils.showLong("请填写昵称");
                    return;
                }

                userInfo.setNickname(mUserNickNameInputEt.getText().toString());
                userInfo.setBirthday(updateBir);
                userInfo.setStar(updateStar);
                userInfo.setIntro(mIntroInputEt.getText().toString());
                userInfoPresenterImp.updateUserInfo(userInfo);
            }
        });

    }

    public void initData() {
        configDialog = new ConfigDialog(this, R.style.login_dialog, 1, "确认修改吗?", "每个用户只有一次修改性别的机会，你确认修改吗");
        configDialog.setConfigListener(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在保存");
        progressDialog.setCanceledOnTouchOutside(false);

        bottomSheetDialog = new BottomSheetDialog(this);
        updateHeadBottomSheetDialog = new BottomSheetDialog(this);

        //性别
        View sexView = LayoutInflater.from(this).inflate(R.layout.view_sex_dialog, null);
        mBoyLayout = sexView.findViewById(R.id.layout_sex_boy);
        mGirlLayout = sexView.findViewById(R.id.layout_sex_girl);
        mCancelLayout = sexView.findViewById(R.id.layout_cancel);
        mBoyLayout.setOnClickListener(this);
        mGirlLayout.setOnClickListener(this);
        mCancelLayout.setOnClickListener(this);
        bottomSheetDialog.setContentView(sexView);

        //头像
        View headSelectView = LayoutInflater.from(this).inflate(R.layout.photo_select_view, null);
        takePhotoLayout = headSelectView.findViewById(R.id.layout_camera);
        localPhotoLayout = headSelectView.findViewById(R.id.layout_local_photo);
        cancelPhotoLayout = headSelectView.findViewById(R.id.layout_head_select_cancel);
        takePhotoLayout.setOnClickListener(this);
        localPhotoLayout.setOnClickListener(this);
        cancelPhotoLayout.setOnClickListener(this);

        updateHeadBottomSheetDialog.setContentView(headSelectView);

        updateHeadPresenterImp = new UpdateHeadPresenterImp(this, this);
        userInfoPresenterImp = new UserInfoPresenterImp(this, this);
    }

    @Override
    public void onResume() {
        super.onResume();

        userInfo = App.getApp().getmUserInfo();

        if (userInfo != null) {
            mUserNickNameInputEt.setText(userInfo.getNickname());
            RequestOptions options = new RequestOptions().skipMemoryCache(true);
            options.transform(new GlideRoundTransform(this, 30));
            Glide.with(this).load(userInfo.getUserimg()).apply(options).into(mUserHeadIv);
            mIntroInputEt.setText(userInfo.getIntro());
            String tempStar = StringUtils.isEmpty(userInfo.getStar()) ? "" : userInfo.getStar();
            mBirthdayTv.setText(StringUtils.isEmpty(userInfo.getBirthday()) ? "" : userInfo.getBirthday() + "  " + tempStar);
            mSexLabelTv.setText(userInfo.getSex() == 1 ? "男" : "女");
            updateBir = userInfo.getBirthday();
            updateStar = userInfo.getStar();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EditUserInfoActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @NeedsPermission(Manifest.permission.CAMERA)
    void showCamera() {
        //ToastUtils.showLong("允许使用存储权限");
        if (updateHeadBottomSheetDialog != null && !updateHeadBottomSheetDialog.isShowing()) {
            updateHeadBottomSheetDialog.show();
        }
    }

    @OnPermissionDenied(Manifest.permission.CAMERA)
    void onCameraDenied() {
        Toast.makeText(this, R.string.permission_camera_denied, Toast.LENGTH_SHORT).show();
    }

    @OnShowRationale(Manifest.permission.CAMERA)
    void showRationaleForCamera(PermissionRequest request) {
        showRationaleDialog(R.string.permission_camera_rationale, request);
    }

    @OnNeverAskAgain(Manifest.permission.CAMERA)
    void onCameraNeverAskAgain() {
        Toast.makeText(this, R.string.permission_camera_never_ask_again, Toast.LENGTH_SHORT).show();
    }

    private void showRationaleDialog(@StringRes int messageResId, final PermissionRequest request) {
        new AlertDialog.Builder(this)
                .setPositiveButton(R.string.button_allow, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        request.proceed();
                    }
                })
                .setNegativeButton(R.string.button_deny, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        request.cancel();
                    }
                })
                .setCancelable(false)
                .setMessage(messageResId)
                .show();
    }

    @OnClick(R.id.layout_update_head)
    void updateHead() {
        EditUserInfoActivityPermissionsDispatcher.showCameraWithPermissionCheck(this);
    }

    @OnClick(R.id.layout_sex)
    public void chooseSex() {
        if (userInfo.getSexCanChange() == 0) {
            ToastUtils.showLong("暂无权限修改");
            return;
        }

        if (bottomSheetDialog != null && !bottomSheetDialog.isShowing()) {
            bottomSheetDialog.show();
        }
    }

    @OnClick(R.id.layout_birthday)
    public void birthday() {
        Calendar cal = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Logger.i("year--->" + year + "---month--->" + (monthOfYear + 1) + "---day--->" + dayOfMonth);

                updateBir = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                updateStar = date2Constellation(updateBir);

                mBirthdayTv.setText(updateBir + "  " + updateStar);
            }
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));

        dialog.show();
    }

    @Override
    public void onBackPressed() {
        popBackStack();
    }

    @Override
    public void onClick(View view) {
        if (bottomSheetDialog != null && bottomSheetDialog.isShowing()) {
            bottomSheetDialog.dismiss();
        }
        if (updateHeadBottomSheetDialog != null && updateHeadBottomSheetDialog.isShowing()) {
            updateHeadBottomSheetDialog.dismiss();
        }
        switch (view.getId()) {
            case R.id.layout_sex_boy:
                selectSexItem = 1;
                if (configDialog != null && !configDialog.isShowing()) {
                    configDialog.show();
                }
                break;
            case R.id.layout_sex_girl:
                selectSexItem = 2;
                if (configDialog != null && !configDialog.isShowing()) {
                    configDialog.show();
                }
                break;
            case R.id.layout_cancel:
                break;
            case R.id.layout_camera:
                takeCamera();
                break;
            case R.id.layout_local_photo:
                Matisse.from(EditUserInfoActivity.this)
                        .choose(MimeType.ofImage())
                        .countable(true)
                        .maxSelectable(1)
                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                        .thumbnailScale(0.85f)
                        .imageEngine(new Glide4Engine())
                        .forResult(REQUEST_CODE_CHOOSE);
                break;
            case R.id.layout_head_select_cancel:

                break;
            default:
                break;
        }
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
    public void loadDataSuccess(ResultInfo tData) {
        Logger.i(JSON.toJSONString(tData));

        KeyboardUtils.hideSoftInput(this);

        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

        if (tData != null && tData.getCode() == Constants.SUCCESS) {
            if (tData instanceof UserInfoRet) {
                //MyToastUtils.showToast(this, 0, "修改成功");
                Toasty.normal(this, "修改成功").show();

                userInfo.setNickname(((UserInfoRet) tData).getData().getNickname());
                userInfo.setIntro(((UserInfoRet) tData).getData().getIntro());
                userInfo.setBirthday(((UserInfoRet) tData).getData().getBirthday());
                userInfo.setStar(((UserInfoRet) tData).getData().getStar());
                userInfo.setSex(((UserInfoRet) tData).getData().getSex());
                userInfo.setSexCanChange(((UserInfoRet) tData).getData().getSexCanChange());
                App.getApp().setmUserInfo(userInfo);
                App.getApp().setLogin(true);
                SPUtils.getInstance().put(Constants.USER_INFO, JSONObject.toJSONString(userInfo));
                finish();
            }

            if (tData instanceof UpdateHeadRet) {
                if (!StringUtils.isEmpty(((UpdateHeadRet) tData).getData().getImage())) {
                    RequestOptions myOptions = new RequestOptions()
                            .transform(new GlideRoundTransform(this, 33));
                    Glide.with(this).load(((UpdateHeadRet) tData).getData().getImage()).apply(myOptions).into(mUserHeadIv);

                    userInfo.setUserimg(((UpdateHeadRet) tData).getData().getImage());

                    App.getApp().setmUserInfo(userInfo);
                    App.getApp().setLogin(true);
                    SPUtils.getInstance().put(Constants.USER_INFO, JSONObject.toJSONString(userInfo));
                }
            }

        } else {
            //MyToastUtils.showToast(this, 1, "修改失败");
            Logger.i(tData.getMsg() != null ? tData.getMsg() : "操作错误");
            Toasty.normal(this, tData.getMsg() != null ? tData.getMsg() : "操作错误").show();
        }
    }

    @Override
    public void loadDataError(Throwable throwable) {
        KeyboardUtils.hideSoftInput(this);
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        Toasty.normal(this, "操作错误").show();
    }

    /**
     * 使用相机
     */
    private void takeCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        outputImage = new File(PathUtils.getExternalAppPicturesPath(), TimeUtils.getNowMills() + ".png");
        outputImage.getParentFile().mkdirs();

        Uri uri = null;
        Logger.i("currentApiVersion--->" + android.os.Build.VERSION.SDK_INT);
        if (android.os.Build.VERSION.SDK_INT < 24) {
            uri = Uri.fromFile(outputImage);
            imageUri = uri;
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(intent, TAKE_BIG_PICTURE);
        } else {
            ContentValues contentValues = new ContentValues(1);
            contentValues.put(MediaStore.Images.Media.DATA, outputImage.getAbsolutePath());
            uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            imageUri = uri;
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(intent, TAKE_BIG_PICTURE);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case TAKE_BIG_PICTURE:
                    if (imageUri == null) {
                        ToastUtils.showLong("数据异常，请重试");
                        break;
                    }
                    cropImageUri(imageUri, 300, 300, CROP_SMALL_PICTURE);
                    break;
                case REQUEST_CODE_CHOOSE:
                    Logger.i(JSONObject.toJSONString(Matisse.obtainPathResult(data)));
                    if (Matisse.obtainResult(data) != null && Matisse.obtainResult(data).size() > 0) {
                        outputImage = new File(PathUtils.getExternalAppPicturesPath(), TimeUtils.getNowMills() + ".png");
                        imageUri = Uri.fromFile(outputImage);
                        cropImageUri(Matisse.obtainResult(data).get(0), 300, 300, CROP_SMALL_PICTURE);
                    }
                    break;
                case CROP_SMALL_PICTURE:
                    Logger.i("crop out path--->" + outputImage.getAbsolutePath());

                    if (updateHeadBottomSheetDialog != null && updateHeadBottomSheetDialog.isShowing()) {
                        updateHeadBottomSheetDialog.dismiss();
                    }
                    if (progressDialog != null && !progressDialog.isShowing()) {
                        progressDialog.show();
                    }
                    updateHeadPresenterImp.updateHead(userInfo != null ? userInfo.getId() : "", outputImage.getAbsolutePath());

                    //updateInfoPresenterImp.updateHead(userInfo != null ? userInfo.getUserId() : "", outputImage);
                    break;
            }
        }

    }

    private void cropImageUri(Uri uri, int outputX, int outputY, int requestCode) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        //是否裁剪
        intent.putExtra("crop", "true");
        //设置xy的裁剪比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        //设置输出的宽高
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        //是否缩放
        intent.putExtra("scale", false);
        //输入图片的Uri，指定以后，可以在这个uri获得图片
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        //是否返回图片数据，可以不用，直接用uri就可以了
        intent.putExtra("return-data", false);
        //设置输入图片格式
        intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());
        //是否关闭面部识别
        intent.putExtra("noFaceDetection", true); // no face detection
        //启动
        startActivityForResult(intent, requestCode);
    }


    @Override
    public void config() {
        mSexLabelTv.setText(selectSexItem == 1 ? "男" : "女");
        userInfo.setSex(selectSexItem);
    }

    @Override
    public void cancel() {
        if (configDialog != null && configDialog.isShowing()) {
            configDialog.dismiss();
        }
        if (bottomSheetDialog != null && bottomSheetDialog.isShowing()) {
            bottomSheetDialog.dismiss();
        }
    }

    /**
     * 根据日期获取星座
     *
     * @param time
     * @return
     */
    public static String date2Constellation(Calendar time) {
        int month = time.get(Calendar.MONTH);
        int day = time.get(Calendar.DAY_OF_MONTH);
        if (day < constellationEdgeDay[month]) {
            month = month - 1;
        }
        if (month >= 0) {
            return constellationArray[month];
        }
        // default to return 魔羯
        return constellationArray[11];
    }

    /**
     * 根据日期获取星座
     *
     * @param time
     * @return
     */
    public String date2Constellation(String time) {

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        try {
            date = sdf.parse(time);
            cal.setTime(date);

            String constellation = date2Constellation(cal);
            Logger.i("星座--->" + constellation);
            return constellation;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
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
                Toasty.normal(EditUserInfoActivity.this, "字数达到上限").show();
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
