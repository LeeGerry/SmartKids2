package hci.me.smartkids.ui.fragment;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;

import hci.me.smartkids.R;
import hci.me.smartkids.base.BaseFragment;
import hci.me.smartkids.model.NewsModel;
import hci.me.smartkids.ui.MainActivity;
import hci.me.smartkids.ui.pages.fivetabs.FavoritePage;

/**
 * 侧边栏fragment
 * Author: Gary
 * Time: 17/1/11
 */

public class LeftMenuFragment extends BaseFragment {
    private ArrayList<NewsModel.NewsMenuData> list;
    private int currentPosition;//记录当前menu位置
    private LeftMenuAdapter adapter;
    private ListView lvMenu;
    @Override
    public void initData() {

    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_left_menu, null);
        lvMenu = (ListView) view.findViewById(R.id.lv_left_menu);
        return view;
    }

    /**
     * 给外部点击事件调用，用来更新侧边栏listView数据,例如FavoritePage解析完json数据后
     * @param data
     */
    public void setMenuData(ArrayList<NewsModel.NewsMenuData> data) {
        currentPosition = 0;//当前选中位置归零
        list = data;
        adapter = new LeftMenuAdapter();
        lvMenu.setAdapter(adapter);
        lvMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentPosition = position;//点击了侧边item后，进行位置记录，以便状态进行更改
                adapter.notifyDataSetChanged();
                toggle();//侧边栏收起
                //侧边栏收起后要更新核心区域的内容FrameLayout,这里实现一个核心区域内容界面的基类BaseMenuDetailPager
                setCurrentDetailPager(position);
            }
        });
    }

    /**
     * 设置核心内容页的内容
     */
    private void setCurrentDetailPager(int position) {
        MainActivity activity = (MainActivity) mActivity;//得到mainActivity
        ContentFragment contentFragment = activity.getContentFragment();//根据MainActivity找到ContentFragment对象
        FavoritePage favoritePager = contentFragment.getFavoritePager();//在contentFragment对象中找到FavoratePager对象
        favoritePager.setCurrentDetailPager(position);//对favoritePager设置当前应显示的页
    }

    /**
     * 关闭或隐藏侧边栏
     */
    protected void toggle(){
        MainActivity main = (MainActivity) mActivity;
        SlidingMenu slidingMenu = main.getSlidingMenu();
        slidingMenu.toggle();
    }

    /**
     * 侧边栏ListView适配器
     */
    class LeftMenuAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(mActivity, R.layout.list_item_left_menu, null);
            TextView tv = (TextView) view.findViewById(R.id.tv_menu);
            NewsModel.NewsMenuData item = list.get(position);
            tv.setText(item.title);
            tv.setEnabled(currentPosition==position);//更改侧边栏item的选中状态
            return view;
        }
    }
}
