package com.example.find.ui.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.commonlib.adapter.CommonViewPagerAdapter;
import com.example.commonlib.base.BaseFullBottomSheetFragment;
import com.example.find.R;
import com.example.find.R2;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapBottomSheetFragmentSecond extends BaseFullBottomSheetFragment {

    private final String TAG = getClass().getSimpleName();
    @BindView(R2.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R2.id.viewpager)
    ViewPager viewPager;

    private ArrayList<String> tabTitleList;
    private ArrayList<Fragment> fragmentList;
    Unbinder unbinder;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.e(TAG, "onAttach:地图底部弹出框 context = " + context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map_bottom_sheet_fragment_second, container, false);
        unbinder = ButterKnife.bind(this, view);
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
        viewPager.setOffscreenPageLimit(2);
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
