package com.work.common.utils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class JsonUtil {
	
	/**
	 * 判断是否为数组，true表示是
	 * @param json
	 * @param key
	 * @return
	 */
	public static boolean isArray(JSONObject json,String key){
		try {
			json.getJSONArray(key);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * 判断是否为json对象
	 * @param object
	 * @return
	 */
	public static boolean isJsonObject(Object object){
		try {
			JSONObject.fromObject(object);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * 对象转json字符串
	 * @param obj
	 * @return
	 */
	public static String toJsonStr(Object obj){
		return JSONObject.fromObject(obj).toString();
	}
	
	
	public static void main(String[] args) {
		String s = "{a:\"sdf\",b:[{c:\"df\"}]}";
		JSONObject json = JSONObject.fromObject(s);
		System.out.println(isArray(json, "a"));
		JSONArray array =  json.getJSONArray("b");
		for (Object object : array) {
			System.out.println(isJsonObject(object));
		}
		
	}
}
