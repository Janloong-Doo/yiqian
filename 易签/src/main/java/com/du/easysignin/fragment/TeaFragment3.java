package com.du.easysignin.fragment;

import android.os.Environment;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.du.easysignin.R;
import com.du.easysignin.consts.AllConsts;
import com.du.easysignin.sql.DbHelper;
import com.du.easysignin.sql.StuTable;
import com.du.easysignin.utils.FileUtils;
import com.du.easysignin.utils.SharePreferenceUtils;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 信息管理页面
 * 作者：DragonDu
 * Email: 807110586@qq.com
 */
public class TeaFragment3 extends BaseFragment implements View.OnClickListener {
    private Button add_db_bt;//添加学生信息
    private EditText et_add_card;//学号编辑
    private EditText et_add_name;//名字编辑
    private DbManager d;//数据库操作对象
    private EditText et_add_pass;//密码编辑
    private Button delete_db_bt;//删除信息按钮
    private Button set_db_bt;//修改信息按钮
    private Button find_db_bt;//查找信息按钮
    private TextView tv_display;//信息展示框
    private String classname;


    @Override
    public int getLayoutRes() {
        return R.layout.teapager3;
    }

    @Override
    public void initView() {

        tv_display = (TextView) parentView.findViewById(R.id.tv_display);
        add_db_bt = (Button) parentView.findViewById(R.id.add_db_bt);
        delete_db_bt = (Button) parentView.findViewById(R.id.delete_db_bt);
        set_db_bt = (Button) parentView.findViewById(R.id.set_db_bt);
        find_db_bt = (Button) parentView.findViewById(R.id.find_db_bt);
        et_add_card = (EditText) parentView.findViewById(R.id.et_add_card);
        et_add_name = (EditText) parentView.findViewById(R.id.et_add_name);
        et_add_pass = (EditText) parentView.findViewById(R.id.et_add_pass);
    }

    @Override
    public void initData() {
        initdb();
        SharePreferenceUtils utils = new SharePreferenceUtils();
        classname = utils.getString(AllConsts.TEALOGINCLASS);
    }

    @Override
    public void initEvent() {
        add_db_bt.setOnClickListener(this);
        delete_db_bt.setOnClickListener(this);
        set_db_bt.setOnClickListener(this);
        find_db_bt.setOnClickListener(this);
    }

    private void initdb() {
//        File file = new File(Environment.getExternalStorageDirectory() + "/yiqian/class");
//        if (!file.exists()) {
//            file.mkdirs();
//            // System.out.println("教师数据库文件目录创建" + file.toString());
//        }
        File classDir = FileUtils.getfilepath(AllConsts.FILE_CLASS);
        SharePreferenceUtils utils = new SharePreferenceUtils();
        String dbname = utils.getString(AllConsts.TEALOGINCLASS);
        dbname = dbname + ".db";
//        File f=new File(file,dbname);
//        if()
        DbManager.DaoConfig config = DbHelper.getDaoconfig(dbname, classDir, 1);
        d = x.getDb(config);


    }

    StuTable byId;//修改信息的id

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_db_bt:
                //添加数据库信息
                String card = et_add_card.getText().toString().trim();
                String name = et_add_name.getText().toString().trim();
                String pas = et_add_pass.getText().toString().trim();
                if (TextUtils.isEmpty(card) || TextUtils.isEmpty(name) || TextUtils.isEmpty(pas)) {
                    Toast.makeText(getActivity(), "输入的内容不能为空", Toast.LENGTH_LONG).show();
                    break;
                }
                if (pas.length() < 8) {
                    Toast.makeText(getActivity(), "输入的密码长度不能小于8位", Toast.LENGTH_LONG).show();


                    et_add_pass.setText("");
                    break;
                }
                StuTable stu = new StuTable();
                stu.setName(name);
                stu.setCard_number(card);
                stu.setPassword(pas);
                stu.setClassName(classname);//设置config中的班级信息
                try {
                    d.save(stu);
                    tv_display.setText("添加成功");
                } catch (DbException e) {
                    Toast.makeText(getActivity(), "学号信息已存在，请重新添加", Toast.LENGTH_LONG).show();
                }
                break;
            //删除信息按钮
            case R.id.delete_db_bt:
                String idcard = et_add_card.getText().toString();
                try {
                    d.deleteById(StuTable.class, idcard);
                } catch (DbException e) {
                    Toast.makeText(getActivity(), "删除失败，学号不存在", Toast.LENGTH_SHORT).show();
                }catch (Exception e) {
                    tv_display.setText("不存在学生信息");
                    Toast.makeText(getActivity(), "删除失败，学号不存在", Toast.LENGTH_SHORT).show();
                }
                break;
            //修改信息按钮,默认无智能修改,先全部修改
            case R.id.set_db_bt:
                String sid = et_add_card.getText().toString();
                String na = et_add_name.getText().toString();
                String pa = et_add_pass.getText().toString();

                try {
                    byId = d.findById(StuTable.class, sid);
                    byId.setName(na);
                    byId.setPassword(pa);

                } catch (DbException e) {
                    Toast.makeText(getActivity(), "未找到该id", Toast.LENGTH_SHORT).show();
                }catch (Exception e) {
                    Toast.makeText(getActivity(), "未找到该id", Toast.LENGTH_SHORT).show();
                    tv_display.setText("不存在学生信息");
                }
                try {
                    d.update(byId, "name", "password");
                } catch (DbException e) {
                    Toast.makeText(getActivity(), "更新数据失败", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "不存在学生信息", Toast.LENGTH_SHORT).show();
                    tv_display.setText("不存在学生信息");

                }

                break;
            //查找信息按钮
            case R.id.find_db_bt:
                tv_display.setText("");
                String asid = et_add_card.getText().toString().trim();
//                System.out.println("find"+asid);
                if (TextUtils.isEmpty(asid)) {
                    //如果学号为空就查找所有信息
                    try {
                        List<StuTable> all = d.findAll(StuTable.class);
//                        tv_display.setText(all.toString());
                        for(StuTable s:all){
                            tv_display.append(s.toString());
                        }
                        tv_display.setMovementMethod(ScrollingMovementMethod.getInstance());
                    } catch (DbException e) {
                        tv_display.setText("不存在学生信息");
                    }catch (Exception e) {
                        tv_display.setText("不存在学生信息");
                    }
                } else {

                    //否则就查找单一的人
                    try {
                        StuTable dById = d.findById(StuTable.class, asid);
//                        System.out.println("find" + dById);
                        if (dById == null) {
                            tv_display.setText("找不到该学号信息");
                        } else {

                            tv_display.setText(dById.toString());
                        }
                    } catch (DbException e) {
                        tv_display.setText("找不到该学号信息");
                        Toast.makeText(getActivity(), "查找不到该学号信息", Toast.LENGTH_SHORT).show();

                    }catch (Exception e) {
                        tv_display.setText("不存在学生信息");
                    }
                }
                break;

        }
    }
}
