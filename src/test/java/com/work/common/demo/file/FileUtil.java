package com.work.common.demo.file;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileUtil {
	
	public static void scan(String filePath,FileListener listener) throws IOException{
		FileInputStream inputStream = null;
		Scanner sc = null;
		List<String> lineList = new ArrayList<>();
		try {
		    inputStream = new FileInputStream(filePath);
		    sc = new Scanner(inputStream, "UTF-8");
		    String line;
		    while (sc.hasNextLine()) {
		        line = sc.nextLine();
		        // System.out.println(line);
		        lineList.add(line);
		        listener.handleFile(new FileEvent(lineList));
		    }
		    // note that Scanner suppresses exceptions
		    if (sc.ioException() != null) {
		        throw sc.ioException();
		    }
		} finally {
		    if (inputStream != null) {
		        inputStream.close();
		    }
		    if (sc != null) {
		        sc.close();
		    }
		}
	}
	
	public static void main(String[] args) {
		
	}
}
