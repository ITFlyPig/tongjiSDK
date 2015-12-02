package com.aimeizhuyi.users.analysis.bean;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by wangyuelin on 15/7/10.
 */
public class EventBean {

    private String eventId;
    private String eventName;
    private String sf;
    private Map<String, String> eventIfo;
    private String stayTime;
   /* private String page;
    private String totalPage;
    private String buyerid;
    private String cid;
    private String stockid;
    private String price;
    private String suboid;
    private String payid;
    private String etype;
    private String searchkey;
    private String share;
    private String pcatid;
    private String subcatid;
    private String brandid;
    private String staytime;
    private String strategyVer;*/

    public String getEventId() {
        return eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public String getSf() {
        return sf;
    }

    public Map<String, String> getEventIfo() {
        return eventIfo;
    }
    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public void setSf(String sf) {
        this.sf = sf;
    }

    public void setEventIfo(Map<String, String> eventIfo) {
        this.eventIfo = eventIfo;
    }

    public String getStayTime() {
        return stayTime;
    }

    public void setStayTime(String stayTime) {
        this.stayTime = stayTime;
    }

    public JSONObject getJsonObject(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("eventId", eventId);
            jsonObject.put("eventName", eventName);
            jsonObject.put("sf", TextUtils.isEmpty(sf)? "0" : sf);
            jsonObject.put("stayTime", stayTime);
            if(eventIfo != null){
                Iterator it = eventIfo.entrySet().iterator();
                while (it.hasNext()){
                    Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
                    jsonObject.put(entry.getKey(), entry.getValue());
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

}
