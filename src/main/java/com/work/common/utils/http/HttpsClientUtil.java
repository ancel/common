package com.work.common.utils.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;

import javax.net.ssl.SSLContext;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.work.common.constant.CharSet;
/**
 * 1、浏览器导出证书文件，xx.cer
 * 2、使用keytool工具将证书转换成密钥形式
    keytool -import -alias xx -file d:\xx.cer -keystore xx.keystore
 * 
 * 创建人：wanghaibo <br>
 * 创建时间：2015-4-29 下午1:41:34 <br>
 * 功能描述： <br>
 * 版本： <br>
 * ====================================== <br>
 * 修改记录 <br>
 * ====================================== <br>
 * 序号 姓名 日期 版本 简单描述 <br>
 *
 */
public class HttpsClientUtil {
	
	public static final Logger LOGGER = LoggerFactory
			.getLogger(HttpsClientUtil.class);
	
	/**
	 * @param keyStorePath  keystore文件路径
	 * @param keyStorePassword  keystore密码
	 * @return
	 */
	public static SSLConnectionSocketFactory getSSLConnectionSocketFactory(String keyStorePath,String keyStorePassword){
		SSLConnectionSocketFactory sslsf = null;
		try {
			KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
			FileInputStream instream = new FileInputStream(new File(keyStorePath));
			try {
				trustStore.load(instream, keyStorePassword.toCharArray());
			} finally {
				instream.close();
			}

			// Trust own CA and all self-signed certs
			SSLContext sslcontext = SSLContexts.custom()
					.loadTrustMaterial(trustStore, new TrustSelfSignedStrategy())
					.build();
			// Allow TLSv1 protocol only
			sslsf = new SSLConnectionSocketFactory(
					sslcontext, new String[] { "TLSv1" }, null,
					SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return sslsf;
	}
	
	public static String getResponseByGet(SSLConnectionSocketFactory sslsf,HttpHost httpHost,String url,String charset){
		CloseableHttpClient httpclient = HttpClients.custom()
				.setSSLSocketFactory(sslsf).build();
		HttpGet httpGet = new HttpGet(url);
		CloseableHttpResponse response = null;
		HttpEntity entity;
		String responseStr = null;
		boolean flag = true;
		while(flag){
			try {
				// 开始请求
				if(httpHost==null){
					response = httpclient.execute(httpGet);
				}else{
					response = httpclient.execute(httpHost,httpGet);
				}

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
	public static String getResponseByGet(SSLConnectionSocketFactory sslsf,HttpHost httpHost,String url){
		return getResponseByGet(sslsf,httpHost,url,CharSet.UTF_8);
	}
	public static String getResponseByGet(SSLConnectionSocketFactory sslsf,String url){
		return getResponseByGet(sslsf,null,url,CharSet.UTF_8);
	}
	
	public static void main(String[] args) {
		String keyStorePath = "C:\\Users\\Li Yujie\\huoche.keystore";
		String keyStorePassword = "111111";
		SSLConnectionSocketFactory sslsf =  HttpsClientUtil.getSSLConnectionSocketFactory(keyStorePath, keyStorePassword);
		
		String url = "https://kyfw.12306.cn/otn/leftTicket/query?leftTicketDTO.train_date=2015-04-29&leftTicketDTO.from_station=BJP&leftTicketDTO.to_station=SHH&purpose_codes=ADULT";
		String content = HttpsClientUtil.getResponseByGet(sslsf,null, url);
		System.out.println(content);
	}
}
