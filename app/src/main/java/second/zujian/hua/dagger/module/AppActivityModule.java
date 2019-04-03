package second.zujian.hua.dagger.module;

import com.example.chat.dagger.module.ChatModuleAllActivityModule;
import com.example.commonlib.dagger.scope.ActivityScope;
import com.example.find.dagger.module.FindModuleAllActivityModule;
import com.example.group.dagger.module.GroupModuleAllActivityModule;
import com.example.mine.dagger.module.MineModuleAllActivityModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import second.zujian.hua.ui.MainActivity;
import second.zujian.hua.ui.LoginActivity;

/**
 * Created by smile on 2019/3/20.
 * 注意：
 *      1、主模块（即app模块）所有可能使用dagger2注入实例的activity，都要这个类里面声明；
 *      2、其他子模块可能使用dagger2注入实例的activity，在子模块相应的module类声明后，需要将module类通过includes的方式包含到此类来；
 */
@Module(includes = {
        ChatModuleAllActivityModule.class,
        GroupModuleAllActivityModule.class,
        FindModuleAllActivityModule.class,
        MineModuleAllActivityModule.class})
public abstract class AppActivityModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = {AppFragmentModule.class})
    abstract MainActivity provideMainActivity();

    @ActivityScope
    @ContributesAndroidInjector
    abstract LoginActivity provideLoginActivity();
}
