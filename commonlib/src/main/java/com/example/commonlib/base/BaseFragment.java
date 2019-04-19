package com.example.commonlib.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.support.AndroidSupportInjection;

/**
 * Created by smile on 2019/3/20.
 */

public abstract class BaseFragment extends Fragment {

    /**
     * fragment显示状态标记
     */
    private final String STATE_SAVE_IS_HIDDEN = "STATE_SAVE_IS_HIDDEN";
    /**
     * 当前fragment是否可见
     */
    protected boolean currentFragmentIsVisible;
    private Unbinder unbinder;

    protected abstract int bindLayout();

    protected abstract void initView();

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            // 1、处理fragment重叠的问题
            // 2、解决思路：由Activity/父Fragment来管理子Fragment的Hidden状态 转变为 由Fragment自己来管理自己的Hidden状态！
            // 3、不管多深的嵌套Fragment、同级Fragment等场景，全都可以正常工作，不会发生重叠！
            // 4、加载Fragment时，在Activity的onCreate方法或者Fragment的onCreateView方法中通过findFragmentByTag判断一下要加载的fragment是否为空，只有为空才加载；
            boolean fragmentIsHidden = savedInstanceState.getBoolean(STATE_SAVE_IS_HIDDEN);

            FragmentManager fragmentManager = getFragmentManager();

            FragmentTransaction fragmentTransaction = null;
            if (fragmentManager != null) {
                fragmentTransaction = fragmentManager.beginTransaction();
            }

            if (fragmentTransaction != null) {
                if (fragmentIsHidden) {
                    fragmentTransaction.hide(this);
                } else {
                    fragmentTransaction.show(this);
                }
                fragmentTransaction.commit();
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(bindLayout(), container, false);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATE_SAVE_IS_HIDDEN, isHidden());
    }

    protected void dismissXhProgressDialog(AlertDialog alertDialog) {
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
            alertDialog = null;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
