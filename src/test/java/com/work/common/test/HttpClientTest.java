package com.work.common.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.http.HttpHost;
import org.apache.http.client.config.CookieSpecs;
import org.junit.Test;

import com.work.common.utils.http.HttpClientUtil;

public class HttpClientTest {
	@Test
	public void request(){
		String  url = "http://api.nuomi.com/api/dailydeal?version=v1&city=beijing";
//		System.out.println(HttpClientUtil.getHttpResponseByGet(url, "UTF-8", new HttpHost("proxy.dianhua.cn", 8080), CookieSpecs.IGNORE_COOKIES));
//		System.out.println(HttpClientUtil.getHttpResponseByGet(url, "utf-8", CookieSpecs.BROWSER_COMPATIBILITY));
//		try {
//			BufferedWriter bw = new BufferedWriter(new FileWriter("file/nuomi_baidu"));
//			bw.write(HttpClientUtil.getHttpResponseByGet(url, "utf-8", CookieSpecs.IGNORE_COOKIES));
//			bw.flush();
//			bw.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		try {
			BufferedReader br = new BufferedReader(new FileReader("file/nuomi_baidu"));
			String line;
			while((line=br.readLine())!=null){
				System.out.println(line);
			}
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
