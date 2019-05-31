package com.work.common.test;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

public class FileTest {
	
	@Test
	public void delete(){
		String filename = "C:\\Users\\Li Yujie\\Desktop\\tt";
		try {
//			FileUtils.deleteDirectory(new File(filename));
			FileUtils.cleanDirectory(new File(filename));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
