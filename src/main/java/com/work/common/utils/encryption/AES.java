package com.work.common.utils.encryption;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;



/**
 * 
 * 参考链接：http://www.awaysoft.com/taor/aes-cbc-%E7%9B%B8%E4%BA%92%E5%8A%A0%E8%A7%A3%E5%AF%86-javaphpc.html
 * 
 * key的长度有限制，需要替换${java_home}\jre\lib\security下的local_policy.jar和US_export_policy.jar
 * JCE：Java Cryptography Extension（JCE）是一组包，它们提供用于加密、密钥生成和协商以及 Message Authentication Code（MAC）算法的框架和实现。
 * 		它提供对对称、不对称、块和流密码的加密支持，它还支持安全流和密封的对象。它不对外出口，用它开发完成封装后将无法调用。
 * JCE下载地址：http://www.oracle.com/technetwork/java/javase/downloads/index.html
 * 
 * @author：wanghaibo 
 * @creattime：2016年9月18日 下午2:57:36 
 * 
 */  
public class AES {
    
    /**
     * 加密
     * @param target 
     * @param key
     * @param ivStr
     * @return
     * @throws Exception
     */
    public static String Encrypt(String target, String key,String ivStr) throws Exception {  
        if (key == null) {  
            System.out.print("Key为空null");  
            return null;  
        }  
        byte[] raw = new BASE64Decoder().decodeBuffer(key);  
        SecretKeySpec keySpec = new SecretKeySpec(raw, "AES");  
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");//"算法/模式/补码方式"  
        IvParameterSpec iv = new IvParameterSpec(ivStr.getBytes());//使用CBC模式，需要一个向量iv，可增加加密算法的强度  
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);  
       
        byte[] srawt = target.getBytes();
        byte[] encrypted = cipher.doFinal(srawt);  
  
        return new BASE64Encoder().encode(encrypted);//此处使用BASE64做转码功能，同时能起到2次加密的作用。  
    }  
  
    /**
     * 解密
     * @param target
     * @param key
     * @param ivStr
     * @return
     * @throws Exception
     */
    public static String Decrypt(String target, String key,String ivStr) throws Exception {  
        // 判断Key是否正确  
        if (key == null) {  
            System.out.print("Key为空null");  
            return null;  
        }  
        byte[] raw = new BASE64Decoder().decodeBuffer(key);  
        SecretKeySpec keySpec = new SecretKeySpec(raw, "AES");  
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");  
        IvParameterSpec iv = new IvParameterSpec(ivStr.getBytes());  
        cipher.init(Cipher.DECRYPT_MODE, keySpec, iv);  
        byte[] encrypted1 = new BASE64Decoder().decodeBuffer(target);//先用base64解密  
        try {  
            byte[] original = cipher.doFinal(encrypted1);  
            String originalString = new String(original);  
            return originalString.trim();  
        } catch (Exception e) {  
            System.out.println(e.toString());  
            return null;  
        }  
    } 
    public static void main(String[] args) throws Exception {
    	 String data = "北京羽乐创新有限公司-电话邦";
         String key = "kq49MtFgDyNwuWtBxeyQqzfJhqOyW92k";
         String ivStr = "0102030405060708";
         System.out.println(new BASE64Decoder().decodeBuffer(key).length);
         String encryptStr = Encrypt(data, key ,ivStr);
         String decryptStr = Decrypt(encryptStr, key,ivStr);
         System.out.println(encryptStr);
         System.out.println(decryptStr);
	}
}
