package hci.me.smartkids.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import hci.me.smartkids.R;
import hci.me.smartkids.base.BaseActivity;
import hci.me.smartkids.config.AppConfig;
import hci.me.smartkids.utils.PrefUtils;

public class SplashActivity extends BaseActivity {
    @ViewInject(R.id.rl_root)
    private RelativeLayout rlRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        x.view().inject(this);//注入xUtils绑定view

        //旋转动画
        RotateAnimation rotateAnimation = new RotateAnimation(0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(1000);
        rotateAnimation.setFillAfter(true);//保持动画结束状态

        //缩放效果
        ScaleAnimation sa = new ScaleAnimation(0, 1, 0, 1,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        sa.setDuration(1000);
        sa.setFillAfter(true);//保持动画结束状态

        //渐变效果
        AlphaAnimation aa = new AlphaAnimation(0, 1);
        sa.setDuration(1000);
        sa.setFillAfter(true);//保持动画结束状态

        AnimationSet set = new AnimationSet(true);
        set.addAnimation(rotateAnimation);
        set.addAnimation(sa);
        set.addAnimation(aa);
        rlRoot.startAnimation(set);
        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //动画结束进入主界面
                boolean is_first = PrefUtils.getBoolean(SplashActivity.this, AppConfig.IS_FIRST_ENTER, true);
                Intent intent;
                if (is_first) {//如果是第一次进入，就跳入新手引导
                    intent = new Intent(SplashActivity.this, GuideActivity.class);
                }else {//否则直接进入主界面
                    intent = new Intent(SplashActivity.this, MainActivity.class);
                }
                startActivity(intent);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
