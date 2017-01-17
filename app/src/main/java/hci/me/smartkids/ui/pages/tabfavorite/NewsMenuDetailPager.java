package hci.me.smartkids.ui.pages.tabfavorite;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.viewpagerindicator.TabPageIndicator;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

import hci.me.smartkids.R;
import hci.me.smartkids.base.BaseMenuDetailPager;
import hci.me.smartkids.model.NewsModel;
import hci.me.smartkids.ui.MainActivity;
import hci.me.smartkids.ui.pages.tabfavorite.favpages.NewsDetailsTabPager;

/**
 * 菜单详情-新闻
 * Author: Gary
 * Time: 17/1/15
 */

public class NewsMenuDetailPager extends BaseMenuDetailPager implements ViewPager.OnPageChangeListener{
    @ViewInject(value=R.id.vp_news)
    private ViewPager mViewPager;
    @ViewInject(value=R.id.ib_right)
    private ImageButton ibRight;
    @ViewInject(value=R.id.indicator)
    private TabPageIndicator indicator;
    private ArrayList<NewsDetailsTabPager> mTabPagers;
    private ArrayList<NewsModel.NewsTabData> mTabData;
    private NewsMenuDetailsAdapter adapter;
    public NewsMenuDetailPager(Activity activity, ArrayList<NewsModel.NewsTabData> newsMenuData) {
        super(activity);
        this.mTabData = newsMenuData;
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.pager_menu_news_details, null);
        x.view().inject(this,view);
//        mViewPager.addOnPageChangeListener(this);
        indicator.setOnPageChangeListener(this);//要给indicator设置监听
        return view;
    }

    @Override
    public void initData() {
        mTabPagers = new ArrayList<>();
        for (int i = 0;i<mTabData.size();i++){
            NewsDetailsTabPager pager = new NewsDetailsTabPager(mActivity, mTabData.get(i));//初始化内容集
            mTabPagers.add(pager);
        }
        adapter = new NewsMenuDetailsAdapter();
        mViewPager.setAdapter(adapter);
        indicator.setViewPager(mViewPager);//indicator要在viewPager设置完adapter以后，设置viewpager
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (position == 0)//如果滑到第一个内容，就开启侧边栏
            setSlidingMenuEnable(true);
        else
            setSlidingMenuEnable(false);
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

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

    /**
     * 内容集适配器
     */
    private class NewsMenuDetailsAdapter extends PagerAdapter{
        @Override
        public CharSequence getPageTitle(int position) {
            String title = mTabData.get(position).title;
            return title;
        }

        @Override
        public int getCount() {
            return mTabData.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            NewsDetailsTabPager newsDetailsTabPager = mTabPagers.get(position);
            View mRootView = newsDetailsTabPager.mRootView;
            container.addView(mRootView );
            newsDetailsTabPager.initData();
            return mRootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View)object);
        }
    }

    /**
     * 点击侧边小按钮，进行下一页切换
     * @param view
     */
    @Event(value = R.id.ib_right,type = View.OnClickListener.class)
    private void nextPage(View view){
        int cur = mViewPager.getCurrentItem();
        cur++;
        mViewPager.setCurrentItem(cur);
    }
}
