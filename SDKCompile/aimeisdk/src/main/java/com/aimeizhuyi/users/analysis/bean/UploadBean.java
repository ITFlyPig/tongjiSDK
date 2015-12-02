package com.aimeizhuyi.users.analysis.bean;

import com.aimeizhuyi.users.analysis.common.DeviceInfo;

/**
 * Created by wangyuelin on 15/7/10.
 */
public class UploadBean {
    private EventBean ad;
    private DeviceInfo device;
    private CommonBean common;


    public DeviceInfo getDevice() {
        return device;
    }

    public void setDevice(DeviceInfo device) {
        this.device = device;
    }

    public CommonBean getCommon() {
        return common;
    }

    public void setCommon(CommonBean common) {
        this.common = common;
    }

    public EventBean getAd() {
        return ad;
    }

    public void setAd(EventBean ad) {
        this.ad = ad;
    }
}
