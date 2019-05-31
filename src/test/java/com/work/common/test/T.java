package com.work.common.test;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import com.work.common.utils.encryption.EncryptionUtil;

public class T {
	public static String getSha1(String str){
        if(str==null||str.length()==0){
            return null;
        }
        char hexDigits[] = {'0','1','2','3','4','5','6','7','8','9',
                'a','b','c','d','e','f'};
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
            mdTemp.update(str.getBytes("UTF-8"));

            byte[] md = mdTemp.digest();
            int j = md.length;
            char buf[] = new char[j*2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
                buf[k++] = hexDigits[byte0 & 0xf];      
            }
            return new String(buf);
        } catch (Exception e) {
            // TODO: handle exception
            return null;
        }
    }
	private static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7',
		'8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	
	public static void main(String[] args) {
		
		try {
			System.out.println(EncryptionUtil.md5("13000000007"));
			System.out.println(System.currentTimeMillis());
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(getSha1("123"));
		
		// 分配的appid
		String appid = "Kakneg3mgWAg075d";
		// 分配的密钥
		String secret = "cHHpXukItHR3NLX0";
		// 拼装消息请求header,使用TreeMap保证key的字典序
		Map<String, String> header = new TreeMap<String, String>();
		header.put("appid", appid);
//		header.put("timestamp", new Date().getTime() + "");
		header.put("timestamp", "1542075814846");
		header.put("v", "1.0");
		header.put("nonce", "8471f652234d6af392276a0b8c3d7f3d"); // 自定义方法生成随机不重复字符串，此处省略生成方法
		// 拼接生成签名的字符串
		StringBuilder msg = new StringBuilder();
		for (Map.Entry<String, String> entry : header.entrySet()) {
		msg.append(entry.getValue());
		System.out.println(entry.getKey());
		}
		msg.append(secret);
		// 生成签名
		try {
			header.put("sign", md5(msg.toString()));
			System.out.println(header.toString());
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	public static String md5(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		if (password == null) {
			return null;
		}

		byte[] temp = password.getBytes("UTF-8");
		MessageDigest digest = MessageDigest.getInstance("MD5");
		digest.update(temp);

		byte[] md = digest.digest();
		int length = md.length;
		char buffer[] = new char[length * 2];
		int k = 0;
		for (int i = 0; i < length; i++) {
			byte byte0 = md[i];
			buffer[k++] = hexDigits[byte0 >>> 4 & 0xf];
			buffer[k++] = hexDigits[byte0 & 0xf];
		}
		return new String(buffer);

	}
}
