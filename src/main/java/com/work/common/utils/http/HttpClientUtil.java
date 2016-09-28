package com.work.common.utils.http;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.work.common.utils.Regex;

/**
 * http请求工具类
 * @author：wanghaibo 
 * @creattime：2016年9月28日 上午11:41:01 
 * 
 */  
public class HttpClientUtil {

	public static final Logger LOGGER = LoggerFactory
			.getLogger(HttpClientUtil.class);

	public static Header[] defaultHeaders;
	public static CloseableHttpClient httpclient;
	static{
		List<Header> headerList = new ArrayList<Header>();
		headerList.add(new BasicHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8"));
		headerList.add(new BasicHeader("Accept-Encoding", "gzip,deflate,sdch"));
		headerList.add(new BasicHeader("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6"));
//		headerList.add(new BasicHeader("Connection", "keep-alive"));
		headerList.add(new BasicHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/36.0.1985.125 Safari/537.36"));
		defaultHeaders = (Header[]) headerList.toArray();
		httpclient = HttpClientManager.getHttpClient();
	}
	
	
	public static String getHttpResponseByPost(String url, String charset,
			List<NameValuePair> params, String charset2, HttpHost httpHost,
			String cookieSpecs) {
		return getHttpResponseByPost(httpclient, url, charset, params, charset2, httpHost, cookieSpecs);
	}
	
	public static String getHttpResponseByPost(CloseableHttpClient httpclient,String url, String charset,
			List<NameValuePair> params, String charset2, HttpHost httpHost,
			String cookieSpecs) {
		RequestConfig config = RequestConfig.custom().setCookieSpec(cookieSpecs).setProxy(httpHost).build();
		HttpPost httpPost = new HttpPost(url);
		httpPost.setConfig(config);
		httpPost.setHeaders(defaultHeaders);
		CloseableHttpResponse response = null;
		String reponseStr = null;
		boolean flag = true;
		while (flag) {
			try {
				httpPost.setEntity(new UrlEncodedFormEntity(params, charset));
				
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
						LOGGER.error("request-------" + url, e2);
					}finally{
						flag = false;
					}
				}
			} catch (Exception e) {
				LOGGER.error("request-------" + url, e);
				//代理挂了继续跑
				if(e instanceof HttpHostConnectException){
					LOGGER.error("==============proxy down!!!========");
					flag = true;
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}else{
					LOGGER.error("request-------" + url, e);
					//非正常运行结束（除代理挂了的情况），关闭httpclient
					try {
						httpclient.close();
					} catch (IOException e2) {
						LOGGER.error("request-------" + url, e2);
					}finally{
						flag = false;
					}
				}
			}
		}
		
		return reponseStr;
	}

	public static String getHttpResponseByGet(String url, String charset,
			HttpHost httpHost, String cookieSpecs) {
		return getHttpResponseByGet(httpclient, url, charset, httpHost, cookieSpecs);

	}
	public static String getHttpResponseByGet(CloseableHttpClient httpclient,String url, String charset,
			HttpHost httpHost, String cookieSpecs) {
		HttpGet httpGet = new HttpGet(url);
		RequestConfig config = RequestConfig.custom().setCookieSpec(cookieSpecs).setCircularRedirectsAllowed(true).build();
		httpGet.setConfig(config);
		httpGet.setHeaders(defaultHeaders);
		CloseableHttpResponse response = null;
		HttpEntity entity;
		String responseStr = null;
		boolean flag = true;
		while(flag){
			try {
				// 开始请求
				response = httpclient.execute(httpGet);
				
				int status = response.getStatusLine().getStatusCode();
				if (status >= 200 && status < 300) {
					entity = response.getEntity();
					if(StringUtils.isBlank(charset)){
						charset = getCharSet(response);
					}
					if (StringUtils.isNotBlank(charset)) {
						responseStr = EntityUtils.toString(entity, charset);
					}else{
						responseStr = EntityUtils.toString(entity);
					}
				} else {
					LOGGER.error(url+"----"+status);
				}
				
				//正常运行结束，关闭httpclient
				try {
					httpGet.abort();
				} finally {
					response.close();
					try {
						httpclient.close();
					} catch (IOException e2) {
						LOGGER.error("request-------" + url, e2);
					}finally{
						flag = false;
					}
				}
			} catch (Exception e) {
				LOGGER.error("request-------" + url, e);
				//代理挂了继续跑
				if(e instanceof HttpHostConnectException){
					LOGGER.error("==============proxy down!!!========");
					flag = true;
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}else{
					LOGGER.error("request-------" + url, e);
					//非正常运行结束（除代理挂了的情况），关闭httpclient
					try {
						httpclient.close();
					} catch (IOException e2) {
						LOGGER.error("request-------" + url, e2);
					}finally{
						flag = false;
					}
				}
			}
		}
		return responseStr;
		
	}
	
