package com.feiyou.headstyle.model;

import com.feiyou.headstyle.base.IBaseRequestCallBack;

/**
 * Created by iflying on 2018/1/9.
 */

public interface ReportInfoModel<T> {
    void takeReport(String userId, String rid, int type, String content, String intro, IBaseRequestCallBack<T> iBaseRequestCallBack);
}
