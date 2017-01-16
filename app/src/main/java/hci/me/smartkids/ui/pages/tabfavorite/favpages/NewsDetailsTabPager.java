package hci.me.smartkids.ui.pages.tabfavorite.favpages;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import hci.me.smartkids.base.BaseMenuDetailPager;
import hci.me.smartkids.model.NewsModel;

/**
 * 新闻中心的各个页签对象
 * Author: Gary
 * Time: 17/1/15
 */

public class NewsDetailsTabPager extends BaseMenuDetailPager {
    private NewsModel.NewsTabData data;
    private TextView tv;
    public NewsDetailsTabPager(Activity activity, NewsModel.NewsTabData newsTabData) {
        super(activity);
        this.data = newsTabData;
    }

    @Override
    public View initView() {
        tv = new TextView(mActivity);
        tv.setTextColor(Color.RED);
        tv.setGravity(Gravity.CENTER);
        //tv.setText(data.title);//此处会空指针异常，因为initView在父类初始化时调用,那时候还没有数据；应放到initData中
        return tv;
    }

    @Override
    public void initData() {
        tv.setText(data.title);
    }
}
