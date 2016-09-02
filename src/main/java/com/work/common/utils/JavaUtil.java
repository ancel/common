package com.work.common.utils;

public class JavaUtil {
	
	// 首字母转小写
	public static String toLowerCaseFirstOne(String s) {
		if (Character.isLowerCase(s.charAt(0))){
			return s;
		}
		return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
	}

	// 首字母转大写
	public static String toUpperCaseFirstOne(String s) {
		if (Character.isUpperCase(s.charAt(0))){
			return s;
		}
		return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
	}
	
	/**
	 * 将字符串转为驼峰命名，首字母小写，例如对象名
	 * @param str
	 * @param separator 字符串分隔符
	 * @return
	 */
	public static String toHumpForObject(String str,String separator){
		String[] strs = str.split(separator);
		StringBuilder sb = new StringBuilder();
		sb.append(strs[0].toLowerCase());
		for (int i = 1; i < strs.length; i++) {
			sb.append(toUpperCaseFirstOne(strs[i].toLowerCase()));
		}
		return sb.toString();
	}
	
	/**
	 * 将字符串转为驼峰命名，首字母大写，例如类名
	 * @param str
	 * @param separator 字符串分隔符
	 * @return
	 */
	public static String toHumpForClass(String str,String separator){
		String[] strs = str.split(separator);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < strs.length; i++) {
			sb.append(toUpperCaseFirstOne(strs[i].toLowerCase()));
		}
		return sb.toString();
	}


}
