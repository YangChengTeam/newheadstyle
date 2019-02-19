package com.feiyou.headstyle.api;

import com.feiyou.headstyle.bean.NoteCommentRet;
import com.feiyou.headstyle.bean.NoteInfoDetailRet;
import com.feiyou.headstyle.bean.NoteInfoRet;
import com.feiyou.headstyle.bean.NoteSubCommentRet;
import com.feiyou.headstyle.bean.NoteTypeRet;
import com.feiyou.headstyle.bean.ReplyResultInfoRet;
import com.feiyou.headstyle.bean.ResultInfo;
import com.feiyou.headstyle.bean.ZanResultRet;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
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

    //评论的回复列表
    @POST("v1.message/messageCommentDetail")
    Observable<NoteCommentRet> getNoteCommentData(@Body RequestBody requestBody);

    @POST("v1.message/setMessageInfo")
    Observable<ResultInfo> addNote(@Body MultipartBody multipartBody);

    @POST("v1.message/messageDetailCommentRepeat")
    Observable<ReplyResultInfoRet> replyComment(@Body RequestBody requestBody);

    //回复下面的子回复列表
    @POST("v1.message/messageCommentRepeatDetail")
    Observable<NoteSubCommentRet> getNoteSubCommentData(@Body RequestBody requestBody);

    //帖子点赞
    @POST("v1.message/messageDetailCommentZan")
    Observable<ZanResultRet> addZan(@Body RequestBody requestBody);

}
