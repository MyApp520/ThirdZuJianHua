package com.example.commonlib.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import dagger.android.AndroidInjection;

/**
 * Created by smile on 2019/3/20.
 */

public class BaseActivity extends FragmentActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        // dagger-android注入
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
    }
}
