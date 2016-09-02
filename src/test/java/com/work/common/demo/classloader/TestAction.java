package com.work.common.demo.classloader;


public class TestAction extends AbstractAction{
	public String action() {
		System.out.println("I am working ! ");
		return "this ActionTest class";
	}
}
