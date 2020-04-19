package com.feiyou.headstyle.presenter;

/**
 * Created by iflying on 2018/1/9.
 */

public interface TaskRecordInfoPresenter {
    void addTaskRecord(String uid, String openid,String taskId, int goldNum, double cash, int status, String recordId);

    void addHomeTaskRecord(String uid, String openid,String imei, double cash, int type);
}
