package com.aimeizhuyi.users.analysis.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.aimeizhuyi.users.analysis.bean.LogDataBean;
import com.aimeizhuyi.users.analysis.common.Config;
import com.aimeizhuyi.users.analysis.common.Constant;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by wangyuelin on 15/7/12.
 */
public class LogDataDao {
    private String TAG = "LogDataDao.class";

    private DBHelper dbHelper;

    public DBHelper getDbHelper() {
        return dbHelper;
    }

    public LogDataDao(Context context) {
        dbHelper = new DBHelper(context);
    }

    public LogDataDao() {
    }

    public boolean insert(LogDataBean logDataBean){

        if(logDataBean == null ){
            return false;

        }
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql = "insert into "+DBConstant.LOG_DATA_TABLE_BANE+"(log_data) values(?)";

        try {
            db.execSQL(sql, new Object[]{logDataBean.getLogContent()});
        }catch (Exception e){
            if(Config.isDebug){
                Log.i(Config.SDK_NAME, TAG+" insert Execption:"+ e.getLocalizedMessage());
            }

        }finally {
            if(db != null){
                db.close();
            }

        }
        if(Config.isDebug)
        Log.d("tt","插入成功");
        return true;
    }


    public boolean delete(String id){

        if(id == null )
            return false;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "delete from "+DBConstant.LOG_DATA_TABLE_BANE+" where id = ?";

        try {
            if(Config.isDebug)
        	Log.i("tt", "删除的数据：id"+id);
            db.execSQL(sql, new Object[]{id});
        }catch (Exception e){
            if(Config.isDebug){
                Log.i(Config.SDK_NAME, TAG+" delete Execption:"+ e.getLocalizedMessage());
            }
            return false;
        }finally {
            if(db != null){
                db.close();
            }

        }
        return true;
    }


    public boolean deleteAll(){

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "delete from event_tab ";

        try {
            db.execSQL(sql, null);
        }catch (Exception e){
            if(Config.isDebug){
                Log.i(Config.SDK_NAME, TAG+" delateAll Execption:"+ e.getLocalizedMessage());
            }
            return false;
        }finally {
            if(db != null){
                db.close();
            }

        }
        return true;
    }


    public int getLogCount(){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "select count(*) from "+DBConstant.LOG_DATA_TABLE_BANE;
        Cursor cursor = null;

        try {
            cursor = db.rawQuery(sql,null);
            cursor.moveToFirst();
            int count = cursor.getInt(0);
            return count;
        }catch (Exception e){
            if(Config.isDebug){
                Log.i(Config.SDK_NAME, TAG+" getCount Execption:"+ e.getLocalizedMessage());
            }
        }finally {
            if(cursor != null){
                cursor.close();
            }
            if(db != null){
                db.close();
            }

        }
        return 0;
    }


    public ArrayList<LogDataBean> getAll(){

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "select * from "+ DBConstant.LOG_DATA_TABLE_BANE;
        Cursor cursor = null;
        ArrayList<LogDataBean> list = new ArrayList<LogDataBean>();

        try {
            cursor = db.rawQuery(sql,null);
            while (cursor.moveToNext()){
                LogDataBean logDataBean = new LogDataBean();
                logDataBean.setId(cursor.getInt(cursor.getColumnIndex("id")));
                logDataBean.setLogContent(cursor.getString(cursor.getColumnIndex("log_data")));
                //构建Map
                list.add(logDataBean);
            }
        }catch (Exception e){
            if(Config.isDebug){
                Log.i(Config.SDK_NAME, TAG+" getAll Execption:"+ e.getLocalizedMessage());
            }
        }finally {
            if(cursor != null){
                cursor.close();
            }
            if(db != null){
                db.close();
            }

            return list;
        }

    }



//    public EventBean2 getById(String id){
//        SQLiteDatabase db = dbHelper.getReadableDatabase();
//        String sql = "select * from event_tab where id = ?";
//        Cursor cursor = null;
//        EventBean2 eventBean2 = new EventBean2();
//
//        try {
//            cursor = db.rawQuery(sql,new String[]{id});
//            while (cursor.moveToNext()){
//                eventBean2.setId(cursor.getInt(cursor.getColumnIndex("id")));
//                eventBean2.setEventId(cursor.getString(cursor.getColumnIndex("event_key")));
//                eventBean2.setEventName(cursor.getString(cursor.getColumnIndex("event_lable")));
//                String paramsStr = cursor.getString(cursor.getColumnIndex("params"));
//                //构建Map
//                HashMap<String, String> map = new HashMap<String, String>();
//                eventBean2.setParams(map);
//
//            }
//        }catch (Exception e){
//            Log.i(TAG,e.getLocalizedMessage());
//        }finally {
//            if(cursor != null){
//                cursor.close();
//            }
//            if(db != null){
//                db.close();
//            }
//
//        }
//        return eventBean2;
//    }




    public void close(){
        if(dbHelper != null){
            dbHelper.close();
        }
    }
}
