package com.work.common.test;

import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.junit.Test;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.work.common.utils.db.MongoDBUtil;

public class MongoTest {
	@Test
	public void testQuery(){
		String ip = "172.18.19.121";
		int port = 27017;
		String dbName = "quality";
		String colName = "inputs";
		DBCollection coll = MongoDBUtil.getDBCollection(ip, port, dbName, colName);
		BasicDBObject query = new BasicDBObject(); 
	    query.put("time", "20160419"); 
		DBCursor cursor = coll.find(query);
		DBObject dbObj;
		int shopHeadNum = 0;
		while(cursor.hasNext()){
			dbObj = cursor.next();
			List<DBObject> inputs = (List<DBObject>)dbObj.get("inputs"); 
			for (DBObject dbObject : inputs) {
				if("shop".equals(dbObject.get("phone_type"))&&"middle".equals(dbObject.get("frequency_type"))){
					shopHeadNum++;
				}
			}
		}
		System.out.println(shopHeadNum);
	}
}
