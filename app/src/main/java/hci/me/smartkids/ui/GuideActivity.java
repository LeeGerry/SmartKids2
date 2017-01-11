package hci.me.smartkids.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import org.xutils.common.util.LogUtil;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import hci.me.smartkids.R;
import hci.me.smartkids.base.BaseActivity;
import hci.me.smartkids.config.AppConfig;
import hci.me.smartkids.utils.PrefUtils;

/**
 * Author: Gary
 * Time: 17/1/10
 */
public class GuideActivity extends BaseActivity {
    @ViewInject(R.id.vp_guide)
    private ViewPager vpGuide;
    @ViewInject(R.id.btn_begin)
    private Button btnStart;
    @ViewInject(R.id.ll_index)
    private LinearLayout llIndex;
    @ViewInject(R.id.iv_red_poing)
    private ImageView ivRedPoint;

    private int pointMargin;
    private GuideAdapter adapter;
    private List<ImageView> pages;
    private int[] ids = {R.mipmap.guide_1, R.mipmap.guide_2, R.mipmap.guide_3};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        x.view().inject(this);
        initData();
        adapter = new GuideAdapter();
        vpGuide.setAdapter(adapter);

        //计算两个圆点的距离，移动距离等于第二个圆点左边距 减去 第一个左边距
        //view绘制过程：measure -> layout -> draw, 该过程在activity 的 onCreate方法执行后才进行
        //所以要对小红点进行监听
        ivRedPoint.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ivRedPoint.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                //layout方法执行结束时回调
                pointMargin = llIndex.getChildAt(1).getLeft() - llIndex.getChildAt(0).getLeft();
                LogUtil.i("pointMargin"+ pointMargin);
            }
        });
        vpGuide.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            //页面滑动过程中
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //此时要更新小红点的位置，应该为 移动百分比*两个小灰点的间距
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ivRedPoint.getLayoutParams();
                params.leftMargin = (int) (pointMargin * positionOffset) + position * pointMargin;//因为offset到0.99后变成0，所以要用位置来增加基数
                ivRedPoint.setLayoutParams(params);
            }
            //某个页面被选中
            @Override
            public void onPageSelected(int position) {
                if (position == pages.size()-1){//滑动到最后一屏，把开始按钮显示出来
                    btnStart.setVisibility(View.VISIBLE);
                }else {
                    btnStart.setVisibility(View.GONE);
                }
            }
            //页面状态发生变化
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(GuideActivity.this, MainActivity.class);
                startActivity(intent);
                PrefUtils.setBoolean(getApplication(), AppConfig.IS_FIRST_ENTER, false);//设置首次打开APP为false
                finish();
            }
        });
    }

    /**
     * 初始化page页图片以及小圆点
     */
    private void initData() {
        pages = new ArrayList<>();
        for (int i = 0; i < ids.length; i++) {
            ImageView image = new ImageView(this);
            image.setBackgroundResource(ids[i]);
            pages.add(image);
            //初始化图片资源的同时，把小圆点初始化
            ImageView point = new ImageView(this);
            LinearLayout.LayoutParams params
                    = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            point.setImageResource(R.drawable.shape_point_gray);
            if (i > 0) {//从第二个小圆点开始，左边距10
                params.leftMargin = 10;
            }
            point.setLayoutParams(params);
            llIndex.addView(point);
        }
    }

    class GuideAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return pages.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        //初始化item布局
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView view = pages.get(position);
            container.addView(view);
            return view;
        }

        //销毁布局
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
