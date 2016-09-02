package com.work.common.utils;

import java.util.Random;


public class NumberUtil {
	public static Random random = new Random();
	public static boolean isDouble(String str){
		try {
			Double.parseDouble(str);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}
	
	public static int getRandomValue(int size){
		return random.nextInt(size);
	}
}
