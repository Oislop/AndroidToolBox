package com.example.t2.util;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class RootCheck {

    private static String LOG_TAG = RootCheck.class.getName();



    public static boolean appRoot() {
        try {
            Process process = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(process.getOutputStream());
            os.writeBytes("ls /data/data/\n");
            os.writeBytes("exit\n");
            os.flush();
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder builder = new StringBuilder();
            String line = null;
            while ( (line = reader.readLine()) != null) {
                builder.append(line);
                builder.append(System.getProperty("line.separator"));
            }
            String result = builder.toString();
            Log.e("tag",result);
            Log.e("tag","after Log string buffer");
            if (result.contains( "com.android.phone" )){
                return true;
            }
        } catch (IOException e) {
            return false;
        }
        return false;
    }


    /**
     * 判断手机是否root成功
     * @return 有权限返回真，没权限返回假
     * */
    public static boolean isAPPRoot() {
        int i = execRootCmdSilent("echo test"); // 通过执行测试命令来检测
        return i == 0;
    }
    /**
     * 执行命令判断有没有权限
     * @param paramString 要执行的命令
     * */
    private static int execRootCmdSilent(String paramString) {
        try {
            Process localProcess = Runtime.getRuntime().exec("su");
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
            return (Integer)result;
        } catch (Exception localException) {
            localException.printStackTrace();
            return -1;
        }
    }


    public static boolean isRoot() {
        return checkRootMethod1() || checkRootMethod2() || checkRootMethod3();
    }

    private static boolean checkRootMethod1() {
        String buildTags = android.os.Build.TAGS;
        boolean root = buildTags != null && buildTags.contains("test-keys");
        Log.d("*** DEBUG1 ***", "checkRootMethod1 "+root);
        return root;
    }

    private static boolean checkRootMethod2() {

        String[] paths = {"/system/app/Superuser.apk", "/sbin/su", "/system/bin/su", "/system/xbin/su", "/data/local/xbin/su", "/data/local/bin/su", "/system/sd/xbin/su",
                "/system/bin/failsafe/su", "/data/local/su", "/su/bin/su"};
        for (String path : paths) {
            if (new File(path).exists()) {
                Log.d("*** DEBUG2 ***", "checkRootMethod2 true");
                return true;
            }

        }
        Log.d("*** DEBUG2 ***", "checkRootMethod2 false");
        return false;
    }

    public static boolean checkRootMethod3() {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(new String[]{"/system/xbin/which", "su"});
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            if (in.readLine() != null) {
                Log.d("*** DEBUG3 ***", "checkRootMethod3 true");
                return true;
            }

            Log.d("*** DEBUG3 ***", "checkRootMethod3 false");
            return false;
        } catch (Throwable t) {
            Log.d("*** DEBUG3 ***", "checkRootMethod3 false");
            return false;
        } finally {
            if (process != null) process.destroy();
        }
    }

}