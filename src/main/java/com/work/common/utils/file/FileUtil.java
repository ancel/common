package com.work.common.utils.file;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.work.common.constant.FileConstant;

/**
 * 文件工具类
 * 
 * @author admin
 * 
 */
public class FileUtil {
	public static final Logger LOGGER = LoggerFactory.getLogger(FileUtil.class);
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
	 * 记录数据
	 * @param fileName
	 * @param data
	 */
	public static void write(String fileName,String data){
		FileRecordHelper.write(fileName, data);
	}
	
	/**
	 * 记录数据 ,通过FileOutputStream
	 * @param fileName
	 * @param data
	 * @param charset
	 * @param flag true表示换行，否则不换行
	 */
	public static void write(String fileName,String data,String charset,boolean flag){
		File file = new File(fileName);
		try {
			FileUtils.writeStringToFile(file, data,Charset.forName(charset),true);
			if(flag){
				FileUtils.writeStringToFile(file, FileConstant.LINE_SEPARATOR,Charset.forName(charset),true);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 记录数据
	 * @param fileName
	 * @param data
	 * @param charset
	 * @param flag true表示换行，否则不换行
	 */
	public static void writeByBuffer(String fileName,String data,String charset,boolean flag){
		File file = new File(fileName);
		BufferedOutputStream out = null;
		try {
			out = new BufferedOutputStream(new FileOutputStream(file));
			out.write(data.getBytes(charset));
			if(flag){
				out.write(FileConstant.LINE_SEPARATOR_BYTES);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static String readFile(String fileName){
		BufferedReader br;
		StringBuffer sb = new StringBuffer();
		String line;
		try {
			br = new BufferedReader(new FileReader(fileName));
			while((line=br.readLine())!=null){
				sb.append(line);
			}
			br.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sb.toString();
	}
	
	
    /**
     * 删除目标文件
     * @param target
     * @return
     * @throws IOException 
     */
    public static boolean delete(File target) throws IOException  {
    	if(null==target){
    		return true;
    	}
    	if(!target.exists()){
    		return true;
    	}
        if (target.isDirectory()) {
        	for (File file : target.listFiles()) {
				delete(file);
			}
        }
        boolean result = target.delete();
        if(result){
        	LOGGER.info("已删除---"+target.getPath());
        }else{
        	throw new IOException("删除失败---"+target.getPath());
        }
        return result;
    }
	
}
