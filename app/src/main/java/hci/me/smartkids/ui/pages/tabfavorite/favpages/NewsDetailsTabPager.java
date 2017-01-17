package hci.me.smartkids.ui.pages.tabfavorite.favpages;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.viewpagerindicator.CirclePageIndicator;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

import hci.me.smartkids.R;
import hci.me.smartkids.base.BaseMenuDetailPager;
import hci.me.smartkids.config.AppConfig;
import hci.me.smartkids.model.NewTabModel;
import hci.me.smartkids.model.NewsModel;
import hci.me.smartkids.ui.view.NewsTopImageViewPager;
import hci.me.smartkids.utils.CacheUtils;

/**
 * 新闻中心的各个页签对象
 * Author: Gary
 * Time: 17/1/15
 */

public class NewsDetailsTabPager extends BaseMenuDetailPager {
    private NewsModel.NewsTabData data;
    private NewTabModel newTabModel;//新闻中心的对象
    private ArrayList<NewTabModel.TopData> topnews;//头条新闻
    private ArrayList<NewTabModel.NewsData> newsList;//listView新闻列表
    private String dataUrl;
    private CenterNewsAdapter adapter;
    @ViewInject(R.id.lv_news_list)
    private ListView lvNewsList;
    @ViewInject(R.id.indicator)
    private CirclePageIndicator indicator;
    @ViewInject(R.id.tv_top_news_title)
    private TextView tvTopNewsTitle;
    @ViewInject(value = R.id.vp_center_news)
    private NewsTopImageViewPager vpCenterNews;

    public NewsDetailsTabPager(Activity activity, NewsModel.NewsTabData newsTabData) {
        super(activity);
        this.data = newsTabData;
        this.dataUrl = AppConfig.SERVER_ADDR + data.url;
    }

    @Override
    public View initView() {
//        tv = new TextView(mActivity);
//
//        tv.setTextColor(Color.RED);
//        tv.setGravity(Gravity.CENTER);
        //tv.setText(data.title);//此处会空指针异常，因为initView在父类初始化时调用,那时候还没有数据；应放到initData中
//        return tv;
        View view = View.inflate(mActivity, R.layout.pager_center_details, null);
        x.view().inject(this, view);
        return view;
    }

    @Override
    public void initData() {
        //tv.setText(data.title);
        getDataFromServer();
    }

    private void getDataFromServer() {
        RequestParams params = new RequestParams(dataUrl);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                //获取成功，则进行缓存
                CacheUtils.setCache(dataUrl, result, mActivity);
                //解析数据并更新界面
                parserJson(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onFinished() {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }
        });
    }

    private void parserJson(String result) {
        Gson gson = new Gson();
        newTabModel = gson.fromJson(result, NewTabModel.class);
        //Log.i("page",newTabModel.toString());
        topnews = newTabModel.data.topnews;
        newsList = newTabModel.data.news;

        if (topnews == null || newsList == null)    return;
        NewsListAdapter newsListAdapter =  new NewsListAdapter();
        lvNewsList.setAdapter(newsListAdapter);
        adapter = new CenterNewsAdapter();
        vpCenterNews.setAdapter(adapter);
        indicator.setViewPager(vpCenterNews);
        indicator.setSnap(true);
        //事件设置给indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                String title = topnews.get(position).title;
                if (!TextUtils.isEmpty(title))
                    tvTopNewsTitle.setText(title);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tvTopNewsTitle.setText(topnews.get(0).title);
        indicator.onPageSelected(0);//默认让第一个选中，解决页面销毁后重新初始化，indicator仍然保留上次位置

    }

    private class CenterNewsAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return topnews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
//            TextView t = new TextView(mActivity);
            ImageView iv = new ImageView(mActivity);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            //通过ImageOptions.Builder().set方法设置图片的属性
            ImageOptions options = new ImageOptions.Builder()
                            .setLoadingDrawableId(R.mipmap.news_pic_default)
                            .setFailureDrawableId(R.mipmap.news_pic_default)
                            .build();
            String url = topnews.get(position).topimage;
            url = url.replace("10.0.2.2","192.168.142.151");
            x.image().bind(iv, url, options);
//            t.setTextColor(Color.RED);
//            t.setTextSize(25);
//            t.setText(topnews.get(position).title);
//            container.addView(t);
            container.addView(iv);
            return iv;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
    private class NewsListAdapter extends BaseAdapter{
        ImageOptions options;
        public NewsListAdapter(){
            options = new ImageOptions.Builder()
                    .setLoadingDrawableId(R.mipmap.image_demo)
                    .setFailureDrawableId(R.mipmap.image_demo)
                    .build();
        }
        @Override
        public int getCount() {
            return newsList.size();
        }

        @Override
        public Object getItem(int position) {
            return newsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            if (convertView == null){
                convertView = View.inflate(mActivity, R.layout.news_list_item,null);
                holder = new ViewHolder();
                holder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
                holder.tvTime = (TextView) convertView.findViewById(R.id.tv_date);
                holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
                convertView.setTag(holder);
            }else {
                holder = (ViewHolder) convertView.getTag();
            }
            NewTabModel.NewsData item = (NewTabModel.NewsData) getItem(position);
            holder.tvTitle.setText(item.title);
            holder.tvTime.setText(item.pubdate);

            String url = item.listimage;
            url = url.replace("10.0.2.2","192.168.142.151");
            x.image().bind(holder.ivIcon, url, options);
            return convertView;
        }

    }
    private static class ViewHolder{
        ImageView ivIcon;
        TextView tvTitle;
        TextView tvTime;
    }
}
