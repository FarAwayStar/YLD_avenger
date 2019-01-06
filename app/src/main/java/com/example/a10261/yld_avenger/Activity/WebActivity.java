package com.example.a10261.yld_avenger.Activity;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.a10261.yld_avenger.DBC;
import com.example.a10261.yld_avenger.GetNetstate;
import com.example.a10261.yld_avenger.Get_NewsContent;
import com.example.a10261.yld_avenger.R;
import com.example.a10261.yld_avenger.Sqlmanager;

public class WebActivity extends AppCompatActivity {

    private Bundle bundle;
    private WebView webView;

    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.webview_layout);
        bundle = getIntent().getExtras();
        String url = bundle.getString("URL");
        webView = findViewById(R.id.webview);
        WebSettings settings = webView.getSettings();
        webView.addJavascriptInterface(this,"android");//添加js监听 这样html就能调用客户端
        //settings.setSupportZoom(true);
        //settings.setTextSize(WebSettings.TextSize.SMALLER);
        settings.setDomStorageEnabled(true);
        settings.setJavaScriptEnabled(false);
        settings.setBlockNetworkImage(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        Sqlmanager sqlmanager=new Sqlmanager(new DBC(this).getWritableDatabase());
        Cursor cursor=sqlmanager.findbyurl(url);
        cursor.moveToFirst();
        String content=cursor.getString(cursor.getColumnIndex(DBC.KEY_CONTENT));

        if(!GetNetstate.isNetConnected(this))
        {
                //Log.d("源码",content);
                webView.loadDataWithBaseURL(null,content,"text/html" , "utf-8", null);
        }
        else
        {
            settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            settings.setBlockNetworkImage(false);
            webView.loadUrl(url);

        }

    }

    @Override
    protected void onDestroy() {
        if (webView!= null) {
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webView.clearHistory();
            ((ViewGroup) webView.getParent()).removeView(webView);
            webView.destroy();
            webView= null;
        }
        super.onDestroy();
    }

}
