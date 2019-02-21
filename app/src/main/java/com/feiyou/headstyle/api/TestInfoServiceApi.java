package com.feiyou.headstyle.api;

import com.feiyou.headstyle.bean.CollectInfoRet;
import com.feiyou.headstyle.bean.TestDetailInfoRet;
import com.feiyou.headstyle.bean.TestInfoRet;
import com.feiyou.headstyle.bean.TestResultInfoRet;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by iflying on 2018/2/6.
 */

public interface TestInfoServiceApi {

    @POST("appIndex")
    Observable<TestInfoRet> getDataList(@Body RequestBody requestBody);

    @POST("testTypeInfoList")
    Observable<TestInfoRet> getDataListByCid(@Body RequestBody requestBody);

    @POST("testTypeInfoView")
    Observable<TestDetailInfoRet> getTestDetail(@Body RequestBody requestBody);

    @POST("testTextResult")
    Observable<TestResultInfoRet> createImage(@Body RequestBody requestBody);

    @POST("testClassInfoList")
    Observable<TestInfoRet> getHotAndRecommendList(@Body RequestBody requestBody);

}
