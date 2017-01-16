package hci.me.smartkids.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import hci.me.smartkids.R;
import hci.me.smartkids.ui.fragment.ContentFragment;
import hci.me.smartkids.ui.fragment.LeftMenuFragment;

/**
 * Author: Gary
 * Time: 17/1/10
 */
public class MainActivity extends SlidingFragmentActivity{
    private static final String TAG_FRAGMENT_MENU = "tag_fragment_menu", TAG_FRAGMENT_MAIN = "tag_fragment_main";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setBehindContentView(R.layout.left_menu);
        SlidingMenu menu = getSlidingMenu();
//        menu.setSecondaryMenu(R.layout.right_menu);//设置右侧menu，此处只需左侧即可
//        menu.setMode(SlidingMenu.LEFT_RIGHT);
//        menu.setFadeDegree(.3f);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menu.setBehindOffset(300);
        initFragment();
    }

    /**
     * 初始化fragment
     */
    private void initFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();//开始事务
        transaction.replace(R.id.fl_left, new LeftMenuFragment(), TAG_FRAGMENT_MENU);//用于fragment替换
        transaction.replace(R.id.fl_main, new ContentFragment(),TAG_FRAGMENT_MAIN);
        transaction.commit();//提交事务
    }

    /**
     * 获取侧边栏fragment对象
     * @return
     */
    public LeftMenuFragment getLeftMenuFragment(){
        FragmentManager fm = getSupportFragmentManager();
        LeftMenuFragment menu = (LeftMenuFragment) fm.findFragmentByTag(TAG_FRAGMENT_MENU);
        return menu;
    }
    /**
     * 获取Content fragment对象
     * @return
     */
    public ContentFragment getContentFragment(){
        FragmentManager fm = getSupportFragmentManager();
        ContentFragment main = (ContentFragment) fm.findFragmentByTag(TAG_FRAGMENT_MAIN);
        return main;
    }
}
