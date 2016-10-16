package com.du.easysignin.base;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.du.easysignin.activity.StuHomeActivity;
import com.du.easysignin.activity.StuLoginActivity;
import com.du.easysignin.activity.StuRegistActivity;
import com.du.easysignin.bean.SignInfo;
import com.du.easysignin.consts.AllConsts;
import com.du.easysignin.fragment.TeaFragment1;
import com.du.easysignin.fragment.TeaFragment2;
import com.umeng.analytics.MobclickAgent;

/**
 * BaseActivity
 * 作者：DragonDu
 * Email: 807110586@qq.com
 */
public abstract class BaseAcitivity extends FragmentActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Toast.makeText(BaseAcitivity.this, msg.obj.toString(), Toast.LENGTH_SHORT)
                            .show();
                    break;
                case 2:
                    Toast.makeText(BaseAcitivity.this, msg.obj.toString(), Toast.LENGTH_SHORT)
                            .show();
                    System.out.println(msg.obj);

                    break;
                /**
                 *以下为服务端返回客户端的处理
                 */

                //注册失败
                case AllConsts.STUREGIST_REQUEST_FALSE:
                    showShortToast("注册失败,用户已存在");
                    break;
                //注册成功
                case AllConsts.STUREGIST_REQUEST_TRUE:
                    showShortToast("注册成功");
                    StuRegistActivity.instance.finish();
                    break;

                //登录成功
                case AllConsts.STULOGIN_REQUEST_TRUE:
                    Toast.makeText(BaseAcitivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    startActivity(StuHomeActivity.class);
                    StuLoginActivity.instance2.finish();
                    break;
                //登录失败
                case AllConsts.STULOGIN_REQUEST_FALSE:
                    showShortToast("登录失败");
                    break;
                //用户名不存在
                case AllConsts.STULOGIN_REQUEST_NOCARDE:
                    showShortToast("用户不存在");
                    break;

                //签到成功
                case AllConsts.STUSIGNIN_REQUEST:
                   //学生端的处理办法
                    showLongToast("签到成功");
                    System.out.println("为" + msg.obj);
//                    if (stupager1_signin.getText().toString().equals("已签到")) {
//                        showShortToast("本节课您已签到过!");
//                    } else {
//                        stupager1_signin.setText("已签到");
//                    }


                    break;


                /**
                 * 以下为服务端对应的处理方法
                 */
                //教师在收到签到处理信息后
                case AllConsts.TEA_FOR_SIGNIN:
//                    System.out.println("服务器得到的数据" + msg.obj);
                   if(msg.obj!=null) {
                       TeaFragment1.list.add((SignInfo) msg.obj);
                       TeaFragment1.adapter.notifyDataSetChanged();
                       TeaFragment2.tea2.getNameOfLog(0);
                       TeaFragment2.tea2.showLogSign();

                       TeaFragment1.getteafragmentInstance().setTv_signin_number();

                   }
                    break;
                case AllConsts.WHISPER_FOR_TEA:
                    //教师对悄悄话的处理
                    String whisper= (String) msg.obj;
                    TeaFragment1.getteafragmentInstance().setwhisper(whisper);
                    break;

                case AllConsts.REFLASH_ONLINE:
                TeaFragment1.getteafragmentInstance().setTv_online_number();
                    break;
                case -1:
                    showShortToast("未知错误");
                    break;
                default:
                    showLongToast("无对应处理请求");
                    break;
            }
        }
    };

    /**
     * 初始化布局
     */
    public abstract void initUI();

    /**
     * 初始化控件
     */
    public void initEvent() {
    }


    /**
     * 短暂显示Toast提示(来自res)
     **/
    protected void showShortToast(int resId) {
        Toast.makeText(this, getString(resId), Toast.LENGTH_SHORT).show();
    }

    /**
     * 短暂显示Toast提示(来自String)
     **/
    protected void showShortToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    /**
     * 长时间显示Toast提示(来自res)
     **/
    protected void showLongToast(int resId) {
        Toast.makeText(this, getString(resId), Toast.LENGTH_LONG).show();
    }

    /**
     * 长时间显示Toast提示(来自String)
     **/
    protected void showLongToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }


    /**
     * 含有标题和内容的对话框
     **/
    protected AlertDialog showAlertDialog(String title, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).setTitle(title).setMessage(message)
                .show();
        return alertDialog;
    }

    /**
     * 含有标题、内容、两个按钮的对话框
     **/
    protected AlertDialog showAlertDialog(String title, String message, String positiveText,
                                          DialogInterface.OnClickListener
                                                  onPositiveClickListener, String negativeText,
                                          DialogInterface.OnClickListener onNegativeClickListener) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).setTitle(title).setMessage(message)
                .setPositiveButton(positiveText, onPositiveClickListener)
                .setNegativeButton(negativeText, onNegativeClickListener).show();
        return alertDialog;
    }


    /**
     * 通过Class跳转界面
     **/
    protected void startActivity(Class<?> cls) {
        startActivity(cls, null);
    }

    /**
     * 含有Bundle通过Class跳转界面
     **/
    protected void startActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
