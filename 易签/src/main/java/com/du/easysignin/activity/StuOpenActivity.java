package com.du.easysignin.activity;

import android.app.Activity;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.du.easysignin.R;
import com.du.easysignin.adapter.WifiapAdapter;
import com.du.easysignin.base.BaseAcitivity;
import com.du.easysignin.consts.WifiApConst;
import com.du.easysignin.dialog.ConnWifiDialog;
import com.du.easysignin.utils.WifiUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 学生打开wifi页面
 * 作者：DragonDu
 * Email: 807110586@qq.com
 */
public class StuOpenActivity extends BaseAcitivity implements View.OnClickListener, AdapterView
        .OnItemClickListener {

    private LinearLayout wifiap_ly_wifistatus;

    private ListView wifiap_lv_wifi;//wifi列表listview
    private Button wifi_btn_create;//打开wifi
    private Button wifi_btn_next;//下一步
    private ArrayList<ScanResult> mWifiList; // 符合条件的热点列表
    private WifiapAdapter wifiapAdapter;
    private TextView wifiap_tv_wifistatus;//wifi状态展示
    private ApHandler apHandler;
    private SearchWifiThread mSearchWifiThread;
    private ConnWifiDialog mConnWifiDialog;
    private String localIPaddress; // 本地WifiIP
    private String serverIPaddres; // 热点IP

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stu_login);
        initUI();
        initAction();
        initEvent();
    }


    @Override
    public void initUI() {
        wifiap_ly_wifistatus = (LinearLayout) findViewById(R.id.wifiap_ly_wifistatus);
        wifiap_tv_wifistatus = (TextView) findViewById(R.id.wifiap_tv_wifistatus);
        wifiap_lv_wifi = (ListView) findViewById(R.id.wifiap_lv_wifi);
        wifi_btn_create = (Button) findViewById(R.id.wifi_btn_create);
        wifi_btn_next = (Button) findViewById(R.id.wifi_btn_next);


    }

    @Override
    public void initEvent() {


        mWifiList = new ArrayList<ScanResult>();
//        getWifiList();

        wifiapAdapter = new WifiapAdapter(this, mWifiList);

        wifiap_lv_wifi.setAdapter(wifiapAdapter);
        apHandler = new ApHandler();
        mSearchWifiThread = new SearchWifiThread(apHandler);
        mSearchWifiThread.start();//开启扫描列表线程
        mConnWifiDialog = new ConnWifiDialog(this, apHandler);
        wifi_btn_create.setOnClickListener(this);
        wifi_btn_next.setOnClickListener(this);
        wifiap_lv_wifi.setOnItemClickListener(this);
    }

    /**
     * 初始化控件设置
     */
    private void initAction() {
        if (!WifiUtils.isWifiEnabled() && !WifiUtils.isWifiApEnabled()) { // 无开启热点无连接WIFI
            wifiap_tv_wifistatus.setText(getString(R.string.wifiap_text_wifi_0));

            WifiUtils.OpenWifi();
            showLongToast("正在开启wifi之中……");
        }

        if (WifiUtils.isWifiConnect()) { // Wifi已连接
            wifiap_tv_wifistatus.setText(getString(R.string.wifiap_text_wifi_connected)
                    + WifiUtils.getSSID());

        }

        if (WifiUtils.isWifiApEnabled()) { // 已开启热点
//            if (WifiUtils.getApSSID().startsWith(WifiApConst.WIFI_AP_HEADER)) {
//                wifiap_tv_wifistatus.setText(getString(R.string.wifiap_text_ap_1));
////                mLvWifiList.setVisibility(View.GONE);
////                mLlApInfo.setVisibility(View.VISIBLE);
////                mTvApSSID.setText("SSID: " + WifiUtils.getApSSID());
////                mBtnCreateAp.setText(getString(R.string.wifiap_btn_closeap));
//            }
//            else {
                WifiUtils.closeWifiAp();
                WifiUtils.OpenWifi();
                wifiap_tv_wifistatus.setText("正在开启wifi之中");
//            }
        }

        if (WifiUtils.isWifiEnabled() && !WifiUtils.isWifiConnect()) { // Wifi已开启，未连接
            wifiap_tv_wifistatus.setText(getString(R.string.wifiap_text_wifi_1_0));
        }

//        mSearchWifiThread.start();
    }

    /**
     * 扫描周围wifi信息
     */
    private void getWifiList() {
        mWifiList.clear();
        WifiUtils.startScan();
        List<ScanResult> scanResults = WifiUtils.getScanResults();
        if (scanResults != null) {
            mWifiList.addAll(scanResults);
            if (!WifiUtils.isWifiConnect()) {
                wifiap_tv_wifistatus.setText(getString(R.string.wifiap_text_wifi_1_0));
            }
        }
    }

    /**
     * 点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.wifi_btn_create:
              getWifiList();

                break;
            case R.id.wifi_btn_next:
                //点击下一步
            if(isValidated()){
                startActivity(StuLoginActivity.class);
                finish();
            }


                break;
        }
    }

    /**
     * 点击wifi列表条目
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ScanResult ap = mWifiList.get(position);
        if (ap.SSID.startsWith(WifiApConst.WIFI_AP_HEADER)) {
            wifiap_tv_wifistatus.setText(getString(R.string.wifiap_btn_connecting) + ap.SSID);
            // 连接网络
            boolean connFlag = WifiUtils.connectWifi(ap.SSID, WifiApConst.WIFI_AP_MPASSWORD,//设置专用网络连接
                    WifiUtils.WifiCipherType.WIFICIPHER_WPA);
            if (!connFlag) {
                wifiap_tv_wifistatus.setText(getString(R.string.wifiap_toast_connectap_error_1));
                apHandler.sendEmptyMessage(WifiApConst.WiFiConnectError);
            }
        }
        else if (!WifiUtils.isWifiConnect() || !ap.BSSID.equals(WifiUtils.getBSSID())) {
            mConnWifiDialog.setTitle(ap.SSID);
            mConnWifiDialog.setScanResult(ap);
            mConnWifiDialog.show();
        }
    }


    /**
     * 定时刷新Wifi列表信息
     */
    class SearchWifiThread implements Runnable {
        private boolean running = false;
        private Thread thread = null;
        private Handler handler = null;

        SearchWifiThread(Handler handler) {
            this.handler = handler;
        }

        public void run() {
            while (!WifiUtils.isWifiApEnabled()) {
                if (!this.running)
                    return;
                try {
                    Thread.sleep(1000); // 扫描间隔
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(WifiApConst.ApScanResult);
            }
        }

        public void start() {
            try {
                this.thread = new Thread(this);
                this.running = true;
                this.thread.start();
            } finally {
            }
        }

        public void stop() {
            try {
                this.running = false;
                this.thread = null;
            } finally {
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
                case WifiApConst.ApScanResult: // 扫描Wifi列表
                    if (isRespond) {
                        getWifiList();
                        refreshAdapter(mWifiList);
                    }
                    break;

                case WifiApConst.NetworkChanged: // Wifi状态变化
                    if (WifiUtils.isWifiEnabled()) {
                        wifiap_tv_wifistatus.setText(getString(R.string.wifiap_text_wifi_1_0));
                    } else {
                        wifiap_tv_wifistatus.setText(getString(R.string.wifiap_text_wifi_0));
                        showShortToast(R.string.wifiap_text_wifi_disconnect);
                    }

                default:
                    break;
            }
        }
    }

    /**
     * 刷新热点列表UI
     *
     * @param list
     */
    public void refreshAdapter(List<ScanResult> list) {
        wifiapAdapter.setData(list);
        wifiapAdapter.notifyDataSetChanged();
    }

    /**
     * 设置IP地址信息
     *
     *
     *            是否为客户端
     */
    public void setIPaddress() {
        if (WifiUtils.isWifiApEnabled()) {
            serverIPaddres = localIPaddress = "192.168.43.1";
        }
        else {
            localIPaddress = WifiUtils.getLocalIPAddress();
            serverIPaddres = WifiUtils.getServerIPAddress();
            System.out.println("localIPaddress"+localIPaddress+"serverIPaddres"+serverIPaddres);
        }
//        logger.i("localIPaddress:" + localIPaddress + " serverIPaddres:" + serverIPaddres);
    }


    /**
     * IP地址正确性验证
     *
     * @return boolean 返回是否为正确， 正确(true),不正确(false)
     */
    private boolean isValidated() {

        setIPaddress();
        String nullIP = "0.0.0.0";

        if (nullIP.equals(localIPaddress) || nullIP.equals(serverIPaddres)
                || localIPaddress == null || serverIPaddres == null) {
            showShortToast(R.string.wifiap_toast_connectap_unavailable);
            return false;
        }

        return true;
    }

    @Override
    public void finish() {
        super.finish();
        mSearchWifiThread.stop();
    }
}
