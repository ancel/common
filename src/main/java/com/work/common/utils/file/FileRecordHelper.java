package com.work.common.utils.file;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.work.common.constant.CharSet;



/**
 * 写数据,每n行写一次
 * 
 * 
 * @author：wanghaibo 
 * @creattime：2016年9月18日 下午5:13:39 
 * 
 */  
public class FileRecordHelper {
	private Map<String, List<String>> fileMap;
	private Map<String, Lock> fileLocks;
	
	private int flushNum;
	
	public FileRecordHelper(int flushNum) {
		this.flushNum = flushNum;
		this.fileMap = new HashMap<>();
		this.fileLocks = new HashMap<String, Lock>();
	}
	public FileRecordHelper() {
		this(50);
	}
	
	private static FileRecordHelper instance;
	public static FileRecordHelper getInstance(){
		if (null==instance) {
			synchronized (FileRecordHelper.class) {
				if(null==instance){
					instance = new FileRecordHelper();
				}
			}
		}
		return instance;
	}
	private Lock getLock(File file){
		String filename = file.getAbsolutePath();
		return getLock(filename);
	}
	
	private Lock getLock(String filename){
		if (!fileLocks.containsKey(filename)) {
			synchronized (this) {
				if(!fileLocks.containsKey(filename)){
					fileLocks.put(filename, new ReentrantLock());
				}
			}
		}
		return fileLocks.get(filename);
	}
	public List<String> getRecord(File file){
		String filename = file.getAbsolutePath();
		return getRecord(filename);
	}
	public List<String> getRecord(String filename){
		if(!fileMap.containsKey(filename)){
			fileMap.put(filename, new ArrayList<String>());
		}
		return fileMap.get(filename);
	}
	
	public void write(File file,String line) throws IOException{
		String filename = file.getAbsolutePath();
		getLock(file).lock();
		try {
			List<String> record = getRecord(file);
			record.add(line);
			if(record.size()%flushNum==0){
				FileUtil.write(filename, record,  CharSet.UTF_8, true, true);
				record.clear();
			}
		} finally {
			getLock(file).unlock();
		}
	}

	/**
	 * 写数据，若是数据量满足flushNum则刷入文件
	 * @param errorFileName
	 * @param line
	 * @throws IOException 
	 */
	@Deprecated
	public void write(String filename,String line){
		getLock(filename).lock();
		try {
			List<String> record = getRecord(filename);
			record.add(line);
			if(record.size()%flushNum==0){
				try {
					FileUtil.write(filename, record,  CharSet.UTF_8, true, true);
					record.clear();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		} finally {
			getLock(filename).unlock();
		}
	}
	
	/**
	 * 清空缓存数据
	 * @throws IOException 
	 */
	public synchronized void flush(){
		Set<String> keys = fileMap.keySet();
		for (String key : keys) {
			getLock(key).lock();
			List<String> record = getRecord(key);
			try {
				FileUtil.write(key, record,  CharSet.UTF_8, true, true);
				record.clear();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}finally{
				getLock(key).unlock();
			}
		}
	}
	
	public synchronized void flush(File file) throws IOException{
		String filename = file.getAbsolutePath();
		
		getLock(file).lock();
		List<String> record = getRecord(file);
		try {
			FileUtil.write(filename, record,  CharSet.UTF_8, true, true);
			record.clear();
		} finally{
			getLock(file).unlock();
		}
	}

	public int getFlushNum() {
		return flushNum;
	}

	public void setFlushNum(int flushNum) {
		this.flushNum = flushNum;
	}
	
}
