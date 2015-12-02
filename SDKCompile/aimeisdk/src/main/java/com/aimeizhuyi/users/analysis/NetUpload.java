package com.aimeizhuyi.users.analysis;

import android.annotation.TargetApi;
import android.os.Build;
import android.util.Base64;
import android.util.Log;

import com.aimeizhuyi.users.analysis.common.Config;
import com.aimeizhuyi.users.analysis.common.Constant;
import com.aimeizhuyi.users.analysis.utils.GZip;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.zip.DeflaterOutputStream;

/**
 * Created by wangyuelin on 15/7/10.
 */
public class NetUpload {

    private static final int SUCC = 0;//返回成功
    private static final  int FAIl = -1;//返回失败
    private static final String TAG = "Net.class";

    private static final String SUCCESS = "1";

    @TargetApi(Build.VERSION_CODES.FROYO)

    public static boolean commitData(String data){
        if(Config.isDebug)
        Log.i("net", "net开始");
        String result = "";
        URL url = null;
        HttpURLConnection conn = null;
        OutputStream os = null;
        BufferedReader in = null;
        try{
            url = new URL(Constant.POST_URL);
            conn= (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            //设置连接超时
            conn.setConnectTimeout(50000);
            //设置读取超时
            conn.setReadTimeout(50000);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.connect();
            //写入数据
            os = conn.getOutputStream();

//            byte[] dataByte;
//            dataByte = Base64.encode(data.getBytes("UTF-8"), Base64.DEFAULT);
//            String encodedStr = new String(dataByte);
//            Log.i("net", "编码后的数据："+encodedStr);
//            StringBuffer buffer = new StringBuffer();
//            buffer.append("d=").append("5Y+v57uP5bi46KKr5oiR55yL6KeB5LiN5Ye65p2l");
//            buffer.append("d=").append(URLEncoder.encode("56yR5YKy5rGf5rmW", "utf-8"));
            if(Config.isDebug)
            Log.i("net", "net上传的字符串："+data);
            StringBuffer buffer = new StringBuffer("d=");
//            String compressStr = GZip.compress(data);
//            Log.i("tt","压缩之后的字符串：" + compressStr);
//            Log.d("tt", "解压之后的字符串："+ GZip.uncompress(compressStr));
            buffer.append(data);
//            buffer.append(compressStr);
            os.write(buffer.toString().getBytes("utf-8"));
            os.flush();
            os.close();

            //读取返回的数据
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    result += inputLine;
                }
                in.close();

            }

        } catch (MalformedURLException e) {
            if(Config.isDebug){
                Log.i(Config.SDK_NAME, TAG+" net Execption:"+ e.getLocalizedMessage());
            }
        } catch (UnsupportedEncodingException e) {
            if(Config.isDebug){
                Log.i(Config.SDK_NAME, TAG+" net Execption:"+ e.getLocalizedMessage());
            }
        } catch (ProtocolException e) {
            if(Config.isDebug){
                Log.i(Config.SDK_NAME, TAG+" net Execption:"+ e.getLocalizedMessage());
            }
        } catch (IOException e) {
            if(Config.isDebug){
                Log.i(Config.SDK_NAME, TAG+" net Execption:"+ e.getLocalizedMessage());
            }
        }finally {
            if(conn != null){
                conn.disconnect();
            }
        }


        //将返回的数据转换为json对象
        try {
            if(Config.isDebug)
            Log.i("net", "返回的字符串："+result);
            JSONObject jsonObject = new JSONObject(result);
            int ret = jsonObject.getInt("rst");
            String res = jsonObject.getString("msg");
           String chMsg = new String(Base64.decode(res,0)) ;
            if(ret == SUCC){
                return true;
            }
        } catch (JSONException e) {
            if(Config.isDebug){
                Log.i(Config.SDK_NAME, TAG+" parse net result Execption:"+ e.getLocalizedMessage());
            }
        }
        return false;
    }

}
