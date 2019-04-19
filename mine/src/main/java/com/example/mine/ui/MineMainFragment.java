package com.example.mine.ui;


import android.support.v4.app.Fragment;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.commonlib.arouter.MineModuleArouterPath;
import com.example.commonlib.base.BaseFragment;
import com.example.commonlib.bean.UserBean;
import com.example.commonlib.util.MyLog;
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
    protected void onLazyInitEvent() {

    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        MyLog.e(TAG, TAG + "---onSupportVisible()  我的界面可见了");
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        MyLog.e(TAG, TAG + "---onSupportInvisible() 我的界面不可见");
    }
}
