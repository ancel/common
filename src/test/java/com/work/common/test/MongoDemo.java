package com.work.common.test;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class MongoDemo {
	
	/**
	 * 获得DBCollection对象
	 * @param ip
	 * @param port
	 * @param dbName
	 * @param colName
	 * @return
	 * @throws UnknownHostException
	 */
	public DBCollection getDBCollection(String ip,int port,String dbName, String colName) throws UnknownHostException {
		MongoClient	mongoClient = null;
		mongoClient = new MongoClient(ip,port);
		DB db = mongoClient.getDB(dbName);
		return db.getCollection(colName);
	}
	
	public static void main(String[] args) throws UnknownHostException {
		MongoDemo md = new MongoDemo();
		DBCollection coll = md.getDBCollection("172.18.19.121", 27017, "xxx", "xxx");
		
		// insert
		DBObject dbObj = new BasicDBObject();
		dbObj.put("aa", "aa");
		coll.insert(dbObj);
		
		// insert_many
		List<DBObject> dbObjs = new ArrayList<DBObject>();
		dbObjs.add(dbObj);
		coll.insert(dbObjs);
		
		
		// update( upsert = true )
		DBObject condition = new BasicDBObject();
		condition.put("id", "123");
		DBObject modification = new BasicDBObject();
		modification.put("name", "minglun");
		
		/**
 		 * @param q      the selection criteria for the update
	     * @param o      the modifications to apply
	     * @param upsert when true, inserts a document if no document matches the update query criteria
	     * @param multi  when true, updates all documents in the collection that match the update query criteria, otherwise only updates one
	     * @return the result of the operation
	     */
		coll.update(condition, modification, true, true);
	}
}
