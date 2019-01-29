package com.feiyou.headstyle.api;

import com.feiyou.headstyle.bean.MoreTypeInfoRet;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by iflying on 2018/2/6.
 */

public interface MoreTypeDataServiceApi {

    @POST("v1.show/moreImagesTagsList")
    Observable<MoreTypeInfoRet> getMoreTypeList(@Body RequestBody requestBody);
}
