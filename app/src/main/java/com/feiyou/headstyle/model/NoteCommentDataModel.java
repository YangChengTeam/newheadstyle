package com.feiyou.headstyle.model;

import com.feiyou.headstyle.base.IBaseRequestCallBack;

/**
 * Created by iflying on 2018/1/9.
 */

public interface NoteCommentDataModel<T> {
    void getNoteCommentData(String userId,int page, String msgId, int type, IBaseRequestCallBack<T> iBaseRequestCallBack);
}
