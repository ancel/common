package com.work.common.demo;

import java.util.List;
import java.util.regex.Pattern;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.work.common.App;
import com.work.common.utils.db.MongoDBUtil;

public class MongoDemo {
	public static DBCollection coll = MongoDBUtil.getDBCollection("172.18.19.121", 27017, "tempdata", "mm");
	/**
	 * 把一个类对象存到mongodb之后使用setObjectClass将其转换为原先的类
	 * @return
	 */
	public App getObj(){
		App app = new App();
		coll.setObjectClass(App.class);
		app = (App)coll.findOne();
		return app;
	}
	
	
	/**
	 * 获取collection索引
	 * @return
	 */
	public List<DBObject> getIndexs(){
		List<DBObject> indexs = coll.getIndexInfo();
		return indexs;
	}
	
	/**
	 * 获取一个collection索引
	 * @return
	 */
	public void demo(){
		//collection数量
		coll.find().count();
		//查询部分数据块
		coll.find().skip(0).limit(10);
		
		//按age升序排列
		coll.find().sort(new BasicDBObject("age", 1));
		
		//按条件查询,$gt大于、$gte大于等于、$it小于、$ite小于等于、$in包含、$nin不包含、$not反匹配；
		DBObject condition = new BasicDBObject();
		condition.put("age", new BasicDBObject("$gt",50).append("$it", 100));//查找50<age<100
		coll.find(condition);
		
		//正则表达式查询
		Pattern pattern = Pattern.compile("\\d*");
		condition = new BasicDBObject("age", pattern);
		coll.find(condition);
		
		//查询存在age字段的文档
		condition.put("age", new BasicDBObject("$exists",true));
		coll.find(condition);
		
		//设置cursor不超时
		DBCursor cursor = coll.find(condition);
		cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);
		
		//创建索引
		coll.createIndex(new BasicDBObject("age",1));
	}
}
