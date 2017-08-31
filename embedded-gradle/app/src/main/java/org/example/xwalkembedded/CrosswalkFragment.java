package org.example.xwalkembedded;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.xwalk.core.XWalkPreferences;
import org.xwalk.core.XWalkView;


public class CrosswalkFragment extends Fragment {
    private XWalkView mXWalkView;
    private String url;

    public CrosswalkFragment() {

    }
    @SuppressLint("ValidFragment")
    public CrosswalkFragment(String url) {
        super();
        this.url = url;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crosswalk, container, false);
        mXWalkView = (XWalkView) view.findViewById(R.id.web_crosswalk);
        //添加对javascript支持
        XWalkPreferences.setValue("enable-javascript", true);
        //开启调式,支持谷歌浏览器调式
        XWalkPreferences.setValue(XWalkPreferences.REMOTE_DEBUGGING, true);
        //置是否允许通过file url加载的Javascript可以访问其他的源,包括其他的文件和http,https等其他的源
        XWalkPreferences.setValue(XWalkPreferences.ALLOW_UNIVERSAL_ACCESS_FROM_FILE, true);
        //JAVASCRIPT_CAN_OPEN_WINDOW
        XWalkPreferences.setValue(XWalkPreferences.JAVASCRIPT_CAN_OPEN_WINDOW, true);

        mXWalkView.setHorizontalScrollBarEnabled(true);
        mXWalkView.setVerticalScrollBarEnabled(true);
        mXWalkView.setScrollBarStyle(XWalkView.SCROLLBARS_OUTSIDE_INSET);
        mXWalkView.setScrollbarFadingEnabled(true);

        mXWalkView.setFitsSystemWindows(true);

        mXWalkView.canZoomIn();
        mXWalkView.canZoomOut();
        mXWalkView.load(url, null);
        Log.i("Crosswalkfragemnt", "onCreateView");
        return view;
    }


}
