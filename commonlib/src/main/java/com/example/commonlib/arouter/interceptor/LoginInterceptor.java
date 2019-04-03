package com.example.commonlib.arouter.interceptor;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Interceptor;
import com.alibaba.android.arouter.facade.callback.InterceptorCallback;
import com.alibaba.android.arouter.facade.template.IInterceptor;
import com.example.commonlib.arouter.ChatModuleArouterPath;
import com.example.commonlib.base.BaseApplication;
import com.example.commonlib.bean.UserBean;

import javax.inject.Inject;

/**
 * Created by smile on 2019/3/15.
 */

@Interceptor(priority = 1)
public class LoginInterceptor implements IInterceptor {
    private final String TAG = getClass().getSimpleName();

    @Inject
    UserBean userBean;

    @Override
    public void init(Context context) {
        // 拦截器的初始化，会在sdk初始化的时候调用该方法，仅会调用一次
        Log.e(TAG, "init: 初始化 登录功能 路由 拦截器");
        BaseApplication.getBaseComponent().inject(this);
    }

    @Override
    public void process(Postcard postcard, InterceptorCallback callback) {
        Log.e(TAG, "process: 进入了 登录功能 路由 拦截器" + Thread.currentThread().getName() + ", id = " + Thread.currentThread().getId());
        if (postcard == null) {
            return;
        }
        Log.e(TAG, "process: 进入了 登录功能 路由 拦截器 path = " + postcard.getPath() + ", userBean = " + userBean);
        if (ChatModuleArouterPath.CHAT_MAIN_ACTIVITY.equals(postcard.getPath())) {
            if (userBean == null || TextUtils.isEmpty(userBean.getUserToken())) {
                callback.onInterrupt(new RuntimeException("没有登录，  userBean == null"));
                return;
            }
        }
        callback.onContinue(postcard);
    }
}
