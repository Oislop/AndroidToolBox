package com.example.t2.shortLink;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class SinaTcn {
    static String actionUrl = "http://api.t.sina.com.cn/short_url/shorten.json";
    static String shortToLongString = "http://api.t.sina.com.cn/short_url/expand.json";
    static String APPKEY = "2815391962,31641035,3271760578,3925598208,209692453,209682332,209691374,209686590,209689965";

    public static String createLongUrl(String longUrl, String key) {
        String params = "url_short=" + longUrl + "&source=" + key;
        BufferedReader reader = null;
        try {
            // 创建连接
            URL url = new URL(shortToLongString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);

            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.setRequestMethod("GET"); // 设置请求方式
            // 设置发送数据的格式

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

            // 去掉[]
            String str = "[\\[\\]]";
            res = res.replaceAll(str, "");
            // System.out.println(res);

            // 抽取生成短网址
            sinaUrlResponse urlResponse = new Gson().fromJson(res, sinaUrlResponse.class);
            if (urlResponse.getType() < 100) {
                return urlResponse.getUrl_long();
            } else {
                System.out.println(urlResponse.getType());
            }

            return ""; // TODO：自定义错误信息
        } catch (IOException e) {
            // TODO
            e.printStackTrace();
        }
        return ""; // TODO：自定义错误信息
    }

    public static String sinaShortUrl(String longUrl) {
        longUrl = java.net.URLEncoder.encode(longUrl);
        String appkey = APPKEY;
        String[] sourceArray = appkey.split(",");
        for (String key : sourceArray) {
            String shortUrl = createShortUrl(longUrl, key);
            if (shortUrl != null) {
                return shortUrl;
            }
        }
        return null;
    }


    /**
     * 还原长网址短网址
     *
     * @param longUrl 短网址
     * @return 成功：长网址
     *
     */

    public static String createShortUrl(String longUrl, String key) {
        String params = "url_long=" + longUrl + "&source=" + key;
        BufferedReader reader = null;
        try {
            // 创建连接
            URL url = new URL(actionUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);

            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.setRequestMethod("GET"); // 设置请求方式
            // 设置发送数据的格式

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

            // 去掉[]
            String str = "[\\[\\]]";
            res = res.replaceAll(str, "");
            // System.out.println(res);

            // 抽取生成短网址
            sinaUrlResponse urlResponse = new Gson().fromJson(res, sinaUrlResponse.class);
            if (urlResponse.getType() < 100) {
                return urlResponse.getUrl_short();
            } else {
                System.out.println(urlResponse.getType());
            }

            return ""; // TODO：自定义错误信息
        } catch (IOException e) {
            // TODO
            e.printStackTrace();
        }
        return ""; // TODO：自定义错误信息
    }

    public static String sinaLongUrl(String longUrl) {
        longUrl = java.net.URLEncoder.encode(longUrl);
        String appkey = APPKEY;
        String[] sourceArray = appkey.split(",");
        for (String key : sourceArray) {
            String shortUrl = createLongUrl(longUrl, key);
            if (shortUrl != null) {
                return shortUrl;
            }
        }
        return null;
    }
}
