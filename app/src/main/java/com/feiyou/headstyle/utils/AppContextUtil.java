package com.feiyou.headstyle.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Build;

public class AppContextUtil {
    private static Context sContext;

    private AppContextUtil() {
    }

    public static void init(Context context) {
        sContext = context;
    }

    public static Context getInstance() {
        if (sContext == null) {
            throw new NullPointerException("the context is null,please init AppContextUtil in Application first.");
        }
        return sContext;
    }

    @SuppressLint("NewApi")
    public static boolean isValidContext(Context ctx) {
        Activity activity = (Activity) ctx;

        if (Build.VERSION.SDK_INT > 17) {
            if (activity == null || activity.isDestroyed() || activity.isFinishing()) {
                return false;
            } else {
                return true;
            }
        } else {
            if (activity == null || activity.isFinishing()) {
                return false;
            } else {
                return true;
            }
        }
    }

}
