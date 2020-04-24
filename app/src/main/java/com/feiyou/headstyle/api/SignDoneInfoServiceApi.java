package com.feiyou.headstyle.api;

import com.feiyou.headstyle.bean.ExchangeInfoRet;
import com.feiyou.headstyle.bean.SignDoneInfoRet;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by iflying on 2018/2/6.
 */

public interface SignDoneInfoServiceApi {
    @POST("v2.welfare/addusersign")
    Observable<SignDoneInfoRet> signDone(@Body RequestBody requestBody);
}
