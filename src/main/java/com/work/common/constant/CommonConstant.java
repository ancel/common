package com.work.common.constant;

import java.io.UnsupportedEncodingException;


public class CommonConstant {
	//换行符
	public static final String LINE_SEPARATOR = System.getProperty("line.separator");
	public static byte[] LINE_SEPARATOR_BYTES;
	static{
		try {
			LINE_SEPARATOR_BYTES = LINE_SEPARATOR.getBytes(CharSet.UTF_8);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
