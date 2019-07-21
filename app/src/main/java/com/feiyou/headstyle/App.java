package com.feiyou.headstyle;

import android.app.Application;
import android.content.Context;

import com.blankj.utilcode.util.Utils;
import com.bumptech.glide.Glide;
import com.bytedance.sdk.openadsdk.TTAdConfig;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdSdk;
import com.feiyou.headstyle.bean.AdInfo;
import com.feiyou.headstyle.bean.NoteInfo;
import com.feiyou.headstyle.bean.TestDetailInfoWrapper;
import com.feiyou.headstyle.bean.TestInfo;
import com.feiyou.headstyle.bean.TopicInfo;
import com.feiyou.headstyle.bean.UserInfo;
import com.feiyou.headstyle.bean.VideoInfo;
import com.feiyou.headstyle.utils.AppContextUtil;
import com.feiyou.headstyle.utils.TTAdManagerHolder;
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

    public static List<TestInfo> testInfoList;

    public UserInfo mUserInfo;

    public boolean isLogin;

    public static boolean isLoginAuth = false;

    public TestDetailInfoWrapper testInfo;

    public static boolean isShowGuan;

    public static boolean isShowFen;

    public static boolean isRemindComment;

    public static boolean isRemindAt;

    public static boolean isRemindNotice;

    public static boolean isShowTotalCount;

    public AdInfo suspendInfo;//悬浮广告

    public AdInfo messageAdInfo;//悬浮广告

    public boolean showFloatAd = true;

    public boolean showAlertAd = false;

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

        TTAdManagerHolder.init(this);//初始化今日头条
    }

    public UserInfo getmUserInfo() {
        return mUserInfo;
    }

    public void setmUserInfo(UserInfo mUserInfo) {
        this.mUserInfo = mUserInfo;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

    public TestDetailInfoWrapper getTestInfo() {
        return testInfo;
    }

    public void setTestInfo(TestDetailInfoWrapper testInfo) {
        this.testInfo = testInfo;
    }

    public AdInfo getSuspendInfo() {
        return suspendInfo;
    }

    public void setSuspendInfo(AdInfo suspendInfo) {
        this.suspendInfo = suspendInfo;
    }

    public AdInfo getMessageAdInfo() {
        return messageAdInfo;
    }

    public void setMessageAdInfo(AdInfo messageAdInfo) {
        this.messageAdInfo = messageAdInfo;
    }

    public boolean isShowFloatAd() {
        return showFloatAd;
    }

    public void setShowFloatAd(boolean showFloatAd) {
        this.showFloatAd = showFloatAd;
    }
}
