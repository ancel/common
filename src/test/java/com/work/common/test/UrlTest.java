package com.work.common.test;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.junit.Test;

public class UrlTest {
	@Test
	public void testUrlDecoder(){
		try {
			System.out.println(URLDecoder.decode("%D0%A1%D6%ED%B0%AE%CA%D7%CA%CE","gb2312"));
			System.out.println(URLEncoder.encode("小猪爱首饰", "utf-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
