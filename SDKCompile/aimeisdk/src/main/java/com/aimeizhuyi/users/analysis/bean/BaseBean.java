package com.aimeizhuyi.users.analysis.bean;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.aimeizhuyi.users.analysis.common.Config;
import com.aimeizhuyi.users.analysis.common.Constant;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by wangyuelin on 15/7/10.
 * 一些基本的不会变的只需要获取一次的信息
 */
public class BaseBean {
    public static String channel;//渠道
    public static String appId;
    public static String appver;//应用的版本
    public static String test;//测试模式标识(0、正常 1、测试)
    public static void init(Context context) {
        if (context == null)
            return;

        if (TextUtils.isEmpty(channel)) {
            channel = getAppMetaData(context, "TD_CHANNEL_ID");
        }

            appId = Constant.APP_ID;
        if (TextUtils.isEmpty(appver)) {
            appver = getAPPVersion(context);
        }
        if (Config.isDebug) {
            test = "1";
        } else {
            test = "0";
        }


    }


    private static String getChannelIdFromAsset(Context context) {
        BufferedReader reader = null;
        StringBuilder sb = new StringBuilder();
        try {
            reader = new BufferedReader(new InputStreamReader(context.getResources().getAssets().open("cpid")));
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                sb.append(tempString);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }

        return sb.toString();

    }

    private static String getAPPVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取application中指定的meta-data
     * @return 如果没有获取成功(没有对应值，或者异常)，则返回值为空
     */
    public static String getAppMetaData(Context ctx, String key) {
        if (ctx == null || TextUtils.isEmpty(key)) {
            return null;
        }
        String resultData = null;
        try {
            PackageManager packageManager = ctx.getPackageManager();
            if (packageManager != null) {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        resultData = applicationInfo.metaData.getString(key);
                    }
                }

            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return resultData;
    }

}
