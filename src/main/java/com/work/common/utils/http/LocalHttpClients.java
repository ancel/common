package com.work.common.utils.http;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.CodingErrorAction;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.ManagedHttpClientConnectionFactory;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.conn.SystemDefaultDnsResolver;
import org.apache.http.impl.io.DefaultHttpRequestWriterFactory;
import org.apache.http.impl.io.DefaultHttpResponseParserFactory;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;

public final class LocalHttpClients{
	
	private String protocol = "TLSv1.3";
	private List<KeyStore> keyStores;
	// 每个站点的最大连接数
	private int maxPerRoute = 100;
	// 链接池最大连接数
	private int maxTotal = 100;
	private int connectTimeout = 60000;
	private int socketTimeout = 60000;
	
	public LocalHttpClients() {
		keyStores = new ArrayList<KeyStore>();
	}
	
	public CloseableHttpClient create() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
		return getHttpClientBuilder().build();
	}
	
	public HttpClientBuilder getHttpClientBuilder() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
		return HttpClients.custom()
				.useSystemProperties()
				.setConnectionManager(getConnectionManager())
				.setDefaultCredentialsProvider(new BasicCredentialsProvider())
				.setDefaultRequestConfig(getRequestConfig());
	}
	
	
	public void addKeyStore(String keyStoreFilename, String keyStorePassword, String keyStoreType) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException{
		final KeyStore trustStore = KeyStore.getInstance(keyStoreType);
        final FileInputStream inStream = new FileInputStream(keyStoreFilename);
        try {
            trustStore.load(inStream, keyStorePassword.toCharArray());
        } finally {
            inStream.close();
        }
        keyStores.add(trustStore);
	}

	public PoolingHttpClientConnectionManager getConnectionManager() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException{
		ConnectionSocketFactory sslConnectionSocketFactory = getSSLConnectionSocketFactory();
		Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder
				.<ConnectionSocketFactory> create()
				.register("http", PlainConnectionSocketFactory.INSTANCE)
				.register("https", sslConnectionSocketFactory)
				.build();

		DnsResolver dnsResolver = new SystemDefaultDnsResolver() {
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
		HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connectionFactory = new ManagedHttpClientConnectionFactory(
				new DefaultHttpRequestWriterFactory(),
				new DefaultHttpResponseParserFactory());

		MessageConstraints messageConstraints = MessageConstraints.custom()
				.build();
		ConnectionConfig connectionConfig = ConnectionConfig.custom()
				.setMalformedInputAction(CodingErrorAction.IGNORE)
				.setUnmappableInputAction(CodingErrorAction.IGNORE)
				.setCharset(Consts.UTF_8)
				.setMessageConstraints(messageConstraints).build();
		
		//!!!这里一定要设定sockettimeout, 否则https的handshake读取数据会卡死
		SocketConfig socketConfig = SocketConfig.custom().setTcpNoDelay(true).setSoTimeout(socketTimeout).build();
		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(
				new SniHttpClientConnectionOperator(socketFactoryRegistry,
						null, dnsResolver), connectionFactory, -1,
				TimeUnit.MILLISECONDS);
		connectionManager.setDefaultSocketConfig(socketConfig);
		connectionManager.setDefaultConnectionConfig(connectionConfig);
		connectionManager.setMaxTotal(maxTotal);
		connectionManager.setDefaultMaxPerRoute(maxPerRoute);
		return connectionManager;
		
	}
	public SSLConnectionSocketFactory getSSLConnectionSocketFactory() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException{
		SSLContext sslContext;
		SSLContextBuilder sslContextBuilder = SSLContexts.custom().setProtocol(protocol);
		sslContextBuilder.loadTrustMaterial(new TrustStrategy() {
			@Override
			public boolean isTrusted(X509Certificate[] chain, String authType)
					throws CertificateException {
				return true;
			}
		});
		if(null!=keyStores&&keyStores.size()>0){
			for (KeyStore keyStore : keyStores) {
				sslContextBuilder.loadTrustMaterial(keyStore, new TrustSelfSignedStrategy());
			}
		}
		sslContext = sslContextBuilder.build();
		return new SniSSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
	}	
	
	public RequestConfig getRequestConfig(){
		return RequestConfig.custom()
			.setCookieSpec(CookieSpecs.DEFAULT)
			.setExpectContinueEnabled(true)
			.setTargetPreferredAuthSchemes(
					Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST))
			.setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC))
			.setConnectTimeout(connectTimeout)
			.setSocketTimeout(socketTimeout)
			.build();
	}
	

	public void addKeyStore(KeyStore keyStore){
		keyStores.add(keyStore);
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public List<KeyStore> getKeyStores() {
		return keyStores;
	}

	public void setKeyStores(List<KeyStore> keyStores) {
		this.keyStores = keyStores;
	}

	public int getMaxPerRoute() {
		return maxPerRoute;
	}

	public void setMaxPerRoute(int maxPerRoute) {
		this.maxPerRoute = maxPerRoute;
	}

	public int getMaxTotal() {
		return maxTotal;
	}

	public void setMaxTotal(int maxTotal) {
		this.maxTotal = maxTotal;
	}

	public int getConnectTimeout() {
		return connectTimeout;
	}

	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	public int getSocketTimeout() {
		return socketTimeout;
	}

	public void setSocketTimeout(int socketTimeout) {
		this.socketTimeout = socketTimeout;
	}
	
	
}
