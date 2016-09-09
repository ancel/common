package com.work.common.demo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;

import net.sf.json.JSONObject;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.work.common.utils.db.MongoDBUtil;

/**
 * 文件读写
 */
public class FileHandle {
	//项目路径
	String proFilePath = System.getProperty("user.dir");
	public void writeFile() {
		File file = new File("D:\\1688.txt");
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(file)));
			out.write("asdf");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public void readFile() {
		File file = new File("D:\\1688.txt");
		BufferedReader in;
		try {
			in = new BufferedReader(new InputStreamReader(new FileInputStream(
					file)));
			String line = null;
			int i = 0;
			while ((line = in.readLine()) != null) {
				System.out.println(line);
				i++;
			}
			System.out.println(i);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void readFileToMongo() {
		File file = new File("D:\\yinhang.txt");
		DBCollection coll = MongoDBUtil
				.getDBCollection("tempdata", "jh_online");
		BufferedReader in;
		try {
			in = new BufferedReader(new InputStreamReader(new FileInputStream(
					file)));
			String line = null;
			JSONObject json;
			DBObject obj;
			while ((line = in.readLine()) != null) {
				json = JSONObject.fromObject(line);
				obj = new BasicDBObject();
				obj.put("name", json.get("name"));
				obj.put("address", json.get("address"));
				obj.put("city", json.get("city"));
				System.out.println(obj.get("name") + ":"
						+ obj.get("address").toString());
				coll.insert(obj);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@SuppressWarnings("resource")
	public void readRandomAccessFile() {
		try {
			// RandomAccessFile raf = new
			// RandomAccessFile("D:\\FinalData-2014-08-14123_NEW.NEW", "rw");
			RandomAccessFile raf = new RandomAccessFile("D:\\test", "rw");
			for (int i = 0; i < 10; i++) {
				raf.writeDouble(i*1.414);
			}
			raf.close();
			
			raf = new RandomAccessFile("d:\\test", "rw");
			raf.seek(5*8);
			raf.writeDouble(47.0001);
			raf.close();
			
			raf = new RandomAccessFile("d:\\test", "rw");
			for (int i = 0; i < 10; i++) {
				System.out.println("value "+i+":"+raf.readDouble());
			}
			
		} catch (IOException e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		FileHandle fh = new FileHandle();
		fh.readFile();
		fh.writeFile();
		fh.readFileToMongo();
		fh.readRandomAccessFile();
		
	}

}
