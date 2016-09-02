package com.work.common.demo.file;

import java.util.EventListener;

public interface FileListener extends EventListener{
	public void handleFile(FileEvent event);
}
