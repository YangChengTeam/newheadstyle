package com.feiyou.headstyle.model;

import com.feiyou.headstyle.base.IBaseRequestCallBack;

/**
 * Created by iflying on 2018/1/9.
 */

public interface NoteTypeModel<T> {
    void getNoteTypeData(String topicId, int page, int type, String userId, IBaseRequestCallBack<T> iBaseRequestCallBack);
}
