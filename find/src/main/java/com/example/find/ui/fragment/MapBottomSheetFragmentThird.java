package com.example.find.ui.fragment;


import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;

import com.example.commonlib.adapter.CommonViewPagerAdapter;
import com.example.commonlib.base.BaseDialogFragment;
import com.example.commonlib.view.NoScrollViewPager;
import com.example.find.R;
import com.example.find.R2;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapBottomSheetFragmentThird extends BaseDialogFragment {


    private final String TAG = getClass().getSimpleName();
    @BindView(R2.id.appbar_layout)
    AppBarLayout appBarLayout;
    @BindView(R2.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R2.id.viewpager)
    NoScrollViewPager viewPager;

    private ArrayList<String> tabTitleList;
    private ArrayList<Fragment> fragmentList;

    @Override
    protected int bindLayout() {
        return R.layout.fragment_map_bottom_sheet_fragment_third;
    }

    @Override
    protected void initView() {
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
}
