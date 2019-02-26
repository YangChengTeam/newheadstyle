package com.feiyou.headstyle.model;

import android.content.Context;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.feiyou.headstyle.api.NoteDataServiceApi;
import com.feiyou.headstyle.base.BaseModel;
import com.feiyou.headstyle.base.BaseNoRsaModel;
import com.feiyou.headstyle.base.IBaseRequestCallBack;
import com.feiyou.headstyle.bean.NoteSubCommentRet;
import com.feiyou.headstyle.bean.PhotoWallRet;
import com.feiyou.headstyle.bean.ResultInfo;
import com.feiyou.headstyle.bean.ZanResultRet;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by iflying on 2018/1/9.
 */

public class AddZanModelImp extends BaseModel implements AddZanModel<ZanResultRet> {

    private Context context = null;
    private NoteDataServiceApi noteDataServiceApi;
    private CompositeSubscription mCompositeSubscription;

    public AddZanModelImp(Context mContext) {
        super();
        context = mContext;
        noteDataServiceApi = mRetrofit.create(NoteDataServiceApi.class);
        mCompositeSubscription = new CompositeSubscription();
    }

    @Override
    public void addZan(int type, String userId, String messageId, String commentId, String repeatId,int modelType, final IBaseRequestCallBack<ZanResultRet> iBaseRequestCallBack) {

        JSONObject params = new JSONObject();
        try {
            switch (type) {
                case 1:
                    params.put("type", type + "");
                    params.put("user_id", userId);
                    params.put(modelType == 1 ? "message_id": "vedioe_id", messageId);
                    break;
                case 2:
                    params.put("type", type + "");
                    params.put("user_id", userId);
                    params.put("comment_id", commentId);
                    break;
                case 3:
                    params.put("type", type + "");
                    params.put("user_id", userId);
                    params.put("repeat_id", repeatId);
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), params.toString());

        Observable<ZanResultRet> observable = modelType == 1 ? noteDataServiceApi.addZan(requestBody) : noteDataServiceApi.addVideoZan(requestBody);
        mCompositeSubscription.add(observable  //将subscribe添加到subscription，用于注销subscribe
                .observeOn(AndroidSchedulers.mainThread())//指定事件消费线程
                .subscribeOn(Schedulers.io())  //指定 subscribe() 发生在 IO 线程
                .subscribe(new Subscriber<ZanResultRet>() {

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
                    public void onNext(ZanResultRet zanResultRet) {
                        //回调接口：请求成功，获取实体类对象
                        iBaseRequestCallBack.requestSuccess(zanResultRet);
                    }
                }));
    }
}
