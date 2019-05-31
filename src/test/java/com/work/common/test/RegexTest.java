package com.work.common.test;

import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

public class RegexTest {
	
	@Test
	public void testTel(){
		String tel = "05662088371";
		System.out.println(isTel(tel));
		
	}
	
	public boolean isTel(String tel){
		if(tel.startsWith("0")){
			if(isMatch("^0((10)|2[\\d])([2-8][\\d]{7}|96[\\d]{3,15}|1010[\\d]{4,8}|95[\\d]{3,15})$", tel)){
				return true;
			}else if(isMatch("^0[3-9][\\d]{2}([2-8][\\d]{6,7}|96[\\d]{3,15}|1010[\\d]{4,8}|95[\\d]{3,15})$", tel)){
				return true;
			}
		}else if(tel.startsWith("1")){
			if(isMatch("^1[\\d]{2,4}$", tel)){
				return true;
			}else if(isMatch("^1[3-8][\\d]{9}$", tel)){
				return true;
			}else if(isMatch("^106[\\d]{5,17}$", tel)){
				return true;
			}else if(isMatch("^125[\\d]{2,13}$", tel)){
				return true;
			}else if(isMatch("^1010[\\d]{4,8}$", tel)){
				return true;
			}else if(isMatch("^[10086|10001|10000|10010][\\d]{0,15}$", tel)){
				return true;
			}
		}else if(isMatch("^[4|8]00[\\d]{7}$", tel)){
			return true;
		}else if(isMatch("^95[\\d]{3,15}$", tel)){
			return true;
		}
		return false;
	}
	
	public static boolean isMatch(String regex, String target) {
		if (StringUtils.isNotBlank(regex) && StringUtils.isNotBlank(target)) {
			return Pattern.matches(regex, target);
		}
		return false;
	}
}
