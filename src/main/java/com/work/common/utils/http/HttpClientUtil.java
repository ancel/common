package com.work.common.utils.http;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileExistsException;
import org.apache.commons.io.IOUtils;
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
	public static CloseableHttpClient defaultHttpClient;
	static{
		List<BasicHeader> headerList = new ArrayList<BasicHeader>();
		headerList.add(new BasicHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8"));
		headerList.add(new BasicHeader("Accept-Encoding", "gzip,deflate,sdch"));
		headerList.add(new BasicHeader("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6"));
		headerList.add(new BasicHeader("Connection", "keep-alive"));
		headerList.add(new BasicHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/36.0.1985.125 Safari/537.36"));
		defaultHeaders = headerList.toArray(new BasicHeader[headerList.size()]);
		defaultHttpClient = HttpClientManager.getHttpClient();
	}
	
	public static ResponseBall reqByPost(CloseableHttpClient httpclient, String url,
			String charset, List<NameValuePair> params, String charset2,
			HttpHost httpHost, String cookieSpecs, Header[] headers) {
		RequestConfig config = RequestConfig.custom()
				.setCookieSpec(cookieSpecs).setCircularRedirectsAllowed(true)
				.setProxy(httpHost).build();
		HttpPost httpPost = new HttpPost(url);
		httpPost.setConfig(config);
		if (null != headers && headers.length > 0) {
			httpPost.setHeaders(headers);
		}
		CloseableHttpResponse response = null;
		HttpEntity entity;
		ResponseBall ball = new ResponseBall();
		ball.setUrl(url);

		String responseStr = null;
		boolean flag = true;
		while (flag) {
			try {
				httpPost.setEntity(new UrlEncodedFormEntity(params, charset));

				// 开始请求
				response = httpclient.execute(httpPost);

				int statusCode = response.getStatusLine().getStatusCode();
				ball.setStatusCode(statusCode);
				entity = response.getEntity();
				if (StringUtils.isNotBlank(charset2)) {
					responseStr = EntityUtils.toString(entity, charset2);
				} else {
					responseStr = EntityUtils.toString(entity);
				}
				ball.setContent(responseStr);
				ball.setHeaders(response.getAllHeaders());
				
				if (statusCode < 200 && statusCode >= 400) {
					LOGGER.error(url + "----" + statusCode);
				} else if (statusCode >= 300 && statusCode < 400) {
					Header locationHeader = response.getFirstHeader("Location");
					if (locationHeader != null) {
						ball = reqByGet(httpclient, locationHeader.getValue(),
								"UTF-8", null,
								CookieSpecs.BROWSER_COMPATIBILITY, headers);
					}
				}
				
				try {
					httpPost.abort();
				} finally {
					response.close();
					flag = false;
				}
			} catch (Exception e) {
				LOGGER.error("request-------" + url, e);
				// 代理挂了继续跑
				if (e instanceof HttpHostConnectException) {
					LOGGER.error("==============proxy down!!!========");
					flag = true;
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				} else {
					// 非正常运行结束（除代理挂了的情况），关闭httpclient
					LOGGER.error("request-------" + url, e);
					flag = false;
				}
			}
		}

		return ball;
	}

	public static ResponseBall reqByGet(CloseableHttpClient httpclient, String url,
			String charset, HttpHost httpHost, String cookieSpecs,
			Header[] headers) {
		HttpGet httpGet = new HttpGet(url);
		RequestConfig config = RequestConfig.custom()
				.setCookieSpec(cookieSpecs).setCircularRedirectsAllowed(true)
				.setProxy(httpHost).build();
		httpGet.setConfig(config);
		if (null != headers && headers.length > 0) {
			httpGet.setHeaders(headers);
		}
		CloseableHttpResponse response = null;
		HttpEntity entity;
		ResponseBall ball = new ResponseBall();
		ball.setUrl(url);

		String responseStr = null;
		boolean flag = true;
		while (flag) {
			try {
				// 开始请求
				response = httpclient.execute(httpGet);

				int statusCode = response.getStatusLine().getStatusCode();
				ball.setStatusCode(statusCode);

				entity = response.getEntity();
				if (StringUtils.isNotBlank(charset)) {
					responseStr = EntityUtils.toString(entity, charset);
				} else {
					responseStr = EntityUtils.toString(entity);
				}

				ball.setContent(responseStr);
				ball.setHeaders(response.getAllHeaders());

				if (statusCode < 200 && statusCode >= 400) {
					LOGGER.error(url + "----" + statusCode);
				}

				// 正常运行结束，关闭httpclient
				try {
					httpGet.abort();
				} finally {
					response.close();
					flag = false;
				}
			} catch (Exception e) {
				LOGGER.error("request-------" + url, e);
				// 代理挂了继续跑
				if (e instanceof HttpHostConnectException) {
					LOGGER.error("==============proxy down!!!========");
					flag = true;
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				} else {
					// 非正常运行结束（除代理挂了的情况），关闭httpclient
					e.printStackTrace();
					LOGGER.error("request-------" + url, e);
					flag = false;
				}
			}
		}
		return ball;

	}
	
	/**
	 * 通过get方式下载文件
	 * @param httpclient
	 * @param url
	 * @param charset
	 * @param httpHost
	 * @param cookieSpecs
	 * @param headers
	 * @param destFileName
	 * @return
	 * @throws FileExistsException 
	 */
	public static ResponseBall getFileByGet(CloseableHttpClient httpclient, String url,
			String charset, HttpHost httpHost, String cookieSpecs,
			Header[] headers,String destFileName) throws FileExistsException {
		File destFile = new File(destFileName);
		if(destFile.exists()){
			throw new FileExistsException(destFile);
		}
		HttpGet httpGet = new HttpGet(url);
		RequestConfig config = RequestConfig.custom()
				.setCookieSpec(cookieSpecs).setCircularRedirectsAllowed(true)
				.setProxy(httpHost).build();
		httpGet.setConfig(config);
		if (null != headers && headers.length > 0) {
			httpGet.setHeaders(headers);
		}
		CloseableHttpResponse response = null;
		HttpEntity entity;
		ResponseBall ball = new ResponseBall();
		ball.setUrl(url);
		
		String responseStr = null;
		boolean flag = true;
		while (flag) {
			try {
				// 开始请求
				response = httpclient.execute(httpGet);
				
				int statusCode = response.getStatusLine().getStatusCode();
				ball.setStatusCode(statusCode);
				
			   
			    if(statusCode==200){
			    	entity = response.getEntity();
			    	InputStream in = entity.getContent();
			    	FileOutputStream fout =new FileOutputStream(destFile);
			    	IOUtils.copy(in, fout);
					fout.flush();
					fout.close();
					in.close();
			    }
				
				ball.setContent(responseStr);
				ball.setHeaders(response.getAllHeaders());
				
				if (statusCode < 200 && statusCode >= 400) {
					LOGGER.error(url + "----" + statusCode);
				}
				
				// 正常运行结束，关闭httpclient
				try {
					httpGet.abort();
				} finally {
					response.close();
					flag = false;
				}
			} catch (Exception e) {
				LOGGER.error("request-------" + url, e);
				// 代理挂了继续跑
				if (e instanceof HttpHostConnectException) {
					LOGGER.error("==============proxy down!!!========");
					flag = true;
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				} else {
					// 非正常运行结束（除代理挂了的情况），关闭httpclient
					LOGGER.error("request-------" + url, e);
					flag = false;
				}
			}
		}
		return ball;
		
	}
	
	
	public static String getHttpResponseByPost(CloseableHttpClient httpclient,String url, String charset,
			List<NameValuePair> params, String charset2, HttpHost httpHost,
			String cookieSpecs,Header[] headers) {
		return reqByPost(httpclient, url, charset, params, charset2, httpHost, cookieSpecs, headers).getContent();
	}
	
	public static String getHttpResponseByGet(CloseableHttpClient httpclient,String url, String charset,
			HttpHost httpHost, String cookieSpecs,Header[] headers) {
		return reqByGet(httpclient, url, charset, httpHost, cookieSpecs, headers).getContent();
		
	}
	
	
	public static void close(CloseableHttpClient httpClient){
		if(httpClient!=null){
			try {
				httpClient.close();
			} catch (IOException e) {
			}
		}
	}
	
	/**
	 * 获取响应编码，ps：response只能EntityUtils.toString一次
	 * @param response
	 * @return
	 */
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
	
	public static String getHttpResponseByPost(CloseableHttpClient httpclient,String url, String charset,
			List<NameValuePair> params, String charset2, HttpHost httpHost,
			String cookieSpecs) {
		return getHttpResponseByPost(httpclient, url, charset, params, charset2, httpHost, cookieSpecs,defaultHeaders);
	}
	
	public static String getHttpResponseByPost(String url, String charset,
			List<NameValuePair> params, String charset2, HttpHost httpHost,
			String cookieSpecs) {
		return getHttpResponseByPost(defaultHttpClient, url, charset, params, charset2, httpHost, cookieSpecs,defaultHeaders);
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
	
	public static String getHttpResponseByGet(String url, String charset,
			HttpHost httpHost, String cookieSpecs) {
		return getHttpResponseByGet(defaultHttpClient, url, charset, httpHost, cookieSpecs,defaultHeaders);

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
