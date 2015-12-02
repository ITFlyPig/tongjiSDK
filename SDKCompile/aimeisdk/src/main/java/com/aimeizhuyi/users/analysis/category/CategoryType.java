package com.aimeizhuyi.users.analysis.category;

/**
 * Created by wangyuelin on 15/7/10.
 */
public  enum CategoryType {

        BY_NUM, //到达一定的条数开始上传
        BY_TIME,  //间隔固定的时间开始上传
        IMMEDIATELY // 获取到数据就开始上传

}
