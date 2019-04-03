package com.feiyou.headstyle.model;

import com.feiyou.headstyle.base.IBaseRequestCallBack;

/**
 * Created by iflying on 2018/1/9.
 */

public interface VersionModel<T> {
    void getVersionInfo(String channel,IBaseRequestCallBack<T> iBaseRequestCallBack);
}
