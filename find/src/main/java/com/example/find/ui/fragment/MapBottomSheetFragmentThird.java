package com.example.find.ui.fragment;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.example.commonlib.adapter.CommonViewPagerAdapter;
import com.example.commonlib.view.NoScrollViewPager;
import com.example.find.R;
import com.example.find.R2;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapBottomSheetFragmentThird extends DialogFragment {


    private final String TAG = getClass().getSimpleName();
    @BindView(R2.id.appbar_layout)
    AppBarLayout appBarLayout;
    @BindView(R2.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R2.id.viewpager)
    NoScrollViewPager viewPager;

    Unbinder unbinder;

    private Activity activity;
    private ArrayList<String> tabTitleList;
    private ArrayList<Fragment> fragmentList;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.e(TAG, "地图底部弹出框 MapBottomSheetFragmentThird onAttach context = " + context);
        activity = (Activity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.Theme_Design_Light_BottomSheetDialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map_bottom_sheet_fragment_third, container, false);
        unbinder = ButterKnife.bind(this, view);

        Log.e(TAG, "地图底部弹出框 MapBottomSheetFragmentThird onCreateView: ");
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        Log.e(TAG, "地图底部弹出框 MapBottomSheetFragmentThird onActivityCreated: ");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e(TAG, "地图底部弹出框 MapBottomSheetFragmentThird onStart: ");
        DisplayMetrics dm = null;
        WindowManager windowManager = null;
        if (activity != null) {
            windowManager = activity.getWindowManager();
        }
        if (windowManager != null) {
            dm = new DisplayMetrics();
            windowManager.getDefaultDisplay().getMetrics(dm);
        }

        Window window = null;
        Dialog dialog = getDialog();
        if (dialog != null) {
            window = getDialog().getWindow();
        }
        if (window != null) {
            //默认开始的时候关闭软键盘
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            WindowManager.LayoutParams params = window.getAttributes();
            params.gravity = Gravity.BOTTOM;
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            if (dm == null || dm.heightPixels < 320) {
                params.height = 320;
            } else {
                params.height = dm.heightPixels - 360;
            }
            window.setAttributes(params);
        }
    }

    private void initView() {
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());

        tabTitleList = new ArrayList<>();
        tabTitleList.add("解放军");
        tabTitleList.add("八路军");
        tabTitleList.add("新四军");

        fragmentList = new ArrayList<>();
        fragmentList.add(PoliceAlarmVideoFragment.newInstance(99));
        fragmentList.add(PoliceAlarmVideoFragment.newInstance(88));
        fragmentList.add(PoliceAlarmVideoFragment.newInstance(2));

        viewPager.setNoScroll(true);
        viewPager.setAdapter(new CommonViewPagerAdapter(getChildFragmentManager(), tabTitleList, fragmentList));
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setOffscreenPageLimit(2);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // 默认切换的时候，会有一个过渡动画，设为false后，取消动画，直接显示
                viewPager.setCurrentItem(tab.getPosition(), false);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

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
