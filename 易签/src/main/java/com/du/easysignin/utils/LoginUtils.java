package com.du.easysignin.utils;

import android.os.Environment;
import android.os.Message;
import android.util.Log;
import android.view.SoundEffectConstants;

import com.du.easysignin.bean.SignInfo;
import com.du.easysignin.consts.AllConsts;
import com.du.easysignin.fragment.TeaFragment1;
import com.du.easysignin.sql.DbHelper;
import com.du.easysignin.sql.StuTable;
import com.google.gson.Gson;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 登录类工具
 * 作者：DragonDu
 * Email: 807110586@qq.com
 */
public class LoginUtils {
    /**
     * 用户登录类
     *
     * @param card
     * @param pass
     * @return
     */
    public static int loginCheck(String card, String pass) {
        //班级信息文件夹
//        File file = new File(Environment.getExternalStorageDirectory() + "/yiqian/class");
//        if (!file.exists()) {
//            file.mkdirs();
//            // System.out.println("教师数据库文件目录创建" + file.toString());
//        }
        File classDir = FileUtils.getfilepath(AllConsts.FILE_CLASS);
        SharePreferenceUtils utils = new SharePreferenceUtils();
        String dbname = utils.getString(AllConsts.TEALOGINCLASS);
        dbname = dbname + ".db";
        DbManager.DaoConfig config = DbHelper.getDaoconfig(dbname, classDir, 1);
        DbManager d = x.getDb(config);

        try {
            StuTable byId = d.findById(StuTable.class, card);
            if (byId == null) {
                return AllConsts.STULOGIN_REQUEST_NOCARDE;
            }
//            System.out.println("查询数据库中的密码" + byId.getPassword());

            if (pass.equals(byId.getPassword())) {
                //密码正确
                return AllConsts.STULOGIN_REQUEST_TRUE;
            } else {
                //密码错误
                return AllConsts.STULOGIN_REQUEST_FALSE;
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        try {
            d.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 用户注册类(班级信息数据库的创建)
     */
    public static int requestRegist(String main) {
        Gson gson = new Gson();
        StuTable stuTable = gson.fromJson(main, StuTable.class);
        String regist_card = stuTable.getCard_number();
        //创建学生信息文件夹
//        File file = new File(Environment.getExternalStorageDirectory() + "/yiqian/class");
        File classDir = FileUtils.getfilepath(AllConsts.FILE_CLASS);
//        if (!file.exists()) {
//            file.mkdirs();
//            // System.out.println("教师数据库文件目录创建" + file.toString());
//        }
        SharePreferenceUtils utils = new SharePreferenceUtils();
        String dbname = utils.getString(AllConsts.TEALOGINCLASS);
        stuTable.setClassName(dbname);
        dbname = dbname + ".db";
        DbManager.DaoConfig config = DbHelper.getDaoconfig(dbname, classDir, 1);
        DbManager d = x.getDb(config);
        StuTable byId;
        try {
            byId = d.findById(StuTable.class, regist_card);
            if (byId == null) {
                //添加信息
                d.save(stuTable);

                return AllConsts.STUREGIST_REQUEST_TRUE;
            } else {
                //用户已存在
                return AllConsts.STUREGIST_REQUEST_FALSE;
            }
        } catch (DbException e) {
            e.printStackTrace();
        }finally {
            try {
                d.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    /**
     * 通过id查询名字
     */
    public static String getName(String card) {
//        File file = new File(Environment.getExternalStorageDirectory() + "/yiqian/class");
//        if (!file.exists()) {
//            file.mkdirs();
//            // System.out.println("教师数据库文件目录创建" + file.toString());
//        }
        File classDir = FileUtils.getfilepath(AllConsts.FILE_CLASS);
        SharePreferenceUtils utils = new SharePreferenceUtils();
        String dbname = utils.getString(AllConsts.TEALOGINCLASS);
        dbname = dbname + ".db";
        DbManager.DaoConfig config = DbHelper.getDaoconfig(dbname, classDir, 1);
        DbManager d = x.getDb(config);

        try {
            StuTable byId = d.findById(StuTable.class, card);
            String name = byId.getName();

            return name;
        } catch (DbException e) {
            e.printStackTrace();
        }finally {

        try {
            d.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        }
        return "";
    }

    /**
     * 签到信息的统计
     */
    public static  int dealLoginInfo(String main, Message msg) {
        Gson s = new Gson();
        SignInfo signInfo = s.fromJson(main, SignInfo.class);
        signInfo.setPersonname(getName(signInfo.getCard_name()));//通过id 获取名字
//        System.out.println("是否查询成功人名："+signInfo.getPersonname());


//        Log.e("查错logutils signinfo",signInfo.toString());
//        Log.e("查错logutils sigCard_name",signInfo.getCard_name());


        SharePreferenceUtils utils=new SharePreferenceUtils();
        String classname = utils.getString(AllConsts.TEALOGINCLASS);
        //初始化数据库文件

//        Log.e("查错logutils classname",classname);

//        File file = new File(Environment.getExternalStorageDirectory() + "/yiqian/"+classname);
//        if (!file.exists()) {
//            file.mkdirs();
//            // System.out.println("教师数据库文件目录创建" + file.toString());
//        }
        File logClassDir = FileUtils.getfilepath(AllConsts.FILE_LOG + "/" + classname);

//        Log.e("查错logutils logClassDir",logClassDir.toString());
        //数据库名字

        String dbname = HelpUtils.getDate(HelpUtils.DATAFILETYPE).toString();

//        System.out.println("签到信息的数据库名字："+dbname);
        dbname = dbname + ".db";

//        Log.e("查错logutils dbname",dbname);
        DbManager.DaoConfig config2 = DbHelper.getDaoconfig(dbname, logClassDir, 1);
        DbManager de = x.getDb(config2);
        //检验是否已经签到
        SignInfo byId = null;
        try {
            byId = de.findById(SignInfo.class, signInfo.getCard_name());
//            Log.e("时间",byId.getSign_time());
        } catch (DbException e) {
//            Log.e("查找id","失败");
            e.printStackTrace();
        }

        if (byId!= null) {
            System.out.println(AllConsts.STUSIGNIN_REQUEST_FALSE);
//            msg.obj=signInfo;
            return AllConsts.STUSIGNIN_REQUEST_FALSE;
        }
        try {
            de.save(signInfo);//保存数据库

            msg.obj=signInfo;
            return AllConsts.STUSIGNIN_REQUEST_TRUE;
        } catch (DbException e) {
            e.printStackTrace();
        }finally {
            try {
                de.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return -1;
    }

    /**
     * 获得签到日志
     */

    public  static List<SignInfo> getSignLog(){
        SharePreferenceUtils utils=new SharePreferenceUtils();
        String classname = utils.getString(AllConsts.TEALOGINCLASS);
        File logClassDir = FileUtils.getfilepath(AllConsts.FILE_LOG + "/" + classname);
//        Log.e("logutils","logClassDir"+logClassDir.toString());
        //数据库名字
        String dbname = HelpUtils.getDate(HelpUtils.DATAFILETYPE).toString();
//        System.out.println("签到信息的数据库名字："+dbname);
//        Log.e("logutils","数据库名字"+dbname);
        dbname = dbname + ".db";
        //判断日期的日志数据库是否存在
        File file = new File(logClassDir,dbname);
        if(!file.exists()){
            return null;
        }
        DbManager.DaoConfig config3 = DbHelper.getDaoconfig(dbname, logClassDir, 1);
        DbManager de = x.getDb(config3);

        try {

            List<SignInfo> all = de.findAll(SignInfo.class);
            if(all!=null){
//                Log.e("loginutils",all.toString());
                return all;
            }else{
//                Log.e("loginutils","为空");
                return null;
            }
        } catch (DbException e) {
            e.printStackTrace();
        }finally {
            try {
                de.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 签到日志查看
     */

    public static ArrayList<SignInfo> showLog(String classname, String dbname){

        File logClassDir = FileUtils.getfilepath(AllConsts.FILE_LOG + "/" + classname);

        DbManager.DaoConfig config = DbHelper.getDaoconfig(dbname, logClassDir, 1);
        DbManager d = x.getDb(config);

        try {
            ArrayList<SignInfo> all = (ArrayList<SignInfo>) d.findAll(SignInfo.class);
            if(all!=null){
//                Log.e("loginutils",all.toString());
                return all;
            }else{
//                Log.e("loginutils","为空");
                return null;
            }
        } catch (DbException e) {
            e.printStackTrace();
        }finally {
            try {
                d.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
