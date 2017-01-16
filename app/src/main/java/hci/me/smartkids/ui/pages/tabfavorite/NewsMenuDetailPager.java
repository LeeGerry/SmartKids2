package hci.me.smartkids.ui.pages.tabfavorite;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import hci.me.smartkids.R;
import hci.me.smartkids.base.BaseMenuDetailPager;
import hci.me.smartkids.model.NewsModel;
import hci.me.smartkids.ui.pages.tabfavorite.favpages.NewsDetailsTabPager;

/**
 * 菜单详情-新闻
 * Author: Gary
 * Time: 17/1/15
 */

public class NewsMenuDetailPager extends BaseMenuDetailPager {
    private ViewPager mViewPager;
//    private View rootView;
    private ArrayList<NewsDetailsTabPager> mTabPagers;
    private ArrayList<NewsModel.NewsTabData> mTabData;
    private NewsMenuDetailsAdapter adapter;
    public NewsMenuDetailPager(Activity activity, ArrayList<NewsModel.NewsTabData> newsMenuData) {
        super(activity);

//        rootView = initView();
        this.mTabData = newsMenuData;
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.pager_menu_news_details, null);
        mViewPager = (ViewPager) view.findViewById(R.id.vp_news);
        return view;
    }

    @Override
    public void initData() {
        mTabPagers = new ArrayList<>();
        for (int i = 0;i<mTabData.size();i++){
            NewsDetailsTabPager pager = new NewsDetailsTabPager(mActivity, mTabData.get(i));
            mTabPagers.add(pager);
        }
        adapter = new NewsMenuDetailsAdapter();
        mViewPager.setAdapter(adapter);
    }

    private class NewsMenuDetailsAdapter extends PagerAdapter{

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
}
