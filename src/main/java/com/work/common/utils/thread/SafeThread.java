package com.work.common.utils.thread;



/**
 * 安全线程，防止异常造成程序崩溃
 * @author：wanghaibo 
 * @creattime：2016年9月18日 下午3:19:05 
 * 
 */  
public class SafeThread implements Runnable{
	Task task;

	
	public SafeThread(Task task) {
		this.task = task;
	}


	@Override
	public void run() {
		try {
			task.before();
			task.execute();
		} catch (Exception e) {
			task.exception(e);
		}finally{
			task.after();
		}
	}
}
