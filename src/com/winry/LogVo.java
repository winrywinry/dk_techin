package com.winry;

public class LogVo {
    String status;
    String url;
    String browser;
    String timestamp;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "LogVo{" +
                "status='" + status + '\'' +
                ", url='" + url + '\'' +
                ", browser='" + browser + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}
