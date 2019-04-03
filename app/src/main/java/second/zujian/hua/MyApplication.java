package second.zujian.hua;

import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentProvider;
import android.util.Log;

import com.example.commonlib.base.BaseApplication;
import com.example.commonlib.bean.UserBean;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import dagger.android.HasBroadcastReceiverInjector;
import dagger.android.HasContentProviderInjector;
import dagger.android.HasServiceInjector;
import second.zujian.hua.dagger.component.DaggerAppComponent;

/**
 * Created by smile on 2019/3/12.
 */

public class MyApplication extends BaseApplication implements HasActivityInjector, HasServiceInjector, HasBroadcastReceiverInjector, HasContentProviderInjector {

    private final String TAG = getClass().getSimpleName();

    @Inject
    DispatchingAndroidInjector<Activity> activityInjector;
    @Inject
    DispatchingAndroidInjector<BroadcastReceiver> broadcastReceiverInjector;
    @Inject
    DispatchingAndroidInjector<Service> serviceInjector;
    @Inject
    DispatchingAndroidInjector<ContentProvider> contentProviderInjector;

    @Inject
    UserBean userBean;

    @Override
    public void onCreate() {
        super.onCreate();
        DaggerAppComponent.builder().baseComponent(getBaseComponent()).build().inject(this);
        Log.e(TAG, "MyApplication onCreate: userBean = " + userBean);
    }

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return activityInjector;
    }

    @Override
    public AndroidInjector<BroadcastReceiver> broadcastReceiverInjector() {
        return broadcastReceiverInjector;
    }

    @Override
    public AndroidInjector<Service> serviceInjector() {
        return serviceInjector;
    }

    @Override
    public AndroidInjector<ContentProvider> contentProviderInjector() {
        return contentProviderInjector;
    }
}
