package com.feiyou.headstyle.api;

import com.feiyou.headstyle.bean.GoodInfoRet;
import com.feiyou.headstyle.bean.WelfareInfoRet;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by iflying on 2018/2/6.
 */

public interface GoodInfoServiceApi {

    @POST("v1.welfare/goods_list")
    Observable<GoodInfoRet> getGoodListData(@Body RequestBody requestBody);
}
