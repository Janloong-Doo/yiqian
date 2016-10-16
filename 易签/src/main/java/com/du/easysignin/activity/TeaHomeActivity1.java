package com.du.easysignin.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.du.easysignin.R;
import com.du.easysignin.adapter.FragmentViewPagerAdapter;
import com.du.easysignin.base.BaseAcitivity;
import com.du.easysignin.fragment.TeaFragment1;
import com.du.easysignin.fragment.TeaFragment2;
import com.du.easysignin.fragment.TeaFragment3;
import com.du.easysignin.fragment.TeaFragment4;
import com.du.easysignin.service.LocalService;
import com.du.easysignin.service.LocalService.LocalBinder;
import com.du.easysignin.utils.SchedulesUtils;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 教师主页面
 * 作者：DragonDu
 * Email: 807110586@qq.com
 */
public class TeaHomeActivity1 extends BaseAcitivity implements View.OnClickListener {

    private TextView tv_title;
    private ViewPager vp_content;
    private RadioGroup rg_group;
    private ArrayList<Fragment> pagers;
    private int[] bt;
    private ServiceConnection sc;
    private LocalBinder binder;

    private TeaFragment1 teaFragment1;
    private TeaFragment2 teaFragment2;
    private TeaFragment3 teaFragment3;
    private TeaFragment4 teaFragment4;
    private String[] titles = {"签到", "签到查询", "信息管理", "设置"};
    private mServiceConn conn = null;
    private Intent intent;


    public static void actionStart(Activity activity) {
        Intent intent = new Intent(activity, TeaHomeActivity1.class);
        activity.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tea_home);
        initService();
        initUI();
//        connection();

//        initnumber();
    }

//    /**
//     * 初始化更新数据的线程
//     */
//    private void initnumber() {
//        sd = new SchedulesUtils() {
//            @Override
//            public void doTimerCheckWork() {
//
//                TeaFragment1.getteafragmentInstance().setTv_online_number();
//
//            }
//
//            @Override
//            public void doTimeOutWork() {
//                this.exit();
//            }
//        };
//        sd.start(Integer.MAX_VALUE, 5000);
//    }


    @Override
    public void initUI() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        rg_group = (RadioGroup) findViewById(R.id.rg_group);
        vp_content = (ViewPager) findViewById(R.id.vp_content);
    }

    //绑定服务
    public void initService() {
        connection();

    }

    @Override
    public void initEvent() {
        bt = new int[]{R.id.rad_bt_1, R.id.rad_bt_2, R.id.rad_bt_3, R.id.rad_bt_4};
        rg_group.check(bt[0]);
        pagers = new ArrayList<>();
        teaFragment1 = new TeaFragment1();
        teaFragment1.setHandler(handler);
        teaFragment2 = new TeaFragment2();
        teaFragment3 = new TeaFragment3();
        teaFragment4 = new TeaFragment4();
//        teaFragment2.setService(myservice);
        pagers.add(teaFragment1);
        pagers.add(teaFragment2);
        pagers.add(teaFragment3);
        pagers.add(teaFragment4);

//        if (binder == null) {
//
//            System.out.println("binder为空");// 此处都为空
//        } else {
//            System.out.println("binder不为空");
//        }
//        if (myservice == null) {
//
//            System.out.println("myservice为空");//此处为空
//        } else {
//            System.out.println("myservice不为空");
//        }
        vp_content.setAdapter(new FragmentViewPagerAdapter(getSupportFragmentManager(), pagers));
        vp_content.setOffscreenPageLimit(pagers.size());//主要是这句话.设置了vp缓存的页面数量.当缓存的页面大于等于页面数时,
        // 就不会发生销毁现象

        //viewpager 设置监听
        vp_content.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int
                    positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                pagers.get(position).initData();
                rg_group.check(bt[position]);
                tv_title.setText(titles[position]);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        rg_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    //设置当前页面
                    case R.id.rad_bt_1:
                        vp_content.setCurrentItem(0, false);


                        break;
                    case R.id.rad_bt_2:
                        vp_content.setCurrentItem(1, false);
                        break;
                    case R.id.rad_bt_3:
                        vp_content.setCurrentItem(2, false);
                        break;
                    case R.id.rad_bt_4:
                        vp_content.setCurrentItem(3, false);
                        break;

                }
            }

        });
    }

    /**
     * 按钮单击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {

    }


    private static Boolean isQuit = false;
    private Timer timer = new Timer();

    /**
     * 返回键退出
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isQuit == false) {
                isQuit = true;
                Toast.makeText(getBaseContext(), "再次点击确定退出软件", Toast.LENGTH_SHORT).show();
                TimerTask task = null;
                task = new TimerTask() {
                    @Override
                    public void run() {
                        isQuit = false;
                    }
                };
                timer.schedule(task, 2000);
            } else {
                finish();
            }
        } else {
        }
        return false;
    }

    public LocalService myservice;


    /**
     * 绑定服务
     */

    private void connection() {
        intent = new Intent(this, LocalService.class);
        if (conn == null) {
            conn = new mServiceConn();
        }
        bindService(intent, conn, this.BIND_AUTO_CREATE);
    }


    class mServiceConn implements ServiceConnection {

//        private LocalBinder binder;

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d("aaa", "绑定成功");
            binder = (LocalBinder) service;

            //通过IBinder获取Service
            myservice = binder.getService();
            myservice.startWaitDataThread(handler, true);
            initEvent();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            myservice.stopThread();
            Log.e("shifoustop", "asdads");
        }
    }

    @Override
    public void finish() {
//        sd.exit();
//        stopService(intent);
        TeaFragment1.getteafragmentInstance().setFinish();
        unbindService(conn);
        conn = null;
        super.finish();
    }
}
