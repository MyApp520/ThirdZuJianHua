package com.example.group.ui;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.facade.callback.NavigationCallback;
import com.alibaba.android.arouter.launcher.ARouter;
import com.example.commonlib.arouter.AppModuleArouterPath;
import com.example.commonlib.arouter.ArouterActionManager;
import com.example.commonlib.arouter.ChatModuleArouterPath;
import com.example.commonlib.arouter.GroupModuleArouterPath;
import com.example.commonlib.base.BaseFragment;
import com.example.commonlib.bean.UserBean;
import com.example.commonlib.constants.ConfigConstants;
import com.example.commonlib.util.MyLog;
import com.example.group.R;
import com.example.group.R2;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
@Route(path = GroupModuleArouterPath.GROUP_MAIN_FRAGMENT)
public class GroupMainFragment extends BaseFragment {

    @BindView(R2.id.tv_enter_chat_activity)
    TextView tvEnterChatActivity;
    Unbinder unbinder;

    @Inject
    Context context;
    @Inject
    UserBean userBean;

    private final String TAG = getClass().getSimpleName();

    @Override
    protected int bindLayout() {
        return R.layout.fragment_group_main;
    }

    @Override
    protected void initView() {

    }

    @Override
    public void onStart() {
        super.onStart();
        MyLog.e(TAG, TAG + "---onStart()");
    }

    @Override
    public void onResume() {
        super.onResume();
        currentFragmentIsVisible = getUserVisibleHint();
        MyLog.e(TAG, TAG + "---onResume() isHidden() = " + isHidden() + ", currentFragmentIsVisible = " + currentFragmentIsVisible);
    }

    @Override
    public void onPause() {
        super.onPause();
        MyLog.e(TAG, TAG + "---onPause()");
    }

    @Override
    public void onStop() {
        super.onStop();
        MyLog.e(TAG, TAG + "---onStop()");
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        currentFragmentIsVisible = getUserVisibleHint();
        MyLog.e(TAG, TAG + "---setUserVisibleHint() isVisibleToUser = " + isVisibleToUser + ", currentFragmentIsVisible = " + currentFragmentIsVisible);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        currentFragmentIsVisible = getUserVisibleHint();
        MyLog.e(TAG, TAG + "---onHiddenChanged() hidden = " + hidden + ", currentFragmentIsVisible = " + currentFragmentIsVisible);
    }

    @OnClick(R2.id.tv_enter_chat_activity)
    public void onViewClicked() {
        ARouter.getInstance().build(ChatModuleArouterPath.CHAT_MAIN_ACTIVITY)
                .withString("name", "星火电子")
                .withInt("age", 31)
                .withBoolean("booStudent", false)
                .navigation(context, new NavigationCallback() {
                    @Override
                    public void onFound(Postcard postcard) {

                    }

                    @Override
                    public void onLost(Postcard postcard) {
                        Log.e(TAG, "onLost: CHAT_MAIN_ACTIVITY 跑丢了");
                    }

                    @Override
                    public void onArrival(Postcard postcard) {

                    }

                    @Override
                    public void onInterrupt(Postcard postcard) {
                        Log.e(TAG, "onInterrupt: 跳转失败，被拦截了 path = " + postcard.getPath());
                        // 被登录拦截了下来了，需要调转到登录页面，把参数跟被登录拦截下来的路径传递给登录页面，登录成功后再进行跳转被拦截的页面
                        Bundle bundle = new Bundle();
                        bundle.putString(ConfigConstants.AROUTER_TARGET_PATH, ChatModuleArouterPath.CHAT_MAIN_ACTIVITY);
                        ArouterActionManager.getInstance().startActivity(AppModuleArouterPath.LOGIN_ACTIVITY, bundle);
                    }
                });
    }
}
