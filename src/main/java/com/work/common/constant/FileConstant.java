package com.work.common.constant;

import java.io.UnsupportedEncodingException;


/**
 * 文件相关常量
 * @author：wanghaibo 
 * @creattime：2016年9月18日 下午2:42:06 
 * 
 */  
public class FileConstant {
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
