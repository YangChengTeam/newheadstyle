package com.feiyou.headstyle.model;

import android.content.Context;

import com.feiyou.headstyle.api.StickerServiceApi;
import com.feiyou.headstyle.api.TopicDataServiceApi;
import com.feiyou.headstyle.base.BaseModel;
import com.feiyou.headstyle.base.IBaseRequestCallBack;
import com.feiyou.headstyle.bean.StickerInfoRet;
import com.feiyou.headstyle.bean.TopicInfoRet;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by iflying on 2018/1/9.
 */

public class StickerModelImp extends BaseModel implements StickerModel<StickerInfoRet> {

    private Context context = null;
    private StickerServiceApi stickerServiceApi;
    private CompositeSubscription mCompositeSubscription;

    public StickerModelImp(Context mContext) {
        super();
        context = mContext;
        stickerServiceApi = mRetrofit.create(StickerServiceApi.class);
        mCompositeSubscription = new CompositeSubscription();
    }

    @Override
    public void getDataList(IBaseRequestCallBack<StickerInfoRet> iBaseRequestCallBack) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), "");
        mCompositeSubscription.add(stickerServiceApi.getDataList(requestBody)  //将subscribe添加到subscription，用于注销subscribe
                .observeOn(AndroidSchedulers.mainThread())//指定事件消费线程
                .subscribeOn(Schedulers.io())  //指定 subscribe() 发生在 IO 线程
                .subscribe(new Subscriber<StickerInfoRet>() {

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
                    public void onNext(StickerInfoRet stickerInfoRet) {
                        //回调接口：请求成功，获取实体类对象
                        iBaseRequestCallBack.requestSuccess(stickerInfoRet);
                    }
                }));
    }
}
