package com.example.mine.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mine_main, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "MineMainFragment onResume: userBean = " + userBean);
    }
}
