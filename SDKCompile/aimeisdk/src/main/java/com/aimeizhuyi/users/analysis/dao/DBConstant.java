package com.aimeizhuyi.users.analysis.dao;

/**
 * Created by wangyuelin on 15/7/9.
 */
public class DBConstant {
    public static int VERSION = 1;

    public  static final String DATABASE_NAME = "collect.db";

    public static final String EVENT_TABLE_NAME = "event_tab";

    public static final String LOG_DATA_TABLE_BANE = "log_tab";

    public static final String CREATE_EVENT_TABLE = "create table IF NOT EXISTS " + EVENT_TABLE_NAME + "(id INTEGER PRIMARY KEY AUTOINCREMENT , event_key varchar(10) not null, event_lable varchar(20), params text);";
    public static final String CREATE_LOG_DATA_TABLE = "create table IF NOT EXISTS " + LOG_DATA_TABLE_BANE + "(id INTEGER PRIMARY KEY AUTOINCREMENT ,  log_data text);";
}
