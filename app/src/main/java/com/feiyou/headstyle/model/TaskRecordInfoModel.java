package com.feiyou.headstyle.model;

import com.feiyou.headstyle.base.IBaseRequestCallBack;

/**
 * Created by iflying on 2018/1/9.
 */

public interface TaskRecordInfoModel<T> {
    void addTaskRecord(String uid, String openid,String taskId, int goldNum, double cash, int status, String recordId, IBaseRequestCallBack<T> iBaseRequestCallBack);
    void addHomeTaskRecord(String uid, String openid,String imei, double cash, int type,IBaseRequestCallBack<T> iBaseRequestCallBack);
}
