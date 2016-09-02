package com.work.common.demo.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * 创建人：wanghaibo 
 * 创建时间：2015-10-14 下午3:16:37 
 * 功能描述： 
 * 服务器，监听socket
 */  

public class Server implements Runnable{
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private int port;
	private SocketProcessor socketProcessor;
	
	public Server(int port, SocketProcessor socketProcessor) {
		this.port = port;
		this.socketProcessor = socketProcessor;
	}

	@Override
	public void run() {
		// 定义一个ServerSocket监听在某端口上
		try {
			ServerSocket serverSocket = null;
			serverSocket = new ServerSocket(port);
			logger.info("启动监听,端口号"+port);
			Socket socket = null;
			boolean flag = true;
			while(flag){
				// server尝试接收其他Socket的连接请求，server的accept方法是阻塞式的
				socket = serverSocket.accept();
				// 跟客户端建立好连接之后，我们就可以获取socket的InputStream，并从中读取客户端发过来的信息了。
				socketProcessor.process(socket);
			}
			serverSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.info("监听异常,端口号"+port,e);
		}finally{
			logger.info("关闭监听,端口号"+port);
		}
	}
	
	public static void main(String[] args) {
		int port = 30000;
		SocketProcessor socketProcessor = new NameFormat();
		new Thread(new Server(port, socketProcessor)).start();
	}

}
