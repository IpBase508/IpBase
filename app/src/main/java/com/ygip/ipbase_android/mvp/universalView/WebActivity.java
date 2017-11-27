package com.ygip.ipbase_android.mvp.universalView;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.ygip.ipbase_android.R;
import com.ygip.ipbase_android.mvp.universalPresenter.SaveImage;
import com.ygip.ipbase_android.util.DensityUtils;
import com.ygip.ipbase_android.util.ItemLongClickedPopWindow;
import com.ygip.ipbase_android.util.StringUtils;

import java.net.URLEncoder;
import java.util.HashMap;


/**
 * 内置浏览器
 */

public class WebActivity extends AppCompatActivity {
    public WebView mWeb;
    private ProgressBar progressBar;
    private ImageButton imageButton;
    private Toolbar toolbar;
    private static final String urlHead = "https://m.baidu.com/";
    public String url = null;
    public static String title;
    private HashMap<String, String> mapData;
    private String htmlString;
    private String imgurl;
    private GestureDetector gestureDetector;
    private int downX=100;
    private int downY=100;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            mWeb.clearCache(true);
            mWeb.clearHistory();
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private String getData() {
        try {
            mapData = (HashMap<String, String>) getIntent().getSerializableExtra("data");
            if (mapData != null) {
                htmlString = mapData.get("data");
                Log.d("htmlString get",htmlString);
            } else {
                htmlString = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return htmlString;
    }


    private void initView() {

        try {
            toolbar.setTitle(title == null ? "详情" : title + " 详情");
            if (getData() != null) {
                if (StringUtils.isTopURL(htmlString)) {
                    htmlString=URLEncoder.encode(htmlString, "utf-8");
                    if(!htmlString.startsWith("http:")&&!htmlString.startsWith("https:")){//自动补协议头
                        url="http://"+htmlString;
                    }
                    Log.d("htmlString2url",url);
                    mWeb.loadUrl(url);
                } else {
                    mWeb.loadDataWithBaseURL(null, htmlString, "text/html", "utf-8", null);
                }
            } else {
                Log.e("webview", "data is null");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        mWeb.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                downX = (int) event.getX();
                downY = (int) event.getY();
                return false;
            }
        });
        mWeb.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final ItemLongClickedPopWindow itemLongClickedPopWindow;
                WebView.HitTestResult result = ((WebView) v).getHitTestResult();
                if (null == result)
                    return false;
                int type = result.getType();
                if (type == WebView.HitTestResult.UNKNOWN_TYPE)
                    return false;
                if (type == WebView.HitTestResult.EDIT_TEXT_TYPE) {
                    //let TextViewhandles context menu return true;
                }
                 itemLongClickedPopWindow = new ItemLongClickedPopWindow(WebActivity.this, ItemLongClickedPopWindow.IMAGE_VIEW_POPUPWINDOW, DensityUtils.dip2px(getApplicationContext(),120), DensityUtils.dip2px(getApplicationContext(),90));
                // Setup custom handlingdepending on the type
                switch (type) {
                    case WebView.HitTestResult.PHONE_TYPE: // 处理拨号
                        break;
                    case WebView.HitTestResult.EMAIL_TYPE: // 处理Email
                        break;
                    case WebView.HitTestResult.GEO_TYPE: // TODO
                        break;
                    case WebView.HitTestResult.SRC_ANCHOR_TYPE: // 超链接
                        // Log.d(DEG_TAG, "超链接");
                        break;
                    case WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE:
                        break;
                    case WebView.HitTestResult.IMAGE_TYPE: // 处理长按图片的菜单项
                        imgurl = result.getExtra();
                        //通过GestureDetector获取按下的位置，来定位PopWindow显示的位置
                        itemLongClickedPopWindow.showAtLocation(v, Gravity.TOP | Gravity.LEFT, downX, downY + 10);
                        break;
                    default:
                        break;
                }

                itemLongClickedPopWindow.getView(R.id.item_longclicked_viewImage)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                itemLongClickedPopWindow.dismiss();
                                mWeb.loadUrl(result.getExtra());
                            }
                        });

                itemLongClickedPopWindow.getView(R.id.item_longclicked_saveImage)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                itemLongClickedPopWindow.dismiss();
                                new SaveImage().execute(result.getExtra());
                            }
                        });

                return false;
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);
        toolbar = (Toolbar) findViewById(R.id.toolbar_webview);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();//添加返回按钮
        ab.setDisplayHomeAsUpEnabled(true);
        mWeb = (WebView) findViewById(R.id.webview);
        progressBar = (ProgressBar) findViewById(R.id.webview_progressbar);
        imageButton=(ImageButton)findViewById(R.id.btn_right_web);
        WebSettings settings = mWeb.getSettings();
        settings.setJavaScriptEnabled(true);

        //自适应
//        settings.setUseWideViewPort(true);
//        settings.setLoadWithOverviewMode(true);
        //支持屏幕缩放
//
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        //不显示webview缩放按钮
        settings.setDisplayZoomControls(false);

//        mWeb.loadUrl(url);
        mWeb.setWebChromeClient(new WebChromeClient() {


            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress >= 85) {
                    progressBar.setProgress(100);
                    progressBar.setVisibility(View.GONE);
                } else {
                    if (View.GONE == progressBar.getVisibility()) {
                        progressBar.setVisibility(View.VISIBLE);
                    }
                    progressBar.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }
        });

        mWeb.setWebViewClient(new WebViewClient());
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWeb!=null){
            mWeb.destroy();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (url!=null) {
                mWeb.goBack();//监听返回键
            } else {
                finish();
            }
        }
        return true;
    }
}
