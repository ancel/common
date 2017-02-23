package com.work.common.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.work.common.utils.Regex;
import com.work.common.utils.http.HttpsClientUtil;

public class So360 {
	public static void main(String[] args) {
		String keyStorePath = "file/so.keystore";
		String keyStorePassword = "111111";
		String urlTemplate = "https://www.so.com/s?q={0}&src=srp&fr=hao_360so";
		SSLConnectionSocketFactory sslsf = null;
		try {
			sslsf = HttpsClientUtil.getSSLConnectionSocketFactory(keyStorePath, keyStorePassword);
		} catch (Exception e1) {
			e1.printStackTrace();
			System.exit(1);
		}
		CloseableHttpClient httpclient = HttpClients.custom()
				.setSSLSocketFactory(sslsf).build();
		String urlPath = "C:\\Users\\Li Yujie\\Desktop\\360_num_20161020.txt";
		String outPath = "C:\\Users\\Li Yujie\\Desktop\\360_result_20161021.txt";
		BufferedReader br;
		BufferedWriter bw;
		try {
			br = new BufferedReader(new FileReader(urlPath));
			bw = new BufferedWriter(new FileWriter(outPath));
			String line;
			String content = null;
			int i=0;
			HttpHost host = new HttpHost("172.18.19.254", 8080);
//			HttpHost host = new HttpHost("proxy.dianhua.cn", 8080);
			List<String> typeList = new ArrayList<String>(2);
			String url;
			while((line=br.readLine())!=null){
				if(StringUtils.isNotBlank(line)){
					while(true){
						System.out.println((++i)+"\t"+line);
						try {
							url = MessageFormat.format(urlTemplate, line.replaceAll("\\s+", ""));
							content = HttpsClientUtil.getResponseByGet(httpclient,host, url);
//							content = HttpsClientUtil.getResponseByGet(httpclient,null, url);
						} catch (Exception e) {
							e.printStackTrace();
							continue;
						}
						if(null==content||(StringUtils.isNotBlank(content)&&content.contains("系统检测到您操作过于频繁"))){
							System.out.println("封锁");
							httpclient.close();
							httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
							Thread.sleep(5000);
						}
						if(StringUtils.isBlank(content)||!content.contains("系统检测到您操作过于频繁")){
							break;
						}
					}
					
					if(StringUtils.isNotBlank(content)){
						if(StringUtils.isNotBlank(Regex.regexReadOne(content, "target=\"_blank\" data-md='\\{\"p\":\"more\"\\}'>([\\s\\S]*?)</a>"))
								||StringUtils.isNotBlank(Regex.regexReadOne(content, "来自<strong class=\"mohe-tips\">“\\s*?<img\\s*?src=\"([\\S]*?)\" class=\"mh-hy-img\"/>"))
								||StringUtils.isNotBlank(Regex.regexReadOne(content, "<strong class=\"mohe-tips mh-hy\">\\s*?<img\\s*?src=\"([\\S]*?)\""))){
							typeList.add("商户");
						}
						String flag = Regex.regexReadOne(content, "<span\\s*?class=\"mohe-ph-mark\"\\s*?style=\"background-color:#e76639\">([\\s\\S]*?)</span>");
						if(StringUtils.isNotBlank(flag)){
							typeList.add(flag.trim());
						}
						
					}
					if(typeList.size()==0){
						typeList.add("无");
//						continue;
					}
					bw.write(line+"\t"+StringUtils.join(typeList,"+"));
					typeList.clear();
					bw.newLine();
					bw.flush();
				}
			}
			httpclient.close();
			br.close();
			bw.flush();
			bw.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
