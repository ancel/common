package com.work.common.utils.db;

import java.beans.PropertyVetoException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public enum DataSourceManager {
	INSTANCE;
	private Map<String, DataSource> dataSourceMap;
	private DataSource defaultDataSource;
	private DataSourceManager(){
		dataSourceMap = new HashMap<>();
	}
	/**
	 * 获取c3p0数据源
	 * @param db
	 * @return
	 */
	public DataSource getDataSource(DB db) {
		ComboPooledDataSource cpds = new ComboPooledDataSource();
		try {
			cpds.setDriverClass(db.getDriver());
			cpds.setJdbcUrl(db.getUrl());
			cpds.setUser(db.getUsername());
			cpds.setPassword(db.getPassword());
		} catch (PropertyVetoException e) {
			throw new RuntimeException(e);
		}
		return cpds;
	}
	
	/**
	 * 获取默认的c3p0数据源
	 * @return
	 */
	public DataSource getDataSource() {
		if(null==defaultDataSource){
			synchronized (this) {
				if(null==defaultDataSource){
					defaultDataSource = new ComboPooledDataSource();
				}
			}
		}
		return defaultDataSource;
	}
	
	/**
	 * 获取配置为configName的c3p0数据源
	 * @return
	 */
	public DataSource getDataSource(String configName) {
		if(null==dataSourceMap.get(configName)){
			synchronized(this){
				if(null==dataSourceMap.get(configName)){
					dataSourceMap.put(configName, new ComboPooledDataSource(configName));
				}
			}
		}
		return dataSourceMap.get(configName);
	}
	
	public static void main(String[] args) throws SQLException {
		//设置c3p0文件的位置
//		System.setProperty("com.mchange.v2.c3p0.cfg.xml", PathUtil.getJarDir()+File.separator+"c3p0-config.xml");
		QueryRunner runner;
		runner = new QueryRunner(DataSourceManager.INSTANCE.getDataSource());
//		runner = new QueryRunner(DataSourceManager.INSTANCE.getDataSource("cleanDB"));
		String sql = "select prov_name from dict_province_v2 where prov_id=1";
		System.out.println(runner.query(sql, new ScalarHandler<String>(1)));;
	}
}