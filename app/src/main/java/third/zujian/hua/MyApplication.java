package third.zujian.hua;

import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentProvider;
import android.util.Log;

import com.baidu.location.BDLocationService;
import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.example.commonlib.base.BaseApplication;
import com.example.commonlib.bean.UserBean;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import dagger.android.HasBroadcastReceiverInjector;
import dagger.android.HasContentProviderInjector;
import dagger.android.HasServiceInjector;
import third.zujian.hua.dagger.component.DaggerAppComponent;

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

    public static BDLocationService locationService;

    @Override
    public void onCreate() {
        super.onCreate();
        DaggerAppComponent.builder().baseComponent(getBaseComponent()).build().inject(this);
        initBDMap();
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

    private void initBDMap() {
        /***
         * 初始化定位sdk，建议在Application中创建
         */
        locationService = new BDLocationService(getApplicationContext());
        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
        SDKInitializer.initialize(getApplicationContext());
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL);
    }
}
