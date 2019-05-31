package com.work.common.test;

import java.net.MalformedURLException;
import java.util.Map;

import org.junit.Test;

import com.work.common.utils.URLUtil;

public class UrlTest {
	@Test
	public void test(){
		//绝对路径
		String absolutePath = "http://www.qianyan.biz/company/";
		//相对路径
		String relativePath = "qc-009-1.html";
		try {
			System.out.println(URLUtil.relToAbs(absolutePath, relativePath));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		relativePath = "javascript:void(0);";
		try {
			System.out.println(URLUtil.relToAbs(absolutePath, relativePath));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String url = "http://172.18.21.49:8983/solr/yuloreBKWD2_shard1_replica1/select?q=ranking%3A%5B19500+TO+*%5D&start=50000&rows=100&fl=tel&wt=csv&indent=true";
		Map<String, String> params = URLUtil.getUrlParamToMap(url.substring(url.indexOf("?")+1));
		System.out.println(params);
	}
}
