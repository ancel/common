package com.work.common.demo;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.work.common.constant.CharSet;
import com.work.common.utils.http.HttpClientManager;

public class HttpClientDemo {
	
	public static final Logger LOGGER = LoggerFactory.getLogger(HttpClientDemo.class);
	public static void main(String[] args) {
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><Test><id>12</id><name>sdf</name></Test>";
		HttpClientDemo.postXml(xml);
	}

	/**
	 * 向服务器postXML文件
	 */
	public static void postXml(String xml) {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		String url = "http://localhost:8080/cmcc/test";
		try {
			HttpPost httpPost = new HttpPost(url);
			// ***十分重要，要执行提交数据的类型****
			httpPost.setHeader("Content-Type", "application/xml");
			httpPost.setHeader("Accept",
					"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
			httpPost.setHeader("Accept-Charset", "gzip,deflate,sdch");
			httpPost.setHeader("Accept-Encoding", "gzip, deflate");
			httpPost.setHeader("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6");
			httpPost.setHeader("Connection", "keep-alive");
			httpPost.setHeader(
					"User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/36.0.1985.125 Safari/537.36");

			StringEntity myEntity = null;
			try {
				myEntity = new StringEntity(xml, CharSet.UTF_8);
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			httpPost.setEntity(myEntity);
			// httpPost.setEntity(reqEntity);

			System.out.println("executing request " + url);
			CloseableHttpResponse response = null;
			try {
				response = httpclient.execute(httpPost);
				System.out.println("----------------------------------------");
				System.out.println(response.getStatusLine());
				HttpEntity resEntity = response.getEntity();
				if (resEntity != null) {
					System.out.println("Response content length: "
							+ resEntity.getContentLength());
				}
				System.out.println(resEntity.getContentType());
				System.out.println(EntityUtils.toString(resEntity));
				EntityUtils.consume(resEntity);

			} catch (Exception e) {
				// TODO: handle exception
			} finally {
				try {
					response.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} finally {
			try {
				httpclient.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	/**
	 * 处理payload之类的请求
	 */
	public static String getHttpResponseByPost(String url, String charset,
			String payLoadStr, String charset2, HttpHost httpHost,
			String cookieSpecs) {
		// CloseableHttpClient httpclient = HttpClients.createDefault();
		CloseableHttpClient httpclient = HttpClientManager
				.getHttpClient(httpHost);
		// HttpHost httpHost=new HttpHost("proxy.dianhua.cn",8080);
		RequestConfig config = RequestConfig.custom().setConnectTimeout(30000)
				.setSocketTimeout(30000).setCookieSpec(cookieSpecs).build();
		HttpPost httpPost = new HttpPost(url);
		httpPost.setConfig(config);
		httpPost.setHeader("Accept",
				"application/json, text/javascript, */*; q=0.01");
		httpPost.setHeader("Accept-Encoding", "gzip,deflate,sdch");
		httpPost.setHeader("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6");
		httpPost.setHeader("Content-Type", "application/json; charset=UTF-8");
		httpPost.setHeader("Connection", "keep-alive");
		httpPost.setHeader(
				"User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/36.0.1985.125 Safari/537.36");
		CloseableHttpResponse response = null;
		String reponseStr = null;
		boolean flag = true;
		StringEntity se = null;
		try {
			se = new StringEntity(payLoadStr, charset);
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}      
		while (flag) {
			try {
				httpPost.setEntity(se);

				// 开始请求
				response = httpclient.execute(httpPost);

				int status = response.getStatusLine().getStatusCode();
				if (status >= 200 && status < 300) {
					if (StringUtils.isBlank(charset2)) {
						reponseStr = EntityUtils.toString(response.getEntity());
					} else {
						reponseStr = EntityUtils.toString(response.getEntity(),
								charset2);
					}
				} else {
					LOGGER.error(url + "----" + status);
				}

				try {
					httpPost.abort();
				} finally {
					response.close();
					try {
						httpclient.close();
					} catch (IOException e2) {
						// TODO Auto-generated catch block
						LOGGER.error("request-------" + url, e2);
					}finally{
						flag = false;
					}
				}
			} catch (Exception e) {
				LOGGER.error("request-------" + url, e);
				if(e instanceof HttpHostConnectException){
					flag = true;
				}else{
					try {
						httpclient.close();
					} catch (IOException e2) {
						// TODO Auto-generated catch block
						LOGGER.error("request-------" + url, e2);
					}finally{
						flag = false;
					}
				}
			}
		}
		
		return reponseStr;
	}
}
