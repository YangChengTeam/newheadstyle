package com.feiyou.headstyle.model;

import com.feiyou.headstyle.base.IBaseRequestCallBack;
import com.feiyou.headstyle.bean.TestResultParams;

import java.util.List;

/**
 * Created by iflying on 2018/1/9.
 */

public interface TestResultInfoModel<T> {
    void createImage(TestResultParams testResultParams, IBaseRequestCallBack<T> iBaseRequestCallBack);
}
