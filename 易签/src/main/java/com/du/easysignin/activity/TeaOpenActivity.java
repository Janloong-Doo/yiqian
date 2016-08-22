package com.du.easysignin.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.du.easysignin.R;
import com.du.easysignin.base.BaseAcitivity;
import com.du.easysignin.consts.WifiApConst;
import com.du.easysignin.utils.WifiUtils;
import com.du.easysignin.view.BaseDialog;
/**
 * 教师开启热点页面
 * 作者：DragonDu
 * Email: 807110586@qq.com
 */

public class TeaOpenActivity extends BaseAcitivity implements View.OnClickListener {

    private Button create_ap_fast;//快速创建按钮
    private Button create_ap_normal;//自定义创建创建按钮
    private EditText et_ap_name;//输入的热点名称
    private EditText et_ap_password;//输入的热点密码

    String name;
    String pass;
    //自定义的对话框
    private BaseDialog mHintDialog;
    private ApHandler apHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tea_login);
        initUI();
        initEvent();
    }

    public void initUI() {
        create_ap_fast = (Button) findViewById(R.id.create_ap_fast);
        create_ap_normal = (Button) findViewById(R.id.create_ap_normal);
        et_ap_name = (EditText) findViewById(R.id.et_ap_name);
        et_ap_password = (EditText) findViewById(R.id.et_ap_password);
        create_ap_normal.setOnClickListener(this);
        create_ap_fast.setOnClickListener(this);


    }

    @Override
    public void initEvent() {

        //初始化dialog事件
        hintDialogOnClick dialogOnClick = new hintDialogOnClick();
        mHintDialog = BaseDialog.getDialog(this, R.string.dialog_tips, "", "确定",
                dialogOnClick,
                "取消", dialogOnClick);

        apHandler = new ApHandler();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            //快速开启
            case R.id.create_ap_fast:
                et_ap_name.setText("");
                et_ap_password.setText("");
                //快速创建
                name = WifiApConst.WIFI_AP_HEADER + WifiApConst.WIFI_AP_MNAME;
                pass = WifiApConst.WIFI_AP_MPASSWORD;
                createAp();
                break;
            case R.id.create_ap_normal:
                //自定义创建创建
                String getname = et_ap_name.getText().toString();
                String getpass = et_ap_password.getText().toString();
                if (getname.isEmpty() || getpass.isEmpty()) {
                    showLongToast("输入名称或密码不能为空");
                    break;
                } else if (getpass.trim().length() < 8) {
//                    showAlertDialog("提示", "密码长度不能小于8位");
                    showLongToast("密码长度不能小于8位");
                    break;
                } else {
                    name = getname.trim();
                    pass = getpass.trim();
//                    System.out.println(name + "-----" + pass);
                    createAp();
                }

                break;

        }
    }

    /**
     * 创建热点
     */
    public void createAp() {
        // 如果不支持热点创建
        System.out.println("state" + WifiUtils.getWifiApStateInt());
        if (WifiUtils.getWifiApStateInt() == 4) {
            showShortToast(R.string.wifiap_dialog_createap_nonsupport);
            return;
        }
        // 如果wifi正打开着的，就提醒用户
        if (WifiUtils.isWifiEnabled()) {
            mHintDialog.setMessage(getString(R.string.wifiap_dialog_createap_closewifi_confirm));
            mHintDialog.show();
            return;
        }
        // 如果存在一个共享热点
        if (((WifiUtils.getWifiApStateInt() == 3) || (WifiUtils.getWifiApStateInt() == 13))) {
//                && (WifiUtils.getApSSID().startsWith(WifiApConst.WIFI_AP_HEADER))) {
            if (WifiUtils.getApSSID().startsWith(WifiApConst.WIFI_AP_HEADER) && TextUtils.isEmpty
                    (et_ap_name.getText().toString())){//判断输入栏不为空且有热点开启
                showShortToast("热点已创建");
                startActivity(TeaLoginAcitivity.class);
                finish();
                return;
            } else {
                mHintDialog.setMessage(getString(R.string.wifiap_dialog_closeap_confirm));
                mHintDialog.show();
                return;
            }
        }
        if ((WifiUtils.getWifiApStateInt() == 11)) {
            mHintDialog.setMessage(getString(R.string.wifiap_dialog_createap_closewifi_confirm));
            mHintDialog.show();
        }
    }

    /**
     * dialog按钮监听事件
     */
    public class hintDialogOnClick implements DialogInterface.OnClickListener {

        @Override
        public void onClick(DialogInterface hintDialog, int which) {
            switch (which) {

                // 确定
                case 0:
                    hintDialog.dismiss();
                    if (WifiUtils.isWifiApEnabled()) {

                        // 执行关闭热点事件
                        WifiUtils.closeWifiAp();
//                        WifiUtils.OpenWifi();
//
//                        showShortToast(R.string.wifiap_text_ap_0);
//                        mTvStatusInfo.setText(getString(R.string.wifiap_text_wifi_1_0));
//                        mBtnCreateAp.setText(getString(R.string.wifiap_btn_createap));
//                        mLlApInfo.setVisibility(View.GONE);
//                        mLvWifiList.setVisibility(View.VISIBLE);
//
//                        localIPaddress = null;
//                        serverIPaddres = null;
//
//                        mSearchWifiThread.start();
                        showShortToast("正在开启热点中……");
                        WifiUtils.startWifiAp(name, pass, apHandler);
                    } else {
//                        // 创建热点
//                        mTvStatusInfo.setText(getString(R.string.wifiap_text_createap_creating));
//                        mBtnBack.setClickable(false);
//                        mBtnCreateAp.setClickable(false);
//                        mBtnNext.setClickable(false);
//                        WifiUtils.startWifiAp(WifiApConst.WIFI_AP_HEADER + getLocalHostName(),
//                                WifiApConst.WIFI_AP_PASSWORD, mHandler);
                        showShortToast("正在开启热点中……");
                        WifiUtils.startWifiAp(name, pass, apHandler);

                    }
                    break;

                // 取消
                case 1:
                    hintDialog.cancel();
                    break;
            }
        }

    }

    /**
     * handler处理热点连接情况
     */
    private class ApHandler extends Handler {

        private boolean isRespond = true;

        public ApHandler() {
        }

        public void setRespondFlag(boolean flag) {
            isRespond = flag;
        }

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
//                case WifiApConst.ApScanResult: // 扫描Wifi列表
//                    if (isRespond) {
//                        getWifiList();
//                        refreshAdapter(mWifiList);
//                    }
//                    break;

                case WifiApConst.ApCreateApSuccess: // 创建热点成功
//                    mSearchWifiThread.stop();
//                    mTvStatusInfo.setText(getString(R.string.wifiap_text_createap_succeed));
//                    mLvWifiList.setVisibility(View.GONE);
//                    mLlApInfo.setVisibility(View.VISIBLE);
//                    mTvApSSID.setText("SSID: " + WifiUtils.getApSSID());
//                    mBtnCreateAp.setText(getString(R.string.wifiap_btn_closeap));
//                    mBtnBack.setClickable(true);
//                    mBtnCreateAp.setClickable(true);
//                    mBtnNext.setClickable(true);
                    showShortToast("热点开启成功");
//                    System.out.println("创建热点成功");
                    startActivity(TeaLoginAcitivity.class);
                    finish();
                    break;

//                case WifiApConst.WiFiConnectSuccess: // 连接热点成功
//                    String str = getString(R.string.wifiap_text_wifi_connected)
//                            + WifiUtils.getSSID();
//                    mTvStatusInfo.setText(str);
//                    showShortToast(str);
//                    break;

//                case WifiApConst.WiFiConnectError: // 连接热点错误
//                    showShortToast(R.string.wifiap_toast_connectap_error);
//                    break;

//                case WifiApConst.NetworkChanged: // Wifi状态变化
//                    if (WifiUtils.isWifiEnabled()) {
//                        mTvStatusInfo.setText(getString(R.string.wifiap_text_wifi_1_0));
//                    }
//                    else {
//                        mTvStatusInfo.setText(getString(R.string.wifiap_text_wifi_0));
//                        showShortToast(R.string.wifiap_text_wifi_disconnect);
//                    }

                default:
                    break;
            }
        }
    }
}
