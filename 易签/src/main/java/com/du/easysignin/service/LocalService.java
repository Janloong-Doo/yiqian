package com.du.easysignin.service;


import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;

import com.du.easysignin.thread.ListenThread;

/**
 * LocalService
 * 作者：DragonDu
 * Email: 807110586@qq.com
 */
public class LocalService extends Service {

    private static final String TAG = "LocalService";
    private IBinder binder = new LocalService.LocalBinder();
    private ListenThread thread;

    @Override
    public IBinder onBind(Intent intent) {

        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        stopThread();
        super.onDestroy();
    }

    public void startWaitDataThread(Handler handler, boolean manytoone) {
        thread = new ListenThread(handler, 12345, manytoone);
        thread.start();


    }

    public void stopThread() {
        thread.setexit(false);
//        thread.stop();

    }

    //定义内容类继承Binder
    public class LocalBinder extends Binder {
        //返回本地服务
        public LocalService getService() {
            return LocalService.this;
        }

//        public void startWaitDataThread(Handler handler) {
//            new ListenThread(handler, 12345).start();
//        }
    }

}

