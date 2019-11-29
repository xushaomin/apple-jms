package com.appleframework.jms.kafka.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * gson转换json,只会转化有值得属性<br/> 
 */
public class GsonUtils {

	private volatile static Gson gson ;
	
	private GsonUtils(){
		
	}
	
	private static void init(){
		GsonBuilder gsonBuilder=new GsonBuilder();
		//可以自定义个性化功能
		//gsonBuilder.serializeNulls();
		gsonBuilder.setDateFormat("yyyy-MM-dd HH:mm:ss");
		gsonBuilder.disableInnerClassSerialization();
		//gsonBuilder.excludeFieldsWithoutExposeAnnotation();
		gsonBuilder.disableHtmlEscaping() ;
		gson = gsonBuilder.create() ;
	}
	
	public static Gson instance(){
		if(gson!=null){
			return gson ;
		}
		synchronized (GsonUtils.class) {
			if(gson==null){
				init();
			}
		}
		return gson ;
	}
	
	public static String toJson(Object obj){
		return GsonUtils.instance().toJson(obj);
	}
	
	public static Gson serializeNullJson(String dateFormat){
		return new GsonBuilder().serializeNulls().setDateFormat(dateFormat).disableInnerClassSerialization().disableHtmlEscaping().create();
	}
	
	public static Gson serializeNullJson(){
		return serializeNullJson("yyyy-MM-dd HH:mm:ss");
	}
}
