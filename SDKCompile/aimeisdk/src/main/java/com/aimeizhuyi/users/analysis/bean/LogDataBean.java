package com.aimeizhuyi.users.analysis.bean;

/**
 * Created by wangyuelin on 15/7/12.
 * 最终存入数据库的数据
 */
public class LogDataBean {
    private int id;
    private String logContent;

    public int getId() {
        return id;
    }

    public String getLogContent() {
        return logContent;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLogContent(String logContent) {
        this.logContent = logContent;
    }
}
