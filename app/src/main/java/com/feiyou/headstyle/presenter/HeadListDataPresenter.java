package com.feiyou.headstyle.presenter;

/**
 * Created by iflying on 2018/1/9.
 */

public interface HeadListDataPresenter {
    void getDataByTagId(String tagId, int page, int pageSize);
    void getSearchList(int page, String keyword, String userId);
}
