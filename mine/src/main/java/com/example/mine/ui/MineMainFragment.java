package com.example.mine.ui;


import android.support.v4.app.Fragment;
import android.util.Log;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.commonlib.arouter.MineModuleArouterPath;
import com.example.commonlib.base.BaseFragment;
import com.example.commonlib.bean.UserBean;
import com.example.mine.R;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 */
@Route(path = MineModuleArouterPath.MINE_MAIN_FRAGMENT)
public class MineMainFragment extends BaseFragment {


    private final String TAG = getClass().getSimpleName();

    @Inject
    UserBean userBean;

    @Override
    protected int bindLayout() {
        return R.layout.fragment_mine_main;
    }

    @Override
    protected void initView() {

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "MineMainFragment onResume: userBean = " + userBean);
    }
}
