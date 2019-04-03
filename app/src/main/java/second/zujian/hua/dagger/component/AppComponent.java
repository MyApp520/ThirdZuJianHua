package second.zujian.hua.dagger.component;

import com.example.commonlib.dagger.component.BaseComponent;
import com.example.commonlib.dagger.scope.AppScope;

import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.support.AndroidSupportInjectionModule;
import second.zujian.hua.MyApplication;
import second.zujian.hua.dagger.module.AppActivityModule;

/**
 * Created by smile on 2019/3/20.
 */

@AppScope
@Component(modules = {
        AndroidInjectionModule.class,
        AndroidSupportInjectionModule.class,
        AppActivityModule.class}, dependencies = {BaseComponent.class})
public interface AppComponent {
    void inject(MyApplication myApplication);
}
