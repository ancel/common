package com.work.common.test;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.BooleanUtils;

import com.work.common.utils.DateUtil;
import com.work.common.utils.encryption.EncryptionUtil;

public class MayiProxy {
	private String hostname = "s5.proxy.mayidaili.com";
	private int port = 8123;
//	private String appKey = "208564216";
//	private String secret = "00b823785b2c26f172b8f054b02e2b44";
	private String appKey = "114336114";
	private String secret = "a5ff9ee15b68003d28f17d9aa0fb0ae8";

	public Map<String, String> getHeaders() throws NoSuchAlgorithmException, UnsupportedEncodingException {
		StringBuilder sb = new StringBuilder();
		String timestamp = DateUtil.getDateStr(DateUtil.TIME_FORMAT);
		sb.append(secret).append("app_key").append(appKey).append("timestamp")
				.append(timestamp).append(secret);
		String sign = EncryptionUtil.md5(sb.toString()).toUpperCase();
		String authHeaderValue = MessageFormat.format("MYH-AUTH-MD5 app_key={0}&timestamp={1}&sign={2}", appKey, timestamp, sign);
		Map<String, String> map = new HashMap<String, String>(1);
		map.put("proxy-Authorization", authHeaderValue);
		return map;
	}

	
	public String getHostname() {
		return hostname;
	}

	public int getPort() {
		return port;
	}


	public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		MayiProxy mayi = new MayiProxy();
		System.out.println(mayi.getHeaders());
		System.out.println(BooleanUtils.toBoolean("y"));
		System.out.println(BooleanUtils.toBoolean("n"));
		System.out.println(BooleanUtils.toBoolean("null"));
		System.out.println(BooleanUtils.toBoolean(""));
		String a = null;
		System.out.println(BooleanUtils.toBoolean(a));
	}
}
