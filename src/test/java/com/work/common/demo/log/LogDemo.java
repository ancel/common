package com.work.common.demo.log;

import org.apache.log4j.PropertyConfigurator;

public class LogDemo {
	static{
		PropertyConfigurator.configure("conf/log4j.properties");
	}
}
