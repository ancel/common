package com.work.common.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class Base64Util {

	/**
	 * 
	 * @param fileName 编码文件
	 * @return
	 */
	public static String base64Encoder(String fileName) {
		InputStream input = null;
		ByteArrayOutputStream out = null;
		try {
			BASE64Encoder encoder = new BASE64Encoder();
			StringBuilder pictureBuffer = new StringBuilder();
			input = new FileInputStream(new File(fileName));
			out = new ByteArrayOutputStream();
			byte[] temp = new byte[1024];
			for (int len = input.read(temp); len != -1; len = input.read(temp)) {
				out.write(temp, 0, len);
				pictureBuffer.append(encoder.encode(out.toByteArray()));
				out.reset();
			}

			String s = "asga";
			s.getBytes();

			return pictureBuffer.toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				input.close();
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return "";
	}

	/**
	 * 解码字符串 
	 * @param fileName  解码之后输出文件
	 * @param str 解码字符串
	 */
	public static void base64Decode(String fileName, String str) {
		BASE64Decoder decoder = new BASE64Decoder();
		FileOutputStream write = null;
		try {
			write = new FileOutputStream(new File(fileName));
			System.out.println(str);
			byte[] decoderBytes = decoder.decodeBuffer(str);
			write.write(decoderBytes);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				write.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		String fileName = "d:\\1.png";
		String fileName2 = "d:\\2.png";
		String s = base64Encoder(fileName);
		System.out.println(s);
		base64Decode(fileName2, s);
	}
}
