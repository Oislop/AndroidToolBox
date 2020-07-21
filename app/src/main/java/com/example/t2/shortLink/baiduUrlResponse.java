package com.example.t2.shortLink;

import com.google.gson.annotations.SerializedName;

public class baiduUrlResponse {
    @SerializedName("Code")
    private int code;

    @SerializedName("ErrMsg")
    private String errMsg;

    @SerializedName("LongUrl")
    private String longUrl;

    @SerializedName("ShortUrl")
    private String shortUrl;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public String getLongUrl() {
        return longUrl;
    }

    public void setLongUrl(String longUrl) {
        this.longUrl = longUrl;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }
}
