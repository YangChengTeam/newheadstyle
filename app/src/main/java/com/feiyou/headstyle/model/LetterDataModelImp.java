package com.feiyou.headstyle.model;

import android.content.Context;


import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.feiyou.headstyle.api.LetterDataServiceApi;
import com.feiyou.headstyle.base.BaseModel;
import com.feiyou.headstyle.base.IBaseRequestCallBack;
import com.feiyou.headstyle.bean.LetterInfoRet;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by iflying on 2018/1/9.
 */

public class LetterDataModelImp extends BaseModel implements LetterDataModel<LetterInfoRet> {

    private Context context = null;
    private LetterDataServiceApi hotDataServiceApi;
    private CompositeSubscription mCompositeSubscription;

    public LetterDataModelImp(Context mContext) {
        super();
        context = mContext;
        hotDataServiceApi = mRetrofit.create(LetterDataServiceApi.class);
        mCompositeSubscription = new CompositeSubscription();
    }

    @Override
    public void getLetterDataByPage(int page, final IBaseRequestCallBack<LetterInfoRet> iBaseRequestCallBack) {

        JSONObject params = new JSONObject();
        try {
            params.put("page", page + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),params.toString());

        mCompositeSubscription.add(hotDataServiceApi.getDataByPage(requestBody)  //将subscribe添加到subscription，用于注销subscribe
                .observeOn(AndroidSchedulers.mainThread())//指定事件消费线程
                .subscribeOn(Schedulers.io())  //指定 subscribe() 发生在 IO 线程
                .subscribe(new Subscriber<LetterInfoRet>() {

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
                    public void onNext(LetterInfoRet dataInfos) {
                        //回调接口：请求成功，获取实体类对象
                        iBaseRequestCallBack.requestSuccess(dataInfos);
                    }
                }));
    }
}
