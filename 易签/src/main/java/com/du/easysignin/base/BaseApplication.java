package com.du.easysignin.base;

import android.app.Application;
import android.graphics.Bitmap;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.logging.Logger;

/**
 * BaseApplication
 * 作者：DragonDu
 * Email: 807110586@qq.com
 */
public class BaseApplication extends Application {
    private static BaseApplication instance;

    /**
     * <p/>
     * 获取BaseApplication实例
     * <p/>
     * 单例模式，返回唯一实例
     *
     * @return instance
     */
    public static BaseApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (instance == null) {
            instance = this;
        }

//        sendFileStates = new HashMap<String, FileState>();
//        recieveFileStates = new HashMap<String, FileState>();
//        mAvatarCache = new HashMap<String, SoftReference<Bitmap>>();
//
//        ActivitiesManager.init(); // 初始化活动管理器
//        logger = Logger.initLogger(instance, isPrintLog, logLevel); // 初始化日志
//
//        initEmoticons();
//        initNotification();
//        initFolder();
    }
    }
