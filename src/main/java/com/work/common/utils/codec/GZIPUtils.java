package com.work.common.utils.codec;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * gzip压缩和解压
 * 
 * @author：wanghaibo
 * @creattime：2016年11月21日 下午5:57:44
 * 
 */
public class GZIPUtils {

	public static byte[] compress(String target, String charset) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			GZIPOutputStream gzip;
			gzip = new GZIPOutputStream(out);
			gzip.write(target.getBytes(charset));
			gzip.close();
			return out.toByteArray();
		}finally{
			out.close();
		}
	}

	// 解压缩
	public static String uncompress(byte[] target, String charset) throws IOException{
		if (target == null || target.length == 0) {
			return null;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ByteArrayInputStream in = null;
		try {
			in = new ByteArrayInputStream(target);
			GZIPInputStream gunzip = new GZIPInputStream(in);
			byte[] buffer = new byte[256];
			int n;
			while ((n = gunzip.read(buffer)) >= 0) {
				out.write(buffer, 0, n);
			}
			return out.toString(charset);
		} finally {
			out.close();
			if(null!=in){
				in.close();
			}
		}
		
	}
}
