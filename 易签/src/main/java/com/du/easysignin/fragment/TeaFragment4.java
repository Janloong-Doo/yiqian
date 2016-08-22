package com.du.easysignin.fragment;

import android.view.View;
import android.widget.Button;

import com.du.easysignin.R;
import com.du.easysignin.activity.AboutSoft;
import com.du.easysignin.activity.TeaLoginAcitivity;

/**
 * 教师设置页面
 * 作者：DragonDu
 * Email: 807110586@qq.com
 */
public class TeaFragment4 extends BaseFragment implements View.OnClickListener {

    private Button bt_zhuxiao;
    private Button exit_bt;
    private Button bt_aboutsoft;

    @Override
    public int getLayoutRes() {
        return  R.layout.fragment_tea4;
    }

    @Override
    public void initView() {
        exit_bt = (Button) parentView.findViewById(R.id.exit_bt);
        bt_zhuxiao = (Button) parentView.findViewById(R.id.bt_zhuxiao);
        bt_aboutsoft = (Button) parentView.findViewById(R.id.bt_aboutsoft);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {
        exit_bt.setOnClickListener(this);
        bt_zhuxiao.setOnClickListener(this);
        bt_aboutsoft.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_zhuxiao:
                TeaFragment1.list.clear();
                getActivity().finish();
                TeaLoginAcitivity.actionStart(getActivity());
                break;
            case R.id.exit_bt:
                getActivity().finish();
                break;
            case R.id.bt_aboutsoft:
                AboutSoft.actionStart(getActivity());
                break;
        }
    }
}
