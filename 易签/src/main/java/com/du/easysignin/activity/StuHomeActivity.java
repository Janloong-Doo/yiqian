package com.du.easysignin.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.du.easysignin.R;
import com.du.easysignin.adapter.FragmentViewPagerAdapter;
import com.du.easysignin.base.BaseAcitivity;
import com.du.easysignin.fragment.StuFragment1;
import com.du.easysignin.fragment.StuFragment2;
import com.du.easysignin.fragment.StuFragment3;
import com.du.easysignin.fragment.StuFragment4;
import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 学生主界面
 * 作者：DragonDu
 * Email: 807110586@qq.com
 */

public class StuHomeActivity extends BaseAcitivity implements View.OnClickListener{
    private TextView tv_title;
    private RadioGroup rg_group_stu;//radiogroup按钮组
    private ViewPager vp_stu_home;//页面viewpager
    private ArrayList<Fragment> pagers;
    private int[] bt;//radiobutton 集合id
    private String[] titles = {"签到", "悄悄话", "待开发", "设置"};
    private StuFragment1 stuFragment1;
    private StuFragment2 stuFragment2;
    private StuFragment3 stuFragment3;
    private StuFragment4 stuFragment4;

    public static void actionStart(Activity activity) {
        Intent intent = new Intent(activity, StuHomeActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stu_home);
        initUI();
        initEvent();
    }

    @Override
    public void initUI() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        rg_group_stu = (RadioGroup) findViewById(R.id.rg_group_stu);
        vp_stu_home = (ViewPager) findViewById(R.id.vp_stu_home);
    }

    @Override
    public void initEvent() {

        bt = new int[]{R.id.stu_home_rd1, R.id.stu_home_rd2, R.id.stu_home_rd3, R.id.stu_home_rd4};
        rg_group_stu.check(bt[0]);
        pagers = new ArrayList<>();
        stuFragment1 = new StuFragment1();
        stuFragment1.setMhandler(handler);
        stuFragment2 = new StuFragment2();
        stuFragment3 = new StuFragment3();
        stuFragment4 = new StuFragment4();
        pagers.add(stuFragment1);
        pagers.add(stuFragment2);
        pagers.add(stuFragment3);
        pagers.add(stuFragment4);

        vp_stu_home.setAdapter(new FragmentViewPagerAdapter(getSupportFragmentManager(), pagers));
        vp_stu_home.setOffscreenPageLimit(pagers.size());//主要是这句话.设置了vp缓存的页面数量.当缓存的页面大于等于页面数时,就不会发生销毁现象


        vp_stu_home.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int
                    positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                rg_group_stu.check(bt[position]);// viewpager滑动式让button跟随变化
                tv_title.setText(titles[position]);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //根据button选择不同pager页面
        rg_group_stu.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    //设置当前页面
                    case R.id.stu_home_rd1:
                        vp_stu_home.setCurrentItem(0, false);


                        break;
                    case R.id.stu_home_rd2:
                        vp_stu_home.setCurrentItem(1, false);
                        break;
                    case R.id.stu_home_rd3:
                        vp_stu_home.setCurrentItem(2, false);
                        break;
                    case R.id.stu_home_rd4:
                        vp_stu_home.setCurrentItem(3, false);
                        break;

                }
            }

        });
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

    @Override
    public void onClick(View v) {

    }
}

