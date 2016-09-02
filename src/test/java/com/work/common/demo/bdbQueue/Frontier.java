package com.work.common.demo.bdbQueue;


public interface Frontier {
	public CrawlUrl getNext();
	public boolean putUrl(CrawlUrl crawlUrl);
//	public boolean visited(CrawlUrl crawlUrl);
}
