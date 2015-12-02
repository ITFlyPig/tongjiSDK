package com.aimeizhuyi.users.analysis.common;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.aimeizhuyi.users.analysis.utils.MD5Uitl;

import java.io.DataOutputStream;
import java.io.File;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Formatter;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by wangyuelin on 15/7/9. 某些不会变的东西获得之后就可以持久化
 */
public class DeviceInfo {
    private static  final String TAG = "DeviceInfo.class";

    private  String dvid;//(必选) 系统选中的唯一设备ID",

    private String uniqid; //(可选) 应用生成的唯一设备id

    private String ip; //(必选)客户端ip地址 公网IP

    private String dt; //(必选)设备类型"

    private String os; //(必选)os系统

    private String osv; //(必选)os版本

    private String net; //(必选)网络类型 1、wifi 2、2G 3、3G  4、4G、5 其他

    private String mac; //(可选)"12位MAC地址90B11C5AB8FF （Android使用）

    private String imei; //(可选)"手机串号",

//    private String model; //(可选)设备型号

    private String brand;  //(可选)设备品牌

    private String jd; //(可选)是否越狱或root (0为不越狱或root、1为越狱或root)

    private String srw; //(可选)"屏幕分辨率宽

    private String srh; //(可选)"屏幕分辨率高

    private String den; //(可选)"像素密度

    private String cc; //(可选)"国家代码

    private String lang; //(可选)"终端语言

    private String lon; //(可选)"经度"

    private String lat; //(可选)"纬度

    private String lac; //(可选)"位置区号码"

    private String dn; //(可选)"Device Name设备名称"

    private String mcc; //(可选)"运营商国家代码

    private String mnc; //(可选)"运营商网络代码

    private String  ca; //(可选)"运营商显示名

    private String kst; //(可选)"设备启动时间"

    private String rm; //(可选)"接入点路由器的MAC"

    private String rs; //(可选)"接入点路由器的SSD",

    private String cell; //(可选)"小区网号",

    private String mt; //(可选) "移动技术标准",

    private String  ua; //(可选) "浏览器 UA信息 UserAgent"  由用户自己填入，没法获取

    private Context context;

    private static DeviceInfo deviceInfo;


    public String getDvid() {
        return dvid;

    }

    public String getUniqid() {
        PrefManager prefManager = new PrefManager();
        String uniqid =  prefManager.getUniqid(context);
        if(TextUtils.isEmpty(uniqid)){
            uniqid = UUID.randomUUID().toString();
            //保存
            prefManager.saveUniqid(context, uniqid);
        }
        return uniqid;
    }

    public String getIp() {


            String ip = "";
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            if(wifiInfo != null){
                ip =  intToIp(wifiInfo.getIpAddress());
            }

        return ip;

//            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();)
//            {
//                NetworkInterface intf = en.nextElement();
//                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();)
//                {
//                    InetAddress inetAddress = enumIpAddr.nextElement();
//                    if (!inetAddress.isLoopbackAddress())
//                    {
//                        android.text.format.Formatter.formatIpAddress()
//                        return inetAddress.getHostAddress().toString();
//                    }
//                }
//            }
//        try
//        {
//        }
//        catch (SocketException ex)
//        {
//            Log.e(TAG, ex.toString());
//        }
//
//        return "";


    }

    private String intToIp(int i) {

        return (i & 0xFF ) + "." +
                ((i >> 8 ) & 0xFF) + "." +
                ((i >> 16 ) & 0xFF) + "." +
                ( i >> 24 & 0xFF) ;
    }

    public String getDt() {
            Build build = new Build();
            return build.MODEL;
    }

    public String getOs() {
            return "Android";

    }

    public String getOsv() {

            return  Build.VERSION.RELEASE;
    }

    public String getNet() {
        return NetUtil.getNetType(context)+"";
    }

    public String getMac() {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        if(info != null)
        return info.getMacAddress();
        return "";
    }

    public String getImei() {
        return getDvid();
    }

    public String getBrand() {
        return Build.BRAND;
    }

    public String getJd() {

        String status = "";
        try{
            if ((!new File("/system/bin/su").exists()) && (!new File("/system/xbin/su").exists())){
                status = status+"0";
            } else {
                status = status+"1";
            }
        } catch (Exception e) {

        }
        return status;
    }

    public String getSrw() {
        return srw;
    }

    public String getSrh() {
        return srh;
    }

    public String getDen() {
        return den;
    }

    public String getCc() {
        return cc;
    }

    public String getLang() {
        return lang;
    }

    public String getLon() {
        return lon;
    }

    public String getLat() {
        return lat;
    }

    public String getLac() {
        return lac;
    }

