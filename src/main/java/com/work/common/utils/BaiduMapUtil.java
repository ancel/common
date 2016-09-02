package com.work.common.utils;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.work.common.constant.CharSet;
import com.work.common.enums.FormatEnum;
import com.work.common.utils.http.HttpClientUtil;
/**
 * 百度地图工具类
 * @author admin
 *
 */
public class BaiduMapUtil {
	
	public static final Logger LOGGER = LoggerFactory.getLogger(BaiduMapUtil.class);
	public static final String AK = "B057755dda967237687c3688c060298b";
	public static final String BAIDU_MAP_GEOCODER_API = "http://api.map.baidu.com/geocoder/v2/?address={0}&output={1}&ak={2}&callback={3}&city={4}";
	public static final String BAIDU_MAP_GEOCODER_API2 = "http://api.map.baidu.com/geocoder/v2/?ak={0}&callback={1}&location={2},{3}&output={4}&pois={5}";

	/**
	 *	根据地址查询经纬度 
	 * @param address
	 * @param format
	 * @param ak
	 * @return
	 */
	public static Map<String, String> getLngAndLat(String address,FormatEnum format,String ak){
		String responseStr = "";
		try {
			responseStr = HttpClientUtil.getHttpResponseByGet(MessageFormat.format(BAIDU_MAP_GEOCODER_API, address,format,ak,"",""),CharSet.UTF_8);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return addresssToLngAndLat(responseStr);
	}
	
	/**
	 * 根据地址查询经纬度 
	 * @param address
	 * @param format
	 * @return
	 */
	public static  Map<String, String> getLngAndLat(String address,FormatEnum format){
		String responseStr = "";
		try {
			responseStr = HttpClientUtil.getHttpResponseByGet(MessageFormat.format(BAIDU_MAP_GEOCODER_API, address,format,AK,"",""),CharSet.UTF_8);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return addresssToLngAndLat(responseStr);
	}
	
	/**
	 * 根据地址查询经纬度 
	 * @param address
	 * @return
	 */
	public static  Map<String, String> getLngAndLat(String address){
		String responseStr = "";
		try {
			responseStr = HttpClientUtil.getHttpResponseByGet(MessageFormat.format(BAIDU_MAP_GEOCODER_API, address,FormatEnum.json,AK,"",""),CharSet.UTF_8);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return addresssToLngAndLat(responseStr);
	}
	
	/**
	 * 根据城市和地质查询经纬度
	 * @param city
	 * @param address
	 * @return
	 */
	public static  Map<String, String> getLngAndLat(String city,String address){
		String responseStr = "";
		String url = MessageFormat.format(BAIDU_MAP_GEOCODER_API, address,FormatEnum.json,AK,"",city);
		//System.out.println(url);
		try {
			responseStr = HttpClientUtil.getHttpResponseByGet(url,CharSet.UTF_8);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return addresssToLngAndLat(responseStr);
	}
	
	/**
	 * 根据返回的json解析经纬度
	 * @param str
	 * @return
	 */
	private  static Map<String, String> addresssToLngAndLat(String str){
		Map<String, String> map = new HashMap<String, String>();
		String lng = "";
		String lat = "";
		try {
			JSONObject json = JSONObject.fromObject(str.trim());
			JSONObject result = null;
			if(json.getInt("status")==0){
				result = json.getJSONObject("result");
				if(result!=null&&!"null".equals(result.toString())){
					if(result.getJSONObject("location")!=null){
						lng = json.getJSONObject("result").getJSONObject("location").getString("lng");
						lat = json.getJSONObject("result").getJSONObject("location").getString("lat");
					}
				}
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//LOGGER.error(str);
		}
		map.put("lng", lng);
		map.put("lat", lat);
		return map;
	}
	
	
	
	
	/**
	 * 根据经纬度反查
	 * @param lat
	 * @param lng
	 * @return
	 */
	public static String getAddress(String lat,String lng){
		String responseStr = "";
		String url = MessageFormat.format(BAIDU_MAP_GEOCODER_API2, AK,"",lat,lng,FormatEnum.json,"");
		try {
			responseStr = HttpClientUtil.getHttpResponseByGet(url,CharSet.UTF_8);
			JSONObject json = JSONObject.fromObject(responseStr);
			if(json.getInt("status")==0){
				return json.getJSONObject("result").getString("formatted_address");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOGGER.error(url);
			return "";
		}
		return "";
	}
	
	public static void main(String[] args) {
		Map<String, String> map;
		/*try {
			map = getLngAndLat("北京市",URLEncoder.encode("北京市东城区朝阳门内大街2号凯恒中心A、C、E座","UTF-8"));
			map = getLngAndLat("北京市","北京市东城区朝阳门内大街2号凯恒中心A、C、E座");
			for(String key:map.keySet()){
				System.out.println(map.get(key));
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		

		map = getLngAndLat("南昌市","南昌市桃苑小区海关楼");
		for(String key:map.keySet()){
			System.out.println(map.get(key));
		}

	}
	
	
}
