package com.feiyou.headstyle.api;

import com.feiyou.headstyle.bean.CollectInfoRet;
import com.feiyou.headstyle.bean.StickerInfoRet;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by iflying on 2018/2/6.
 */

public interface StickerServiceApi {

    @POST("v1.show/tiezhiList")
    Observable<StickerInfoRet> getDataList(@Body RequestBody requestBody);
}
