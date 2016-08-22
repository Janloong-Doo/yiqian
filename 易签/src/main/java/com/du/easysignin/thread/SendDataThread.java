package com.du.easysignin.thread;

import android.os.Handler;
import android.os.Message;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * 发送数据类线程
 * 作者：DragonDu
 * Email: 807110586@qq.com
 */
public class SendDataThread extends Thread {
    Handler handler;
    private String mAddress;
    private int mPort;
    private PrintWriter pw;
    private Socket socket;
    private Object mOb;
    int typeOfRequest;//请求类型

    public SendDataThread(Handler handler, String address, int port, Object ob, int typeOfRequest) {
        mAddress = address;
        mPort = port;
        mOb = ob;
        this.handler = handler;
        this.typeOfRequest = typeOfRequest;
    }


    @Override
    public void run() {
        super.run();

        try {
            //发送到某个ip地址下的某个端口
            socket = new Socket(mAddress, mPort);
            pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream
                    ())), true);
            //发送数据  加入请求类型
            pw.println(typeOfRequest + ">" + mOb);

            //返回的数据
            InputStream inputStream = socket.getInputStream();
            if (inputStream != null) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader
                        (inputStream, "UTF-8"));
                String str = bufferedReader.readLine();
                System.out.println("服务器返回的recode：" + str);
                if (handler != null) {
                    Message msg = Message.obtain();
                    msg.what = Integer.parseInt(str);
                    msg.obj = str;
//                msg.obj=Integer.parseInt(str);
                    handler.sendMessage(msg);
                }
                pw.close();
                inputStream.close();
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
