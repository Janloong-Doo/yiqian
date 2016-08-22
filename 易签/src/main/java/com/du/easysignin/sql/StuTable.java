package com.du.easysignin.sql;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * 学生班级信息表
 * 作者：DragonDu
 * Email: 807110586@qq.com
 */
@Table(name="classinfo")
public class StuTable {

    //定义学号
    @Column(name="card_number",isId = true)
    private String card_number;

    //定义姓名
    @Column(name="name")
    private String name;

    //定义班级
    @Column(name="classname")
    private  String className;

    //定义管理员
    @Column(name="isAdmin")
    private int isAdmin;

    //定义密码
    @Column(name="password")
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(int isAdmin) {
        this.isAdmin = isAdmin;
    }

//
//    @Override
//    public String toString() {
//        return "StuTable{" +
//                "card_number='" + card_number + '\'' +
//                ", name='" + name + '\'' +
//                ", className='" + className + '\'' +
//                ", isAdmin=" + isAdmin +
//                ", password='" + password + '\'' +
//                '}';
//    }
    @Override
    public String toString() {
        return
                "学生{ 学号  ：" + card_number + '\n' +
                "         姓名  ：" + name + '\n' +
                "         班级  ：" + className + '\n' +
                "         密码  ：" + password + '\n'+
                "         isAdmin  :" + isAdmin +"  }\n\n"
                ;

    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getCard_number() {
        return card_number;
    }

    public void setCard_number(String card_number) {
        this.card_number = card_number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;

    }

}
