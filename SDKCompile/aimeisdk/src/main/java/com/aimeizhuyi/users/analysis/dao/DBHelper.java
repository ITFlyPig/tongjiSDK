package com.aimeizhuyi.users.analysis.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by wangyuelin on 15/7/9.
 */
public class DBHelper extends SQLiteOpenHelper {


    public DBHelper(Context context) {
        super(context, DBConstant.DATABASE_NAME, null, DBConstant.VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DBConstant.CREATE_EVENT_TABLE);
        db.execSQL(DBConstant.CREATE_LOG_DATA_TABLE);
        db.execSQL(DBConstant.CREATE_EXCEPTION_DATA_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
