package com.work.common.utils;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.spreada.utils.chinese.ZHConverter;

public class CodingUtil {
	
	private static final Set<String> CORRENT_STRS = new HashSet<>();
	
	static{
		CORRENT_STRS.add("~");
	}
	
	/**
	 * unicode码转string
	 * @param str
	 * @return
	 */
	public static String unicode2Str(String str){
		Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
		Matcher matcher = pattern.matcher(str);
		char ch;
		while(matcher.find()){
			ch = (char)Integer.parseInt(matcher.group(2),16);
			str = str.replace(matcher.group(1), ch+"");
		}
		return str;
	}
	
	/** 
	* 把中文字符串转换为十六进制Unicode编码字符串   
	*/
	public static String str2Unicode(String s){
		String str = "";
		for (int i = 0; i < s.length(); i++) {
			int ch = (int)s.charAt(i);
			str += "\\u"+Integer.toHexString(ch);
//			str +=Integer.toHexString(ch);
		}
		return str;
		
	}
	
	
	/**
	 * 判断字符是否是中文
	 * @param c 字符
	 * @return 是否是中文
	 */
	public static boolean isChinese(char c) {
	    Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
	    if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
	            || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
	            || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
	            || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
	            || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
	            || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
	        return true;
	    }
	    return false;
	}
	
	/**
	 * 判断是否为数字
	 * @return
	 */
	public static boolean isNumber(char c){
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
	    if (ub == Character.UnicodeBlock.NUMBER_FORMS) {
	        return true;
	    }
	    return false;
	}
	
	/**
	 * 判断字符串是否是乱码
	 * @param strName 字符串
	 * @return 是否是乱码
	 */
	public static boolean isMessyCode(String target) {
		if(CORRENT_STRS.contains(target)){
			return false;
		}
		target = target.replaceAll("\\s+", "");
		/*
		 * \pP 其中的小写 p 是 property 的意思，表示 Unicode 属性，用于 Unicode 正表达式的前缀。  
		 * P 标点字符
		 * L：字母； 
			M：标记符号（一般不会单独出现）； 
			Z：分隔符（比如空格、换行等）； 
			S：符号（比如数学符号、货币符号等）； 
			N：数字（比如阿拉伯数字、罗马数字等）； 
			C：其他字符 
		 */
		target = target.replaceAll("\\p{P}", "");
		target = target.replaceAll("\\p{S}", "");
	    char[] ch = target.trim().toCharArray();
	    float chLength = ch.length;
	    float count = 0;
	    for (int i = 0; i < ch.length; i++) {
	        char c = ch[i];
	        if (!Character.isLetterOrDigit(c)) {
	        	if (!isNumber(c)) {
	        		if (!isChinese(c)) {
		                count = count + 1;
		            }
	            }
	        }
	    }
	    float result = count / chLength;
	    if (result > 0.4) {
	        return true;
	    } else {
	        return false;
	    }
	}
	

	/**
	 * 删除乱码
	 * @param str
	 * @return
	 */
	public static String deleteMessyCode(String str){
		int sLength = str.length();
		StringBuffer sb = new StringBuffer();
		String temp;
		for (int i = 0; i < sLength; i++) {
			temp = String.valueOf(str.charAt(i));
			if(!CodingUtil.isMessyCode(temp)){
				sb.append(temp);
			}
		}
		return sb.toString();
	}
	
	/**
	 * 繁体转简体
	 * @param str
	 * @return
	 */
	public static String traditionalToSimple(String str){
		ZHConverter converter = ZHConverter.getInstance(ZHConverter.SIMPLIFIED); 
		return converter.convert(str);
	}
	
	/**
	 * 简体转繁体
	 * @param str
	 * @return
	 */
	public static String simpleToTraditional(String str){
		ZHConverter converter = ZHConverter.getInstance(ZHConverter.TRADITIONAL); 
		return converter.convert(str);
	}
	
	public static void main(String[] args) {
//		try {
//		    System.out.println(unicode2Str("\u91CD\u5E86"));
//		    System.out.println(unicode2Str("\u5408\u80A5\u5E02"));
//		    System.out.println(unicode2Str("\u4F20\u7EDF\u7167\u76F8\u673A"));
//		    System.out.println(unicode2Str("CP\u6253\u5370\u673A"));
//		    System.out.println(unicode2Str("\u5408\u80A5\u5E02"));
//		    System.out.println(unicode2Str("\u77F3\u5634\u5C71"));
//		    System.out.println(unicode2Str("\u4E2D\u536B"));
//		    System.out.println(deleteMessyCode("39℃"));
//		    System.out.println(deleteMessyCode("大板の寿司"));
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} 
		System.out.println(traditionalToSimple("壽豐鄉"));
	}
}
