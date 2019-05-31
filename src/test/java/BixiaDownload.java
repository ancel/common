import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.impl.client.CloseableHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.work.common.utils.Regex;
import com.work.common.utils.URLUtil;
import com.work.common.utils.http.HttpClientManager;
import com.work.common.utils.http.HttpClientUtil;
import com.work.common.utils.http.ResponseBall;


public class BixiaDownload {
	public static ResponseBall reqByGet(String url) throws ClientProtocolException, IOException{
		HttpHost httpHost = new HttpHost("proxy.dianhua.cn", 8080);
		CloseableHttpClient httpClient = HttpClientManager.getHttpClient();
		int reqNum = 0;
		while(reqNum<20){
			try {
				ResponseBall ball = HttpClientUtil.reqByGet(httpClient, url,  "utf-8", httpHost, CookieSpecs.DEFAULT, null);
				if(ball.getStatusCode()==200&&StringUtils.isNotBlank(ball.getContent())){
					return ball;
				}
			} catch (Exception e) {
			}
			reqNum++;
		}
		return null;
	}
	public static void main(String[] args) throws IOException {
		String xiaoshuoHomeUrl = "https://www.bxwx.la/b/33/33215/";
		String xiaoshuoFilePath = "file/小说_3.txt";
		HttpHost httpHost = new HttpHost("proxy.dianhua.cn", 8080);
		CloseableHttpClient httpClient = HttpClientManager.getHttpClient();
		ResponseBall ball = HttpClientUtil.reqByGet(httpClient, xiaoshuoHomeUrl,  "utf-8", httpHost, CookieSpecs.DEFAULT, null);
		Document doc = Jsoup.parse(ball.getContent());
		Elements es = doc.select("#list dl");
		String xiaoshuoContent = "";
		BufferedWriter bw = new BufferedWriter(new FileWriter(xiaoshuoFilePath));
		int zhangjieCount = 0;
		for (Element element : es) {
			Elements children = element.children();
			for (Element element2 : children) {
				String content = "";
				if("dt".equalsIgnoreCase(element2.tagName())){
					content = element2.text()+"r\n";
				}else if("dd".equalsIgnoreCase(element2.tagName())){
					Element zhangjieElement = element2.select("a").first();
					content += zhangjieElement.text()+"\r\n";
					zhangjieCount++;
					if(zhangjieCount<864){
						continue;
					}
					System.out.println(zhangjieElement.text());
					String url = zhangjieElement.attr("href");
					url = URLUtil.relToAbs(xiaoshuoHomeUrl, url);
					System.out.println(url);
					ResponseBall zhangjieball = reqByGet(url);
					if(zhangjieball==null){
						continue;
					}
//					System.out.println(zhangjieball.getContent());
					String zhangjieContent = Regex.regexReadOne(zhangjieball.getContent(), "[\\w\\W]*<div\\s*id=\"content\">([\\w\\W]*)<script>chaptererror[\\w\\W]*", 1);
//					System.out.println(zhangjieContent);
//					content += Jsoup.parse(zhangjieContent).text()+"\r\n";
					content += zhangjieContent.replace("<br />", "\r\n").replace("&nbsp;", " ")+"\r\n";
//					System.out.println(content);
//					xiaoshuoContent += content;
//					break;
					
				}
				bw.write(content);
				bw.newLine();
				bw.flush();
			}
		}
		System.out.println(xiaoshuoContent);
		
		bw.close();
		
	}
}
