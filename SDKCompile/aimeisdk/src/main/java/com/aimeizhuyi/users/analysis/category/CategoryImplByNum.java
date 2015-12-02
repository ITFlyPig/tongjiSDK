package com.aimeizhuyi.users.analysis.category;

import android.content.Context;
import android.util.Log;

import com.aimeizhuyi.users.analysis.NetUpload;
import com.aimeizhuyi.users.analysis.bean.LogDataBean;
import com.aimeizhuyi.users.analysis.common.Config;
import com.aimeizhuyi.users.analysis.common.NetUtil;
import com.aimeizhuyi.users.analysis.common.ThreadPool;
import com.aimeizhuyi.users.analysis.dao.LogDataDao;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ObjectStreamException;
import java.util.ArrayList;

/**
 * Created by wangyuelin on 15/7/9.
 *
 * 使用当日志达到一定数量时上传的策略
 */
public class CategoryImplByNum implements CategoryInterface {
    private static Object lock = new Object();

    public static  int NUM = 4;//数量达到20就上传


    private ArrayList<LogDataBean> willUploadList;//将要上传的日志的队列


    public CategoryImplByNum() {
        willUploadList = new ArrayList<LogDataBean>();
    }

    private LogDataDao logDataDao;


    @Override
    public void uploadDataCategory(final LogDataBean logData, final Context context) {
        ThreadPool.submit(new Runnable() {
            @Override
            public void run() {

                synchronized (lock) {
                    if (context == null)
                        return;
                    boolean isNetOk = false;
                    if(NetUtil.getNetType(context) != NetUtil.OTHER){
                        isNetOk = true;
                    }

                    if (logDataDao == null) {
                        logDataDao = new LogDataDao(context);
                    }

                    if(!isNetOk){//没有网络直接存贮
                        logDataDao.insert((LogDataBean) logData);
                        return;
                    }
                    if (logData == null && context != null) {//强制上传
                        getAndUpload(logData);
                        return;
                    }
                    int num = logDataDao.getLogCount();
                    if(Config.isDebug)
                    Log.d("tt", "num：" + num);

                    if (num >= (NUM - 1)) {
                        //触发上传的条件
                        if(Config.isDebug)
                        Log.i("tt", "出发上传的条件：NUM：" + NUM + "   count:" + num);
                        getAndUpload(logData);
                    } else {
                        if(Config.isDebug)
                        Log.i("tt", "为单条的时候存储，存储的数据为："+logData.getLogContent());
                        logDataDao.insert((LogDataBean) logData);
                    }
                }
            }
        });

    }


    /*
    *
    * 获得数据并上传
    * */
    private void getAndUpload(final LogDataBean logDataBean){
        getAllFromDb();
        //在加上没有存进数据库的那条
        if(Config.isDebug)
        Log.i("tt", "在加上没有存进数据库的那条");
        willUploadList.add((LogDataBean)logDataBean);

        if(willUploadList.size() > 0){

            if(!uploadLog()){
                if(Config.isDebug)
            	Log.i("tt", "上传失败，放进数据库，等待下次一起传递");
                //上传失败，放进数据库，等待下次一起传递
                for(int i = 0; i < willUploadList.size(); i++){
                    logDataDao.insert(willUploadList.get(i));
                }

            }
        }
    }



    /*
    *
    * 作用：从数据库取出所有的数据，添加到将要上传的队列中
    *
    *
    *
    * */
    private void getAllFromDb(){
    	willUploadList =  logDataDao.getAll();
        //删除数据库里面的数据
        for(int i = willUploadList.size() - 1; i >= 0; i--){
            String id = willUploadList.get(i).getId()+"";
            logDataDao.delete(id);
        }


    }

    /*
    *
    * 上传日志
    *
    * */
    private boolean  uploadLog(){

        JSONArray jsonArray = new JSONArray();
        for(int i = 0;i < willUploadList.size();i++){
        	LogDataBean logdata = willUploadList.get(i);
        	try {
				jsonArray.put(new JSONObject(logdata.getLogContent()));
			} catch (JSONException e) {
				e.printStackTrace();
			}
        }
        String jsonStr = jsonArray.toString();

        if(Config.isDebug)
        Log.i("tt", "上传到服务器的数据："+jsonStr);
        boolean success = false;
        int count = 1;
        while (!success && count <= Config.TRY_TIME){
            success =  NetUpload.commitData(jsonStr);
            count++;
            if(Config.isDebug)
            Log.i("tt", "上传结果：success:"+success+"  重试的次数count:"+count);
        }

        return success;
    }


}
