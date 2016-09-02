package com.work.common.demo.bdbQueue;

import java.io.File;

import com.sleepycat.bind.serial.StoredClassCatalog;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;

public abstract class AbstractFrontier{
	private Environment env;
	private static final String CLASS_CATALOG = "java_class_catalog";
	protected StoredClassCatalog javaCatalog;
	protected Database catalogDatabase;
	protected Database database;
	private String homeDirectory;
	public AbstractFrontier(String homeDirectory) {
		this.homeDirectory = homeDirectory;
		//打开env
		System.out.println("open enviroment in: "+homeDirectory);
		EnvironmentConfig envConfig = new EnvironmentConfig();
		envConfig.setTransactional(true);
		envConfig.setAllowCreate(true);
		File dir = new File(homeDirectory);
		if(!dir.exists()){
			dir.mkdirs();
		}
		env = new Environment(dir, envConfig);
		//设置databaseconfig
		DatabaseConfig dbConfig = new DatabaseConfig();
		dbConfig.setTransactional(true);
		dbConfig.setAllowCreate(true);
		
		catalogDatabase = env.openDatabase(null, CLASS_CATALOG, dbConfig);
		javaCatalog = new StoredClassCatalog(catalogDatabase);
		
		DatabaseConfig dbConfig2 = new DatabaseConfig();
		dbConfig2.setTransactional(true);
		dbConfig2.setAllowCreate(true);
		
		database = env.openDatabase(null, "URL", dbConfig2);
	}
	
	public void close(){
		database.close();
		javaCatalog.close();
		env.close();
	}
	
	
	
	public String getHomeDirectory() {
		return homeDirectory;
	}

	public void setHomeDirectory(String homeDirectory) {
		this.homeDirectory = homeDirectory;
	}

	protected abstract void put(Object key,Object value);
	protected abstract Object get(Object key);
	protected abstract Object delete(Object key);
	
		
}
