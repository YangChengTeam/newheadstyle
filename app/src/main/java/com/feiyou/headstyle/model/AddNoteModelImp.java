package com.feiyou.headstyle.model;

import android.content.Context;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.feiyou.headstyle.api.CollectDataServiceApi;
import com.feiyou.headstyle.api.NoteDataServiceApi;
import com.feiyou.headstyle.base.BaseModel;
import com.feiyou.headstyle.base.BaseNoRsaModel;
import com.feiyou.headstyle.base.IBaseRequestCallBack;
import com.feiyou.headstyle.bean.CollectInfoRet;
import com.feiyou.headstyle.bean.ResultInfo;

import java.io.File;
import java.util.ArrayList;
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

public class AddNoteModelImp extends BaseNoRsaModel implements AddNoteModel<ResultInfo> {

    private Context context = null;
    private NoteDataServiceApi noteDataServiceApi;
    private CompositeSubscription mCompositeSubscription;

    public AddNoteModelImp(Context mContext) {
        super();
        context = mContext;
        noteDataServiceApi = mRetrofit.create(NoteDataServiceApi.class);
        mCompositeSubscription = new CompositeSubscription();
    }

    public static MultipartBody filesToMultipartBody(String uid,String content,String topicId,String friends,List<String> filePath) {
        MultipartBody.Builder builder = new MultipartBody.Builder();

        for (String path : filePath) {
            File tempFile = new File(path);
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/png"), tempFile);
            builder.addFormDataPart("file[]", tempFile.getName(), requestBody);
        }

        builder.addFormDataPart("user_id", uid);
        builder.addFormDataPart("content", content);
        builder.addFormDataPart("topic_id", topicId);
        builder.addFormDataPart("friends_id_str", friends);

        builder.setType(MultipartBody.FORM);
        MultipartBody multipartBody = builder.build();
        return multipartBody;
    }

    @Override
    public void addNote(String uid, String content, String topicId, String friends, List<String> filePaths, final IBaseRequestCallBack<ResultInfo> iBaseRequestCallBack) {

        MultipartBody multipartBody = filesToMultipartBody(uid,content,topicId,friends,filePaths);
        mCompositeSubscription.add(noteDataServiceApi.addNote(multipartBody)  //将subscribe添加到subscription，用于注销subscribe
                .observeOn(AndroidSchedulers.mainThread())//指定事件消费线程
                .subscribeOn(Schedulers.io())  //指定 subscribe() 发生在 IO 线程
                .subscribe(new Subscriber<ResultInfo>() {

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
                    public void onNext(ResultInfo resultInfo) {
                        //回调接口：请求成功，获取实体类对象
                        iBaseRequestCallBack.requestSuccess(resultInfo);
                    }
                }));
    }
}
