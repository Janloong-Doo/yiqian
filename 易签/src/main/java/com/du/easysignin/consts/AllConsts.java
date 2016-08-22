package com.du.easysignin.consts;

/**
 * 所有常量类
 * 作者：DragonDu
 * Email: 807110586@qq.com
 */
public class AllConsts {
    public static final String TEALOGINCLASS = "login_class"; //config中班级类型key
    public static final String STULOGIN_INFO = "stulogin_info"; //config中学生登录信息key

    //客户端处理类型
    /**
     * 请求类型
     */
    public static final int STUREGIST_REQUEST = 21; //学生注册请求
    public static final int STUREGIST_REQUEST_FALSE = 210; //学生注册请求失败
    public static final int STUREGIST_REQUEST_TRUE = 211; //学生注册请求成功


    /**
     * 登录类
     */
    public static final int STULOGIN_REQUEST = 22; //学生登录请求
    public static final int STULOGIN_REQUEST_FALSE = 220; //学生登录请求失败
    public static final int STULOGIN_REQUEST_TRUE = 221; //学生登录请求成功
    public static final int STULOGIN_REQUEST_NOCARDE = 222; //学生信息不存在

    /**
     * 签到类
     */
    public static final int STUSIGNIN_REQUEST = 23; //学生签到请求
    public static final int STUSIGNIN_REQUEST_FALSE = 230; //学生签到请求失败，已经签到过
    public static final int STUSIGNIN_REQUEST_TRUE = 231; //学生签到请求成功
    public static final int STUSIGNIN_REQUEST_NOTIFY = 232; //通知学生去签到



    //服务端处理
    public static final int TEA_FOR_SIGNIN = 31; //学生签到后服务器的处理

    /**
     * 文件目录结构类
     */
    public static final String FILE_TEACHER="/yiqian/teacher"; //教师信息的目录结构
    public static final String FILE_CLASS="/yiqian/class"; //班级信息的目录结构
    public static final String FILE_LOG="/yiqian/log"; //学生签到的总目录结构


    /**
     * 学生发送消息给教师
     */
    public static final int WHISPER_FOR_TEA = 41; //学生发送悄悄话


    /**
     * 添加教师信息
     */
    public static final int CREATE_TEA_REGIST = 51; //添加教师信息

    /**
     * 刷新在线人数
     */
    public static final int REFLASH_ONLINE = 61; //刷新在线人数

}
