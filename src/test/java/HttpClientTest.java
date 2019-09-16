

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.junit.Before;
import org.junit.Test;

import com.work.common.utils.http.HttpClientUtil;
import com.work.common.utils.http.LocalHttpClients;
import com.work.common.utils.http.ResponseBall;

public class HttpClientTest {
	List<String> userAgents;
	@Before
	public void init() throws IOException{
		userAgents = getUserAgent();
	}
	
	@Test
	public void request() throws NoSuchAlgorithmException, IOException, KeyManagementException, KeyStoreException, CertificateException{
		System.setProperty("https.protocols", "TLSv1.2");
//		System.setProperty("jsse.enableSNIExtension", "false");
//		HttpHost httpHost = new HttpHost("172.18.19.254", 8080);
		HttpHost httpHost = new HttpHost("proxy.dianhua.cn", 8080);
//		HttpHost httpHost = new HttpHost("proxy.dianhua.cn", 8081);
//		HttpHost httpHost = new HttpHost("192.168.20.177", 8080);
//		HttpHost httpHost = new HttpHost("proxy-tw.dianhua.cn", 8080);
//		HttpHost httpHost = new HttpHost("123.207.245.211", 3128);
//		HttpHost httpHost = new HttpHost("222.221.147.53", 15180);
//		MayiProxy mayi = new MayiProxy();
//		HttpHost httpHost = new HttpHost(mayi.getHostname(), mayi.getPort());
//		HttpHost httpHost = null;
		Header[] headers = getHeaders();
		headers = null;
		String  url;
//		url = "https://www.biz72.com/b2b/tags/pqsj_p-10.html";
		url = "https://www.56135.com/gongsilist/%bd%ad%ce%f7/P00001.html";
//		HttpClientManager.getInstance().addKeyStore("file/placesmap.keystore", "11111111", KeyStore.getDefaultType());
		CloseableHttpClient httpClient = new LocalHttpClients().create();
		ResponseBall ball = HttpClientUtil.reqByGet(httpClient, url,  "gb2312", httpHost, CookieSpecs.DEFAULT, headers);
		System.out.println(ball);
		httpClient.close();
		
		
	}
	
	@Test
	public void testNormalRequest() throws ClientProtocolException, IOException{
		String url = "https://www.56135.com/gongsilist/%bd%ad%ce%f7/P00001.html";
		HttpHost httpHost = new HttpHost("proxy.dianhua.cn", 8080);
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		HttpGet httpGet = new HttpGet(url);
		RequestConfig requestConfig = RequestConfig.custom().setNormalizeUri(true).setProxy(null).build();
		httpGet.setConfig(requestConfig);
		CloseableHttpResponse response = httpClient.execute(httpGet);
		System.out.println(EntityUtils.toString(response.getEntity()));
	}
	
	public Header[] getHeaders() throws NoSuchAlgorithmException, IOException{
		List<BasicHeader> headerList = new ArrayList<BasicHeader>();
		headerList.add(new BasicHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3"));
		headerList.add(new BasicHeader("Accept-Encoding", "gzip, deflate, br"));
		headerList.add(new BasicHeader("Accept-Language", "zh-CN,zh;q=0.9"));
		headerList.add(new BasicHeader("cache-control", "max-age=0"));
		headerList.add(new BasicHeader("Connection","keep-alive"));
//		headerList.add(new BasicHeader("Host","placesmap.net"));
//		headerList.add(new BasicHeader("Cookie","BYUSR=0ddbc221c2e15a8001c06a85bce945fa"));
		headerList.add(new BasicHeader("Upgrade-Insecure-Requests", "1"));
//		headerList.add(new BasicHeader("User-Agent", userAgents.get(new Random().nextInt(userAgents.size()))));
		headerList.add(new BasicHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.103 Safari/537.36"));
//		headerList.add(new BasicHeader("User-Agent", "Dalvik/2.1.0 (Linux; U; Android 5.0.2; vivo Y51A Build/LRX22G)"));
//		MayiProxy mayi = new MayiProxy();
//		Map<String, String> headerMap = mayi.getHeaders();
//		for (Entry<String, String> entry: headerMap.entrySet()) {
//			headerList.add(new BasicHeader(entry.getKey(), entry.getValue()));
//		}
		Header[] headers = headerList.toArray(new BasicHeader[headerList.size()]);
		return headers;
	}
	
	public List<String> getUserAgent() throws IOException{
		String filename = "file/user-agent";
		BufferedReader br = new BufferedReader(new FileReader(filename));
		String line = null;
		List<String> userAgents = new ArrayList<String>();
		while((line=br.readLine())!=null){
			if(StringUtils.isNotBlank(line)){
				userAgents.add(line.trim());
			}
		}
		br.close();
		return userAgents;
	}
	
}
