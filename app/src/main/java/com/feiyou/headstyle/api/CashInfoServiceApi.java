package com.feiyou.headstyle.api;

import com.feiyou.headstyle.bean.CashInfoRet;
import com.feiyou.headstyle.bean.CashMoneyInfoRet;
import com.feiyou.headstyle.bean.CashRecordRet;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by iflying on 2018/2/6.
 */

public interface CashInfoServiceApi {

    @POST("v2.pay/appwxpay")
    Observable<CashInfoRet> startCash(@Body RequestBody requestBody);

    @POST("v2.welfare/txinfo")
    Observable<CashRecordRet> cashList(@Body RequestBody requestBody);

}
