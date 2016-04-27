package com.daxingframe;
import android.app.Application;

import com.daxingframe.logic.me.model.UserInfo;

/**
 * App application
 * @author jack
 */
public class AppDroid extends Application {

    private static AppDroid sInstance;
    private UserInfo userInfo;//用户信息

    @Override
    public void onCreate() {
        super.onCreate();
        //全局Application
        sInstance = this;
    }

    public static AppDroid getInstance() {
        return sInstance;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
}