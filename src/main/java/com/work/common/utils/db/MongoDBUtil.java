package com.work.common.utils.db;

import java.net.UnknownHostException;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

public class MongoDBUtil {
	public static final String MONGO_IP= "127.0.0.1";
	public static final int MONGO_PORT= 27017;

	/**
	 * 获得本机mongo的DBCollection对象
	 * 
	 * @param dbName
	 * @param colName
	 * @return
	 */
	public static DBCollection getDBCollection(String dbName, String colName) {
		return getDBCollection(MONGO_IP,MONGO_PORT,dbName,colName);
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
			e.printStackTrace();
			System.exit(1);
		}
		DB db = mongoClient.getDB(dbName);
		return db.getCollection(colName);
	}

}