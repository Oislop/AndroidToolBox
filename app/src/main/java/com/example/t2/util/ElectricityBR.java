package com.example.t2.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;

public class ElectricityBR extends BroadcastReceiver {
    Dialog dialog=null;
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction())) {
            int level = intent.getIntExtra("level", 0);				//当前电量
            int scale = intent.getIntExtra("scale", 0);				//总电量
            int voltage = intent.getIntExtra("voltage", 0);			//电压
            int temperature = intent.getIntExtra("temperature", 0);		//当前电池温度
            String technology = intent.getStringExtra("technology");	//电池类型
            if(dialog==null){
                dialog = new AlertDialog.Builder(context)
                        .setTitle("电池电量")//设置对话窗标题
                        .setMessage("电池电量为："
                                        + String.valueOf(level * 100 / scale)
                                        + "%\n" + "电池电压为："
                                        + String.valueOf((float)voltage / 1000) + "v"
                                        + "\n电池类型为：" + technology + "\n" + "电池温度为："
                                        + String.valueOf((float)temperature / 10) + "°C")  //设置消息内容

                        .setNegativeButton("关闭",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface arg0,
                                                        int arg1) {                                   //设置按钮
                                    }
                                }).create();
                dialog.show();                                               //显示电池信息
            }
        }
    }
}
