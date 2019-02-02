package com.feiyou.headstyle.model;

import android.content.Context;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.feiyou.headstyle.api.UserInfoServiceApi;
import com.feiyou.headstyle.base.BaseModel;
import com.feiyou.headstyle.base.BaseNoRsaModel;
import com.feiyou.headstyle.base.IBaseRequestCallBack;
import com.feiyou.headstyle.bean.LoginRequest;
import com.feiyou.headstyle.bean.PhotoWallRet;
import com.feiyou.headstyle.bean.ResultInfo;
import com.feiyou.headstyle.bean.UserInfoRet;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by iflying on 2018/1/9.
 */

public class UploadPhotoModelImp extends BaseNoRsaModel implements UploadPhotoModel<PhotoWallRet> {

    private Context context = null;
    private UserInfoServiceApi userInfoServiceApi;
    private CompositeSubscription mCompositeSubscription;

    public UploadPhotoModelImp(Context mContext) {
        super();
        context = mContext;
        userInfoServiceApi = mRetrofit.create(UserInfoServiceApi.class);
        mCompositeSubscription = new CompositeSubscription();
    }

    public static MultipartBody filesToMultipartBody(String uid, List<String> filePath) {
        MultipartBody.Builder builder = new MultipartBody.Builder();

        for (String path : filePath) {
            File tempFile = new File(path);
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/png"), tempFile);
            builder.addFormDataPart("file[]", tempFile.getName(), requestBody);
        }

        builder.addFormDataPart("user_id", uid);

        builder.setType(MultipartBody.FORM);
        MultipartBody multipartBody = builder.build();
        return multipartBody;
    }

    @Override
    public void uploadPhotoWall(String uid, List<String> filePaths, final IBaseRequestCallBack<PhotoWallRet> iBaseRequestCallBack) {
        MultipartBody multipartBody = filesToMultipartBody(uid, filePaths);
        mCompositeSubscription.add(userInfoServiceApi.uploadPhotoWall(multipartBody)  //将subscribe添加到subscription，用于注销subscribe
                .observeOn(AndroidSchedulers.mainThread())//指定事件消费线程
                .subscribeOn(Schedulers.io())  //指定 subscribe() 发生在 IO 线程
                .subscribe(new Subscriber<PhotoWallRet>() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        //onStart它总是在 subscribe 所发生的线程被调用 ,如果你的subscribe不是主线程，则会出错，则需要指定线程;
                        iBaseRequestCallBack.beforeRequest();
                    }

                    @Override
                    public void onCompleted() {
                        //回调接口：请求已完成，可以隐藏progress
                        iBaseRequestCallBack.requestComplete();
                    }

                    @Override
                    public void onError(Throwable e) {
                        //回调接口：请求异常
                        iBaseRequestCallBack.requestError(e);
                    }

                    @Override
                    public void onNext(PhotoWallRet photoWallRet) {
                        //回调接口：请求成功，获取实体类对象
                        iBaseRequestCallBack.requestSuccess(photoWallRet);
                    }
                }));
    }

}
