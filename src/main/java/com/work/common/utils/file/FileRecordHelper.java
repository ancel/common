package com.work.common.utils.file;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * 写数据,每n行写一次
 * 
 * 
 * @author：wanghaibo 
 * @creattime：2016年9月18日 下午5:13:39 
 * 
 */  
public class FileRecordHelper {
	private static final Logger LOGGER = LoggerFactory.getLogger(FileRecordHelper.class);
	public static Map<String, List<String>> map = new HashMap<>();
	private int flushNum = 50;
	
	public FileRecordHelper() {
	}

	public FileRecordHelper(int flushNum) {
		this.flushNum = flushNum;
	}

	/**
	 * 写数据，若是数据量满足flushNum则刷入文件
	 * @param errorFileName
	 * @param line
	 * @throws IOException 
	 */
	public synchronized void write(String fileName,String line){
		List<String> record = map.get(fileName);
		int lineNum;
		if(record==null){
			record = new ArrayList<>();
			record.add(line);
		}else{
			record.add(line);
			lineNum = record.size();
			if(lineNum%flushNum==0){
				StringBuffer sb = new StringBuffer();
				for (String string : record) {
						sb.append(string).append(FileUtil.LINE_SEPARATOR);
				}
				try {
					FileUtil.write(fileName, sb.toString(), "UTF-8", false);
				} catch (IOException e) {
					LOGGER.error("文件写入失败:"+fileName,e);
					System.exit(1);
				}
				record.clear();
			}
			
		}
		map.put(fileName, record);
	}
	
	/**
	 * 清空缓存数据
	 * @throws IOException 
	 */
	public synchronized void flush(){
		Set<String> keys = map.keySet();
		for (String key : keys) {
			StringBuffer sb = new StringBuffer();
			List<String> record = map.get(key);
			for (String string : record) {
				sb.append(string).append(FileUtil.LINE_SEPARATOR);
			}
			try {
				FileUtil.write(key, sb.toString(), "UTF-8", false);
			} catch (IOException e) {
				LOGGER.error("文件写入失败:"+key,e);
				System.exit(1);
			}
			record.clear();
			map.put(key, record);
		}
	}

	public int getFlushNum() {
		return flushNum;
	}

	public void setFlushNum(int flushNum) {
		this.flushNum = flushNum;
	}
	
}
