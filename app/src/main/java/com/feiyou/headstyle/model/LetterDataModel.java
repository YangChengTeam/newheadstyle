package com.feiyou.headstyle.model;

import com.feiyou.headstyle.base.IBaseRequestCallBack;

/**
 * Created by iflying on 2018/1/9.
 */

public interface LetterDataModel<T> {
    void getLetterDataByPage(int page, IBaseRequestCallBack<T> iBaseRequestCallBack);
}
