package hci.me.smartkids.ui.pages.fivetabs;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import hci.me.smartkids.base.BasePage;

/**
 * 首页
 * Author: Gary
 * Time: 17/1/13
 */

public class HomePage extends BasePage {
    public HomePage(Activity ctx) {
        super(ctx);
    }

    @Override
    public void initData() {
        Log.i("page","home init");
        TextView tv = new TextView(mActivity);
        tv.setText("main page");
        tv.setTextColor(Color.RED);
        tv.setGravity(Gravity.CENTER);
        flContent.addView(tv);
        tvTitle.setText("main");
        ibLeftButton.setVisibility(View.GONE);
    }
}
