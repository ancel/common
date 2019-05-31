import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;




public class HttpclientDemo {
	
	public static String reqByGet(String url) throws ClientProtocolException, IOException{
		HttpGet httpGet = new HttpGet(url);
		CloseableHttpClient httpclient = HttpClients.custom().build();
		try {
			HttpResponse response = httpclient.execute(httpGet);
			return EntityUtils.toString(response.getEntity());
		} finally{
			httpclient.close();
		}
	}
	
	public static void main(String[] args) {
		System.setProperty("https.protocols", "TLSv1.2,TLSv1.1,SSLv3");
		String url = "https://placesmap.net/IN/";
		try {
			System.out.println(reqByGet(url));
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
