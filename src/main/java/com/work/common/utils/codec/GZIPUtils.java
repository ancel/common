package com.work.common.utils.codec;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.codec.binary.Base64;


public class GZIPUtils {
	
	public static byte[] encode(String target,String charset) throws IOException{
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		GZIPOutputStream gzip;
		gzip = new GZIPOutputStream(out);
		gzip.write(target.getBytes(charset));
		gzip.close();
		return out.toByteArray();
	}
	
	public static void main(String[] args) {
		String str = "{\"flag\":[\"疑似诈骗\"],\"ids\":[\"5\"],\"tels\":[\"02031004613\"],\"time\":[\"1476412149140\"],\"uip\":[\"106.37.212.73\"],\"userid\":[\"8787686555\"]}";
		try {
			System.out.println(new String(Base64.encodeBase64(encode(str, "utf-8"))));
			System.out.println(Base64.encodeBase64String(encode(str, "utf-8")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
