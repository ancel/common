package com.work.common.utils.http;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.net.ssl.SSLContext;

import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;

public class MyHttpClients {
	protected String protocol = "TLSv1.2";
	protected List<KeyStore> keyStores;
	// 每个站点的最大连接数
	protected int maxPerRoute = 100;
	// 链接池最大连接数
	protected int maxTotal = 100;
	protected int connectTimeout = 60000;
	protected int socketTimeout = 60000;
	protected boolean normalizeUri = true;

	public MyHttpClients() {
		keyStores = new ArrayList<KeyStore>();
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
	
	public RequestConfig getRequestConfig(){
		return RequestConfig.custom()
			.setCookieSpec(CookieSpecs.DEFAULT)
			.setExpectContinueEnabled(true)
			.setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST))
			.setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC))
			.setConnectTimeout(connectTimeout)
			.setSocketTimeout(socketTimeout)
			.setNormalizeUri(normalizeUri)
			.setRedirectsEnabled(true)
			.setContentCompressionEnabled(true)
			.build();
	}
	
	public SSLContext getSSLContext() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException{
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
		return sslContextBuilder.build();
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

	public boolean getNormalizeUri() {
		return normalizeUri;
	}

	public void setNormalizeUri(boolean normalizeUri) {
		this.normalizeUri = normalizeUri;
	}

}
