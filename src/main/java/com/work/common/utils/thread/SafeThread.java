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
			//线程发生未知异常，需直接退出，避免executservice线程池中线程发生异常后，其他线程正常运行
			System.exit(1);
		}finally{
			task.after();
		}
	}
}
