package com.feiyou.headstyle.api;

import com.feiyou.headstyle.bean.AddCollectionRet;
import com.feiyou.headstyle.bean.CollectInfoRet;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by iflying on 2018/2/6.
 */

public interface CollectionDataServiceApi {

    @POST("v1.images/userImageCollect")
    Observable<AddCollectionRet> addCollection(@Body RequestBody requestBody);
}
