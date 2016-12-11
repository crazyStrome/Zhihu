package com.example.webviewtest;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.transition.Explode;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.TextView;

import com.kogitune.activity_transition.ActivityTransition;
import com.kogitune.activity_transition.ExitActivityTransition;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by QDQ on 2016/8/27.
 */
public class MainDataActivity extends Activity{
    private WebView webView;
    private String url="";
    private ExitActivityTransition start;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maindata);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        start= ActivityTransition.with(getIntent()).to(findViewById(R.id.cardView)).start(savedInstanceState);
        url=getIntent().getExtras().getString("data");
        webView=(WebView)findViewById(R.id.web);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBlockNetworkImage(false);
        webView.loadUrl(url);
    }

    @Override
    public void onBackPressed() {
        start.exit(this);
    }
}
