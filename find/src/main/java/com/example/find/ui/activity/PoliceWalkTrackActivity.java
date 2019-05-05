package com.example.find.ui.activity;


import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

import com.baidu.BaiduMapManager;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.example.commonlib.base.BaseActivity;
import com.example.commonlib.util.MyLog;
import com.example.find.R;
import com.example.find.R2;
import com.example.find.ui.fragment.MapBottomSheetFragmentThird;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class PoliceWalkTrackActivity extends BaseActivity {
    @BindView(R2.id.walk_trace_mapview)
    MapView walkTraceMapview;

    private final String TAG = getClass().getSimpleName();
    private BaiduMap mBaiduMap;
    private List<LatLng> walkTrackLatlngList = new ArrayList<>();

    @Override
    protected int bindLayout() {
        return R.layout.activity_police_walk_track;
    }

    @Override
    protected void initView() {
        initBDMapView();
        onLazyInitEvent();
    }

    protected void onLazyInitEvent() {
        walkTrackLatlngList.add(new LatLng(22.587207384048263, 114.122750898853));
        walkTrackLatlngList.add(new LatLng(22.588359756046753, 114.12473886872937));
        walkTrackLatlngList.add(new LatLng(22.586193216783315, 114.1244530679942));
        walkTrackLatlngList.add(new LatLng(22.587813614234943, 114.12531940457545));
        walkTrackLatlngList.add(new LatLng(22.590578444988914, 114.1261735397880));
        walkTrackLatlngList.add(new LatLng(22.59537710637028, 114.12445916931247));
        walkTrackLatlngList.add(new LatLng(22.59899723568473, 114.12275089967542));
        walkTrackLatlngList.add(new LatLng(22.59836839385846, 114.12059115859824));
    }

    @Override
    public void onResume() {
        MyLog.e(TAG, TAG + "---onResume() 查询轨迹地图界面可见了 walkTraceMapview = " + walkTraceMapview);
        if (walkTraceMapview != null) {
            walkTraceMapview.onResume();
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        MyLog.e(TAG, TAG + "---onPause() 查询轨迹地图界面不可见 walkTraceMapview = " + walkTraceMapview);
        if (walkTraceMapview != null) {
            walkTraceMapview.onPause();
        }
        super.onPause();
    }

    private void initBDMapView() {
        MyLog.e(TAG, TAG + "---initBDmapView() walkTraceMapview = " + walkTraceMapview);
        if (walkTraceMapview == null) {
            return;
        }
        mBaiduMap = BaiduMapManager.getInstance().initBaiduMapView(walkTraceMapview, mBaiDuMapLoadedCallback);
    }

    private void showMapBottomSheetFragment() {
//        MapBottomSheetFragment mapBottomSheetFragment = new MapBottomSheetFragment();
//        mapBottomSheetFragment.setTopOffset(250);
//        mapBottomSheetFragment.show(getSupportFragmentManager(), "MapBottomSheetFragment");

//        MapBottomSheetFragmentSecond mapBottomSheetFragmentSecond = new MapBottomSheetFragmentSecond();
//        mapBottomSheetFragmentSecond.setTopOffset(250);
//        mapBottomSheetFragmentSecond.show(getSupportFragmentManager(), "mapBottomSheetFragmentSecond");

        DialogFragment dialogFragment = new MapBottomSheetFragmentThird();
        dialogFragment.show(getSupportFragmentManager(), "MapBottomSheetFragmentThird");
    }

    @OnClick({R2.id.btn_query_track})
    public void onViewClicked(View view) {
        if (R.id.btn_query_track == view.getId()) {
            showMapBottomSheetFragment();
        }
    }

    private BaiduMap.OnMapLoadedCallback mBaiDuMapLoadedCallback = new BaiduMap.OnMapLoadedCallback() {
        @Override
        public void onMapLoaded() {
            BaiduMapManager.getInstance().goToTargetLocation(mBaiduMap, walkTrackLatlngList.get(0), 16.0f);
            //设置折线的属性
            OverlayOptions mOverlayOptions = new PolylineOptions()
                    .width(10)
                    .color(0xAAFF0000)
                    .points(walkTrackLatlngList)
                    .dottedLine(false); //设置折线是否显示为虚线

            //在地图上绘制折线，mPloyline 折线对象
            Overlay mPolyline = mBaiduMap.addOverlay(mOverlayOptions);
            Log.e(TAG, "onMapLoaded: 在地图上绘制折线，mPloyline = " + mPolyline);
        }
    };

    @Override
    protected void onDestroy() {
        if (mBaiduMap != null) {
            //清除地图上的所有覆盖物
            mBaiduMap.clear();
            mBaiduMap.setOnMapLoadedCallback(null);
        }
        if (walkTraceMapview != null) {
            walkTraceMapview.onDestroy();
        }
        super.onDestroy();
        mBaiduMap = null;
        walkTraceMapview = null;
    }
}
