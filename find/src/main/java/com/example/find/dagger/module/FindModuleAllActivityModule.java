package com.example.find.dagger.module;

import com.example.commonlib.dagger.scope.ActivityScope;
import com.example.find.ui.FindMainActivity;
import com.example.find.ui.activity.PoliceWalkTrackActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by smile on 2019/3/23.
 * Find模块所有可能使用dagger2注入实例的activity，都要这个类里面声明；
 */

@Module
public abstract class FindModuleAllActivityModule {

    @ActivityScope
    @ContributesAndroidInjector
    abstract FindMainActivity provideFindMainActivity();

    @ActivityScope
    @ContributesAndroidInjector
    abstract PoliceWalkTrackActivity providePoliceWalkTrackActivity();
}
