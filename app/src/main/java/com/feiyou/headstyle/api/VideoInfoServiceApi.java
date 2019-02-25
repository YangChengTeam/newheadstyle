package com.feiyou.headstyle.api;

import com.feiyou.headstyle.bean.CollectInfoRet;
import com.feiyou.headstyle.bean.VideoInfoRet;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by iflying on 2018/2/6.
 */

public interface VideoInfoServiceApi {

    @POST("v1.vedio/vedioList")
    Observable<VideoInfoRet> getDataList(@Body RequestBody requestBody);
}
