package com.feiyou.headstyle.api;

import com.feiyou.headstyle.bean.TaskRecordInfoRet;
import com.feiyou.headstyle.bean.WelfareInfoRet;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by iflying on 2018/2/6.
 */

public interface TaskRecordInfoServiceApi {

    @POST("v1.welfare/addusertask")
    Observable<TaskRecordInfoRet> addTaskRecord(@Body RequestBody requestBody);



}
