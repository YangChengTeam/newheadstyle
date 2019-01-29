package com.feiyou.headstyle.model;

import android.content.Context;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.feiyou.headstyle.api.UserInfoServiceApi;
import com.feiyou.headstyle.api.WordInfoServiceApi;
import com.feiyou.headstyle.base.BaseModel;
import com.feiyou.headstyle.base.IBaseRequestCallBack;
import com.feiyou.headstyle.bean.LoginRequest;
import com.feiyou.headstyle.bean.UserInfoRet;
import com.feiyou.headstyle.bean.WordInfoRet;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by iflying on 2018/1/9.
 */

public class UserInfoModelImp extends BaseModel implements UserInfoModel<UserInfoRet> {

    private Context context = null;
    private UserInfoServiceApi userInfoServiceApi;
    private CompositeSubscription mCompositeSubscription;

    public UserInfoModelImp(Context mContext) {
        super();
        context = mContext;
        userInfoServiceApi = mRetrofit.create(UserInfoServiceApi.class);
        mCompositeSubscription = new CompositeSubscription();
    }

    @Override
    public void login(LoginRequest loginRequest, final IBaseRequestCallBack<UserInfoRet> iBaseRequestCallBack) {
        JSONObject params = new JSONObject();
        try {
            params.put("type", loginRequest.getType());
            params.put("openid", loginRequest.getOpenid());
            params.put("imeil", loginRequest.getImeil());
            params.put("sex", loginRequest.getSex());
            params.put("nickname", loginRequest.getNickname());
            params.put("userimg", loginRequest.getUserimg());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), params.toString());

        mCompositeSubscription.add(userInfoServiceApi.login(requestBody)  //将subscribe添加到subscription，用于注销subscribe
                .observeOn(AndroidSchedulers.mainThread())//指定事件消费线程
                .subscribeOn(Schedulers.io())  //指定 subscribe() 发生在 IO 线程
                .subscribe(new Subscriber<UserInfoRet>() {

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
                    public void onNext(UserInfoRet userInfoRet) {
                        //回调接口：请求成功，获取实体类对象
                        iBaseRequestCallBack.requestSuccess(userInfoRet);
                    }
                }));
    }

}
