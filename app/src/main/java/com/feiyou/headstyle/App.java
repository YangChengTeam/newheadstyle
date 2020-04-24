package com.feiyou.headstyle;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.Utils;

import com.cmcm.cmgame.CmGameSdk;
import com.cmcm.cmgame.activity.H5GameActivity;
import com.cmcm.cmgame.gamedata.CmGameAppInfo;
import com.feiyou.headstyle.bean.AdInfo;
import com.feiyou.headstyle.bean.TestDetailInfoWrapper;
import com.feiyou.headstyle.bean.TestInfo;
import com.feiyou.headstyle.bean.TopicInfo;
import com.feiyou.headstyle.bean.UserInfo;
import com.feiyou.headstyle.common.Constants;
import com.feiyou.headstyle.ui.custom.CmGameImageLoader;
import com.feiyou.headstyle.utils.AppContextUtil;
import com.feiyou.headstyle.utils.AppUtils;
import com.feiyou.headstyle.utils.TTAdManagerHolder;
import com.mob.MobSDK;
import com.orhanobut.logger.Logger;
import com.umeng.commonsdk.BuildConfig;
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

    public String randomNoteId;

    public int userGoldNum;

    private int isFromTaskSign;

    public static String appChannel = "1";

    public static String imei;

    public static String androidId;

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

        CmGameAppInfo cmGameAppInfo = new CmGameAppInfo();
        cmGameAppInfo.setAppId("gexingtouxiang");                             // GameSdkID，向我方申请
        cmGameAppInfo.setAppHost("https://gxtx-xyx-sdk-svc.beike.cn");   // 游戏host地址，向我方申请

        // 游戏退出时，增加游戏推荐弹窗，提高游戏的点击个数，注释此行可去掉此功能
        cmGameAppInfo.setQuitGameConfirmFlag(true);

        // 可设置进入游戏默认静音，默认有声，设置true为游戏静音
        // cmGameAppInfo.setMute(true);

        CmGameAppInfo.TTInfo ttInfo = new CmGameAppInfo.TTInfo();
        ttInfo.setRewardVideoId("920819795");   // 激励视频
        ttInfo.setFullVideoId("920819314");     // 全屏视频
        ttInfo.setExpressBannerId("920819459"); // Banner广告，模板渲染，尺寸：600*150
        ttInfo.setExpressInteractionId("920819698"); // 插屏广告，模板渲染，尺寸比例 2：3

        cmGameAppInfo.setTtInfo(ttInfo);

        CmGameSdk.initCmGameSdk(this, cmGameAppInfo, new CmGameImageLoader(), BuildConfig.DEBUG);
        Log.d("cmgamesdk", "current sdk version : " + CmGameSdk.getVersion());
        appChannel = AppUtils.getMetaDataValue(this, "UMENG_CHANNEL");

        loadUserInfo();
    }

    public void loadUserInfo(){
        if (!StringUtils.isEmpty(SPUtils.getInstance().getString(Constants.USER_INFO))) {
            Logger.i(SPUtils.getInstance().getString(Constants.USER_INFO));
                mUserInfo = JSON.parseObject(SPUtils.getInstance().getString(Constants.USER_INFO), new TypeReference<UserInfo>() {
            });
            App.getApp().setmUserInfo(mUserInfo);
            App.getApp().setLogin(true);
        }
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

    public String getRandomNoteId() {
        return randomNoteId;
    }

    public void setRandomNoteId(String randomNoteId) {
        this.randomNoteId = randomNoteId;
    }

    public int getUserGoldNum() {
        return userGoldNum;
    }

    public void setUserGoldNum(int userGoldNum) {
        this.userGoldNum = userGoldNum;
    }

    public int getIsFromTaskSign() {
        return isFromTaskSign;
    }

    public void setIsFromTaskSign(int isFromTaskSign) {
        this.isFromTaskSign = isFromTaskSign;
    }


}
