package com.work.common.demo;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.ObjectArrays;
import com.google.common.collect.Sets;



/**
 * 
 * 创建人：wanghaibo <br>
 * 创建时间：2015-6-1 上午10:20:10 <br>
 * 功能描述： <br>
 * google工具demo
 * 版本： <br>
 * ====================================== <br>
 * 修改记录 <br>
 * ====================================== <br>
 * 序号 姓名 日期 版本 简单描述 <br>
 * 
 */  

public class GuavaDemo {
	public static void main(String[] args) {
		GuavaDemo gd = new GuavaDemo();
		gd.collect();
		
	}
	
	@SuppressWarnings("unused")
	public void collect(){
		Map<String, Map<String, String>> map = Maps.newHashMap();

		List<List<Map<String, String>>> list = Lists.newArrayList();

		//1,简化集合的创建
		List<Person> personList= Lists.newLinkedList();
		Set<Person> personSet= Sets.newHashSet();
		Map<String,Person> personMap= Maps.newHashMap();
		Integer[] intArrays= ObjectArrays.newArray(Integer.class,10);
		
	}
	
	class Person{
		private String name;
		private String[] friends;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String[] getFriends() {
			return friends;
		}
		public void setFriends(String[] friends) {
			this.friends = friends;
		}
		
	}
	
}
