package com.example.find.ui;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.baidu.BaiduMapManager;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.BDLocationService;
import com.baidu.mapapi.clusterutil.clustering.Cluster;
import com.baidu.mapapi.clusterutil.clustering.ClusterItem;
import com.baidu.mapapi.clusterutil.clustering.ClusterManager;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.utils.DistanceUtil;
import com.example.commonlib.arouter.FindModuleArouterPath;
import com.example.commonlib.base.BaseFragment;
import com.example.commonlib.util.AndroidLocationUtils;
import com.example.commonlib.util.AppDebugUtil;
import com.example.commonlib.util.MyLog;
import com.example.commonlib.util.ShowToast;
import com.example.commonlib.util.UIUtils;
import com.example.find.R;
import com.example.find.R2;
import com.example.find.ui.activity.PoliceWalkTrackActivity;
import com.example.find.ui.fragment.MapBottomSheetFragmentThird;

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
    TextureMapView mTextureMapView;
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
    private final int GPS_REQUEST_CODE = 0x02;
    private Activity mActivity;
    private BaiduMap mBaiduMap;
    private BDLocationService mBDLocationService;
    private String locationAddress = "";
    private LatLng locationLatlng;//保存定位接口回调结果的经纬度，用于添加一个固定不变的标记(marker)
    private double screenCenterLongitude, screenCenterLatitude;//屏幕中心点经纬度

    private BitmapDescriptor bitmapDescScreenCenter, bitmapDescLocation, bitmapRedAlarmMarker, bitmapPolicemenMarker, bitmapCameraMarker;
    private GeoCoder mGeoCoder;
    private boolean bdmapHasChange;//地图状态是否发生了变化

    private boolean isFirstFinishLocation;
    private boolean isFirstFinishMapChange;
    private float mapCurrentZoom = 15.0f;//实时记录地图层级 默认15
    private LatLng rightTopLatlng, leftBottomLatlng;
    private HashMap<Integer, Integer> mapZoomScale = new HashMap<>();// 地图缩放层级比例尺对应的距离

    private List<BaiDuMapMarkerItem> allMarkerItems = new ArrayList<>();
    private List<BaiDuMapMarkerItem> needMarkerItems = new ArrayList<>();

    /**
     * 发起百度地图定位的时间
     */
    private long startBDLocationTime;
    /**
     * 百度地图聚合点管理类
     */
    private ClusterManager<BaiDuMapMarkerItem> mClusterManager;
    private AlertDialog tipsAlertDialog;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = (Activity) context;
    }

    @Override
    protected int bindLayout() {
        return R.layout.fragment_find_main;
    }

    @Override
    protected void initView() {
        initMarkerBitmap();
        initBDMapView();
    }

    @Override
    public void onStart() {
        super.onStart();
        MyLog.e(TAG, TAG + "---onStart() mTextureMapView = " + mTextureMapView);
    }

    @Override
    public void onResume() {
        super.onResume();
        MyLog.e(TAG, TAG + "---onResume() mTextureMapView = " + mTextureMapView);
    }

    @Override
    public void onPause() {
        super.onPause();
        MyLog.e(TAG, TAG + "---onPause()  mTextureMapView = " + mTextureMapView);
    }

    @Override
    public void onStop() {
        super.onStop();
        MyLog.e(TAG, TAG + "---onStop() mTextureMapView = " + mTextureMapView);
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        MyLog.e(TAG, TAG + "---onSupportVisible()地图界面可见了 mTextureMapView = " + mTextureMapView);
        if (mTextureMapView != null) {
            mTextureMapView.onResume();
        }
        if (mBDLocationService == null) {
            //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
            mBDLocationService = BaiduMapManager.getInstance().getBDLocationService();
            mBDLocationService.registerListener(mBaiDuMapLocationListener);
        }
        if (AndroidLocationUtils.checkGPSIsOpen(mContext)) {
            startBaiDuMapLocation();
        } else {
            tipsAlertDialog = AndroidLocationUtils.openGPSSettings(mActivity, tipsAlertDialog, GPS_REQUEST_CODE);
        }
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        MyLog.e(TAG, TAG + "---onSupportInvisible()地图界面不可见 mTextureMapView = " + mTextureMapView);
        if (mTextureMapView != null) {
            mTextureMapView.onPause();
        }
    }

    @Override
    protected void onLazyInitEvent() {
    }

    private void testMarker() {
        allMarkerItems.clear();
        BaiDuMapMarkerItem baiDuMapMarkerItem;
        for (int i = 0; i < 360; i++) {
            LatLng latLng;
            if (i < 20) {
                latLng = new LatLng(22.587589000555752 + (0.000333 * i), 114.12248499999997 + (0.000333 * i));
            } else if (i < 40) {
                latLng = new LatLng(22.587589000555752 + (0.000123 * i), 114.12248499999997 + (0.000107 * i));
            } else if (i < 60) {
                latLng = new LatLng(22.581589000555752 + (0.000103 * i), 114.12548499999997 + (0.000083 * i));
            } else if (i < 80) {
                latLng = new LatLng(22.583589000555752 + (0.000053 * i), 114.12548499999997 + (0.000123 * i));
            } else if (i < 100) {
                latLng = new LatLng(22.585289000555752 + (0.000123 * i), 114.12348499999997 + (0.000107 * i));
            } else if (i < 140) {
                latLng = new LatLng(22.586189000555752 + (0.000103 * i), 114.12248499999997 + (0.000083 * i));
            } else if (i < 180) {
                latLng = new LatLng(22.586589000555752 + (0.000053 * i), 114.12248499999997 + (0.000123 * i));
            }  else if (i < 220) {
                latLng = new LatLng(22.585589000555752 + (0.000123 * i), 114.12248499999997 + (0.000107 * i));
            } else if (i < 260) {
                latLng = new LatLng(22.584589000555752 + (0.000103 * i), 114.12248499999997 + (0.000083 * i));
            } else if (i < 300) {
                latLng = new LatLng(22.583589000555752 + (0.000053 * i), 114.12248499999997 + (0.000123 * i));
            }  else {
                latLng = new LatLng(22.582589000555752 + (0.000009 * i), 114.12248499999997 + (0.000073 * i));
            }
            baiDuMapMarkerItem = new BaiDuMapMarkerItem(latLng);
            if (i % 3 == 0) {
                baiDuMapMarkerItem.setMarkerType(baiDuMapMarkerItem.CAMERA_MARKER);
            } else if (i % 2 == 0) {
                baiDuMapMarkerItem.setMarkerType(baiDuMapMarkerItem.POLICEMEN_MARKER);
                baiDuMapMarkerItem.setName("民警 " + i + " 号");
                baiDuMapMarkerItem.setNum("602680" + i * 6);
            } else {
                baiDuMapMarkerItem.setMarkerType(baiDuMapMarkerItem.RED_ALARM_MARKER);
                baiDuMapMarkerItem.setName("警情 " + i + " 号");
                baiDuMapMarkerItem.setNum("999" + i * 6);
            }
            allMarkerItems.add(baiDuMapMarkerItem);
        }
        reloadClusterMarker(-1);
    }

    private void initBDMapView() {
        MyLog.e(TAG, TAG + "---initBDmapView() mTextureMapView = " + mTextureMapView);
        if (mTextureMapView == null) {
            return;
        }
        isFirstFinishMapChange = true;
        mapZoomScale = BaiduMapManager.getInstance().initMapZoomScale();
        mBaiduMap = BaiduMapManager.getInstance().initBaiduTextureMapView(mTextureMapView, mBaiDuMapLoadedCallback);

        // 设置地图监听，当地图状态发生改变时，进行点聚合运算
        // ClusterManager类已经实现了BaiduMap.OnMapStatusChangeListener, BaiduMap.OnMarkerClickListener两个接口
        initClusterManager();//初始化聚合点功能

        mBaiduMap.setOnMapStatusChangeListener(mClusterManager);
        // 设置maker点击时的响应
        mBaiduMap.setOnMarkerClickListener(mClusterManager);

        // 初始化地理/逆地理编码模块，注册事件监听
        mGeoCoder = GeoCoder.newInstance();
        mGeoCoder.setOnGetGeoCodeResultListener(mBaiDuMapOnGetGeoCoderResultListener);
    }

    /**
     * 初始化聚合点功能
     */
    private void initClusterManager() {
        // 定义点聚合管理类ClusterManager
        mClusterManager = new ClusterManager<>(mContext, mBaiduMap);
        mClusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<BaiDuMapMarkerItem>() {
            @Override
            public boolean onClusterClick(Cluster<BaiDuMapMarkerItem> cluster) {
                Toast.makeText(mContext, "有" + cluster.getSize() + "个点", Toast.LENGTH_SHORT).show();
                MyLog.e(TAG, TAG + "---onClusterClick 发生了");
                return false;
            }
        });
        mClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<BaiDuMapMarkerItem>() {
            @Override
            public boolean onClusterItemClick(BaiDuMapMarkerItem item) {

                return false;
            }
        });
        mClusterManager.setMapStatusChangeFinishListener(new ClusterManager.OnMapStatusChangeListener() {
            @Override
            public void onClusterFindMapStatusChangeStart(MapStatus mapStatus) {
                bdmapHasChange = true;
                if (rlSubscribeMsg.getVisibility() == View.VISIBLE) {
                    rlSubscribeMsg.setVisibility(View.GONE);
                }
            }

            @Override
            public void onClusterFindMapStatusChangeFinish(MapStatus mapStatus) {
                if (mapStatus == null || mTextureMapView == null || mBaiduMap == null) {
                    bdmapHasChange = false;
                    return;
                }
                mapCurrentZoom = mapStatus.zoom;
                leftBottomLatlng = mapStatus.bound.southwest;
                rightTopLatlng = mapStatus.bound.northeast;

                MyLog.e(TAG, "地图状态变化结束 MapStatus=" + mapStatus.toString());
                MyLog.e(TAG, "地图状态变化结束 rightTopLatlng = " + rightTopLatlng + ", leftBottomLatlng = " + leftBottomLatlng);
                //target为地图操作的中心点经纬度坐标
                BaiduMapManager.getInstance().reGeoCode(mGeoCoder, mapStatus.target.latitude, mapStatus.target.longitude);
                startJumpAnimation();
                rlSubscribeMsg.setVisibility(View.VISIBLE);

                double mapMoveDistance = 0;
                double temp_longitude = mapStatus.target.longitude;
                double temp_latitude = mapStatus.target.latitude;
                if (temp_longitude > 108 && temp_latitude > 20 && screenCenterLongitude > 108 && screenCenterLatitude > 20) {
                    mapMoveDistance = DistanceUtil.getDistance(new LatLng(temp_latitude, temp_longitude), new LatLng(screenCenterLatitude, screenCenterLongitude));
                }

                screenCenterLongitude = temp_longitude;
                screenCenterLatitude = temp_latitude;
                int tempZoomInteger = Math.round((float) Math.floor(mapCurrentZoom)); //取整
                int mapCurrentScale = mapZoomScale.get(tempZoomInteger);
                double mustMoveDistance = mapCurrentZoom / tempZoomInteger * mapCurrentScale;
                if (tempZoomInteger > 18) {
                    mustMoveDistance = mustMoveDistance * 2;
                }

                MyLog.e(TAG, "地图移动距离 mapMoveDistance = " + mapMoveDistance + ", mustMoveDistance = " + mustMoveDistance
                        + "\n tempZoomInteger = " + tempZoomInteger + ", mapCurrentScale = " + mapCurrentScale);
                if (isFirstFinishMapChange) {
                    isFirstFinishMapChange = false;
                }
                if (AppDebugUtil.isDebug()) {
                    testMarker();//测试用例，正式发版时去掉，调试使用
                }
            }
        });
    }

    /**
     * 屏幕中心marker(ImageView) 跳动
     */
    private void startJumpAnimation() {
        if (imageScreenCenter != null) {
            MyLog.e(TAG, TAG + "---来个动画");
            Animation animation = new TranslateAnimation(0, 0, -UIUtils.dp2px(mContext, 68), 0);
            //越来越快
            animation.setInterpolator(new AccelerateInterpolator());
            //整个移动所需要的时间
            animation.setDuration(200);
            //设置动画
            imageScreenCenter.setAnimation(animation);
            //开始动画
            imageScreenCenter.startAnimation(animation);
        } else {
            Log.e(TAG, TAG + " imageScreenCenter is null");
        }
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
     *
     * @param markerType 图片需要显示的是哪种类型的marker  -1：显示全部的   0：镜头    1：警情    2：警员
     */
    private void reloadClusterMarker(int markerType) {
        needMarkerItems.clear();
        for (BaiDuMapMarkerItem markerItem : allMarkerItems) {
            double markerLat = markerItem.getPosition().latitude;
            double markerLng = markerItem.getPosition().longitude;
            if (markerLat > leftBottomLatlng.latitude && markerLat < rightTopLatlng.latitude
                    && markerLng > leftBottomLatlng.longitude && markerLng < rightTopLatlng.longitude) {
                switch (markerType) {
                    case -1:
                        needMarkerItems.add(markerItem);
                        break;
                    default:
                        if (markerType == markerItem.getMarkerType()) {
                            needMarkerItems.add(markerItem);
                        }
                        break;
                }
            }
        }
        rlSubscribeMsg.setVisibility(View.VISIBLE);
        tvSubscribeMsg.setText("聚合点（" + needMarkerItems.size() + "）");
        MyLog.e(TAG, "开始聚合了 allMarkerItems = " + allMarkerItems.size() + ", needMarkerItems = " + needMarkerItems.size());
        mClusterManager.clearItems();//清除所有的items
        //        mClusterManager.getMarkerCollection().clear();
        //        mClusterManager.getClusterMarkerCollection().clear();
        mClusterManager.addItems(needMarkerItems);
        //算法计算聚合，并显示
        mClusterManager.cluster();//类似于通知地图刷新聚合点marker
    }

    private void initMarkerBitmap() {
        // 初始化全局 bitmap 信息，不用时及时 recycle
        bitmapDescScreenCenter = BitmapDescriptorFactory.fromResource(R.drawable.screen_center_marker);
        bitmapDescLocation = BitmapDescriptorFactory.fromResource(R.drawable.location_marker);
        bitmapRedAlarmMarker = BitmapDescriptorFactory.fromResource(R.drawable.red_alarm_marker);
        bitmapPolicemenMarker = BitmapDescriptorFactory.fromView(LayoutInflater.from(mContext).inflate(R.layout.view_map_marker_policemen, null));
        bitmapCameraMarker = BitmapDescriptorFactory.fromResource(R.drawable.image_camera);
    }

    private void recycleBitmap() {
        bitmapDescLocation.recycle();
        bitmapDescScreenCenter.recycle();
        bitmapRedAlarmMarker.recycle();
        bitmapPolicemenMarker.recycle();
        bitmapCameraMarker.recycle();
    }

    @OnClick({R2.id.tv_subscribe_msg, R2.id.rl_location, R2.id.tv_subscribe_camera})
    public void onViewClicked(View view) {
        if (R.id.tv_subscribe_msg == view.getId()) {
//            int[] types = new int[] {-1, 0, 1, 2};
//            reloadClusterMarker(types[new Random().nextInt(4)]);
            DialogFragment dialogFragment = new MapBottomSheetFragmentThird();
            dialogFragment.show(getChildFragmentManager(), "MapBottomSheetFragmentThird");
        } else if (R.id.rl_location == view.getId()) {
            startBaiDuMapLocation();
        } else if (R.id.tv_subscribe_camera == view.getId()) {
            startActivity(new Intent(mActivity, PoliceWalkTrackActivity.class));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //  super.onActivityResult(requestCode, resultCode, data);
        MyLog.e(TAG, TAG + "---resultCode=" + resultCode);
        switch (requestCode) {
            case GPS_REQUEST_CODE:
                break;
        }
    }

    private BaiduMap.OnMapLoadedCallback mBaiDuMapLoadedCallback = new BaiduMap.OnMapLoadedCallback() {
        @Override
        public void onMapLoaded() {
            //  screenCenterPoint = new Point(mMapView.getWidth() / 2, mMapView.getHeight() / 2);
            //  MyLog.e(TAG, TAG + "地图加载完成---mMapView.getWidth()=" + mMapView.getWidth() + ", mMapView.getHeight()=" + mMapView.getHeight());
            MyLog.e(TAG, TAG + "地图加载完成");
            //必须在地图加载完后才能去设置，否则设置无效
            mTextureMapView.setZoomControlsPosition(new Point(UIUtils.dp2px(mContext, 306), UIUtils.dp2px(mContext, 416)));
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
            if (mTextureMapView == null || mBaiduMap == null) {
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
                BaiduMapManager.getInstance().jumpToMapSpecifiedLocation(mBaiduMap, location);
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
        private final int CAMERA_MARKER = 0;//镜头
        private final int RED_ALARM_MARKER = 1;//警情
        private final int POLICEMEN_MARKER = 2;//警员
        private LatLng latlng;
        private int markerType;
        private String name;
        private String num;

        public BaiDuMapMarkerItem(LatLng latlng) {
            this.latlng = latlng;
        }

        public LatLng getLatlng() {
            return latlng;
        }

        public void setLatlng(LatLng latlng) {
            this.latlng = latlng;
        }

        public int getMarkerType() {
            return markerType;
        }

        public void setMarkerType(int markerType) {
            this.markerType = markerType;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        @Override
        public LatLng getPosition() {
            return latlng;
        }

        @Override
        public BitmapDescriptor getBitmapDescriptor() {
            if (RED_ALARM_MARKER == markerType) {
                return bitmapRedAlarmMarker;
            } else if (POLICEMEN_MARKER == markerType) {
                return bitmapPolicemenMarker;
            }
            return bitmapCameraMarker;
        }
    }

    @Override
    public void onDestroyView() {
        MyLog.e(TAG, TAG + "---onDestroyView() mTextureMapView = " + mTextureMapView);
        if (mBDLocationService != null) {
            mBDLocationService.unregisterListener(mBaiDuMapLocationListener);
            stopBaiDuMapLocation();
        }
        imageScreenCenter.clearAnimation();
        if (mBaiduMap != null) {
            mBaiduMap.clear();
            // 关闭定位图层
            mBaiduMap.setMyLocationEnabled(false);
            mBaiduMap.setOnMapLoadedCallback(null);
        }

        dismissXhProgressDialog(tipsAlertDialog);
        recycleBitmap();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        MyLog.e(TAG, TAG + "---onDestroy()  mTextureMapView = " + mTextureMapView);
        if (mTextureMapView != null) {
            mTextureMapView.onDestroy();
        }
        super.onDestroy();
        mBaiduMap = null;
        mTextureMapView = null;
    }
}
