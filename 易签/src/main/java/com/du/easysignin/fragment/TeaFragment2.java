package com.du.easysignin.fragment;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.du.easysignin.R;
import com.du.easysignin.bean.SignInfo;
import com.du.easysignin.consts.AllConsts;
import com.du.easysignin.service.LocalService;
import com.du.easysignin.thread.SendDataThread;
import com.du.easysignin.utils.FileUtils;
import com.du.easysignin.utils.LoginUtils;
import com.du.easysignin.utils.WifiUtils;

import java.io.File;
import java.util.ArrayList;

/**
 * 签到查询页面
 * 作者：DragonDu
 * Email: 807110586@qq.com
 */
public class TeaFragment2 extends BaseFragment {
    private Spinner sp_log_tea;//班级下拉框
    ArrayList<String> spname = new ArrayList<>();//班级名字
    ArrayList<String> logname = new ArrayList<>();//日志名字
    private ArrayAdapter<String> adapter;
    private Spinner sp_log_tea_day;//日志下拉框
    private ArrayAdapter<String> adapter1;
    private TextView lv_showLog;
    public  static TeaFragment2 tea2;
//    public void setService(LocalService service) {
//        this.myservice = service;
//    }

    public TeaFragment2() {
        tea2=this;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.teapager2;
    }

    @Override
    public void initView() {
        lv_showLog = (TextView) parentView.findViewById(R.id.lv_showLog);
        sp_log_tea = (Spinner) parentView.findViewById(R.id.sp_log_tea);
        sp_log_tea_day = (Spinner) parentView.findViewById(R.id.sp_log_tea_day);
    }

    @Override
    public void initData() {
        getNameOfClass();
        showLogSign();
//        getNameOfLog(0);
    }
//        handler = new Handler() {
//            @Override
//            public void handleMessage(Message msg) {
//                super.handleMessage(msg);
//                switch (msg.what) {
//
//                    case 1:
//                        Toast.makeText(getActivity(), msg.obj.toString(), Toast.LENGTH_LONG)
// .show();
//                        break;
//                }
//            }
//        };


    @Override
    public void initEvent() {

//        adapter = new ArrayAdapter<>(getActivity(), R.layout
//                .simple_spinner, spname);
//        adapter1 = new ArrayAdapter<>(getActivity(), R.layout
//                .simple_spinner, logname);
        adapter = new ArrayAdapter<>(getActivity(),R.layout.simple_spinner, spname);
        adapter1 = new ArrayAdapter<>(getActivity(), R.layout.simple_spinner, logname);
        sp_log_tea.setAdapter(adapter);
        sp_log_tea.setOnItemSelectedListener(selectlistener);
//        sp_log_tea_day.setAdapter(adapter1);

//sp_log_tea.setPrompt();
        sp_log_tea_day.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                showLogSign();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public AdapterView.OnItemSelectedListener selectlistener = new AdapterView
            .OnItemSelectedListener() {


        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            logname.clear();
            getNameOfLog(position);
            lv_showLog.setText("");
            adapter1 = new ArrayAdapter<String>(getActivity(),R.layout.simple_spinner, logname);
            sp_log_tea_day.setAdapter(adapter1);

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.teapager2_sendsocket:
//                String[] fileName = FileUtils.getFileDirName(FileUtils.getfilepath(AllConsts
//                        .FILE_LOG));
////                for(int i =0;i<fileName.length;i++){
////
////                    Log.e("dirlength", String.valueOf(fileName.length));
////                    Log.e("teafragment",fileName[i]);
////                    File[] logName = FileUtils.getFileName(FileUtils.getfilepath(AllConsts
////                            .FILE_LOG + "/" + fileName[i]));
////                    for(int j=0;j<logName.length;j++){
////
////                        Log.e("----", String.valueOf(logName.length));
////                        Log.e("----",logName[j].getName());
////                        Log.e("----","--------");
////                    }
////                }
//                    spname.clear();
//                for (int i = 0; i < fileName.length; i++) {
//                    spname.add(fileName[i]);
//                    Log.e("dirlength", String.valueOf(fileName.length));
//                    Log.e("teafragment", fileName[i]);
//
////                    String[] logName = FileUtils.getFileDirName(FileUtils.getfilepath(AllConsts
////                            .FILE_LOG + "/" + fileName[i]));
////                    for (int j = 0; j < logName.length; j++) {
////                        if (logName[j].endsWith(".db")) {
////                            Log.e("----", String.valueOf(logName.length));
////                            Log.e("----", logName[j]);
////                            Log.e("----", "--------");
////                        }
////                    }
//                }
//                adapter.notifyDataSetChanged();
//
//
//                break;
//        }
//    }


    /**
     * 初始化班级信息
     */
    public void getNameOfClass() {
        //计划用数据库中的班级信息来初始化
//        spname.add("杜江龙大队1");
//        spname.add("杜江龙大队2");
//        spname.add("杜江龙大队3");
//        spname.add("杜江龙大队4");
//
//        adapter.notifyDataSetChanged();
        String[] fileName = FileUtils.getFileDirName(FileUtils.getfilepath(AllConsts
                .FILE_LOG));
        spname.clear();
        for (int i = 0; i < fileName.length; i++) {
            spname.add(fileName[i]);
//            Log.e("dirlength", String.valueOf(fileName.length));
//            Log.e("teafragment", fileName[i]);
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * 初始化日志信息
     */
    public void getNameOfLog(int position) {
//        //计划用数据库中的班级信息来初始化
//        logname.add("日志" + i);
//        logname.add("日志" + i);
//        logname.add("日志" + i);
//        logname.add("日志" + i);
        logname.clear();
        String clname = (String) sp_log_tea.getItemAtPosition(position);
        String[] logName = FileUtils.getFileDirName(FileUtils.getfilepath(AllConsts
                .FILE_LOG + "/" + clname));
//        logname.clear();
        for (int j = 0; j < logName.length; j++) {
            if (logName[j].endsWith(".db")) {
                logname.add(logName[j]);
//                Log.e("----", String.valueOf(logName.length));
//                Log.e("----", logName[j]);
//                Log.e("----", "--------");
            }
        }
        adapter1.notifyDataSetChanged();
    }

    /**
     * 页面显示签到日志
     */
    public void showLogSign() {
        String showclass = (String) sp_log_tea.getSelectedItem();
        String showdata = (String) sp_log_tea_day.getSelectedItem();
        lv_showLog.setText("");
        if (!TextUtils.isEmpty(showclass) &&!TextUtils.isEmpty(showdata)) {
            ArrayList<SignInfo> signInfos = LoginUtils.showLog(showclass, showdata);
           if(signInfos!=null){


//            lv_showLog.setText(signInfos.toString());
               for(int i=0;i<signInfos.size();i++){
                   lv_showLog.append(signInfos.get(i).toString());
               }
               lv_showLog.setMovementMethod(ScrollingMovementMethod.getInstance());
           }
        }
    }
}
