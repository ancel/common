package com.work.common.demo;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import com.work.common.constant.CharSet;
import com.work.common.utils.BaiduMapUtil;
import com.work.common.utils.StringUtil;

public class BaiduMapDemo {
	public static void main(String[] args) {
		String city = "北京";
		String address = "静安中心";
		Map<String, String> map = BaiduMapUtil.getLngAndLat(city, address);
		if(StringUtil.isBlank(map.get("lng"))){
			try {
				map = BaiduMapUtil.getLngAndLat(city, URLEncoder.encode(address, CharSet.UTF_8));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
