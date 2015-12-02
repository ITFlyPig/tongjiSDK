package com.aimeizhuyi.users.analysis.category;

import android.content.Context;

import com.aimeizhuyi.users.analysis.bean.LogDataBean;


/**
 * Created by wangyuelin on 15/7/9.
 * 规定了各种策略应该有得基本方法
 */
public interface CategoryInterface {

    void uploadDataCategory(LogDataBean logDataBean, Context context);//日志的上传策略，各种策略的核心
}
