
import java.security.cert.CertificateException;
import java.util.Arrays;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


public class HttpsTest {
    public static void main(String[] args) {
        SSLContext sc;
        try {
            sc = SSLContext.getInstance("TLS");
            // 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法
            X509TrustManager trustManager = new X509TrustManager() {
                @Override
                public void checkClientTrusted(
                        java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
                        String paramString) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(
                        java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
                        String paramString) throws CertificateException {
                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };

            sc.init(null, new TrustManager[] { trustManager }, null);
            System.out.println("缺省安全套接字使用的协议: " + sc.getProtocol());  
            // 获取SSLContext实例相关的SSLEngine  
            SSLEngine en = sc.createSSLEngine();  
            System.out  
                    .println("支持的协议: " + Arrays.asList(en.getSupportedProtocols()));  
            System.out.println("启用的协议: " + Arrays.asList(en.getEnabledProtocols()));  
            System.out.println("支持的加密套件: "  
                    + Arrays.asList(en.getSupportedCipherSuites()));  
            System.out.println("启用的加密套件: "  
                    + Arrays.asList(en.getEnabledCipherSuites())); 
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}