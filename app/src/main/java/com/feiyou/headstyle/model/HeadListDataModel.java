package com.feiyou.headstyle.model;

import com.feiyou.headstyle.base.IBaseRequestCallBack;

/**
 * Created by iflying on 2018/1/9.
 */

public interface HeadListDataModel<T> {
    void getDataByTagId(String tagId, int page, int pageSize, IBaseRequestCallBack<T> iBaseRequestCallBack);

    void getSearchList(int page, String keyword, String userId, IBaseRequestCallBack<T> iBaseRequestCallBack);
}
