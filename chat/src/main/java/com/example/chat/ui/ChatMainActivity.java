package com.example.chat.ui;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.chat.R;
import com.example.chat.R2;
import com.example.commonlib.arouter.ArouterActionManager;
import com.example.commonlib.arouter.ChatModuleArouterPath;
import com.example.commonlib.arouter.FindModuleArouterPath;
import com.example.commonlib.base.BaseActivity;
import com.example.commonlib.bean.UserBean;
import com.example.commonlib.util.ShowToast;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;


// 在支持路由的页面上添加注解(必选)
// 这里的路径需要注意的是至少需要有两级，/xx/xx
@Route(path = ChatModuleArouterPath.CHAT_MAIN_ACTIVITY)
public class ChatMainActivity extends BaseActivity {

    @BindView(R2.id.tv_chat_toast1)
    TextView tvChatToast1;
    @BindView(R2.id.tv_chat_toast2)
    TextView tvChatToast2;

    private final String TAG = getClass().getSimpleName();

    @Autowired(name = "name")
    String name;
    @Autowired(name = "age")
    int age;
    @Autowired(name = "booStudent")
    boolean booStudent;
    @Autowired(name = "token")
    String userToken;

    @Inject
    UserBean userBean;

    @Override
    protected int bindLayout() {
        return R.layout.activity_chat_main;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "ChatMainActivity onResume: userBean = " + userBean);
    }

    @OnClick({R2.id.tv_chat_toast1, R2.id.tv_chat_toast2})
    public void onViewClicked(View view) {
        int viewId = view.getId();
        if (R.id.tv_chat_toast1 == viewId) {
            ShowToast.showToast(getApplicationContext(), "name = " + name + ", userToken = " + userToken);
            ArouterActionManager.getInstance().startActivity(FindModuleArouterPath.FIND_MAIN_ACTIVITY);
        } else if (R.id.tv_chat_toast2 == viewId) {
            ShowToast.showToast(getApplicationContext(), "name = " + name + ", age = " + age + ", booStudent = " + booStudent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
