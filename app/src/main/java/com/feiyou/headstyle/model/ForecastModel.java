package com.feiyou.headstyle.model;

import com.feiyou.headstyle.base.IBaseRequestCallBack;

import java.util.List;

/**
 * Created by iflying on 2018/1/9.
 */

public interface ForecastModel<T> {
    void getForecastData(String star, String day, IBaseRequestCallBack<T> iBaseRequestCallBack);
}
