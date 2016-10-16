package com.du.easysignin.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.du.easysignin.R;
import com.du.easysignin.base.BaseAcitivity;
import com.du.easysignin.bean.SignInfo;
import com.du.easysignin.consts.AllConsts;
import com.du.easysignin.sql.StuTable;
import com.du.easysignin.thread.SendDataThread;
import com.du.easysignin.utils.HelpUtils;
import com.du.easysignin.utils.SharePreferenceUtils;
import com.du.easysignin.utils.WifiUtils;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * 学生签到页面
 * 作者：DragonDu
 * Email: 807110586@qq.com
 */
public class StuFragment1 extends BaseFragment implements View.OnClickListener {
    public Button stupager1_signin;
    private Handler mhandler;
    private SignInfo signInfo;

    public StuFragment1() {

    }

    public void setMhandler(Handler handler){
        mhandler=handler;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.stupager1;
    }

    @Override
    public void initView() {
        stupager1_signin = (Button) parentView.findViewById(R.id.stupager1_signin);

    }

    @Override
    public void initData() {
        mhandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case AllConsts.STUSIGNIN_REQUEST_TRUE:

                        if (stupager1_signin.getText().toString().equals("已签到")) {
//                        showShortToast("本节课您已签到过!");
                            Toast.makeText(getActivity(), "本节课您今日已签到过，请勿重复签到", Toast
                                    .LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "签到成功", Toast.LENGTH_SHORT).show();
                            stupager1_signin.setText("已签到");
                            stupager1_signin.setTextColor(Color.RED);
                            //     stupager1_signin.setOnClickListener(null);
                        }
                        break;
                    case AllConsts.STUSIGNIN_REQUEST_FALSE:
                        stupager1_signin.setText("已签到");
                        stupager1_signin.setTextColor(Color.RED);
                        Toast.makeText(getActivity(), "本节课您今日已签到过，请勿重复签到", Toast.LENGTH_SHORT)
                                .show();
                        break;
                    case -1:
                        Toast.makeText(getActivity(), "未知错误", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };

        initSign();
    }

    @Override
    public void initEvent() {
        stupager1_signin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.stupager1_signin:

                //一键签到功能
                if (!stupager1_signin.getText().toString().equals("已签到")) {
//                    SharePreferenceUtils utils =new SharePreferenceUtils();
//                    String loginname = utils.getString(AllConsts.STULOGIN_INFO);
//                    //签到信息初始化
//                    SignInfo signInfo = new SignInfo();
//                    signInfo.setCard_name(loginname);//初始化学号
                    signInfo.setSign_time(HelpUtils.getDate(HelpUtils.FORMATTIMESTR).toString());
                    //日期的设置
//                    System.out.println("stufg1__日期格式化:"+signInfo.getSign_time());

                    //转为json字符串
                    Gson g = new Gson();
                    String signjson = g.toJson(signInfo, SignInfo.class);

//                    String s = "哈哈 我来签到啦   么么哒……";
                    new SendDataThread(mhandler, WifiUtils.getServerIPAddress(), 12345, signjson,
                            AllConsts.STUSIGNIN_REQUEST).start();

                } else {
                    Toast.makeText(getActivity(), "本节课您已签到过", Toast.LENGTH_SHORT).show();

                }
                break;

        }
    }

    /**
     * 初始化签到信息
     */
    public void initSign() {
        SharePreferenceUtils utils = new SharePreferenceUtils();
        String loginname = utils.getString(AllConsts.STULOGIN_INFO);
        //签到信息初始化
        signInfo = new SignInfo();
        signInfo.setCard_name(loginname);//初始化学号
        signInfo.setPersonname("");
        signInfo.setIp_address(WifiUtils.getLocalIPAddress());
        signInfo.setmMac(WifiUtils.getMacAddress());
    }


}
