package com.work.common.test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicHeader;
import org.junit.Before;
import org.junit.Test;

import com.sun.corba.se.impl.orbutil.fsm.NameBase;
import com.work.common.utils.http.HttpClientManager;
import com.work.common.utils.http.HttpClientUtil;
import com.work.common.utils.http.ResponseBall;

public class HttpClientTest {
	List<String> userAgents;
	@Before
	public void init() throws IOException{
		userAgents = getUserAgent();
	}
	
	@Test
	public void request() throws NoSuchAlgorithmException, IOException{
//		System.setProperty("jsse.enableSNIExtension", "false");
		
//		HttpHost httpHost = new HttpHost("172.18.19.254", 8080);
//		HttpHost httpHost = new HttpHost("proxy.dianhua.cn", 8080);
		HttpHost httpHost = new HttpHost("proxy.dianhua.cn", 8081);
//		HttpHost httpHost = new HttpHost("192.168.20.177", 8080);
//		HttpHost httpHost = new HttpHost("proxy-tw.dianhua.cn", 8080);
//		HttpHost httpHost = new HttpHost("123.207.245.211", 3128);
//		HttpHost httpHost = new HttpHost("222.221.147.53", 15180);
//		MayiProxy mayi = new MayiProxy();
//		HttpHost httpHost = new HttpHost(mayi.getHostname(), mayi.getPort());
//		httpHost = null;
		Header[] headers = getHeaders();
//		headers = null;
		String  url;
		url = "http://0ds6402801.atobo.com.cn/WebSite/0Ds6402801-c13.html";
		CloseableHttpClient httpClient = HttpClientManager.getHttpClient();
		ResponseBall ball = HttpClientUtil.reqByGet(httpClient, url,  "gb2312", httpHost, CookieSpecs.DEFAULT, headers);
		System.out.println(ball);
		
		
		
	}
	
	public Header[] getHeaders() throws NoSuchAlgorithmException, IOException{
		List<BasicHeader> headerList = new ArrayList<BasicHeader>();
		headerList.add(new BasicHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8"));
		headerList.add(new BasicHeader("Accept-Encoding", "gzip, deflate, sdch, br"));
		headerList.add(new BasicHeader("Accept-Language", "zh-CN,zh;q=0.8"));
		headerList.add(new BasicHeader("cache-control", "max-age=0"));
		headerList.add(new BasicHeader("Connection","keep-alive"));
//		headerList.add(new BasicHeader("Host","www.dianping.com"));
//		headerList.add(new BasicHeader("User-Agent", userAgents.get(new Random().nextInt(userAgents.size()))));
		headerList.add(new BasicHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.75 Safari/537.36"));
//		headerList.add(new BasicHeader("User-Agent", "Dalvik/2.1.0 (Linux; U; Android 5.0.2; vivo Y51A Build/LRX22G)"));
//		MayiProxy mayi = new MayiProxy();
//		Map<String, String> headerMap = mayi.getHeaders();
//		for (Entry<String, String> entry: headerMap.entrySet()) {
//			headerList.add(new BasicHeader(entry.getKey(), entry.getValue()));
//		}
		Header[] headers = headerList.toArray(new BasicHeader[headerList.size()]);
		return headers;
	}
	
	public List<String> getUserAgent() throws IOException{
		String filename = "file/user-agent";
		BufferedReader br = new BufferedReader(new FileReader(filename));
		String line = null;
		List<String> userAgents = new ArrayList<String>();
		while((line=br.readLine())!=null){
			if(StringUtils.isNotBlank(line)){
				userAgents.add(line.trim());
			}
		}
		br.close();
		return userAgents;
	}
	
}
