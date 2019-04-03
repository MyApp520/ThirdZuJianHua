package com.example.commonlib.arouter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.alibaba.android.arouter.launcher.ARouter;

/**
 * Created by smile on 2019/3/15.
 */

public class ArouterActionManager {

    private static ArouterActionManager arouterActionManager;

    public static ArouterActionManager getInstance() {
        if (arouterActionManager == null) {
            synchronized (ArouterActionManager.class) {
                if (arouterActionManager == null) {
                    arouterActionManager = new ArouterActionManager();
                }
            }
        }
        return arouterActionManager;
    }

    public void startActivity(String arouterPath) {
        if (TextUtils.isEmpty(arouterPath)) {
           return;
        }
        ARouter.getInstance().build(arouterPath).navigation();
    }

    public void startActivity(String arouterPath, Bundle bundle) {
        if (TextUtils.isEmpty(arouterPath)) {
            return;
        }
        ARouter.getInstance().build(arouterPath).with(bundle).navigation();
    }

    public Fragment getFragmentInstance(String arouterPath) {
        return (Fragment) ARouter.getInstance().build(arouterPath).navigation();
    }
}
