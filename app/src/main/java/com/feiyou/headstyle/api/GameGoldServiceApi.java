package com.feiyou.headstyle.api;

import com.feiyou.headstyle.bean.BindAccountInfoRet;
import com.feiyou.headstyle.bean.GameGoldInfoRet;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by iflying on 2018/2/6.
 */

public interface GameGoldServiceApi {

    @POST("v1.welfare/getgamegold")
    Observable<GameGoldInfoRet> gameGold(@Body RequestBody requestBody);
}
