package hci.me.smartkids.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import hci.me.smartkids.R;

/**
 * Author: Gary
 * Time: 17/1/18
 */

public class PollToRefreshListView extends ListView implements AbsListView.OnScrollListener{
    private View mHeader;
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
    private View footer;
    private int measuredHeight;

    public PollToRefreshListView(Context context) {
        super(context);
        initHeaderView();
        initFooterView();
    }

    public PollToRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initHeaderView();
        initFooterView();
    }

    public PollToRefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initHeaderView();
        initFooterView();
    }

    /**
     * 加载footer布局(加载更多)
     */
    private void initFooterView(){
        footer = View.inflate(getContext(), R.layout.poll_refresh_footer, null);
        this.addFooterView(footer);
        footer.measure(0,0);
        measuredHeight = footer.getMeasuredHeight();
        footer.setPadding(0,-measuredHeight,0,0);
        this.setOnScrollListener(this);
    }

    /**
     * 记载头布局（下拉刷新）
     */
    private void initHeaderView() {
        mHeader = View.inflate(getContext(), R.layout.pull_to_refresh, null);
        tvTime = (TextView) mHeader.findViewById(R.id.tv_time);
        tvTitle = (TextView) mHeader.findViewById(R.id.tv_refresh);
        ivArr = (ImageView) mHeader.findViewById(R.id.iv_arr);
        pb = (ProgressBar) mHeader.findViewById(R.id.pb_loading);
        this.addHeaderView(mHeader);
        //隐藏头布局
        mHeader.measure(0, 0);
        height = mHeader.getMeasuredHeight();
        mHeader.setPadding(0, -height, 0, 0);
        initAnimate();
        setcurrentTime();
    }

    private int startY = -1;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
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
                if (dy > 0 && position == 0) {//当下拉（dy>0）且当前显示的是第一个item
                    int padding = dy - height;//计算当前下拉header的padding value
                    mHeader.setPadding(0, padding, 0, 0);
                    if (padding > 0 && currentState != STATE_RELEASE_TO_REFRESH) {
                        //改为松开刷新
                        currentState = STATE_RELEASE_TO_REFRESH;
                        refreshState();
                    } else if (padding < 0 && currentState != STATE_PULL_TO_REFRESH) {
                        //改为下拉刷新
                        currentState = STATE_PULL_TO_REFRESH;
                        refreshState();
                    }
                    return true;
                }

                break;
            case MotionEvent.ACTION_UP:
                startY = -1;
                if (currentState == STATE_RELEASE_TO_REFRESH) {//松手的时候如果是"release refresh"状态
                    currentState = STATE_REFRESHING;//更改为"refresh"状态
                    refreshState();//更新刷新状态
                    mHeader.setPadding(0, 0, 0, 0);//完整显示刷新头布局
                    //4.进行回调
                    if (mListener != null)
                        mListener.onRefresh();
                } else if (currentState == STATE_PULL_TO_REFRESH) {//松手的时候如果是"pull refresh"状态
                    mHeader.setPadding(0, -height, 0, 0);//隐藏头布局
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    private void initAnimate() {
        raUp = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        raUp.setDuration(200);
        raUp.setFillAfter(true);
        raDown = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        raDown.setDuration(200);
        raDown.setFillAfter(true);
    }

    /**
     * 更新状态
     */
    private void refreshState() {
        switch (currentState) {
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
    private boolean isLoadMore;
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == SCROLL_STATE_IDLE){
            int lastVisiblePosition = getLastVisiblePosition();
            if (lastVisiblePosition == getCount() - 1 && !isLoadMore){
                isLoadMore = true;
                footer.setPadding(0,0,0,0);//显示加载更多
                setSelection(getCount() - 1);//将listview显示在最后一个item上，从而直接展示加载更多
                if (mListener != null){
                    mListener.loadMore();
                }
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    /**
     * 1. 下拉刷新的回调接口
     */
    public interface OnRefreshListener {
        public void onRefresh();

        public void loadMore();
    }

    // 2. 接收回调接口
    private OnRefreshListener mListener;

    /**
     * 3. 暴露接口，设置监听
     *
     * @param listener
     */
    public void setOnRefreshListener(OnRefreshListener listener) {
        this.mListener = listener;
    }

    /**
     * 6. 刷新结束，收起控件
     */
    public void onRefreshComplete(boolean success) {
        if(!isLoadMore){//不是加载更多
            //更新状态
            currentState = STATE_PULL_TO_REFRESH;
            tvTitle.setText(getContext().getString(R.string.pull_refresh));
            pb.setVisibility(INVISIBLE);
            ivArr.setVisibility(VISIBLE);
            mHeader.setPadding(0, -height, 0, 0);
            if (success)//只有成功刷新才设置时间
                setcurrentTime();
        }else{//是加载更多，结束后要隐藏footer
            footer.setPadding(0,-measuredHeight,0,0);
            isLoadMore = false;//把isloadmore 置为false
        }

    }

    private void setcurrentTime(){
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        String time = format.format(new Date());
        tvTime.setText(time);
    }
}
