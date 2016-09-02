package com.work.common.demo.socket;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * 创建人：wanghaibo 
 * 创建时间：2015-10-14 下午3:17:31 
 * 功能描述： 
 * 默认的socket处理器，将接收到的内容处理之后写回客户端
 */  

public abstract class DefaultSocketProcessor implements SocketProcessor {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public Boolean process(Socket socket) {
		Reader reader = null;
		Writer writer = null;
		// 跟客户端建立好连接之后，我们就可以获取socket的InputStream，并从中读取客户端发过来的信息了。
		try {
			reader = new InputStreamReader(socket.getInputStream());
			StringBuilder sb = new StringBuilder();
			while (true) {  
			    int c=reader.read();  
			    if (c=='\r'||c=='\n'||c==-1) {//\r是回车,\n是换行,-1是结尾
			        break;  
			    }  
			    sb.append((char)c);  
			      
			}  
			
			logger.info("client message: " + sb);
			writer = new OutputStreamWriter(socket.getOutputStream());
			writer.write(handleReq(sb.toString()));
			writer.close();
			reader.close();
			
		} catch (IOException e) {
			logger.error("", e);
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}
	
	protected abstract String handleReq(String reqStr);
}
