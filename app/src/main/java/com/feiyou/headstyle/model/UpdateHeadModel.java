package com.feiyou.headstyle.model;

import com.feiyou.headstyle.base.IBaseRequestCallBack;

/**
 * Created by iflying on 2018/1/9.
 */

public interface UpdateHeadModel<T> {
    void updateHead(String userId, String filePath, IBaseRequestCallBack<T> iBaseRequestCallBack);
}
