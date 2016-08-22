package com.du.easysignin.fragment;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.du.easysignin.R;
import com.du.easysignin.consts.AllConsts;
import com.du.easysignin.thread.ListenThread;
import com.du.easysignin.thread.SendDataThread;
import com.du.easysignin.utils.HelpUtils;
import com.du.easysignin.utils.WifiUtils;

import java.util.ArrayList;

/**
 * 学生提建议页面
 * 作者：DragonDu
 * Email: 807110586@qq.com
 */
public class StuFragment2 extends BaseFragment implements View.OnClickListener {
    private Button stupager2_sendsocket;
    private Handler handler;
    private EditText ed_whisper;
    private TextView tv_sendwhisper;

    @Override
    public int getLayoutRes() {
        return R.layout.stupager2;
    }

    @Override
    public void initView() {
        stupager2_sendsocket = (Button) parentView.findViewById(R.id.stupager2_sendsocket);
        ed_whisper = (EditText) parentView.findViewById(R.id.ed_whisper);
        tv_sendwhisper = (TextView) parentView.findViewById(R.id.tv_sendwhisper);


    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {
        stupager2_sendsocket.setOnClickListener(this);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                switch (msg.what) {

                    case 1:
                        Toast.makeText(getActivity(), msg.obj.toString(), Toast.LENGTH_LONG).show();
                        break;
                }
            }
        };

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //发送悄悄话
            case R.id.stupager2_sendsocket:
                String whisper = ed_whisper.getText().toString();
                if(TextUtils.isEmpty(whisper)){
                    Toast.makeText(getActivity(),"请输入悄悄话内容",Toast.LENGTH_SHORT).show();
                    return;
                }

                ArrayList<String> connectedIP = WifiUtils.getConnectedIP();
                for (String ip : connectedIP) {
                    if (ip.contains(".")) {
                        System.out.println("ip" + ip);

                        new SendDataThread(handler, ip, 12345, whisper, AllConsts
                                .WHISPER_FOR_TEA).start();
                    }
                }
                ed_whisper.setText("");
                ed_whisper.setHint("请输入悄悄话：");
                putwhisper(whisper);
                break;
        }
    }

    public  void putwhisper(String whisper){
        tv_sendwhisper.append(HelpUtils.getDate(HelpUtils.FORMATTIMESTR+"\n"));
        tv_sendwhisper.append("我说 ：  "+whisper+"\n");
    }
}
