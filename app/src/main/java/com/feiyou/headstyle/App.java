package com.feiyou.headstyle;

import android.app.Application;
import android.content.Context;

import com.blankj.utilcode.util.Utils;
import com.feiyou.headstyle.bean.TopicInfo;
import com.feiyou.headstyle.bean.UserInfo;
import com.feiyou.headstyle.utils.AppContextUtil;
import com.mob.MobSDK;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;

import java.util.List;

/**
 * Created by admin on 2017/3/24.
 */

public class App extends Application {

    protected static App mInstance;

    public static Context applicationContext;

    public static Context getContext() {
        return applicationContext;
    }

    public static List<TopicInfo> topicInfoList;

    public UserInfo mUserInfo;

    public App() {
        mInstance = this;
    }

    public static App getApp() {
        if (mInstance != null && mInstance instanceof App) {
            return (App) mInstance;
        } else {
            mInstance = new App();
            mInstance.onCreate();
            return (App) mInstance;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, "");//58edcfeb310c93091c000be2 5965ee00734be40b580001a0

        PlatformConfig.setQQZone("1105592461", "xCJux2hAAjyh1qdx");
        PlatformConfig.setWeixin("wxd1112ca9a216aeda", "0e18de42fc068c41f0aca921403b9932");

        MobSDK.init(this);
        Utils.init(this);
        AppContextUtil.init(this);
        applicationContext = this;
    }

    public UserInfo getmUserInfo() {
        return mUserInfo;
    }

    public void setmUserInfo(UserInfo mUserInfo) {
        this.mUserInfo = mUserInfo;
    }
}
