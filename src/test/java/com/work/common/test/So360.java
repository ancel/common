package com.work.common.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.work.common.utils.Regex;
import com.work.common.utils.http.HttpsClientUtil;

public class So360 {
	public static void main(String[] args) {
		String keyStorePath = "C:\\Users\\Li Yujie\\Desktop\\so.keystore";
		String keyStorePassword = "111111";
		SSLConnectionSocketFactory sslsf = null;
		try {
			sslsf = HttpsClientUtil.getSSLConnectionSocketFactory(keyStorePath, keyStorePassword);
		} catch (Exception e1) {
			e1.printStackTrace();
			System.exit(1);
		}
		CloseableHttpClient httpclient = HttpClients.custom()
				.setSSLSocketFactory(sslsf).build();
		String urlPath = "C:\\Users\\Li Yujie\\Desktop\\360.txt";
		String outPath = "C:\\Users\\Li Yujie\\Desktop\\360_so.txt";
		BufferedReader br;
		BufferedWriter bw;
		try {
			br = new BufferedReader(new FileReader(urlPath));
			bw = new BufferedWriter(new FileWriter(outPath));
			String line;
			String content = null;
			String name;
			int i=0;
			HttpHost host = new HttpHost("172.18.19.254", 8080);
			while((line=br.readLine())!=null){
				if(StringUtils.isNotBlank(line)){
					while(true){
						System.out.println((++i)+"\t"+line);
						try {
							content = HttpsClientUtil.getResponseByGet(httpclient,host, line.trim());
						} catch (Exception e) {
							e.printStackTrace();
							continue;
						}
						if(null==content||(StringUtils.isNotBlank(content)&&content.contains("系统检测到您操作过于频繁"))){
							System.out.println(content);
							httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
							Thread.sleep(5000);
						}
						if(StringUtils.isBlank(content)||!content.contains("系统检测到您操作过于频繁")){
							break;
						}
					}
					
					System.out.println("start regex");
					name = "";
					if(StringUtils.isNotBlank(content)){
						name = Regex.regexReadOne(content, "来自<strong class=\"mohe-tips\">“\\s*?<img\\s*?src=\"([\\S]*?)\" class=\"mh-hy-img\"/>"); 
						if(StringUtils.isBlank(name)){
							name = Regex.regexReadOne(content, "<strong class=\"mohe-tips mh-hy\">\\s*?<img\\s*?src=\"([\\S]*?)\"");
						}
					}
					if(StringUtils.isBlank(name)){
						continue;
					}
//					System.out.println(line+"\t"+name);
					bw.write(line+"\t"+name);
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
