package com.feiyou.headstyle.model;

import com.feiyou.headstyle.base.IBaseRequestCallBack;

/**
 * Created by iflying on 2018/1/9.
 */

public interface RecordInfoModel<T> {

    void headSetInfo(String uid, String headId, IBaseRequestCallBack<T> iBaseRequestCallBack);

    void adClickInfo(String uid, String aid, IBaseRequestCallBack<T> iBaseRequestCallBack);
}
