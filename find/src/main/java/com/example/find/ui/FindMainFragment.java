package com.example.find.ui;


import android.content.Context;
import android.graphics.Point;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.baidu.BaiduMapManager;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.BDLocationService;
import com.baidu.mapapi.clusterutil.clustering.ClusterItem;
import com.baidu.mapapi.clusterutil.clustering.ClusterManager;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.example.commonlib.arouter.FindModuleArouterPath;
import com.example.commonlib.base.BaseFragment;
import com.example.commonlib.util.MyLog;
import com.example.commonlib.util.ShowToast;
import com.example.commonlib.util.UIUtils;
import com.example.find.R;
import com.example.find.R2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
@Route(path = FindModuleArouterPath.FIND_MAIN_FRAGMENT)
public class FindMainFragment extends BaseFragment {

    @BindView(R2.id.bd_mapview)
    TextureMapView mMapView;
    @BindView(R2.id.image_screen_center)
    ImageView imageScreenCenter;
    @BindView(R2.id.tv_subscribe_msg)
    TextView tvSubscribeMsg;
    @BindView(R2.id.tv_subscribe_camera)
    TextView tvSubscribeCamera;
    @BindView(R2.id.rl_subscribe_msg)
    RelativeLayout rlSubscribeMsg;
    @BindView(R2.id.rl_location)
    RelativeLayout rlLocation;

    @Inject
    Context mContext;

    private final String TAG = getClass().getSimpleName();
    private BaiduMap mBaiduMap;
    private BDLocationService mBDLocationService;
    private String locationAddress = "";
    private LatLng locationLatlng;//保存定位接口回调结果的经纬度，用于添加一个固定不变的标记(marker)
    private double screenCenterLongitude, screenCenterLatitude;//屏幕中心点经纬度

    private BitmapDescriptor bitmapDescScreenCenter, bitmapDescLocation, bitmapDescJuheMarker;
    private GeoCoder mGeoCoder;
    private boolean bdmapHasChange;//地图状态是否发生了变化

    private boolean isFirstFinishLocation;
    private boolean isFirstFinishMapChange;
    private float mapCurrentZoom = 15.0f;//实时记录地图层级 默认15
    private LatLng rightTopLatlng, leftBottomLatlng;
    private HashMap<Integer, Integer> mapZoomScale = new HashMap<>();// 地图缩放层级比例尺对应的距离

    private List<BaiDuMapMarkerItem> allCameraMarkerItems = new ArrayList<>();
    private List<BaiDuMapMarkerItem> needCameraMarkerItems = new ArrayList<>();

    /**
     * 发起百度地图定位的时间
     */
    private long startBDLocationTime;
    /**
     * 百度地图聚合点管理类
     */
    private ClusterManager<BaiDuMapMarkerItem> mClusterManager;


    @Override
    protected int bindLayout() {
        return R.layout.fragment_find_main;
    }

    @Override
    protected void initView() {
        initMarkerBitmap();
    }

    @Override
    public void onStart() {
        super.onStart();
        MyLog.e(TAG, TAG + "---onStart() mMapView = " + mMapView);
    }

    @Override
    public void onResume() {
        if (mMapView != null) {
            mMapView.onResume();
        }
        super.onResume();
        MyLog.e(TAG, TAG + "---onResume() mMapView = " + mMapView);
    }

    @Override
    public void onPause() {
        if (mMapView != null) {
            mMapView.onPause();
        }
        super.onPause();
        MyLog.e(TAG, TAG + "---onPause()  mMapView = " + mMapView);
    }

    @Override
    public void onStop() {
        super.onStop();
        MyLog.e(TAG, TAG + "---onStop() mMapView = " + mMapView);
    }

    /**
     * 启动百度地图定位
     */
    private void startBaiDuMapLocation() {
        if (System.currentTimeMillis() - startBDLocationTime < 10 * 1000) {
            return;
        }
        startBDLocationTime = System.currentTimeMillis();
        if (mBDLocationService != null) {
            //重启定位SDK,后台常驻运行的APP可以尝试在回到前台的情况下重启定位SDK,防止因长时间后台运行被系统回收定位权限造成定位失败
            mBDLocationService.resStartBDLocation();
        }
    }

