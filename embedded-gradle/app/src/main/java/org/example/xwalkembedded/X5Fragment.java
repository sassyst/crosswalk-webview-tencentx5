package org.example.xwalkembedded;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;


public class X5Fragment extends Fragment {
    private com.tencent.smtt.sdk.WebView mTencentWebview;

    // TODO: Rename and change types of parameters
    private String url;

    public X5Fragment(){

    }
    @SuppressLint("ValidFragment")
    public X5Fragment(String url) {
        this.url = url;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_x5, container, false);
        mTencentWebview = (WebView) view.findViewById(R.id.web_tencentx5);
        // Inflate the layout for this fragment
        WebSettings webSetting = mTencentWebview.getSettings();
        webSetting.setAllowFileAccess(true);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSetting.setSupportZoom(true);
        webSetting.setBuiltInZoomControls(true);
        webSetting.setUseWideViewPort(true);
        webSetting.setSupportMultipleWindows(false);
        // webSetting.setLoadWithOverviewMode(true);
        webSetting.setAppCacheEnabled(true);
        // webSetting.setDatabaseEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setJavaScriptEnabled(true);
        webSetting.setGeolocationEnabled(true);

        mTencentWebview.loadUrl(url);
        return view;
    }
}
