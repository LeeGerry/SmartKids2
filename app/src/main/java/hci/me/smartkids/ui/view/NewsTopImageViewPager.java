package hci.me.smartkids.ui.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Author: Gary
 * Time: 17/1/16
 */

public class NewsTopImageViewPager extends ViewPager {
    public NewsTopImageViewPager(Context context) {
        super(context);
    }

    public NewsTopImageViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private int startX,startY;
    /**
     * 上下滑动需要拦截 getParent().requestDisallowInterceptTouchEvent(false);
     * 向右滑动并且当前是第一个图，需要拦截
     * 向左滑动并且当前是最后一个图，需要拦截
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        getAdapter().notifyDataSetChanged();//在这里需要添加adapter刷新，避免加载更多后下拉刷新报错
        getParent().requestDisallowInterceptTouchEvent(true);
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN://按下
                startX = (int) ev.getX();
                startY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE://滑动
                int endX = (int) ev.getX();
                int endY = (int) ev.getY();
                int dx = endX - startX;
                int dy = endY - startY;
                if (Math.abs(dy) < Math.abs(dx)){//Y轴变化距离小于X轴，则为左右滑动
                    int current = getCurrentItem();//当前条目位置
                    if(dx>0){//向右滑动
                        if(current == 0){//当前是第一个，则需要拦截
                            getParent().requestDisallowInterceptTouchEvent(false);
                        }
                    }else {//向左滑动
                        if (current == getAdapter().getCount()-1){//当前是最后一个，需要拦截
                            getParent().requestDisallowInterceptTouchEvent(false);
                        }
                    }
                }else {//否则为上下滑动，需要拦截
                    getParent().requestDisallowInterceptTouchEvent(false);

                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

}
