package com.du.easysignin.fragment;

import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.du.easysignin.R;
import com.du.easysignin.adapter.ListAdapter;
import com.du.easysignin.base.BaseApplication;
import com.du.easysignin.bean.SignInfo;
import com.du.easysignin.consts.AllConsts;
import com.du.easysignin.thread.SendDataThread;
import com.du.easysignin.utils.HelpUtils;
import com.du.easysignin.utils.LoginUtils;
import com.du.easysignin.utils.SchedulesUtils;
import com.du.easysignin.utils.WifiUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 教师签到页面
 * 作者：DragonDu
 * Email: 807110586@qq.com
 */
public class TeaFragment1 extends BaseFragment {
    private TextView tv_online_number;//所有在线人数
    private TextView tv_signin_number;//所有签到人数
    public ListView lv_sign_info;
    public static List<SignInfo> list = new ArrayList<>();
    public static ListAdapter adapter = new ListAdapter(BaseApplication.getInstance(), list);
    Handler handler;
    private TextView tv_tea_msg;
    public static TeaFragment1 tea;
    private SchedulesUtils sd;
    private int onLineSize = -1;

    public static TeaFragment1 getteafragmentInstance() {
        return tea;
    }

    public TeaFragment1() {

        tea = this;

    }
    public void setHandler(Handler handler) {
        this.handler = handler;
    }
    @Override
    public int getLayoutRes() {
        return R.layout.teapager1;
    }

    @Override
    public void initView() {
        tv_tea_msg = (TextView) parentView.findViewById(R.id.tv_tea_msg);

        tv_online_number = (TextView) parentView.findViewById(R.id.tv_online_number);
        tv_signin_number = (TextView) parentView.findViewById(R.id.tv_signin_number);
        lv_sign_info = (ListView) parentView.findViewById(R.id.lv_sign_info);
        initnumber();
    }

    /**
     * 显示悄悄画
     *
     * @param whisper 悄悄话内容
     */
    public void setwhisper(String whisper) {
        tv_tea_msg.append(HelpUtils.getDate(HelpUtils.FORMATTIMESTR + "\n"));
        tv_tea_msg.append("匿名消息 ： " + whisper + "\n");
    }

    public void setTv_online_number() {
        onLineSize = getOnLineSize();
        if (onLineSize != -1) {
            tv_online_number.setText("当前在线人数：" + onLineSize + " 人");
        }
    }

    /**
     * 设置在线人数
     */
    public void setTv_signin_number() {
        tv_signin_number.setText("当前已签到人数：" + list.size() + " 人");
    }

    @Override
    public void initData() {
        //初始化签到日志
        if (LoginUtils.getSignLog() != null) {
            list.addAll(LoginUtils.getSignLog());
//            Log.e("teafragment1_list", list.toString());
            tv_signin_number.setText("当前已签到人数：" + list.size() + " 人");
        }
        lv_sign_info.setAdapter(adapter);


    }


    @Override
    public void initEvent() {
        tv_tea_msg.setMovementMethod(ScrollingMovementMethod.getInstance());
    }


    public int getOnLineSize() {

        ArrayList<String> ip = WifiUtils.getConnectedIP();
//        String localIPAddress = WifiUtils.getServerIPAddress();
        Log.e("shuaxinip",ip.toString());
//        Log.e("shuaxinip",localIPAddress);

        return ip.size()-1;
    }

    /**
     * 初始化更新数据的线程
     */
    private void initnumber() {
        sd = new SchedulesUtils() {
            @Override
            public void doTimerCheckWork() {

//             setTv_online_number();
                Message msg = Message.obtain();
                msg.what = AllConsts.REFLASH_ONLINE;
                handler.sendMessage(msg);
            }

            @Override
            public void doTimeOutWork() {
                this.exit();
            }
        };
        sd.start(Integer.MAX_VALUE, 5000);
    }

    public void setFinish() {
        list.clear();
        sd.exit();
    }


}
