package com.work.common.utils;

public class BooleanUtil {
	/**
	 * 为true则可修改，否则不可修改 
	 * @return
	 */
	public static boolean getBool(boolean flag,boolean flag2){
		if(flag){
			return flag2;
		}else{
			return flag;
		}
	}
}
