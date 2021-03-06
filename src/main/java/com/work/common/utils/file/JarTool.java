package com.work.common.utils.file;
import java.io.File;

/**
 * 获取打包后jar的路径信息 
 * @author：wanghaibo 
 * @creattime：2016年9月18日 下午3:03:02 
 * 
 */  
public class JarTool {
	//获取jar绝对路径
	public static String getJarPath(){
		File file = getFile();
		if(file==null)return null;
		 return file.getAbsolutePath();
	}
	//获取jar目录
	public static String getJarDir() {
		File file = getFile();
		if(file==null)return null;
	     return getFile().getParent();
	}
	//获取jar包名
	public static String getJarName() {
		File file = getFile();
		if(file==null)return null;
		return getFile().getName();
	}

	private static File getFile() {
		//关键是这行...
		String path = JarTool.class.getProtectionDomain().getCodeSource()
				.getLocation().getFile();
        try{
            path = java.net.URLDecoder.decode(path, "UTF-8");//转换处理中文及空格
        }catch (java.io.UnsupportedEncodingException e){
            return null;
        }
		return new File(path);
	}
	
	public static void main(String[] args) {
		System.out.println(JarTool.getJarDir());
		System.out.println(JarTool.getJarName());
		System.out.println(JarTool.getJarPath());
	}
}
