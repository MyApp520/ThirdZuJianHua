package com.example.chat.ui;


import android.support.v4.app.Fragment;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.chat.R;
import com.example.commonlib.arouter.ChatModuleArouterPath;
import com.example.commonlib.base.BaseFragment;
import com.example.commonlib.util.MyLog;

/**
 * A simple {@link Fragment} subclass.
 */
@Route(path = ChatModuleArouterPath.CHAT_MAIN_FRAGMENT)
public class ChatMainFragment extends BaseFragment {

    private final String TAG = getClass().getSimpleName();

    @Override
    protected int bindLayout() {
        return R.layout.fragment_chat_main;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void onLazyInitEvent() {

    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        MyLog.e(TAG, TAG + "---onSupportVisible()  chat界面可见了");
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        MyLog.e(TAG, TAG + "---onSupportInvisible() chat界面不可见");
    }
}
