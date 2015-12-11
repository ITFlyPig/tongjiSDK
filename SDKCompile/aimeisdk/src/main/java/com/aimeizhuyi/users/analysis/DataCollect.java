package com.aimeizhuyi.users.analysis;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.aimeizhuyi.users.analysis.bean.BaseBean;
import com.aimeizhuyi.users.analysis.bean.CommonBean;
import com.aimeizhuyi.users.analysis.bean.ErrorBean;
import com.aimeizhuyi.users.analysis.bean.EventBean;
import com.aimeizhuyi.users.analysis.bean.LogDataBean;
import com.aimeizhuyi.users.analysis.bean.PageVisitBean;
import com.aimeizhuyi.users.analysis.bean.ScanBean;
import com.aimeizhuyi.users.analysis.bean.UploadBean;
import com.aimeizhuyi.users.analysis.category.CategoryImplByNum;
import com.aimeizhuyi.users.analysis.category.CategoryImplByTime;
import com.aimeizhuyi.users.analysis.category.CategoryInterface;
import com.aimeizhuyi.users.analysis.common.Config;
import com.aimeizhuyi.users.analysis.common.Constant;
import com.aimeizhuyi.users.analysis.common.DeviceInfo;
import com.aimeizhuyi.users.analysis.common.ThreadPool;
import com.aimeizhuyi.users.analysis.utils.Session;

/**
 * Created by wangyuelin on 15/7/9.
 */
public class DataCollect {

	private static final String TAG = "DataCollect.class";

	public static Object lock = new Object();

	private static CategoryInterface categoryInterface, categoryByTime;

	private static PageVisitBean pageVisitBean = null;

	private static long starttime;
	
	private static ScanBean scanBean;

    private static HashMap<String, String> pageVisitParmas;

    private static String currentSession ;
    private static long pauseTime;

//    private static Context mContext;

    private static String pageId;

    private static WeakReference<Context> wk;

	static {
		categoryInterface = new CategoryImplByNum();
        categoryByTime = new CategoryImplByTime();
	}

	// 自定义的上传策略可以通过这个传入
	public static void setCategoryInterface(CategoryInterface categoryInterface) {
		DataCollect.categoryInterface = categoryInterface;
	}

	/**
	 * 事件统计
	 * @param context
	 * @param event_id
	 * @param event_lable
	 */
	public static void onEvent(Context context, String event_id,
			String event_lable,int event_type) {
		try {
			onEvent(context, event_id, event_lable, null,event_type);
		} catch (Exception e) {

		}
	}

	public static void onEvent(final Context context, final String event_id,
			final String event_lable, final Map<String, String> event_paralist , final int event_type) {
		ThreadPool.submit(new Runnable() {
			@SuppressLint("NewApi")
			@Override
			public void run() {
				synchronized (lock) {
                    if(wk == null){
                        wk = new WeakReference<Context>(context);
                    }
                    checkSessionIsNull(wk.get());
                    BaseBean.init(wk.get().getApplicationContext());
                    try {
                        event_paralist.put("uid", Config.uid);
                        EventBean eventBean = new EventBean();
                        eventBean.setEventName(event_lable);
                        eventBean.setEventId(event_id);
                        eventBean.setEventIfo(event_paralist);
//                        String sf = updateSF();
//                        if(!TextUtils.isEmpty(sf) && TextUtils.isEmpty(eventBean.getSf())){//默认获取的是页面的来源，如果有数据，那就是事件的来源
//                            eventBean.setSf(sf);
//                        }
                        CommonBean commonBean = new CommonBean();
                        commonBean.setEvt(event_type+"");
                        String myEvt = event_paralist.get("evt");
                        if(!TextUtils.isEmpty(myEvt)){
                            commonBean.setEvt(myEvt);
                        }
                        if(TextUtils.isEmpty(commonBean.getSource())){
                            commonBean.setSource("未获取到");
                        }


                        //部分数据需要app传递，自己获取不到
                        DeviceInfo deviceInfo = new DeviceInfo(wk.get().getApplicationContext(), true);
                        if(event_paralist != null){
                            String lon = event_paralist.get("lon");
                            String lat = event_paralist.get("lat");
                            String ua = event_paralist.get("ua");
                            String den = event_paralist.get("den");
                            deviceInfo.setLat(lat);
                            deviceInfo.setLon(lon);
                            deviceInfo.setUa(ua);
                            deviceInfo.setDen(den);

                        }

                        // 构造LogDataBean
                        final LogDataBean logDataBean = new LogDataBean();

                        JSONObject eventJsonObject = eventBean.getJsonObject();

                        JSONObject uploadJsonObject = new JSONObject();
                        uploadJsonObject.put("ad", eventJsonObject);
                        uploadJsonObject.put("common",
                                commonBean.getJsonObject());
                        uploadJsonObject.put("device", deviceInfo.getJsonObject());
                        String jsonStr = uploadJsonObject.toString();
                        if(Config.isDebug)
                        Log.d("tt", "onEvent上传的json：" + jsonStr);
                        logDataBean.setLogContent(jsonStr);

                        categoryInterface.uploadDataCategory(logDataBean,
                                wk.get().getApplicationContext());
                    } catch (Exception e) {

                        if(Config.isDebug){
                            Log.i(Config.SDK_NAME, TAG+" onEvent Execption:"+ e.getLocalizedMessage());
                        }
                    }
				}
			}
		});
	}

