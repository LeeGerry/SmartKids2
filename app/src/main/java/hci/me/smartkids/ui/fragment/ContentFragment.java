package hci.me.smartkids.ui.fragment;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;

import hci.me.smartkids.R;
import hci.me.smartkids.base.BaseFragment;
import hci.me.smartkids.base.BasePage;
import hci.me.smartkids.ui.MainActivity;
import hci.me.smartkids.ui.pages.fivetabs.FavoritePage;
import hci.me.smartkids.ui.pages.fivetabs.HomePage;
import hci.me.smartkids.ui.pages.fivetabs.SavedPage;
import hci.me.smartkids.ui.pages.fivetabs.SearchPage;
import hci.me.smartkids.ui.pages.fivetabs.SettingPage;
import hci.me.smartkids.ui.view.NoScrollViewPager;

/**
 * 内容fragment
 * Author: Gary
 * Time: 17/1/11
 */

public class ContentFragment extends BaseFragment {
    private NoScrollViewPager mViewPager;
    private ArrayList<BasePage> mPages;//5个标签页的集合
    private ContentAdapter adapter;
    private RadioGroup rgTabs;
    @Override
    public void initData() {
        mPages = new ArrayList<>();
        mPages.add(new HomePage(mActivity));
        mPages.add(new FavoritePage(mActivity));
        mPages.add(new SavedPage(mActivity));
        mPages.add(new SearchPage(mActivity));
        mPages.add(new SettingPage(mActivity));
        adapter = new ContentAdapter();
        mViewPager.setAdapter(adapter);

        rgTabs.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rbTab1:
                        mViewPager.setCurrentItem(0, false);//false表示是否具有滑动效果
                        break;
                    case R.id.rbTab2:
                        mViewPager.setCurrentItem(1, false);//false表示是否具有滑动效果
                        break;
                    case R.id.rbTab3:
                        mViewPager.setCurrentItem(2, false);//false表示是否具有滑动效果
                        break;
                    case R.id.rbTab4:
                        mViewPager.setCurrentItem(3, false);//false表示是否具有滑动效果
                        break;
                    case R.id.rbTab5:
                        mViewPager.setCurrentItem(4, false);//false表示是否具有滑动效果
                        break;
                }
            }
        });
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                BasePage pager = mPages.get(position);
                pager.initData();
                if (position == 0 || position == mPages.size()-1){
                    //如果是第一个TAB或者最后一个TAB，就禁用侧边栏
                    setSlidingMenuEnable(false);
                }else {
                    //否则就开启
                    setSlidingMenuEnable(true);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mPages.get(0).initData();//刚开始调用第一个page初始化
        setSlidingMenuEnable(false);
    }

    /**
     * 开启/禁用侧边栏
     * @param enable
     */
    private void setSlidingMenuEnable(boolean enable){
        MainActivity main = (MainActivity)mActivity;
        SlidingMenu slidingMenu = main.getSlidingMenu();
        if (enable) slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        else        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_content, null);
        mViewPager = (NoScrollViewPager) view.findViewById(R.id.vp_content);
        rgTabs = (RadioGroup) view.findViewById(R.id.rg_tabs);
        return view;
    }
    class ContentAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return mPages.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            BasePage basePage = mPages.get(position);//找到当前加载的page对象
            View view = basePage.rootView;
            //basePage.initData();//加载数据，才能显示;
            // viewPage会默认加载下一个页面，为了节省流量和性能，不在此处调用初始化数据的方法
            // 放到页签被选择的时候加载当前数据
            container.addView(view);//增加给容器
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View)object);
        }
    }

    /**
     * 获取第二个tab页面，FavoritePage . 供外部使用.例如在侧边栏点击后，替换其内容
     * @return
     */
    public FavoritePage getFavoritePager(){
        FavoritePage pager = (FavoritePage) mPages.get(1);
        return pager;
    }


}
