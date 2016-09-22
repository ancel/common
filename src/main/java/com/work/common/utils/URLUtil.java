package com.work.common.utils;

import java.net.MalformedURLException;
import java.net.URL;

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
	
	public static void main(String[] args) {
		//绝对路径
		String absolutePath = "http://www.aaa.com/1/2/3.html";
		//相对路径
		String relativePath = "../../a.jpg";
		try {
			System.out.println(relToAbs(absolutePath, relativePath));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		relativePath = "javascript:void(0);";
		try {
			System.out.println(relToAbs(absolutePath, relativePath));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
