package com.work.common.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

/**
 * 正则工具类
 * @author：wanghaibo 
 * @creattime：2016年9月9日 下午4:45:32 
 * 
 */  
public class Regex {
	// 日期表达式
	public static final String REG_EXT = "^([1-9]\\d{3})((0[1-9])|(1[0-2]))((0[1-9])|([1-2][0-9])|(3[0-1]))(([0-1][0-9])|(2[0-3]))([0-5][0-9])([0-5][0-9])$";
	// 邮箱表达式
	public static final String REG_EMAIL = "^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)+$";
	// MD5值格式
	public static final String REG_MD = "^[a-zA-Z0-9]{32}$";
	// 中文真实姓名格式
	public static final String REG_REALNAME = "^[\u4E00-\u9FA5]{2,8}$";
	// 数字格式
	public static final String REG_NUMBER = "^\\d+$";
	// 正整数
	public static final String REG_NUMBER_POSITIVE = "^[1-9]\\d*$";
	// 时间戳格式
	public static final String REG_TIMESTAMP = "^\\d{13}$";
	// IP格式
	public static final String REG_IP = "^(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9])\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])$";
	// MAC地址格式
	public static final String REG_MAC = "^[0-9A-Fa-f]{2}-[0-9A-Fa-f]{2}-[0-9A-Fa-f]{2}-[0-9A-Fa-f]{2}-[0-9A-Fa-f]{2}-[0-9A-Fa-f]{2}$";
	// 字母符号空白
	public static final String REG_LETTER_SYMBOL = "[a-zA-Z\\pP\\p{Punct}\\s]+";
	// 字母或者数字的字符串
	public static final String REG_LETTER_OR_NUM = "^[A-Za-z0-9]+$";
	// 字母
	public static final String REG_LETTER = "^[A-Za-z]+$";
	
	/**
	 * 读取所有匹配结果
	 * @param target
	 * @param regex
	 * @return
	 */
	public static List<String> regexRead(String target,String regex){
		Pattern p=Pattern.compile(regex);
		Matcher m=p.matcher(target);
		List<String> list = new ArrayList<String>();
		while(m.find()){
			list.add(m.group());
		}
		return list;
	}
	
	
	/**
	 * 读取所有匹配结果并生成字符串
	 * @param target
	 * @param regex
	 * @return
	 */
	public static String regexReadToStr(String target,String regex){
		List<String> list = regexRead(target,regex);
		StringBuffer sb = new StringBuffer();
		for (String string : list) {
			sb.append(string);
		}
		return sb.toString();
	}
	
	
	/**
	 * 读取第n个匹配结果
	 * @param target
	 * @param regex
	 * @return
	 */
	public static String regexReadOne(String target,String regex){
		Pattern p=Pattern.compile(regex);
		Matcher m=p.matcher(target);
		String result = null;
		while(m.find()){
			result = m.group(1);
		}
		return result;
	}
	/**
	 * 匹配一个字符串，并返回第几个匹配成功的子串
	 * @param target
	 * @param regex
	 * @param index
	 * @return
	 */
	public static String regexReadOne(String target,String regex,int index){
		Pattern p=Pattern.compile(regex);
		Matcher m=p.matcher(target);
		String result = "";
		while(m.find()){
			result = m.group(index);
		}
		return result;
	}
	
	/**
	 * 匹配一个字符串，并返回第几个匹配成功的子串
	 * @param target
	 * @param regex
	 * @param index
	 * @return
	 */
	public static List<String> regexReadList(String target,String regex,int index){
		Pattern p=Pattern.compile(regex);
		Matcher m=p.matcher(target);
		List<String> list = new ArrayList<String>();
		String result = "";
		while(m.find()){
			result = m.group(index);
			list.add(result);
		}
		return list;
	}
	
	/**
	 * 验证字符串
	 * @param regex
	 * @param input
	 * @return
	 */
	public static boolean check(String target,String regex) {
		if (StringUtils.isNotEmpty(regex) && StringUtils.isNotEmpty(target)) {
			return Pattern.matches(regex, target);
		}
		return false;
	}

	public static void main(String[] args) {
		List<String> list = Regex.regexRead("\"tel_num\": \"028-68352628\"", "\"tel_num\":\"([\\s\\S]*?)\"");
		for (String string : list) {
			System.out.println(string);
		}
		
		System.out.println(regexReadOne("中国（sagd(sadf））", "（([\\s\\S]*?)\\(([\\s\\S]*?)））",2));
		
		String phone = "asgas125243nsag你好";
		System.out.println(Regex.regexReadToStr(phone, "\\d").toString());

	}
}
