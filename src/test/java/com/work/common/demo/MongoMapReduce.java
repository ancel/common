package com.work.common.demo;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MapReduceCommand;
import com.mongodb.MapReduceOutput;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;

/**
 * mapReduce其实是一种编程模型，用在分布式计算中，其中有一个“map”函数，一个”reduce“函数。
 *　① map：
 *　这个称为映射函数，里面会调用emit(key,value)，集合会按照你指定的key进行映射分组。
 *　② reduce：
 *　这个称为简化函数，会对map分组后的数据进行分组简化，注意：在reduce(key,value)中的key就是
 * emit中的key，vlaue为emit分组后的emit(value)的集合，这里也就是很多{"count":1}的数组。
 * @author admin
 *
 */
public class MongoMapReduce {
	public static void main(String[] args) {
		Mongo mongo;
		try {
			mongo = new MongoClient("172.18.19.121", 27017);
			DB db = mongo.getDB("tempdata");

			DBCollection books = db.getCollection("books");

			BasicDBObject book = new BasicDBObject();
			book.put("name", "Understanding JAVA");
			book.put("pages", 100);
			books.insert(book);

			book = new BasicDBObject();
			book.put("name", "Understanding JSON");
			book.put("pages", 200);
			books.insert(book);

			book = new BasicDBObject();
			book.put("name", "Understanding XML");
			book.put("pages", 300);
			books.insert(book);

			book = new BasicDBObject();
			book.put("name", "Understanding Web Services");
			book.put("pages", 400);
			books.insert(book);

			book = new BasicDBObject();
			book.put("name", "Understanding Axis2");
			book.put("pages", "150");
			books.insert(book);

			//查找超过250页的图书
			String map = "function() { " 
					+ "var category; "
					+ "if ( this.pages >= 250 ) " 
					+ "category = 'Big Books'; "
					+ "else " 
					+ "category = 'Small Books'; "
					+ "emit(category, {name: this.name});}";
			

			//统计图书页数
			String reduce = "function(key, values) { " 
					+ "var sum = 0; "
					+ "values.forEach(function(doc) { " 
					+ "sum += 1; " 
					+ "}); "
					+ "return {books: sum};} ";

			MapReduceCommand cmd = new MapReduceCommand(books, map, reduce,
					null, MapReduceCommand.OutputType.INLINE, null);

			MapReduceOutput out = books.mapReduce(cmd);

			for (DBObject o : out.results()) {
				System.out.println(o.toString());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}