package com.feiyou.headstyle.presenter;

/**
 * Created by iflying on 2018/1/9.
 */

public interface GoldDetailPresenter {
    void goldDetailList(String uid, String openid,int page, int pageSize);
    void goldCashDetailList(String uid, String openid,int page, int pageSize,int iscash);
}
