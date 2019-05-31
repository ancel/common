package com.work.common.java.lang;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

public class StringTest {
	
	@Test
	public void testSplit(){
		String str;
		str = "-,";
		String[] strs = str.split(","); 
		System.out.println(strs.length);
		System.out.println(Arrays.toString(strs));
	}
	
	@Test
	public void testSubString(){
		String str = "ab";
		System.out.println(StringUtils.substring(str, 0, 3));
		System.out.println(str.substring(0,3));
	}
	
	@Test
	public void testReplace(){
		System.out.println("地区政府".replaceAll("(^地区)", ""));
		System.out.println("\\\"aaa".replaceAll("(\\\\+\")", "\""));
		System.out.println("文达路22号114".getBytes().length);
	}
	
	@Test
	public void testLen(){
		String str = "大连旺盛机电设备有限公司";
		System.out.println(str.length());
	}
}
