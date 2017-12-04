package com.work.common.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Stack;
import java.util.UUID;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.CharUtils;



public abstract class StringUtil {
	
	/** / **/
	public static final String MARK_SLASH = "/";

	/** = **/
	public static final String MARK_EQUAL = "=";

	/** & **/
	public static final String MARK_AND = "&";

	/** , **/
	public static final String MARK_COMMA = ",";

	/** ? **/
	public static final String MARK_QUESTION = "?";

	/** UTF-8 **/
	public static final String UTF_8 = "UTF-8";

	public static final String URLCODE_UTF8 = "UTF-8";
	
	/**
	 * 返回一个对象的默认字符串，
	 * @param obj
	 * @return
	 */
	public static String defaultIfBlank(Object obj,String str){
		if(obj==null){
			return str;
		}
		if("null".equals(obj.toString())){
			return str;
		}
		return StringUtils.defaultString(obj.toString());
	}
	
	/**
	 * 判断一个对象是否为空
	 * @param obj
	 * @return
	 */
	public static boolean isBlank(Object obj){
		if(obj==null){
			return true;
		}
		String str = obj.toString().trim();
		if("null".equals(str)){
			return true;
		}
		return StringUtils.isBlank(str);
	}
	
	
	/**
	 * 判断一个对象是否不为空
	 * @param obj
	 * @return
	 */
	public static boolean isNotBlank(Object obj){
		return !StringUtil.isBlank(obj);
	}
	

	/**
	 * 判断参数序列是否有空
	 * 
	 * @param params
	 * @return boolean
	 */
	public static boolean isNull(String... params) {
		boolean flag = false;
		for (String str : params) {
			if (null == str || "".equals(str)) {
				flag = true;
				break;
			}
		}
		return flag;
	}

	/**
	 * @description 生成指定位数的随机数
	 * @param length
	 * @return String
	 */
	public static String getRandomDigit(int length) {
		StringBuffer result = new StringBuffer();
		int i = 0;
		Random random = new Random();
		while (i < length) {
			result.append(random.nextInt(9));
			i++;
		}
		return result.toString();
	}

	/**
	 * @description 生成指定位数的字符串(字母与数字)
	 * @param length
	 * @return String
	 */
	public static String getRandomString(int n) {
		String[] s = { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
				"t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" };
		int i = 0;
		StringBuffer result = new StringBuffer();
		Random random = new Random();
		while (i < n) {
			int j = random.nextInt(s.length);
			result.append(s[j]);
			i++;
		}
		return result.toString();
	}

	/**
	 * 转换为字符串
	 * 
	 * @param strs
	 * @return String
	 * @author:sunjiaxiao
	 * @date:2013-4-16
	 */
	public static String getStrsToStr(String... strs) {
		if (strs == null || strs.length == 0) {
			return null;
		}

		StringBuffer result = new StringBuffer(strs.length);
		for (String s : strs) {
			result.append(s);
		}
		return result.toString();
	}

	/**
	 * 验证map中所包含的params中指定的键值是否为空
	 * 
	 * @param map
	 * @param params
	 * @return boolean
	 * @author:User
	 * @date:2013-8-8
	 */
	public static boolean validateMap(Map<String, String> map, String... params) {
		for (String str : params) {
			String res = map.get(str);
			if (isNull(res)) {
				return false;
			}
		}

		return true;
	}

	/**
	 * 比较两个字符串（大小写敏感）。
	 * 
	 * <pre>
	 * StringUtil.equals(null, null)   = true
	 * StringUtil.equals(null, "abc")  = false
	 * StringUtil.equals("abc", null)  = false
	 * StringUtil.equals("abc", "abc") = true
	 * StringUtil.equals("abc", "ABC") = false
	 * </pre>
	 * 
	 * @param str1
	 *            要比较的字符串1
	 * @param str2
	 *            要比较的字符串2
	 * 
	 * @return 如果两个字符串相同，或者都是<code>null</code>，则返回<code>true</code>
	 */
	public static boolean equals(String str1, String str2) {
		if (str1 == null) {
			return str2 == null;
		}

		return str1.equals(str2);
	}

