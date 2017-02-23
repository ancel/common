package com.work.common.test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.message.BasicHeader;
import org.junit.Test;

import com.work.common.utils.http.HttpClientUtil;
import com.work.common.utils.http.ResponseBall;

public class HttpClientTest {
	@Test
	public void request(){
//		String  url = "http://www.qiyeku.com/huangye/#";
		String  url = "http://www.qiyeku.com/huangye/jianguo_ganguo";
		List<BasicHeader> headerList = new ArrayList<BasicHeader>();
		headerList.add(new BasicHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"));
		headerList.add(new BasicHeader("Accept-Encoding", "gzip, deflate"));
		headerList.add(new BasicHeader("Accept-Language", "zh-CN,en-US;q=0.7,en;q=0.3"));
		headerList.add(new BasicHeader("Connection", "keep-alive"));
		headerList.add(new BasicHeader("Host", "www.qiyeku.com"));
		headerList.add(new BasicHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:47.0) Gecko/20100101 Firefox/47.0"));
		Header[] headers = headerList.toArray(new BasicHeader[headerList.size()]);
//		System.out.println(HttpClientUtil.getHttpResponseByGet(url, "utf-8", CookieSpecs.BROWSER_COMPATIBILITY));
		ResponseBall ball = HttpClientUtil.reqByGet(HttpClientUtil.defaultHttpClient, url,  "UTF-8", new HttpHost("proxy.dianhua.cn", 8080), CookieSpecs.BROWSER_COMPATIBILITY, null);
		System.out.println(ball.getContent());
		try {
			FileUtils.write(new File("file/qiyeku"), ball.getContent());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		System.out.println(HttpClientUtil.reqByGet(HttpClientUtil.defaultHttpClient, url, "UTF-8", null, CookieSpecs.BROWSER_COMPATIBILITY, HttpClientUtil.defaultHeaders));
	}
	
//	@Test
//	public void request2(){
//		String initUrlPath = "file/init-tel-kuaidi100";
//		BufferedReader br = null;
//		String keyStorePath = "file/kuaidi100.keystore";
//		String keyStorePassword = "111111";
//		SSLConnectionSocketFactory sslsf = null;
//		try {
//			sslsf = HttpsClientUtil.getSSLConnectionSocketFactory(keyStorePath, keyStorePassword);
//		} catch (Exception e1) {
//			e1.printStackTrace();
//		}
//		CloseableHttpClient httpclient = HttpClients.custom()
//				.setSSLSocketFactory(sslsf).build();
//		try {
//			br = new BufferedReader(new FileReader(initUrlPath));
//			String line;
//			ResponseBall ball;
//			HttpHost httpHost = new HttpHost("proxy.dianhua.cn", 8080);
//			List<BasicHeader> headerList = new ArrayList<BasicHeader>();
////			headerList.add(new BasicHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8"));
////			headerList.add(new BasicHeader("Accept-Encoding", "gzip,deflate,sdch"));
//			headerList.add(new BasicHeader("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6"));
//			headerList.add(new BasicHeader("Connection", "keep-alive"));
//			headerList.add(new BasicHeader("Host", "www.kuaidi100.com"));
//			headerList.add(new BasicHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36"));
//			BasicHeader[] Headers = headerList.toArray(new BasicHeader[headerList.size()]);
//			while((line=br.readLine())!=null){
//				ball = HttpClientUtil.reqByGet(httpclient, line, "UTF-8", httpHost, CookieSpecs.BROWSER_COMPATIBILITY, Headers);
//				if(ball.getStatusCode()>=200&&ball.getStatusCode()<300){
//					String phone = Regex.regexReadOne(ball.getContent(), "<dd class=\"kd-tel\"><strong>([\\s\\S]*?)</strong></dd>", 1);
//					System.out.println(phone);
//					if(phone.contains("-")){
//						System.out.println(line);
//						return;
//					}
//				}
//			}
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}finally{
//			if(br!=null){
//				try {
//					br.close();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		}
//	}
}
