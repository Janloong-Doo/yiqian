package com.du.easysignin.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.du.easysignin.R;
import com.du.easysignin.base.BaseAcitivity;
import com.du.easysignin.consts.AllConsts;
import com.du.easysignin.service.LocalService;
import com.du.easysignin.sql.StuTable;
import com.du.easysignin.thread.SendDataThread;
import com.du.easysignin.utils.SharePreferenceUtils;
import com.du.easysignin.utils.WifiUtils;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * 学生登录页面
 * 作者：DragonDu
 * Email: 807110586@qq.com
 */
public class StuLoginActivity extends BaseAcitivity implements View.OnClickListener {

    private EditText et_stu_password;//学生密码输入框
    private EditText et_stu_name;//学生学号输入框
    private Button stu_btn_regist;//学生注册按钮
    private Button stu_btn_login;//学生登录按钮
    private mServiceConn conn;
    public static StuLoginActivity instance2=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stu_denglu);
        instance2=this;
        initUI();
        initData();
    }
    public static void actionStart(Activity activity) {
        Intent intent = new Intent(activity, StuLoginActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void initUI() {
        et_stu_name = (EditText) findViewById(R.id.et_stu_name);
        et_stu_password = (EditText) findViewById(R.id.et_stu_password);
        stu_btn_login = (Button) findViewById(R.id.stu_btn_login);
        stu_btn_regist = (Button) findViewById(R.id.stu_btn_regist);
        stu_btn_regist.setOnClickListener(this);
        stu_btn_login.setOnClickListener(this);
    }


    private void initData() {
//        connection();
    }

    /**
     * 页面按钮响应事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //学生注册按钮
            case R.id.stu_btn_regist:
                startActivity(StuRegistActivity.class);
                break;
            //学生登录按钮
            case R.id.stu_btn_login:
                //获取输入数据
                String login_name = et_stu_name.getText().toString().trim();
                String login_pass = et_stu_password.getText().toString().trim();
                if (TextUtils.isEmpty(login_name) || TextUtils.isEmpty(login_pass)) {
                    showShortToast("输入的内容不能为空");
                    break;
                }
                if (login_pass.length() < 8) {
                    showShortToast("输入的密码长度不能小于8位");
                    et_stu_password.setText("");
                    break;
                }
                StuTable s = new StuTable();
                s.setCard_number(login_name);
                s.setPassword(login_pass);
                Gson g = new Gson();
                String ss = g.toJson(s);
                //存储login info
                SharePreferenceUtils utils = new SharePreferenceUtils();
                utils.saveString(AllConsts.STULOGIN_INFO, login_name);
                //请求数据
                ArrayList<String> connectedIP = WifiUtils.getConnectedIP();
                for (String ip : connectedIP) {
                    if (ip.contains(".")) {
                        System.out.println("ip" + ip);
//                        String str = "我想登录 可以么 亲。。。。";
                        new SendDataThread(handler, ip, 12345, ss, AllConsts.STULOGIN_REQUEST)
                                .start();
                    }
                }

//                startActivity(StuHomeActivity.class);
//                StuHomeActivity.actionStart(this);
                break;


        }
    }

//    Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//
//            switch (msg.what) {
//                case 1:
//                    Toast.makeText(StuLoginActivity.this, msg.obj.toString(), Toast.LENGTH_LONG)
//                            .show();
//                    break;
//                case 2:
//                    Toast.makeText(StuLoginActivity.this, msg.obj.toString(), Toast.LENGTH_LONG)
//                            .show();
//                    break;
//            }
//
//        }
//    };


    /**
     * 绑定服务
     */

    private void connection() {
        Intent intent = new Intent(this, LocalService.class);
        conn = new mServiceConn();
        bindService(intent, conn, this.BIND_AUTO_CREATE);
    }

    private LocalService.LocalBinder binder;
    private LocalService myservice;

    class mServiceConn implements ServiceConnection {


//        private LocalBinder binder;

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d("aaa", "绑定成功");
            LocalService.LocalBinder binder = (LocalService.LocalBinder) service;

            //通过IBinder获取Service
            LocalService myservice = binder.getService();
            myservice.startWaitDataThread(handler, false);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }

    @Override
    public void finish() {
        super.finish();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
