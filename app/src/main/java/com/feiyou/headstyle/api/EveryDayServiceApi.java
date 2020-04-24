package com.feiyou.headstyle.api;

import com.feiyou.headstyle.bean.CashInfoRet;
import com.feiyou.headstyle.bean.CashRecordRet;
import com.feiyou.headstyle.bean.EveryDayHbRet;
import com.feiyou.headstyle.bean.HongBaoInfoRet;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by iflying on 2018/2/6.
 */

public interface EveryDayServiceApi {

    @POST("v2.welfare/dlhb")
    Observable<EveryDayHbRet> everyDayHongBaoInfo(@Body RequestBody requestBody);

    @POST("v2.userinfo/init")
    Observable<HongBaoInfoRet> getHBInfo(@Body RequestBody requestBody);
}
