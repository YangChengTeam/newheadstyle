package com.feiyou.headstyle.model;

import com.feiyou.headstyle.base.IBaseRequestCallBack;

/**
 * Created by iflying on 2018/1/9.
 */

public interface DeleteNoteModel<T> {
    void deleteNote(String userId, String msgId, IBaseRequestCallBack<T> iBaseRequestCallBack);
}