    /**
     * 停止百度地图定位
     */
    private void stopBaiDuMapLocation() {
        if (mBDLocationService != null) {
            mBDLocationService.stopBDLocation();
        }
    }

    /**
     * 重新添加聚合点marker
     */
    private void reloadClusterMarker() {
        needCameraMarkerItems.clear();
        for (BaiDuMapMarkerItem cameraMarkerItem : allCameraMarkerItems) {
            double markerLat = cameraMarkerItem.getPosition().latitude;
            double markerLng = cameraMarkerItem.getPosition().longitude;
            if (markerLat > leftBottomLatlng.latitude && markerLat < rightTopLatlng.latitude
                    && markerLng > leftBottomLatlng.longitude && markerLng < rightTopLatlng.longitude) {
                needCameraMarkerItems.add(cameraMarkerItem);
            }
        }
        rlSubscribeMsg.setVisibility(View.VISIBLE);
        tvSubscribeMsg.setText("人脸抓拍机（" + allCameraMarkerItems.size() + "）");
        MyLog.e(TAG, "开始聚合了 allCameraMarkerItems = " + allCameraMarkerItems.size() + ", needCameraMarkerItems = " + needCameraMarkerItems.size());
        mClusterManager.clearItems();//清除所有的items
        //        mClusterManager.getMarkerCollection().clear();
        //        mClusterManager.getClusterMarkerCollection().clear();
        mClusterManager.addItems(needCameraMarkerItems);
        //算法计算聚合，并显示
        mClusterManager.cluster();//类似于通知地图刷新聚合点marker
    }

    private void initMarkerBitmap() {
        // 初始化全局 bitmap 信息，不用时及时 recycle
        bitmapDescScreenCenter = BitmapDescriptorFactory.fromResource(R.drawable.screen_center_marker);
        bitmapDescLocation = BitmapDescriptorFactory.fromResource(R.drawable.location_marker);
        bitmapDescJuheMarker = BitmapDescriptorFactory.fromResource(R.drawable.location_marker);
    }

    private void recycleBitmap() {
        bitmapDescLocation.recycle();
        bitmapDescScreenCenter.recycle();
        bitmapDescJuheMarker.recycle();
    }

    @OnClick({R2.id.image_screen_center, R2.id.rl_subscribe_msg, R2.id.rl_location})
    public void onViewClicked(View view) {
        if (R.id.image_screen_center == view.getId()) {

        } else if (R.id.rl_subscribe_msg == view.getId()) {

        } else if (R.id.rl_location == view.getId()) {

        }
    }

    private BaiduMap.OnMapLoadedCallback mBaiDuMapLoadedCallback = new BaiduMap.OnMapLoadedCallback() {
        @Override
        public void onMapLoaded() {
            //  screenCenterPoint = new Point(mMapView.getWidth() / 2, mMapView.getHeight() / 2);
            //  MyLog.e(TAG, TAG + "地图加载完成---mMapView.getWidth()=" + mMapView.getWidth() + ", mMapView.getHeight()=" + mMapView.getHeight());
            MyLog.e(TAG, TAG + "地图加载完成");
            //必须在地图加载完后才能去设置，否则设置无效
            mMapView.setZoomControlsPosition(new Point(UIUtils.dp2px(mContext, 306), UIUtils.dp2px(mContext, 416)));
            UIUtils.setViewLayout(rlLocation, UIUtils.dp2px(mContext, 301), UIUtils.dp2px(mContext, 356));
        }
    };

