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
	private static ConnectionConfig connectionConfig;
	private static HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connFactory;
	private static Registry<ConnectionSocketFactory> socketFactoryRegistry;
	private static DnsResolver dnsResolver;
	private static  SocketConfig socketConfig;
	private static RequestConfig defaultRequestConfig;
	/**
	 * 最大连接数
	 */
	public final static int MAX_TOTAL_CONNECTIONS = 200;
	/**
	 * 每个路由最大连接数
	 */
	public final static int MAX_PER_ROUTE = 100;
	
	static {
		HttpMessageWriterFactory<HttpRequest> requestWriterFactory = new DefaultHttpRequestWriterFactory();
        
		// Use a custom connection factory to customize the process of
        // initialization of outgoing HTTP connections. Beside standard connection
        // configuration parameters HTTP connection factory can define message
        // parser / writer routines to be employed by individual connections.
        connFactory = new ManagedHttpClientConnectionFactory(
                requestWriterFactory, new DefaultHttpResponseParserFactory());
		
		// SSL context for secure connections can be created either based on
        // system or application specific properties.
        SSLContext sslcontext = SSLContexts.createSystemDefault();
        // Use custom hostname verifier to customize SSL hostname verification.
        X509HostnameVerifier hostnameVerifier = new BrowserCompatHostnameVerifier();

		// Create a registry of custom connection socket factories for supported
        // protocol schemes.
        socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
            .register("http", PlainConnectionSocketFactory.INSTANCE)
            .register("https", new SSLConnectionSocketFactory(sslcontext, hostnameVerifier))
            .build();

        // Use custom DNS resolver to override the system DNS resolution.
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
		// Create socket configuration
        socketConfig = SocketConfig.custom()
            .setTcpNoDelay(true)
            .build();
		// Create message constraints
        MessageConstraints messageConstraints = MessageConstraints.custom()
//            .setMaxHeaderCount(2000)
//            .setMaxLineLength(20000)
            .build();
		// Create connection configuration
        connectionConfig = ConnectionConfig.custom()
            .setMalformedInputAction(CodingErrorAction.IGNORE)
            .setUnmappableInputAction(CodingErrorAction.IGNORE)
            .setCharset(Consts.UTF_8)
            .setMessageConstraints(messageConstraints)
            .build();
        // Create global request configuration
        defaultRequestConfig = RequestConfig.custom()
            .setCookieSpec(CookieSpecs.BEST_MATCH)
            .setExpectContinueEnabled(true)
            .setStaleConnectionCheckEnabled(true)
            .setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST))
            .setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC))
            .build();
        
	}
	
	public static PoolingHttpClientConnectionManager getConnectionManager(){
		PoolingHttpClientConnectionManager connManager;
        
		// Create a connection manager with custom configuration.
        connManager = new PoolingHttpClientConnectionManager(
                socketFactoryRegistry, connFactory, dnsResolver);
        
        // Configure the connection manager to use socket configuration either
        // by default or for a specific host.
        connManager.setDefaultSocketConfig(socketConfig);
//        connManager.setSocketConfig(new HttpHost("somehost", 80), socketConfig);
        
        // Configure the connection manager to use connection configuration either
        // by default or for a specific host.
        connManager.setDefaultConnectionConfig(connectionConfig);
//        connManager.setConnectionConfig(new HttpHost("somehost", 80), ConnectionConfig.DEFAULT);

        // Configure total max or per route limits for persistent connections
        // that can be kept in the pool or leased by the connection manager.
        connManager.setMaxTotal(MAX_TOTAL_CONNECTIONS);
        connManager.setDefaultMaxPerRoute(MAX_PER_ROUTE);
        return connManager;
	}
	
	public static CloseableHttpClient getHttpClient(HttpHost httpHost) {
		// Create an HttpClient with the given custom dependencies and configuration.
		CloseableHttpClient httpclient = HttpClients.custom()
				.setConnectionManager(getConnectionManager())
				.setDefaultCookieStore(new BasicCookieStore())
				.setDefaultCredentialsProvider(new BasicCredentialsProvider())
				.setProxy(httpHost)
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
