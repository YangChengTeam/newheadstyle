package com.feiyou.headstyle.model;

import com.feiyou.headstyle.base.IBaseRequestCallBack;

import java.util.List;

/**
 * Created by iflying on 2018/1/9.
 */

public interface TestInfoModel<T> {

    void getDataList(IBaseRequestCallBack<T> iBaseRequestCallBack);

    void getDataListByCid(String cid,IBaseRequestCallBack<T> iBaseRequestCallBack);

}
