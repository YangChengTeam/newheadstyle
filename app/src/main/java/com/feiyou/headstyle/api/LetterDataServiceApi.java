package com.feiyou.headstyle.api;

import com.feiyou.headstyle.bean.LetterInfoRet;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by iflying on 2018/2/6.
 */

public interface LetterDataServiceApi {

    @POST("aquerywordtypes")
    Observable<LetterInfoRet> getDataByPage(@Body RequestBody requestBody);
}
