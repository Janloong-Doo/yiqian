package com.du.easysignin.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * 登录信息
 * 作者：DragonDu
 * Email: 807110586@qq.com
 */
@Table(name = "signinfo")
public class SignInfo {
    @Column(name = "card_name", isId = true)
    String card_name;
    @Column(name = "personname")
    String personname;
    @Column(name = "ip_address")
    String ip_address;
    @Column(name = "mMac")
    String mMac;
    @Column(name = "sign_time")
    String sign_time;

    @Override
    public String toString() {
        return " 学生：{ \n" +
                " 学号  ： " + card_name + '\n' +
                " 姓名  ： " + personname + '\n' +
                " IP地址 ：" + ip_address + '\n' +
                " Mac地址 ： " + mMac + '\n' +
                " 签到时间 ： " + sign_time + "  }  "+'\n'+'\n'
                ;
    }

    public String getmMac() {
        return mMac;
    }

    public void setmMac(String mMac) {
        this.mMac = mMac;
    }

    public String getPersonname() {
        return personname;
    }

    public void setPersonname(String personname) {
        this.personname = personname;
    }

    public String getCard_name() {
        return card_name;
    }

    public void setCard_name(String card_name) {
        this.card_name = card_name;
    }

    public String getIp_address() {
        return ip_address;
    }

    public void setIp_address(String ip_address) {
        this.ip_address = ip_address;
    }

    public String getSign_time() {
        return sign_time;
    }

    public void setSign_time(String sign_time) {
        this.sign_time = sign_time;
    }
}
