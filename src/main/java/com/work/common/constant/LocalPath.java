package com.work.common.constant;

import java.net.URL;

public class LocalPath {
	//resources路径
	public static final URL RES_URL = LocalPath.class.getResource("/");//file:/D:/workspace/workspace3_7/demo/target/classes/
	public static final String RES_PATH = RES_URL.getPath().substring(1);//D:/workspace/workspace3_7/demo/target/classes/
}
