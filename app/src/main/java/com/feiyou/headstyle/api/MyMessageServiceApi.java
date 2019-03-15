package com.feiyou.headstyle.api;

import com.feiyou.headstyle.bean.FollowInfoRet;
import com.feiyou.headstyle.bean.MyAtMessageRet;
import com.feiyou.headstyle.bean.MyCommentRet;
import com.feiyou.headstyle.bean.NoteCommentRet;
import com.feiyou.headstyle.bean.NoteInfoDetailRet;
import com.feiyou.headstyle.bean.NoteInfoRet;
import com.feiyou.headstyle.bean.NoteSubCommentRet;
import com.feiyou.headstyle.bean.NoteTypeRet;
import com.feiyou.headstyle.bean.ReplyResultInfoRet;
import com.feiyou.headstyle.bean.ResultInfo;
import com.feiyou.headstyle.bean.SystemInfoRet;
import com.feiyou.headstyle.bean.ZanResultRet;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by iflying on 2018/2/6.
 */

public interface MyMessageServiceApi {

    @POST("v1.userinfo/myNoticeList")
    Observable<MyCommentRet> getMyCommentList(@Body RequestBody requestBody);

    @POST("v1.userinfo/myNoticeList")
    Observable<MyAtMessageRet> getMyAtMessageList(@Body RequestBody requestBody);

    @POST("v1.userinfo/myNoticeList")
    Observable<SystemInfoRet> getSystemInfoList(@Body RequestBody requestBody);
}
