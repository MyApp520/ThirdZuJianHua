package com.example.commonlib.dagger.module;

import android.content.Context;

import com.example.commonlib.bean.UserBean;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by smile on 2019/3/19.
 */

@Module
public class BaseModule {
    Context context;

    public BaseModule(Context context) {
        this.context = context;
    }

    @Singleton
    @Provides
    public Context provideContext() {
        return context;
    }

    @Singleton
    @Provides
    public UserBean provideUserBean() {
        return new UserBean();
    }
}
