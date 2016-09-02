package com.work.common.utils;

import java.net.MalformedURLException;
import java.net.URL;

public class URLUtil {
	/**
	 * 相对路径转绝对路径
	 * @return
	 */
	public static String relToAbs(String absolutePath,String relativePath ){
		URL absoluteUrl;
		try {
			absoluteUrl = new URL(absolutePath);
			URL parseUrl = new URL(absoluteUrl ,relativePath );  
			return parseUrl.toString();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}  
	}
	
	public static void main(String[] args) {
		//绝对路径
		String absolutePath = "http://www.aaa.com/1/2/3.html";
		//相对路径
		String relativePath = "../../a.jpg";
		System.out.println(relToAbs(absolutePath, relativePath));
		
		relativePath = "javascript:void(0);";
		System.out.println(relToAbs(absolutePath, relativePath));
	}
}
