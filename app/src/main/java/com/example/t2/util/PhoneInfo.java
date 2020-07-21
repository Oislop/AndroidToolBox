package com.example.t2.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.view.View;

public class PhoneInfo extends DialogFragment {

    Dialog dialog = null;

    String brand, model, androidversion, sign, sdk;
    String device, product, cpu, board, display, id, version_codes_base, maker, user, tags;
    String hardware, host, unknown, type, time, radio, serial, cpu2;
    String cl;


    public void onReceive(Context context, Intent intent) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        product = "产品 : " + android.os.Build.PRODUCT;
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

        builder.setTitle("设备信息")//设置对话窗标题
                .setMessage(product + cl
                        + cl + cpu
                        + cl + tags
                        + cl + version_codes_base
                        + cl + model
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
                        + cl + serial).setNegativeButton("关闭",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        //设置按钮
                    }
                }).create();
        builder.show();


    }

}
