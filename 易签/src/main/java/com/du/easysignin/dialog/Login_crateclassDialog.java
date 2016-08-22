package com.du.easysignin.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.widget.EditText;
import android.widget.TextView;

import com.du.easysignin.R;
import com.du.easysignin.activity.TeaHomeActivity1;
import com.du.easysignin.activity.TeaLoginAcitivity;
import com.du.easysignin.consts.AllConsts;
import com.du.easysignin.consts.WifiApConst;
import com.du.easysignin.sql.DbHelper;
import com.du.easysignin.sql.StuTable;
import com.du.easysignin.view.BaseDialog;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.io.File;

/**
 * 提示创建教师信息弹窗
 * 作者：DragonDu
 * Email: 807110586@qq.com
 */
public class Login_crateclassDialog extends BaseDialog {

    Handler mhandler;
    private TextView dialog_tv_addclass;

    public Login_crateclassDialog(Context context, Handler handler) {
        super(context);
        mhandler=handler ;
        setDialogContentView(R.layout.activity_creat_class);

        initViews();
        initEvents();
    }

    private void initViews() {
        dialog_tv_addclass = (TextView) findViewById(R.id.dialog_tv_addclass);

    }

    private void initEvents() {

        setButton1("确定", new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
//
//                String name = dialog_tv_addclass.getText().toString();
                Message message = Message.obtain(mhandler, AllConsts.CREATE_TEA_REGIST);
                mhandler.sendMessage(message);
//                clearInput();
                Login_crateclassDialog.this.cancel();


            }
        });

        setButton2(null,null);
        setButton3("取消", new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                Login_crateclassDialog.this.cancel();

            }
        });


    }

    private void clearInput() {
        this.dialog_tv_addclass.setText("");

    }
}