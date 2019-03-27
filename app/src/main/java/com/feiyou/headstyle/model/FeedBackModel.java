package com.feiyou.headstyle.model;

import com.feiyou.headstyle.base.IBaseRequestCallBack;

import java.util.List;

/**
 * Created by iflying on 2018/1/9.
 */

public interface FeedBackModel<T> {
    void addFeedBack(String uid, String content, String phone, List<String> filePaths, IBaseRequestCallBack<T> iBaseRequestCallBack);
}
