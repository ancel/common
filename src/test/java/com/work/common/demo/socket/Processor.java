package com.work.common.demo.socket;

public interface Processor<I,O>{
	public O process(I input);
}
