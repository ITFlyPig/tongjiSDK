package com.aimeizhuyi.users.analysis.bean;

import java.util.Map;

/**
 * Created by wangyuelin on 15/7/12.
 */
public class ADBean {

    private String eventId;
    private String eventName;
    private String sf;
    private Map<String, String> eventIfo;
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
}



