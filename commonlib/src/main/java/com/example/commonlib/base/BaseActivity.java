package com.example.commonlib.base;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.example.commonlib.util.ShowToast;
import com.tbruyelle.rxpermissions2.RxPermissions;

import dagger.android.AndroidInjection;
import io.reactivex.functions.Consumer;

/**
 * Created by smile on 2019/3/20.
 */

public class BaseActivity extends FragmentActivity {

    private String[] permissionArray = new String[] {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_PHONE_STATE
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        // dagger-android注入
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        initCheckPermission();
    }

    private void initCheckPermission() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(permissionArray).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                if (aBoolean) {
                    ShowToast.showToast(getApplicationContext(), "权限申请成功");
                } else {
                    ShowToast.showToast(getApplicationContext(), "权限申请失败了");
                }
            }
        });
    }
}
