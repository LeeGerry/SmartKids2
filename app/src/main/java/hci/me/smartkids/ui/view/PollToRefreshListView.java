package hci.me.smartkids.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import hci.me.smartkids.R;

/**
 * Author: Gary
 * Time: 17/1/18
 */

public class PollToRefreshListView extends ListView {
    private View mHeader;
    private int measuredHeight;
    private int height;

    private final int STATE_PULL_TO_REFRESH = 1;
    private final int STATE_RELEASE_TO_REFRESH = 2;
    private final int STATE_REFRESHING = 3;
    private int currentState = STATE_PULL_TO_REFRESH;

    private TextView tvTitle;
    private TextView tvTime;
    private RotateAnimation raUp; //array image up
    private RotateAnimation raDown;//array image down
    private ImageView ivArr;
    private ProgressBar pb;

    public PollToRefreshListView(Context context) {
        super(context);
        initHeaderView();
    }

    public PollToRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initHeaderView();
    }

    public PollToRefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initHeaderView();
    }

    private void initHeaderView(){
        mHeader = View.inflate(getContext(), R.layout.pull_to_refresh, null);
        tvTime = (TextView) mHeader.findViewById(R.id.tv_time);
        tvTitle = (TextView) mHeader.findViewById(R.id.tv_refresh);
        ivArr = (ImageView) mHeader.findViewById(R.id.iv_arr);
        pb = (ProgressBar) mHeader.findViewById(R.id.pb_loading);
        this.addHeaderView(mHeader);
        //隐藏头布局
        mHeader.measure(0,0);
        height = mHeader.getMeasuredHeight();
        mHeader.setPadding(0, -height, 0, 0);
        initAnimate();
    }

    private int startY = -1;
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                startY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (startY == -1) //有可能用户按住头条的viewpager进行下拉时，ACTION_DOWN会被viewpager消费掉,导致startY没有被赋值。所以给一个初始值-1，在move的时候做判断
                    startY = (int) ev.getY();
                if (currentState == STATE_REFRESHING)//如果是正在刷新状态，直接break
                    break;
                int endY = (int) ev.getY();
                int dy = endY - startY;
                int position = getFirstVisiblePosition();
                if(dy > 0 && position == 0){//当下拉（dy>0）且当前显示的是第一个item
                    int padding = dy - height;//计算当前下拉header的padding value
                    mHeader.setPadding(0, padding, 0, 0);
                    if (padding > 0 && currentState != STATE_RELEASE_TO_REFRESH){
                        //改为松开刷新
                        currentState = STATE_RELEASE_TO_REFRESH;
                        refreshState();
                    }else if(padding < 0 && currentState != STATE_PULL_TO_REFRESH){
                        //改为下拉刷新
                        currentState = STATE_PULL_TO_REFRESH;
                        refreshState();
                    }
                    return true;
                }

                break;
            case MotionEvent.ACTION_UP:
                startY = -1;
                if (currentState == STATE_RELEASE_TO_REFRESH){//松手的时候如果是"release refresh"状态
                    currentState = STATE_REFRESHING;//更改为"refresh"状态
                    refreshState();//更新刷新状态
                    mHeader.setPadding(0,0,0,0);//完整显示刷新头布局
                }else if (currentState == STATE_PULL_TO_REFRESH){//松手的时候如果是"pull refresh"状态
                    mHeader.setPadding(0,-height,0,0);//隐藏头布局
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    private void initAnimate(){
        raUp = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f,Animation.RELATIVE_TO_SELF, 0.5f);
        raUp.setDuration(200);
        raUp.setFillAfter(true);
        raDown = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF, 0.5f,Animation.RELATIVE_TO_SELF, 0.5f);
        raDown.setDuration(200);
        raDown.setFillAfter(true);
    }

    /**
     * update the refresh state
     */
    private void refreshState(){
        switch (currentState){
            case STATE_PULL_TO_REFRESH:
                tvTitle.setText(getContext().getString(R.string.pull_refresh));
                pb.setVisibility(INVISIBLE);
                ivArr.setVisibility(VISIBLE);
                ivArr.startAnimation(raDown);
                break;
            case STATE_RELEASE_TO_REFRESH:
                tvTitle.setText(getContext().getString(R.string.release_refresh));
                pb.setVisibility(INVISIBLE);
                ivArr.setVisibility(VISIBLE);
                ivArr.startAnimation(raUp);
                break;
            case STATE_REFRESHING:
                tvTitle.setText(getContext().getString(R.string.loading));
                pb.setVisibility(VISIBLE);
                ivArr.clearAnimation();//清除动画，否则无法隐藏
                ivArr.setVisibility(INVISIBLE);
                break;
        }
    }
}