    public static void pageVisit(Context context , HashMap<String, String > params) {
        pageVisitParmas  =params;
    }

	/**
	 * 页面访问的统计
	 * @param context
	 * @param source
	 * @param target
	 */
	public static void collectPageVisit(Context context, String source, String target) {
        collectPageVisit(context, source, target, Constant.PAGE_VISIT_ID,
				Constant.PAGE_VISIT_name);
	}

	public static void collectPageVisit(final Context context, final String source,
			final String target, final String eventId, final String eventName) {

		if(scanBean != null){
			scanBean.inPage = source;
			scanBean.outPage = target;
		}


	}

    /*
    *
    * 表示要收集页面的访问行为
    * */
    public static void collectPageVisit() {
        if(scanBean != null){
            scanBean.inPage = "collect";
        }
    }

	/*
	 * 
	 * 在调试模式下，会打印出异常信息和数据信息
	 */
	public static void setDebugMode(boolean isDebug) {
		Config.isDebug = isDebug;
	}

	/*
	 * 
	 * 强制上传日志信息
	 */
	public static void forceUploadLog(Context context) {
		if (categoryInterface != null && context != null) {
			categoryInterface.uploadDataCategory(null, context.getApplicationContext());
		}
	}

	/*
	 * 
	 * 
	 * 设置当到达一定的数量之后日志就上传（当然得在特定的上传策略下才起作用）
	 */
	public static void setLogNumToUpload(int num) {

		if (num > 0) {
			CategoryImplByNum.NUM = num;
		}
	}

	/*
	 * 设置在上传失败之后的尝试次数
	 */
	public static void setTimeWhenFail(int time) {
		if (time > 0) {
			Config.TRY_TIME = time;
		}
	}

	public static void onResume(final Context context, final String pageId, final String pageName) {

        ThreadPool.submit(new Runnable() {
            @Override
            public void run() {
                synchronized (lock){


                    if(Config.isDebug)
                        Log.i("tt", "onResume");
                    if(context == null)
                        return;
                    checkUpdateSession(context.getClass().getName());//检查是否更新Session

                    wk = new WeakReference<Context>(context);
                    init(wk.get().getApplicationContext());
                    BaseBean.init(wk.get().getApplicationContext());

                     scanBean = new ScanBean();
                        scanBean.pageId  = pageId;
                        scanBean.visitTime = System.currentTimeMillis();

                   //如果pageid相同就不添加
                   if(ScanBean.queen.size() > 0){
                       ScanBean temp = ScanBean.queen.get(ScanBean.queen.size() -1);
                       if(TextUtils.isEmpty(temp.pageId) ){
                    scanBean.addAndCheck(scanBean);
                       }else{

                           if(!temp.pageId.equals(pageId)){
                               scanBean.addAndCheck(scanBean);
                           }
                       }

                   }else{
                       scanBean.addAndCheck(scanBean);
                   }

                    if(Config.isDebug)
                        Log.i("pa", "onResume添加的pageId："+pageId);


                }

            }
        });


	}

