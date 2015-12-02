package com.aimeizhuyi.users.analysis.dao;

import android.bluetooth.BluetoothClass;
import android.content.Context;

import com.aimeizhuyi.users.analysis.common.DeviceInfo;

/**
 * Created by wangyuelin on 15/7/9.
 */
public class DeviceDao extends DeviceInfo{

    public DeviceDao(Context context, boolean isAll) {
		super(context, isAll);
		// TODO Auto-generated constructor stub
	}

	private int eventId; //对应于所属的事件的id


}
