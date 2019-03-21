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
	private static final LRUCache<String, DataSource> DATA_SOURCE_CACHE = new LRUCache<>(5);
//	static {
//		System.setProperty("com.mchange.v2.c3p0.cfg.xml",
//				"conf/c3p0-config.xml");
//	}
	protected QueryRunner runner = new QueryRunner(getDataSource());

	private DataSource getDataSource() {
		synchronized (DATA_SOURCE_CACHE) {
			if (!DATA_SOURCE_CACHE.containsKey(getConfigName())) {
				DATA_SOURCE_CACHE.put(getConfigName(), new ComboPooledDataSource(getConfigName()));
			}
		}
		return DATA_SOURCE_CACHE.get(getConfigName());
	}

	public abstract String getConfigName();
}