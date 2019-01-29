package com.feiyou.headstyle.model;

import com.feiyou.headstyle.base.IBaseRequestCallBack;

/**
 * Created by iflying on 2018/1/9.
 */

public interface NoteDataModel<T> {
    void getNoteData(int page, int type,String userid, IBaseRequestCallBack<T> iBaseRequestCallBack);
}
