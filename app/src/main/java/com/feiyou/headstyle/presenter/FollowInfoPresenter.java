package com.feiyou.headstyle.presenter;

import java.util.List;

/**
 * Created by iflying on 2018/1/9.
 */

public interface FollowInfoPresenter {
    void addFollow(String owenUserId, String userId);

    void followTopic(String userId, String topicId);
}
