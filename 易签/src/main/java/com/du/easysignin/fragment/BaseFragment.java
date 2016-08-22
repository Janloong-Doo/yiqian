package com.du.easysignin.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * BaseFragment
 * 作者：DragonDu
 * Email: 807110586@qq.com
 */
public abstract class BaseFragment extends Fragment {
    public View parentView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(getLayoutRes(), container, false);
        initView();
        initEvent();
        initData();
        return parentView;
    }

    public abstract int getLayoutRes();

    public abstract void initView();

    public abstract void initData();

    public abstract void initEvent();

    @Override
    public void onDestroy() {
        super.onDestroy();
        parentView = null;
    }


}
