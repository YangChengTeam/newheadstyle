package com.feiyou.headstyle.api;

import com.feiyou.headstyle.bean.TaskInfoRet;
import com.feiyou.headstyle.bean.TaskRecordInfoRet;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by iflying on 2018/2/6.
 */

public interface TaskInfoServiceApi {

    @POST("v2.welfare/gettaskinfo")
    Observable<TaskInfoRet> taskList(@Body RequestBody requestBody);

}
