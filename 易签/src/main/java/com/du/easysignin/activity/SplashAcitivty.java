package com.du.easysignin.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.du.easysignin.R;
import com.du.easysignin.base.BaseAcitivity;
import com.shelwee.update.UpdateHelper;


/**
 * 欢迎页面
 * 作者：DragonDu
 * Email: 807110586@qq.com
 */
public class SplashAcitivty extends BaseAcitivity implements View.OnClickListener {

    private Button welcome_btn_login_stu;
    private Button welcome_btn_login_tea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_acitivty);

        initUI();
        checkUpdate();
    }

    private void checkUpdate() {
        String url = null;
        if (url != null) {
            UpdateHelper updateHelper = new UpdateHelper.Builder(this)
                    .checkUrl(url)
                    .isAutoInstall(false)
                    .build();
            updateHelper.check();
        }else {
            Toast.makeText(this,"服务器地址不存在",Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * gao kang edit to look github
     */
    public void initUI() {
        welcome_btn_login_stu = (Button) findViewById(R.id.welcome_btn_login_stu);
        welcome_btn_login_tea = (Button) findViewById(R.id.welcome_btn_login_tea);
        welcome_btn_login_tea.setOnClickListener(this);
        welcome_btn_login_stu.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.welcome_btn_login_stu:
                //学生登录入口
                startActivity(StuOpenActivity.class);
                finish();
                break;
            case R.id.welcome_btn_login_tea:
                //教师登录入口
//                Intent intent=new Intent(this,TeaLoginActivity.class);
//                startActivity(intent);
                startActivity(TeaOpenActivity.class);
                finish();
                break;
        }
    }
}
