package com.work.common.demo.http.async;

import java.io.IOException;
import java.nio.CharBuffer;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.nio.IOControl;
import org.apache.http.nio.client.methods.AsyncCharConsumer;
import org.apache.http.nio.client.methods.HttpAsyncMethods;
import org.apache.http.nio.protocol.HttpAsyncRequestProducer;
import org.apache.http.nio.reactor.IOReactorException;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import com.work.common.utils.http.LocalHttpAsyncClients;

public class HttpAsyncClientDemo {
	public static void main(String[] args) throws InterruptedException, ExecutionException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException, IOReactorException {
		CloseableHttpAsyncClient httpclient = new LocalHttpAsyncClients().create();
//		CloseableHttpAsyncClient httpclient = HttpAsyncClients.createDefault();
		try {
		    // Start the client
		    httpclient.start();

		    // Execute request
		    final HttpGet request1 = new HttpGet("http://www.apache.org/");
		    Future<HttpResponse> future = httpclient.execute(request1, null);
		    // and wait until a response is received
		    HttpResponse response1 = future.get();
		    System.out.println(request1.getRequestLine() + "->" + response1.getStatusLine());

		    // One most likely would want to use a callback for operation result
		    final CountDownLatch latch1 = new CountDownLatch(1);
		    final HttpGet request2 = new HttpGet("http://www.apache.org/");
		    httpclient.execute(request2, new FutureCallback<HttpResponse>() {

		        public void completed(final HttpResponse response2) {
		            latch1.countDown();
		            System.out.println(request2.getRequestLine() + "->" + response2.getStatusLine());
		            try {
						System.out.println(EntityUtils.toString(response2.getEntity()));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		        }

		        public void failed(final Exception ex) {
		            latch1.countDown();
		            System.out.println(request2.getRequestLine() + "->" + ex);
		        }

		        public void cancelled() {
		            latch1.countDown();
		            System.out.println(request2.getRequestLine() + " cancelled");
		        }

		    });
		    latch1.await();

		    // In real world one most likely would also want to stream
		    // request and response body content
		    final CountDownLatch latch2 = new CountDownLatch(1);
		    final HttpGet request3 = new HttpGet("http://www.apache.org/");
		    HttpAsyncRequestProducer producer3 = HttpAsyncMethods.create(request3);
		    AsyncCharConsumer<HttpResponse> consumer3 = new AsyncCharConsumer<HttpResponse>() {

		        HttpResponse response;
		        

		        @Override
		        protected void onResponseReceived(final HttpResponse response){
		        	System.out.println("onResponseReceived");
		            this.response = response;
		            
		        }

		        @Override
		        protected void onCharReceived(final CharBuffer buf, final IOControl ioctrl) throws IOException {
		            // Do something useful
		        	System.out.println("onCharReceived----"+buf.length());
//		        	while (buf.hasRemaining()) {
//		        		System.out.print(buf.get());
//		            }
		        }

		        @Override
		        protected void releaseResources() {
		        	System.out.println("releaseResources");
		        }

		        @Override
		        protected HttpResponse buildResult(final HttpContext context) {
		        	System.out.println("buildResult");
		            return this.response;
		        }

		    };
		    future = httpclient.execute(producer3, consumer3, new FutureCallback<HttpResponse>() {

		        public void completed(final HttpResponse response3) {
		        	System.out.println("completed");
		        	for (Header header : response3.getAllHeaders()) {
						System.out.println(header.getName()+"="+header.getValue());
					}
		        	System.out.println(response3.getEntity());
		            latch2.countDown();
		            System.out.println(request3.getRequestLine() + "->" + response3.getStatusLine());
		        }

		        public void failed(final Exception ex) {
		        	System.out.println("failed");
		            latch2.countDown();
		            System.out.println(request3.getRequestLine() + "->" + ex);
		        }

		        public void cancelled() {
		        	System.out.println("cancelled");
		            latch2.countDown();
		            System.out.println(request3.getRequestLine() + " cancelled");
		        }

		    });
		    latch2.await();

		} finally {
			if(null!=httpclient){
				try {
					httpclient.close();
				} catch (IOException e) {
				}
			}
		}
	}
}
