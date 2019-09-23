package com.work.common.utils.http;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.CodingErrorAction;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

import org.apache.http.Consts;
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
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.ManagedHttpClientConnectionFactory;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.conn.SystemDefaultDnsResolver;
import org.apache.http.impl.io.DefaultHttpRequestWriterFactory;
import org.apache.http.impl.io.DefaultHttpResponseParserFactory;

public final class LocalHttpClients extends MyHttpClients{
	
	private HttpClientBuilder httpClientBuilder;
	
	public CloseableHttpClient create() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
		if(null==httpClientBuilder) {
			synchronized (this) {
				if(null==httpClientBuilder){
					httpClientBuilder = createHttpClientBuilder();
				}
			}
		}
		return httpClientBuilder.build();
	}
	public HttpClientBuilder createHttpClientBuilder() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
		return HttpClients.custom()
				.useSystemProperties()
				.setConnectionManager(getConnectionManager())
				.setDefaultCredentialsProvider(new BasicCredentialsProvider())
				.setDefaultRequestConfig(getRequestConfig());
	}
	
	public PoolingHttpClientConnectionManager getConnectionManager() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException{
		Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder
				.<ConnectionSocketFactory> create()
				.register("http", PlainConnectionSocketFactory.INSTANCE)
				.register("https", new SniSSLConnectionSocketFactory(getSSLContext(), NoopHostnameVerifier.INSTANCE))
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
	public HttpClientBuilder getHttpClientBuilder() {
		return httpClientBuilder;
	}
	public void setHttpClientBuilder(HttpClientBuilder httpClientBuilder) {
		this.httpClientBuilder = httpClientBuilder;
	}	
	
}
