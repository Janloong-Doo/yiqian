package com.du.easysignin.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.du.easysignin.base.BaseApplication;
import com.du.easysignin.consts.WifiApConst;

import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;


/**
 * wifi工具类
 * 作者：DragonDu
 * Email: 807110586@qq.com
 */

public class WifiUtils {
    private static Context mContext = BaseApplication.getInstance();
    private static WifiManager mWifiManager = (WifiManager) mContext
            .getSystemService(Context.WIFI_SERVICE);

    /**
     * wifi的几种加密方式
     */
    public static enum WifiCipherType {
        WIFICIPHER_WEP, WIFICIPHER_WPA, WIFICIPHER_NOPASS, WIFICIPHER_INVALID
    }

    /**
     * 开启AP
     * @param ssid    热点名称
     * @param passwd  热点密码
     * @param handler
     */
    public static void startWifiAp(String ssid, String passwd, final Handler handler) {
        if (mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(false);
        }
        startAp(ssid, passwd);

        SchedulesUtils schedulesThread = new SchedulesUtils() {
            @Override
            public void doTimerCheckWork() {

                if (isWifiApEnabled()) {
                    // LogUtils.v("WifiAp enabled success!");
                    Message msg = handler.obtainMessage(WifiApConst.ApCreateApSuccess);
                    handler.sendMessage(msg);
                    this.exit();
                }
                else {
                    // LogUtils.v("WifiAp enabled failed!");
                }
            }

            @Override
            public void doTimeOutWork() {
                // TODO Auto-generated method stub
                this.exit();
            }
        };
        schedulesThread.start(10, 1000);

    }

