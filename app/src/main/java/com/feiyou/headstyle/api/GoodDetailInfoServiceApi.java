package com.feiyou.headstyle.api;

import com.feiyou.headstyle.bean.GoodDetailInfoRet;
import com.feiyou.headstyle.bean.GoodInfoRet;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by iflying on 2018/2/6.
 */

public interface GoodDetailInfoServiceApi {

    @POST("v1.welfare/goodsinfo")
    Observable<GoodDetailInfoRet> getGoodDetail(@Body RequestBody requestBody);
}
