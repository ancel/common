package com.work.common.utils.http;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.CodingErrorAction;
import java.util.Arrays;

import javax.net.ssl.SSLContext;

import org.apache.http.Consts;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
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
import org.apache.http.conn.ssl.BrowserCompatHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.ManagedHttpClientConnectionFactory;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.conn.SystemDefaultDnsResolver;
import org.apache.http.impl.io.DefaultHttpRequestWriterFactory;
import org.apache.http.impl.io.DefaultHttpResponseParserFactory;
import org.apache.http.io.HttpMessageWriterFactory;

import com.work.common.constant.CharSet;

public class HttpClientManager {
	/**
	 * 最大连接数
	 */
	public final static int MAX_TOTAL_CONNECTIONS = 200;
	/**
	 * 每个路由最大连接数
	 */
	public final static int MAX_PER_ROUTE = 100;
	
	public static RequestConfig defaultRequestConfig;
	public static Registry<ConnectionSocketFactory> socketFactoryRegistry;
	public static HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connFactory;
	public static DnsResolver dnsResolver;
	public static SocketConfig socketConfig;
	public static ConnectionConfig connectionConfig;

	static {
		HttpMessageWriterFactory<HttpRequest> requestWriterFactory = new DefaultHttpRequestWriterFactory();
		connFactory = new ManagedHttpClientConnectionFactory(
                requestWriterFactory, new DefaultHttpResponseParserFactory());
		
        SSLContext sslcontext = SSLContexts.createSystemDefault();
        X509HostnameVerifier hostnameVerifier = new BrowserCompatHostnameVerifier();
        socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
            .register("http", PlainConnectionSocketFactory.INSTANCE)
            .register("https", new SSLConnectionSocketFactory(sslcontext, hostnameVerifier))
            .build();

        dnsResolver = new SystemDefaultDnsResolver() {
            @Override
            public InetAddress[] resolve(final String host) throws UnknownHostException {
                if (host.equalsIgnoreCase("localhost")) {
                    return new InetAddress[] { InetAddress.getByAddress(new byte[] {127, 0, 0, 1}) };
                } else {
                    return super.resolve(host);
                }
            }

        };
        
        socketConfig = SocketConfig.custom()
            .setTcpNoDelay(true)
            .build();
        
        MessageConstraints messageConstraints = MessageConstraints.custom()
            .build();
        connectionConfig = ConnectionConfig.custom()
            .setMalformedInputAction(CodingErrorAction.IGNORE)
            .setUnmappableInputAction(CodingErrorAction.IGNORE)
            .setCharset(Consts.UTF_8)
            .setMessageConstraints(messageConstraints)
            .build();
        
        defaultRequestConfig = RequestConfig.custom()
            .setCookieSpec(CookieSpecs.BEST_MATCH)
            .setExpectContinueEnabled(true)
            .setStaleConnectionCheckEnabled(true)
            .setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST))
            .setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC))
            .setConnectTimeout(30000)
			.setSocketTimeout(30000)
            .build();
	}

	public static PoolingHttpClientConnectionManager getConnManager(){
		PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(
                socketFactoryRegistry, connFactory, dnsResolver);
        connManager.setDefaultSocketConfig(socketConfig);
        connManager.setDefaultConnectionConfig(connectionConfig);
        connManager.setMaxTotal(MAX_TOTAL_CONNECTIONS);
        connManager.setDefaultMaxPerRoute(MAX_PER_ROUTE);
        return connManager;
	}
	
	
	public static CloseableHttpClient getHttpClient() {
		CloseableHttpClient httpclient = HttpClients.custom()
				.setConnectionManager(getConnManager())
				.setDefaultCookieStore(new BasicCookieStore())
				.setDefaultCredentialsProvider(new BasicCredentialsProvider())
				.setDefaultRequestConfig(defaultRequestConfig)
				.build();
		return httpclient;
	}

	public static void main(String[] args) {
		String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=-33.8670522,151.1957362&radius=500&types=food&name=harbour&sensor=false&key=AIzaSyBR0i9RL44iG8IUx9LcCgxsYOJf6FutQhE";
		HttpHost httpHost = new HttpHost("proxy.dianhua.cn", 8080);
		System.out.println(HttpClientUtil.getHttpResponseByGet(url, httpHost, CharSet.UTF_8));
	}
}