	/**
	 * 把map转换为数组
	 * 
	 * @param map
	 * @return String[]
	 * @date:2013-8-8
	 */
	public static String[] getArrayByMap(Map<String, String> map) {
		if (map == null || map.size() == 0) {
			return null;
		}
		String[] params = new String[map.size()];
		int i = 0;
		for (String key : map.keySet()) {
			params[i] = map.get(key);
			i++;
		}
		return params;
	}

	/**
	 * 把数组转为List
	 * 
	 * @param strs
	 * @return List<String>
	 * @date:2013-8-8
	 */
	public static List<String> toList(String... strs) {
		List<String> list = new ArrayList<String>();
		for (String str : strs) {
			list.add(str);
		}
		return list;
	}

	/**
	 * Map转换为String请求串
	 * 
	 * @param paramMap
	 * @return String
	 * @date:2013-8-6
	 */
	public static String getMapToString(Map<String, String> paramMap) {
		if (null == paramMap || paramMap.size() == 0) {
			return "";
		}

		StringBuffer buffer = new StringBuffer();
		for (Map.Entry<String, String> entry : paramMap.entrySet()) {
			buffer.append(entry.getKey()).append(MARK_EQUAL).append(entry.getValue()).append(MARK_AND);
		}
		return buffer.substring(0, buffer.length() - 1);
	}

	public static String getUUID() {
		return UUID.randomUUID().toString();
	}
	
	public static String getRandomAlphanumeric(int length) {
		return RandomStringUtils.randomAlphanumeric(length);
	}
	
	public static String getRandomNumeric(int length) {
		return RandomStringUtils.randomNumeric(length);
	}
	
	/**
	 * 
	 * 去除字符串前后的指定字符串
	 * @param source
	 * @param trimStr
	 * @return
	 */
	public static String trim(String source, String trimStr) {

		source = source.trim(); // 循环去掉字符串首的trimStr字符

		if(source.length()<1){
			return source;
		}
		String beginChar = source.substring(0, 1);
		while (source.length()>0&&beginChar.equalsIgnoreCase(trimStr)) {
			source = source.substring(1, source.length());
			beginChar = source.substring(0, 1);
		}
		
		if(source.length()<1){
			return source;
		}
		// 循环去掉字符串尾的trimStr字符
		String endChar = source.substring(source.length() - 1, source.length());
		while (source.length()>0&&endChar.equalsIgnoreCase(trimStr)) {
			source = source.substring(0, source.length() - 1);
			endChar = source.substring(source.length() - 1, source.length());
		}
		return source;
	}

	/**
	 * 去除source中的前后某些字符
	 * @param str
	 * @return
	 */
	public static String trim(String source, List<String> trimStrs) {
		source = source.trim(); // 循环去掉字符串首的trimStr字符

		if(source.length()<1){
			return source;
		}
		String beginChar = source.substring(0, 1);
		while (trimStrs.contains(beginChar)) {
			source = source.substring(1, source.length());
			if(source.length()>0){
				beginChar = source.substring(0, 1);
			}else{
				return source;
			}
		}

		if(source.length()<1){
			return source;
		}
		// 循环去掉字符串尾的trimStr字符
		String endChar = source.substring(source.length() - 1, source.length());
		while (trimStrs.contains(endChar)) {
			source = source.substring(0, source.length() - 1);
			if(source.length()>0){
				endChar = source.substring(source.length() - 1, source.length());
			}else{
				return source;
			}
		}
		return source;
	}
	
	/**
	 * 单词首字母大写
	 * 
	 * @param addr
	 * @return
	 */
	public static String upperForFirst(String str) {
		if(StringUtils.isBlank(str)){
			return str;
		}
		char[] chars = str.toCharArray();
		chars[0] = CharUtils.toString(chars[0]).toUpperCase().charAt(0);
		for (int i = 1; i < chars.length; i++) {
			// 前一个字符不是字母时转大写，否则转小写
			if (Regex.check(Regex.REG_LETTER, CharUtils.toString(chars[i - 1]))) {
				chars[i] = CharUtils.toString(chars[i]).toLowerCase().charAt(0);
			} else {
				chars[i] = CharUtils.toString(chars[i]).toUpperCase().charAt(0);
			}
		}
		return new String(chars);
	}