	public static String getCharSet(HttpResponse response){
		HttpEntity entity = response.getEntity();
		String charset = null;
		if (entity.getContentEncoding() != null){
			String contentType= entity.getContentEncoding().getValue();
			if(StringUtils.isNotBlank(contentType)){
				charset = Regex.regexReadOne(contentType, "charset=([\\s\\S]*)", 1);
				if(StringUtils.isNotBlank(charset)){
					return charset;
				}
			}
		}
		String responseStr;
		try {
			responseStr = EntityUtils.toString(entity);
			if(StringUtils.isNotBlank(responseStr)){
				charset = Regex.regexReadOne(responseStr, "charset=[\"|']?([\\s\\S]*?)[\"|']", 1);
			}
		} catch (Exception e) {
			charset = null;
		} 
		return charset;
	}

	public static String getHttpResponseByPost(String url,
			List<NameValuePair> params, HttpHost httpHost, String charset,
			String charset2) {
		return getHttpResponseByPost(url, charset, params, charset2, httpHost,
				CookieSpecs.IGNORE_COOKIES);
	}

	public static String getHttpResponseByPost(String url,
			List<NameValuePair> params, String charset, String charset2) {
		return getHttpResponseByPost(url, charset, params, charset2, null,
				CookieSpecs.IGNORE_COOKIES);
	}

	public static String getHttpResponseByGet(String url, HttpHost httpHost,
			String charset) {
		return getHttpResponseByGet(url, charset, httpHost,
				CookieSpecs.IGNORE_COOKIES);
	}

	public static String getHttpResponseByGet(String url, String charset) {
		return getHttpResponseByGet(url, charset, null,
				CookieSpecs.IGNORE_COOKIES);
	}

	public static String getHttpResponseByGet(String url, String charset,
			String cookieSpecs) {
		return getHttpResponseByGet(url, charset, null, cookieSpecs);
	}

	
	/**
	 * 多次get请求
	 * 若请求次数小于1，则无限请求
	 * @param url
	 * @param httpHost
	 * @param charSet
	 * @param visitLimit
	 * @return
	 */
	public static String getResponse(String url,HttpHost httpHost,String charSet,int visitLimit){
		String response = null;
		//无限循环
		if(visitLimit<1){
			while(true){
				if(httpHost!=null){
					response = HttpClientUtil.getHttpResponseByGet(url, httpHost,charSet);
				}else{
					response = HttpClientUtil.getHttpResponseByGet(url,charSet);
				}
				
				if(StringUtils.isNotBlank(response)){
					break;
				}
			}
		}else{
			int visitNum = 0;
			while(visitNum<visitLimit){
				if(httpHost!=null){
					response = HttpClientUtil.getHttpResponseByGet(url, httpHost,charSet);
				}else{
					response = HttpClientUtil.getHttpResponseByGet(url,charSet);
				}
				
				if(StringUtils.isNotBlank(response)){
					break;
				}else{
					visitNum++;
				}
			}
		}
		return response;
	}
	
	/**
	 * 多次post请求
	 * 若请求次数小于1，则无限请求
	 * @param params
	 * @param url
	 * @param charset
	 * @param charset2
	 * @param visitLimit
	 * @return
	 */
	public static String getResponse(List<NameValuePair> params,String url,String charset,String charset2,int visitLimit){
		String response = null;
		if(visitLimit<1){
			while(true){
				response = HttpClientUtil.getHttpResponseByPost(url, params, charset, charset2);
				if(StringUtils.isNotBlank(response)){
					break;
				}
			}
		}else{
			int visitNum = 1;
			while(visitNum<visitLimit){
				response = HttpClientUtil.getHttpResponseByPost(url, params, charset, charset2);
				if(StringUtils.isNotBlank(response)){
					break;
				}else{
					visitNum++;
				}
			}
		}
		
		return response;
	}

}
