package com.work.common.demo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class JsoupDemo {
	public void getAbsUrl(String html){
		Document doc = Jsoup.parse(html);
		Element element = doc.select("a").first();
		//路径属性值
		String refHref = element.attr("href");
		System.out.println(refHref);
		//绝对路径
		String absHref = element.attr("abs:href");
		System.out.println(absHref);
	}
}
