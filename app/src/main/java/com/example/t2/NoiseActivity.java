package com.example.t2;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.t2.Noise.FileUtil;
import com.example.t2.Noise.MyMediaRecorder;
import com.example.t2.Noise.SoundDiscView;
import com.example.t2.Noise.World;
import com.example.t2.Permission.PermissionHelper;
import com.example.t2.Permission.PermissionInterface;

import java.io.File;

public class NoiseActivity extends AppCompatActivity  {

    float volume = 10000;
    private SoundDiscView soundDiscView;
    private MyMediaRecorder mRecorder;
    private static final int msgWhat = 0x1001;
    private static final int refreshTime = 100;
    private String sl = "";
    private Toolbar toolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noise);

        toolBar= (Toolbar) findViewById(R.id.toolbar);
        toolBar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolBar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //判断是否没有授权权限
            if (checkSelfPermission(Manifest.permission.RECORD_AUDIO)
                    != PackageManager.PERMISSION_GRANTED) {
                //申请权限
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.RECORD_AUDIO}, 1);
            } else {
                //Toast.makeText(this, "定位权限已经授权！", Toast.LENGTH_SHORT).show();
            }
        }

        mRecorder = new MyMediaRecorder();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {       //判断授权码为1的申请权限数组
                //判断授权结果是否已经授权
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    for (int i = 0; i < permissions.length; i++) {
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                            sl += "" + "权限" + permissions[i] + "申请成功\n";
                        } else {
                            sl += "" + "权限" + permissions[i] + "申请失败\n";
                        }
                    }
                    Toast.makeText(this, sl + "", Toast.LENGTH_SHORT).show();
                } else {
                    new AlertDialog.Builder(this)
                            .setTitle("提示信息")
                            .setMessage("当前应用缺少必要权限，该功能暂时无法使用。" +
                                    "如若需要，请单击【确定】按钮前往设置中心进行权限授权。")
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //通过Intent跳转设置中心界面
                                    Intent intent = new Intent(
                                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    intent.setData(Uri.parse("package:" + getPackageName()));
                                    startActivity(intent);
                                }
                            }).show();
                }
                return;
            }
        }
    }



    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (this.hasMessages(msgWhat)) {
                return;
            }
            volume = mRecorder.getMaxAmplitude();  //获取声压值
            if(volume > 0 && volume < 1000000) {
                World.setDbCount(20 * (float)(Math.log10(volume)));  //将声压值转为分贝值
                soundDiscView.refresh();
            }
            handler.sendEmptyMessageDelayed(msgWhat, refreshTime);
        }
    };

    private void startListenAudio() {
        handler.sendEmptyMessageDelayed(msgWhat, refreshTime);
    }

    /**
     * 开始记录
     * @param fFile
     */
    public void startRecord(File fFile){
        try{
            Thread.sleep(50);
            mRecorder.setMyRecAudioFile(fFile);
            if (mRecorder.startRecorder()) {
                startListenAudio();
            }else{
                Toast.makeText(this, "启动录音失败", Toast.LENGTH_SHORT).show();
            }
        }catch(Exception e){
            Toast.makeText(this, "录音机已被占用或录音权限被禁止", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        soundDiscView = (SoundDiscView) findViewById(R.id.soundDiscView);
        File file = FileUtil.createFile("temp.amr");
        if (file != null) {
            Log.v("file", "file =" + file.getAbsolutePath());
            startRecord(file);
        } else {
            Toast.makeText(getApplicationContext(), "创建文件失败", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 停止记录
     */
    @Override
    protected void onPause() {
        super.onPause();
        mRecorder.delete(); //停止记录并删除录音文件
        handler.removeMessages(msgWhat);
    }

    @Override
    protected void onDestroy() {
        handler.removeMessages(msgWhat);
        mRecorder.delete();
        super.onDestroy();
    }
}
