package com.example.find.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.commonlib.arouter.FindModuleArouterPath;
import com.example.commonlib.base.BaseFragment;
import com.example.find.R;

/**
 * A simple {@link Fragment} subclass.
 */
@Route(path = FindModuleArouterPath.FIND_MAIN_FRAGMENT)
public class FindMainFragment extends BaseFragment {


    public FindMainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_find_main, container, false);
    }

}
