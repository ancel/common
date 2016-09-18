package com.work.common.utils.db;

import javax.sql.DataSource;

import org.apache.commons.dbutils.QueryRunner;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.work.common.utils.LRUCache;

/**
 * 基类dao
 * @author：wanghaibo 
 * @creattime：2015-10-27 上午11:52:22 
 * 
 */  

public abstract class AbstractDAO {
	LRUCache<String, DataSource> sourceCache = new LRUCache<>(5);
//	static {
//		System.setProperty("com.mchange.v2.c3p0.cfg.xml","conf/c3p0-config.xml");
//	}
	protected QueryRunner runner = new QueryRunner(getDataSource());

	private DataSource getDataSource() {
		if (null == sourceCache.get(getConfigName())) {
			sourceCache.put(getConfigName(), new ComboPooledDataSource(getConfigName()));
		}
		return sourceCache.get(getConfigName());
	}

	public abstract String getConfigName();
}