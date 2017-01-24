package hci.me.smartkids.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
                //return super.shouldOverrideUrlLoading(view, request);
                view.loadUrl(request.getUrl().toString());
                //Log.i("webview","overideurlloading");
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                //Log.i("webview","started");
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                //Log.i("webview", "finished");
            }
        });
        wvNews.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                //Log.i("webview", "load..."+newProgress);
                if (newProgress > 50){//WebViewClient中的 pageFinished 方法有bug,不执行，
                    // 这个progress也不准，现提前结束。
                    pbLoading.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                //Log.i("webview", "title:" + title);
                super.onReceivedTitle(view, title);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_textsize:
                showChooseSizeDialog();
                break;
            case R.id.btn_menu:

                break;
        }
    }
    private int mWhich ;
    private int mCurrentWhich = 2;
    private void showChooseSizeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.text_size_setting));
        String[] items = new String[]{"Super big", "Big","Normal","Small","Super small"};
        builder.setSingleChoiceItems(items, mCurrentWhich, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mWhich = which;
            }
        });
        builder.setPositiveButton("Comfirm", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                WebSettings settings = wvNews.getSettings();
                switch (mWhich){
                    case 0:
                        settings.setTextZoom(200);
                        break;
                    case 1:
                        settings.setTextZoom(150);
                        break;
                    case 2:
                        settings.setTextZoom(100);
                        break;
                    case 3:
                        settings.setTextZoom(75);
                        break;
                    case 4:
                        settings.setTextZoom(50);
                        break;
                }
                mCurrentWhich = mWhich;
            }
        });
        builder.setNegativeButton("Cancel",null);
        builder.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        url = null;
        wvNews.destroy();
    }
}
