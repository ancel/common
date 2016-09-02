package com.work.common.demo;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import com.work.common.utils.file.PathUtil;

public class PathDemo {
	private static String LOCATION;

	static {
	    try {
	        LOCATION = URLDecoder.decode(PathDemo.class.getProtectionDomain()
	            .getCodeSource().getLocation().getFile(), "UTF-8");
	    } catch (UnsupportedEncodingException e) {
	        LOCATION = "";
	    }
	} 
	public static void main(String[] args) {
		System.out.println(LOCATION);
		System.out.println(PathUtil.getJarDir());
	}
	
}
