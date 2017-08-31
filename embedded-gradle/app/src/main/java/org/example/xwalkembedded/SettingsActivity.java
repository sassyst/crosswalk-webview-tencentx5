package org.example.xwalkembedded;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.support.annotation.IdRes;
import android.support.v7.app.ActionBar;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.List;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends Activity implements RadioGroup.OnCheckedChangeListener {

    private AppCompatButton btn_save;
    private EditText et_url,et_zoom;
    private  int web_type;
    private Context context;
    private RadioGroup rg_web;
    private SharedPreferencesHelper spHelper;
    private RadioButton rb_webview,rb_cross,rb_x5;
//    RadioButton radioButton_web,radioButton_cross,radioButton_x5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        context = getApplicationContext();
        spHelper = new SharedPreferencesHelper(context);
        initView();
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
//                intent.putExtra("url",et_url.getText().toString());
//                intent.putExtra("zoom",et_zoom.getText());
//                intent.putExtra("web_type",web_type);
                spHelper.saveString("url",et_url.getText().toString());
                spHelper.saveString("zoom",et_zoom.getText().toString());
                spHelper.saveInteger("web_type",web_type);
                setResult(RESULT_OK,intent);
                finish();
            }
        });

    }

    private void initView() {
        btn_save = (AppCompatButton) findViewById(R.id.btn_save);
        et_url = (EditText) findViewById(R.id.et_url);
        et_zoom = (EditText) findViewById(R.id.et_zoom);
        rg_web  = (RadioGroup) findViewById(R.id.rg_web);
        rg_web.setOnCheckedChangeListener(this);
        rb_webview = (RadioButton) findViewById(R.id.radio_webview);
        rb_cross = (RadioButton) findViewById(R.id.radio_cross);
        rb_x5 = (RadioButton) findViewById(R.id.radio_X5);
        et_url.setText(spHelper.getString("url"));
        et_zoom.setText(spHelper.getString("zoom"));
        switch (spHelper.getInteger("web_type")){
            case 0:
                rb_cross.setChecked(true);
                break;
            case 1:
                rb_x5.setChecked(true);
                break;
            case 2:
                rb_webview.setChecked(true);
                break;
            default:
                rb_cross.setChecked(true);
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
        RadioButton rb = (RadioButton) findViewById(i);
        Toast.makeText(context,rb.getText(),Toast.LENGTH_SHORT).show();
        switch (i){
            case R.id.radio_cross:
                web_type=0;
                break;
            case R.id.radio_webview:
                web_type=2;
                break;
            case R.id.radio_X5:
                web_type=1;
                break;
            default:
                web_type=0;
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
