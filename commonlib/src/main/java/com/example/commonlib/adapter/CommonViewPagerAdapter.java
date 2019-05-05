package com.example.commonlib.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by smile on 2019/4/29.
 */

public class CommonViewPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<String> tabTitleList;
    private ArrayList<Fragment> fragmentList;

    public CommonViewPagerAdapter(FragmentManager fm, ArrayList<String> tabTitleList, ArrayList<Fragment> fragmentList) {
        super(fm);
        this.tabTitleList = tabTitleList;
        this.fragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitleList.get(position);
    }
}