	public static void onPause(final Context context, final String pageId, final String pageName) {

//        ThreadPool.submit(new Runnable() {
//            @Override
//            public void run() {
//                synchronized (lock) {
//                    Log.i("tt", "onPause");
//                    if(Config.isDebug)
//                        Log.i("pa", "onPause中的pageId："+pageId);
//                    if(scanBean == null)
//                        return;
//                    scanBean.endTime = System.currentTimeMillis();
//                    if(context == null)
//                        return;
//                    try {
//                        //构造EventBean
//                                //提交
//                                String eventId = null;
//                                String eventName = null;
//                                String sf = null;//来源
//                                String logType = null;
//                                if(pageVisitParmas != null){
//                                    eventId  = pageVisitParmas.get("event_id");
//                                    eventName  = pageVisitParmas.get("event_name");
//                                    logType = pageVisitParmas.get("page_type");
//                                }
//                                if(TextUtils.isEmpty(eventName)){
//
//                                    eventName = pageName;
//                                }
//
//                                sf = updateSF(pageId);//记录页面的来源
//                                EventBean eventBean = new EventBean();
//                                eventBean.setEventName(eventName);
//                                eventBean.setEventId(pageId);
//                                if(TextUtils.isEmpty(sf)){
//                                    sf = "0";//赋值为未知
//                                }
//                                eventBean.setSf(sf);
//                                eventBean.setStayTime((scanBean.endTime - scanBean.visitTime)+"");
//                                CommonBean commonBean = new CommonBean();
//                                if(TextUtils.isEmpty(logType)){
//                                    commonBean.setEvt("20");//设置事件的类型
//                                }else{
//                                    commonBean.setEvt(logType);//设置事件的类型
//                                }
//
//                                //部分数据需要app传递，自己获取不到
//                                DeviceInfo deviceInfo = new DeviceInfo(wk.get(), true);
//                                if(pageVisitParmas != null){
//                                    String lon = pageVisitParmas.get("lon");
//                                    String lat = pageVisitParmas.get("lat");
//                                    String ua = pageVisitParmas.get("ua");
//                                    String den = pageVisitParmas.get("den");
//                                    deviceInfo.setLat(lat);
//                                    deviceInfo.setLon(lon);
//                                    deviceInfo.setUa(ua);
//                                    deviceInfo.setDen(den);
//
//                                }
//
//                                JSONObject eventJsonObject = eventBean.getJsonObject();
//                                JSONObject uploadJsonObject = new JSONObject();
//                                // 构造LogDataBean
//                                final LogDataBean logDataBean = new LogDataBean();
//                                uploadJsonObject.put("ad", eventJsonObject);
//                                uploadJsonObject.put("common",
//                                        commonBean.getJsonObject());
//                                uploadJsonObject.put("device", deviceInfo.getJsonObject());
//
//                                String jsonStr = uploadJsonObject.toString();
//                                if(Config.isDebug)
//                                    Log.i("pa", "页面浏览记录Json：" + jsonStr);
//                                logDataBean.setLogContent(jsonStr);
//
//                                categoryInterface.uploadDataCategory(logDataBean,
//                                        wk.get());
//                    } catch (Exception e) {
//
//                        if(Config.isDebug){
//                            Log.i(Config.SDK_NAME, TAG+" onPause Execption:"+ e.getLocalizedMessage());
//                        }
//                    }
//                }
//                pauseTime = System.currentTimeMillis();
//            }
//        });

        onPause(context, pageId, pageName, null);
	}


