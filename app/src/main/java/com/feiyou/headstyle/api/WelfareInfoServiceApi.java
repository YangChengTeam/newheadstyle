package com.feiyou.headstyle.api;

import com.feiyou.headstyle.bean.WelfareInfo;
import com.feiyou.headstyle.bean.WelfareInfoRet;
import com.feiyou.headstyle.bean.WordInfoRet;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by iflying on 2018/2/6.
 */

public interface WelfareInfoServiceApi {

    @POST("v2.welfare/index")
    Observable<WelfareInfoRet> getWelfareData(@Body RequestBody requestBody);
}
