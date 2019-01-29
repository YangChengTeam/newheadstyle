package com.feiyou.headstyle.api;

import com.feiyou.headstyle.bean.SearchHotWordRet;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by iflying on 2018/2/6.
 */

public interface HotWordServiceApi {

    @POST("v1.images/searchTagList")
    Observable<SearchHotWordRet> getTagData(@Body RequestBody requestBody);
}
