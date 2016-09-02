package com.work.common.utils.file;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.work.common.constant.CharSet;
import com.work.common.constant.CommonConstant;

/**
 * 写数据,每n行写一次
 * @author admin
 *
 */
public class FileRecordHelper {
	public static Map<String, List<String>> map = new HashMap<>();
	public static int flushNum = 50;
	
	/**
	 * 写数据，若是数据量满足flushNum则刷入文件
	 * @param errorFileName
	 * @param line
	 */
	public synchronized static void write(String fileName,String line){
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
						sb.append(string).append(CommonConstant.LINE_SEPARATOR);
				}
				FileUtil.write(fileName, sb.toString(), CharSet.UTF_8, false);
				record.clear();
			}
			
		}
		map.put(fileName, record);
	}
	
	/**
	 * 清空缓存数据
	 */
	public synchronized static void flush(){
		Set<String> keys = map.keySet();
		for (String key : keys) {
			StringBuffer sb = new StringBuffer();
			List<String> record = map.get(key);
			for (String string : record) {
				sb.append(string).append(CommonConstant.LINE_SEPARATOR);
			}
			FileUtil.write(key, sb.toString(), CharSet.UTF_8, false);
			record.clear();
			map.put(key, record);
		}
	}
}
