package com.aimeizhuyi.users.analysis.common;

import com.aimeizhuyi.users.analysis.category.CategoryType;

import java.security.PublicKey;

/**
 * Created by wangyuelin on 15/7/10.
 *
 * 全局的配置
 */
public class Config {

    public static CategoryType category ;

    public static boolean isDebug;//是否是测试模式

    public static  int TRY_TIME = 3;//尝试的次数

    public static String LOG_VERSION = "1.0";

    public static String  SDK_NAME = "aimeiSDk";

    public static String version = "2.0";//修改统计的Context（使用弱引用或者使用ApplicationContext），避免占用导致不能及时释放

}
