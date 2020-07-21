package com.example.t2;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.t2.Wifi.QRCodeView;
import com.example.t2.Wifi.WifiInfo;
import com.example.t2.util.RootCheck;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class WifiActivity extends AppCompatActivity {

    private static boolean mHaveRoot = false;

    ProgressDialog progress_dialog;
    Process localProcess;
    private ListView mListView;
    private Toolbar toolBar;


    private ArrayList<WifiInfo> mWifiInfoList;
    private MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi);
        toolBar = (Toolbar) findViewById(R.id.toolbar);
        toolBar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolBar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);
        get_root();
        if (RootCheck.isRoot()) {
            setListView();
        } else {
            showDialog();
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void showDialog() {
        // 这里的属性可以一直设置，因为每次设置后返回的是一个builder对象
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String sl = "手机未获得ROOT权限，需要获得ROOT权限才能进行该操作";
        builder.setTitle("警告")//设置对话窗标题
                .setMessage(sl).setNegativeButton("关闭",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        //设置按钮

                    }
                });
        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

    }


    /**
     * 判断app是否获得root权限
     *
     * @return
     */
    public boolean isAPPRoot() {
        int i = execRootCmdSilent("echo test"); // 通过执行测试命令来检测
        if (i != 0) {
            showDialog();
        }
        return i == 0;
    }

    private int execRootCmdSilent(String paramString) {
        try {
            localProcess = Runtime.getRuntime().exec("su");
            Object localObject = localProcess.getOutputStream();
            DataOutputStream localDataOutputStream = new DataOutputStream((OutputStream) localObject);
            String str = String.valueOf(paramString);
            localObject = str + "\n";
            localDataOutputStream.writeBytes((String) localObject);
            localDataOutputStream.flush();
            localDataOutputStream.writeBytes("exit\n");
            localDataOutputStream.flush();
            localProcess.waitFor();
            int result = localProcess.exitValue();
            mHaveRoot = true;

            Log.d("*** DEBUG ***", "ROOT suc " + result);
            return (Integer) result;
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("*** DEBUG ***", "ROOT REE " + e.getMessage());
            return -1;
        }
    }

    /**
     * 应用程序运行命令获取 Root权限，设备必须已破解(获得ROOT权限)
     *
     * @return 应用程序是/否获取Root权限
     */
    public void get_root() {
        //Toast.makeText(this, "已经具有ROOT权限!", Toast.LENGTH_LONG).show();
        try {
            Log.d("*** DEBUG ***", "ROOT " + mHaveRoot);
            progress_dialog = new ProgressDialog(this);
            if (!mHaveRoot) {
                progress_dialog.setTitle("ROOT");
                progress_dialog.setMessage("正在获取ROOT权限...");
                progress_dialog.setCanceledOnTouchOutside(false);
                progress_dialog.show();
            }
            new Thread() {
                public void run() {
                    //休息一会
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            isAPPRoot();
                        }
                    });
                    progress_dialog.dismiss();
                }

            }.start();

        } catch (Exception e) {
            Toast.makeText(this, "获取ROOT权限时出错!", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        getWifiInfo();
    }

    private void setListView() {
        mListView = (ListView) findViewById(R.id.listview);
        myAdapter = new MyAdapter();
        mListView.setAdapter(myAdapter);
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // 复制剪切板
                ClipboardManager cmb = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                cmb.setPrimaryClip(ClipData.newPlainText(getString(R.string.item_pasword_hint), mWifiInfoList.get(position)
                        .getPassword()));
                Toast.makeText(getApplicationContext(), R.string.copy_success, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //生成二维码让小米链接
                Intent intent = new Intent(WifiActivity.this, QRCodeView.class);
                intent.putExtra("str", "WIFI:T:" + mWifiInfoList.get(position).getKeymgmt() + ";P:\"" + mWifiInfoList.get(position).getPassword() + "\";S:" + mWifiInfoList.get(position).getName() + ";");
                startActivity(intent);
            }
        });
    }

    private void getWifiInfo() {
        Process process = null;
        DataOutputStream dataOutputStream = null;
        DataInputStream dataInputStream = null;
        StringBuffer wifiConf = new StringBuffer();
        try {
            process = Runtime.getRuntime().exec("su");
            dataOutputStream = new DataOutputStream(process.getOutputStream());
            dataInputStream = new DataInputStream(process.getInputStream());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                dataOutputStream.writeBytes("cat /data/misc/wifi/WifiConfigStore.xml\n");
            } else {
                dataOutputStream.writeBytes("cat /data/misc/wifi/wpa_supplicant.conf\n");
            }
            dataOutputStream.writeBytes("exit\n");
            dataOutputStream.flush();
            InputStreamReader inputStreamReader = new InputStreamReader(dataInputStream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                wifiConf.append(line);
            }
            bufferedReader.close();
            inputStreamReader.close();
            process.waitFor();
        } catch (Exception e) {
            return;
        } finally {
            try {
                if (dataOutputStream != null) {
                    dataOutputStream.close();
                }
                if (dataInputStream != null) {
                    dataInputStream.close();
                }
                process.destroy();
            } catch (Exception e) {

            }
        }

        mWifiInfoList = new ArrayList<>();
        String strO;
        String strO1;
        String strO2;
        String strO3;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            strO = "\"SSID\">&quot;([^\"]+)&quot;";
            strO1 = "\"PreSharedKey\">&quot;([^\"]+)&quot;";
            strO2 = "(<WifiConfiguration>[^>])([\\s\\S]*?)(<null name=\"WEPKeys\" />)";
        } else {
            strO = "network=\\{([^\\}]+)\\}";
            strO1 = "ssid=\"([^\"]+)\"";
            strO2 = "psk=\"([^\"]+)\"";
        }

        Pattern network = Pattern.compile(strO2, Pattern.DOTALL);
        Matcher networkMatcher = network.matcher(wifiConf.toString());
        WifiInfo wifiInfo;
        while (networkMatcher.find()) {
            String networkBlock = networkMatcher.group();
            Log.d("**** DEG ****", "suc" + networkBlock);
            Pattern ssid = Pattern.compile(strO);
            Matcher ssidMatcher = ssid.matcher(networkBlock);
            if (ssidMatcher.find()) {
                wifiInfo = new WifiInfo();
                String name = ssidMatcher.group(1);
                wifiInfo.setName(ssidMatcher.group(1));
                Pattern psk = Pattern.compile(strO1);
                Matcher pskMatcher = psk.matcher(networkBlock);

                if (pskMatcher.find()) {
                    wifiInfo.setPassword(pskMatcher.group(1));
                } else {
                    wifiInfo.setPassword(getString(R.string.empty_password));
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    strO3 = "<string name=\"ConfigKey\">&quot;" + name + "&quot;([^\"]+)</string>";
                } else {
                    strO3 = "psk=\"([^\"]+)\"";
                }
                Pattern keymgmt = Pattern.compile(strO3);
                Matcher mkeymgmt = keymgmt.matcher(networkBlock);
                if (mkeymgmt.find()) {
                    wifiInfo.setKeymgmt(mkeymgmt.group(1));
                } else {
                    wifiInfo.setKeymgmt("");
                }
                mWifiInfoList.add(wifiInfo);
            }
        }
        // 列表倒序
        Collections.reverse(mWifiInfoList);
        myAdapter.notifyDataSetChanged();
    }


    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mWifiInfoList == null ? 0 : mWifiInfoList.size();
        }

        @Override
        public WifiInfo getItem(int position) {
            return mWifiInfoList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(WifiActivity.this).inflate(R.layout.item_list, null);
                holder.name = (TextView) convertView.findViewById(R.id.item_name);
                holder.password = (TextView) convertView.findViewById(R.id.item_password);
                holder.keymgmt = (TextView) convertView.findViewById(R.id.item_keymgmt);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            WifiInfo wifi = getItem(position);

            holder.name.setText(getString(R.string.iten_name_hint) + wifi.getName());
            holder.password.setText(getString(R.string.item_pasword_hint) + wifi.getPassword());
            holder.keymgmt.setText(getString(R.string.item_keymgmt_hint) + wifi.getKeymgmt());
            return convertView;
        }

        class ViewHolder {
            TextView name;
            TextView password;
            TextView keymgmt;
        }
    }


}
