package com.feiyou.headstyle.api;

import com.feiyou.headstyle.bean.ExchangeInfoRet;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by iflying on 2018/2/6.
 */

public interface ExchangeInfoServiceApi {

    @POST("v1.welfare/addgoodschange")
    Observable<ExchangeInfoRet> exchangeGood(@Body RequestBody requestBody);

    @POST("v1.welfare/getorderinfo")
    Observable<ExchangeInfoRet> exchangeList(@Body RequestBody requestBody);
}
