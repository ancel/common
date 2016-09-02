package com.work.common.utils.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.CharArrayBuffer;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.work.common.constant.CharSet;

/**
 * http请求工具类
 * 
 * @author admin
 * 
 */
public class HttpClientUtil {

	public static final Logger LOGGER = LoggerFactory
			.getLogger(HttpClientUtil.class);

	/**
	 * 
	 * @param url
	 * @param charset
	 *            请求参数编码
	 * @param params
	 * @param charset2
	 *            response编码
	 * @param httpHost
	 *            代理
	 * @param cookieSpecs
	 *            cookie策略
	 * @return
	 * @throws Exception
	 */
	public static String getHttpResponseByPost(String url, String charset,
			List<NameValuePair> params, String charset2, HttpHost httpHost,
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
				"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		httpPost.setHeader("Accept-Encoding", "gzip,deflate,sdch");
		httpPost.setHeader("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6");
		httpPost.setHeader("Connection", "keep-alive");
		httpPost.setHeader(
				"User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/36.0.1985.125 Safari/537.36");
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
						// TODO Auto-generated catch block
						LOGGER.error("request-------" + url, e2);
					}finally{
						flag = false;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
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

	/**
	 * 
	 * @param url
	 * @param charset
	 * @param httpHost
	 * @param cookieSpecs
	 *            例如不使用cookie时为CookieSpecs.IGNORE_COOKIES
	 * @return
	 * @throws Exception
	 */
	public static String getHttpResponseByGet(String url, String charset,
			HttpHost httpHost, String cookieSpecs) {
		// CloseableHttpClient httpclient = HttpClients.createDefault();
		CloseableHttpClient httpclient = HttpClientManager
				.getHttpClient(httpHost);
		HttpGet httpGet = new HttpGet(url);
		// cookie策略
		RequestConfig config = RequestConfig.custom().setConnectTimeout(30000)
				.setSocketTimeout(30000).setCookieSpec(cookieSpecs).setCircularRedirectsAllowed(true).build();
		httpGet.setConfig(config);
		httpGet.setHeader("Accept",
				"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		httpGet.setHeader("Accept-Encoding", "gzip,deflate,sdch");
		httpGet.setHeader("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6");
		httpGet.setHeader("Connection", "keep-alive");
		httpGet.setHeader(
				"User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/36.0.1985.125 Safari/537.36");
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
					if (StringUtils.isBlank(charset)) {
						responseStr = EntityUtils.toString(entity);
					} else {
						 responseStr = EntityUtils.toString(entity, charset);
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
						// TODO Auto-generated catch block
						LOGGER.error("request-------" + url, e2);
					}finally{
						flag = false;
					}
				}
			} catch (Exception e) {
				
				//代理挂了继续跑
				if(e instanceof HttpHostConnectException){
					LOGGER.error("==============proxy down!!!========");
					flag = true;
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}else{
					LOGGER.error("request-------" + url, e);
					//非正常运行结束（除代理挂了的情况），关闭httpclient
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
		return responseStr;

	}

	/**
	 * 
	 * @param url
	 * @param params
	 * @param httpHost
	 * @param charset
	 * @param charset2
	 * @return
	 * @throws Exception
	 */
	public static String getHttpResponseByPost(String url,
			List<NameValuePair> params, HttpHost httpHost, String charset,
			String charset2) {
		return getHttpResponseByPost(url, charset, params, charset2, httpHost,
				CookieSpecs.IGNORE_COOKIES);
	}

	/**
	 * 不使用cookie
	 * 
	 * @param url
	 * @param params
	 * @param charset
	 *            请求编码
	 * @param charset2
	 *            响应编码
	 * @return
	 * @throws Exception
	 */
	public static String getHttpResponseByPost(String url,
			List<NameValuePair> params, String charset, String charset2) {
		return getHttpResponseByPost(url, charset, params, charset2, null,
				CookieSpecs.IGNORE_COOKIES);
	}

	/**
	 * 不使用cookie
	 * 
	 * @param url
	 * @param httpHost
	 * @return
	 * @throws Exception
	 */
	public static String getHttpResponseByGet(String url, HttpHost httpHost,
			String charset) {
		return getHttpResponseByGet(url, charset, httpHost,
				CookieSpecs.IGNORE_COOKIES);
	}

	/**
	 * 不使用cookie
	 * 
	 * @param url
	 * @param charset
	 *            返回结果编码
	 * @return
	 * @throws Exception
	 */
	public static String getHttpResponseByGet(String url, String charset) {
		return getHttpResponseByGet(url, charset, null,
				CookieSpecs.IGNORE_COOKIES);
	}

	/**
	 * cookie
	 * 
	 * @param url
	 * @param charset
	 *            返回结果编码
	 * @return
	 * @throws Exception
	 */
	public static String getHttpResponseByGet(String url, String charset,
			String cookieSpecs) {
		return getHttpResponseByGet(url, charset, null, cookieSpecs);
	}

	@SuppressWarnings("unused")
	private static String printResponse(HttpResponse response) {
		HttpEntity entity = response.getEntity();
		StringBuffer sb = new StringBuffer();
		try {
			BufferedReader is = new BufferedReader(new InputStreamReader(
					entity.getContent()));
			String line = null;
			while ((line = is.readLine()) != null) {
				// System.out.println(line);
				sb.append(line);
			}
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sb.toString();
	}

	public static String processEntity(HttpEntity entity, String charset) {
		InputStream is = null;
		if (entity.getContentEncoding() != null
				&& entity.getContentEncoding().getValue()
						.equalsIgnoreCase("gzip")) {
			GzipDecompressingEntity gde = new GzipDecompressingEntity(entity);
			try {
				is = gde.getContent();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		CharArrayBuffer buffer = null;
		try {
			if (is == null)
				is = entity.getContent();
			int i = (int) entity.getContentLength();
			if (i < 0) {
				i = 4096;
			}
			Reader reader = new InputStreamReader(is, charset);
			buffer = new CharArrayBuffer(i);
			try {
				char[] tmp = new char[1024];
				int l;
				while ((l = reader.read(tmp)) != -1) {
					buffer.append(tmp, 0, l);
				}
			} catch (Exception e) {
				e.printStackTrace();
				return buffer.toString();
			} finally {
				reader.close();
				is.close();
			}
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer.toString();
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
		int visitNum = 0;
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
				}else{
					visitNum++;
					System.out.println(url+"---"+visitNum);
				}
			}
		}else{
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
					System.out.println(url+"---"+visitNum);
				}
			}
		}
		return response;
	}
	
	/**
	 * 不使用代理，请求限制20次
	 * @param url
	 * @return
	 */
	public static String getResponse(String url){
		return getResponse(url,null,CharSet.UTF_8,20);
	}
	
	public static String getResponse(List<NameValuePair> params,String url,String charset,String charset2,int visitLimit){
		String response = null;
		int visitNum = 1;
		while(visitNum<visitLimit){
			response = HttpClientUtil.getHttpResponseByPost(url, params, CharSet.UTF_8, CharSet.UTF_8);
			if(StringUtils.isNotBlank(response)){
				break;
			}else{
				System.out.println(url+"----"+visitNum);
				visitNum++;
			}
		}
		return response;
	}
	public static String getResponse(List<NameValuePair> params,String url){
		return getResponse(params,url,CharSet.UTF_8, CharSet.UTF_8,20);
	}

}
