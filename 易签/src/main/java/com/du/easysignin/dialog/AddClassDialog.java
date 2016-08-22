package com.du.easysignin.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.du.easysignin.R;
import com.du.easysignin.consts.WifiApConst;
import com.du.easysignin.utils.WifiUtils;
import com.du.easysignin.view.BaseDialog;

/**
 * 添加班级弹窗
 * 作者：DragonDu
 * Email: 807110586@qq.com
 */
public class AddClassDialog extends BaseDialog {

    private EditText dialog_et_addclass;
    Handler mhandler;

    public AddClassDialog(Context context, Handler handler) {
        super(context);
        mhandler=handler ;
        setDialogContentView(R.layout.activity_add_class);

        initViews();
        initEvents();
    }

    private void initViews() {
        dialog_et_addclass = (EditText) findViewById(R.id.dialog_et_addclass);

    }

    private void initEvents() {

        setButton1(mContext.getString(R.string.btn_add), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                String name = dialog_et_addclass.getText().toString();
                Message message = Message.obtain(mhandler, WifiApConst.ADDCLASS_NAME, name);
                mhandler.sendMessage(message);
                clearInput();
                AddClassDialog.this.cancel();
            }
        });

        setButton2(null,null);
        setButton3(mContext.getString(R.string.btn_cancel), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                clearInput();
                AddClassDialog.this.cancel();

            }
        });


    }

    private void clearInput() {
        this.dialog_et_addclass.setText("");

    }
}