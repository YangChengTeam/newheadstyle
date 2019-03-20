package com.feiyou.headstyle.model;

import com.feiyou.headstyle.base.IBaseRequestCallBack;

import java.util.List;

/**
 * Created by iflying on 2018/1/9.
 */

public interface AddCollectionModel<T> {
    void addCollection(String uid, String imgId,IBaseRequestCallBack<T> iBaseRequestCallBack);

    void addVideoCollection(String uid, String vid,IBaseRequestCallBack<T> iBaseRequestCallBack);
}
