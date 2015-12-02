package com.aimeizhuyi.users.analysis.common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

/**
 * Created by wangyuelin on 15/7/9.
 */
public class NetUtil {

    public static final int WIFI = 1;
    public static final int G2 = 2;
    public static final int G3 = 3;
    public static final int G4  = 4;
    public static final int OTHER = 5;



    public static int getNetType(Context context){
        int netType = OTHER;
        if(context == null)
            return netType;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo == null)
            return netType;
        if( networkInfo.getType() == ConnectivityManager.TYPE_WIFI){
            netType = WIFI;
        }else if(networkInfo.getType() == ConnectivityManager.TYPE_MOBILE){
            int type = networkInfo.getSubtype();
            switch (type){
                case TelephonyManager.NETWORK_TYPE_GPRS:
                case TelephonyManager.NETWORK_TYPE_EDGE:
                case TelephonyManager.NETWORK_TYPE_CDMA:
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                case TelephonyManager.NETWORK_TYPE_IDEN:
                    netType = G2;
                    break;
                case TelephonyManager.NETWORK_TYPE_UMTS:
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                case TelephonyManager.NETWORK_TYPE_HSPA:
                case TelephonyManager.NETWORK_TYPE_EVDO_B:
                case TelephonyManager.NETWORK_TYPE_EHRPD:
                case TelephonyManager.NETWORK_TYPE_HSPAP:
                    netType = G3;
                    break;
                case TelephonyManager.NETWORK_TYPE_LTE:
                    netType = G4;
                    break;
                default:
                    netType = OTHER;
                    break;

            }
        }
        return netType;
    }
}
