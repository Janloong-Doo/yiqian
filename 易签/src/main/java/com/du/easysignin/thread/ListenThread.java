package com.du.easysignin.thread;

import android.net.LocalServerSocket;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.du.easysignin.consts.AllConsts;
import com.du.easysignin.sql.DbHelper;
import com.du.easysignin.sql.StuTable;
import com.du.easysignin.utils.HelpUtils;
import com.du.easysignin.utils.LoginUtils;
import com.du.easysignin.utils.SharePreferenceUtils;
import com.google.gson.Gson;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.net.ServerSocketFactory;
import javax.xml.transform.Source;

/**
 * 监听线程
 * 作者：DragonDu
 * Email: 807110586@qq.com
 */
public class ListenThread extends Thread {
    ServerSocket serverSocket = null;
    Handler mHandler;
    boolean mBool;
    private Socket socket = null;
    boolean exit = true;
    private Thread thread=null;

    public ListenThread(Handler handler, int port, boolean bool) {
        try {
            mBool = bool;
            mHandler = handler;
            if (serverSocket == null) {
                serverSocket = new ServerSocket(port);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void setexit(boolean exit) {
        this.exit = exit;
        try {
            serverSocket.close();
            serverSocket=null;
           if(socket!=null){

            socket.close();
           }
            socket=null;
        } catch (IOException e) {
            e.printStackTrace();
        }
           if(thread!=null){

               try {
                   thread.stop();
               } catch (Exception e) {
                   System.out.println("这里强制报错");
               }
           }

    }

    @Override
    public void run() {

        //是否实现实现多客户端的连接
        if (mBool) {
//            while (true) {
            while (exit) {
                try {
                    Message msg = new Message();
                    //等待消息并接收客户端socket对象

                        socket = serverSocket.accept();

//                    InputStream is = socket.getInputStream();
//                    if(thread==null){
                    Task task = new Task(socket, msg);

                    thread = new Thread(task);
                    thread.start();
//                    }
                } catch (IOException e) {
//                    Log.e("jieshou","出错了");
                    e.printStackTrace();
                }

            }
        } else {
            try {
                Message msg = new Message();
                //等待消息并接收客户端socket对象
                Socket socket = serverSocket.accept();
//                InputStream is = socket.getInputStream();
                if(thread==null) {
                    thread = new Thread(new Task(socket, msg));
                    thread.start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    /**
     * 处理socket请求
     */
    class Task implements Runnable {
        private Socket mSoc;
        private Message mMsg;

        public Task(Socket socket, Message msg) {
            mSoc = socket;
            mMsg = msg;
        }

        @Override
        public void run() {

            try {
                handlScoket();
            } catch (IOException e) {
//                Log.e("jieshou","接受出错了");
                e.printStackTrace();
            }
        }


        private void handlScoket() throws IOException {
            InputStream inputStream = mSoc.getInputStream();
            if (inputStream != null) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader
                        (inputStream, "UTF-8"));
                //读取到的数据
                String str = bufferedReader.readLine();
                System.out.println("主字符串" + str);
                String[] s = str.split(">");
                String recode = s[0];
                String main = s[1];
//                System.out.println("recode" + recode);
//                System.out.println("main" + main);
                //数据处理结果
                int recode_type = dealWithType(mHandler, Integer.parseInt(recode), main);
//                if (mHandler != null) {
//                    mMsg.what = 1;
//                    mMsg.obj = recode_type + "--" + main;
//                    //收到信息后发送handler  显示在屏幕上主要信息
//                    mHandler.sendMessage(mMsg);
//                }
                //依据请求码进行事件处理

//                Gson gson=new Gson();
//                StuTable stuTable = gson.fromJson(main, StuTable.class);
//
//                System.out.println("-----"+stuTable.getName()+stuTable.getPassword());
//                boolean b = LoginUtils.loginCheck(stuTable.getCard_number(), stuTable
// .getPassword());
//                System.out.println("数据库查询密码是否正确"+b);
                PrintWriter pw = new PrintWriter(mSoc.getOutputStream());
                pw.write(recode_type + "");

                //关闭流
                pw.close();
                inputStream.close();
                bufferedReader.close();
                mSoc.close();


            }
        }


        //根据请求类型处理
        public int dealWithType(Handler handler, int type, String main) {
            switch (type) {
                //注册请求
                case AllConsts.STUREGIST_REQUEST:
                    int regist_result = LoginUtils.requestRegist(main);
//                    System.out.println("注册结果情况：" + regist_result);
                    return regist_result;
//                    break;
                //登录事件
                case AllConsts.STULOGIN_REQUEST:
                    Gson gson = new Gson();
                    StuTable stuTable = gson.fromJson(main, StuTable.class);
                    int login_result = LoginUtils.loginCheck(stuTable.getCard_number(), stuTable
                            .getPassword());
//                    System.out.println("数据库查询密码是否正确" + login_result);
                    return login_result;
//                    break;
                //签到事件
                case AllConsts.STUSIGNIN_REQUEST:
                    int info = LoginUtils.dealLoginInfo(main, mMsg);
                    if (mHandler != null) {
                        mMsg.what = AllConsts.TEA_FOR_SIGNIN;
//                        Log.i("listenThread", mMsg.what+"");
//                    mMsg.obj =  ;
                        //收到信息后发送handler  显示在屏幕上主要信息
                        mHandler.sendMessage(mMsg);
                    }
                    return info;
                //学生悄悄话
                case AllConsts.WHISPER_FOR_TEA:
                    mMsg.what = AllConsts.WHISPER_FOR_TEA;
                    mMsg.obj = main;
                    mHandler.sendMessage(mMsg);

                    break;
                default:
                    break;
            }
            return -1;
        }
    }


}
