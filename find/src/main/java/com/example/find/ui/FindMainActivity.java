package com.example.find.ui;

import android.os.Bundle;
import android.util.Log;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.commonlib.arouter.FindModuleArouterPath;
import com.example.commonlib.base.BaseActivity;
import com.example.commonlib.bean.UserBean;
import com.example.find.R;

import javax.inject.Inject;

@Route(path = FindModuleArouterPath.FIND_MAIN_ACTIVITY)
public class FindMainActivity extends BaseActivity {

    private final String TAG = getClass().getSimpleName();

    @Inject
    UserBean userBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "FindMainActivity onResume: userBean = " + userBean);
    }
}
