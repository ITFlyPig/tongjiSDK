package com.aimeizhuyi.users.analysis.bean;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by wangyuelin on 15/7/12.
 */
public class PageVisitBean {
    private String eventId;
    private String eventName;
    private String sourcePage;
    private String targetPage;
    private String visitedTime;
    private String stayTime;

    public String getEventId() {
        return eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public String getSourcePage() {
        return sourcePage;
    }

    public String getTargetPage() {
        return targetPage;
    }

    public String getVisitedTime() {
        return visitedTime;
    }

    public String getStayTime() {
        return stayTime;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public void setSourcePage(String sourcePage) {
        this.sourcePage = sourcePage;
    }

    public void setTargetPage(String targetPage) {
        this.targetPage = targetPage;
    }

    public void setVisitedTime(String visitedTime) {
        this.visitedTime = visitedTime;
    }

    public void setStayTime(String stayTime) {
        this.stayTime = stayTime;
    }

    public JSONObject getJsonObject(){
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("eventId",eventId);
        hashMap.put("eventName",eventName);
        hashMap.put("sourcePage",sourcePage);
        hashMap.put("targetPage",targetPage);
        hashMap.put("visitedTime",visitedTime);
        hashMap.put("stayTime",stayTime);
        return new JSONObject(hashMap);
    }
}
