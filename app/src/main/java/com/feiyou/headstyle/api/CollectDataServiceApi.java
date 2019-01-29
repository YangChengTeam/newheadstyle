package com.feiyou.headstyle.api;

import com.feiyou.headstyle.bean.CollectInfoRet;
import com.feiyou.headstyle.bean.HomeDataRet;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by iflying on 2018/2/6.
 */

public interface CollectDataServiceApi {

    @POST("v1.show/banner_view_list")
    Observable<CollectInfoRet> getDataById(@Body RequestBody requestBody);
}
