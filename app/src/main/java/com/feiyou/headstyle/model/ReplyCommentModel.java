package com.feiyou.headstyle.model;

import com.feiyou.headstyle.base.IBaseRequestCallBack;

/**
 * Created by iflying on 2018/1/9.
 */

public interface ReplyCommentModel<T> {
    void addReplyInfo(int type, String content,String repeatUserId,String messageId, IBaseRequestCallBack<T> iBaseRequestCallBack);
}
