package com.work.common.demo.bdbQueue;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.work.common.utils.EncryptionUtil;


public class CrawlUrl implements Serializable{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CrawlUrl.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = -139316048829221946L;
	private String url;
	private String pageId;
	private int visitNum = 0;
	
	public CrawlUrl() {
	}
	public CrawlUrl(String url, String pageId) {
		this.url = url;
		this.pageId = pageId;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getPageId() {
		return pageId;
	}
	public void setPageId(String pageId) {
		this.pageId = pageId;
	}
	public int getVisitNum() {
		return visitNum;
	}
	public void setVisitNum(int visitNum) {
		this.visitNum = visitNum;
	}
	@Override
	public String toString() {
		return "CrawlUrl [url=" + url + ", pageId=" + pageId + ", visitNum="
				+ visitNum + "]";
	}
	
	public String getKey(){
		try {
			return EncryptionUtil.md5(url);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOGGER.error("=================url key生成失败", e);
			return "";
		}
	}
}
