package com.feiyou.headstyle.model;

import android.content.Context;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.feiyou.headstyle.api.SignDoneInfoServiceApi;
import com.feiyou.headstyle.api.TaskRecordInfoServiceApi;
import com.feiyou.headstyle.base.BaseModel;
import com.feiyou.headstyle.base.IBaseRequestCallBack;
import com.feiyou.headstyle.bean.SignDoneInfoRet;
import com.feiyou.headstyle.bean.TaskRecordInfoRet;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by iflying on 2018/1/9.
 */

public class TaskRecordInfoModelImp extends BaseModel implements TaskRecordInfoModel<TaskRecordInfoRet> {

    private Context context = null;
    private TaskRecordInfoServiceApi taskRecordInfoServiceApi;
    private CompositeSubscription mCompositeSubscription;

    public TaskRecordInfoModelImp(Context mContext) {
        super();
        context = mContext;
        taskRecordInfoServiceApi = mRetrofit.create(TaskRecordInfoServiceApi.class);
        mCompositeSubscription = new CompositeSubscription();
    }

    @Override
    public void addTaskRecord(String uid, String taskId, int goldNum, double cash, int status, String recordId, IBaseRequestCallBack<TaskRecordInfoRet> iBaseRequestCallBack) {
        JSONObject params = new JSONObject();
        try {
            params.put("user_id", uid);
            params.put("taskid", taskId);
            params.put("goldnum", goldNum + "");
            params.put("cash", cash + "");
            params.put("status", status + "");
            params.put("infoid", recordId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), params.toString());

        mCompositeSubscription.add(taskRecordInfoServiceApi.addTaskRecord(requestBody)  //将subscribe添加到subscription，用于注销subscribe
                .observeOn(AndroidSchedulers.mainThread())//指定事件消费线程
                .subscribeOn(Schedulers.io())  //指定 subscribe() 发生在 IO 线程
                .subscribe(new Subscriber<TaskRecordInfoRet>() {

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
                    public void onNext(TaskRecordInfoRet taskRecordInfoRet) {
                        //回调接口：请求成功，获取实体类对象
                        iBaseRequestCallBack.requestSuccess(taskRecordInfoRet);
                    }
                }));
    }

}
