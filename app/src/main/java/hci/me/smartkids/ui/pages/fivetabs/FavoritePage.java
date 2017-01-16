package hci.me.smartkids.ui.pages.fivetabs;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;

import hci.me.smartkids.base.BaseMenuDetailPager;
import hci.me.smartkids.base.BasePage;
import hci.me.smartkids.config.AppConfig;
import hci.me.smartkids.model.NewsModel;
import hci.me.smartkids.ui.MainActivity;
import hci.me.smartkids.ui.fragment.LeftMenuFragment;
import hci.me.smartkids.ui.pages.tabfavorite.InteractMenuDetailPager;
import hci.me.smartkids.ui.pages.tabfavorite.NewsMenuDetailPager;
import hci.me.smartkids.ui.pages.tabfavorite.PhotosMenuDetailPager;
import hci.me.smartkids.ui.pages.tabfavorite.TopicMenuDetailPager;
import hci.me.smartkids.utils.CacheUtils;

/**
 * Favorite Page
 * Author: Gary
 * Time: 17/1/13
 */

public class FavoritePage extends BasePage {
    private TextView tv;
    private NewsModel newsModel;
    private ArrayList<BaseMenuDetailPager> mMenuDetailPagers;
    public FavoritePage(Activity ctx) {
        super(ctx);
    }
    @Override
    public void initData() {
//        Log.i("page","fav init");
//        tv = new TextView(mActivity);
//        tv.setText("Favourite");
//        tv.setTextColor(Color.RED);
//        tv.setGravity(Gravity.CENTER);
//        flContent.addView(tv);
        tvTitle.setText("favourite");
        ibLeftButton.setVisibility(View.VISIBLE);
        //查找缓存中的信息
        String cacheData = CacheUtils.getCache(AppConfig.CATEGORY_URL, mActivity);
        if (!TextUtils.isEmpty(cacheData)){
            parserJson(cacheData);//有缓存，则进行解析，并更新侧边栏和界面信息
        }
        getDataFromServer();//再从服务器获取数据，更新侧边栏和界面信息，先用缓存可以优化用户体验
    }

    /**
     * 从服务器获取信息
     */
    private void getDataFromServer(){
        RequestParams params = new RequestParams(AppConfig.CATEGORY_URL);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //获取成功，则进行缓存
                CacheUtils.setCache(AppConfig.CATEGORY_URL, result, mActivity);
                //解析数据并更新界面
                parserJson(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    /**
     * 解析json数据
     * @param json
     */
    private void parserJson(String json){
        Gson gson = new Gson();
        newsModel = gson.fromJson(json, NewsModel.class);
//        Log.i("page", "parser..."+newsModel.data.size());
        //解析完数据后，根据MainActivity找到侧边栏
        MainActivity main = (MainActivity) mActivity;
        LeftMenuFragment menu = main.getLeftMenuFragment();
        //更新侧边栏的ListView信息
        menu.setMenuData(newsModel.data);
        //初始化四个菜单详情页
        mMenuDetailPagers = new ArrayList<>();
        Log.i("test",newsModel.data.get(0).children.toString());
        mMenuDetailPagers.add(new NewsMenuDetailPager(mActivity, newsModel.data.get(0).children));
        mMenuDetailPagers.add(new TopicMenuDetailPager(mActivity));
        mMenuDetailPagers.add(new PhotosMenuDetailPager(mActivity));
        mMenuDetailPagers.add(new InteractMenuDetailPager(mActivity));
        //解析完毕后要设置一个初始页面，把NewsMenuDetailPager设置为当前页
        setCurrentDetailPager(0);
    }

    /**
     * 设置菜单详情页
     */
    public void setCurrentDetailPager(int position){
        BaseMenuDetailPager currentPager = mMenuDetailPagers.get(position);//获取当前应显示的页
        View view = currentPager.mRootView;//获取当前页的根布局
        flContent.removeAllViews();//增加布局前要清楚所有子布局
        flContent.addView(view);//添加到帧布局
        currentPager.initData();//初始化页面数据
        String title = newsModel.data.get(position).title;
        if (!TextUtils.isEmpty(title))
            tvTitle.setText(title);
    }
}
