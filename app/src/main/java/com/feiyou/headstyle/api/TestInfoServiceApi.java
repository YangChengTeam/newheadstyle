package com.feiyou.headstyle.api;

import com.feiyou.headstyle.bean.CollectInfoRet;
import com.feiyou.headstyle.bean.TestDetailInfoRet;
import com.feiyou.headstyle.bean.TestInfoRet;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by iflying on 2018/2/6.
 */

public interface TestInfoServiceApi {

    @POST("testClassInfoList")
    Observable<TestInfoRet> getDataList(@Body RequestBody requestBody);

    @POST("banner_view_list")
    Observable<TestInfoRet> getDataListByCid(@Body RequestBody requestBody);

    @POST("testTypeInfoView")
    Observable<TestDetailInfoRet> getTestDetail(@Body RequestBody requestBody);

}
