package hci.me.smartkids.ui.pages.fivetabs;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import hci.me.smartkids.base.BasePage;

/**
 * Saved Page
 * Author: Gary
 * Time: 17/1/13
 */

public class SavedPage extends BasePage {
    public SavedPage(Activity ctx) {
        super(ctx);
    }

    @Override
    public void initData() {
        Log.i("page","saved init");
        TextView tv = new TextView(mActivity);
        tv.setText("Saved");
        tv.setTextColor(Color.RED);
        tv.setGravity(Gravity.CENTER);
        flContent.addView(tv);
        tvTitle.setText("saved");
        ibLeftButton.setVisibility(View.VISIBLE);

    }
}
