package com.example.t2;


import android.content.res.Configuration;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.example.t2.util.MD5Util;
import com.example.t2.util.NotchPhoneUtils;
import com.example.t2.util.hasNotch;
import com.githang.statusbar.StatusBarCompat;


public class MD5Activity extends AppCompatActivity {

    private Toolbar toolBar;
    private EditText editText;
    private TextView textView;
    MD5Util md5Util;

    private Boolean isNotch = false;// 是否为刘海屏
    private int type;
    ConstraintLayout mRlall;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
        // 设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        */
        setContentView(R.layout.activity_md5);

        // 沉浸状态栏
        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.colorPrimary));

        mRlall = findViewById(R.id.rl_all);

        String deviceBrand = NotchPhoneUtils.getDeviceBrand(); //获取手机厂商
        //判断相应手机是否有刘海屏

        if ("vivo".equals(deviceBrand)) {
            isNotch = NotchPhoneUtils.HasNotchVivo(MD5Activity.this);
            type = 1;
        } else if ("HUAWEI".equals(deviceBrand)) {
            isNotch = NotchPhoneUtils.hasNotchAtHuawei(MD5Activity.this);
            type = 2;
        } else if ("OPPO".equals(deviceBrand)) {
            isNotch = NotchPhoneUtils.HasNotchOPPO(MD5Activity.this);
            type = 3;
        } else if ("Xiaomi".equals(deviceBrand)) {
            isNotch = NotchPhoneUtils.HasNotchXiaoMi();
            type = 4;
        }
        NotchPhoneUtils.onConfigurationChanged(MD5Activity.this, isNotch, type, mRlall);


        editText = (EditText) findViewById(R.id.textB);
        textView = (TextView) findViewById(R.id.textA);
        toolBar= (Toolbar) findViewById(R.id.toolbar);
        toolBar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolBar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);
        md5Util = new MD5Util();

        //Log.d("***suc dev","hasnotch: " + hasNotch.hasNotchInScreen(this));

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void onMD5(View v){
        String s = editText.getText().toString();
        textView.setText(md5Util.string2MD5(s));
    }
    //屏幕方向发生改变的回调方法
    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        NotchPhoneUtils.onConfigurationChanged(MD5Activity.this, isNotch, type, mRlall);
        super.onConfigurationChanged(newConfig);
    }



}
