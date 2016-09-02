package com.work.common.demo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.work.common.constant.CharSet;
import com.work.common.utils.http.HttpClientUtil;


/**
 * httpclient模拟登陆
 * @author admin
 *
 */
public class HttpClientLogin {
	public static void main(String[] args) {
		BasicCookieStore cookieStore = new BasicCookieStore();
		HttpPost post = null;
		try {

			//登陆
			String url = "http://www.czvv.com/Application/miniLoginValidate";
			CloseableHttpClient httpclient = HttpClients.custom()
					.setDefaultCookieStore(cookieStore).build();
			post = new HttpPost(url);
			post.setHeader(
					"User-Agent",
					"mozilla/5.0 (linux; u; android 4.2.2; zh-cn; mi-one c1 build/imm76d) uc applewebkit/534.31 (khtml, like gecko) mobile safari/534.31");
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("u", "ancel00"));
			nvps.add(new BasicNameValuePair("p", "victory")); 
			nvps.add(new BasicNameValuePair("callback","postmethodcallback"));
			nvps.add(new BasicNameValuePair("returnValue","returnValue"));
			nvps.add(new BasicNameValuePair("autologin","false"));
			post.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));
			HttpResponse response = httpclient.execute(post);
			String getvalue = EntityUtils.toString(response.getEntity());
System.out.println("getvalue"+getvalue);

			
			//登陆之后继续使用同一个httpclient访问，即可携带使用同一cookie
			HttpGet httpGet = new HttpGet("http://www.czvv.com/kp101c110000ccs0m0e0f0d0.html");
			httpGet.setHeader(
					"User-Agent",
					"mozilla/5.0 (linux; u; android 4.2.2; zh-cn; mi-one c1 build/imm76d) uc applewebkit/534.31 (khtml, like gecko) mobile safari/534.31");
			HttpResponse response2 = httpclient.execute(httpGet);
			String reallyURL = EntityUtils.toString(response2.getEntity());
System.out.println(reallyURL);



			List<Cookie> cookies = cookieStore.getCookies();
			if (cookies.isEmpty()) {
				System.out.println("None");
			} else {
				for (int i = 0; i < cookies.size(); i++) {
					System.out.println("- " + cookies.get(i).toString());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			post.abort();
		}

	}
	
	/**
	 * 判断是否已到尾页
	 * true表示已经是最后一页，反之不是
	 * @param url
	 * @return
	 */
	public boolean isEnd(String url){
		String response = null;
		int num=0;
		try {
			while (num<10) {
				response = HttpClientUtil.getHttpResponseByGet(url, CharSet.UTF_8);
				if(response.contains("503 Service Temporarily Unavailable")){
					num++;
				}else{
					break;
				}
			}
			System.out.println(response);
			if(response.contains("下一页")){
				return false;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
		
	}
}