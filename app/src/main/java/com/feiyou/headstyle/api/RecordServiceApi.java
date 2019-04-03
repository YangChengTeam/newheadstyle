package com.feiyou.headstyle.api;

import com.feiyou.headstyle.bean.CollectInfoRet;
import com.feiyou.headstyle.bean.RecordInfoRet;
import com.feiyou.headstyle.bean.ResultInfo;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by iflying on 2018/2/6.
 */

public interface RecordServiceApi {

    @POST("v1.images/userImageSet")
    Observable<RecordInfoRet> headSetInfo(@Body RequestBody requestBody);

    @POST("v1.images/adClickLog")
    Observable<RecordInfoRet> adClickInfo(@Body RequestBody requestBody);
}
