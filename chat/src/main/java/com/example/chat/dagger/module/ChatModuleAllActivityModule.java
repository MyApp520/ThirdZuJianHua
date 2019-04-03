package com.example.chat.dagger.module;

import com.example.chat.ui.ChatMainActivity;
import com.example.commonlib.dagger.scope.ActivityScope;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by smile on 2019/3/23.
 * Chat模块所有可能使用dagger2注入实例的activity，都要这个类里面声明；
 */

@Module
public abstract class ChatModuleAllActivityModule {

    @ActivityScope
    @ContributesAndroidInjector
    abstract ChatMainActivity provideChatMainActivity();
}
