package hci.me.smartkids.ui.pages.fivetabs;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import hci.me.smartkids.base.BasePage;

/**
 * Setting Page
 * Author: Gary
 * Time: 17/1/13
 */

public class SettingPage extends BasePage {
    public SettingPage(Activity ctx) {
        super(ctx);
    }

    @Override
    public void initData() {
        Log.i("page","setting init");
        TextView tv = new TextView(mActivity);
        tv.setText("Setting");
        tv.setTextColor(Color.RED);
        tv.setGravity(Gravity.CENTER);
        flContent.addView(tv);
        tvTitle.setText("setting");
        ibLeftButton.setVisibility(View.GONE);
    }
}
