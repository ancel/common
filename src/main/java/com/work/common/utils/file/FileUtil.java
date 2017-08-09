package com.work.common.utils.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.OutputStreamWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 文件工具类
 * @author：ancel.wang
 * @creattime：2017年7月12日 下午2:49:50 
 * 
 */  
public class FileUtil {
	public static final Logger LOGGER = LoggerFactory.getLogger(FileUtil.class);
	
	//换行符
	public static final String LINE_SEPARATOR = System.getProperty("line.separator");
	//文件读写所映射
	public static final Map<String, ReadWriteLock> FILE_LOCK_MAP = new HashMap<>();
	
	/**
	 * 获取文件行数
	 * 
	 * @param fileName
	 * @return
	 * @throws IOException 
	 */
	public static int getLineNum(String filename) throws IOException {
		File target = new File(filename);
		long fileLength = target.length();
		LineNumberReader reader = null;
		int lineNumber = 0;
		reader = new LineNumberReader(new FileReader(target));
		reader.skip(fileLength);
		lineNumber = reader.getLineNumber();
		reader.close();
		return lineNumber;
	}

	/**
	 * 新建文件
	 * @param file
	 */
	public static void createFile(File target) throws IOException{
		File parentFile = target.getParentFile();
		if(!parentFile.exists()){
			parentFile.mkdirs();
		}
		target.createNewFile();
	}
	
	
	/**
	 * 获取root下所有文件
	 * @param root
	 * @param fileList
	 * @param fileFilter
	 */
	public static void getFiles(File target,List<File> fileList,FileFilter fileFilter){
		if(null!=target&&target.exists()){
			if(target.isFile()){
				if(null==fileFilter||fileFilter.accept(target)){
					fileList.add(target);
				}
			}else{
				for (File file : target.listFiles()) {
					getFiles(file, fileList,fileFilter);
				}
			}
		}
	}
	
	/**
	 * 获取文件内容
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public static String readFile(String fileName) throws IOException{
		BufferedReader br;
		StringBuffer sb = new StringBuffer();
		String line;
		br = new BufferedReader(new FileReader(fileName));
		while((line=br.readLine())!=null){
			sb.append(line);
		}
		br.close();
		return sb.toString();
	}
	
	
    /**
     * 删除目标文件
     * @param target
     * @return
     * @throws IOException 
     */
    public static void delete(File target) throws IOException  {
    	if(null==target){
    		return;
    	}
    	if(!target.exists()){
    		return;
    	}
        if (target.isDirectory()) {
        	for (File file : target.listFiles()) {
				delete(file);
			}
        }
    	target.delete();
    }
    
    /**
	 * 获取相对路径
	 * @param parentPath
	 * @param path
	 * @return 如果存在相对路径则返回,否则返回null
	 */
	public static String getRelativePath(String parentPath,String path){
		if(StringUtils.isNotBlank(parentPath)&&StringUtils.isNotBlank(path)&&path.startsWith(parentPath)){
			String relativePath = path.substring(parentPath.length());
			if(relativePath.startsWith(File.separator)){
				relativePath = relativePath.substring(1);
			}
			return relativePath;
		}
		return null;
	}
	
	
	/**
	 * 获取相对路径
	 * @param parentFile
	 * @param file
	 * @return
	 */
	public static String getRelativePath(File parentFile,File file){
		if(parentFile==null||file==null){
			return null;
		}
		return getRelativePath(parentFile.getPath(),file.getPath());
	}
	
	/**
	 * 集合写入文件
	 * @param filePath
	 * @param charset
	 * @param data
	 * @param append
	 * @throws IOException
	 */
	public static void write(String filePath,Collection<String> data,String charset,boolean append,boolean newLine) throws IOException{
		if(data==null||data.size()<1){
			return;
		}
		
		synchronized (FILE_LOCK_MAP) {
			if(!FILE_LOCK_MAP.containsKey(filePath)){
				FILE_LOCK_MAP.put(filePath, new ReentrantReadWriteLock());
			}
		}
		FILE_LOCK_MAP.get(filePath).writeLock().lock();
		createFile(new File(filePath));
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath,append), charset));
			for (String string : data) {
				writer.write(string);
				if(newLine){
					writer.newLine();
				}
			}
		} finally{
			if(writer!=null){
				try {
					writer.flush();
					writer.close();
				} catch (Exception e) {
					LOGGER.error("[write flush or close failure,filepath="+filePath+"]");
				}
			}
			FILE_LOCK_MAP.get(filePath).writeLock().unlock();
		}
		
	}
	
	/**
	 * 写入文件
	 * @param filePath
	 * @param charset
	 * @param line
	 * @param append
	 * @throws IOException
	 */
	public static void write(String filePath,String data,String charset,boolean append) throws IOException{
		if(data==null){
			return;
		}
		
		synchronized (FILE_LOCK_MAP) {
			if(!FILE_LOCK_MAP.containsKey(filePath)){
				FILE_LOCK_MAP.put(filePath, new ReentrantReadWriteLock());
			}
		}
		FILE_LOCK_MAP.get(filePath).writeLock().lock();
		createFile(new File(filePath));
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath,append), charset));
			writer.write(data);
		} finally{
			if(writer!=null){
				try {
					writer.flush();
					writer.close();
				} catch (Exception e) {
					LOGGER.error("[write flush or close failure,filepath="+filePath+"]");
				}
			}
			FILE_LOCK_MAP.get(filePath).writeLock().unlock();
		}
		
	}
	
}
