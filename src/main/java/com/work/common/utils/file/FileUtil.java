package com.work.common.utils.file;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.work.common.constant.CommonConstant;

/**
 * 文件工具类
 * 
 * @author admin
 * 
 */
public class FileUtil {
	private List<File> fileList = new ArrayList<File>();

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

	public List<File> getFileList() {
		return fileList;
	}

	public void setFileList(List<File> fileList) {
		this.fileList = fileList;
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
	 * 通过递归得到某一路径下所有的目录及其文件
	 */
	public void getDirsAndFiles(String filePath) {
		File root = new File(filePath);
		File[] files = root.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				fileList.add(file);
				getDirsAndFiles(file.getAbsolutePath());
				System.out.println("显示" + filePath + "下所有子目录及其文件"
						+ file.getAbsolutePath());
			} else {
				fileList.add(file);
				System.out.println("显示" + filePath + "下所有子目录"
						+ file.getAbsolutePath());
			}
		}
	}
	
	/**
	 * 通过递归得到某一路径下所有的文件
	 */
	public void getFiles(String filePath) {
		File root = new File(filePath);
		File[] files = root.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				getFiles(file.getAbsolutePath());
				System.out.println("显示" + filePath + "下所有子目录及其文件"
						+ file.getAbsolutePath());
			} else {
				fileList.add(file);
				System.out.println("显示" + filePath + "下所有子目录"
						+ file.getAbsolutePath());
			}
		}
	}
	
	/**
	 * 通过递归得到某一路径下所有的文件
	 */
	public static void getFiles(List<File> fileList,String filePath) {
		File root = new File(filePath);
		File[] files = root.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				getFiles(fileList,file.getAbsolutePath());
				System.out.println("显示" + filePath + "下所有子目录及其文件"
						+ file.getAbsolutePath());
			} else {
				fileList.add(file);
				System.out.println("显示" + filePath + "下所有子目录"
						+ file.getAbsolutePath());
			}
		}
	}
	
	/**
	 * 记录数据
	 * @param fileName
	 * @param data
	 */
	public static void write(String fileName,String data){
		//writeByBuffer(fileName,data,CharSet.UTF_8,true);
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
				FileUtils.writeStringToFile(file, CommonConstant.LINE_SEPARATOR,Charset.forName(charset),true);
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
				out.write(CommonConstant.LINE_SEPARATOR_BYTES);
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
     * 递归删除目录下的所有文件及子目录下所有文件
     * @param dir 将要删除的文件目录
     * @return boolean Returns "true" if all deletions were successful.
     *                 If a deletion fails, the method stops attempting to
     *                 delete and returns "false".
     */
    public static boolean deleteDir(File dir) {
    	System.out.println("删除文件或文件夹"+dir.getAbsolutePath());
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }
	
	public static void main(String[] args) {
		FileUtil fu = new FileUtil();
		fu.getFiles("D:\\data");
		List<File> fileList = fu.getFileList();
		for (File file : fileList) {
			System.out.println(file.getAbsolutePath());
		}
		File f = new File("D:\\dat\\马可波罗\\makepolo-45800-20140818.txt");
		try {
			createFile(f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
