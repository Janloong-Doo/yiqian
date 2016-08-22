package com.du.easysignin.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;

import com.du.easysignin.R;

public class AboutSoft extends Activity {

    private TextView tv_about;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_soft);
        initView();
        initData();
    }

    private void initView() {
        tv_about = (TextView) findViewById(R.id.tv_about);
    }

    private void initData() {
        String about_soft = "        此软件为自己的毕业设计，也是自己自学安卓开发的第一个实战项目，" +
                "毕业题目是自己定的，初衷只是想做个与学校结合比较紧密的软件，考虑到自己所经历过的大学生活，" +
                "所以就做了这样一个签到软件。软件本身还有许多可以扩展的地方，也有许多不足的地方，" +
                "希望这款简易的软件能为大家带来一定的帮助。四年的大学生活，感谢所有帮助过我，" +
                "以及我帮助过的人，真的学到了很多。\n" +
                "        如果有需要，会在以后不断的改进。源码之后会分享在GitHub上。\n" +
                "GitHub:  DragonDu\n" +
                "Email:  807110586@qq.com";


        tv_about.setText(about_soft);

    }

    public static void actionStart(Activity activity) {
        Intent intent = new Intent(activity, AboutSoft.class);
        activity.startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return false;
    }
}
