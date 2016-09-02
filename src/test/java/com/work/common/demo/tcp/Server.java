package com.work.common.demo.tcp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	public static void main(String[] args) {
		ServerSocket ss = null;
		PrintStream ps = null;
		ByteArrayOutputStream baos;
		InputStream in;
		try {
			ss = new ServerSocket(30000);
			while (true) {
				Socket s = ss.accept();
				ps = new PrintStream(s.getOutputStream());
				ps.println("您好，您收到了服务器的祝福");
				in = s.getInputStream();
				baos = new ByteArrayOutputStream();
				int i = -1;
				while ((i = in.read()) != -1) {
					baos.write(i);
				}
				System.out.println(baos.toString());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			ps.close();
		}
	}
}
