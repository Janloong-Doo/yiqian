package com.du.easysignin.utils;

/**
 * 计时任务工具类
 * 作者：DragonDu
 * Email: 807110586@qq.com
 */
public abstract class SchedulesUtils {
    private int mCount = 0;
    private int mTimeOutCount = 1;
    private int mSleepTime = 1000; // 休眠时间为 1s
    private boolean mExitFlag = false;
    private Thread mThread = null;

    /**
     * Do not process UI work in this.
     */
    public abstract void doTimerCheckWork();

    public abstract void doTimeOutWork();

    public SchedulesUtils() {
        mThread = new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                while (!mExitFlag) {
                    mCount++;
                    if (mCount < mTimeOutCount) {
                        doTimerCheckWork();
                        try {
                            Thread.sleep(mSleepTime);
                        }
                        catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                            exit();
                        }
                    }
                    else {
                        doTimeOutWork();
                    }
                }
            }
        });
    }

    /**
     * start
     * 
     * @param times
     *            How many times will check?
     * @param sleepTime
     *            ms, Every check sleep time.
     */
    public void start(int timeOutCount, int sleepTime) {
        mTimeOutCount = timeOutCount;
        mSleepTime = sleepTime;

        mThread.start();
    }

    public void exit() {
        mExitFlag = true;
    }

}
