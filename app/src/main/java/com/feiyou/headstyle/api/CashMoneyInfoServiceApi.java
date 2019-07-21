package com.feiyou.headstyle.api;

import com.feiyou.headstyle.bean.CashMoneyInfoRet;
import com.feiyou.headstyle.bean.ExchangeInfoRet;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by iflying on 2018/2/6.
 */

public interface CashMoneyInfoServiceApi {

    @POST("v1.welfare/cashset")
    Observable<CashMoneyInfoRet> cashMoneyList(@Body RequestBody requestBody);

}
