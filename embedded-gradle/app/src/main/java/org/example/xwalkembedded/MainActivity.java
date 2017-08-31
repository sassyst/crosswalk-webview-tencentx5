package org.example.xwalkembedded;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xwalk.core.XWalkView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements View.OnClickListener {
    private final int CROSSWALK = 0;
    private final int TENCENTx5 = 1;
    private final int WEB = 2;
    private final String TAG = "TANJIN";
    private Boolean isFabOpen = false;
    private FloatingActionButton fab;
    private AppCompatButton btn_settings,btn_reflsh;
    private Animation fab_open, fab_close, rotate_forward, rotate_backward;
    private ListView lvMenus;
    private ProgressDialog dialog;
    private int web_type = 0;
    private Context context;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private boolean isfirstrun;
    private SharedPreferencesHelper spHelper;
    private String now_url = Constant.url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e(TAG,"onCreate");
        context = getApplicationContext();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initView();
        spHelper = new SharedPreferencesHelper(context);
        isfirstrun = spHelper.getBoolean("isfirstrun");
//        if (isfirstrun){
            new JSONTask().execute(Constant.url_menu);
//        }else{
           loadListView();
//        }
        ChangeFragment(now_url, web_type);
    }

    public void loadListView(){
        MenuAdapter menuAdapter = new MenuAdapter(MainActivity.this, R.layout.row_menu, Constant.menu_data);
        lvMenus.setAdapter(menuAdapter);
        lvMenus.setOnItemClickListener(new AdapterView.OnItemClickListener() {  // list item click opens a new detailed activity
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MenuModel menuModel = Constant.menu_data.get(position); // getting the model
                now_url = menuModel.getUrl();
                ChangeFragment(now_url, web_type);
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (isfirstrun) {
            spHelper.saveBoolean("isfirstrun",false);
        }
    }


    public void ChangeFragment(String url, int web_type) {
        fm = getFragmentManager();
        ft = fm.beginTransaction();
        switch (web_type) {
            case 0:
                ft.replace(R.id.content_main, new CrosswalkFragment(url));
                break;
            case 1:
                ft.replace(R.id.content_main, new X5Fragment(url));
                break;
            case 2:
                ft.replace(R.id.content_main, new WebFragment(url));
                break;
            default:
                break;
        }
        ft.commit();

    }

    private void initView() {

        dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setMessage("Loading. Please wait..."); // showing a dialog for loading the data
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        ImageLoader.getInstance().init(config); // Do it on Application start
        lvMenus = (ListView) findViewById(R.id.lvMenus);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_backward);
        fab.setOnClickListener(this);
        btn_settings = (AppCompatButton) findViewById(R.id.btn_settings);
        btn_settings.setOnClickListener(this);
        btn_reflsh = (AppCompatButton) findViewById(R.id.btn_reflsh);
        btn_reflsh.setOnClickListener(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(TAG,"onRestart");
    }

    public class JSONTask extends AsyncTask<String, String, List<MenuModel>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.i(TAG,"onPreExecute");
            Constant.menu_data.clear();
            dialog.show();
        }

        @Override
        protected List<MenuModel> doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                String finalJson = buffer.toString();

                JSONObject parentObject = new JSONObject(finalJson);
                JSONArray parentArray = parentObject.getJSONArray("data");

                List<MenuModel> menuModelList = new ArrayList<>();

                Gson gson = new Gson();
                for (int i = 0; i < parentArray.length(); i++) {
                    JSONObject finalObject = parentArray.getJSONObject(i);
                    MenuModel menuModel = gson.fromJson(finalObject.toString(), MenuModel.class); // a single line json parsing using Gson
                    menuModelList.add(menuModel);
                    Constant.menu_data.add(menuModel);
                }
                Log.i(TAG,Constant.menu_data.toString());
                return menuModelList;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(final List<MenuModel> result) {
            super.onPostExecute(result);
            dialog.dismiss();
            if (result != null) {
                loadListView();
                ChangeFragment(now_url, web_type);
            } else {
                Toast.makeText(getApplicationContext(), "Not able to fetch data from server, please check url.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class MenuAdapter extends ArrayAdapter {

        private List<MenuModel> menuModelList;
        private int resource;
        private LayoutInflater inflater;

        public MenuAdapter(Context context, int resource, List<MenuModel> objects) {
            super(context, resource, objects);
            menuModelList = objects;
            this.resource = resource;
            inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;

            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(resource, null);
                holder.ivIcon = (ImageView) convertView.findViewById(R.id.ivIcon);
                holder.tvUrl = (TextView) convertView.findViewById(R.id.tv_url);
                holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final ProgressBar progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar);

            // Then later, when you want to display image
            final ViewHolder finalHolder = holder;
            ImageLoader.getInstance().displayImage(menuModelList.get(position).getIcon(), holder.ivIcon, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    progressBar.setVisibility(View.VISIBLE);
                    finalHolder.ivIcon.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    progressBar.setVisibility(View.GONE);
                    finalHolder.ivIcon.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    progressBar.setVisibility(View.GONE);
                    finalHolder.ivIcon.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {
                    progressBar.setVisibility(View.GONE);
                    finalHolder.ivIcon.setVisibility(View.INVISIBLE);
                }
            });

            holder.tvUrl.setText(menuModelList.get(position).getUrl());
            holder.tvName.setText(menuModelList.get(position).getName());
            return convertView;
        }


        class ViewHolder {
            private ImageView ivIcon;
            private TextView tvUrl;
            private TextView tvName;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.fab:
                animateFAB();
                break;
            case R.id.btn_settings:
                gotoActivity();
                break;
            case R.id.btn_reflsh:
                ChangeFragment(now_url, web_type);
        }
    }

    private void gotoActivity() {
        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivityForResult(intent, 0);
    }

    private void animateFAB() {
        if (isFabOpen) {
            fab.startAnimation(rotate_backward);
            lvMenus.setVisibility(View.INVISIBLE);
            btn_settings.setVisibility(View.INVISIBLE);
            btn_reflsh.setVisibility(View.INVISIBLE);
            isFabOpen = false;
            Log.d("Raj", "close");
        } else {
            fab.startAnimation(rotate_forward);
            lvMenus.setVisibility(View.VISIBLE);
            btn_settings.setVisibility(View.VISIBLE);
            btn_reflsh.setVisibility(View.VISIBLE);
            isFabOpen = true;
            Log.d("Raj", "open");

        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        loadListView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String url = null;
        switch (resultCode) { //resultCode为回传的标记，我在B中回传的是RESULT_OK
            case RESULT_OK:
                break;
            default:
                break;
        }
        web_type = spHelper.getInteger("web_type");
        ChangeFragment(Constant.url, web_type);
    }

}
