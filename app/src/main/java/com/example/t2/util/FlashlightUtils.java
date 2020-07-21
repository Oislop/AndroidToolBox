package com.example.t2.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

/*
 * 手电筒（开启闪光灯）工具类
 * 使用前，先申请闪光灯权限 <uses-permission android:name="android.permission.FLASHLIGHT" />
 * 部分手机需要摄像头权限： <uses-permission android:name="android.permission.CAMERA" /> 注：注意android5.0以上权限调用
 * 使用前，请先用hasFlashlight()方法判断设备是否有闪光灯
 * 务必在activity或fragment的onDestroy()方法里调用lightsOff()方法，确保Camera被释放
 *
 * 使用方法
 *  开启sos:new FlashlightUtils().sos()
 *  开启闪光灯: new FlashlightUtils().lightsOn()
 *  初始化：FlashlightUtils flashlight = new FlashlightUtils();
    开启闪光灯： flashlight.lightsOn(getContext());
    关闭闪光灯：flashlight.lightsOff();
    开启sos: flashlight.sos(getContext(),1);
    关闭sos: flashlight.offSos();
    判断设备是否有闪光灯：flashlight.hasFlashlight(getContext());
    判断闪光灯是否打开：flashlight.isOff();
    判断sos(求救信号是否开户):flashlight.isSos();
 */
public class FlashlightUtils {

    static {
        try {
            Class.forName("android.hardware.Camera");
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private Camera mCamera;
    private CameraManager manager;
    private boolean isSos = false;


    public boolean isOff() {
        if (isVersionM()) {
            return manager == null;
        } else
            return mCamera == null;

    }

    //打开手电筒
    public void lightsOn(Context context) {
        offSos();
        if (hasFlashlight(context)) {
            if (isVersionM()) {
                linghtOn23(context);
            } else {
                lightOn22();
            }
        }else {
            Toast.makeText(context,"您的手机不支持开启闪光灯",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 安卓6.0以上打开手电筒
     */
    @TargetApi(Build.VERSION_CODES.M)
    private void linghtOn23(Context context) {
        try {
            manager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
            manager.setTorchMode("0", true);// "0"是主闪光灯
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * android6.0以下打开手电筒
     */
    private void lightOn22() {
        if (mCamera == null) {
            mCamera = Camera.open();
            Camera.Parameters params = mCamera.getParameters();
            params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            mCamera.setParameters(params);
        }
    }

    private TimerTask mTimerTask;
    private Timer mTimer = null;
    private int mInt = 0;
    private Context context;

    //关闭sos
    public void offSos() {
        isSos = false;
        if (mTimer == null)
            return;
        mTimer.cancel();
        lightsOff();
    }

    public boolean isSos() {
        return isSos;
    }

    /**
     * 打开sos
     *
     * @param context
          闪烁速度，建议取值1~6
     */
    public void sos(Context context) {

        lightsOff();

        this.context = context;
        if (mTimer != null)
            offSos();

        isSos = true;

        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                mInt = mInt == 0 ? 1 : 0;
                message.what = mInt;
                handler.sendMessage(message);
            }
        };
        mTimer = new Timer();
        mTimer.schedule(mTimerTask, 0, 500);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    lightsOn(context);
                    break;
                case 0:
                    lightsOff();
                    break;
            }
        }
    };


    public void lightsOff() {
        if (isVersionM()) {
            lightsOff23();
        } else {
            lightsOff22();
        }
    }

    //安卓6.0以下关闭手电筒
    private void lightsOff22() {
        if (mCamera != null) {
            Camera.Parameters params = mCamera.getParameters();
            params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            mCamera.setParameters(params);
            mCamera.release();
            mCamera = null;
        }
    }

    //安卓6.0以上打关闭电筒
    @TargetApi(Build.VERSION_CODES.M)
    private void lightsOff23() {
        try {
            if (manager == null) {
                return;
            }
            manager.setTorchMode("0", false);
            manager = null;
        } catch (Exception e) {
        }
    }

    private boolean isVersionM() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    /**
     * 判断设备是否有闪光灯
     *
     * @param context
     * @return true 有 false 没有
     */
    public boolean hasFlashlight(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }
}