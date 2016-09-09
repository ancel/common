package com.work.common.test;

import org.apache.http.client.config.CookieSpecs;
import org.junit.Test;

import com.work.common.utils.http.HttpClientUtil;

public class HttpClientTest {
	@Test
	public void request(){
		String  url = "https://www.so.com/s?q=18516998996&src=srp&fr=hao_360so";
//		System.out.println(HttpClientUtil.getHttpResponseByGet(url, "UTF-8", new HttpHost("proxy.dianhua.cn", 8080), CookieSpecs.BROWSER_COMPATIBILITY));
		System.out.println(HttpClientUtil.getHttpResponseByGet(url, "utf-8", CookieSpecs.BROWSER_COMPATIBILITY));
	}
}
