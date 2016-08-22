package com.du.easysignin.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.du.easysignin.R;
import com.du.easysignin.base.BaseAcitivity;
import com.du.easysignin.consts.AllConsts;
import com.du.easysignin.sql.StuTable;
import com.du.easysignin.thread.SendDataThread;
import com.du.easysignin.utils.WifiUtils;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * 学生注册页面
 * 作者：DragonDu
 * Email: 807110586@qq.com
 */
public class StuRegistActivity extends BaseAcitivity implements View.OnClickListener {

    private EditText regist_et_idcard;//学号输入框
    private EditText regist_et_password;//密码输入框
    private EditText regist_et_name;//名字输入框
    private Button regist_bt_regist;//注册按钮
    private String card_number;
    private String pass_number;
    private String name;
    private StuTable info;
    public static StuRegistActivity instance=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stu_regist);
        instance=this;
        initUI();
        initEvent();
    }

    @Override
    public void initUI() {
        regist_et_idcard = (EditText) findViewById(R.id.regist_et_idcard);
        regist_et_password = (EditText) findViewById(R.id.regist_et_password);
        regist_et_name = (EditText) findViewById(R.id.regist_et_name);
        regist_bt_regist = (Button) findViewById(R.id.regist_bt_regist);
        regist_bt_regist.setOnClickListener(this);

    }


    @Override
    public void initEvent() {
        super.initEvent();



    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //注册按钮
            case R.id.regist_bt_regist:
                //获取输入信息
                card_number = regist_et_idcard.getText().toString();
                pass_number = regist_et_password.getText().toString();
                name = regist_et_name.getText().toString();
                info = new StuTable();
                info.setCard_number(card_number);
                info.setPassword(pass_number);
               info.setName(name);
                Gson g=new Gson();
                String s = g.toJson(info);
//                System.out.println(card_number+"--"+pass_number+"--"+name);
                System.out.println(info);

                //请求数据
                ArrayList<String> connectedIP = WifiUtils.getConnectedIP();
                for (String ip : connectedIP) {
                    if (ip.contains(".")) {
                        System.out.println("ip" + ip);
//                        String str = "我想登录 可以么 亲。。。。";
//                        System.out.println("StuRegist info:"+ s);
                        new SendDataThread(handler, ip, 12345, s, AllConsts.STUREGIST_REQUEST).start();
                    }
                }

            break;
        }
    }

}
