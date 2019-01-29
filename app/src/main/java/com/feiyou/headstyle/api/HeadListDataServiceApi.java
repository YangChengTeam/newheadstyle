package com.feiyou.headstyle.api;

import com.feiyou.headstyle.bean.HeadInfoRet;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by iflying on 2018/2/6.
 */

public interface HeadListDataServiceApi {

    @POST("v1.show/imagesTagsList")
    Observable<HeadInfoRet> getDataByTagId(@Body RequestBody requestBody);

    @POST("v1.images/searchResult")
    Observable<HeadInfoRet> searchList(@Body RequestBody requestBody);
}
