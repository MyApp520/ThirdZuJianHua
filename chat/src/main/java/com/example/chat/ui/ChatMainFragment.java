package com.example.chat.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.chat.R;
import com.example.commonlib.arouter.ChatModuleArouterPath;
import com.example.commonlib.base.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 */
@Route(path = ChatModuleArouterPath.CHAT_MAIN_FRAGMENT)
public class ChatMainFragment extends BaseFragment {


    public static ChatMainFragment newInstance() {
        // Required empty public constructor
        ChatMainFragment chatMainFragment = new ChatMainFragment();
        return chatMainFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_chat_main, container, false);
    }
}
