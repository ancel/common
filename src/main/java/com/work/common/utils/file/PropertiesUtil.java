package com.work.common.utils.file;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 功能描述：处理Properties的常用方法 <br>
 * 
 */
public abstract class PropertiesUtil {

	/**
	 * 转换Properties为Map对象
	 *  
	 * @param prop
	 * @return Map<String,String>
	 * @date:2013-8-7
	 */
	public static Map<String, String> convertToMap(Properties prop) {
		if (prop == null) {
			return null;
		}

		Map<String, String> result = new HashMap<String, String>();
		for (Object eachKey : prop.keySet()) {
			if (eachKey == null) {
				continue;
			}

			String key = eachKey.toString();
			String value = (String) prop.get(key);
			if (value == null) {
				value = "";
			} else {
				value = value.trim();
			}

			result.put(key, value);
		}
		return result;
	}

}
