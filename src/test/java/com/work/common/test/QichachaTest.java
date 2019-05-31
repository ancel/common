package com.work.common.test;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicHeader;
import org.junit.Test;

import com.work.common.utils.http.HttpClientManager;
import com.work.common.utils.http.HttpClientUtil;
import com.work.common.utils.http.ResponseBall;

public class QichachaTest {
	@Test
	public void test(){
		List<BasicHeader> headerList = new ArrayList<BasicHeader>();
		Header[] headers = null;
		
		CloseableHttpClient httpclient = HttpClientManager.getHttpClient();
		HttpHost httpHost = null;
		String url;
		String reqBody;
		ResponseBall ball;
		
		
		headerList = new ArrayList<BasicHeader>();
		headerList.add(new BasicHeader("Accept", "application/json, text/plain, */*"));
		headerList.add(new BasicHeader("Accept-Encoding", "gzip, deflate"));
		headerList.add(new BasicHeader("Accept-Language", "zh-CN,en-US;q=0.7,en;q=0.3"));
		headerList.add(new BasicHeader("Connection", "keep-alive"));
		headerList.add(new BasicHeader("Content-Type", "application/json;charset=utf-8"));
		headerList.add(new BasicHeader("Host", "www.qixin.com"));
		headerList.add(new BasicHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36"));
		headerList.add(new BasicHeader("1b4872273d2048da5e29", "10e43165a22c2e8c6e42ef7257eb4f84dbc5e05e149452befc0e9f2211b58a339733a07a52caa96a3913f9b01062da4d376eed154f3f4cb3208dff83dc88c187"));
		headers = headerList.toArray(new BasicHeader[headerList.size()]);
		
		
		url = "http://www.qixin.com/api/user/login";
		reqBody = "{\"acc\":\"13810107594\",\"pass\":\"111111\",\"captcha\":{\"isTrusted\":true}}";
		ball = HttpClientUtil.reqByPost(httpclient, 30000, 30000, url, "UTF-8", JSONObject.fromObject(reqBody),  "UTF-8", httpHost, CookieSpecs.DEFAULT, headers);
		System.out.println(ball);
		
		url = "http://www.qixin.com/search?page=1";
		ball = HttpClientUtil.reqByGet(httpclient, 30000, 30000, url, "UTF-8", httpHost, CookieSpecs.DEFAULT, headers);
		System.out.println(ball);
		
		url = "http://www.qixin.com/company/17d18828-0a41-450d-9eca-987493a8b471";
		ball = HttpClientUtil.reqByGet(httpclient, 30000, 30000, url, "UTF-8", httpHost, CookieSpecs.DEFAULT, headers);
		System.out.println(ball);
		
		
		headerList = new ArrayList<BasicHeader>();
		headerList.add(new BasicHeader("Accept", "application/json, text/plain, */*"));
		headerList.add(new BasicHeader("Accept-Encoding", "gzip, deflate"));
		headerList.add(new BasicHeader("Accept-Language", "zh-CN,en-US;q=0.7,en;q=0.3"));
		headerList.add(new BasicHeader("Connection", "keep-alive"));
		headerList.add(new BasicHeader("Content-Type", "application/json;charset=utf-8"));
		headerList.add(new BasicHeader("Host", "www.qixin.com"));
		headerList.add(new BasicHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36"));
		headerList.add(new BasicHeader("b11d49860089daea3b5b", "1f52f1827e1a10a8b4b5fb939758358a014f0e005aa7b2b9e650e7ae68b7cd8e498a269e92c88a63c87a63848d495a9ae7f8c38db27bee2a94b79cf79b0d3412"));
		headers = headerList.toArray(new BasicHeader[headerList.size()]);
		
		url = "http://www.qixin.com/api/search";
		reqBody = "{\"key\":\"\",\"page\":499}";
		ball = HttpClientUtil.reqByPost(httpclient, 30000, 30000, url, "UTF-8", JSONObject.fromObject(reqBody),  "UTF-8", httpHost, CookieSpecs.DEFAULT, headers);
		System.out.println(ball);
	}
}
