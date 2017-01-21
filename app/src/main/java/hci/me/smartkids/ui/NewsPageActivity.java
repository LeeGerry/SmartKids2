package hci.me.smartkids.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import hci.me.smartkids.R;
import hci.me.smartkids.base.BaseActivity;

/**
 * Author: Gary
 * Time: 17/1/20
 */

public class NewsPageActivity extends BaseActivity implements View.OnClickListener{
    @ViewInject(R.id.ll_control)
    private LinearLayout llControl;
    @ViewInject(R.id.btn_back)
    private ImageButton ibBack;
    @ViewInject(R.id.btn_menu)
    private ImageButton ibMenu;
    @ViewInject(R.id.btn_textsize)
    private ImageButton ibTextSize;
    @ViewInject(R.id.btn_share)
    private ImageButton ibShare;
    @ViewInject(R.id.wv_news_details)
    private WebView wvNews;
    @ViewInject(R.id.pb_loading)
    private ProgressBar pbLoading;
    private boolean isload = false;
    private String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_page);
        x.view().inject(this);
        url = getIntent().getStringExtra("url");
        llControl.setVisibility(View.VISIBLE);
        ibBack.setVisibility(View.VISIBLE);
        ibMenu.setVisibility(View.GONE);
        ibBack.setOnClickListener(this);
        ibShare.setOnClickListener(this);
        ibTextSize.setOnClickListener(this);
        wvNews.loadUrl(url);
        WebSettings settings = wvNews.getSettings();
        settings.setBuiltInZoomControls(true);
        settings.setUseWideViewPort(true);
        settings.setJavaScriptEnabled(true);
        pbLoading.setVisibility(View.VISIBLE);
        wvNews.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });
        wvNews.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress > 50){//
                    pbLoading.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }
        });
    }

    @Override
    public void onClick(View v) {

    }
}
