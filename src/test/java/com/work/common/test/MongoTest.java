package com.work.common.test;

import java.net.UnknownHostException;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.work.common.utils.db.MongoDBUtil;

public class MongoTest {
	@SuppressWarnings("unchecked")
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
	
	
	@Test
	public void testDrop(){
		String ip = "172.18.19.121";
		int port = 27017;
//		String dbName = "data-china";
		String dbName = "pool";
		MongoClient	mongoClient = null;
		try {
			mongoClient = new MongoClient(ip,port);
		} catch (UnknownHostException e) {
			System.exit(1);
		}
		DB db = mongoClient.getDB(dbName);
		DBObject dbObj;
		
		Set<String> collNames = db.getCollectionNames();
		System.out.println(collNames.size());
		int count = 0;
		for (String string : collNames) {
			boolean flag = false;
			if(string.contains("life-16")
					||string.contains("life-15")
					||string.contains("yp-16")
					||string.contains("yp-15")
					||string.contains("bkwd-15")
					||string.contains("bkwd-16")){
				flag = true;
			}
			if(string.contains("-16")&&!string.contains("-16-")&&!string.endsWith("-15")){
//				System.out.println(string);
				if(string.contains("-life-")&&string.lastIndexOf("-life-")<string.lastIndexOf("-16")){
					flag = true;
				}else if(string.contains("-yp-")&&string.lastIndexOf("-yp-")<string.lastIndexOf("-16")){
					flag = true;
				}else if(string.contains("-bkwd-")&&string.lastIndexOf("-bkwd-")<string.lastIndexOf("-16")){
					flag = true;
				}
			}
			if(string.contains("-15")&&!string.contains("-15-")&&!string.endsWith("-15")){
//				System.out.println(string);
				if(string.contains("-life-")&&string.lastIndexOf("-life-")<string.lastIndexOf("-15")){
					flag = true;
				}else if(string.contains("-yp-")&&string.lastIndexOf("-yp-")<string.lastIndexOf("-15")){
					flag = true;
				}else if(string.contains("-bkwd-")&&string.lastIndexOf("-bkwd-")<string.lastIndexOf("-15")){
					flag = true;
				}
			}
			if(flag){
				System.out.println(string);
//				db.getCollection(string).drop();
//				dbObj = new BasicDBObject();
//				dbObj.put("compact", string);
//				db.command(dbObj);
				count++;
			}
		}
		System.out.println(count);
	}
}
