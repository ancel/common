package com.work.common.demo.bdbQueue;

import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

import com.sleepycat.bind.EntryBinding;
import com.sleepycat.bind.serial.SerialBinding;
import com.sleepycat.collections.StoredMap;

public class BDBFrontier extends AbstractFrontier implements Frontier{
	private StoredMap<String, CrawlUrl> pendingUrlsDB = null;
	public BDBFrontier(String homeDirectory) {
		super(homeDirectory);
		EntryBinding<String> keyBinding = new SerialBinding<>(javaCatalog, String.class);
		EntryBinding<CrawlUrl> valueBinding = new SerialBinding<>(javaCatalog, CrawlUrl.class);
		pendingUrlsDB = new StoredMap<>(database, keyBinding, valueBinding, true);
	}

	@Override
	public synchronized CrawlUrl getNext() {
		// TODO Auto-generated method stub
		CrawlUrl result = null;
		if(!pendingUrlsDB.isEmpty()){
			Entry<String, CrawlUrl> entry = pendingUrlsDB.entrySet().iterator().next();
			result = entry.getValue();
			delete(entry.getKey());
		}
		return result;
	}

	@Override
	public boolean putUrl(CrawlUrl crawlUrl) {
		if(StringUtils.isNotBlank(crawlUrl.getKey())){
			put(crawlUrl.getKey(),crawlUrl);
		}
		return true;
	}

	@Override
	protected synchronized void put(Object key, Object value) {
		pendingUrlsDB.put(key.toString(), (CrawlUrl)value);		
	}

	@Override
	protected Object get(Object key) {
		// TODO Auto-generated method stub
		return pendingUrlsDB.get(key);
	}

	@Override
	public synchronized Object delete(Object key) {
		return pendingUrlsDB.remove(key);
	}
	
	public synchronized  int getSize(){
		return pendingUrlsDB.size();
	}

	public static void main(String[] args) {
		BDBFrontier bdbFrontier = new BDBFrontier("e:\\bdb");
		String pageId = "asdf";
		CrawlUrl crawlUrl;
		long start = System.currentTimeMillis();
		for (int i = 0; i < 10; i++) {
			crawlUrl = new CrawlUrl(String.valueOf(i),pageId);
			bdbFrontier.putUrl(crawlUrl);
		}
		
		for (int i = 0; i < 10; i++) {
			System.out.println(bdbFrontier.getNext());
		}
		System.out.println(System.currentTimeMillis()-start);
		bdbFrontier.close();
	}
}
