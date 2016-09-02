package com.work.common.demo.tcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
	public static void main(String[] args) {
		Socket socket = null;
		BufferedReader br = null;
		try {
			socket = new Socket("127.0.0.1", 30000);
			socket.getOutputStream().write("哈哈".getBytes());
			socket.setSoTimeout(10000);
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String line = br.readLine();
			System.out.println("来自服务器的信息："+line);
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			
			try {
				br.close();
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
}
