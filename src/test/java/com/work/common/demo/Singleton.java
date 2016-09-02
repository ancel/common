package com.work.common.demo;

/**
 * 
 * 创建人：wanghaibo <br>
 * 创建时间：2015-3-18 下午3:31:39 <br>
 * 功能描述： <br>
 * 单例模式,利用内部类延迟初始化
 * 并且不影响性能
 * Initialization on Demand Holder
 * 版本： <br>
 * ====================================== <br>
 * 修改记录 <br>
 * ====================================== <br>
 * 序号 姓名 日期 版本 简单描述 <br>
 * 
 */  

public class Singleton {
	
	private static class SingletonHolder{
		public final static Singleton instance = new Singleton();
	}
	
	public static Singleton getInstance(){
		return SingletonHolder.instance;
	}
}
