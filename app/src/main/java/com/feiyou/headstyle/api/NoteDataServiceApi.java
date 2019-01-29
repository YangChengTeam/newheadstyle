package com.feiyou.headstyle.api;

import com.feiyou.headstyle.bean.NoteCommentRet;
import com.feiyou.headstyle.bean.NoteInfoDetailRet;
import com.feiyou.headstyle.bean.NoteInfoRet;
import com.feiyou.headstyle.bean.NoteTypeRet;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by iflying on 2018/2/6.
 */

public interface NoteDataServiceApi {

    @POST("v1.message/messageList")
    Observable<NoteInfoRet> getNoteData(@Body RequestBody requestBody);

    @POST("v1.message/messageTypeList")
    Observable<NoteTypeRet> getNoteTypeData(@Body RequestBody requestBody);

    @POST("v1.message/messageInfoDetail")
    Observable<NoteInfoDetailRet> getNoteInfoDetailData(@Body RequestBody requestBody);

    @POST("v1.message/messageCommentDetail")
    Observable<NoteCommentRet> getNoteCommentData(@Body RequestBody requestBody);
}
