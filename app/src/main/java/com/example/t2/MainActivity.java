package com.example.t2;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.example.t2.shortLink.shortLinkActivity;
import com.example.t2.util.ElectricityBR;
import com.example.t2.util.FlashlightUtils;
import com.githang.statusbar.StatusBarCompat;
import com.yatoooon.screenadaptation.ScreenAdapterTools;


public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private TextView batterytv;
    private FlashlightUtils flashlight;
    private Button buttonLED;
    private Button buttonSOS;
    private boolean isSOS = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //ScreenAdapterTools.getInstance().loadView(getWindow().getDecorView());

        // 获取toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_call);//将Call菜单项设置为默认选中
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                mDrawerLayout.closeDrawers();//关闭滑动菜单
                return true;
            }
        });
        ActionBar ab = getSupportActionBar();
        if(ab != null) {
            ab.setDisplayHomeAsUpEnabled(true); // 显示导航栏
            ab.setHomeAsUpIndicator(R.mipmap.ic_menu);
        }
        // 沉浸状态栏
        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.colorPrimary));
        //透明状态栏效果

        // 获取按钮
        batterytv = (TextView) findViewById(R.id.button2);
        buttonLED = (Button) findViewById(R.id.button3);
        buttonSOS = (Button) findViewById(R.id.button4);
        flashlight = new FlashlightUtils();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.backup:
                Toast.makeText(this,"You clicked Backup" , Toast.LENGTH_SHORT).show();
                break;
            case R.id.delete:
                Toast.makeText(this,"You clicked Delete" , Toast.LENGTH_SHORT).show();
                break;
            case R.id.settings:
                Toast.makeText(this,"You clicked Settings" , Toast.LENGTH_SHORT).show();
                break;
            default:
        }
        return true;
    }

    public void onDC(View v) {
        ElectricityBR receiver = new ElectricityBR();//接受电量信息
        IntentFilter filter = new IntentFilter(
                Intent.ACTION_BATTERY_CHANGED);//声明电量信息接收者
        MainActivity.this.registerReceiver(receiver, filter);//注册广播
    }

    public void onLED(View v) {
        //flashlight.lightsOff();
        if (flashlight.isOff()) {
            flashlight.lightsOn(this);
            buttonLED.setText("关闭手电筒");
        } else {
            flashlight.lightsOff();
            buttonLED.setText("打开手电筒");
        }
    }

    public void onSOS(View v) {
        flashlight.sos(getApplicationContext());
        flashlight.offSos();
        /*
        if(!flashlight.isSos()){
            buttonSOS.setText("关闭SOS");
            flashlight.sos(getApplicationContext());

        } else {
            flashlight.offSos();
            buttonSOS.setText("打开SOS");
        }*/

    }

    public void onMD5(View v) {
        //Intent intent = new Intent();
        try {
            Intent intent = new Intent(this, MD5Activity.class);//显示intent
            startActivity(intent);
            //startActivity(new Intent("com.example.t2.MD5Activity"));
        } catch (Exception ex) {// 显示异常信息
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void onRC4(View v) {
        try {
            Intent intent = new Intent(this, RC4Activity.class);//显示intent
            startActivity(intent);
        } catch (Exception ex) {// 显示异常信息
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void onNoise(View v) {

        try {
            Intent intent = new Intent(this, NoiseActivity.class);//显示intent
            startActivity(intent);
        } catch (Exception ex) {// 显示异常信息
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void onPhoneInfo(View v) {

        String brand, model, androidversion, sign, sdk;
        String device, product, cpu, board, display, id, version_codes_base, maker, user, tags;
        String hardware, host, unknown, type, time, radio, serial, cpu2;
        String cl;
        final String sl;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        product = " 产品 : " + android.os.Build.PRODUCT;
        cpu = " CPU_ABI : " + android.os.Build.CPU_ABI;
        tags = " 标签 : " + android.os.Build.TAGS;
        version_codes_base = " VERSION_CODES.BASE: " + android.os.Build.VERSION_CODES.BASE;
        model = " 型号 : " + android.os.Build.MODEL;
        sdk = " SDK : " + android.os.Build.VERSION.SDK;
        androidversion = " Android 版本 : " + android.os.Build.VERSION.RELEASE;
        device = " 驱动 : " + android.os.Build.DEVICE;
        display = " DISPLAY: " + android.os.Build.DISPLAY;
        brand = " 品牌 : " + android.os.Build.BRAND;
        board = " 基板 : " + android.os.Build.BOARD;
        sign = " 设备标识 : " + android.os.Build.FINGERPRINT;
        id = " 版本号 : " + android.os.Build.ID;
        maker = " 制造商 : " + android.os.Build.MANUFACTURER;
        user = " 用户 : " + android.os.Build.USER;
        cpu2 = " CPU_ABI2 : " + android.os.Build.CPU_ABI2;
        hardware = " 硬件 : " + android.os.Build.HARDWARE;
        host = " 主机地址 :" + android.os.Build.HOST;
        unknown = " 未知信息 : " + android.os.Build.UNKNOWN;
        type = " 版本类型 : " + android.os.Build.TYPE;
        time = " 时间 : " + String.valueOf(android.os.Build.TIME);
        radio = " Radio : " + android.os.Build.RADIO;
        serial = " 序列号 : " + android.os.Build.SERIAL;
        cl = "\n\n";
        sl = product
                + cl + tags
                + cl + version_codes_base
                + cl + model
                + cl + cpu
                + cl + sdk
                + cl + androidversion
                + cl + device
                + cl + display
                + cl + brand
                + cl + board
                + cl + sign
                + cl + id
                + cl + maker
                + cl + user
                + cl + cpu2
                + cl + hardware
                + cl + host
                + cl + unknown
                + cl + type
                + cl + time
                + cl + radio
                + cl + serial;

        builder.setTitle("设备信息")//设置对话窗标题
                .setMessage(sl).setNeutralButton("复制全部", null
        ).setNegativeButton("关闭",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        //设置按钮
                    }
                });
        final AlertDialog dialog = builder.create();
        //dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
            public void onClick(View vv) {

                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                //创建ClipData对象
                ClipData clipData = ClipData.newPlainText("simple text copy", sl);
                //添加ClipData对象到剪切板中
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(getApplicationContext(), "复制成功", Toast.LENGTH_SHORT).show();
                return; // 拦截后续dialog自身的事件
            }
        });
    }

    public void onWifi(View v) {
        try {
            Intent intent = new Intent(this, WifiActivity.class);//显示intent
            startActivity(intent);
        } catch (Exception ex) {// 显示异常信息
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void onShortLink(View v){
        try {
            Intent intent = new Intent(this, shortLinkActivity.class);//显示intent
            startActivity(intent);
        } catch (Exception ex) {// 显示异常信息
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}
