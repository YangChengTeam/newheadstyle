package com.feiyou.headstyle.presenter;

import com.feiyou.headstyle.bean.LoginRequest;

/**
 * Created by iflying on 2018/1/9.
 */

public interface UserInfoListPresenter {
    void addFriendsList(int page);
    void searchFriendsList(int page,String keyWord);
}
