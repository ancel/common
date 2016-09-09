package com.work.common.utils;

import java.io.File;
import java.io.FileFilter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * 自定义类加载器，加载程序以外的类
 * @author：wanghaibo 
 * @creattime：2016年9月9日 下午4:58:49 
 * 
 */  
public class ResourceClassLoader extends URLClassLoader {
	
	private String name;

	public static ResourceClassLoader getInstance() {
		return SingletonHolder.INSTANCE;
	}

	private static class SingletonHolder {
		private static final ResourceClassLoader INSTANCE = new ResourceClassLoader();
	}

	public ResourceClassLoader() {
		super(new URL[0]);
	}
	public ResourceClassLoader(String name) {
		super(new URL[0]);
		this.setName(name);
	}
	public ResourceClassLoader(String name,ClassLoader parent) {
		super(new URL[0],parent);
		this.setName(name);
	}
	public ResourceClassLoader newInstance(){
		return new ResourceClassLoader();
	}
	
	/**
	 * 添加一些class到该classloader环境下
	 * 
	 * @param filename
	 *            文件or文件夹
	 * @param filenameFilter
	 * @throws MalformedURLException
	 */
	public void addURL(String filename, FileFilter fileFilter)
			throws MalformedURLException {
		File targetFile = new File(filename);
		if (!targetFile.exists()) {
			System.out.println(filename + " is not exist");
			return;
		}
		File[] jarFiles;
		if (targetFile.isDirectory()) {
			jarFiles = targetFile.listFiles(fileFilter);
		} else {
			if (fileFilter.accept(targetFile)) {
				jarFiles = new File[] { targetFile };
			} else {
				return;
			}
		}
		for (File file : jarFiles) {
			URL url = file.toURI().toURL();
			super.addURL(url);
		}
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}