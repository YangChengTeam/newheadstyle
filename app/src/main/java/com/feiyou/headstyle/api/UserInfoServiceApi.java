package com.feiyou.headstyle.api;

import com.feiyou.headstyle.bean.CollectInfoRet;
import com.feiyou.headstyle.bean.PhotoWallRet;
import com.feiyou.headstyle.bean.ResultInfo;
import com.feiyou.headstyle.bean.UserInfoRet;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by iflying on 2018/2/6.
 */

public interface UserInfoServiceApi {

    @POST("v1.userinfo/login")
    Observable<UserInfoRet> login(@Body RequestBody requestBody);

    @POST("v1.userinfo/userImageWallAdd")
    Observable<PhotoWallRet> uploadPhotoWall(@Body MultipartBody multipartBody);

    @POST("v1.userinfo/userImageWallDel")
    Observable<PhotoWallRet> deletePhoto(@Body RequestBody requestBody);
}
