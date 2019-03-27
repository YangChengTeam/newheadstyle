package com.feiyou.headstyle.presenter;

import java.util.List;

/**
 * Created by iflying on 2018/1/9.
 */

public interface FeedBackPresenter {
    void addFeedBack(String uid, String content, String phone,List<String> filePaths);
}
