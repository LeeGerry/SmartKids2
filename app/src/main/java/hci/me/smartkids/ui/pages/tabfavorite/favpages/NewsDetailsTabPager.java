package hci.me.smartkids.ui.pages.tabfavorite.favpages;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import hci.me.smartkids.ui.NewsPageActivity;
import hci.me.smartkids.ui.view.NewsTopImageViewPager;
import hci.me.smartkids.ui.view.PollToRefreshListView;
import hci.me.smartkids.utils.CacheUtils;
import hci.me.smartkids.utils.PrefUtils;

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
    private PollToRefreshListView lvNewsList;
    @ViewInject(R.id.indicator)
    private CirclePageIndicator indicator;
    @ViewInject(R.id.tv_top_news_title)
    private TextView tvTopNewsTitle;
    @ViewInject(value = R.id.vp_center_news)
    private NewsTopImageViewPager vpCenterNews;

    private NewsListAdapter newsListAdapter;
    private String mUrlMore;
    public NewsDetailsTabPager(Activity activity, NewsModel.NewsTabData newsTabData) {
        super(activity);
        this.data = newsTabData;
        this.dataUrl = AppConfig.SERVER_ADDR + data.url;
    }
    private final String READ_IDS = "readed_ids";
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
        View header = View.inflate(mActivity, R.layout.list_item_header, null);
        x.view().inject(this, header);//此时也要把头布局进行注入
        lvNewsList.addHeaderView(header);
        // 5.前端界面设置回调
        lvNewsList.setOnRefreshListener(new PollToRefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //刷新数据
                getDataFromServer();
            }

            @Override
            public void loadMore() {
                if (mUrlMore!=null){//有下一页
                    getMoreDataFromServer();
                }else{
                    Toast.makeText(mActivity, "no more data",Toast.LENGTH_LONG).show();
                    lvNewsList.onRefreshComplete(true);//没有更多数据时也要调用该回调，隐藏footer布局
                }
            }
        });

        lvNewsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int headViewsCount = lvNewsList.getHeaderViewsCount();
                position -= headViewsCount;
                NewTabModel.NewsData data = newsList.get(position);
                String readIds = PrefUtils.getString(mActivity, READ_IDS,"");
                if (!readIds.contains(data.id + "")){//把读过的item 的ID号存起来
                    readIds = readIds + data.id;
                    PrefUtils.setString(mActivity, READ_IDS, readIds);;
                }
                TextView title = (TextView) view.findViewById(R.id.tv_title);
                title.setTextColor(Color.GRAY);//把点过的ITEM颜色title改为灰色，在adapter中也要改
                Intent intent = new Intent(mActivity, NewsPageActivity.class);
                String url = data.url.replace("10.0.2.2","192.168.1.6");
                intent.putExtra("url",url);
                mActivity.startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void initData() {
        String cache = CacheUtils.getCache(dataUrl, mActivity);
        if (!TextUtils.isEmpty(cache)){
            parserJson(cache, false);
        }
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
                parserJson(result, false);

                // 7. 收起下拉刷新控件
                lvNewsList.onRefreshComplete(true);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ex.printStackTrace();
                Toast.makeText(mActivity, ex.getMessage(), Toast.LENGTH_LONG).show();
                // 7. 收起下拉刷新控件
                lvNewsList.onRefreshComplete(false);
            }

            @Override
            public void onFinished() {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }
        });
    }

    /**
     * get more, 不用缓存了，只需要第一页缓存
     */
    private void getMoreDataFromServer() {
        RequestParams params = new RequestParams(mUrlMore);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //解析数据并更新界面
                parserJson(result, true);
                lvNewsList.onRefreshComplete(true);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ex.printStackTrace();
                Toast.makeText(mActivity, ex.getMessage(), Toast.LENGTH_LONG).show();
                lvNewsList.onRefreshComplete(true);
            }

            @Override
            public void onFinished() {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }
        });
    }

    private void parserJson(String result, boolean isMore) {
        Gson gson = new Gson();
        newTabModel = gson.fromJson(result, NewTabModel.class);
        //Log.i("page",newTabModel.toString());
        topnews = newTabModel.data.topnews;
        newsList = newTabModel.data.news;
        String moreUrl = newTabModel.data.more;
        if (!TextUtils.isEmpty(moreUrl)){
            mUrlMore = AppConfig.SERVER_ADDR + moreUrl;
        }else {
            mUrlMore = null;
        }

        if (!isMore){//不是加载更多
            if (topnews == null || newsList == null)    return;
            newsListAdapter =  new NewsListAdapter();
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

        }else {
            ArrayList<NewTabModel.NewsData> moreData = newTabModel.data.news;
            newsList.addAll(moreData);
            newsListAdapter.notifyDataSetChanged();
        }

    }

    /**
     * 头条新闻图片轮播适配器
     */
    public class CenterNewsAdapter extends PagerAdapter {

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
            ImageView iv = new ImageView(mActivity);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            //通过ImageOptions.Builder().set方法设置图片的属性
            ImageOptions options = new ImageOptions.Builder()
                            .setLoadingDrawableId(R.mipmap.news_pic_default)
                            .setFailureDrawableId(R.mipmap.news_pic_default)
                            .build();
            String url = topnews.get(position).topimage;
            url = url.replace("10.0.2.2","192.168.1.6");
            x.image().bind(iv, url, options);
            container.addView(iv);
            return iv;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    /**
     * 新闻列表listView适配器
     */
    private class NewsListAdapter extends BaseAdapter{
        ImageOptions options;
        String ids ;
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
            ids = PrefUtils.getString(mActivity, READ_IDS, "");
            if (ids.contains(item.id+"")){
                holder.tvTitle.setTextColor(Color.GRAY);
            }else {
                holder.tvTitle.setTextColor(Color.BLACK);
            }
            String url = item.listimage;
            url = url.replace("10.0.2.2","192.168.1.6");
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
