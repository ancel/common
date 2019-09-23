package com.work.common.utils.http;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.CodingErrorAction;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.MessageConstraints;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.DnsResolver;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.conn.SystemDefaultDnsResolver;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.codecs.DefaultHttpRequestWriterFactory;
import org.apache.http.impl.nio.codecs.DefaultHttpResponseParser;
import org.apache.http.impl.nio.codecs.DefaultHttpResponseParserFactory;
import org.apache.http.impl.nio.conn.ManagedNHttpClientConnectionFactory;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicLineParser;
import org.apache.http.message.LineParser;
import org.apache.http.nio.NHttpMessageParser;
import org.apache.http.nio.NHttpMessageParserFactory;
import org.apache.http.nio.NHttpMessageWriterFactory;
import org.apache.http.nio.conn.ManagedNHttpClientConnection;
import org.apache.http.nio.conn.NHttpConnectionFactory;
import org.apache.http.nio.conn.NoopIOSessionStrategy;
import org.apache.http.nio.conn.SchemeIOSessionStrategy;
import org.apache.http.nio.conn.ssl.SSLIOSessionStrategy;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.nio.reactor.IOReactorException;
import org.apache.http.nio.reactor.SessionInputBuffer;
import org.apache.http.nio.util.HeapByteBufferAllocator;
import org.apache.http.util.CharArrayBuffer;

public final class LocalHttpAsyncClients extends MyHttpClients{
	
	private int ioThreadCount = Runtime.getRuntime().availableProcessors();
	private HttpAsyncClientBuilder httpAsyncClientBuilder;
	
	public CloseableHttpAsyncClient create() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, IOReactorException {
		if(null==httpAsyncClientBuilder) {
			synchronized (this) {
				if(null==httpAsyncClientBuilder){
					httpAsyncClientBuilder = createHttpAsyncClientBuilder();
				}
			}
		}
		return httpAsyncClientBuilder.build();
	}
	
	public HttpAsyncClientBuilder createHttpAsyncClientBuilder() throws KeyManagementException, IOReactorException, NoSuchAlgorithmException, KeyStoreException {
		return HttpAsyncClients.custom()
	            .setConnectionManager(getConnManager())
	            .setDefaultCookieStore(new BasicCookieStore())
	            .setDefaultCredentialsProvider(new BasicCredentialsProvider())
	            .setDefaultRequestConfig(getRequestConfig());
	}
	

	private PoolingNHttpClientConnectionManager getConnManager() throws IOReactorException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException{
		 // Use custom message parser / writer to customize the way HTTP
        // messages are parsed from and written out to the data stream.
        NHttpMessageParserFactory<HttpResponse> responseParserFactory = new DefaultHttpResponseParserFactory() {
            @Override
            public NHttpMessageParser<HttpResponse> create(
                    final SessionInputBuffer buffer,
                    final MessageConstraints constraints) {
                LineParser lineParser = new BasicLineParser() {

                    @Override
                    public Header parseHeader(final CharArrayBuffer buffer) {
                        try {
                            return super.parseHeader(buffer);
                        } catch (ParseException ex) {
                            return new BasicHeader(buffer.toString(), null);
                        }
                    }
                };
                return new DefaultHttpResponseParser(
                        buffer, lineParser, DefaultHttpResponseFactory.INSTANCE, constraints);
            }

        };
        NHttpMessageWriterFactory<HttpRequest> requestWriterFactory = new DefaultHttpRequestWriterFactory();

        // Use a custom connection factory to customize the process of
        // initialization of outgoing HTTP connections. Beside standard connection
        // configuration parameters HTTP connection factory can define message
        // parser / writer routines to be employed by individual connections.
        NHttpConnectionFactory<ManagedNHttpClientConnection> connFactory = new ManagedNHttpClientConnectionFactory(
                requestWriterFactory, responseParserFactory, HeapByteBufferAllocator.INSTANCE);

        // Create a registry of custom connection session strategies for supported
        // protocol schemes.
        Registry<SchemeIOSessionStrategy> sessionStrategyRegistry = RegistryBuilder.<SchemeIOSessionStrategy>create()
            .register("http", NoopIOSessionStrategy.INSTANCE)
            .register("https", new SSLIOSessionStrategy(getSSLContext(), NoopHostnameVerifier.INSTANCE))
            .build();

        // Use custom DNS resolver to override the system DNS resolution.
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

        // Create I/O reactor configuration
        IOReactorConfig ioReactorConfig = IOReactorConfig.custom()
                .setIoThreadCount(ioThreadCount)
                .setConnectTimeout(connectTimeout)
                .setSoTimeout(socketTimeout)
                .setTcpNoDelay(true)
                .build();

        // Create a custom I/O reactort
        ConnectingIOReactor ioReactor = new DefaultConnectingIOReactor(ioReactorConfig);

        // Create a connection manager with custom configuration.
        PoolingNHttpClientConnectionManager connManager = new PoolingNHttpClientConnectionManager(
                ioReactor, connFactory, sessionStrategyRegistry, dnsResolver);

        // Create message constraints
        MessageConstraints messageConstraints = MessageConstraints.custom().build();
        // Create connection configuration
        ConnectionConfig connectionConfig = ConnectionConfig.custom()
            .setMalformedInputAction(CodingErrorAction.IGNORE)
            .setUnmappableInputAction(CodingErrorAction.IGNORE)
            .setCharset(Consts.UTF_8)
            .setMessageConstraints(messageConstraints)
            .build();
        // Configure the connection manager to use connection configuration either
        // by default or for a specific host.
        connManager.setDefaultConnectionConfig(connectionConfig);
        
        // Configure total max or per route limits for persistent connections
        // that can be kept in the pool or leased by the connection manager.
        connManager.setMaxTotal(maxTotal);
        connManager.setDefaultMaxPerRoute(maxPerRoute);

		return connManager;
		
	}
	
	
	public HttpAsyncClientBuilder getHttpAsyncClientBuilder() {
		return httpAsyncClientBuilder;
	}
	public void setHttpAsyncClientBuilder(HttpAsyncClientBuilder httpAsyncClientBuilder) {
		this.httpAsyncClientBuilder = httpAsyncClientBuilder;
	}

	public int getIoThreadCount() {
		return ioThreadCount;
	}

	public void setIoThreadCount(int ioThreadCount) {
		this.ioThreadCount = ioThreadCount;
	}
}
