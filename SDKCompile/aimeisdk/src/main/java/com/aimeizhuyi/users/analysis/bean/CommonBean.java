package com.aimeizhuyi.users.analysis.bean;

import com.aimeizhuyi.users.analysis.common.Config;
import com.aimeizhuyi.users.analysis.utils.Session;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by wangyuelin on 15/7/12.
 */
public class CommonBean {
    private String  evt;
    private String logversion;
    private String cheat;
    private String offline;
    private String stime;
    private String ctime;
    private String sessionId;
    private String source;
    private String appId;
    private String appver;
    private String test;
    private String onlineIp;


    public CommonBean() {
        ctime = getCtime();
        source = BaseBean.channel;
        appId = BaseBean.appId;
        appver = BaseBean.appver;
        test = BaseBean.test;

    }

    public String getEvt() {
        return evt;
    }

    public String getLogversion() {
        return logversion;
    }

    public String getCheat() {
        return cheat;
    }

    public String getOffline() {
        return offline;
    }

    public String getStime() {
        return stime;
    }

    public String getCtime() {
        return System.currentTimeMillis()+"";
    }

    public String getSessionId() {
        return Session.session;
    }

    public String getSource() {
        return source;
    }

    public String getAppId() {
        return appId;
    }

    public String getAppver() {
        return appver;
    }

    public String getTest() {
        return test;
    }

    public String getOnlineIp() {
        return onlineIp;
    }

    public void setEvt(String evt) {
        this.evt = evt;
    }

    public void setLogversion(String logversion) {
        this.logversion = logversion;
    }

    public void setCheat(String cheat) {
        this.cheat = cheat;
    }

    public void setOffline(String offline) {
        this.offline = offline;
    }

    public void setStime(String stime) {
        this.stime = stime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public void setAppver(String appver) {
        this.appver = appver;
    }

    public void setTest(String test) {
        this.test = test;
    }

    public void setOnlineIp(String onlineIp) {
        this.onlineIp = onlineIp;
    }

    public JSONObject getJsonObject(){

        JSONObject jsonObject = new JSONObject();
        try {
        jsonObject.put("evt",evt+"");
        jsonObject.put("logversion",logversion+"");
        jsonObject.put("offline",offline+"");
        jsonObject.put("stime",stime+"");
            jsonObject.put("ctime",ctime+"");
        jsonObject.put("sessionId",Session.session+"");
        jsonObject.put("source",source+"");
        jsonObject.put("appid",appId+"");
        jsonObject.put("appver",appver+"");
        jsonObject.put("test",test+"");
        jsonObject.put("onlineip",onlineIp+"");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;

    }
}




