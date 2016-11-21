package com.work.common.utils.http;

import java.util.Arrays;

import org.apache.http.Header;

/**
 * http响应球
 * @author：wanghaibo 
 * @creattime：2016年11月18日 下午6:04:27 
 * 
 */  
public class ResponseBall {
	private String url;
	private int statusCode;
	private Header[] headers;
	private String content;
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	public Header[] getHeaders() {
		return headers;
	}
	public void setHeaders(Header[] headers) {
		this.headers = headers;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	@Override
	public String toString() {
		return "ResponseBall [url=" + url + ", statusCode=" + statusCode
				+ ", headers=" + Arrays.toString(headers) + ", content="
				+ content + "]";
	}
	
}
