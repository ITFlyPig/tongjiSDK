package com.aimeizhuyi.users.analysis.common;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by wangyuelin on 15/7/15.
 */
public class PrefManager {
    private static final String UNIQID  ="uniqid";

    public void  saveUniqid(Context context, String uniqid){
        if(context == null)
            return;
        SharedPreferences preferences = context.getSharedPreferences(UNIQID, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(UNIQID,uniqid);
        editor.commit();
    }

    public String  getUniqid(Context context){
        if(context == null)
            return "";
        SharedPreferences preferences = context.getSharedPreferences(UNIQID, 0);
        return preferences.getString(UNIQID, "");
    }

}
