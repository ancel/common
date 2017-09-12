package com.work.common.utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class URLUtil {
	/**
	 * 相对路径转绝对路径
	 * @return
	 * @throws MalformedURLException 
	 */
	public static String relToAbs(String absolutePath,String relativePath ) throws MalformedURLException{
		URL absoluteUrl = new URL(absolutePath);
		URL parseUrl = new URL(absoluteUrl ,relativePath );  
		return parseUrl.toString();

	}
	
	/**
	 * 
	 * 将URL返回的字符串解析成Map, 解析的字符串格式：a=a&b=b&c=c
	 * 
	 * @param result
	 * @return Map<String,String>
	 */
	public static Map<String, String> getUrlParamToMap(String urlParam) {
		if (StringUtil.isNull(urlParam)) {
			return null;
		}

		Map<String, String> map = new HashMap<String, String>();
		String[] params = urlParam.split(StringUtil.MARK_AND);
		for (String param : params) {
			String[] values = param.split(StringUtil.MARK_EQUAL, 2);
			String value = values.length == 2 ? values[1] : "";
			map.put(values[0], value);
		}
		return map;
	}
	
}
