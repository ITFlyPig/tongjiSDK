package com.aimeizhuyi.users.analysis;

import android.content.Context;
import android.text.TextUtils;


import com.mogujie.utils.MGVegetaGlass;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangyuelin on 15/7/14.
 */
public class CollectUserData {

    /*
    * 事件的打点
    * @param eventId 事件的id
    * @param eventName  事件的名字
    * @param map  其它的参数
    * */
    public static void onEvent(Context context, String eventId, String eventName, Map<String, String> map) {

        if (map == null)
            map = new HashMap<>();

        String url = map.get("url");
        if (!TextUtils.isEmpty(url)) {
            try {
                url = URLEncoder.encode(url, "utf-8");
                map.put("url", url);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        Map<String, Object> event_map = new HashMap<>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (entry != null){
                event_map.put(entry.getKey(),entry.getValue());
            }
        }
        MGVegetaGlass.instance().event(eventId, event_map);
        DataCollect.onEvent(context, eventId, eventName, map, 8);

    }

    public static void onResume(Context context, String pageId, String pageName) {
        DataCollect.onResume(context, pageId, pageName);
    }


    public static void onPause(Context context, String pageId, String pageName) {
        DataCollect.onPause(context, pageId, pageName);
    }


    public static void onPause(Context context, String pageId, String pageName, HashMap<String, String> params) {
        DataCollect.onPause(context, pageId, pageName, params);
    }

    /*
    * @params clientcode:异常码  e：异常对象
    * */
    public static void onExecption(final Context context , final String clientcode, final Exception e) {
        DataCollect.onExecption(context, clientcode, e);

    }

    /*
    * @params clientcode:异常码  eStr：自定义异常信息
    * */
    public static void onExecption(final Context context , final String clientcode , final String eStr) {

        DataCollect.onExecption(context, clientcode, eStr);
    }

    /*
    * 设置用户的唯一标识
    * */
    public static void setUid(String uid){
        DataCollect.setUid(uid);
    }

    public static void setDebugMode(boolean debug){
        DataCollect.setDebugMode(debug);//测试模式，打印出需要的信息
    }

}