    private BDLocationListener mBaiDuMapLocationListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // TODO Auto-generated method stub
            MyLog.e(TAG, "定位结果回调 location = " + location);
            stopBaiDuMapLocation();
            startBDLocationTime = startBDLocationTime - 10000;
            if (mMapView == null || mBaiduMap == null) {
                return;
            }
            if (location == null) {
                ShowToast.showToast(mContext, "地图定位功能返回空的数据");
            } else if (location.getLocType() == BDLocation.TypeGpsLocation || location.getLocType() == BDLocation.TypeOffLineLocation
                    || location.getLocType() == BDLocation.TypeNetWorkLocation) {
                StringBuffer sb = new StringBuffer(256);
                sb.append("time : ");
                /**
                 * 时间也可以使用systemClock.elapsedRealtime()方法 获取的是自从开机以来，每次回调的时间；
                 * location.getTime() 是指服务端出本次结果的时间，如果位置不发生变化，则时间不变
                 */
                sb.append(location.getTime());
                sb.append("\nlocType : ");// 定位类型
                sb.append(location.getLocType());
                sb.append("\nlocType description : ");// *****对应的定位类型说明*****
                sb.append(location.getLocationDescribe());
                sb.append("\nlatitude : ");// 纬度
                sb.append(location.getLatitude());
                sb.append("\nlontitude : ");// 经度
                sb.append(location.getLongitude());
                sb.append("\naddr : ");// 地址信息
                sb.append(location.getAddrStr());
                sb.append("\nUserIndoorState: ");// *****返回用户室内外判断结果*****
                MyLog.e(TAG, sb.toString());
                locationLatlng = new LatLng(location.getLatitude(), location.getLongitude());
                if (location.getAddress() != null) {
                    locationAddress = location.getAddress().city + location.getAddress().district + location.getAddress().street;
                }

                MyLog.e(TAG, TAG + "---location.getLatitude() = " + location.getLatitude() + "  location.getLongitude() = " + location.getLongitude());
//                jumpToMapSpecifiedLocation(location);
                ShowToast.showToast(mContext, "地图定位成功 locType = " + location.getLocType());
            } else {
                ShowToast.showToast(mContext, "地图定位失败，请检查网络：locType = " + location.getLocType());
                if (isFirstFinishLocation) {
                    BaiduMapManager.getInstance().goToTargetLocation(mBaiduMap, new LatLng(22.587564, 114.122458), 18.0f);
                }
            }
            isFirstFinishLocation = false;
        }
    };

    private OnGetGeoCoderResultListener mBaiDuMapOnGetGeoCoderResultListener = new OnGetGeoCoderResultListener() {

        @Override
        public void onGetGeoCodeResult(GeoCodeResult result) {
            //地理编码查询结果回调函数
            MyLog.e(TAG, TAG + "地理编码查询结果:result = " + result);
            if (result == null) {
                return;
            }
            MyLog.e(TAG, TAG + "地理编码查询结果:result.error = " + result.error);
        }

        @Override
        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
            //反地理编码查询结果回调函数
            MyLog.e(TAG, TAG + "逆地理编码查询结果:result = " + result);
            if (result == null) {
                ShowToast.showToast(mContext, "哎呀，逆地理编码查询不到任何结果!!!");
                return;
            }
            ReverseGeoCodeResult.AddressComponent addressComponent = result.getAddressDetail();
            if (addressComponent != null && !TextUtils.isEmpty(result.getAddress())) {
                locationAddress = addressComponent.city + addressComponent.district + addressComponent.street;
                ShowToast.showToast(mContext, result.getAddress() + "-" + result.getSematicDescription());
            } else {
                ShowToast.showToast(mContext, "地图上查不到位置信息，请检查网络!");
            }

            MyLog.e(TAG, TAG + "逆地理编码查询结果:result.getLocation() = " + result.getLocation() + ", locationAddress = " + locationAddress);
        }
    };

    /**
     * 每个Marker点，包含Marker点坐标以及图标  方便聚合功能使用
     */
    private class BaiDuMapMarkerItem implements ClusterItem {
        private final LatLng mPosition;

        public BaiDuMapMarkerItem(LatLng latLng) {
            mPosition = latLng;
        }

        @Override
        public LatLng getPosition() {
            return mPosition;
        }

        @Override
        public BitmapDescriptor getBitmapDescriptor() {
            return bitmapDescJuheMarker;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
