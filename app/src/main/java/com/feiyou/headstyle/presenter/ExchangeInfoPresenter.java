package com.feiyou.headstyle.presenter;

/**
 * Created by iflying on 2018/1/9.
 */

public interface ExchangeInfoPresenter {
    void exchangeGood(String gid, String uid, int type);

    void exchangeList(String uid,String openid, int page, int pageSize, String eid);
}
