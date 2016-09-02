package com.work.common.test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(LogTest.class);
	public static void main(String[] args) {
		try {
			new LogTest().readFile();
		} catch (IOException e) {
			LOGGER.error("main error",e);
		}
	}
	
	public void readFile() throws IOException{
		String fileName = "";
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(fileName));
		} catch (FileNotFoundException e) {
			throw new FileNotFoundException("文件没找到");
		}
		String line;
		try {
			while((line=br.readLine())!=null){
				System.out.println(line);
			}
		} catch (IOException e) {
			throw new IOException("文件读取失败",e);
		}
	}
}
