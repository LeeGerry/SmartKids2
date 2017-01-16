package hci.me.smartkids.ui.pages.tabfavorite;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import hci.me.smartkids.base.BaseMenuDetailPager;

/**
 * 菜单详情-组图
 * Author: Gary
 * Time: 17/1/15
 */

public class PhotosMenuDetailPager extends BaseMenuDetailPager {
    public PhotosMenuDetailPager(Activity activity) {
        super(activity);
    }

    @Override
    public View initView() {
        TextView tv = new TextView(mActivity);
        tv.setText("菜单详情-组图");
        tv.setTextColor(Color.RED);
        tv.setGravity(Gravity.CENTER);
        return tv;
    }
}