    /**
     * 开启AP工具方法
     * @param ssid
     * @param passwd
     */
    public static void startAp(String ssid, String passwd) {
        Method method1 = null;
        try {
            method1 = mWifiManager.getClass().getMethod("setWifiApEnabled",
                    WifiConfiguration.class, boolean.class);
            WifiConfiguration netConfig = new WifiConfiguration();

            netConfig.SSID = ssid;
            netConfig.preSharedKey = passwd;

            netConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            netConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            netConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            netConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            netConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            netConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            netConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            netConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);

            method1.invoke(mWifiManager, netConfig, true);

        }
        catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 关闭AP
     */
    public static void closeWifiAp() {
        if (isWifiApEnabled()) {
            try {
                Method method = mWifiManager.getClass().getMethod("getWifiApConfiguration");
                method.setAccessible(true);

                WifiConfiguration config = (WifiConfiguration) method.invoke(mWifiManager);

                Method method2 = mWifiManager.getClass().getMethod("setWifiApEnabled",
                        WifiConfiguration.class, boolean.class);
                method2.invoke(mWifiManager, config, false);
            }
            catch (NoSuchMethodException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch (InvocationTargetException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     * 热点是否关闭
     * @return
     */
    public static boolean isWifiApEnabled() {
        try {
            Method method = mWifiManager.getClass().getMethod("isWifiApEnabled");
            method.setAccessible(true);
            return (Boolean) method.invoke(mWifiManager);

        }
        catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 获取AP的状态
     * @return
     */
    public static int getWifiApStateInt() {
        try {
            int i = ((Integer) mWifiManager.getClass().getMethod("getWifiApState", new Class[0])
                    .invoke(mWifiManager, new Object[0])).intValue();
            return i;
        }
        catch (Exception localException) {
        }
        return 4;
    }

    /**
     * 判断是否连接上wifi
     * 
     * @return boolean值(isConnect),对应已连接(true)和未连接(false)
     */
    public static boolean isWifiConnect() {
        NetworkInfo mNetworkInfo = ((ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE))
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return mNetworkInfo.isConnected();
    }

    /**
     * 判断wifi是否开启
     * @return
     */
    public static boolean isWifiEnabled() {
        return mWifiManager.isWifiEnabled();
    }

    public static void OpenWifi() {
        if (!mWifiManager.isWifiEnabled())
            mWifiManager.setWifiEnabled(true);
    }

    /**
     * 关闭wifi
     */
    public static void closeWifi() {
        mWifiManager.setWifiEnabled(false);
    }

    public static void addNetwork(WifiConfiguration paramWifiConfiguration) {
        int i = mWifiManager.addNetwork(paramWifiConfiguration);
        mWifiManager.enableNetwork(i, true);
    }

    public static void removeNetwork(int netId) {
        if (mWifiManager != null) {
            mWifiManager.removeNetwork(netId);
            mWifiManager.saveConfiguration();
        }
    }

    /**
     * Function: 连接Wifi热点 <br>
     * 
     * @date 2015年2月14日 上午11:17
     * @change hillfly
     * @version 1.0
     * @param SSID
     * @param Password
     * @param Type
     * <br>
     *            没密码： {@linkplain WifiCipherType#WIFICIPHER_NOPASS}<br>
     *            WEP加密: {@linkplain WifiCipherType#WIFICIPHER_WEP}<br>
     *            WPA加密： {@linkplain WifiCipherType#WIFICIPHER_WPA}<br>
     * @return true:连接成功；false:连接失败
     */
    public static boolean connectWifi(String SSID, String Password, WifiCipherType Type) {
        if (!isWifiEnabled()) {
            return false;
        }
        // 开启wifi需要一段时间,要等到wifi状态变成WIFI_STATE_ENABLED
        while (mWifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLING) {
            try {
                // 避免程序不停循环
                Thread.currentThread();
                Thread.sleep(500);
            }
            catch (InterruptedException ie) {
            }
        }

        WifiConfiguration wifiConfig = createWifiInfo(SSID, Password, Type);
        if (wifiConfig == null) {
            return false;
        }

        WifiConfiguration tempConfig = isExsits(SSID);

        if (tempConfig != null) {
            mWifiManager.removeNetwork(tempConfig.networkId);
        }

        int netID = mWifiManager.addNetwork(wifiConfig);

        // 断开连接
        mWifiManager.disconnect();
        
        // 设置为true,使其他的连接断开
        boolean bRet = mWifiManager.enableNetwork(netID, true);
        mWifiManager.reconnect();
        return bRet;
    }

    public static void disconnectWifi(int paramInt) {
        mWifiManager.disableNetwork(paramInt);
    }

    public static void startScan() {
        mWifiManager.startScan();
    }

    /**
     * 配置一个wifi连接信息对象
     * @param SSID
     * @param Password
     * @param Type
     * @return
     */
    private static WifiConfiguration createWifiInfo(String SSID, String Password,
            WifiCipherType Type) {
        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        config.SSID = "\"" + SSID + "\"";
        if (Type == WifiCipherType.WIFICIPHER_NOPASS) {
            config.wepKeys[0] = "";
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
        }
        if (Type == WifiCipherType.WIFICIPHER_WEP) {
            config.preSharedKey = "\"" + Password + "\"";
            config.hiddenSSID = true;
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
        }
        if (Type == WifiCipherType.WIFICIPHER_WPA) {

            config.preSharedKey = "\"" + Password + "\"";
            config.hiddenSSID = true;
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            // config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);

        }
        else {
            return null;
        }
        return config;
    }

    /**
     * 获取AP 的 SSID
     * @return 返回AP的SSID
     */
    public static String getApSSID() {
        try {
            Method localMethod = mWifiManager.getClass().getDeclaredMethod(
                    "getWifiApConfiguration", new Class[0]);
            if (localMethod == null)
                return null;
            Object localObject1 = localMethod.invoke(mWifiManager, new Object[0]);
            if (localObject1 == null)
                return null;
            WifiConfiguration localWifiConfiguration = (WifiConfiguration) localObject1;
            if (localWifiConfiguration.SSID != null)
                return localWifiConfiguration.SSID;
            Field localField1 = WifiConfiguration.class.getDeclaredField("mWifiApProfile");
            if (localField1 == null)
                return null;
            localField1.setAccessible(true);
            Object localObject2 = localField1.get(localWifiConfiguration);
            localField1.setAccessible(false);
            if (localObject2 == null)
                return null;
            Field localField2 = localObject2.getClass().getDeclaredField("SSID");
            localField2.setAccessible(true);
            Object localObject3 = localField2.get(localObject2);
            if (localObject3 == null)
                return null;
            localField2.setAccessible(false);
            String str = (String) localObject3;
            return str;
        }
        catch (Exception localException) {
        }
        return null;
    }

    /**
     * 获取BSSID
     * @return
     */
    public static String getBSSID() {
        WifiInfo mWifiInfo = mWifiManager.getConnectionInfo();
        return mWifiInfo.getBSSID();
    }
    /**
     * 获取SSID
     * @return
     */
    public static String getSSID() {
        WifiInfo mWifiInfo = mWifiManager.getConnectionInfo();
        return mWifiInfo.getSSID();
    }
    /**
     * 获取IP地址
     * @return
     */
    public static String getLocalIPAddress() {
        WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
        return intToIp(wifiInfo.getIpAddress());
    }
    /**
     * 获取getGPRSLocalIPAddress
     * @return
     */
//    public static String getGPRSLocalIPAddress() {
//        try {
//            Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
//            while (en.hasMoreElements()) {
//                NetworkInterface nif = en.nextElement();
//                Enumeration<InetAddress> enumIpAddr = nif.getInetAddresses();
//                while (enumIpAddr.hasMoreElements()) {
//                    InetAddress mInetAddress = enumIpAddr.nextElement();
//                    if (!mInetAddress.isLoopbackAddress()
//                            && InetAddressUtils.isIPv4Address(mInetAddress.getHostAddress())) {
//                        return mInetAddress.getHostAddress();
//                    }
//                }
//            }
//        }
//        catch (SocketException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    /**
     * 获取服务端的IPAddress
     * @return
     */
    public static String getServerIPAddress() {
        DhcpInfo mDhcpInfo = mWifiManager.getDhcpInfo();
        return intToIp(mDhcpInfo.gateway);
    }

    /**
     * 获取getBroadcastAddress
     * @return
     */
//    public static String getBroadcastAddress() {
//        System.setProperty("java.net.preferIPv4Stack", "true");
//        try {
//            for (Enumeration<NetworkInterface> niEnum = NetworkInterface.getNetworkInterfaces(); niEnum
//                    .hasMoreElements();) {
//                NetworkInterface ni = niEnum.nextElement();
//                if (!ni.isLoopback()) {
//                    for (InterfaceAddress interfaceAddress : ni.getInterfaceAddresses()) {
//                        if (interfaceAddress.getBroadcast() != null) {
//                            logger.d(interfaceAddress.getBroadcast().toString().substring(1));
//                            return interfaceAddress.getBroadcast().toString().substring(1);
//                        }
//                    }
//                }
//            }
//        }
//        catch (SocketException e) {
//            e.printStackTrace();
//        }
//
//        return null;
//    }

    /**
     * 获取MAC地址
     * @return
     */
    public static String getMacAddress() {
        WifiInfo mWifiInfo = mWifiManager.getConnectionInfo();
        if (mWifiInfo == null)
            return "NULL";
        return mWifiInfo.getMacAddress();
    }
    /**
     * 获取网络ID
     * @return
     */
    public static int getNetworkId() {
        WifiInfo mWifiInfo = mWifiManager.getConnectionInfo();
        if (mWifiInfo == null)
            return 0;
        return mWifiInfo.getNetworkId();
    }

    /**
     * 获取WIFI的连接信息
     * @return
     */
    public static WifiInfo getWifiInfo() {
        return mWifiManager.getConnectionInfo();
    }

    /**
     * 获取附近的wifi的信息
     * @return
     */
    public static List<ScanResult> getScanResults() {
        return mWifiManager.getScanResults();
    }

    /**
     * 查看以前是否也配置过这个网络
     * @param SSID
     * @return
     */
    private static WifiConfiguration isExsits(String SSID) {
        List<WifiConfiguration> existingConfigs = mWifiManager.getConfiguredNetworks();
        for (WifiConfiguration existingConfig : existingConfigs) {
            if (existingConfig.SSID.equals("\"" + SSID + "\"")) {
                return existingConfig;
            }
        }
        return null;
    }

    /**
     * ip的格式化
     * @param i
     * @return
     */
    private static String intToIp(int i) {
        return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF) + "."
                + ((i >> 24) & 0xFF);
    }


    /**
     * 获取到连接到手机上的所有IP
     * @return
     */
    public  static ArrayList<String> getConnectedIP() {//获取连接到本机热点上的手机ip
        ArrayList<String> connectedIP = new ArrayList<String>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(
                    "/proc/net/arp"));
            String line;
            while ((line = br.readLine()) != null) {
                String[] splitted = line.split(" +");
                if (splitted != null && splitted.length >= 4) {
                    String ip = splitted[0];
                    connectedIP.add(ip);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connectedIP;
    }
}