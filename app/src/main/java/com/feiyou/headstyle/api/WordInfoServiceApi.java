package com.feiyou.headstyle.api;

import com.feiyou.headstyle.bean.WordInfoRet;
import com.feiyou.headstyle.bean.WordTypeRet;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by iflying on 2018/2/6.
 */

public interface WordInfoServiceApi {

    @POST("aquerydatas")
    Observable<WordInfoRet> getDataByType(@Body RequestBody requestBody);
}