    public static void onPause(final Context context, final String pageId, final String pageName, final Map<String, String> params) {

        ThreadPool.submit(new Runnable() {
            @Override
            public void run() {
                synchronized (lock) {
                    if(Config.isDebug)
                    Log.i("tt", "onPause");
                    if(Config.isDebug)
                        Log.i("pa", "onPause中的pageId："+pageId);
                    if(scanBean == null)
                        return;
                    scanBean.endTime = System.currentTimeMillis();
                    if(context == null)
                        return;
                    try {
                        //构造EventBean
                        //提交
                        String eventId = null;
                        String eventName = null;
                        String sf = null;//来源
                        String logType = null;
                        if(pageVisitParmas != null){
                            eventId  = pageVisitParmas.get("event_id");
                            eventName  = pageVisitParmas.get("event_name");
                            logType = pageVisitParmas.get("page_type");
                        }
                        if(TextUtils.isEmpty(eventName)){

                            eventName = pageName;
                        }

                        sf = updateSF(pageId);//记录页面的来源
                        EventBean eventBean = new EventBean();
                        eventBean.setEventName(eventName);
                        eventBean.setEventId(pageId);
                        if(TextUtils.isEmpty(sf)){
                            sf = "0";//赋值为未知
                        }
                        eventBean.setSf(sf);
                        eventBean.setStayTime((scanBean.endTime - scanBean.visitTime) + "");
                        params.put("uid", Config.uid);
                        eventBean.setEventIfo(params);
                        CommonBean commonBean = new CommonBean();
                        if(TextUtils.isEmpty(logType)){
                            commonBean.setEvt("20");//设置事件的类型
                        }else{
                            commonBean.setEvt(logType);//设置事件的类型
                        }

                        //部分数据需要app传递，自己获取不到
                        DeviceInfo deviceInfo = new DeviceInfo(wk.get().getApplicationContext(), true);
                        if(pageVisitParmas != null){
                            String lon = pageVisitParmas.get("lon");
                            String lat = pageVisitParmas.get("lat");
                            String ua = pageVisitParmas.get("ua");
                            String den = pageVisitParmas.get("den");
                            deviceInfo.setLat(lat);
                            deviceInfo.setLon(lon);
                            deviceInfo.setUa(ua);
                            deviceInfo.setDen(den);

                        }

                        JSONObject eventJsonObject = eventBean.getJsonObject();
                        JSONObject uploadJsonObject = new JSONObject();
                        // 构造LogDataBean
                        final LogDataBean logDataBean = new LogDataBean();
                        uploadJsonObject.put("ad", eventJsonObject);
                        uploadJsonObject.put("common",
                                commonBean.getJsonObject());
                        uploadJsonObject.put("device", deviceInfo.getJsonObject());

                        String jsonStr = uploadJsonObject.toString();
                        if(Config.isDebug)
                            Log.i("pa", "页面浏览记录Json：" + jsonStr);
                        logDataBean.setLogContent(jsonStr);

                        categoryInterface.uploadDataCategory(logDataBean,
                                wk.get().getApplicationContext());
                    } catch (Exception e) {

                        if(Config.isDebug){
                            Log.i(Config.SDK_NAME, TAG+" onPause Execption:"+ e.getLocalizedMessage());
                        }
                    }
                }
                pauseTime = System.currentTimeMillis();
            }
        });

    }

	public static void test() {
		ThreadPool.submit(new Runnable() {
			@Override
			public void run() {
				JSONArray jsonArray = new JSONArray();
				for (int i = 0; i < 40; i++) {

					JSONObject jsonObject = new JSONObject();

					JSONObject jsonObject2 = new JSONObject();
					try {
						jsonObject.put("eventId", "111" + i);
						jsonObject.put("eventName", "name" + i);
						jsonObject2.put("ad", jsonObject);
						jsonArray.put(jsonObject2);
					} catch (JSONException e) {
						e.printStackTrace();
					}

				}
                if(Config.isDebug)
				Log.d("aa", "上传的数据：" + jsonArray.toString());
				NetUpload.commitData(jsonArray.toString());
			}
		});

	}


    private  static void init(Context context){
        if(context != null && TextUtils.isEmpty(Session.session)){
            if(Config.isDebug)
            Log.i("ss", "初始化：");

        Session.session = Session.generateSeesion(context);
        }
    }

    /*
    *
    * 按一定的策略检测是否更新sessionid
    * */
    private static void checkUpdateSession(String className){
        Context mContext = null;
        if(wk != null){
         mContext = wk.get();
        }
        if(TextUtils.isEmpty(className) || mContext == null)
            return;
        if(className.equals(mContext.getClass().getName())){//表示是同一个页面
            if((System.currentTimeMillis() - pauseTime) > Session.time){//间隔大于一分钟

                if(Config.isDebug)
                Log.i("ss", "更新：");
                Session.session = Session.generateSeesion(wk.get());
            }
        }
    }

    /*
    *
    * 更新当前页面的来源页面
    * */
    private static String  updateSF(String pageId){
        String sf ="";
        if(ScanBean.queen.size() == 0 )
            return sf;
        ScanBean temp = ScanBean.queen.get(ScanBean.queen.size() -1);
        if(!temp.pageId.equals(pageId)){
            sf = temp.pageId;
        }else if(ScanBean.queen.size() >= 2){
             temp = ScanBean.queen.get(ScanBean.queen.size() -2);
            sf = temp.pageId;
        }
        return sf;
    }

    private static void checkSessionIsNull(Context context){
        if(TextUtils.isEmpty(Session.session)){
            Session.session = Session.generateSeesion(context);
        }
    }

