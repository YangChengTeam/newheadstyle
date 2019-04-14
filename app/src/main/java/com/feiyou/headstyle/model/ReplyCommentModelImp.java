package com.feiyou.headstyle.model;

import android.content.Context;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.feiyou.headstyle.api.NoteDataServiceApi;
import com.feiyou.headstyle.api.TopicDataServiceApi;
import com.feiyou.headstyle.base.BaseModel;
import com.feiyou.headstyle.base.IBaseRequestCallBack;
import com.feiyou.headstyle.bean.ReplyParams;
import com.feiyou.headstyle.bean.ReplyResultInfoRet;
import com.feiyou.headstyle.bean.TopicInfoRet;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by iflying on 2018/1/9.
 */

public class ReplyCommentModelImp extends BaseModel implements ReplyCommentModel<ReplyResultInfoRet> {

    private Context context = null;
    private NoteDataServiceApi noteDataServiceApi;
    private CompositeSubscription mCompositeSubscription;

    public ReplyCommentModelImp(Context mContext) {
        super();
        context = mContext;
        noteDataServiceApi = mRetrofit.create(NoteDataServiceApi.class);
        mCompositeSubscription = new CompositeSubscription();
    }

    @Override
    public void addReplyInfo(ReplyParams replyParams, final IBaseRequestCallBack<ReplyResultInfoRet> iBaseRequestCallBack) {

        JSONObject params = new JSONObject();
        try {

            if (replyParams != null) {
                switch (replyParams.getType()) {
                    case 1:
                        params.put("type", replyParams.getType() + "");
                        params.put("content", replyParams.getContent());
                        params.put("repeat_user_id", replyParams.getRepeatUserId());
                        params.put("friends_id_str", replyParams.getAtUserIds());
                        params.put(replyParams.getModelType() == 1 ? "message_id" : "vedio_id", replyParams.getMessageId());
                        break;
                    case 2:
                        params.put("type", replyParams.getType() + "");
                        params.put("content", replyParams.getContent());
                        params.put("repeat_user_id", replyParams.getRepeatUserId());
                        params.put("comment_id", replyParams.getCommentId());
                        params.put("user_id", replyParams.getRepeatCommentUserId());
                        params.put("friends_id_str", replyParams.getAtUserIds());
                        params.put(replyParams.getModelType() == 1 ? "message_id" : "vedio_id", replyParams.getMessageId());
                        break;
                    case 3:
                        params.put("type", replyParams.getType() + "");
                        params.put("content", replyParams.getContent());
                        params.put("comment_id",replyParams.getCommentId());
                        params.put("repeat_user_id", replyParams.getRepeatUserId());
                        params.put("repeat_id", replyParams.getRepeatId());
                        params.put("user_id", replyParams.getRepeatCommentUserId());
                        params.put("friends_id_str", replyParams.getAtUserIds());
                        params.put(replyParams.getModelType() == 1 ? "message_id" : "vedio_id", replyParams.getMessageId());
                        break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), params.toString());
        Observable<ReplyResultInfoRet> observable = replyParams.getModelType() == 1 ? noteDataServiceApi.replyComment(requestBody) : noteDataServiceApi.replyVideoComment(requestBody);
        mCompositeSubscription.add(observable  //将subscribe添加到subscription，用于注销subscribe
                .observeOn(AndroidSchedulers.mainThread())//指定事件消费线程
                .subscribeOn(Schedulers.io())  //指定 subscribe() 发生在 IO 线程
                .subscribe(new Subscriber<ReplyResultInfoRet>() {

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
                    public void onNext(ReplyResultInfoRet replyResultInfoRet) {
                        //回调接口：请求成功，获取实体类对象
                        iBaseRequestCallBack.requestSuccess(replyResultInfoRet);
                    }
                }));

    }

}
