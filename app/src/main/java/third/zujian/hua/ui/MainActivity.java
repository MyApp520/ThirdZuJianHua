package third.zujian.hua.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MenuItem;

import com.example.commonlib.arouter.ArouterActionManager;
import com.example.commonlib.arouter.ChatModuleArouterPath;
import com.example.commonlib.arouter.FindModuleArouterPath;
import com.example.commonlib.arouter.GroupModuleArouterPath;
import com.example.commonlib.arouter.MineModuleArouterPath;
import com.example.commonlib.base.BaseActivity;
import com.example.commonlib.bean.UserBean;
import com.example.commonlib.util.BottomNavigationViewHelper;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import third.zujian.hua.R;

public class MainActivity extends BaseActivity implements HasSupportFragmentInjector {

    private final String TAG = getClass().getSimpleName();

    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.bottomNavigation)
    BottomNavigationView bottomNavigationView;

    @Inject
    DispatchingAndroidInjector<Fragment> supportFragmentInjector;
    @Inject
    Context context;
    @Inject
    UserBean userBean;

    private MenuItem menuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
        Log.e(TAG, "onCreate: userBean = " + userBean + ", context = " + context);
    }

    private void initView() {
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //设置底部导航栏菜单选项平均分布：如果Android28以上，调用：bottomNavigationView.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (menuItem != null) {
                    menuItem.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                menuItem = bottomNavigationView.getMenu().getItem(position);
                menuItem.setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        List<Fragment> list = new ArrayList<>();
        list.add(ArouterActionManager.getInstance().getFragmentInstance(ChatModuleArouterPath.CHAT_MAIN_FRAGMENT));
        list.add(ArouterActionManager.getInstance().getFragmentInstance(GroupModuleArouterPath.GROUP_MAIN_FRAGMENT));
        list.add(ArouterActionManager.getInstance().getFragmentInstance(FindModuleArouterPath.FIND_MAIN_FRAGMENT));
        list.add(ArouterActionManager.getInstance().getFragmentInstance(MineModuleArouterPath.MINE_MAIN_FRAGMENT));
        viewPagerAdapter.setList(list);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(3);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            menuItem = item;
            switch (item.getItemId()) {
                case R.id.chat:
                    viewPager.setCurrentItem(0, false);//第二个参数表示是否跳转到指定这个界面时，是直接跳转还是一个一个页面滑动过去
                    return true;
                case R.id.group:
                    viewPager.setCurrentItem(1, false);
                    return true;
                case R.id.find:
                    viewPager.setCurrentItem(2, false);
                    return true;
                case R.id.mine:
                    viewPager.setCurrentItem(3, false);
                    return true;
            }
            return false;
        }
    };

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return supportFragmentInjector;
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> list;

        public void setList(List<Fragment> list) {
            this.list = list;
        }

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list != null ? list.size() : 0;
        }
    }
}
