package com.work.common.demo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import net.spy.memcached.MemcachedClient;

public class LocalMemcachedClient {
	
	private static final String hostname = "172.18.19.82";
	private static final int port = 11211; 
	
	public static MemcachedClient getMemcachedClient() throws IOException, InterruptedException, TimeoutException, ExecutionException{
		MemcachedClient memcachedClient = new MemcachedClient(new InetSocketAddress(hostname, port));
		return memcachedClient;
	}
	
	public static boolean checkMemcached(MemcachedClient memcachedClient,long duration, TimeUnit units ){
		int memcachedAliveSign = 1;
		memcachedClient.set("memcached_alive_sign", 0, memcachedAliveSign);
		try {
			memcachedClient.asyncGet("memcached_alive_sign").get(duration, units);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
}
