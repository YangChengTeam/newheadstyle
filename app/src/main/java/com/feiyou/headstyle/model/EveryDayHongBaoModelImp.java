package com.feiyou.headstyle.model;

import android.content.Context;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.feiyou.headstyle.App;
import com.feiyou.headstyle.api.CollectionDataServiceApi;
import com.feiyou.headstyle.api.EveryDayServiceApi;
import com.feiyou.headstyle.base.BaseModel;
import com.feiyou.headstyle.base.IBaseRequestCallBack;
import com.feiyou.headstyle.bean.AddCollectionRet;
import com.feiyou.headstyle.bean.EveryDayHbRet;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by iflying on 2018/1/9.
 */

public class EveryDayHongBaoModelImp extends BaseModel implements EveryDayHongBaoModel<EveryDayHbRet> {

    private Context context = null;
    private EveryDayServiceApi everyDayServiceApi;
    private CompositeSubscription mCompositeSubscription;

    public EveryDayHongBaoModelImp(Context mContext) {
        super();
        context = mContext;
        everyDayServiceApi = mRetrofit.create(EveryDayServiceApi.class);
        mCompositeSubscription = new CompositeSubscription();
    }

    @Override
    public void everyDayHongBaoInfo(String uid, String openid, String imei, IBaseRequestCallBack<EveryDayHbRet> iBaseRequestCallBack) {
        JSONObject params = new JSONObject();
        try {
            params.put("user_id", uid);
            params.put("openid", openid);
            params.put("imei", imei);
            params.put("android_id", App.androidId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), params.toString());

        mCompositeSubscription.add(everyDayServiceApi.everyDayHongBaoInfo(requestBody)  //将subscribe添加到subscription，用于注销subscribe
                .observeOn(AndroidSchedulers.mainThread())//指定事件消费线程
                .subscribeOn(Schedulers.io())  //指定 subscribe() 发生在 IO 线程
                .subscribe(new Subscriber<EveryDayHbRet>() {

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
                    public void onNext(EveryDayHbRet everyDayHbRet) {
                        //回调接口：请求成功，获取实体类对象
                        iBaseRequestCallBack.requestSuccess(everyDayHbRet);
                    }
                }));
    }
}
