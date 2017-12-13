package com.work.common.utils.http;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.CodingErrorAction;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;

import org.apache.http.Consts;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.MessageConstraints;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.DnsResolver;
import org.apache.http.conn.HttpConnectionFactory;
import org.apache.http.conn.ManagedHttpClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.ManagedHttpClientConnectionFactory;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.conn.SystemDefaultDnsResolver;
import org.apache.http.impl.io.DefaultHttpRequestWriterFactory;
import org.apache.http.impl.io.DefaultHttpResponseParserFactory;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;

public final class HttpClientManager{
	/**
	 * 最大连接数
	 */
	private int MAX_TOTAL_CONNECTIONS = 200;
	/**
	 * 每个路由最大连接数
	 */
	private int MAX_PER_ROUTE = 100;

	private RequestConfig defaultRequestConfig;
	private Registry<ConnectionSocketFactory> socketFactoryRegistry;
	private HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connFactory;
	private SSLConnectionSocketFactory sslConnectionSocketFactory;
	private SSLConnectionSocketFactory certSSLConnectionSocketFactory;
	private DnsResolver dnsResolver;
	private SocketConfig socketConfig;
	private ConnectionConfig connectionConfig;
	private int connectTimeout = 30000;
	private int socketTimeout = 30000;
	
	static class InstanceHolder{
		final static HttpClientManager INSTANCE = new HttpClientManager();
	}
	public static HttpClientManager getInstance() {
		return InstanceHolder.INSTANCE;
	}
	public HttpClientManager(){
		init();
	}
	private void init(){
		connFactory = new ManagedHttpClientConnectionFactory(
				new DefaultHttpRequestWriterFactory(),
				new DefaultHttpResponseParserFactory());

		sslConnectionSocketFactory = getTrustSSLConnectionSocketFactory();
		socketFactoryRegistry = RegistryBuilder
				.<ConnectionSocketFactory> create()
				.register("http", PlainConnectionSocketFactory.INSTANCE)
				.register("https", sslConnectionSocketFactory)
				.build();

		dnsResolver = new SystemDefaultDnsResolver() {
			@Override
			public InetAddress[] resolve(final String host)
					throws UnknownHostException {
				if (host.equalsIgnoreCase("localhost")) {
					return new InetAddress[] { InetAddress
							.getByAddress(new byte[] { 127, 0, 0, 1 }) };
				} else {
					return super.resolve(host);
				}
			}

		};

		MessageConstraints messageConstraints = MessageConstraints.custom()
				.build();
		connectionConfig = ConnectionConfig.custom()
				.setMalformedInputAction(CodingErrorAction.IGNORE)
				.setUnmappableInputAction(CodingErrorAction.IGNORE)
				.setCharset(Consts.UTF_8)
				.setMessageConstraints(messageConstraints).build();

		socketConfig = SocketConfig.custom().setTcpNoDelay(true).build();

		defaultRequestConfig = RequestConfig
				.custom()
				.setCookieSpec(CookieSpecs.DEFAULT)
				.setExpectContinueEnabled(true)
				.setTargetPreferredAuthSchemes(
						Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST))
				.setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC))
				.setConnectTimeout(connectTimeout).setSocketTimeout(socketTimeout).build();
	}
	
	/**
	 * 跳过ssl验证
	 * @return
	 */
	public SSLConnectionSocketFactory getTrustSSLConnectionSocketFactory(){
		SSLContext sslContext;
		try {
			sslContext = SSLContexts.custom().loadTrustMaterial(new TrustStrategy() {
						@Override
						public boolean isTrusted(X509Certificate[] chain, String authType)
								throws CertificateException {
							return true;
						}
					}).build();
		} catch (Exception e) {
			throw new RuntimeException("SSLContext初始化异常!", e);
		}
		return new SniSSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
	}

	public PoolingHttpClientConnectionManager getConnManager() {
		PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(
				new SniHttpClientConnectionOperator(socketFactoryRegistry,
						null, dnsResolver), connFactory, -1,
				TimeUnit.MILLISECONDS);
		connManager.setDefaultSocketConfig(socketConfig);
		connManager.setDefaultConnectionConfig(connectionConfig);
		connManager.setMaxTotal(MAX_TOTAL_CONNECTIONS);
		connManager.setDefaultMaxPerRoute(MAX_PER_ROUTE);
		return connManager;
	}
	
	/**
	 * 1、浏览器导出证书文件，xx.cer，例如chrome可以打开开发者工具之后选择security，然后点击view certificate导出。或者使用ie打开网页后右键属性导出
	 * 2、使用keytool工具将证书转换成密钥形式
     * keytool -import -alias xx -file d:\xx.cer -keystore xx.keystore
	 * @param keyStorePath
	 * @param keyStorePassword
	 * @return
	 */
	public static SSLConnectionSocketFactory getSSLConnectionSocketFactory(String keyStorePath,String keyStorePassword){
		SSLConnectionSocketFactory sslsf = null;
		SSLContext sslcontext;
		try {
			sslcontext = SSLContexts.custom()
					.loadTrustMaterial(new File(keyStorePath), keyStorePassword.toCharArray(), new TrustSelfSignedStrategy())
					.build();
		} catch (Exception e) {
			throw new RuntimeException("导入证书异常！", e);
		} 
		sslsf = new SSLConnectionSocketFactory(sslcontext, NoopHostnameVerifier.INSTANCE);
		return sslsf;
	}

	public CloseableHttpClient getDefaultHttpClient() {
		CloseableHttpClient httpclient = HttpClients.custom()
				.setConnectionManager(getConnManager())
				.setDefaultCookieStore(new BasicCookieStore())
				.setDefaultCredentialsProvider(new BasicCredentialsProvider())
				.setDefaultRequestConfig(defaultRequestConfig).build();
		return httpclient;
	}
	
	public static CloseableHttpClient getHttpClient() {
		return HttpClientManager.getInstance().getDefaultHttpClient();
	}
	
	public CloseableHttpClient getDefaultHttpsClient(String keyStorePath,String keyStorePassword){
		if(null==certSSLConnectionSocketFactory){
			certSSLConnectionSocketFactory = getSSLConnectionSocketFactory(keyStorePath, keyStorePassword);
		}
		CloseableHttpClient httpclient = HttpClients.custom()
				.setConnectionManager(getConnManager())
				.setDefaultCookieStore(new BasicCookieStore())
				.setDefaultCredentialsProvider(new BasicCredentialsProvider())
				.setDefaultRequestConfig(defaultRequestConfig).build();
		return httpclient;
	}
}
