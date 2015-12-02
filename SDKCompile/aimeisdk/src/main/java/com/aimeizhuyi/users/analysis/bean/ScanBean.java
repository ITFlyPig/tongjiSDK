package com.aimeizhuyi.users.analysis.bean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class ScanBean {
	public static ArrayList<ScanBean> queen = new ArrayList<ScanBean>();//维持一个队列，用来临时保存信息
	
	public int id;
	public String inPage; //入口页面
	public String outPage; //出口页面
	public long visitTime; //访问的时间
	public long endTime;//出去的时间
	public long stayTime; //停留的时间
	public String pageId;//本页面的class
	
	//实现的思想：在下一个页面开启之后统计上一个页面的信息
	public void addAndCheck(ScanBean scanBean){
		Log.i("ee", "addAndCheck kaishi");
		if(scanBean == null)
			return;
		
		if(queen.size() > 0){
			ScanBean temp = queen.get(queen.size()-1);
			if(temp.pageId.equals(scanBean.pageId)){//虽然在onResume中，但是是同一个
				Log.i("ee", "addAndCheck 相同的对象");
				temp.endTime = scanBean.endTime;
				temp.stayTime = temp.stayTime + scanBean.stayTime;
				return;
			}
		}
			queen.add(scanBean);
			Log.i("ee", "正常添加");
			
		if(queen.size() >= 3){
			Log.i("ee", "删除第一个");
			queen.remove(0);
		}
		
		
		
	}
	
	public JSONObject getJsonObject(){
		JSONObject jsonObject = new JSONObject();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		try {
			jsonObject.put("inPage", inPage);
			jsonObject.put("outPage", outPage);
			jsonObject.put("visitTime", visitTime);
			jsonObject.put("endTime", endTime);
			jsonObject.put("stayTime", stayTime);
			jsonObject.put("pageId", pageId);
            Log.i("tt", "呆得时间长度"+sdf.format(new Date(Long.parseLong(stayTime+""))));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonObject;
	}

}
