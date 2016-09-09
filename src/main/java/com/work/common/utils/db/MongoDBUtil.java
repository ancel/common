package com.work.common.utils.db;

import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

public class MongoDBUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(MongoDBUtil.class);
	public static final String LOCAL_MONGO_IP= "127.0.0.1";
	public static final int lOCAL_MONGO_PORT= 27017;

	/**
	 * 获得本机mongo的DBCollection对象
	 * 
	 * @param dbName
	 * @param colName
	 * @return
	 */
	public static DBCollection getDBCollection(String dbName, String colName) {
		return getDBCollection(LOCAL_MONGO_IP,lOCAL_MONGO_PORT,dbName,colName);
	}
	
	/**
	 * 获得DBCollection对象
	 * 
	 * @param dbName
	 * @param colName
	 * @return
	 */
	public static DBCollection getDBCollection(String ip,int port,String dbName, String colName) {
		MongoClient	mongoClient = null;
		try {
			mongoClient = new MongoClient(ip,port);
		} catch (UnknownHostException e) {
			LOGGER.error("[instance mongoclient error!]",e);
			System.exit(1);
		}
		DB db = mongoClient.getDB(dbName);
		return db.getCollection(colName);
	}

}