	/**
	 * 单词首字母小写
	 * 
	 * @param addr
	 * @return
	 */
	public static String lowerForFirst(String str) {
		char[] chars = str.toCharArray();
		chars[0] = CharUtils.toString(chars[0]).toUpperCase().charAt(0);
		for (int i = 1; i < chars.length; i++) {
			// 前一个字符不是字母时转大写，否则转小写
			if (Regex.check(Regex.REG_LETTER, CharUtils.toString(chars[i - 1]))) {
				chars[i] = CharUtils.toString(chars[i]).toUpperCase().charAt(0);
			} else {
				chars[i] = CharUtils.toString(chars[i]).toLowerCase().charAt(0);
			}
		}
		return new String(chars);
	}
	
	/**
	 * 判断str是否包含str2，并且str2的前后为trimStrs中的某个字符
	 * @param str
	 * @param str2
	 * @param trimStrs
	 * @return
	 */
	public static boolean containForStrs(String str,String str2,List<String> trimStrs){
		if(!str.contains(str2)){
			return false;
		}
		boolean flag = false;
		StringBuilder sb = new StringBuilder();
		
		//校验排头
		if(str.indexOf(str2)==0){//str2位于排头
			flag = true;
		}else{
			for (String string : trimStrs) {
				sb.delete(0, sb.length());
				sb = sb.append(string).append(str2);
				if(str.contains(sb.toString())){
					flag = true;
					break;
				}
			}
		}
		
		//校验排尾
		if(flag){
			flag = false;
			if(str.indexOf(str2)+str2.length()==str.length()){//str2位于排尾
				flag = true;
			}else{
				for (String string : trimStrs) {
					sb.delete(0, sb.length());
					sb = sb.append(str2).append(string);
					if(str.contains(sb.toString())){
						flag = true;
						break;
					}
				}
			}
			
		}
		return flag;
	}
	
	/**
	 * 删除第num层以下的嵌套括号,ps:括号不匹配时返回空字符串
	 * 
	 * @param str
	 * @param num
	 *            删除的嵌套层
	 * @return
	 */
	public static String deleteNestBracket(String str, int num) {
		if (StringUtils.isBlank(str)) {
			return "";
		}
		Stack<Character> sk = new Stack<Character>();
		int bracketNum = 0;
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == '(') {
				bracketNum++;
			}
			if (bracketNum < num) {
				sk.push(str.charAt(i));
			}
			if (str.charAt(i) == ')') {
				bracketNum--;
			}
			if (bracketNum < 0) {
				return "";
			}
		}
		if (bracketNum != 0) {
			return "";
		}
		return StringUtils.join(sk, "");
	}
	
	/**
	 * 按多个分隔符将字符串切分，ps:暂不支持多个字符切分
	 * @param str
	 * @param separater
	 * @return
	 */
	@Deprecated
	public static List<String> divStr(String str, String...separaters){
		List<String> strs = new ArrayList<>();
		if(StringUtils.isNotBlank(str)&&separaters.length>0){
			if(separaters.length==1){
				strs = Arrays.asList(str.split(separaters[0]));
			}
//			String separater = null;
//			int index;
//			int tempIndex;
//			while(str!=""){
//				index = str.length();
//				for (String tempSep : separaters) {
//					tempIndex = str.indexOf(tempSep);
//					if(tempIndex!=-1&&tempIndex<index){
//						index = tempIndex;
//						separater = tempSep;
//					}
//				}
//				if(index!=str.length()){
//					strs.add(str.substring(0, index));
//					str = str.substring(index+separater.length());
//				}else{
//					strs.add(str);
//					break;
//				}
//			}
		}
		return strs;
	}
	
	public static void main(String[] args) {
		String str;
		str = "asdf";
		System.out.println(str.indexOf("w"));
	}
}
