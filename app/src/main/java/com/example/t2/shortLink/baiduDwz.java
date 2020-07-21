package com.example.t2.shortLink;

import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class baiduDwz {
    final static String CREATE_API_LONG_TO_SHORT= "https://dwz.cn/admin/v2/create";
    final static String CREATE_API_SHORT_TO_LONG = "https://dwz.cn/admin/v2/query";
    final static String TOKEN = "b703a9bff953c0012487c538d0bf3443"; // TODO:设置Token
    static String termOfValidity = "long-term";

    /**
     * 创建短网址
     *
     * @param longUrl
     *            长网址：即原网址
     *        termOfValidity
     *            有效期：1-year或long-term
     * @return  成功：短网址
     *          失败：返回空字符串
     */
    /**
     * 还原长网址短网址
     *
     * @param shortUrl 短网址
     * @return  成功：长网址
     *
     */
    public String queryLongUrl(String shortUrl) {
        String params = "{\"shortUrl\":\""+ shortUrl + "\"}";

        BufferedReader reader = null;
        try {
            // 创建连接
            URL url = new URL(CREATE_API_SHORT_TO_LONG);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestMethod("POST"); // 设置请求方式
            connection.setRequestProperty("Content-Type", "application/json"); // 设置发送数据的格式
            connection.setRequestProperty("Token", TOKEN); // 设置发送数据的格式");

            // 发起请求
            connection.connect();
            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), "UTF-8"); // utf-8编码
            out.append(params);
            out.flush();
            out.close();

            // 读取响应
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String line;
            String res = "";
            while ((line = reader.readLine()) != null) {
                res += line;
            }
            reader.close();

            // 抽取生成长网址
            baiduUrlResponse urlResponse = new Gson().fromJson(res, baiduUrlResponse.class);
            if (urlResponse.getCode() == 0) {
                return urlResponse.getLongUrl();
            } else {
                Log.d("**error**","msg: " + urlResponse.getErrMsg());
                return "URL错误"; // TODO：自定义错误信息
            }


        } catch (IOException e) {
            // TODO
            e.printStackTrace();
        }
        return "URL错误"; // TODO：自定义错误信息
    }

    public static String createShortUrl(String longUrl) {
        String params = "{\"Url\":\""+ longUrl + "\"" + ",\"TermOfValidity\":\""+ termOfValidity + "\"}";

        BufferedReader reader = null;
        try {
            // 创建连接
            URL url = new URL(CREATE_API_LONG_TO_SHORT);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestMethod("POST"); // 设置请求方式
            connection.setRequestProperty("Content-Type", "application/json"); // 设置发送数据的格式
            connection.setRequestProperty("Token", TOKEN); // 设置发送数据的格式");

            // 发起请求
            connection.connect();
            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), "UTF-8"); // utf-8编码
            out.append(params);
            out.flush();
            out.close();

            // 读取响应
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String line;
            String res = "";
            while ((line = reader.readLine()) != null) {
                res += line;
            }
            reader.close();
            //System.out.println(res);

            // 抽取生成短网址
            baiduUrlResponse urlResponse = new Gson().fromJson(res, baiduUrlResponse.class);
            if (urlResponse.getCode() == 0) {
                return urlResponse.getShortUrl();
            } else {
                Log.d("**error**","msg: " + urlResponse.getErrMsg());
            }

            return ""; // TODO：自定义错误信息
        } catch (IOException e) {
            // TODO
            e.printStackTrace();
        }
        return ""; // TODO：自定义错误信息
    }

}
