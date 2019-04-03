package com.example.commonlib.base;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;
import com.example.commonlib.BuildConfig;
import com.example.commonlib.dagger.component.BaseComponent;
import com.example.commonlib.dagger.component.DaggerBaseComponent;
import com.example.commonlib.dagger.module.BaseModule;
import com.example.commonlib.util.XhCrashHandler;

/**
 * Created by smile on 2019/3/19.
 */

public class BaseApplication extends Application {

    private static BaseComponent baseComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        baseComponent = DaggerBaseComponent.builder().baseModule(new BaseModule(getApplicationContext())).build();
        XhCrashHandler.getsInstance().init(this);
        initARouter();
    }

    private void initARouter() {
        if (BuildConfig.DEBUG) {   // 这两行必须写在init之前，否则这些配置在init过程中将无效
            ARouter.openLog();     // 打印日志
            ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(this);
    }

    public static BaseComponent getBaseComponent() {
        return baseComponent;
    }
}
