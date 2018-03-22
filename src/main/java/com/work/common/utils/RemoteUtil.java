package com.work.common.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.MessageFormat;

/**
 * 远程操作工具类
 * @author：ancel.wang
 * @creattime：2018年3月21日 下午5:18:47 
 * 
 */  
public class RemoteUtil {
	
	public static final String RSYNC_COMMEND_TEMPLATE = "rsync -avzP {0} {1}@{2}:{3}";
	public static final String REMOTE_COMMEND_TEMPLATE = "ssh {0}@{1} {2}";
	public static final String DEFAULT_USERNAME = "root";
	
	
	/**
	 * @param commend
	 * @return 0表示成功，非0表示失败
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static int exec(String commend) throws IOException, InterruptedException{
		System.out.println(commend);
		Process pro = Runtime.getRuntime().exec(commend);
		InputStream in = pro.getInputStream();  
        BufferedReader read = new BufferedReader(new InputStreamReader(in));  
        while(read.readLine()!=null){  
        }  
        int result = pro.waitFor();
		return result;
	}
	
	public static int exec(String distIp, String distUser, String commend) throws IOException, InterruptedException{
		commend = MessageFormat.format(REMOTE_COMMEND_TEMPLATE, distUser, distIp, commend);
		return exec(commend);
	}
	
	public static int rsyncFile(String srcFile, String distIp, String distUser, String distFile) throws IOException, InterruptedException{
		String commend = MessageFormat.format(RSYNC_COMMEND_TEMPLATE, srcFile, distUser, distIp, distFile);
		return exec(commend);
	}
	
	public static int rsyncFile(String srcFile, String distIp, String distFile) throws IOException, InterruptedException{
		return rsyncFile(srcFile, distIp, DEFAULT_USERNAME, distFile);
	}
	
	public static int delFile(String distIp, String distUser, String targetFile) throws IOException, InterruptedException{
		return exec(distIp, distUser, "rm -rf "+targetFile);
	}
	public static int cleanDir(String distIp, String distUser, String targetDir) throws IOException, InterruptedException{
		String targetFile = new File(targetDir, "*").getAbsolutePath();
		return delFile(distIp, distUser, targetFile);
	}
	public static int cleanDir(String distIp, String targetDir) throws IOException, InterruptedException{
		return cleanDir(distIp, DEFAULT_USERNAME, targetDir);
	}
}
