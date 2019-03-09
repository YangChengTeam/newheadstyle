package com.feiyou.headstyle.api;

import com.feiyou.headstyle.bean.ForecastInfoRet;
import com.feiyou.headstyle.bean.StarPosterRet;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by iflying on 2018/2/6.
 */

public interface ForecastDataServiceApi {

    @POST("starapp/index")
    Observable<ForecastInfoRet> getForecastData(@Body RequestBody requestBody);

    @POST("starapp/imageTestAppSet")
    Observable<StarPosterRet> createPoster(@Body RequestBody requestBody);
}
