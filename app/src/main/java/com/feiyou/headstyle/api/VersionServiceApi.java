package com.feiyou.headstyle.api;

import com.feiyou.headstyle.bean.CollectInfoRet;
import com.feiyou.headstyle.bean.VersionInfoRet;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by iflying on 2018/2/6.
 */

public interface VersionServiceApi {

    @POST("v2.show/versionInfo")
    Observable<VersionInfoRet> getVersionInfo(@Body RequestBody requestBody);
}
