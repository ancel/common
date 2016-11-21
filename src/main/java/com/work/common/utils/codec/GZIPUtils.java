package com.work.common.utils.codec;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

import net.sf.json.JSONObject;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringEscapeUtils;


/**
 * zip文件编码解码
 * @author：wanghaibo 
 * @creattime：2016年11月21日 下午5:57:44 
 * 
 */  
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
			str = "{\"flag\":[\"\u4f60\u597d\"]}";
			System.out.println(str);
			
			JSONObject json = new JSONObject();
			json.put("key", StringEscapeUtils.escapeJava("你好"));
			System.out.println(json);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