    public String getDn() {
        return dn;
    }

    public String getMcc() {
        return mcc;
    }

    public String getMnc() {
        return mnc;
    }

    public String getCa() {
        return ca;
    }

    public String getKst() {
        return kst;
    }

    public String getRm() {
        return rm;
    }

    public String getRs() {
        return rs;
    }

    public String getCell() {
        return cell;
    }

    public String getMt() {
        return mt;
    }

    public String getUa() {
        return ua;
    }

    public void setDvid(String dvid) {
        this.dvid = dvid;
    }

    public void setUniqid(String uniqid) {
        this.uniqid = uniqid;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setDt(String dt) {
        this.dt = dt;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public void setOsv(String osv) {
        this.osv = osv;
    }

    public void setNet(String net) {
        this.net = net;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setJd(String jd) {
        this.jd = jd;
    }

    public void setSrw(String srw) {
        this.srw = srw;
    }

    public void setSrh(String srh) {
        this.srh = srh;
    }

    public void setDen(String den) {
        this.den = den;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public void setLac(String lac) {
        this.lac = lac;
    }

    public void setDn(String dn) {
        this.dn = dn;
    }

    public void setMcc(String mcc) {
        this.mcc = mcc;
    }

    public void setMnc(String mnc) {
        this.mnc = mnc;
    }

    public void setCa(String ca) {
        this.ca = ca;
    }

    public void setKst(String kst) {
        this.kst = kst;
    }

    public void setRm(String rm) {
        this.rm = rm;
    }

    public void setRs(String rs) {
        this.rs = rs;
    }

    public void setCell(String cell) {
        this.cell = cell;
    }

    public void setMt(String mt) {
        this.mt = mt;
    }

    public void setUa(String ua) {
        this.ua = ua;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }


    /*
    * isAll表示是否全部初时化
    * */
    public DeviceInfo(Context context, boolean isAll) {
        if(context != null){
            this.context = context;
            if(deviceInfo != null){
                deviceInfo.initDynamicData();
            }else{
                initNecessary();
                if(isAll){
                    initNoNecessary();
                }
            }
        }

    }
    
    public void initNoNecessary(){

        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        setUniqid(MD5Uitl.md5(getUniqid()));
        
        setMac(getMac());
        setImei(getImei());
        setBrand(getBrand());
        setJd(getJd());
        setLac(getLac());
        setDn(getDn());
        

        //可以让应用自己获得
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        setSrw(width+"");
        setSrh(height+"");

        setCc(manager.getSimCountryIso());

        String simOperator = manager.getSimOperator();
        if(simOperator != null && simOperator.length() >=4  ){
            //获得国家码
            String countryCode = simOperator.substring(0, 2);
            setMcc(countryCode);
            //获得网络码
            String netCode = simOperator.substring(3, 4);
            setMnc(netCode);
        }

        setCa(manager.getSimOperatorName());
    }
    
    public void initNecessary(){
    	TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        setDvid(manager.getDeviceId());
        setIp(getIp());
        setDt(getDt());
        setOs(getOs());
        setOsv(getOsv());
        setNet(getNet());

    }
    
    
    public JSONObject getJsonObject(){
    	JSONObject jsonObject = new JSONObject();
    	try {
			jsonObject.put("dvid", dvid);
			jsonObject.put("uniqid", uniqid);
			jsonObject.put("ip", ip);
			jsonObject.put("dt", dt);
			jsonObject.put("os", os);
			jsonObject.put("osv", osv);
			jsonObject.put("net", net);
			jsonObject.put("mac", mac);
			jsonObject.put("imei", imei);
			jsonObject.put("brand", brand);
			jsonObject.put("jd", jd);
			jsonObject.put("srw", srw);
			jsonObject.put("srh", srh);
			jsonObject.put("den", den);
			jsonObject.put("cc", cc);
			jsonObject.put("lang", lang);
			jsonObject.put("lon", lon);
			jsonObject.put("lac", lac);
			jsonObject.put("dn", dn);
			jsonObject.put("mcc", mcc);
			jsonObject.put("ca", ca);
			jsonObject.put("kst", kst);
			jsonObject.put("rm", rm);
			jsonObject.put("rs", rs);
			jsonObject.put("cell", cell);
			jsonObject.put("mt", mt);
			jsonObject.put("ua", ua);
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
    	return jsonObject;
    }


    /*
    * 动态的数据每次获取都获取最新的
    * */
    private void initDynamicData(){
        //动态的数据，经纬度和网络
        setLon(getLon());
        setLat(getLat());
        setNet(getNet());
        setIp(getIp());
        setMac(getMac());
        setNet(getNet());
    }

}
