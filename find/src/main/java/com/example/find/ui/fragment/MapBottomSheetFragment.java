package com.example.find.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.commonlib.adapter.CommonViewPagerAdapter;
import com.example.commonlib.base.BaseFullBottomSheetFragment;
import com.example.commonlib.view.CommonNestedScrollView;
import com.example.find.R;
import com.example.find.R2;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by smile on 2019/4/28.
 */

public class MapBottomSheetFragment extends BaseFullBottomSheetFragment {

    @BindView(R2.id.common_nestedScrollView)
    CommonNestedScrollView commonNestedScrollView;
    @BindView(R2.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R2.id.viewPager)
    ViewPager viewPager;

    private final String TAG = getClass().getSimpleName();
    private ArrayList<String> tabTitleList;
    private ArrayList<Fragment> fragmentList;
    Unbinder unbinder;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.e(TAG, "onAttach:地图底部弹出框 context = " + context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map_bottom_sheet, container, false);
        unbinder = ButterKnife.bind(this, view);
        initCommonNestedScrollView();
        Log.e(TAG, "onCreateView: 地图底部弹出框");
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e(TAG, "onStart: 地图底部弹出框");
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());

        tabTitleList = new ArrayList<>();
        tabTitleList.add("解放军");
        tabTitleList.add("八路军");
        tabTitleList.add("新四军");

        fragmentList = new ArrayList<>();
        fragmentList.add(PoliceAlarmVideoFragment.newInstance(99));
        fragmentList.add(PoliceAlarmVideoFragment.newInstance(88));
        fragmentList.add(PoliceAlarmVideoFragment.newInstance(20));

        viewPager.setAdapter(new CommonViewPagerAdapter(getChildFragmentManager(), tabTitleList, fragmentList));
        tabLayout.setupWithViewPager(viewPager);
    }

    private void initCommonNestedScrollView() {
        commonNestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                int[] tabLayoutXY = new int[2];
                int[] tabLayoutXYW = new int[2];
                tabLayout.getLocationOnScreen(tabLayoutXY);
                tabLayout.getLocationInWindow(tabLayoutXYW);
                Log.e(TAG, "tabLayout.getY() = " + tabLayout.getY() + ",  tabLayout.getHeight() = " + tabLayout.getHeight()
                        + ",  tabLayoutXY[1] = " + tabLayoutXY[1] + ", tabLayoutXYW[1] = " + tabLayoutXYW[1]);
            }
        });
    }

    private final BottomSheetBehavior.BottomSheetCallback mBottomSheetCallback = new BottomSheetBehavior.BottomSheetCallback() {
        @Override
        public void onStateChanged(@NonNull View bottomSheet,
                                   @BottomSheetBehavior.State int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
                BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        Log.e(TAG, "onDestroyView: 地图底部弹出框已销毁1");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy: 地图底部弹出框已销毁2");
    }
}
