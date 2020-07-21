package com.example.t2.shortLink;

import com.google.gson.annotations.SerializedName;

public class sinaUrlResponse {
    @SerializedName("type")
    private int type;

    @SerializedName("url_long")
    private String url_long;

    @SerializedName("url_short")
    private String url_short;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUrl_long() {
        return url_long;
    }

    public void setUrl_long(String url_long) {
        this.url_long = url_long;
    }

    public String getUrl_short() {
        return url_short;
    }

    public void setUrl_short(String url_short) {
        this.url_short = url_short;
    }
}
