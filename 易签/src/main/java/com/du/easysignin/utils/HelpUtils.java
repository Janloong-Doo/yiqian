package com.du.easysignin.utils;

import android.widget.Toast;

import com.du.easysignin.base.BaseApplication;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * 小工具类
 * 作者：DragonDu
 * Email: 807110586@qq.com
 */
public class HelpUtils {

    public static String FORMATTIMESTR = "yyyy年MM月dd日 HH:mm:ss"; // 时间格式化格式
    public static String DATAFILETYPE = "yyyyMMdd"; // 时间格式化格式

    /**
     * 获取yyyyMMdd格式日期
     *
     * @param type 格式化格式
     * @return
     */
    public static String getDate(String type) {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat(type);
        String s = format.format(date);
        return s;
    }

    /**
     * 获取所有ip地址
     */
    public void getAllIp(){
        ArrayList<String> connectedIP = WifiUtils.getConnectedIP();

        StringBuilder resultList = new StringBuilder();

        for (String ip : connectedIP) {
            resultList.append(ip);
            resultList.append("\n");
        }
        Toast.makeText(BaseApplication.getInstance(), "连接到手机上的Ip是：" + resultList.toString(),
                Toast.LENGTH_SHORT).show();
    }
}
