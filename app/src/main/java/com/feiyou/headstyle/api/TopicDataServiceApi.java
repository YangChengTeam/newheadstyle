package com.feiyou.headstyle.api;

import com.feiyou.headstyle.bean.CollectInfoRet;
import com.feiyou.headstyle.bean.FollowInfoRet;
import com.feiyou.headstyle.bean.TopicInfoRet;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by iflying on 2018/2/6.
 */

public interface TopicDataServiceApi {

    @POST("v2.show/topicList")
    Observable<TopicInfoRet> getTopicDataList(@Body RequestBody requestBody);

    //关注话题
    @POST("v2.message/userTopicGuan")
    Observable<FollowInfoRet> topicFollow(@Body RequestBody requestBody);
}
