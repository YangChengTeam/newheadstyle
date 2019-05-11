package com.feiyou.headstyle.model;

import android.content.Context;
import android.provider.Settings;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.PhoneUtils;
import com.feiyou.headstyle.api.NoteDataServiceApi;
import com.feiyou.headstyle.api.RecordServiceApi;
import com.feiyou.headstyle.base.BaseModel;
import com.feiyou.headstyle.base.IBaseRequestCallBack;
import com.feiyou.headstyle.bean.NoteTypeRet;
import com.feiyou.headstyle.bean.RecordInfoRet;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by iflying on 2018/1/9.
 */

public class RecordInfoModelImp extends BaseModel implements RecordInfoModel<RecordInfoRet> {

    private Context context = null;
    private RecordServiceApi recordServiceApi;
    private CompositeSubscription mCompositeSubscription;

    public RecordInfoModelImp(Context mContext) {
        super();
        context = mContext;
        recordServiceApi = mRetrofit.create(RecordServiceApi.class);
        mCompositeSubscription = new CompositeSubscription();
    }

    @Override
    public void headSetInfo(String uid, String headId, IBaseRequestCallBack<RecordInfoRet> iBaseRequestCallBack) {
        JSONObject params = new JSONObject();
        try {
            params.put("image_id", headId);
            params.put("user_id", uid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), params.toString());

        mCompositeSubscription.add(recordServiceApi.headSetInfo(requestBody)  //将subscribe添加到subscription，用于注销subscribe
                .observeOn(AndroidSchedulers.mainThread())//指定事件消费线程
                .subscribeOn(Schedulers.io())  //指定 subscribe() 发生在 IO 线程
                .subscribe(new Subscriber<RecordInfoRet>() {

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
                    public void onNext(RecordInfoRet recordInfoRet) {
                        //回调接口：请求成功，获取实体类对象
                        iBaseRequestCallBack.requestSuccess(recordInfoRet);
                    }
                }));
    }

    @Override
    public void adClickInfo(String uid, String aid, IBaseRequestCallBack<RecordInfoRet> iBaseRequestCallBack) {
        JSONObject params = new JSONObject();
        try {
            //String uuid = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            params.put("ad_id", aid);
            params.put("user_id", uid);
            params.put("imeil", PhoneUtils.getDeviceId());
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), params.toString());

        mCompositeSubscription.add(recordServiceApi.adClickInfo(requestBody)  //将subscribe添加到subscription，用于注销subscribe
                .observeOn(AndroidSchedulers.mainThread())//指定事件消费线程
                .subscribeOn(Schedulers.io())  //指定 subscribe() 发生在 IO 线程
                .subscribe(new Subscriber<RecordInfoRet>() {

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
                    public void onNext(RecordInfoRet recordInfoRet) {
                        //回调接口：请求成功，获取实体类对象
                        iBaseRequestCallBack.requestSuccess(recordInfoRet);
                    }
                }));
    }

}
