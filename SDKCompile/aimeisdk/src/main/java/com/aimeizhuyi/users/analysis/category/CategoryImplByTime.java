package com.aimeizhuyi.users.analysis.category;

import android.content.Context;
import android.util.Log;

import com.aimeizhuyi.users.analysis.NetUpload;
import com.aimeizhuyi.users.analysis.bean.LogDataBean;
import com.aimeizhuyi.users.analysis.common.Config;
import com.aimeizhuyi.users.analysis.common.NetUtil;
import com.aimeizhuyi.users.analysis.common.ThreadPool;
import com.aimeizhuyi.users.analysis.dao.ExecptionDao;
import com.aimeizhuyi.users.analysis.dao.LogDataDao;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by wangyuelin on 15/7/9.
 *
 * 每隔一定的时间就上传日志
 */
public class CategoryImplByTime implements CategoryInterface {
    private static Object lock = new Object();


    private ArrayList<LogDataBean> willUploadList;//将要上传的日志的队列


    public CategoryImplByTime() {
        willUploadList = new ArrayList<LogDataBean>();
    }

    private ExecptionDao execptionDao;

    private WeakReference<Context> wk;


    @Override
    public void uploadDataCategory(final LogDataBean logData, final Context context) {
        ThreadPool.submit(new Runnable() {
            @Override
            public void run() {

                synchronized (lock) {
                    //将异常数据存储在表里面
                    if(context == null){
                        return;
                    }
                    wk = new WeakReference<Context>(context);

                    if(execptionDao == null){
                        execptionDao = new ExecptionDao(wk.get());
                    }

                    execptionDao.insert(logData);

                    startTask(); //开启定时器
                }
            }
        });

    }


    /*
    *
    * 获得数据并上传
    * */
    private void upload(){
        if(willUploadList.size() > 0){
            if(!uploadLog()){
                if(Config.isDebug)
            	Log.i("tt", "上传失败，放进数据库，等待下次一起传递");
                //上传失败，放进数据库，等待下次一起传递
                for(int i = 0; i < willUploadList.size(); i++){
                    execptionDao.insert(willUploadList.get(i));
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
    	willUploadList =  execptionDao.getAll();
        //删除数据库里面的数据
        for(int i = willUploadList.size() - 1; i >= 0; i--){
            String id = willUploadList.get(i).getId()+"";
            execptionDao.delete(id);
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



    private Timer timer;

    private TimerTask timerTask;

    private void startTask(){
        if(timer != null){
            return;
        }else{
            timer = new Timer();
        }

        if(timerTask == null){
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    try{
                        boolean isNetOk = false;
                        if(wk.get() == null){
                            return;
                        }
                        if(NetUtil.getNetType(wk.get()) != NetUtil.OTHER){
                            isNetOk = true;
                        }

                        //获取数据库的里面的数据
                        if(isNetOk){
                            getAllFromDb();
                        }
                        //上传获得的数据，最多尝试3此
                        upload();
                    }catch (Exception e){
                        Log.w(CategoryImplByTime.this.getClass().getName(), e.getLocalizedMessage());
                    }

                }
            };
        }

        timer.schedule(timerTask, 0, Config.PER_TIME);

    }

}
