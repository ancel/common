package com.work.common.demo;

import com.work.common.utils.db.BDB;

public class BDBDemo {
	
	public static void main(String[] args) {
		testBDB();
	}
	
	public static void testBDB(){
		String fileName = "C:\\Users\\Li Yujie\\Desktop\\bdb";
		String dbName = "helloworld";
		BDB bdb = new BDB(fileName, dbName);
		bdb.openDatabase();
		bdb.writeToDatabase("asd", "asdf", false);
		System.out.println(bdb.readFromDatabase("asd"));
		bdb.closeDatabase();
	}
}