    /*
    * 统计异常信息
    * */
    public static void onExecption(final Context context , final String clientcode, final Exception e) {
        ThreadPool.submit(new Runnable() {
            @SuppressLint("NewApi")
            @Override
            public void run() {
                synchronized (lock) {
                    if(wk == null){
                        wk = new WeakReference<Context>(context);
                    }
                    checkSessionIsNull(wk.get());
                    BaseBean.init(wk.get().getApplicationContext());
                    try {

                        CommonBean commonBean = new CommonBean();
                        //部分数据需要app传递，自己获取不到
                        DeviceInfo deviceInfo = new DeviceInfo(wk.get().getApplicationContext(), true);
                        // 构造LogDataBean
                        final LogDataBean logDataBean = new LogDataBean();

                        //构造异常的bean
                        ErrorBean errorBean = new ErrorBean();
                        errorBean.setClientcode(clientcode);
                        String err = parseExecption(e);
                        errorBean.setClienterrs(err);
                        errorBean.setUid(Config.uid);

                        JSONObject uploadJsonObject = new JSONObject();
                        uploadJsonObject.put("common",
                                commonBean.getJsonObject());
                        uploadJsonObject.put("device", deviceInfo.getJsonObject());
                        uploadJsonObject.put("error", errorBean.getJsonObject());
                        String jsonStr = uploadJsonObject.toString();
                        if(Config.isDebug)
                            Log.d("tt", "onEvent上传的json：" + jsonStr);
                        logDataBean.setLogContent(jsonStr);

                        categoryByTime.uploadDataCategory(logDataBean,
                                wk.get().getApplicationContext());
                    } catch (Exception e) {

                        if(Config.isDebug){
                            Log.i(Config.SDK_NAME, TAG+" onEvent Execption:"+ e.getLocalizedMessage());
                        }
                    }
                }
            }
        });
    }

    /*
   * 统计异常信息
   * 注意：不建议使用这个函数，因为使用这个函数，查找异常所需要的 方法   行数  异常的类型等这些数据都没有
   * */
    public static void onExecption(final Context context , final String clientcode , final String err) {
        ThreadPool.submit(new Runnable() {
            @SuppressLint("NewApi")
            @Override
            public void run() {
                synchronized (lock) {
                    if(wk == null){
                        wk = new WeakReference<Context>(context);
                    }
                    checkSessionIsNull(wk.get());
                    BaseBean.init(wk.get().getApplicationContext());
                    try {

                        CommonBean commonBean = new CommonBean();
                        //部分数据需要app传递，自己获取不到
                        DeviceInfo deviceInfo = new DeviceInfo(wk.get().getApplicationContext(), true);
                        // 构造LogDataBean
                        final LogDataBean logDataBean = new LogDataBean();

                        //构造异常的bean
                        ErrorBean errorBean = new ErrorBean();
                        errorBean.setClientcode(clientcode);
                        StringBuffer buffer = new StringBuffer();
                        buffer.append("Execption:").append(err);
                        errorBean.setClienterrs(buffer.toString());

                        JSONObject uploadJsonObject = new JSONObject();
                        uploadJsonObject.put("common",
                                commonBean.getJsonObject());
                        uploadJsonObject.put("device", deviceInfo.getJsonObject());
                        uploadJsonObject.put("error", errorBean.getJsonObject());
                        String jsonStr = uploadJsonObject.toString();
                        if(Config.isDebug)
                            Log.d("tt", "onEvent上传的json：" + jsonStr);
                        logDataBean.setLogContent(jsonStr);

                        categoryByTime.uploadDataCategory(logDataBean,
                                wk.get().getApplicationContext());
                    } catch (Exception e) {

                        if(Config.isDebug){
                            Log.i(Config.SDK_NAME, TAG+" onEvent Execption:"+ e.getLocalizedMessage());
                        }
                    }
                }
            }
        });
    }

    /*
    * 解析异常对象
    * */
    public static String parseExecption( Throwable e){
        String errsStr = null;
        if(e == null ){
            return errsStr;
        }

        int stackSize = e.getStackTrace().length;
        if(stackSize > 0){
            StringBuffer buffer = new StringBuffer();
            StackTraceElement element = e.getStackTrace()[0];
            buffer.append("Execption:").append(e.getClass().getName()).append(":").append(e.getLocalizedMessage())
                    .append("  /n ").append("at ").append(element.getMethodName()).append("(").append(element.getFileName()).append(":").append(element.getLineNumber()).append(")");
            errsStr = buffer.toString();
        }
        return errsStr;
    }

    /*
    *
    *
    * 设置一个全局的uid*/
    public static void setUid(String uid){
        Config.uid = uid;
    }
}
