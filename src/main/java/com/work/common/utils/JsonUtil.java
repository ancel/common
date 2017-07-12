package com.work.common.utils;

import org.apache.commons.lang3.StringEscapeUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class JsonUtil {
	
	/**
	 * jsonobject禁止html转义，不支持多层jsonarray
	 * @param json
	 * @return
	 */
	public static JSONObject unescapeHtml(JSONObject json){
		for(Object key:json.keySet()){
			Object value = json.get(key);
			if(value instanceof String){
				value = StringEscapeUtils.unescapeHtml4(value.toString());
			}else if(value instanceof JSONArray){
				JSONArray value2 = (JSONArray) value;
				if(value2.size()>0){
					if(value2.get(0) instanceof String){
						JSONArray value3 = new JSONArray();
						for (Object object : value2) {
							value3.add(StringEscapeUtils.unescapeHtml4(object.toString()));
						}
						value = value3;
					}else if(value2.get(0) instanceof JSONObject){
						JSONArray value3 = new JSONArray();
						for (Object object : value2) {
							value3.add(unescapeHtml((JSONObject)object));
						}
						value = value3;
					}
				}
			}else if(value instanceof JSONObject){
				value = unescapeHtml((JSONObject)value);
			}
			json.put(key, value);
		}
		return json;
	}
	
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
