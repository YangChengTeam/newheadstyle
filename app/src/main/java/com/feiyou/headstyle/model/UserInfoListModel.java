package com.feiyou.headstyle.model;

import com.feiyou.headstyle.base.IBaseRequestCallBack;

import java.util.List;

/**
 * Created by iflying on 2018/1/9.
 */

public interface UserInfoListModel<T> {
    void addFriendsList(int page, IBaseRequestCallBack<T> iBaseRequestCallBack);

    void searchFriendsList(int page, String keyWord, IBaseRequestCallBack<T> iBaseRequestCallBack);

    void getMyGuanFenList(int page, String userId, String intoUserId,int type, IBaseRequestCallBack<T> iBaseRequestCallBack);
}
