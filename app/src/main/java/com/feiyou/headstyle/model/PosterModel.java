package com.feiyou.headstyle.model;

import com.feiyou.headstyle.base.IBaseRequestCallBack;

/**
 * Created by iflying on 2018/1/9.
 */

public interface PosterModel<T> {
    void createPoster(String nickname, String headimg,String uniqid, IBaseRequestCallBack<T> iBaseRequestCallBack);
}
