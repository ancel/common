package com.work.common.demo.socket;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.Socket;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Client {
	protected Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	public static final String LINE_SEPARTOR = System.getProperties()
			.getProperty("line.separator");

	/**
	 * 
	 * @param host
	 * @param port
	 * @param content
	 * @return
	 * @throws IOException
	 */
	public static String request(String host, int port, String reqContent)
			throws IOException {
		// 与服务端建立连接
		Socket client = new Socket(host, port);
		String response = "";
		// 建立连接后就可以往服务端写数据了
		Writer writer = new OutputStreamWriter(client.getOutputStream());
		if (StringUtils.isNotBlank(reqContent)) {
			writer.write(reqContent);
			writer.write(LINE_SEPARTOR);
			writer.flush();
			// 写完以后进行读操作
			Reader reader = new InputStreamReader(client.getInputStream());
			StringBuilder sb = new StringBuilder();
			while (true) {
				int c = reader.read();
				if (c == -1) {// \r是回车,\n是换行
					break;
				}
				sb.append((char) c);

			}
			reader.close();
			writer.close();
			client.close();
			response = sb.toString();
		}
		return response;
	}
	
	public static void main(String[] args) {
		String host = "127.0.0.1";
		int port = 30000;
		String reqContent = "空间和刻录机和";
		try {
			System.out.println(Client.request(host, port, reqContent));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
