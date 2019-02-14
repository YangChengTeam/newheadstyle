package com.feiyou.headstyle.model;

import com.feiyou.headstyle.base.IBaseRequestCallBack;
import com.feiyou.headstyle.bean.ReplyParams;

/**
 * Created by iflying on 2018/1/9.
 */

public interface ReplyCommentModel<T> {
    void addReplyInfo(ReplyParams replyParams, IBaseRequestCallBack<T> iBaseRequestCallBack);
}
