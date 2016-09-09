package com.work.common.java.io;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import org.junit.Test;

public class RandomAccessFileTest {
	@SuppressWarnings("resource")
	@Test
	public void testSeek() {
		String filePath = "C:\\Users\\Li Yujie\\Desktop\\新建文本文档 (2).txt";
		File newFile = new File(filePath);
		try {
			RandomAccessFile raf = new RandomAccessFile(newFile, "rw");
			int pos = 0;
			String line;
			line = raf.readLine();
			pos += line.length();
			raf.seek(pos);
			while((line = raf.readLine())!=null){
				System.out.println(new String(line.getBytes("iso-8859-1"), "utf-8"));
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
