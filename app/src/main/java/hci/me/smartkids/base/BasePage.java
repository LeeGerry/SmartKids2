package hci.me.smartkids.base;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import hci.me.smartkids.R;
import hci.me.smartkids.ui.MainActivity;

/**
 * 五个标签页的基类
 * Author: Gary
 * Time: 17/1/13
 */

public class BasePage {
    public Activity mActivity;
    public TextView tvTitle;
    public ImageButton ibLeftButton;
    public FrameLayout flContent;
    public View rootView;
    public BasePage(Activity ctx){
        this.mActivity = ctx;
        rootView = initView();
    }

    /**
     * 初始化布局
     * @return
     */
    public View initView() {
        View view = View.inflate(mActivity, R.layout.base_page, null);
        flContent = (FrameLayout) view.findViewById(R.id.fl_content);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        ibLeftButton = (ImageButton) view.findViewById(R.id.ib_left_menu);
        ibLeftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle();
            }
        });
        return view;
    }

    /**
     * 初始化数据
     */
    public void initData(){

    }
    protected void toggle(){
        MainActivity main = (MainActivity) mActivity;
        SlidingMenu slidingMenu = main.getSlidingMenu();
        slidingMenu.toggle();
    }
}
