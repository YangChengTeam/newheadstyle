package com.feiyou.headstyle.model;

import com.feiyou.headstyle.base.IBaseRequestCallBack;

/**
 * Created by iflying on 2018/1/9.
 */

public interface HomeDataModel<T> {
    void getData(String page,String pageSize,String change, IBaseRequestCallBack<T> iBaseRequestCallBack);
}
