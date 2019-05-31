package com.work.common.java.encrypt;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
/**
 *
 * @author Administrator
 *
 */
public class AES {

    // 加密
    public static byte[] Encrypt(byte[] data, byte[] key) throws Exception {
    	SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
    	Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");//"算法/模式/补码方式"
    	cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
    	return cipher.doFinal(data);
    }

    // 解密
    public static byte[] Decrypt(byte[] encrypted1, byte[] key) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        return cipher.doFinal(encrypted1);
    }
    
    public static byte[] uncompress(byte[] bytes) throws IOException {  
        if (bytes == null || bytes.length == 0) {  
            return null;  
        }  
        ByteArrayOutputStream out = new ByteArrayOutputStream();  
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);  
        GZIPInputStream ungzip = new GZIPInputStream(in);  
        byte[] buffer = new byte[256];  
        int n;  
        while ((n = ungzip.read(buffer)) >= 0) {  
            out.write(buffer, 0, n);  
        }  
  
        return out.toByteArray();  
    }  

    public static void main(String[] args) throws Exception {

        String enString = "zGKtL83kEkWClZSaSNLmAVV2lfnWVFjT4tfm0NO1UHSwTVSSuJ8swN+oZNyqQlr5QhyI2zg+pIQR3Spyv1Qj2A==";
        byte[] appKeyBytes = new Base64().decode("UHs70NPKpZVkTuygrtg/fA==");
//        System.out.println(appKeyBytes.length);
//        System.out.println(new String(appKeyBytes));
//        byte[] encrypted1 = new Base64().decode(enString);//先用base64解密
//        System.out.println(new String(encrypted1));
//        System.out.println(Decrypt(encrypted1, appKeyBytes));
        
        String target = "hahahah";
        System.out.println(new String(Encrypt(target.getBytes(), appKeyBytes)));
        System.out.println(new String(Base64.encodeBase64(Encrypt(target.getBytes(), appKeyBytes))));
        System.out.println(new String(Decrypt(Encrypt(target.getBytes(), appKeyBytes), appKeyBytes)));
        System.out.println(new String(Decrypt(Base64.decodeBase64(enString), appKeyBytes)));
        byte[] a = Decrypt(Base64.decodeBase64(enString), appKeyBytes);
        System.out.println(new String(uncompress(a)));
        System.out.println(Base64.encodeBase64String("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.106 Safari/537.36".getBytes()));
    }
}

//源代码片段来自云代码http://yuncode.net