package com.aimeizhuyi.users.analysis.utils;

import android.content.Context;
import android.text.TextUtils;

import com.aimeizhuyi.users.analysis.common.DeviceInfo;

import java.util.UUID;

/**
 * Created by wangyuelin on 15/7/13.
 */
public class Session {
    public static long time = 1000*60;//session的生成时间间隔

    public static String session;

    public static String generateSeesion( Context context){
        String session = "";
        if (context == null){
            return session;
        }
            //生成新的Seesion
            String dvid = new DeviceInfo(context.getApplicationContext(), false).getDvid();
            if (TextUtils.isEmpty(dvid)){
                session = UUID.randomUUID() + MD5Uitl.md5(System.currentTimeMillis()+"");
            }else{
                session  = dvid + MD5Uitl.md5(System.currentTimeMillis()+"");
            }


        return session;

    }
}
