package com.feiyou.headstyle.base;

import com.feiyou.headstyle.common.RetrofitTestManager;

import retrofit2.Retrofit;

/**
 * 描述：业务对象的基类
 */
public class BaseTestModel {

    //retrofit请求数据的管理类
    public Retrofit mRetrofit;

    public BaseTestModel() {
        mRetrofit = RetrofitTestManager.retrofit();
    }
}
