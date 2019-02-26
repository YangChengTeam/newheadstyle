package com.feiyou.headstyle.model;

import com.feiyou.headstyle.base.IBaseRequestCallBack;

/**
 * Created by iflying on 2018/1/9.
 */

public interface NoteSubCommentDataModel<T> {
    void getNoteSubCommentData(int page, String userId, String commentId, int modelType, IBaseRequestCallBack<T> iBaseRequestCallBack);
}
