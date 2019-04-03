package com.example.group.ui;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.group.R;
import com.example.group.R2;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
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
    UserBean userBean;

    private final String TAG = getClass().getSimpleName();
    private Context context;

    public static GroupMainFragment newInstance() {
        // Required empty public constructor
        GroupMainFragment groupMainFragment = new GroupMainFragment();
        return groupMainFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_group_main, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "GroupMainFragment onResume: userBean = " + userBean);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
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
