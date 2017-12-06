package com.work.common.utils.http;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
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
	private byte[] contentBytes;
	private String charset;
	
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
	public String getContent() throws UnsupportedEncodingException {
		if(null==contentBytes){
			return null;
		}
		if(StringUtils.isBlank(charset)){
			return new String(contentBytes);
		}else{
			return new String(contentBytes, charset);
		}
	}
	public byte[] getContentBytes() {
		return contentBytes;
	}
	public void setContentBytes(byte[] contentBytes) {
		this.contentBytes = contentBytes;
	}
	
	public String getCharset() {
		return charset;
	}
	public void setCharset(String charset) {
		this.charset = charset;
	}
	@Override
	public String toString() {
		return "ResponseBall [url=" + url + ", statusCode=" + statusCode
				+ ", headers=" + Arrays.toString(headers) + ", contentBytes="
				+ Arrays.toString(contentBytes) + "]";
	}
	
	
}
