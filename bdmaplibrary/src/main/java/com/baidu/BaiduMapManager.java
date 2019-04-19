package com.baidu;


import com.baidu.location.BDLocationService;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;

import java.util.HashMap;

/**
 * Created by xh_peng on 2017/10/18.
 */
public class BaiduMapManager {
    private static BaiduMapManager instance;
    private BDLocationService mBDLocationService;

    public static BaiduMapManager getInstance() {
        if (instance == null) {
            synchronized (BaiduMapManager.class) {
                instance = new BaiduMapManager();
            }
        }
        return instance;
    }

    /**
     *
     * @param mMapView
     * @param onMapLoadedCallback 地图加载完成监听器
     */
    public BaiduMap initBaiduMap(TextureMapView mMapView, BaiduMap.OnMapLoadedCallback onMapLoadedCallback) {
        //  mMapView.getChildAt(1).setVisibility(View.GONE);//去掉百度地图logo 官方禁止此操作
        // 比例尺控件
        mMapView.showScaleControl(true);
        // 缩放控件
        mMapView.showZoomControls(true);

        // 得到操作地图的对象
        BaiduMap mBaiduMap = mMapView.getMap();
        // 普通地图
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        // mBaiduMap.showMapPoi(true);//默认true 地图标注,地图上面地点名称
        // 开启定位图层，一定不要少了这句，否则对在地图的设置、绘制定位点将无效
        mBaiduMap.setMyLocationEnabled(false);//如果为false，定位出来的那个指示点将不会显示
        // 对定位的图标进行配置，需要MyLocationConfiguration实例，这个类是用于设置定位图标的显示方式的
        // mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, bitmapDescLocation));
        mBaiduMap.setOnMapLoadedCallback(onMapLoadedCallback);
        mBaiduMap.setTrafficEnabled(false);//设置是否打开交通图层
        mBaiduMap.setIndoorEnable(true);//是否允许打开室内图
        mBaiduMap.setBuildingsEnabled(false);//是否需要3D楼层效果
        // mBaiduMap.getUiSettings().setAllGesturesEnabled(true);//允许所有手势操作
        mBaiduMap.getUiSettings().setCompassEnabled(false);//是否需要指南针
        return mBaiduMap;
    }

    /**
     * 地图定位到指定坐标点
     *
     * @param targetLocation 指定点经纬度
     */
    public void goToTargetLocation(BaiduMap mBaiduMap, LatLng targetLocation, float mapZoom) {
        if (mBaiduMap == null) {
            return;
        }
        if (mapZoom < 4.0f || mapZoom > 21.0f) {
            mapZoom = 15.0f;
        }
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.overlook(0);
        //设置缩放中心点；缩放比例；
        builder.target(targetLocation).zoom(mapZoom);
        //给地图设置状态
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
    }

    /**
     * 在屏幕中心添加一个Marker 标记你想要定位的位置
     */
    public Marker addMarkerInScreenCenter(BaiduMap mBaiduMap, LatLng screenLatlng, BitmapDescriptor bitmapDescScreenCenter) {
        //构建MarkerOption，用于在地图上添加Marker
        MarkerOptions markerOption = new MarkerOptions().position(screenLatlng)
                .icon(bitmapDescScreenCenter)
                .perspective(false)
                .anchor(0.5f, 0.5f)//设置 marker覆盖物与位置点的位置关系，默认（0.5f, 1.0f）水平居中，垂直下对齐
                .rotate(0)//图片旋转角度
                .zIndex(8)
                .animateType(MarkerOptions.MarkerAnimateType.drop);//marker出现的动画方式，上下跳动
        //在地图上添加Marker，并显示
        return (Marker) mBaiduMap.addOverlay(markerOption);
    }

    /**
     * 添加一个从地上生长的Marker 标记一个固定的位置
     */
    public Marker addGrowMarker(BaiduMap mBaiduMap, Marker mGrowMarker, LatLng markerLatlng, BitmapDescriptor bitmapDescLocation) {
        removeMarker(mGrowMarker);
        MarkerOptions markerOption = new MarkerOptions().position(markerLatlng)
                .icon(bitmapDescLocation)
                .perspective(false)
                .anchor(0.5f, 0.5f)
                .rotate(0)
                .zIndex(6);
        // 生长动画
        markerOption.animateType(MarkerOptions.MarkerAnimateType.grow);
        //在地图上添加Marker，并显示
        return (Marker) mBaiduMap.addOverlay(markerOption);
    }

    public void removeMarker(Marker moverMarker) {
        if (moverMarker != null) {
            moverMarker.remove();
            moverMarker = null;
        }
    }

    /**
     * 逆地理编码请求---根据经纬度获取地址
     *
     * @param lat 纬度
     * @param lon 经度
     */
    public void reGeoCode(GeoCoder mGeoCoder, double lat, double lon) {
        if (mGeoCoder == null) {
            return;
        }
        // 反Geo搜索
        mGeoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(new LatLng(lat, lon)));
    }

    /**
     * 地理编码请求---根据地址获取经纬度
     *
     * @param cityName
     * @param geoAddress
     */
    public void geoCode(GeoCoder mGeoCoder, String cityName, String geoAddress) {
        if (mGeoCoder == null) {
            return;
        }
        // Geo搜索
        mGeoCoder.geocode(new GeoCodeOption().city(cityName).address(geoAddress));
    }

    public BDLocationService getBDLocationService() {
        return mBDLocationService;
    }

    public void setBDLocationService(BDLocationService mBDLocationService) {
        this.mBDLocationService = mBDLocationService;
    }

    public HashMap<Integer, Integer> initMapZoomScale() {
        HashMap<Integer, Integer> mapZoomScale = new HashMap<>();// 地图缩放层级比例尺对应的距离
        mapZoomScale.put(3, 2000000);
        mapZoomScale.put(4, 1000000);
        mapZoomScale.put(5, 500000);
        mapZoomScale.put(6, 200000);
        mapZoomScale.put(7, 100000);
        mapZoomScale.put(8, 50000);
        mapZoomScale.put(9, 25000);
        mapZoomScale.put(10, 20000);
        mapZoomScale.put(11, 10000);
        mapZoomScale.put(12, 5000);
        mapZoomScale.put(13, 2000);
        mapZoomScale.put(14, 1000);
        mapZoomScale.put(15, 500);
        mapZoomScale.put(16, 200);
        mapZoomScale.put(17, 100);
        mapZoomScale.put(18, 50);
        mapZoomScale.put(19, 20);
        mapZoomScale.put(20, 10);
        mapZoomScale.put(21, 5);
        mapZoomScale.put(22, 2);
        return mapZoomScale;
    }
}
