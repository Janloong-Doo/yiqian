package com.du.easysignin.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.du.easysignin.R;
import com.du.easysignin.base.BaseAcitivity;
import com.du.easysignin.consts.AllConsts;
import com.du.easysignin.consts.WifiApConst;
import com.du.easysignin.dialog.AddClassDialog;
import com.du.easysignin.dialog.Login_crateclassDialog;
import com.du.easysignin.sql.DbHelper;
import com.du.easysignin.sql.StuTable;
import com.du.easysignin.utils.FileUtils;
import com.du.easysignin.utils.SharePreferenceUtils;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;

/**
 * 教师登录页面
 * 作者：DragonDu
 * Email: 807110586@qq.com
 */
public class TeaLoginAcitivity extends BaseAcitivity implements View.OnClickListener {

    private EditText et_tea_pass;
    private EditText et_tea_name;
    private Button bt_add_class;
    private Button tea_bt_login;
    private Spinner sp_class;
    ArrayList<String> spname = new ArrayList<>();
    private AddClassDialog addClassDialog;
    private Mhandler mhandler;
    private String name;//添加的班级的名字
    private ArrayAdapter<String> adapter;
    private String login_pass;//教师名字
    private String login_name;//教师密码
    private String dbname;
    private SharePreferenceUtils sp;
    private File teaDir;
    private Login_crateclassDialog login_crateclassDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tea_denglu_acitivity);
        initUI();
        initEvent();
        initData();
    }

    public static void actionStart(Activity activity) {
        Intent intent = new Intent(activity, TeaLoginAcitivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void initUI() {
        et_tea_pass = (EditText) findViewById(R.id.et_tea_pass);
        et_tea_name = (EditText) findViewById(R.id.et_tea_name);
        bt_add_class = (Button) findViewById(R.id.bt_add_class);
        tea_bt_login = (Button) findViewById(R.id.tea_bt_login);
        sp_class = (Spinner) findViewById(R.id.sp_class);


    }

    /**
     * 初始化事件
     */
    @Override
    public void initEvent() {
        bt_add_class.setOnClickListener(this);
        tea_bt_login.setOnClickListener(this);
        adapter = new ArrayAdapter<String>(this, R.layout.simple_spinner,
                spname);

        mhandler = new Mhandler();
        sp_class.setAdapter(adapter);
        addClassDialog = new AddClassDialog(this, mhandler);
        login_crateclassDialog = new Login_crateclassDialog(this, mhandler);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        //初始化班级信息
        getNameOfClass();
    }

    /**
     * 页面点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_add_class:
                //添加新班级
                addClassDialog.setTitle("添加新的班级：");
                addClassDialog.show();


                break;
            case R.id.tea_bt_login:
                //登录按钮

                //开发便利自动填充用户名密码
//                et_tea_name.setText("杜江龙");
//                et_tea_pass.setText("123456789");

                //  getNameAndPass();
                //获得名字和密码
                login_pass = et_tea_pass.getText().toString().trim();
                login_name = et_tea_name.getText().toString().trim();
                if (TextUtils.isEmpty(login_name) || TextUtils.isEmpty(login_pass)) {
                    showShortToast("输入的内容不能为空");
                    break;
                }
                if (login_pass.length() < 8) {
                    showShortToast("输入的密码长度不能小于8位");
                    et_tea_pass.setText("");
                    break;
                }
                //获取下拉框的选中数据,即班级信息
                String first = (String) sp_class.getSelectedItem();

                // System.out.println("first"+first);
                if (TextUtils.isEmpty(first)) {
                    showShortToast("请添加一个新班级");
                    break;
                }
                //配置文件中设置登录班级
                sp = new SharePreferenceUtils();
                sp.saveString(AllConsts.TEALOGINCLASS, first);
                //登录相关的数据库处理等逻辑
                if (!login()) {
                    break;
                }
                //登录成功进入主界面
//                startActivity(TeaHomeActivity.class);
                TeaHomeActivity1.actionStart(this);
                finish();
                break;

        }
    }

    /**
     * 登录到主页面
     */
    private boolean login() {
        //创建数据库
        // System.out.println(login_name);

        // File file=new File("/sdcard");
        //将文件存储在sd卡根目录的"yiqian"目录下
//        file = new File(Environment.getExternalStorageDirectory() + "/yiqian");

        teaDir = FileUtils.getfilepath(AllConsts.FILE_TEACHER);
        //判断文件目录是否存在
//        if (!file.exists()) {
//            file.mkdirs();
//            // System.out.println("教师数据库文件目录创建" + file.toString());
//        }
        // System.out.println(file.toString());

        dbname = login_name + ".db";
        File f = new File(teaDir, dbname);
        //判断对应老师名字的数据库文件是否存在
//        DbManager.DaoConfig daoconfig = DbHelper.getDaoconfig(dbname, file, 1);
//        DbManager db = x.getDb(daoconfig);
        if (f.exists()) {
            System.out.println("数据库文件存在");
            //数据库文件存在时判断密码是否正确 然后可以进行登录
            try {
                DbManager.DaoConfig config = DbHelper.getDaoconfig(dbname, teaDir, 1);
                DbManager d = x.getDb(config);

                StuTable first = d.findFirst(StuTable.class);
                if (!first.getPassword().equals(login_pass)) {
                    et_tea_pass.setText("");
                    showLongToast("您输入的密码不正确!");
                    return false;
                } else {
                    showLongToast("恭喜您登录成功!");
                    return true;
                }

            } catch (DbException e) {
                e.printStackTrace();
            }

        } else {
//            System.out.println("数据库不存在");
//            //初次使用或用户名不存在是否需要创建该新用户
//            showAlertDialog("创建新用户", "用户名尚不存在，是否需要建立新用户？", "确定", new DialogInterface
//                    .OnClickListener() {
//                //确定按钮的点击
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    DbManager.DaoConfig daoconfig = DbHelper.getDaoconfig(dbname, teaDir, 1);
//                    DbManager db = x.getDb(daoconfig);
//                    try {
////                        String classname = sp.getString(AllConsts.TEALOGINCLASS);
//                        //输入教师相关信息
//                        StuTable stuTable = new StuTable();
//                        stuTable.setCard_number("0");
//                        stuTable.setName(login_name);
//                        stuTable.setPassword(login_pass);
//                        stuTable.setIsAdmin(1);
//                        stuTable.setClassName("管理者");
//                        db.save(stuTable);
//                        showLongToast("初次登陆信息初始化完成!");
//                        //进入主界面
////                        startActivity(TeaHomeActivity.class);
//                        TeaHomeActivity1.actionStart(TeaLoginAcitivity.this);
//                        finish();
//                    } catch (DbException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }, "取消", new DialogInterface.OnClickListener() {
//                //取消按钮的点击
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.dismiss();
//
//                }
//            });
            login_crateclassDialog.setTitle("创建新用户：");
            login_crateclassDialog.show();
        }
        return false;
    }


    class Mhandler extends Handler {


        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WifiApConst.ADDCLASS_NAME:

                    name = (String) msg.obj;
                    spname.add(name);
                    adapter.notifyDataSetChanged();
                    break;

                case AllConsts.CREATE_TEA_REGIST:
                    DbManager.DaoConfig daoconfig = DbHelper.getDaoconfig(dbname, teaDir, 1);
                    DbManager db = x.getDb(daoconfig);
                    try {
//                        String classname = sp.getString(AllConsts.TEALOGINCLASS);
                        //输入教师相关信息
                        StuTable stuTable = new StuTable();
                        stuTable.setCard_number("0");
                        stuTable.setName(login_name);
                        stuTable.setPassword(login_pass);
                        stuTable.setIsAdmin(1);
                        stuTable.setClassName("管理者");
                        db.save(stuTable);
                        showLongToast("初次登陆信息初始化完成!");
                        //进入主界面
//                        startActivity(TeaHomeActivity.class);
                        TeaHomeActivity1.actionStart(TeaLoginAcitivity.this);
                        finish();
                    } catch (DbException e) {
                        e.printStackTrace();
                    }

                    break;
            }

        }
    }

    /**
     * 初始化班级信息
     */
    public void getNameOfClass() {
//        //计划用数据库中的班级信息来初始化
//        spname.add("杜江龙大队1");
//        spname.add("杜江龙大队2");
//        spname.add("杜江龙大队3");
//        spname.add("杜江龙大队4");
//
//        adapter.notifyDataSetChanged();
        String[] fileName = FileUtils.getFileDirName(FileUtils.getfilepath(AllConsts
                .FILE_LOG));
        spname.clear();
        if (fileName != null) {
            for (int i = 0; i < fileName.length; i++) {
                spname.add(fileName[i]);
//            Log.e("dirlength", String.valueOf(fileName.length));
//            Log.e("teafragment", fileName[i]);
            }
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 获得教师名字和密码
     */
    private void getNameAndPass() {
        login_pass = et_tea_pass.getText().toString().trim();
        login_name = et_tea_name.getText().toString().trim();
        if (TextUtils.isEmpty(login_name) || TextUtils.isEmpty(login_pass)) {
            showShortToast("输入的内容不能为空");
            return;
        }
        if (login_pass.length() < 8) {
            showShortToast("输入的密码长度不能小于8位");
            return;
        }
    }
}
