package com.example.chat.ui;


import android.support.v4.app.Fragment;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.chat.R;
import com.example.commonlib.arouter.ChatModuleArouterPath;
import com.example.commonlib.base.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 */
@Route(path = ChatModuleArouterPath.CHAT_MAIN_FRAGMENT)
public class ChatMainFragment extends BaseFragment {


    @Override
    protected int bindLayout() {
        return R.layout.fragment_chat_main;
    }

    @Override
    protected void initView() {

    }
}
