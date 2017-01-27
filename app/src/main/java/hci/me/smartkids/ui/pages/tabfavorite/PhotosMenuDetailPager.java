package hci.me.smartkids.ui.pages.tabfavorite;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

import hci.me.smartkids.R;
import hci.me.smartkids.base.BaseMenuDetailPager;
import hci.me.smartkids.config.AppConfig;
import hci.me.smartkids.model.GroupNewsMode;
import hci.me.smartkids.utils.CacheUtils;

/**
 * 菜单详情-组图
 * Author: Gary
 * Time: 17/1/15
 */

public class PhotosMenuDetailPager extends BaseMenuDetailPager {
    @ViewInject(R.id.lv_group_news)
    private ListView lvGroupNews;
    @ViewInject(R.id.gv_group_news)
    private GridView gvGroupNews;
    private ArrayList<GroupNewsMode.PhotoNews> news;
    private GroupNewsAdapter adapter;
    public PhotosMenuDetailPager(Activity activity) {
        super(activity);
    }

    @Override
    public View initView() {
        View rootView = View.inflate(mActivity, R.layout.pager_menu_news_group, null);
        x.view().inject(this, rootView);
        return rootView;
    }

    @Override
    public void initData() {
        String cache = CacheUtils.getCache(AppConfig.PHOTO_GROUP_URL, mActivity);
        if (!TextUtils.isEmpty(cache))
            processData(cache);
        getFromServer();
    }

    private void processData(String result) {
        Gson gson = new Gson();
        GroupNewsMode groupsNews = gson.fromJson(result, GroupNewsMode.class);
        news = groupsNews.data.news;
        Log.i("from",news.size()+"");
        adapter = new GroupNewsAdapter();
        //listView和gridView结构完全一致，可以共用一个adapter
        lvGroupNews.setAdapter(adapter);
        gvGroupNews.setAdapter(adapter);
    }

    public void getFromServer() {
        RequestParams params = new RequestParams(AppConfig.PHOTO_GROUP_URL);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if(!TextUtils.isEmpty(result)){
                    CacheUtils.setCache(AppConfig.PHOTO_GROUP_URL, result,mActivity);
                    processData(result);
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ex.printStackTrace();
                Toast.makeText(mActivity,ex.getMessage(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }
    class GroupNewsAdapter extends BaseAdapter{
        ImageOptions options;
        public GroupNewsAdapter() {
            //通过ImageOptions.Builder().set方法设置图片的属性
            options = new ImageOptions.Builder()
                    .setLoadingDrawableId(R.mipmap.news_pic_default)
                    .setFailureDrawableId(R.mipmap.news_pic_default)
                    .build();
        }

        @Override
        public int getCount() {
            return news.size();
        }

        @Override
        public GroupNewsMode.PhotoNews getItem(int position) {
            return news.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
           ViewHolder holder;
            if (convertView == null){
                convertView = View.inflate(mActivity, R.layout.group_list_item,null);
                holder = new ViewHolder();
                holder.ivNews = (ImageView) convertView.findViewById(R.id.iv_image);
                holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
                convertView.setTag(holder);
            }else {
                holder = (ViewHolder) convertView.getTag();
            }
            GroupNewsMode.PhotoNews model = getItem(position);
            holder.tvTitle.setText(model.title);

            String url = model.listimage;
            url = url.replace("10.0.2.2",AppConfig.CURR_SERVER_IP);
            x.image().bind(holder.ivNews, url, options);
            return convertView;
        }
    }
    private static class ViewHolder{
        TextView tvTitle;
        ImageView ivNews;
    }
}
