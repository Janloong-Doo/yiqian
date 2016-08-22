package com.du.easysignin.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.du.easysignin.base.BaseApplication;

/**
 * sharedpreferences工具类
 * 作者：DragonDu
 * Email: 807110586@qq.com
 */
public class SharePreferenceUtils {
//    private static final String GlobalSharedName = "LocalUserInfo";
    private static final String GlobalSharedName = "config";
    private SharedPreferences mSP;
    public SharedPreferences.Editor mEditor;

    public SharePreferenceUtils() {
        mSP = BaseApplication.getInstance().getSharedPreferences(GlobalSharedName,
                Context.MODE_PRIVATE);
        mEditor = mSP.edit();
    }

    public SharedPreferences.Editor getEditor() {
        return mEditor;
    }

    public  void saveString(String key , String value){
        mEditor.putString(key,value).commit();

    }

    public String getString(String key) {
        return  mSP.getString(key,null);

    }

    public void saveboolean(String key, boolean value) {
        mEditor.putBoolean(key, value).commit();

    }

    public boolean getboolean(String key) {
        return mSP.getBoolean(key,false);

    }




//    public static void saveBoolean(Context context,String key , boolean value){
//        SharedPreferences sp = context.getSharedPreferences(GlobalSharedName, 0);
//        sp.edit().putBoolean(key, value).commit();
//    }
//
//    public static boolean getBoolean(Context context,String key,boolean defValue){
//        SharedPreferences sp = context.getSharedPreferences(GlobalSharedName, 0);
//        return sp.getBoolean(key, defValue);
//
//    }

//
//    public String getIMEI() {
//        return mSP.getString(Users.IMEI, "");
//    }
//
//    public String getNickname() {
//        return mSP.getString(Users.NICKNAME, "");
//    }
//
//    public int getAvatarId() {
//        return mSP.getInt(Users.AVATAR, 0);
//    }
//
//    public String getBirthday() {
//        return mSP.getString(Users.BIRTHDAY, "000000");
//    }
//
//    public int getOnlineStateId() {
//        return mSP.getInt(Users.ONLINESTATEINT, 0);
//    }
//
//    public String getGender() {
//        return mSP.getString(Users.GENDER, "获取失败");
//    }
//
//    public int getAge() {
//        return mSP.getInt(Users.AGE, -1);
//    }
//
//    public String getConstellation() {
//        return mSP.getString(Users.CONSTELLATION, "获取失败");
//    }
//
//    public String getLogintime() {
//        return mSP.getString(Users.LOGINTIME, "获取失败");
//    }
//
//    public void setNickname(String paramNickname) {
//        mEditor.putString(Users.NICKNAME, "");
//    }
//
//    public void setIMEI(String paramIMEI) {
//        mEditor.putString(Users.IMEI, paramIMEI);
//    }
//
//    public void setAvatarId(int paramAvatar) {
//        mEditor.putInt(Users.AVATAR, paramAvatar);
//    }
//
//    public void setBirthday(String paramBirthday) {
//        mEditor.putString(Users.BIRTHDAY, SessionUtils.getBirthday());
//    }
//
//    public void setOnlineStateId(int paramOnlineStateId) {
//        mEditor.putInt(Users.ONLINESTATEINT, paramOnlineStateId);
//    }
//
//    public void setGender(String paramGender) {
//        mEditor.putString(Users.GENDER, paramGender);
//    }
//
//    public void setAge(int paramAge) {
//        mEditor.putInt(Users.AGE, paramAge);
//    }
//
//    public void setConstellation(String paramConstellation) {
//        mEditor.putString(Users.CONSTELLATION, paramConstellation);
//    }
//
//    public void setLogintime(String paramLongtime) {
//        mEditor.putString(Users.LOGINTIME, paramLongtime);
//    }
}
