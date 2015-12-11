package com.aimeizhuyi.users.analysis.bean;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by wangyuelin on 15/7/12.
 */
public class ErrorBean {

    private String clientcode;
    private String clienterrs;
    private String uid;

    public String getClientcode() {
        return clientcode;
    }

    public String getClienterrs() {
        return clienterrs;
    }

    public void setClientcode(String clientcode) {
        this.clientcode = clientcode;
    }

    public void setClienterrs(String clienterrs) {
        this.clienterrs = clienterrs;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public JSONObject getJsonObject(){
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("clientcode", clientcode);
            jsonObject.put("clienterrs", clienterrs);
            jsonObject.put("uid", uid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
