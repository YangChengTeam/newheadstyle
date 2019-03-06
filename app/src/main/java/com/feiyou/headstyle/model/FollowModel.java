package com.feiyou.headstyle.model;

import com.feiyou.headstyle.base.IBaseRequestCallBack;

import java.util.List;

/**
 * Created by iflying on 2018/1/9.
 */

public interface FollowModel<T> {
    void addFollow(String owenUserId, String userId,IBaseRequestCallBack<T> iBaseRequestCallBack);

    void followTopic(String userId, String topicId,IBaseRequestCallBack<T> iBaseRequestCallBack);
}
