package com.work.common.utils.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLContext;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.work.common.constant.CharSet;
import com.work.common.utils.Regex;
/**
 * 1、浏览器导出证书文件，xx.cer，例如chrome可以打开开发者工具之后选择security，然后点击view certificate导出。或者使用ie打开网页后右键属性导出
 * 2、使用keytool工具将证书转换成密钥形式
    keytool -import -alias xx -file d:\xx.cer -keystore xx.keystore
    
 * @author：wanghaibo 
 * @creattime：2016年9月9日 下午4:18:22 
 * 
 */  
public class HttpsClientUtil {
	
	public static final Logger LOGGER = LoggerFactory
			.getLogger(HttpsClientUtil.class);
	
	/**
	 * @param keyStorePath  keystore文件路径
	 * @param keyStorePassword  keystore密码
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws CertificateException
	 * @throws IOException
	 * @throws KeyManagementException
	 * @throws KeyStoreException
	 */
	public static SSLConnectionSocketFactory getSSLConnectionSocketFactory(String keyStorePath,String keyStorePassword) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, KeyManagementException{
		SSLConnectionSocketFactory sslsf = null;
		KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
		FileInputStream instream = new FileInputStream(new File(keyStorePath));
		trustStore.load(instream, keyStorePassword.toCharArray());

		// Trust own CA and all self-signed certs
		SSLContext sslcontext = SSLContextBuilder.create()
				.loadTrustMaterial(trustStore, new TrustSelfSignedStrategy())
				.build();
		// Allow TLSv1 protocol only
		sslsf = new SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1" }, null, NoopHostnameVerifier.INSTANCE);
		
		return sslsf;
	}
	
	/**
	 * @param httpclient CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
	 * @param httpHost
	 * @param url
	 * @param charset
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String getResponseByGet(CloseableHttpClient httpclient,HttpHost httpHost,String url,String charset) throws ClientProtocolException, IOException{
		RequestConfig config = RequestConfig.custom().setConnectTimeout(30000)
				.setSocketTimeout(30000).build();
		HttpGet httpGet = new HttpGet(url);
		httpGet.setConfig(config);
		CloseableHttpResponse response = null;
		HttpEntity entity;
		String responseStr = null;
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
			if(null!=response){
				response.close();
			}
		}
		return responseStr;
	}
	public static String getResponseByGet(CloseableHttpClient httpclient,HttpHost httpHost,String url) throws ClientProtocolException, IOException{
		return getResponseByGet(httpclient,httpHost,url,CharSet.UTF_8);
	}
	public static String getResponseByGet(CloseableHttpClient httpclient,String url) throws ClientProtocolException, IOException{
		return getResponseByGet(httpclient,null,url,CharSet.UTF_8);
	}
	
	public static void main(String[] args) throws Exception{
		String keyStorePath = "C:\\Users\\Li Yujie\\Desktop\\so.keystore";
		String keyStorePassword = "111111";
		SSLConnectionSocketFactory sslsf = null;
		sslsf = HttpsClientUtil.getSSLConnectionSocketFactory(keyStorePath, keyStorePassword);
		CloseableHttpClient httpclient = HttpClients.custom()
				.setSSLSocketFactory(sslsf).build();
		String url = "https://www.so.com/s?q=0592-2177512&src=srp&fr=hao_360so";
		String content = null;
		content = HttpsClientUtil.getResponseByGet(httpclient,null, url);
//		System.out.println(content);
		System.out.println(Regex.regexReadOne(content, "来自<strong class=\"mohe-tips\">“\\s*?<img\\s*?src=\"([\\S]*?)\" class=\"mh-hy-img\"/>"));
		System.out.println(Regex.regexReadOne(content, "<p class=\"mh-detail\">([\\s\\S]*?)"));
		System.out.println(Regex.regexReadOne(content, "<p class=\"mh-detail\">([\\s\\S]*?)</p>"));
		System.out.println(Regex.regexReadOne(content, "<div class=\"gclearfix mh-detail\">([\\s\\S]*?)</div>"));
	}
}
