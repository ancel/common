package com.work.common.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则工具类
 * @author admin
 *
 */
public class Regex {
	
	/**
	 * 读取所有匹配结果
	 * @param content
	 * @param regex
	 * @return
	 */
	public static List<String> regexRead(String content,String regex){
		Pattern p=Pattern.compile(regex);
		Matcher m=p.matcher(content);
		List<String> list = new ArrayList<String>();
		while(m.find()){
			list.add(m.group());
		}
		return list;
	}
	
	
	/**
	 * 读取所有匹配结果并生成字符串
	 * @param content
	 * @param regex
	 * @return
	 */
	public static String regexReadToStr(String content,String regex){
		List<String> list = regexRead(content,regex);
		StringBuffer sb = new StringBuffer();
		for (String string : list) {
			sb.append(string);
		}
		return sb.toString();
	}
	
	
	/**
	 * 读取第n个匹配结果
	 * @param content
	 * @param regex
	 * @return
	 */
	public static String regexReadOne(String content,String regex){
		Pattern p=Pattern.compile(regex);
		Matcher m=p.matcher(content);
		String result = null;
		while(m.find()){
			result = m.group(1);
		}
		return result;
	}
	/**
	 * 匹配一个字符串，并返回第几个匹配成功的子串
	 * @param content
	 * @param regex
	 * @param index
	 * @return
	 */
	public static String regexReadOne(String content,String regex,int index){
		Pattern p=Pattern.compile(regex);
		Matcher m=p.matcher(content);
		String result = "";
		while(m.find()){
			result = m.group(index);
		}
		return result;
	}
	
	/**
	 * 匹配一个字符串，并返回第几个匹配成功的子串
	 * @param content
	 * @param regex
	 * @param index
	 * @return
	 */
	public static List<String> regexReadList(String content,String regex,int index){
		Pattern p=Pattern.compile(regex);
		Matcher m=p.matcher(content);
		List<String> list = new ArrayList<String>();
		String result = "";
		while(m.find()){
			result = m.group(index);
			list.add(result);
		}
		return list;
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
